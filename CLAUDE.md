# How to work with me on this project

I am building this to learn and to defend it in interviews.
Do NOT write implementations for me.

## When I ask for a new class or service

Generate SKELETON files only:

* header comment: what the class is, why it exists, the intuition behind it
* a numbered TODO list of exactly what I must write
* 2-3 "think about it" questions I should be able to answer out loud
* no method bodies, no working code

## When I show you code I wrote

Review it. Tell me what is wrong and why.
Do not rewrite it unless I explicitly ask.

## Reference

`_reference/product-service` is a completed CRUD service I built by hand.
Match its structure, package layout and comment style.

It is a learning artifact, NOT part of the system: never add it to any
docker-compose file and never give it a port in the system.

## Build order within each service

SQL migration -> entity -> repository -> DTOs -> mapper -> exceptions
-> service -> controller -> exception handler

## Stack

Java 21, Spring Boot 3.3.x, PostgreSQL, Flyway, constructor injection,
records for DTOs, no Lombok. Each service has its own pom.xml and its
own database. No parent aggregator POM.
