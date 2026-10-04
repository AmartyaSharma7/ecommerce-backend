package com.learn.productservice.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
/**
 * ============================================================
 * CROSS-CUTTING — GLOBAL EXCEPTION HANDLER   (build this LAST of all)
 * ============================================================
 *
 * WHAT IT IS:
 *   One class that catches exceptions thrown anywhere in any controller and
 *   turns each into a proper HTTP response.
 *
 * HOW IT WORKS:
 *   @RestControllerAdvice registers this class as a global interceptor around
 *   every controller. When a method inside a controller throws, Spring looks for
 *   an @ExceptionHandler here matching that exception type and calls it instead.
 *
 * WHY THIS IS BETTER THAN try/catch IN CONTROLLERS:
 *   Without it, every controller method needs the same try/catch block, and the
 *   day you change the error format you edit thirty methods. This is the same
 *   idea as a global error boundary — handle it once, at the edge.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @RestControllerAdvice.
 *
 *  2. Write these handlers. Each takes the exception (and optionally
 *     HttpServletRequest, to read request.getRequestURI() for the path field)
 *     and returns ResponseEntity<ApiError>.
 *
 *     @ExceptionHandler(ProductNotFoundException.class)   -> 404 NOT_FOUND
 *
 *     @ExceptionHandler(DuplicateProductException.class)  -> 409 CONFLICT
 *
 *     @ExceptionHandler(MethodArgumentNotValidException.class) -> 400 BAD_REQUEST
 *        This is the one @Valid throws. Extract the per-field messages:
 *           ex.getBindingResult().getFieldErrors()
 *             -> collect into Map<String, String> of field -> message
 *        Put that map in ApiError.fieldErrors so the client can highlight the
 *        exact fields that failed. This is the difference between a usable API
 *        and one that just says "400".
 *
 *     @ExceptionHandler(Exception.class)                  -> 500 INTERNAL_SERVER_ERROR
 *        The catch-all. Log the real exception at ERROR level, but return a
 *        GENERIC message to the caller. Never echo an internal stack trace to
 *        a client — that is an information-disclosure vulnerability.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - If two handlers could match a thrown exception (say Exception.class and
 *    ProductNotFoundException.class), which one wins? Test it.
 *  - Where would you add a correlation/trace id so a user reporting "I got a
 *    500" can be matched to a specific log line? (You will actually need this
 *    once you have five services in Phase 5.)
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ProductNotFoundException ex,
                                                   HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateProductException ex,
                                                    HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", request, fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleEverythingElse(Exception ex,
                                                         HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request, null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message,
                                           HttpServletRequest request,
                                           Map<String, String> fieldErrors) {
        ApiError body = new ApiError(
                Instant.now(), status.value(), status.getReasonPhrase(),
                message, request.getRequestURI(), fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
}
