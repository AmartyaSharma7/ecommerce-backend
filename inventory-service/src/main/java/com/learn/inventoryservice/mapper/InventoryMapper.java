package com.learn.inventoryservice.mapper;

/**
 * ============================================================
 * STEP 5 of 9 — MAPPER
 * ============================================================
 *
 * WHAT IT DOES:
 *   Entity <-> DTO conversion, same as ProductMapper.
 *
 * WHAT IS DIFFERENT:
 *   There is no applyUpdate(). Inventory has no "edit everything" endpoint;
 *   stock only changes through reserve(), which is business logic, not mapping.
 *   A mapper that starts deciding quantities has stopped being a mapper.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate with @Component.
 *
 *  2. Write (signatures given, bodies are yours):
 *        public InventoryItemResponse toResponse(InventoryItem item)
 *        public InventoryItem toEntity(CreateInventoryItemRequest request)
 *
 *  3. toEntity must NOT touch id, reservedQty, version or timestamps.
 *     If you find yourself passing `0` for reservedQty here, move it into the
 *     entity constructor where it belongs.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Why must toResponse be called on the SAVED entity in create(), not on the
 *    one you passed into save()? (Two fields differ. Name them.)
 *  - Where does the rule "a new item starts with 0 reserved" belong — mapper,
 *    entity, or service? Defend your answer.
 */
public class InventoryMapper {
}
