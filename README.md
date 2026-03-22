# 🦆 Duck.Messenger

<p align="center">
  <img src="[LINK_POZA_PREVIEW_AICI]" alt="Duck.Messenger Preview">
</p>

<img width="963" height="903" alt="Captură de ecran 2026-03-22 220445" src="https://github.com/user-attachments/assets/1ae4e2e3-e32b-4349-ba38-ba1c1fb609be" />
<img width="833" height="826" alt="Captură de ecran 2026-03-22 220548" src="https://github.com/user-attachments/assets/fa593da8-b89a-401b-8c91-8c9039cd2fe4" />
<img width="1914" height="978" alt="Captură de ecran 2026-03-22 220810" src="https://github.com/user-attachments/assets/72e80ab4-50e4-4ec0-b9b4-8f012e676565" />
<img width="730" height="206" alt="Captură de ecran 2026-03-22 220833" src="https://github.com/user-attachments/assets/773e87c7-7dc8-4882-92a4-ed1e07114509" />
<img width="1916" height="979" alt="Captură de ecran 2026-03-22 220944" src="https://github.com/user-attachments/assets/82e881bf-1d8a-4356-9392-156d25dff4c6" />

## 🛠️ Local Development Setup

Follow these steps to configure the source code and the database on your machine.

### 1. Clone the Repository

```bash
git clone https://github.com/davidCrejenovschi/Duck.Messenger.git
```

```bash
cd Duck.Messenger
```

### 2. Configure the Database

**A. Create the Database Structure**
The SQL script required to generate the database schema (tables, relationships, and constraints) is included in the project. You can find it at the following path:
> `src\test\resources\schema.sql`

Execute this script in your PostgreSQL management tool (such as pgAdmin, DataGrip, or DBeaver) to set up the database before running the application.

**B. Set Environment Variables**
For security reasons, database credentials are not tracked in Git. You need to create your own configuration file.

1. Locate the `.env.example` file in the root directory.
2. Create a copy of it and name it `.env`.
3. Fill in your local PostgreSQL credentials:

```env
DB_URL=jdbc:postgresql://localhost:5432/name_of_the_db
DB_USER=your_postgres_username
DB_PASSWORD=your_postgres_password
```

### 3. Build the Project
This project uses Gradle. To build the project and download all necessary dependencies, run the following command in your terminal:

**Windows:**
```bash
gradlew build
```

**macOS / Linux:**
```bash
./gradlew build
```

### 4. Run the Application

**Windows:**
```bash
gradlew run
```

**macOS / Linux:**
```bash
./gradlew run
```
