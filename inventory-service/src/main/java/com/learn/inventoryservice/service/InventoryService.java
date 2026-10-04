package com.learn.inventoryservice.service;

/**
 * ============================================================
 * STEP 7 of 9 — SERVICE   (the most important layer)
 * ============================================================
 *
 * WHAT THIS IS:
 *   Business rules for stock: "one inventory row per product", "you cannot
 *   reserve more than is available". Transactions and dirty checking work
 *   exactly as in ProductService — re-read that header if they feel shaky.
 *
 * WHERE @Version SHOWS UP IN THIS CLASS:
 *   Nowhere, syntactically. reserve() loads the item, checks the quantity,
 *   calls item.reserve(qty), and returns. Dirty checking issues the UPDATE at
 *   commit, and because the entity has @Version, that UPDATE carries
 *   "AND version = ?". If another transaction won the race, the exception is
 *   thrown at COMMIT — after your method body has already returned, inside the
 *   @Transactional proxy. Read the "WHEN IT IS THROWN" section of
 *   InventoryItem.java again. This is why you cannot try/catch it in here.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. @Service. Constructor-inject InventoryItemRepository and InventoryMapper
 *     into private final fields. No @Autowired on fields.
 *
 *  2. @Transactional
 *     public InventoryItemResponse create(CreateInventoryItemRequest request)
 *        - existsByProductId -> DuplicateInventoryItemException
 *        - map, save, return the SAVED entity mapped to a response
 *
 *  3. @Transactional(readOnly = true)
 *     public InventoryItemResponse getByProductId(Long productId)
 *        - findByProductId, map, or InventoryItemNotFoundException
 *
 *  4. @Transactional(readOnly = true)
 *     public List<InventoryItemResponse> getAll()
 *
 *  5. @Transactional
 *     public InventoryItemResponse reserve(ReserveStockRequest request)
 *        a. load by productId or InventoryItemNotFoundException
 *        b. if availableQty < quantity -> InsufficientStockException
 *        c. item.reserve(quantity)
 *        d. return the mapped item — and note: NO save() call, same as update()
 *           in ProductService. Make sure you can say why.
 *
 *  6. Do NOT add retry logic yet. First get the 409 path working end-to-end
 *     (README Checkpoint D). Then, as a stretch, write a SEPARATE bean that
 *     calls reserve() up to 3 times when ObjectOptimisticLockingFailureException
 *     is thrown. It must be a separate bean — explain why before writing it.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Step 5b reads availableQty and step 5c writes it. Without @Version, what
 *    can happen between those two lines? With @Version, what happens instead?
 *  - Why would a retry placed INSIDE this @Transactional method never succeed?
 *    (Two reasons: the proxy, and the persistence context still holding the
 *    stale entity.)
 *  - Reservations never expire right now. A user reserves, closes the tab, and
 *    that unit is held forever. Whose job is it to release it?
 */
public class InventoryService {
}
