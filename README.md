# 🍕 Next Pizza – Online Pizza Ordering System

Next Pizza is a **monolithic backend application built with Spring Boot**, allowing users to order pizza online, track their orders, and enabling admins to manage the entire system.
The project follows real business logic and is built on **role-based access control (RBAC)**.

---

## 🚀 Features

### 👤 User
- Register and login with JWT authentication
- Browse and filter pizzas and drinks
- Add products to cart and manage quantities
- Place orders with delivery address
- View order history and track order status
- Cancel orders (only when status is CREATED or COOKING)
- Automatic balance deduction on checkout

### 🧑‍🍳 Admin
- Create, update, and delete pizzas and drinks (with image upload)
- View and manage all orders
- Update order status (CREATED → COOKING → DELIVERING → COMPLETED)

---

## 🏗 Tech Stack

| Technology | Purpose |
|---|---|
| **Java 17** | Programming language |
| **Spring Boot 3.2** | Application framework |
| **Spring Security 6** | Authentication & Authorization |
| **JWT (jjwt)** | Token-based auth (access + refresh) |
| **Spring Data JPA** | ORM / Database access |
| **PostgreSQL** | Relational database |
| **Supabase Storage** | Cloud image storage |
| **Lombok** | Boilerplate reduction |
| **Swagger / OpenAPI** | API documentation |
| **Gradle** | Build tool |
| **Docker** | Containerization |
| **GitHub Actions** | CI/CD pipeline |
| **Railway** | Cloud hosting |

---

## 🗄 Database Schema
```
users        → id, email, password, role, balance, ...
pizzas       → id, name, type, price, image_url, ...
drinks       → id, name, type, volume, price, image_url, ...
cart         → id, user_id, total_price, total_items
cart_items   → id, cart_id, pizza_id, drink_id, quantity
orders       → id, user_id, status, total_price, address
order_items  → id, order_id, product_name, quantity, price
```

---

## 🔐 Security

- **JWT Authentication** — Stateless token-based authentication
- **BCrypt** — Password encryption (strength: 10)
- **Role-based access control** — USER / ADMIN
- **Soft Delete** — Records are never physically deleted (`is_active = false`)

---

## 📡 API Endpoints

### Auth
```
POST /api/auth/register   → Register new user
POST /api/auth/login      → Login and receive JWT tokens
POST /api/auth/refresh    → Refresh access token
```

### Pizza
```
GET    /api/pizza                → Get all pizzas
GET    /api/pizza/{id}           → Get pizza by ID
GET    /api/pizza/type/{type}    → Filter by type
POST   /api/pizza/create-pizza   → Create pizza (ADMIN)
PUT    /api/pizza/update/{id}    → Update pizza (ADMIN)
DELETE /api/pizza/delete/{id}    → Delete pizza (ADMIN)
```

### Cart
```
GET    /api/cart              → Get user cart
POST   /api/cart/add-pizza    → Add pizza to cart
POST   /api/cart/add-drink    → Add drink to cart
PUT    /api/cart/item/{id}    → Update item quantity
DELETE /api/cart/item/{id}    → Remove item from cart
DELETE /api/cart/clear        → Clear entire cart
```

### Orders
```
POST /api/orders                      → Place an order
GET  /api/orders/my-orders            → Get my orders
GET  /api/orders/{id}                 → Get order details
PUT  /api/orders/{id}/cancel          → Cancel order
GET  /api/orders/admin/all            → Get all orders (ADMIN)
PUT  /api/orders/admin/{id}/status    → Update order status (ADMIN)
```

---

## 🔄 Business Logic

### Order Creation:
```
1. Check if cart exists
2. Check if cart is not empty
3. Check if user balance is sufficient
4. Create order entity
5. Copy CartItems → OrderItems (price snapshot)
6. Deduct balance from user
7. Clear the cart
```

### Order Cancellation:
```
1. Status must be CREATED or COOKING
2. Update status → CANCELLED
3. Refund full amount to user balance
```

---

## 🐳 Docker & CI/CD
```bash
# Run locally
docker compose up --build
```

**CI/CD Pipeline:**
```
git push (master branch)
        ↓
GitHub Actions
  - Build with Gradle
  - Build Docker image
  - Push to Docker Hub
        ↓
Railway pulls latest image
        ↓
Auto redeploy
```

---

## ⚙️ Environment Variables
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/dbname
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your-secret-key-min-256-bits
SUPABASE_URL=https://xxx.supabase.co
SUPABASE_SECRET_KEY=your-supabase-service-role-key
SUPABASE_BUCKET=nextpizza-uploads
```

---

## 🌍 Live Demo
```
Backend API:  https://nextpizza-backend-production.up.railway.app
Swagger UI:   https://nextpizza-backend-production.up.railway.app/swagger-ui.html
Frontend:     https://nextpizza-frontend-production.up.railway.app
```

---

## 👨‍💻 Author

**Javohir** – Java Backend Developer

- GitHub: [@Javohir004](https://github.com/Javohir004)
