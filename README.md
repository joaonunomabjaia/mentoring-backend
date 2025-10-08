# Mentoring Backend

This project is a **Micronaut-based backend application** designed to support mentoring processes in healthcare and related domains.  
It provides APIs for managing sessions, mentors, mentees, forms, questions, and reporting, with integration to databases and messaging systems.

---

## 🚀 Features

- Built with **Micronaut 3.8.
- REST API with **JWT authentication**.
- Persistence with **MariaDB** using **JPA & Hibernate**.
- Database migrations with **Liquibase**.
- Background tasks for notifications and AI-assisted summaries.
- Docker-ready for deployment.
- Integration with **Kafka** or **ActiveMQ Artemis** (configurable).
- Caching support.

---

## 📂 Project Structure

```
/backend
 ├── src/main/java/mz/org/fgh/mentoring   # Main source code
 ├── src/main/resources                   # Config files (application.yml, logback.xml, etc.)
 ├── db/liquibase-changelog.xml           # Database migrations
 ├── Dockerfile                           # Docker image build
 └── README.md                            # Project documentation
```

---

## ⚙️ Configuration

Environment variables control sensitive settings such as:

- `MENTORING_DB_HOST`, `MENTORING_DB_PORT`, `MENTORING_DB_USERNAME`, `MENTORING_DB_PASSWORD`
- `JWT_GENERATOR_SIGNATURE_SECRET`
- `MICRONAUT_ENVIRONMENTS` (e.g., `development`, `production`)
- Logging levels set via `ROOT_LOG_LEVEL`

---

## 🐳 Docker

Build and run the image:

```bash
docker build -t mentoring-backend:latest .
docker docker compose up -d
```

You can also use **docker-compose.yml** to orchestrate DB + backend.

---

## 🗄️ Database

- **MariaDB**
- Migrations managed via **Liquibase**
- Ensure schema is initialized before starting the app

---

## 🔒 Security

- Endpoints secured with `@Secured(SecurityRule.IS_AUTHENTICATED)`
- JWT Bearer token required for all API calls

---

## 🧪 Tests

Run tests with:

```bash
./gradlew test
```

---

## 📜 License

This project is licensed under the [MIT License](./LICENSE).

---

## ✨ Authors

- **C-SAÚDE** and contributors  
