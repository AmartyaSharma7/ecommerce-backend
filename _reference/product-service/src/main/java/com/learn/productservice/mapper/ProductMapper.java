package com.learn.productservice.mapper;

import com.learn.productservice.dto.CreateProductRequest;
import com.learn.productservice.dto.ProductResponse;
import com.learn.productservice.dto.UpdateProductRequest;
import com.learn.productservice.entity.Product;
import org.springframework.stereotype.Component;

/**
 * ============================================================
 * SUPPORT CLASS — MAPPER   (build this FOURTH, it is small)
 * ============================================================
 *
 * WHAT IT DOES:
 *   Converts between DTOs and entities. Entity -> ProductResponse on the way
 *   out, CreateProductRequest -> Entity on the way in.
 *
 * WHY A SEPARATE CLASS:
 *   This conversion has to happen somewhere. If you leave it inline in the
 *   service, that method grows into a wall of getters and the real business
 *   logic gets buried. Pulling it out keeps the service readable.
 *
 *   Libraries like MapStruct generate this code for you. Write it by hand once
 *   so you understand what MapStruct is doing, then reach for MapStruct later.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @Component so Spring can inject it.
 *     (@Component is the generic version of @Service / @Repository — use it for
 *     helper beans that are neither.)
 *
 *  2. Write:
 *        public ProductResponse toResponse(Product product)
 *           -> build a ProductResponse from the entity's getters.
 *
 *        public Product toEntity(CreateProductRequest request)
 *           -> build a new Product from the request. Do NOT set id or
 *              timestamps; the database and @PrePersist handle those.
 *
 *        public void applyUpdate(Product product, UpdateProductRequest request)
 *           -> copy the request's values onto an EXISTING entity via setters.
 *              Note it returns void. Understand why before you move on — the
 *              answer is in the service class comments (dirty checking).
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - Could this be a class of static methods instead of a Spring bean? What
 *    would you lose? (Hint: mocking it in a unit test.)
 */
@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product){
        return new ProductResponse(product.getId(),
                product.getName(), product.getDescription(), product.getPrice(), product.getStockQty(), product.getCreatedAt());
    }

    public Product toEntity(CreateProductRequest request){
        return new Product(request.name(),request.description(),request.price(), request.stockQty());
    }

    public void applyUpdate(Product product, UpdateProductRequest request){
        product.setDescription(request.description());
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStockQty(request.stockQty());
    }

}
