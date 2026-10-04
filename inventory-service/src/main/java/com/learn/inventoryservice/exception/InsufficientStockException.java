package com.learn.inventoryservice.exception;

/**
 * Thrown by the service when a reserve asks for more than availableQty.
 *
 * INTUITION:
 *   This is the BUSINESS answer "not enough stock". It is a different thing from
 *   ObjectOptimisticLockingFailureException, which is the TECHNICAL answer
 *   "someone changed the row while you were working". Keep them separate:
 *     - insufficient stock -> definitive, retrying will not help
 *     - optimistic lock    -> transient, retrying with fresh data might help,
 *                             and the retry usually ends in THIS exception
 *   That distinction is the whole reason this class exists.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Extend RuntimeException.
 *  2. Constructor taking (Long productId, int requested, int available) with a
 *     message like "Insufficient stock for productId 42: requested 3, available 1".
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - 409 CONFLICT or 422 UNPROCESSABLE ENTITY for this? Pick one and justify it.
 *  - Is telling the caller the exact available quantity a business-data leak?
 *    When would a real shop hide it?
 */
public class InsufficientStockException {
}
