# Inventory Reservation and Warehouse Allocation API

A transaction-safe, concurrent inventory management API built with Java 21, Spring Boot 3.3.0, and PostgreSQL.

## Features
- Concurrency Control: Uses Pessimistic Locking to prevent overselling under high load.
- Smart Allocation: Distributes reservations across multiple warehouses greedily.
- Transactional Safety: Spring Transactional ensures atomic rollbacks.
- Stock Management: Create products, warehouses, add stock, and cancel reservations.

## Tech Stack
- Language: Java 21
- Framework: Spring Boot 3.3.0
- Database: PostgreSQL 15
- Build Tool: Maven
- Infrastructure: Docker

## How to Run

1. Start Infrastructure:
   docker-compose up -d
   
2. Start the Application:
   mvn spring-boot:run
   
   (API will be available at http://localhost:8100)

## API Endpoints
- POST /api/inventory/products: Create a new product.
- POST /api/inventory/warehouses: Create a new warehouse.
- POST /api/inventory/stock: Add stock for a product in a specific warehouse.
- POST /api/inventory/reserve: Reserve stock (throws 409 Conflict if insufficient).
- POST /api/inventory/cancel: Cancel a reservation and return stock.

## Concurrency Proof
The system implements Pessimistic Write Locking on the inventory query. If two users attempt to reserve the same last item simultaneously, the database forces one transaction to wait. The first transaction deducts the stock and commits; the second transaction sees 0 stock remaining and throws a 409 Conflict.