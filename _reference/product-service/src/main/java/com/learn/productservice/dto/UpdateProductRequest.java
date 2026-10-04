package com.learn.productservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO — the body of PUT /api/products/{id}.
 *
 * WHY A SEPARATE CLASS FROM CreateProductRequest:
 *   They look identical today and it is tempting to reuse one. Don't. Create and
 *   update diverge almost immediately: creation may later require a categoryId,
 *   update may forbid changing the SKU. One shared DTO forces every field to be
 *   optional, and at that point your validation means nothing.
 *
 * YOUR TASK:
 *   Same shape and same validation rules as CreateProductRequest, as a record.
 *
 * THINK ABOUT IT:
 *   - PUT replaces the whole resource; PATCH changes part of it. Which are you
 *     building, and what should happen to a field the client left out?
 */
public record UpdateProductRequest(
      @NotBlank(message = "name is required")@Size(max=200)
      String name,
      @Size(max=1000)
      String description,
      @NotNull @DecimalMin(value="0.01",message="price must be > 0")
      BigDecimal price,
      @NotNull @Min(0)
      Integer stockQty
) {}