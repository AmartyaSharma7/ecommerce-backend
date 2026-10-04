package com.learn.productservice.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;


/**
 * ============================================================
 * LAYER 3 of 5 — DTOs   (build these THIRD)
 * ============================================================
 *
 * WHAT A DTO IS:
 *   Data Transfer Object. A plain class describing the JSON shape crossing your
 *   API boundary — one for what comes in, one for what goes out.
 *
 * WHY NOT JUST USE THE ENTITY? (near-guaranteed interview question)
 *   Four reasons. Learn all four:
 *     1. SECURITY — if the controller accepted a Product, a caller could POST
 *        {"id": 5, "createdAt": "1999-01-01"} and set fields they must never
 *        control. This is called mass assignment.
 *     2. COUPLING — your JSON contract would change every time you renamed a
 *        database column. Clients break because of an internal refactor.
 *     3. LEAKAGE — entities hold internal fields (audit columns, version, cost
 *        price) that should not be public.
 *     4. LAZY LOADING — serializing an entity with lazy relations to JSON either
 *        throws LazyInitializationException or silently fires extra queries.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  Rewrite this file as a Java record (records are immutable — ideal for DTOs):
 *
 *      public record CreateProductRequest(
 *              String name,
 *              String description,
 *              BigDecimal price,
 *              Integer stockQty
 *      ) {}
 *
 *  Then put validation annotations directly on the record components:
 *      name        -> @NotBlank(message = "name is required") @Size(max = 200)
 *      description -> @Size(max = 1000)
 *      price       -> @NotNull @DecimalMin(value = "0.01", message = "price must be > 0")
 *      stockQty    -> @NotNull @Min(0)
 *
 *  Notice there is NO id field. The client does not get to choose the id.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - These annotations do nothing by themselves. What actually triggers them?
 *    (The answer lives in the controller: @Valid. People forget it and then
 *    wonder why garbage data reached the database.)
 */

public record CreateProductRequest(
        @NotBlank(message = "name is required") @Size(max=200)
        String name,
        @Size(max=1000)
        String description,
        @NotNull @DecimalMin(value = "0.01",message="price must be > 0")
        BigDecimal price,
        @NotNull @Min(0)
        Integer stockQty
) {}