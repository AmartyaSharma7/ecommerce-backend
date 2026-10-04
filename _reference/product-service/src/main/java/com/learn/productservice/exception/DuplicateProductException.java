package com.learn.productservice.exception;

/**
 * Thrown when creating a product whose name already exists.
 *
 * INTUITION: this one maps to HTTP 409 CONFLICT, not 400. 400 means "your
 * request was malformed"; 409 means "your request was fine, but it conflicts
 * with the current state of the server". Being able to justify that distinction
 * is the kind of detail that makes an API look deliberately designed.
 *
 * YOUR TASK:
 *   Same shape as ProductNotFoundException, but the constructor takes a String
 *   name and the message reads something like "Product already exists: " + name.
 */
public class DuplicateProductException extends RuntimeException{
   public DuplicateProductException(String name){
       super("Product already exists: " + name);
   }
}
