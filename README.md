# UserHub — Full-Stack Web Application (Spring Boot + ReactJS)

UserHub is a full-stack user management web application built from scratch for the BSIT hands-on laboratory activity. It demonstrates end-to-end communication between a **ReactJS** frontend (powered by Vite), a **Spring Boot** RESTful API backend, and an in-memory **H2 Database**.

---

## 🛠️ Technologies Used

| Layer | Technology | Details |
|---|---|---|
| **Backend** | Java 17+, Spring Boot 3.3.5 | Spring Web, Spring Data JPA, Spring Validation (Jakarta) |
| **Database** | H2 Database (In-Memory) | Embedded SQL engine with browser console enabled |
| **Security** | Custom Token Session Filter | SHA-256 cryptographic password hashing, Bearer UUID tokens |
| **Frontend** | React 18, Vite | React Router v6, Axios HTTP client, modern responsive CSS |
| **Architecture** | Layered Architecture | Entity, Repository, DTO, Service, Controller, Exception |

---

## 📋 System Requirements and Prerequisites

Before running the application, ensure the following software is installed on your machine:

- **Java Development Kit (JDK):** Version 17, 21, or newer (`java -version`)
- **Node.js & npm:** Node.js LTS (v18+) and npm (`node -v`, `npm -v`)
- **Git:** Version 2.x+ (`git --version`)
- **Available Ports:**
  - `8080`: Spring Boot REST API & H2 Console
  - `5173`: ReactJS Vite Development Server

---

## 📁 Project Directory Structure

```
Samarinta-Roxette-First-Web-App/
├── backend/
│   ├── mvnw / mvnw.cmd              # Maven wrapper scripts
│   ├── pom.xml                      # Spring Boot Maven dependencies & build config
│   └── src/main/
│       ├── java/edu/bsit/samarinta/
│       │   ├── UserhubApplication.java
│       │   ├── config/              # DataSeeder (seeds default ADMIN user)
│       │   ├── controller/          # AuthController, DashboardController, UserController, AdminController
│       │   ├── dto/                 # RegisterRequest, LoginRequest, UserResponse, LoginResponse, DashboardResponse, UpdateProfileRequest
│       │   ├── exception/           # AuthException, ForbiddenException, ApiExceptionHandler
│       │   ├── model/               # User JPA Entity (mapped to table "users")
│       │   ├── repository/          # UserRepository (Spring Data JPA)
│       │   └── service/             # UserService (business logic, password hashing, token sessions)
│       └── resources/
│           └── application.properties # H2 & server port configuration
│
├── frontend/
│   ├── package.json                 # React, Vite, Axios, React Router dependencies
│   ├── vite.config.js               # Vite development server configuration
│   └── src/
│       ├── api/                     # client.js (Axios instance + auth Bearer interceptor)
│       ├── components/              # Navbar.jsx, ProtectedRoute.jsx
│       ├── context/                 # AuthContext.jsx (authentication state management)
│       ├── pages/                   # Register.jsx, Login.jsx, Dashboard.jsx, AdminUsers.jsx, EditProfile.jsx
│       ├── App.jsx                  # Route definitions
│       ├── index.css                # Polished responsive styling
│       └── main.jsx                 # Entry point
│
├── docs/
│   ├── REQUIREMENTS.md              # Feature specification & implementation matrix
│   └── screenshots/                 # Application proof screenshots (11 images)
│
└── README.md                        # Project documentation
```

---

## 🚀 Setup & Execution Instructions

Follow the steps below to run both the backend and frontend simultaneously.

### 1. Start the Spring Boot Backend

Open your terminal or command prompt and run:

```bash
cd backend
# On Windows:
.\mvnw.cmd spring-boot:run

# On macOS/Linux:
./mvnw spring-boot:run
```

- The API will start at **`http://localhost:8080`**.
- Upon startup, `DataSeeder` automatically initializes:
  - Default **ADMIN** account: `admin@userhub.com` / `Admin@123`
  - Demo **USER** account: `ana@bsit.edu` / `password123`

---

### 2. Start the ReactJS Frontend

Open a **separate terminal window** and run:

```bash
cd frontend
npm install
npm run dev
```

- The frontend will start at **`http://localhost:5173`**.

> **Note:** Always keep both terminals running simultaneously. The frontend connects to the backend API at `http://localhost:8080/api`.

---

## 🌐 Application URLs and Ports

| Service | URL | Notes |
|---|---|---|
| **Frontend UI** | [http://localhost:5173](http://localhost:5173) | Main React Web Application |
| **Backend REST API** | [http://localhost:8080/api](http://localhost:8080/api) | Base REST API endpoint |
| **H2 Web Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | In-memory database inspector |

### H2 Console Login Credentials:
- **JDBC URL:** `jdbc:h2:mem:userhubdb`
- **User Name:** `sa`
- **Password:** *(leave blank)*

---

## 🔑 Pre-Configured Test Accounts

| Account Role | Email Address | Password | Permissions |
|---|---|---|---|
| **ADMIN (Seeded)** | `admin@userhub.com` | `Admin@123` | Full access: View all users, dashboard, edit profile |
| **USER (Demo)** | `ana@bsit.edu` | `password123` | Standard access: Dashboard, edit profile |
| **New USER** | Register at `/register` | Custom (min 8 chars) | Standard user access |

---

## 🧪 Testing the Application End-to-End

### Guided User Workflow (Browser)

1. **User Registration:**
   - Navigate to [http://localhost:5173/register](http://localhost:5173/register).
   - Try submitting with a password shorter than 8 characters to observe validation feedback.
   - Register a new account (e.g., `Juan Dela Cruz`, `juan@example.com`, `password123`).
   - Try registering again with the exact same email to confirm the duplicate email error (`409 Conflict`).
2. **User Login:**
   - Log in using your new credentials at [http://localhost:5173/login](http://localhost:5173/login).
   - On success, you will be redirected to the **Protected User Dashboard**.
3. **Protected Dashboard:**
   - Confirm your name, role badge (`USER`), member date, and total registered users count are displayed.
4. **Edit My Profile:**
   - Click **"Edit my profile"** or open [http://localhost:5173/profile](http://localhost:5173/profile).
   - Change your full name and save.
   - Notice the success message and verify your updated name reflects in both the dashboard and the navbar.
5. **Role-Based Authorization Gate:**
   - As a normal `USER`, attempt to open [http://localhost:5173/admin/users](http://localhost:5173/admin/users).
   - Notice that the client-side route guard (`ProtectedRoute`) automatically redirects you back to `/dashboard`.
   - The backend also rejects unauthorized attempts with `403 Forbidden`.
6. **Server-Side Logout:**
   - Click the **"Logout"** button.
   - The application invokes `POST /api/auth/logout`, invalidating your session token on the server, clears `localStorage`, and redirects to `/login`.
7. **Admin Privileges:**
   - Log in as the administrator using `admin@userhub.com` and `Admin@123`.
   - The navbar will show the **"Users"** link and the dashboard displays the **"View all users (Admin)"** button.
   - Open the **Admin Users Directory** ([http://localhost:5173/admin/users](http://localhost:5173/admin/users)) to view all registered users in a styled table.
8. **H2 In-Memory Database:**
   - Visit [http://localhost:8080/h2-console](http://localhost:8080/h2-console).
   - Click **Connect** and run `SELECT * FROM USERS;` to confirm password hashes (SHA-256) are stored and plain-text passwords are never kept.

---

### Command-Line API Testing (cURL / PowerShell)

```bash
# 1. Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Juan Dela Cruz","email":"juan@example.com","password":"password123"}'

# 2. Login to obtain Bearer token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"juan@example.com","password":"password123"}'

# 3. Access protected dashboard using token
curl http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer <TOKEN>"

# 4. Update profile full name
curl -X PUT http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Juan Updated Dela Cruz"}'

# 5. Access admin directory (200 with ADMIN token, 403 with USER token)
curl http://localhost:8080/api/dashboard/users \
  -H "Authorization: Bearer <ADMIN_TOKEN>"

# 6. Invalidate session via server-side logout
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <TOKEN>"
```

---

## 📡 REST API Specification Summary

| Method | Endpoint | Access Level | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Registers a new USER account with validation |
| `POST` | `/api/auth/login` | Public | Authenticates credentials and returns a Bearer session token |
| `POST` | `/api/auth/logout` | Authenticated | Server-side logout; invalidates the active Bearer token |
| `GET` | `/api/dashboard` | Authenticated | Returns personalized user information and platform statistics |
| `GET` | `/api/users/me` | Authenticated | Fetches current user profile |
| `PUT` | `/api/users/me` | Authenticated | Updates the authenticated user's full name |
| `GET` | `/api/dashboard/users` | **ADMIN Only** | Lists all registered accounts in the system |

---

## ⚙️ Additional Configurations & Troubleshooting

- **CORS Configuration:** Enabled via `@CrossOrigin(origins = "http://localhost:5173")` on all Spring Boot controllers.
- **Port Conflicts:** If port `8080` is in use, modify `server.port` in `backend/src/main/resources/application.properties` and update `baseURL` in `frontend/src/api/client.js`.
- **Database Persistence:** The H2 database runs in-memory (`jdbc:h2:mem:userhubdb`). Restarting the Spring Boot server resets the database, automatically re-seeding the default admin and demo user accounts on every startup.
