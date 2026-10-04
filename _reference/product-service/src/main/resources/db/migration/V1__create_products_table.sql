-- ============================================================
-- FLYWAY MIGRATION V1 — you write the SQL.
-- ============================================================
-- HOW FLYWAY WORKS:
--   On startup it scans db/migration, finds files named V<n>__<description>.sql,
--   and runs any that are not yet recorded in the flyway_schema_history table.
--   Each file runs exactly once, in version order, and is then frozen forever.
--
-- THE RULE THAT MATTERS:
--   Never edit a migration that has already run anywhere. Flyway stores a
--   checksum; changing the file makes startup fail. To change the schema you
--   add V2__whatever.sql. This is the whole point — the schema has a history
--   that matches your Git history.
--
-- WHY NOT JUST ddl-auto: update?
--   Because it never drops or renames anything, it produces different results
--   on different machines, and it can silently lock a large table in
--   production. Interviewers notice when you use migrations instead.
--
-- ------------------------------------------------------------
-- YOUR TASK: write the CREATE TABLE. It must match your Product entity
-- exactly, or `ddl-auto: validate` will refuse to start the app.
-- ------------------------------------------------------------
--   Table: products
--     id           bigint, auto-generated, primary key
--                  (Postgres syntax:  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY)
--     name         VARCHAR(200)  NOT NULL
--     description  VARCHAR(1000)
--     price        NUMERIC(12,2) NOT NULL
--     stock_qty    INTEGER       NOT NULL
--     created_at   TIMESTAMPTZ   NOT NULL
--     updated_at   TIMESTAMPTZ
--
--   Then add, on separate statements:
--     - a UNIQUE constraint on name
--         (this is the REAL enforcement behind your existsByName check —
--          the Java check is only for a friendly error message)
--     - a CHECK constraint that price > 0
--     - a CHECK constraint that stock_qty >= 0
--     - an index on name, since you query with LIKE on it
--
-- THINK ABOUT IT:
--   - Why TIMESTAMPTZ rather than TIMESTAMP?
--   - Your CHECK constraint duplicates the DTO's @DecimalMin. Is that bad?
--     (No — validate at the edge for good errors, constrain in the database for
--      guarantees. Different jobs. Be able to say that.)

CREATE TABLE products (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(200)  NOT NULL,
    description VARCHAR(1000),
    price       NUMERIC(12,2) NOT NULL,
    stock_qty   INTEGER       NOT NULL,
    created_at  TIMESTAMPTZ   NOT NULL,
    updated_at  TIMESTAMPTZ
);
