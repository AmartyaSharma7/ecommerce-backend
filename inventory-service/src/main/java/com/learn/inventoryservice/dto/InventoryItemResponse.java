package com.learn.inventoryservice.dto;

/**
 * DTO — what every endpoint in this service sends back, including reserve.
 *
 * INTUITION:
 *   Same rule as ProductResponse: server-generated data out, internals hidden.
 *   The internal you are deliberately hiding this time is `version`. It is a
 *   concurrency mechanism, not business data.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Rewrite as a record with components:
 *        Long id, Long productId, String name, String description,
 *        BigDecimal price, Integer availableQty, Integer reservedQty,
 *        Instant createdAt
 *
 *  2. No validation annotations — you do not validate your own output.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - There IS a well-known reason to expose a version to clients: HTTP ETag +
 *    If-Match headers, so two admins editing the same item do not overwrite each
 *    other. How is that the same idea as @Version, one layer further out?
 *  - Is reusing this response for /reserve the right call, or does a reservation
 *    deserve its own response shape? Argue both sides.
 */
public class InventoryItemResponse {
}
