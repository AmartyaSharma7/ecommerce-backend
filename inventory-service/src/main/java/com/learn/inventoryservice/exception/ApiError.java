package com.learn.inventoryservice.exception;

/**
 * ============================================================
 * STEP 9 of 9 — ERROR SHAPE   (build with GlobalExceptionHandler)
 * ============================================================
 *
 * The JSON body returned when something goes wrong. Identical to
 * product-service's ApiError.
 *
 * WHY IDENTICAL:
 *   order-service will soon call product AND inventory. If both services
 *   return the same error shape, it needs one parser, not two. A consistent
 *   error contract across services is a design choice you can talk about.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Rewrite as a record:
 *        Instant timestamp, int status, String error, String message,
 *        String path, Map<String, String> fieldErrors
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - You now have this record copy-pasted in two services. Should it live in a
 *    shared library jar? What does that cost you in independent deployments?
 *  - Is "same shape" enough, or do the services also need the same set of
 *    `error` values for clients to rely on them?
 */
public class ApiError {
}
