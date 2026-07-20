# 🔗 URL Shortener Platform

A full-stack URL Shortener application built using **Spring Boot**, **React**, **MySQL**, and **JWT Authentication**. The platform allows users to securely create, manage, and track shortened URLs through an intuitive dashboard with click analytics.

---

## 🚀 Features

* 🔐 JWT-based User Authentication
* 👤 User Registration & Login
* 🔗 Generate Short URLs
* ↪️ Redirect Short URLs to Original Links
* 📊 URL Click Analytics
* 📁 View All URLs Created by a User
* 🛡️ Spring Security Role-Based Authorization
* 💾 MySQL Database Integration
* ⚡ RESTful API Architecture

---

## 🛠 Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* JWT Authentication
* Maven
* MySQL

### Frontend

* React
* Vite
* JavaScript
* Tailwind CSS
* Axios

---

## 📂 Project Structure

```text
URL-shortener/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
│
├── frontend/
│   ├── src/
│   ├── public/
│   └── package.json
│
├── screenshots/
│
├── docs/
│
└── README.md
```

---

## ⚙️ Backend Architecture

```text
Controller
      │
      ▼
Service Layer
      │
      ▼
Repository Layer
      │
      ▼
MySQL Database
```

The backend follows a layered architecture to keep business logic, data access, and REST APIs separated for better maintainability.

---

## 🔑 Authentication Flow

1. User registers an account.
2. User logs in with email/username and password.
3. JWT token is generated.
4. Token is sent with every protected request.
5. Spring Security validates the token before allowing access.

---

## 📡 REST API

### Authentication

| Method | Endpoint                    |
| ------ | --------------------------- |
| POST   | `/api/auth/public/register` |
| POST   | `/api/auth/public/login`    |

### URL Management

| Method | Endpoint            |
| ------ | ------------------- |
| POST   | `/api/urls/shorten` |
| GET    | `/api/urls/myurls`  |
| GET    | `/{shortCode}`      |

---

## 🗄 Database

Main entities:

* User
* URL Mapping
* Click Event

Relationships:

* One User → Many URLs
* One URL → Many Click Events

---

## 📸 Screenshots

> Add screenshots inside the `screenshots/` folder.

* Login Page
* Registration Page
* Dashboard
* URL Management
* Analytics

---

## ▶️ Getting Started

### Clone Repository

```bash
git clone https://github.com/Neerajmartial/URL-shortner.git
```

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

---

## ⚙️ Configuration

Configure the following before running the project:

* MySQL Database
* JWT Secret
* Database Username
* Database Password

Store sensitive values using environment variables or an `.env` file instead of committing them to GitHub.

---

## 🚀 Future Improvements

* QR Code Generation
* Custom Alias Support
* URL Expiration
* Password Protected Links
* Redis Caching
* Docker Deployment
* Swagger/OpenAPI Documentation
* CI/CD with GitHub Actions
* Rate Limiting
* Email Verification

---

## 👨‍💻 Author

**Nandyala Neeraj Lochan Reddy**

If you found this project useful, feel free to ⭐ the repository.
