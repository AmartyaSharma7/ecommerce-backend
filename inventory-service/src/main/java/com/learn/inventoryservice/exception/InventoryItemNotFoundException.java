package com.learn.inventoryservice.exception;

/**
 * ============================================================
 * STEP 6 of 9 — EXCEPTIONS   (three files, plus ApiError later)
 * ============================================================
 *
 * Thrown when no inventory row exists for a productId. Maps to 404.
 *
 * INTUITION:
 *   Same reasoning as ProductNotFoundException: unchecked, so @Transactional
 *   rolls back, and named, so it maps to exactly one status in one place.
 *   Note the message should mention the PRODUCT id — that is the id the caller
 *   used, not inventory's internal one.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Extend RuntimeException.
 *  2. One constructor taking Long productId, passing a message like
 *     "No inventory for productId: 42" to super(...).
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Product 42 exists in product-service but has no inventory row. From
 *    order-service's point of view, is that a 404, or "0 in stock"?
 *  - Should this message ever include anything from the database layer?
 */
public class InventoryItemNotFoundException {
}
