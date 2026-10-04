package com.learn.inventoryservice.exception;

/**
 * Thrown when POST /api/inventory is called for a productId that already has
 * an inventory row. Maps to 409 CONFLICT.
 *
 * INTUITION:
 *   Same shape and same race as DuplicateProductException: the
 *   existsByProductId check gives a friendly message, the UNIQUE constraint on
 *   product_id is the real guarantee.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Extend RuntimeException.
 *  2. Constructor taking Long productId, message like
 *     "Inventory already exists for productId: 42".
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Two creates for product 42 arrive together and both pass the exists check.
 *    The second INSERT violates UNIQUE. Which Spring exception does that
 *    surface as, and with your current handler, what status does the caller
 *    get? Is that acceptable?
 *  - @Version does NOT help here. Why not? (Hint: there is no row to version yet.)
 */
public class DuplicateInventoryItemException {
}
