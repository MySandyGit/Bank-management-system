# Simple Bank Management System

A beginner-friendly full-stack bank management project.

**Backend:** Java Spring Boot + Spring Security + JWT + MySQL
**Frontend:** React (Vite) + plain CSS + Axios

## Features

- JWT-based login (no sessions, stateless token auth)
- Role-based access: **ADMIN** and **USER**
- Self-registration creates a User + Customer profile + Bank Account automatically
- Deposit / Withdraw money
- Transaction history (deposit/withdraw log)
- Admin CRUD: Create, Read, Update, Delete customers + view any customer's balance/history
- Simple, clean UI

---

## 1. Database setup (MySQL)

You only need to create an empty database — the app creates all the tables for you automatically.

```sql
CREATE DATABASE bank_db;
```

That's it. (A full `database/schema.sql` is included for reference if you'd rather create tables manually.)

Open `backend/src/main/resources/application.properties` and update these if your MySQL setup is different:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bank_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

## 2. Run the backend

Requires Java 17+ and Maven.

```bash
cd backend
```

The API runs on **http://localhost:8080**.

On the very first run, a default admin account is auto-created:

```
username: admin
password: admin123
```

(You can change these in `application.properties` under `app.admin.username` / `app.admin.password`.)

## 3. Run the frontend

Requires Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

The app runs on **http://localhost:5173**.

---

## How to use it

1. Go to http://localhost:5173
2. **As a new customer:** click "Register here", fill in your details — this creates your login, profile, and a savings account with ₹0 balance, all at once.
3. **As admin:** log in with `admin` / `admin123` to see the Admin Panel — view/create/edit/delete any customer, and check anyone's balance or transaction history.
4. As a regular user you can: deposit money, withdraw money (can't go below ₹0), view your transaction history, and edit your profile.

---

## Project structure

```
backend/
  src/main/java/com/bank/
    entity/        -> User, Customer, Account, Transaction, Role
    repository/    -> Spring Data JPA repositories
    dto/           -> Request/response objects
    security/      -> JwtUtil, JwtAuthFilter, CustomUserDetailsService
    config/        -> SecurityConfig (CORS + route protection)
    service/       -> Business logic (Auth, Customer, Account, Transaction, Admin)
    controller/    -> REST endpoints
    exception/     -> Custom exceptions + global error handler

frontend/
  src/
    pages/         -> Login, Register, Dashboard, Deposit, Withdraw, Transactions, Profile, AdminDashboard
    components/     -> Navbar, AccountCard, TransactionTable, PrivateRoute
    services/      -> Axios calls to the backend (Auth, Account, Transaction, Admin)
```

## API endpoints (quick reference)

| Method | Endpoint                              | Access        | Purpose                       |
|--------|----------------------------------------|---------------|-------------------------------|
| POST   | /api/auth/register                     | Public        | Create a new customer account |
| POST   | /api/auth/login                         | Public        | Login, returns JWT token      |
| GET    | /api/customers/me                       | USER/ADMIN    | Get own profile                |
| PUT    | /api/customers/me                       | USER/ADMIN    | Update own profile             |
| GET    | /api/accounts/me                        | USER/ADMIN    | Get own account/balance        |
| POST   | /api/transactions/deposit               | USER/ADMIN    | Deposit money                  |
| POST   | /api/transactions/withdraw              | USER/ADMIN    | Withdraw money                 |
| GET    | /api/transactions/history                | USER/ADMIN    | Own transaction history        |
| GET    | /api/admin/customers                     | ADMIN only    | List all customers (Read)      |
| POST   | /api/admin/customers                     | ADMIN only    | Create a customer (Create)     |
| PUT    | /api/admin/customers/{id}                | ADMIN only    | Update a customer (Update)     |
| DELETE | /api/admin/customers/{id}                | ADMIN only    | Delete a customer (Delete)     |
| GET    | /api/admin/customers/{id}/account         | ADMIN only    | A customer's account/balance   |
| GET    | /api/admin/customers/{id}/transactions    | ADMIN only    | A customer's transaction history |

Every request (except register/login) must include the JWT token in the header:

```
Authorization: Bearer <token>
```

The frontend already does this automatically for you.
