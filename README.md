# Product Catalog API

Simple REST API for managing a product catalog.

The application allows creating, listing, updating and deleting products.  
Each product belongs to a producer and may contain a dynamic list of attributes.

The project was created as a backend coding task.

---

# Tech stack

- Java 25
- Spring Boot 4
- Spring Data JPA
- Hibernate
- Liquibase
- H2 database
- Maven
- Lombok
- JUnit + MockMvc

---

# Architecture

The application follows a typical layered architecture:
Controller
↓
Service
↓
Repository
↓
Database


Additional layers:
- DTO for API contracts
- Mapper for converting entities to responses
- Global exception handler

---

# Data model

## Producer

| field | type |
|-----|-----|
| id | Long |
| name | String |

---

## Product

| field | type |
|-----|-----|
| id | Long |
| name | String |
| description | String |
| price | BigDecimal |
| producer | Producer |

---

## ProductAttribute

| field | type |
|-----|-----|
| id | Long |
| product | Product |
| attributeName | String |
| attributeValue | String |

Products can contain an arbitrary number of attributes.

Example:
Product
├─ color: black
├─ memory: 256GB
└─ screen: 6.2"


---

# Database

Database schema is managed using **Liquibase**.

Migrations included:

1. Initial schema for `producer` and `product`
2. `product_attribute` table
3. Sample producer seed data

The application uses **H2 in-memory database** for simplicity.

---

# Running the application

## Requirements

- Java 21+
- Maven

## Start application
mvn spring-boot:run

Application starts at:
http://localhost:8080

---
# H2 database console

Available at:
http://localhost:8080/h2-console

Connection settings:
JDBC URL: jdbc:h2:mem:productdb
User: sa
Password: (empty)

---
# API Endpoints

## Get all products
GET /api/products

---
## Create product
POST /api/products

Example request:
```json
{
  "name": "Galaxy S24",
  "description": "Smartphone",
  "price": 3999.99,
  "producerId": 1,
  "attributes": [
    { "name": "color", "value": "black" },
    { "name": "memory", "value": "256GB" }
  ]
}
```

Update product
PUT /api/products/{id}

Example request:
{
  "name": "Galaxy S24 Ultra",
  "description": "Updated smartphone",
  "price": 4999.99,
  "producerId": 1,
  "attributes": [
    { "name": "color", "value": "silver" },
    { "name": "memory", "value": "512GB" }
  ]
}

Delete product
DELETE /api/products/{id}

Validation
The API includes basic request validation:
Example rules:
product name cannot be empty
price must be positive
producer must exist
Invalid requests return HTTP 400.
Error handling
Global exception handler returns consistent responses.

Examples:
Product not found
{
  "error": "Product not found"
}

Producer not found
{
  "error": "Producer not found"
}

Tests
Integration tests are implemented using:
Spring Boot Test
MockMvc
Test coverage includes:
retrieving product list
creating products
validation errors
missing producer handling
updating products
deleting products

Run tests:
mvn test
Design decisions
Separate attribute table

Product attributes are stored in a separate table:
product_attribute

This allows flexible attributes without schema changes.
DTO layer
Entities are not exposed directly to the API.

DTOs are used for:
request validation
API response control
avoiding lazy loading problems

Liquibase migrations
Liquibase ensures database schema is versioned and reproducible.

Future improvements
Possible improvements:
product search/filtering
pagination
OpenAPI / Swagger documentation
Docker support
PostgreSQL profile
authentication

Author
Piotr Żurawski

