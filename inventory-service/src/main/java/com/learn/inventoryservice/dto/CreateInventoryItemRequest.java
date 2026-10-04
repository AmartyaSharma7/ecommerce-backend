package com.learn.inventoryservice.dto;

/**
 * ============================================================
 * STEP 4 of 9 — DTOs   (three files in this package)
 * ============================================================
 *
 * DTO — the body of POST /api/inventory. Registers a product with inventory
 * and sets its starting stock.
 *
 * INTUITION:
 *   Everything you learned in CreateProductRequest still holds (mass
 *   assignment, coupling, leakage). Look at what the client is NOT allowed to
 *   send here: id, reservedQty, version, timestamps. Each one is
 *   server-controlled. Accepting `version` from a client on create would let
 *   them corrupt the locking mechanism itself.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Rewrite this file as a record with components:
 *        Long productId, String name, String description,
 *        BigDecimal price, Integer availableQty
 *
 *  2. Validation, mirroring the migration's constraints:
 *        productId    -> @NotNull @Positive
 *        name         -> @NotBlank @Size(max = 200)
 *        description  -> @Size(max = 1000)
 *        price        -> @NotNull @DecimalMin(value = "0.01", message = "price must be > 0")
 *        availableQty -> @NotNull @Min(0)
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - The client sends productId 42. Nothing checks product 42 exists in
 *    product-service. Is that a bug? What would checking it cost you?
 *  - Why is there no reservedQty here, even though it is a real column?
 */
public class CreateInventoryItemRequest {
}
