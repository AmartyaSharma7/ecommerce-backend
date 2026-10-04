# Phase 2 — Inventory Service (fill-in-the-blanks build)

The second service. Structurally it is product-service again, so the parts you
already know should go fast. The **one genuinely new concept** is optimistic
locking with `@Version`. Most of your thinking time should go there.

Nothing here compiles yet. Every class is an empty shell with instructions in
its header comment. Red errors in IntelliJ are expected until you finish a file.

| | |
|---|---|
| Package | `com.learn.inventoryservice` |
| App port | `8082` |
| Database | `inventory_db` in container `inventory-postgres`, host port **5433** |

---

## Setup (5 minutes)

1. In IntelliJ: `File → Open` → select this `inventory-service` folder (the one
   with `pom.xml`). **Open as Project.** It is its own Maven project. There is
   no parent POM tying it to the other services.
2. SDK = 21, Language level = 21, same as before.
3. Start this service's database from a terminal in this folder:
   ```
   docker compose up -d
   ```
   `docker ps` should show `inventory-postgres` on `0.0.0.0:5433->5432`.
   It can run at the same time as `product-postgres`. That is why it uses 5433.
4. Do **not** run the app yet.

---

## Build order — do NOT skip around

| # | File | Why it comes here |
|---|------|-------------------|
| 1 | `resources/db/migration/V1__create_inventory_items_table.sql` | Schema first. Includes the `version` column. |
| 2 | `entity/InventoryItem.java` | Mirrors the SQL. **Read the @Version section slowly. It is the lesson of this phase.** |
| 3 | `repository/InventoryItemRepository.java` | Lookups by `productId`, not by `id`. |
| 4 | `dto/CreateInventoryItemRequest.java`, `ReserveStockRequest.java`, `InventoryItemResponse.java` | The API contract. |
| 5 | `mapper/InventoryMapper.java` | No `applyUpdate`. Find out why. |
| 6 | `exception/InventoryItemNotFoundException.java`, `DuplicateInventoryItemException.java`, `InsufficientStockException.java` | The service throws these. |
| 7 | `service/InventoryService.java` | `reserve()` is where the race lives. |
| 8 | `controller/InventoryController.java` | Thin, four endpoints. |
| 9 | `exception/ApiError.java`, `GlobalExceptionHandler.java` | Includes the 409 mapping for optimistic-lock failures. |

`application.yml`, `pom.xml`, `docker-compose.yml` and
`InventoryServiceApplication.java` are complete. Read them anyway.

---

## Checkpoints — stop and verify at each one

**Checkpoint A — after step 3 (repository).**
Same trick as Phase 1: temporarily add a `CommandLineRunner` bean that prints
`repo.count()`. App starts + `flyway_schema_history` exists → schema, entity
and repository agree. Delete the bean afterward.
If it fails with `wrong column type` on `version`: `BIGINT` in SQL ↔ `Long` in Java.

**Checkpoint B — after step 8 (controller).**
Run requests **1, 4, 6, 7** in `requests.http`. Expect 201, 200, 200, 200.
After request 7, look in the database:
```sql
SELECT product_id, available_qty, reserved_qty, version FROM inventory_items;
```
`version` must be **1**. If it is still 0, the UPDATE never happened. Check
dirty checking and `@Transactional`.

**Checkpoint C — after step 9 (exception handler).**
Run requests **2, 3, 5, 8, 9, 10**. Expect **400, 409, 404, 409 (or 422), 400, 404**.
Each should return a clean `ApiError`, with no stack traces and no 500s.

**Checkpoint D — the race (this is the one that matters).**
1. Turn on bind logging in `application.yml`
   (`org.hibernate.orm.jdbc.bind: TRACE`) and restart.
2. Run request **11** to create product 2 with exactly **one** unit.
3. From a terminal, fire 10 reserves at the same instant:
   ```bash
   for i in $(seq 1 10); do
     curl -s -o /dev/null -w "%{http_code}\n" -X POST localhost:8082/api/inventory/reserve \
       -H 'Content-Type: application/json' -d '{"productId":2,"quantity":1}' &
   done; wait
   ```
4. **Expected:** exactly **one** `200`. Every other request is a `409`.
   In the DB: `available_qty = 0`, `reserved_qty = 1`, `version = 1`.
5. In the logs, find an `UPDATE ... where id=? and version=?`. Find at least one
   `ObjectOptimisticLockingFailureException` logged at WARN. Some of the 409s
   will be *insufficient stock* instead. Those requests read the row *after* the
   winner committed. Make sure you can explain why there are two kinds of 409.
6. **Prove @Version is doing the work.** Comment out the `@Version` annotation,
   recreate the DB (`docker compose down -v && docker compose up -d`), and
   repeat. You may now see **several 200s** while `reserved_qty` is still only 1.
   That is the lost update. Put `@Version` back.

   *Can't reproduce the race?* Requests on localhost are fast. Widen the window
   **temporarily**: add `Thread.sleep(200)` in `reserve()` between the quantity
   check and `item.reserve(...)`. Remove it afterward.

---

## How to inspect the database

```
docker exec -it inventory-postgres psql -U postgres -d inventory_db
```
Then: `\dt`, `\d inventory_items`, `SELECT * FROM inventory_items;`, `\q`.

---

## When you get stuck

- `Connection refused` on 5432 → you are pointing at the wrong port. This DB is on **5433**.
- `Schema-validation: missing column [version]` → the column is missing from V1,
  or you edited V1 after it ran. Fix: `docker compose down -v`, then start again.
- Optimistic-lock failures come back as **500** → there is no handler for
  `ObjectOptimisticLockingFailureException`, or it handles the Hibernate/jakarta
  type instead of the Spring one.
- `version` never increments → you are mutating a detached copy, or the method
  is missing `@Transactional`, so no flush happens.
- `Ambiguous handler methods` / 405 on `/reserve` → check the HTTP method
  annotations on the controller.

---

## Before you come back for review

Answer these in your own words. If you can't, re-read the class comments.

1. Two requests try to reserve the last unit. Walk through what each one reads,
   what SQL each one sends, and which one fails. Do it once without `@Version`
   and once with it.
2. What exception does Hibernate throw, what does Spring turn it into, and
   **when** is it thrown? Why can't you catch it inside the service method?
3. Why is the optimistic-lock failure a 409 and not a 500?
4. Where must a retry live, and why does it need a fresh transaction?
5. Optimistic vs pessimistic locking. When would you switch?
6. Why is `product_id` not a foreign key?
7. Why does `reserve()` not call `save()`?
