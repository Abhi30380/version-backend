# Spring Boot Backend - Startup & Neon DB Guide

This guide explains how to configure, connect, and run your Spring Boot application locally with your Neon PostgreSQL database.

---

## 1. Setup Environment Variables (`.env`)

Before starting the application, you must configure your local environment. Open the [.env](file:///c:/Users/ABHISHEK/Desktop/new%20project/version-backend/.env) file in the root of the project and update the placeholders with your credentials:

```env
# Neon DB Database Credentials
SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host-name>/<database-name>?sslmode=require
SPRING_DATASOURCE_USERNAME=<neondb_owner_or_username>
SPRING_DATASOURCE_PASSWORD=<your_actual_password>

# Spring Security Credentials (for API Basic Auth)
SPRING_SECURITY_USER_NAME=abhi
SPRING_SECURITY_PASSWORD=abhi
```

---

## 2. Connecting to Neon DB

Neon PostgreSQL requires **SSL** encryption. When configuring the database URL:
- Always ensure that `?sslmode=require` is appended to your JDBC URL.
- **Example URL:** `jdbc:postgresql://ep-icy-wildflower-a8nl14vy-pooler.eastus2.azure.neon.tech/spring-boot?sslmode=require`
- The application automatically initializes the tables/schema on startup because `spring.jpa.hibernate.ddl-auto` is set to `update` in [application.properties](file:///c:/Users/ABHISHEK/Desktop/new%20project/version-backend/src/main/resources/application.properties).

---

## 3. How to Start the Application Locally

You can run the application using Maven. Ensure the environment variables from `.env` are loaded into your terminal session before starting.

### Option A: Using Helper Run Scripts (Recommended)
Run the script matching your terminal from the project root:
- **PowerShell:** 
  ```powershell
  .\run.ps1
  ```
  *(If blocked by execution policy, run: `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass; .\run.ps1`)*
- **Command Prompt (CMD):** 
  ```cmd
  run.bat
  ```
- **Git Bash / Linux:** 
  ```bash
  chmod +x run.sh && ./run.sh
  ```

### Option B: Manual Startup Commands

#### 1. Windows PowerShell
```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^(?<name>[^#\s=]+)=(?<value>.*)$') {
        [System.Environment]::SetEnvironmentVariable($Matches.name, $Matches.value.Trim(), "Process")
    }
}
mvn spring-boot:run
```

#### 2. Command Prompt (CMD)
```cmd
for /f "usebackq delims=" %x in (.env) do set %x
mvn spring-boot:run
```

#### 3. Git Bash / Linux / macOS
```bash
export $(grep -v '^#' .env | xargs)
mvn spring-boot:run
```

> **Note:** If you don't have Maven (`mvn`) installed globally on your machine, replace `mvn` in the commands above with `.\mvnw.cmd` (on Windows) or `./mvnw` (on Git Bash/Linux).
