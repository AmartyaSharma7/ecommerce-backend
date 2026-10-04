package com.learn.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================
 * ENTRY POINT — This file is COMPLETE. Read it, don't edit it.
 * ============================================================
 *
 * Identical in shape to ProductServiceApplication. Everything you write must
 * live under com.learn.inventoryservice, or @ComponentScan will not find it.
 *
 * SELF-CHECK: product-service and inventory-service both have a class annotated
 * @SpringBootApplication. Why is that fine here, and when would it NOT be fine?
 */
@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

}
