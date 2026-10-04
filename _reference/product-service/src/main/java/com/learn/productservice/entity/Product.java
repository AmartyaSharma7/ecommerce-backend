package com.learn.productservice.entity;

// You will need imports from jakarta.persistence.*, java.math.BigDecimal and
// java.time.Instant. Let IntelliJ add them: type the class name, press Alt+Enter.

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * ============================================================
 * LAYER 1 of 5 — ENTITY   (build this FIRST)
 * ============================================================
 *
 * WHAT THIS CLASS IS:
 *   A Java object that maps 1-to-1 onto a row in the `products` table.
 *   One instance of Product == one row.
 *
 * WHY IT EXISTS / THE INTUITION:
 *   Without JPA you would write SQL strings, run them, then manually copy each
 *   column out of a ResultSet into fields. That is hundreds of lines of boring,
 *   error-prone glue. An entity lets Hibernate generate that SQL from the shape
 *   of the class instead, so you write productRepository.save(p).
 *
 *   The tradeoff to remember: you gave up control of the SQL. Later you will hit
 *   the N+1 query problem because of exactly this. Know the tradeoff — it is a
 *   very common interview follow-up.
 *
 * IMPORTANT RULE:
 *   This class NEVER leaves the service layer. It is not what your API returns.
 *   That is what the dto package is for, and the reason is explained there.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @Entity and @Table(name = "products").
 *
 *  2. Declare these fields with these annotations:
 *       id          Long        -> @Id and @GeneratedValue(strategy = GenerationType.IDENTITY)
 *       name        String      -> @Column(nullable = false)
 *       description String      -> @Column(length = 1000)
 *       price       BigDecimal  -> @Column(nullable = false, precision = 12, scale = 2)
 *       stockQty    Integer     -> @Column(name = "stock_qty", nullable = false)
 *       createdAt   Instant     -> @Column(name = "created_at", nullable = false, updatable = false)
 *       updatedAt   Instant     -> @Column(name = "updated_at")
 *
 *  3. Add a no-argument constructor (JPA REQUIRES one — see the question below)
 *     and a second constructor taking name, description, price, stockQty.
 *
 *  4. Add getters for every field, and setters only for the mutable ones
 *     (name, description, price, stockQty). No setter for id.
 *     -> IntelliJ shortcut: Alt+Insert -> Getter and Setter.
 *
 *  5. Add these two lifecycle callbacks so you never set timestamps by hand:
 *
 *        @PrePersist  void onCreate() { createdAt = Instant.now(); updatedAt = createdAt; }
 *        @PreUpdate   void onUpdate() { updatedAt = Instant.now(); }
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT (be ready to answer out loud)
 * ------------------------------------------------------------
 *  - Why BigDecimal for money and never double or float?
 *  - Why does JPA require a no-arg constructor? (Hint: reflection. Hibernate
 *    must construct the object before it has any values to put in it.)
 *  - GenerationType.IDENTITY vs SEQUENCE: which does Postgres prefer, and why
 *    does IDENTITY prevent Hibernate from batching inserts?
 */
@Entity
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 200)
    private String name;

    @Column(length=1000)
    private String description;

    @Column(nullable = false,precision = 12,scale=2)
    private BigDecimal price;

    @Column(name="stock_qty",nullable=false)
    private Integer stockQty;

    @Column(name = "created_at",nullable=false,updatable = false)
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;

    public Product(){

    }

    public Product(String name,String description,BigDecimal price,Integer stockQuantity){
        this.name=name;
        this.description=description;
        this.price=price;
        this.stockQty=stockQuantity;
    }

    //Getters
    public Long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public BigDecimal getPrice(){
        return price;
    }
    public Integer getStockQty(){
        return stockQty;
    }
    public Instant getCreatedAt(){
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description=description;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public void setStockQty(Integer stockQuantity){
        this.stockQty=stockQuantity;
    }


    //Lifecycle callbacks

    @PrePersist
    public void onCreate(){
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate(){
        updatedAt = Instant.now();
    }

}

