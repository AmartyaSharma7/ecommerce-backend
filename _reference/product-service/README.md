# Phase 1 — Product Service (fill-in-the-blanks build)

A single Spring Boot CRUD service. No microservices, no Kafka, no Redis yet.
This is the **pattern** every later service copies, so build it properly.

Nothing here compiles yet. Every class is an empty shell with instructions in
its header comment. Red errors in IntelliJ are expected until you finish a file.

---

## Setup (15 minutes, once)

1. Install **JDK 21** and **Docker Desktop**.
2. In IntelliJ: `File → Open` → select this `product-service` folder (the one
   with `pom.xml`). Choose **Open as Project**. Wait for Maven to download
   dependencies — the bottom status bar shows progress.
3. `File → Project Structure → Project` → SDK = 21, Language level = 21.
4. Start the database from a terminal in this folder:
   ```
   docker compose up -d
   ```
   Check it: `docker ps` should show `product-postgres`.
5. Do **not** run the app yet. It will fail until you fill things in.

---

## Build order — do NOT skip around

Each layer only compiles once the one below it exists. Work bottom-up.

| # | File | Why it comes here |
|---|------|-------------------|
| 1 | `resources/db/migration/V1__create_products_table.sql` | The schema is the foundation. Write it first so the entity has something to match. |
| 2 | `entity/Product.java` | Must mirror the SQL exactly, or startup fails. |
| 3 | `repository/ProductRepository.java` | Needs the entity to exist. |
| 4 | `dto/CreateProductRequest.java`, `UpdateProductRequest.java`, `ProductResponse.java` | Independent of the layers above — defines your API contract. |
| 5 | `mapper/ProductMapper.java` | Needs both the entity and the DTOs. |
| 6 | `exception/ProductNotFoundException.java`, `DuplicateProductException.java` | The service throws these, so define them first. |
| 7 | `service/ProductService.java` | The core. Uses repository + mapper + exceptions. |
| 8 | `controller/ProductController.java` | Thin HTTP wrapper over the service. |
| 9 | `exception/ApiError.java`, `GlobalExceptionHandler.java` | Turns those exceptions into clean HTTP responses. |
| 10 | `application.yml` | Two `TODO` values to set before the first run. |

---

## Checkpoints — stop and verify at each one

**Checkpoint A — after step 3 (repository).**
Temporarily add to `ProductServiceApplication`:
```java
@Bean
CommandLineRunner seed(ProductRepository repo) {
    return args -> System.out.println("PRODUCT COUNT = " + repo.count());
}
```
Run the app. If it starts, prints a count, and Flyway created a
`flyway_schema_history` table in Postgres, your schema + entity + repository
are wired correctly. Delete this bean afterward.

**Checkpoint B — after step 8 (controller).**
Run the app, open `requests.http`, execute requests 1, 4 and 6. You should get
201 and 200 responses with real JSON.

**Checkpoint C — after step 9 (exception handler).**
Execute requests 2, 3, 5 and 10. You should get **400, 409, 404, 404** — with a
clean `ApiError` body, no stack traces, no 500s. Once this passes, Phase 1
is genuinely done.

---

## How to inspect the database

```
docker exec -it product-postgres psql -U postgres -d product_db
```
Then: `\dt` (list tables), `\d products` (describe), `SELECT * FROM products;`,
`\q` to quit.

---

## When you get stuck

Read the error from the **bottom up** — the root cause is usually the last
`Caused by:` line, not the first. Common Phase 1 failures:

- `Schema-validation: missing table [products]` → Flyway didn't run, or your
  migration filename is wrong. It must be exactly `V1__name.sql` — capital V,
  **two** underscores.
- `Schema-validation: wrong column type` → entity field and SQL column disagree.
- `Table "products" already exists` → you ran with `ddl-auto: update` earlier.
  Fix: `docker compose down -v && docker compose up -d` for a clean database.
- `No qualifying bean of type ...` → the class is missing its stereotype
  annotation (`@Service` / `@Component`), or it lives outside
  `com.learn.productservice`.
- JSON comes back as `{}` → your response record has no accessors, or you
  returned the entity instead of the DTO.

---

## Before you come back for review

Answer these in your own words. If you can't, re-read the class comments.

1. Why does the DTO exist instead of returning the entity?
2. What does `@Transactional` actually do, and why does calling a
   `@Transactional` method from the same class not start a transaction?
3. Why does `update()` not call `save()`?
4. Why does `existsByName` not actually guarantee uniqueness?
5. What triggers `@NotBlank` to be checked?
6. What is the difference between JPQL and SQL?
7. Why 409 for a duplicate rather than 400?
