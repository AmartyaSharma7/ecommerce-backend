package com.learn.productservice.service;

import com.learn.productservice.dto.CreateProductRequest;
import com.learn.productservice.dto.ProductResponse;
import com.learn.productservice.dto.UpdateProductRequest;
import com.learn.productservice.entity.Product;
import com.learn.productservice.exception.DuplicateProductException;
import com.learn.productservice.exception.ProductNotFoundException;
import com.learn.productservice.mapper.ProductMapper;
import com.learn.productservice.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * LAYER 4 of 5 — SERVICE   (build this FIFTH — the most important layer)
 * ============================================================
 *
 * WHAT THIS IS:
 *   Where your business logic lives. The controller only handles HTTP; the
 *   repository only handles SQL. Everything that is a RULE belongs here:
 *   "product names must be unique", "you cannot delete a product with stock".
 *
 * WHY IT EXISTS:
 *   Put the rules in the controller and they are welded to HTTP — you can never
 *   reuse them from a Kafka consumer or a scheduled job. In Phase 3 you will do
 *   exactly that: a Kafka listener will call a service method. That only works
 *   if the logic was never entangled with HTTP in the first place.
 *
 * TRANSACTIONS — THE KEY CONCEPT HERE:
 *   @Transactional on a method means: open a DB transaction when it starts,
 *   commit when it returns normally, ROLL BACK if a RuntimeException escapes.
 *   So if you save two things and the second fails, the first is undone.
 *
 *   How it works: Spring wraps your bean in a proxy. Calls coming in from
 *   OUTSIDE go through the proxy and get a transaction. This is why calling one
 *   @Transactional method from another method of the SAME class silently does
 *   nothing — the internal call never passes through the proxy. That is a
 *   classic interview question; remember it.
 *
 * DIRTY CHECKING — the other key concept:
 *   Inside a transaction, an entity loaded from the repository is "managed".
 *   Change its fields with setters and Hibernate notices at commit time and
 *   issues the UPDATE for you. You do not need to call save() on an entity you
 *   just loaded. This is why the mapper's applyUpdate returns void.
 *
 * ------------------------------------------------------------
 * YOUR TASK
 * ------------------------------------------------------------
 *  1. Annotate the class with @Service.
 *
 *  2. Declare two PRIVATE FINAL fields: ProductRepository and ProductMapper.
 *     Inject them with CONSTRUCTOR injection — one constructor taking both and
 *     assigning them. Do NOT use @Autowired on fields.
 *     Why constructor injection: the fields can be final (immutable), the class
 *     is constructible in a plain unit test with no Spring at all, and a missing
 *     dependency fails at startup rather than with a NullPointerException later.
 *     Since Spring 4.3 a single constructor needs no @Autowired annotation.
 *
 *  3. Implement these methods:
 *
 *     @Transactional
 *     public ProductResponse create(CreateProductRequest request)
 *         - if repository.existsByName(request.name()) -> throw
 *           DuplicateProductException (you will create that class)
 *         - map to entity, save, map the SAVED entity back to a response
 *         - return the saved one, not the input one. Why? It now has an id.
 *
 *     @Transactional(readOnly = true)
 *     public ProductResponse getById(Long id)
 *         - repository.findById(id)
 *              .map(mapper::toResponse)
 *              .orElseThrow(() -> new ProductNotFoundException(id));
 *         - readOnly = true is a hint: Hibernate skips dirty-checking, which is
 *           a genuine performance win on read paths.
 *
 *     @Transactional(readOnly = true)
 *     public List<ProductResponse> getAll(String nameFilter)
 *         - if nameFilter is null or blank -> findAll(), else the
 *           findByNameContainingIgnoreCase method
 *         - map every element to a response
 *
 *     @Transactional
 *     public ProductResponse update(Long id, UpdateProductRequest request)
 *         - load the entity or throw ProductNotFoundException
 *         - mapper.applyUpdate(entity, request)
 *         - return mapper.toResponse(entity)   <- note: NO save() call.
 *           If you understand why, you understand dirty checking.
 *
 *     @Transactional
 *     public void delete(Long id)
 *         - if (!repository.existsById(id)) throw ProductNotFoundException
 *         - repository.deleteById(id)
 *         - Why check first? So a delete of something that never existed
 *           returns 404 rather than pretending it worked.
 *
 * ------------------------------------------------------------
 * THINK ABOUT IT
 * ------------------------------------------------------------
 *  - The existsByName check then save is a race condition: two requests can
 *    both pass the check. What actually guarantees uniqueness? (A UNIQUE
 *    constraint in your Flyway migration. The code check is for a nice error
 *    message; the database is the real enforcement.)
 *  - Why does @Transactional roll back on RuntimeException but NOT on a checked
 *    Exception by default?
 */

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository pr,ProductMapper pm){
        productRepository = pr;
        productMapper = pm;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request){
        if(productRepository.existsByName(request.name())){
            throw new DuplicateProductException(request.name());
        }
        Product entity = productMapper.toEntity(request);
        Product saved = productRepository.save(entity);
        return productMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id){
        return productRepository.findById(id)
                .map(productMapper :: toResponse)
                .orElseThrow(()-> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll(String nameFilter){
//        List<ProductResponse> list = new ArrayList<>();
//
//        List<Product> productList;
//        if(nameFilter==null || nameFilter.isBlank()){
//            productList = productRepository.findAll();
//        }
//        else{
//            productList = productRepository.findByNameContainingIgnoreCase(nameFilter);
//        }
//        for(Product row : productList){
//            list.add(productMapper.toResponse(row));
//        }
//
//        return list;
        List<Product> products = (nameFilter == null || nameFilter.isBlank())
                ? productRepository.findAll()
                : productRepository.findByNameContainingIgnoreCase(nameFilter);

        return products.stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductResponse update(Long id , UpdateProductRequest request){
        Product entity = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
        productMapper.applyUpdate(entity,request);
        return productMapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id){
        if(!productRepository.existsById(id))
            throw new ProductNotFoundException(id);
        productRepository.deleteById(id);
    }
}
