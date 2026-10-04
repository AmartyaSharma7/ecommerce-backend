package com.learn.productservice.exception;

/**
 * A custom UNCHECKED exception, thrown by the service when an id does not exist.
 *
 * WHY A CUSTOM TYPE INSTEAD OF RETURNING null:
 *   null forces every caller to remember a null check, and the one who forgets
 *   gets a NullPointerException with no context. A named exception carries
 *   meaning ("this specific product was missing") and can be translated into
 *   exactly one HTTP status in exactly one place.
 *
 * WHY IT EXTENDS RuntimeException, NOT Exception:
 *   Checked exceptions would force `throws` clauses through every layer, and —
 *   importantly — @Transactional does not roll back on checked exceptions by
 *   default. Spring's own convention is unchecked exceptions for this reason.
 *
 * YOUR TASK:
 *   public class ProductNotFoundException extends RuntimeException {
 *       public ProductNotFoundException(Long id) {
 *           super("Product not found with id: " + id);
 *       }
 *   }
 */
public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long id){
        super("Product not found with id:"+id);
    }
}
