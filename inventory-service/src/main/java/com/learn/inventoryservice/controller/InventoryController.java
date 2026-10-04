package com.learn.inventoryservice.controller;

/**
 * ============================================================
 * STEP 8 of 9 — CONTROLLER
 * ============================================================
 *
 * WHAT IT IS:
 *   The HTTP boundary, as thin as ProductController. Validate, delegate, wrap
 *   in a status code. No business `if`s, no try/catch — including NO catching
 *   of the optimistic-lock exception here. That goes in the handler.
 *
 * WHY THE PATH VARIABLE IS productId, NOT id:
 *   Callers (order-service, the gateway, a UI) know products by productId.
 *   Inventory's own primary key is an internal detail they should never need.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. @RestController, @RequestMapping("/api/inventory").
 *  2. Constructor-inject InventoryService.
 *  3. Endpoints, all returning ResponseEntity<...>:
 *
 *     POST /api/inventory
 *        - @Valid @RequestBody CreateInventoryItemRequest
 *        - 201 Created, Location header /api/inventory/{productId}
 *
 *     GET /api/inventory
 *        - 200, List<InventoryItemResponse>
 *
 *     GET /api/inventory/{productId}
 *        - @PathVariable Long productId, 200
 *
 *     POST /api/inventory/reserve
 *        - @Valid @RequestBody ReserveStockRequest
 *        - 200 OK with the updated item
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - GET /{productId} and POST /reserve sit under the same prefix. Why do they
 *    never clash? What WOULD clash if you added GET /reserve?
 *  - Should reserve return 200 or 201? Is a reservation a new resource?
 *  - The Location header: why does it use productId and not the new row's id?
 */
public class InventoryController {
}
