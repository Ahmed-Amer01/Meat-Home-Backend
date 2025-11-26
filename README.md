# Meat Home Backend

You're looking at the Meat Home Backend — a Spring Boot REST API that powers products, orders, customers, reviews and admin features for the Meat Home application.

Quick glance
- Tech: Java 21, Spring Boot 3.5, Spring Data JPA, Spring Security, MySQL.
- What this repo contains: the API implementation and `Sprint2_Meat_Home_API.json` (API spec).

Table of contents
- What is implemented
- Project architecture
- Package overview (where to look)
- Database schema (tables & relations)
- API endpoints (high-level)
- Assumptions & notes
- Contributing, support & license

What is implemented
- Customer management (registration, profile, addresses, password reset)
- Authentication & authorization (JWT-based flows / Spring Security integration)
- Product and Category management (products, categories, stock/pricing)
- Order processing and status history (orders, status changes, order reviews)
- Enquiries and responses (customer enquiries and admin responses)
- Email functionality (SMTP settings present for password reset / notifications)
- Application settings persisted in a `Setting` entity

Project architecture
This repo follows a standard layered architecture:
- Controller layer: REST endpoints that receive requests and return responses.
- Service layer: business logic and transaction boundaries.
- Repository layer: Spring Data JPA repositories that talk to MySQL.
- Domain layer: JPA `entity` classes that model database tables.
- DTOs: request/response objects used by controllers (keeps entities out of API surface).
- Security: Spring Security configuration and JWT utilities.

Package overview (where to look)
- `src/main/java/com/example/meat_home/controller` — endpoint implementations (one per feature area)
- `src/main/java/com/example/meat_home/service` — business logic implementations
- `src/main/java/com/example/meat_home/repository` — Spring Data repositories (interfaces)
- `src/main/java/com/example/meat_home/entity` — JPA entities that map to tables
- `src/main/java/com/example/meat_home/dto` — request/response DTOs
- `src/main/java/com/example/meat_home/security` — security configuration, filters, token handling
- `src/main/java/com/example/meat_home/util` — helpers and utilities
- Entry point: `com.example.meat_home.MeatHomeApplication`

Database schema (tables & relations)
The schema below is inferred from the project's entity names and compiled classes. Use it as a guide — check the `entity` package for exact field names and column types.

Core entities and relationships

- Customer
  - Table: `customer` (or similar)
  - Core fields: `id`, `firstName`/`lastName`, `email` (unique), `password`, `phone`, `createdAt`, `updatedAt`
  - Relationships: One-to-Many -> `Address`; One-to-Many -> `Order`; One-to-Many -> `Enquiry`;
  - Notes: used for authentication and profile data. Password reset tokens reference a `Customer`.

- Address
  - Table: `address`
  - Core fields: `id`, `street`, `city`, `postalCode`, `region` (uses `RegionEnum`), `isDefault`, `createdAt`
  - Relationships: Many-to-One -> `Customer`.

- Product
  - Table: `product`
  - Core fields: `id`, `name`, `description`, `price`, `stockQuantity`, `sku`, `createdAt`, `updatedAt`
  - Relationships: Many-to-One -> `Category`.

- Category
  - Table: `category`
  - Core fields: `id`, `name`, `description`, `createdAt`
  - Relationships: One-to-Many -> `Product`.

- Order
  - Table: `order`
  - Core fields: `id`, `orderNumber`, `totalAmount`, `createdAt`, `updatedAt`, `shippingAddress` (or reference to `Address`)
  - Relationships: Many-to-One -> `Customer`; One-to-Many -> `OrderStatusChange`; One-to-Many -> `OrderReview` (or One-to-One depending on implementation)
  - Notes: order lines/items may be embedded in `Order` or modeled as a separate entity (check `entity` package for `OrderItem` or similar).

- OrderStatusChange
  - Table: `order_status_change`
  - Core fields: `id`, `status` (enum), `changedAt`, `note`
  - Relationships: Many-to-One -> `Order`; tracks the history of order status changes.

- OrderReview
  - Table: `order_review`
  - Core fields: `id`, `rating`, `comment`, `createdAt`
  - Relationships: Many-to-One -> `Order` and/or Many-to-One -> `Product`.

- Enquiry & EnquiryResponse
  - Tables: `enquiry`, `enquiry_response`
  - Enquiry fields: `id`, `subject`, `message`, `status` (enum: `EnquiryStatus`), `createdAt`
  - EnquiryResponse fields: `id`, `response`, `responder` (admin), `createdAt`
  - Relationships: `Enquiry` Many-to-One -> `Customer`; `EnquiryResponse` Many-to-One -> `Enquiry`.

- PasswordResetToken
  - Table: `password_reset_token`
  - Core fields: `id`, `token`, `expiryDate`
  - Relationships: Many-to-One -> `Customer`.

- Setting
  - Table: `setting`
  - Core fields: `id`, `key`, `value`, `createdAt`
  - Purpose: store application-level key/value settings that the app reads at runtime.

Enums and lookup objects
- `RegionEnum` — region constants used by `Address`.
- `EnquiryStatus` — status for enquiries.
- Other enums likely include order-related statuses and role/authority enums.

Notes about relationships and constraints
- Many-to-One relations will typically produce foreign key columns like `customer_id`, `category_id`, `order_id`.
- Uniqueness constraints: `email` on Customer, `sku` on Product are typical — verify exact constraints in the `entity` classes.
- Audit fields: many entities include `createdAt`/`updatedAt` timestamps (check for `@CreatedDate` / `@LastModifiedDate`).

API endpoints (high-level)
The controllers implement the core API surface. Look at `controller` package to find full method-level routes. Example controller-to-feature mapping (open `controller` files for exact routes):
- `AuthController` — login, registration, token refresh
- `CustomerController` — customer CRUD, profile
- `ProfileController` — profile-specific actions
- `AddressController` — address CRUD
- `ProductController` — product listing, details, admin product management
- `CategoryController` — category CRUD
- `OrderController` — place order, view orders, admin order actions
- `OrderReviewController` — create/read order reviews
- `EnquiryController` — create enquiries and list responses
- `AdminController` / `SettingController` — admin actions and application settings

Assumptions & how to confirm them
- The schema above is inferred from entity and compiled class names. For exact column names, types and constraints, open the source files in `src/main/java/com/example/meat_home/entity`.
- If you want a generated ER diagram or SQL DDL, I can produce it by reading entity definitions and translating JPA annotations into DDL statements.

Contributing, support & license
- Follow the code organization; add tests for new business logic.
- Keep secrets out of commits; use environment variables or a secure store.
- This repo does not include a license file. Add `LICENSE` if you plan to publish it.

Want more useful docs?
- I can generate a SQL DDL script that matches the JPA entities.
- I can create an ER diagram (PlantUML or image) derived from the entities.
- I can produce a concise API endpoint list with HTTP methods and paths by scanning controllers.
