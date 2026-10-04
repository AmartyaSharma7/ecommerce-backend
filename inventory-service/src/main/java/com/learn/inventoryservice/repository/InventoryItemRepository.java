package com.learn.inventoryservice.repository;

/**
 * ============================================================
 * STEP 3 of 9 — REPOSITORY
 * ============================================================
 *
 * WHAT THIS IS:
 *   Same idea as ProductRepository: an interface, Spring Data writes the proxy.
 *
 * WHAT IS DIFFERENT HERE:
 *   Other services will never know inventory's internal `id`. order-service will
 *   say "reserve 2 of product 42" — it only knows productId. So the lookups that
 *   matter are by productId, not by primary key.
 *
 *   You do NOT write anything for @Version. JpaRepository's save() and dirty
 *   checking already include the version check automatically once the entity
 *   has the field. That is the whole appeal: optimistic locking costs you one
 *   annotation and zero repository code.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Turn the placeholder below into an INTERFACE extending
 *     JpaRepository<InventoryItem, Long>.
 *
 *  2. Add derived query methods (signatures only):
 *        Optional<InventoryItem> findByProductId(Long productId);
 *        boolean existsByProductId(Long productId);
 *
 *  3. That's it. Resist adding more until a service method needs it.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - findByProductId returns Optional but findAll returns List. Why does it
 *    never make sense for a "find many" method to return Optional?
 *  - If you wanted PESSIMISTIC locking instead, you would add a method here
 *    annotated @Lock(LockModeType.PESSIMISTIC_WRITE). What SQL would that emit,
 *    and what happens to the second transaction?
 */
public class InventoryItemRepository {
}
