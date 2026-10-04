package com.learn.productservice.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO — what your API sends BACK. This is your public contract.
 *
 * INTUITION:
 *   Requests and responses are deliberately different shapes. The response
 *   carries server-generated data (id, createdAt) that the client could never
 *   have supplied, and hides anything internal.
 *
 * YOUR TASK:
 *   Rewrite as a record:
 *
 *      public record ProductResponse(
 *              Long id,
 *              String name,
 *              String description,
 *              BigDecimal price,
 *              Integer stockQty,
 *              Instant createdAt
 *      ) {}
 *
 *   No validation annotations here — you do not validate your own output.
 *
 * THINK ABOUT IT:
 *   - Once a mobile app has shipped against this shape, can you ever rename
 *     `price`? What do real teams do instead?
 */
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQty,
        Instant createdAt
) {}
