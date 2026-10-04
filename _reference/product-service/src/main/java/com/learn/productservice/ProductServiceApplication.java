package com.learn.productservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.learn.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

/**
 * ============================================================
 * ENTRY POINT — This file is COMPLETE. Read it, don't edit it.
 * ============================================================
 *
 * WHAT IT IS:
 *   The main() method. Running this starts an embedded Tomcat server
 *   on the port set in application.yml.
 *
 * WHY @SpringBootApplication MATTERS (interviewers ask this):
 *   It is three annotations in one —
 *     1. @Configuration        — this class can define beans.
 *     2. @EnableAutoConfiguration — Spring looks at what's on your classpath
 *        and configures it for you. Postgres driver present + a datasource URL
 *        in application.yml  ->  it builds a connection pool automatically.
 *     3. @ComponentScan       — it scans THIS package and every sub-package
 *        for @RestController / @Service / @Repository / @Component and
 *        creates one instance ("bean") of each, then injects them into
 *        whoever needs them.
 *
 *   That last point is why your package structure matters: every class you
 *   write must live under com.learn.productservice, or Spring won't find it.
 *
 * SELF-CHECK: If you moved this class into a sub-package, what would break?
 */
@SpringBootApplication
public class ProductServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
