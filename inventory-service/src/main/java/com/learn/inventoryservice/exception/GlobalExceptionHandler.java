package com.learn.inventoryservice.exception;

/**
 * ============================================================
 * STEP 9 of 9 — GLOBAL EXCEPTION HANDLER   (build this LAST of all)
 * ============================================================
 *
 * WHAT IT IS:
 *   Same @RestControllerAdvice pattern as product-service: every exception
 *   becomes exactly one status code with an ApiError body, in one place.
 *
 * WHAT IS NEW:
 *   This handler is where optimistic locking finally becomes visible to the
 *   client. The ObjectOptimisticLockingFailureException that escaped the
 *   service at commit time lands here. Without a dedicated handler it falls
 *   into the Exception.class catch-all and becomes a 500 — which tells the
 *   caller "the server is broken" when the truth is "you lost a race, try
 *   again". Getting this mapping right is the point of this step.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. @RestControllerAdvice, and a private static final Logger.
 *
 *  2. Handlers, each returning ResponseEntity<ApiError>:
 *       InventoryItemNotFoundException            -> 404 NOT_FOUND
 *       DuplicateInventoryItemException           -> 409 CONFLICT
 *       InsufficientStockException                -> 409 (or 422 — your call
 *                                                    from that class's question)
 *       ObjectOptimisticLockingFailureException   -> 409 CONFLICT
 *           (org.springframework.orm — NOT the Hibernate or jakarta type)
 *           Log at WARN, not ERROR: this is expected under load, not a bug.
 *           Message to client: generic, e.g. "Stock was updated concurrently,
 *           please retry". Do not echo the exception message (it names your
 *           entity class and id).
 *       MethodArgumentNotValidException           -> 400 with fieldErrors
 *       Exception                                 -> 500, log ERROR, generic message
 *
 *  3. Reuse the private build(...) helper idea from product-service.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Insufficient stock and optimistic-lock failure can both be 409. How does a
 *    client tell "give up" apart from "retry"? Is the status code enough, or
 *    does the body need something machine-readable?
 *  - Why is an optimistic lock failure a WARN but an unknown exception an ERROR?
 *    What would happen to your alerting if you logged every lock failure as
 *    ERROR during a sale?
 */
public class GlobalExceptionHandler {
}
