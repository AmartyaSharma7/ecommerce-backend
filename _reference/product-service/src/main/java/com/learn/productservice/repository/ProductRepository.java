package com.learn.productservice.repository;

import com.learn.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * ============================================================
 * LAYER 2 of 5 — REPOSITORY   (build this SECOND)
 * ============================================================
 *
 * WHAT THIS IS:
 *   An INTERFACE with no implementation. You write zero method bodies.
 *
 * WHY THAT WORKS (this surprises people — make sure you can explain it):
 *   At startup, Spring Data JPA finds every interface extending JpaRepository,
 *   generates a proxy class implementing it at runtime, and registers that proxy
 *   as a bean. For "derived query methods" it parses the METHOD NAME into a query:
 *       findByNameContainingIgnoreCase  ->  WHERE LOWER(name) LIKE LOWER('%?%')
 *   So the method name IS the query. Spell it wrong and the app refuses to
 *   start — which is good, it fails at boot instead of at 3am.
 *
 * WHY HAVE THIS LAYER AT ALL:
 *   It is the only place allowed to touch the database. That boundary means your
 *   business logic can be tested without a database, and swapping the data store
 *   later touches one package instead of the whole codebase.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Declare:
 *        public interface ProductRepository extends JpaRepository<Product, Long>
 *     (Product = the entity type, Long = the type of its @Id field.)
 *
 *  2. You do NOT need @Repository on this interface. Work out why, then confirm.
 *
 *  3. Add these derived query methods (signatures only, no bodies):
 *        List<Product> findByNameContainingIgnoreCase(String name);
 *        boolean existsByName(String name);
 *        List<Product> findByStockQtyGreaterThan(Integer qty);
 *
 *  4. Add ONE explicit query so you have seen the syntax:
 *
 *        @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
 *        List<Product> findInPriceRange(@Param("min") BigDecimal min,
 *                                       @Param("max") BigDecimal max);
 *
 *     Note it says "FROM Product" (the CLASS), not "FROM products" (the table).
 *     That language is JPQL, not SQL. Know the difference and why it matters.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - What does JpaRepository already give you for free? Ctrl+click it and read.
 *  - findById returns Optional<Product>, not Product. Why did they design it
 *    that way, and what does it force YOU to handle in the service layer?
 */

public interface ProductRepository extends JpaRepository<Product,Long> {

     List<Product> findByNameContainingIgnoreCase(String name);
     boolean existsByName(String name);
     List<Product> findByStockQtyGreaterThan(Integer Qty);

    @Query("Select p FROM Product p WHERE p.price BETWEEN :min AND :max")
     List<Product> findInPriceRange(@Param("min") BigDecimal min,
                                         @Param("max") BigDecimal max);

}
