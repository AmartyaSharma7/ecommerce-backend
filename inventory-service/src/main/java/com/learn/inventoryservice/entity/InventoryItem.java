package com.learn.inventoryservice.entity;

// You will need imports from jakarta.persistence.*, java.math.BigDecimal and
// java.time.Instant. Let IntelliJ add them: type the class name, press Alt+Enter.

/**
 * ============================================================
 * STEP 2 of 9 — ENTITY   (build this right after the migration)
 * ============================================================
 *
 * WHAT THIS CLASS IS:
 *   One row of inventory_items: how many units of a product are free to sell
 *   (availableQty) and how many are held for orders not yet paid (reservedQty).
 *
 * WHY IT EXISTS / THE INTUITION:
 *   Same job as Product: let Hibernate write the SQL. Everything you learned in
 *   Product.java applies. The ONE new concept is the @Version field below, and
 *   it is the reason this service exists as a learning step.
 *
 * ============================================================
 * THE NEW CONCEPT — @Version AND OPTIMISTIC LOCKING
 * ============================================================
 *
 * THE PROBLEM (the "lost update"):
 *   Product 42 has availableQty = 1. Two customers press "Buy" at the same
 *   moment. Two HTTP requests, two threads, two transactions:
 *
 *     T1: SELECT ... -> availableQty = 1      T2: SELECT ... -> availableQty = 1
 *     T1: 1 >= 1 ? yes, reserve               T2: 1 >= 1 ? yes, reserve
 *     T1: UPDATE available=0, reserved=1      T2: UPDATE available=0, reserved=1
 *     T1: COMMIT -> 200 OK                     T2: COMMIT -> 200 OK
 *
 *   Both requests got 200. Both customers were promised the last unit. The
 *   database ends at available=0, reserved=1 — it LOOKS consistent, the CHECK
 *   constraint never fires, and you have oversold. T2 overwrote T1's write
 *   based on a value that was already out of date. Nothing errored. That is
 *   what makes this bug dangerous.
 *
 *   @Transactional does NOT prevent this. Postgres' default isolation is
 *   READ COMMITTED: both transactions legitimately read the committed value 1.
 *
 * WHAT @Version DOES:
 *   You add a numeric field annotated @Version. From then on Hibernate:
 *     - reads it along with the rest of the row (say version = 7), and
 *     - rewrites every UPDATE of this entity to:
 *
 *         UPDATE inventory_items
 *            SET available_qty = ?, reserved_qty = ?, version = 8, ...
 *          WHERE id = ? AND version = 7
 *
 *   Replay the race:
 *     T1's UPDATE matches the row (version is 7), sets version = 8, commits.
 *     T2's UPDATE says "WHERE version = 7" — but the row is now 8.
 *     It matches ZERO rows. Nothing is overwritten.
 *
 *   Hibernate checks the update count. Expected 1, got 0 -> it concludes
 *   "someone changed this row after I read it" and throws.
 *
 *   "Optimistic" = we take NO lock while reading. We assume conflicts are rare
 *   and only DETECT them at write time. Compare to pessimistic locking
 *   (SELECT ... FOR UPDATE), which makes T2 wait at the read.
 *
 * WHICH EXCEPTION YOU WILL ACTUALLY SEE:
 *   Hibernate throws org.hibernate.StaleObjectStateException (JPA's name for the
 *   same thing is jakarta.persistence.OptimisticLockException). Spring's
 *   exception translation wraps it, so what reaches your controller layer is:
 *
 *     org.springframework.orm.ObjectOptimisticLockingFailureException
 *       (a subclass of org.springframework.dao.OptimisticLockingFailureException)
 *
 *   Catch / handle the Spring type, not the Hibernate one.
 *
 * WHEN IT IS THROWN — the trap:
 *   NOT when you call reserve() on the entity. Not even when you call save().
 *   The UPDATE is sent when Hibernate FLUSHES, which by default happens at
 *   COMMIT — i.e. AFTER your @Transactional service method body has finished,
 *   inside the Spring proxy. So a try/catch INSIDE the service method will
 *   never see it. It surfaces to whoever called the service.
 *
 * HOW YOU SHOULD HANDLE IT (in this order):
 *   1. Always: map ObjectOptimisticLockingFailureException to HTTP 409 CONFLICT
 *      in GlobalExceptionHandler. The request was valid; it lost a race with
 *      the current state of the server. Never let it become a 500.
 *   2. Better: retry a small, bounded number of times (say 3) on the SERVER.
 *      Each retry must start a NEW transaction and RE-READ the row — retrying
 *      inside the same transaction just reuses the stale entity. On the retry,
 *      T2 re-reads availableQty = 0, fails the business check, and gets the
 *      honest answer: InsufficientStockException. That is the correct outcome.
 *      The retry therefore has to live OUTSIDE the @Transactional method —
 *      e.g. in a different bean that calls it — because of the self-invocation
 *      proxy rule you already met in ProductService.
 *   3. Never: "fix" it by catching and swallowing. The whole point is that the
 *      second write was based on stale data.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @Entity and @Table(name = "inventory_items").
 *
 *  2. Declare these fields, mirroring the migration EXACTLY:
 *       id            Long        -> @Id, @GeneratedValue(strategy = IDENTITY)
 *       productId     Long        -> @Column(name = "product_id", nullable = false, unique = true)
 *       name          String      -> @Column(nullable = false, length = 200)
 *       description   String      -> @Column(length = 1000)
 *       price         BigDecimal  -> @Column(nullable = false, precision = 12, scale = 2)
 *       availableQty  Integer     -> @Column(name = "available_qty", nullable = false)
 *       reservedQty   Integer     -> @Column(name = "reserved_qty", nullable = false)
 *       version       Long        -> @Version   (jakarta.persistence.Version!)
 *       createdAt     Instant     -> @Column(name = "created_at", nullable = false, updatable = false)
 *       updatedAt     Instant     -> @Column(name = "updated_at")
 *
 *  3. Constructors: the no-arg one JPA needs, and one taking
 *     productId, name, description, price, availableQty.
 *     reservedQty starts at 0 — set it in that constructor, not from the client.
 *     Do NOT set version. Leave it null; Hibernate sets it to 0 on insert.
 *
 *  4. Getters for every field (including getVersion()).
 *     NO setter for id, version, availableQty or reservedQty.
 *     Hibernate owns version. Quantities only change through step 5.
 *
 *  5. Add a domain method instead of quantity setters:
 *        public void reserve(int quantity)
 *     It moves `quantity` units from availableQty to reservedQty. Nothing else.
 *     The "is there enough stock?" decision belongs to the service (step 7),
 *     which throws InsufficientStockException before calling this.
 *
 *  6. @PrePersist / @PreUpdate timestamp callbacks, same as Product.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT (be ready to answer out loud)
 * ------------------------------------------------------------
 *  - Walk through the two-requests-one-unit race with and without @Version.
 *    What SQL does each transaction send, and how many rows does each match?
 *  - Optimistic vs pessimistic locking: on a flash sale where 5,000 people hit
 *    the same product in one second, which one do you pick and why? What does
 *    a single atomic "UPDATE ... SET available_qty = available_qty - ?
 *    WHERE product_id = ? AND available_qty >= ?" give you that neither does?
 *  - Why reserve(int) on the entity instead of setAvailableQty / setReservedQty?
 *    What invariant does that protect?
 */
public class InventoryItem {
}
