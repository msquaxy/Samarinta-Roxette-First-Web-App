# UserHub: Requirements & Feature Specification

This document details all implemented features, specifications, endpoints, expected behaviors, and verification results for **UserHub**, developed as part of the BSIT Full-Stack Web App hands-on laboratory activity following the presentation *"Your First Full-Stack Web App: Spring Boot + ReactJS"*.

---

## 📋 Features Overview & Implementation Matrix

| # | Feature Name | Layer | Relevant Endpoint | Status |
|---|---|---|---|:---:|
| 1 | Layered Architecture & H2 Database | Backend | `jdbc:h2:mem:userhubdb` | ✅ Implemented |
| 2 | User Registration with Validation | Full-Stack | `POST /api/auth/register` | ✅ Implemented |
| 3 | User Authentication & Password Hashing | Full-Stack | `POST /api/auth/login` | ✅ Implemented |
| 4 | Token-Based Authentication | Full-Stack | `Authorization: Bearer <token>` | ✅ Implemented |
| 5 | Role-Based Authorization (USER & ADMIN) | Full-Stack | Role checks on all protected endpoints | ✅ Implemented |
| 6 | Protected User Dashboard | Full-Stack | `GET /api/dashboard` | ✅ Implemented |
| 7 | Global Exception Handling & HTTP Status Codes | Backend | Global `@RestControllerAdvice` | ✅ Implemented |
| 8 | Protected Frontend Routing | Frontend | `ProtectedRoute.jsx` via React Router | ✅ Implemented |
| 9 | Seed an ADMIN User (10 pts) | Backend | Seeded via `CommandLineRunner` at startup | ✅ Implemented |
| 10 | Admin Users Directory Page (15 pts) | Full-Stack | `GET /api/dashboard/users` | ✅ Implemented |
| 11 | Server-Side Logout (10 pts) | Full-Stack | `POST /api/auth/logout` | ✅ Implemented |
| 12 | Edit My Profile (15 pts) | Full-Stack | `PUT /api/users/me` | ✅ Implemented |

---

## 🔍 Detailed Feature Specifications

### 1. Layered Architecture & Database Persistence
- **Description:** Clean separation of concerns following industry standards across Entity, Repository, DTO, Service, Controller, and Exception layers.
- **Expected Behavior:** Domain entity `User` mapped to table `users` (avoiding H2 SQL reserved keyword `user`). Spring Data JPA handles all database operations automatically without requiring raw SQL queries. In-memory H2 database persists data during application runtime.
- **Database Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:userhubdb`, User: `sa`, Password: empty).
- **Status:** ✅ Implemented

### 2. User Registration with Input Validation
- **Description:** Allows visitors to create a standard `USER` account.
- **Expected Behavior:** Validates incoming payloads: `fullName` (required/not blank), `email` (valid email format, not blank), `password` (minimum 8 characters). Reject duplicate email registrations with `409 Conflict`. Reject malformed or invalid inputs with `400 Bad Request`. On successful creation, returns `201 Created` with user details (excluding password) and redirects the user to the login screen.
- **Endpoint:** `POST /api/auth/register`
- **Status:** ✅ Implemented

### 3. User Authentication & Password Hashing
- **Description:** Secure credential verification without exposing plain-text passwords.
- **Expected Behavior:** Passwords are never stored in plain text. Passwords are securely hashed using cryptographic SHA-256 with hex encoding upon registration. During authentication, the hashed candidate is verified against the database hash. Invalid email or password returns `401 Unauthorized` with a generic `"Invalid credentials"` message to prevent credential enumeration.
- **Endpoint:** `POST /api/auth/login`
- **Status:** ✅ Implemented

### 4. Token-Based Authentication
- **Description:** Stateless token issuance and session tracking.
- **Expected Behavior:** Upon successful login, the server generates a random UUID auth token, records the session mapping in memory, and returns `{ "token": "...", "user": { ... } }`. The React application stores the token in `localStorage`. An Axios request interceptor automatically attaches the header `Authorization: Bearer <token>` to all outgoing HTTP requests.
- **Endpoint:** `POST /api/auth/login`
- **Status:** ✅ Implemented

### 5. Role-Based Authorization (`USER` and `ADMIN`)
- **Description:** Restricts access to sensitive routes and API endpoints according to the user's role.
- **Expected Behavior:** Standard `USER` accounts can access personal dashboard and profile editing. The user directory (`GET /api/dashboard/users`) requires the `ADMIN` role. If a `USER` calls an admin endpoint, the server returns `403 Forbidden`. If an unauthenticated client calls a protected endpoint, the server returns `401 Unauthorized`.
- **Enforcement:** `UserService.requireRole(user, "ADMIN")` and `ProtectedRoute.jsx`.
- **Status:** ✅ Implemented

### 6. Protected User Dashboard
- **Description:** A personalized user hub rendered only for logged-in accounts.
- **Expected Behavior:** Authenticated users view their full name, email, role badge (`USER` or `ADMIN`), registration date (`memberSince`), and the total number of registered platform users (`totalUsers`). If unauthenticated, access is denied and redirected to `/login`.
- **Endpoint:** `GET /api/dashboard`
- **Status:** ✅ Implemented

### 7. Global Exception Handling & HTTP Status Responses
- **Description:** Centralized REST exception handler providing standardized JSON responses.
- **Expected Behavior:** Maps Java runtime exceptions to standard HTTP error codes:
  - `400 Bad Request`: Validation failures (`MethodArgumentNotValidException`)
  - `401 Unauthorized`: Missing, invalid, or expired authentication token (`AuthException`)
  - `403 Forbidden`: Role permission denied (`ForbiddenException`)
  - `409 Conflict`: Duplicate email registration (`IllegalArgumentException`)
- **Status:** ✅ Implemented

### 8. Frontend Navigation & Protected Routes
- **Description:** Single-Page Application (SPA) routing with client-side guards using React Router v6.
- **Expected Behavior:** Accessible public routes: `/login`, `/register`. Protected routes: `/dashboard`, `/profile`, `/admin/users`. Navigating to a protected page without an active session immediately redirects to `/login`. Non-admin accounts attempting to access `/admin/users` are redirected to `/dashboard`.
- **Status:** ✅ Implemented

---

## 🌟 Additional Features (50 Points Total)

### 9. Seed an ADMIN User (10 Points)
- **Description:** Automatically populates a default administrator account on server startup.
- **Implementation:** Implemented using Spring Boot `CommandLineRunner` in `edu.bsit.samarinta.config.DataSeeder`.
- **Default Credentials:**
  - **Email:** `admin@userhub.com`
  - **Password:** `Admin@123`
  - **Role:** `ADMIN`
- **Demo User Credentials (Slide 24):**
  - **Email:** `ana@bsit.edu`
  - **Password:** `password123`
  - **Role:** `USER`
- **Status:** ✅ Implemented

### 10. Admin Users Page (15 Points)
- **Description:** Administrator-only user management directory displaying all registered accounts.
- **Expected Behavior:** Accessible only to users with `role="ADMIN"`. Renders a styled table showing user ID, full name, email address, and role badge. Passwords and internal security tokens are never exposed.
- **Endpoint:** `GET /api/dashboard/users` (also aliased at `GET /api/admin/users`)
- **Status:** ✅ Implemented

### 11. Server-Side Logout (10 Points)
- **Description:** Explicit server-side session invalidation endpoint.
- **Expected Behavior:** When the user clicks the "Logout" button, React invokes `POST /api/auth/logout` with the Bearer token. The backend removes the token from its active sessions map. Subsequent requests using that invalidated token are rejected with `401 Unauthorized` (`"Session expired"` or `"Please log in"`). The client then clears `localStorage` and redirects to `/login`.
- **Endpoint:** `POST /api/auth/logout`
- **Status:** ✅ Implemented

### 12. Edit My Profile (15 Points)
- **Description:** Authenticated profile modification feature allowing users to update their full name.
- **Expected Behavior:** The edit profile page (`/profile`) displays the user's read-only email address and an editable full name input. Submitting the form issues a `PUT` request with `{ "fullName": "..." }`. The database is updated, and the new name immediately reflects in the dashboard, navbar, and session store.
- **Endpoint:** `PUT /api/users/me` (also aliased at `PUT /api/user/profile`)
- **Status:** ✅ Implemented

---

## 📸 Captured Verification Screenshots

High-resolution screenshots demonstrating each working feature are located in `docs/screenshots/`:

1. `01_registration_page.png` — User registration form with validation rules.
2. `02_registration_validation_error.png` — Duplicate email rejection (409 Conflict) and input validation feedback.
3. `03_login_page.png` — Authentication login interface.
4. `04_user_dashboard.png` — Protected user dashboard showing welcome banner, user stats, role badge, and total registered users.
5. `05_edit_profile_page.png` — Profile management page displaying read-only email and editable full name.
6. `06_profile_updated_success.png` — Successful update notification on profile save.
7. `07_dashboard_updated_name.png` — Dashboard dynamically reflecting the updated full name.
8. `08_server_side_logout_success.png` — Post-logout redirect confirming server session termination.
9. `09_admin_dashboard.png` — Admin dashboard displaying administrative privileges and "View all users" navigation.
10. `10_admin_users_page.png` — Admin-only registered user directory table.
11. `11_h2_database_console.png` — H2 database web console interface.
