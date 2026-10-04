package com.learn.productservice.controller;

import com.learn.productservice.dto.CreateProductRequest;
import com.learn.productservice.dto.ProductResponse;
import com.learn.productservice.dto.UpdateProductRequest;
import com.learn.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * ============================================================
 * LAYER 5 of 5 — CONTROLLER   (build this LAST)
 * ============================================================
 *
 * WHAT IT IS:
 *   The HTTP boundary. It maps URLs to service calls and nothing more.
 *
 * WHY IT IS DELIBERATELY THIN:
 *   A controller method should read like a summary: validate, delegate, wrap in
 *   a status code. If you find an `if` statement about business rules in here,
 *   it belongs in the service. Rule of thumb: no controller method longer than
 *   about five lines in this project.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @RestController and @RequestMapping("/api/products").
 *     @RestController = @Controller + @ResponseBody, meaning "every return value
 *     is the HTTP response body, serialized to JSON by Jackson" — not a view name.
 *
 *  2. Constructor-inject ProductService (same pattern as the service).
 *
 *  3. Implement these endpoints. Return ResponseEntity<T> so you control the
 *     status code explicitly.
 *
 *     POST /api/products
 *        @PostMapping
 *        public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request)
 *        - return 201 CREATED, not 200. Use ResponseEntity.status(HttpStatus.CREATED).body(...)
 *        - BONUS: also set the Location header to /api/products/{newId}. That is
 *          what REST actually prescribes for a creation response.
 *        - @Valid is what fires the DTO annotations. Leave it off and invalid
 *          data sails straight through.
 *
 *     GET /api/products/{id}
 *        @GetMapping("/{id}")  with  @PathVariable Long id
 *        - return ResponseEntity.ok(...)
 *
 *     GET /api/products?name=xyz
 *        @GetMapping
 *        - @RequestParam(required = false) String name
 *        - returns a List<ProductResponse>
 *
 *     PUT /api/products/{id}
 *        @PutMapping("/{id}") with @PathVariable and @Valid @RequestBody
 *
 *     DELETE /api/products/{id}
 *        @DeleteMapping("/{id}")
 *        - return ResponseEntity.noContent().build()  -> 204, empty body
 *
 *  4. Note what is NOT here: no try/catch. Exceptions thrown by the service
 *     travel up and are handled in one place — see GlobalExceptionHandler.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Which status code for: created / deleted / not found / bad input /
 *    duplicate name? (201, 204, 404, 400, 409 — know why for each.)
 *  - @RequestParam vs @PathVariable — when do you use which?
 *  - GET /api/products currently returns EVERY product. What happens at 2
 *    million rows? What is the fix, and what does Spring Data give you for it?
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse created = productService.create(request);
        URI location = URI.create("/api/products/" + created.id());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(productService.getAll(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}