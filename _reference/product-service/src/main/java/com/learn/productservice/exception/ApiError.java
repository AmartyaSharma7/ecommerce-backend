package com.learn.productservice.exception;

import java.time.Instant;
import java.util.Map;

/**
 * The JSON body your API returns when something goes wrong.
 *
 * WHY DEFINE AN ERROR SHAPE AT ALL:
 *   By default Spring returns a stack-trace-flavoured blob that differs between
 *   error types. A client cannot program against that. One consistent error
 *   shape is a real API-design point you can talk about.
 *
 * YOUR TASK:
 *   public record ApiError(
 *           Instant timestamp,
 *           int status,
 *           String error,        // e.g. "Not Found"
 *           String message,      // human-readable detail
 *           String path,         // the request URI
 *           Map<String, String> fieldErrors   // null unless it was a validation failure
 *   ) {}
 *
 * THINK ABOUT IT:
 *   - Should `message` ever contain a raw exception message from the database?
 *     (No. That leaks table names and is a genuine security issue.)
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {}
