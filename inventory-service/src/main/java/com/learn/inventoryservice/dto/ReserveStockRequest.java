package com.learn.inventoryservice.dto;

/**
 * DTO — the body of POST /api/inventory/reserve.
 *
 * WHAT "RESERVE" MEANS:
 *   Not "sell". It moves units from available to reserved: "hold these for an
 *   order that has not been paid yet". Later, payment succeeding will CONFIRM
 *   the reservation and payment failing will RELEASE it. Those two operations
 *   do not exist yet — they arrive with order-service and the Saga in Phase 3.
 *
 * WHY POST AND NOT PUT/PATCH ON THE ITEM:
 *   Reserve is a COMMAND ("do this action"), not "replace this resource's
 *   fields". The client must not send the new availableQty itself — the server
 *   computes it from the current value. That computation is exactly where the
 *   race condition lives, so it must happen server-side, under @Version.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Rewrite as a record: Long productId, Integer quantity
 *
 *  2. Validation:
 *        productId -> @NotNull @Positive
 *        quantity  -> @NotNull @Positive   (reserving 0 or -3 is meaningless)
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - order-service calls reserve, the network times out, it retries. The first
 *    call actually succeeded. You just reserved twice. What would you add to
 *    this request to make reserve idempotent?
 *  - A real order has 5 different products. Should that be 5 reserve calls or
 *    one? What happens if call 4 fails?
 */
public class ReserveStockRequest {
}
