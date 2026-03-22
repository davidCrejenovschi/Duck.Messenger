# 🦆 Duck.Messenger

<p align="center">
  <img src="[LINK_POZA_PREVIEW_AICI]" alt="Duck.Messenger Preview">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white" alt="Gradle">
</p>

### 📦 Supported Platforms
- **Windows** (via JRE)
- **macOS** (via JRE)
- **Linux** (via JRE)

---

## 📜 Credits & License
This is a **non-commercial, educational project** created for learning purposes.

## 🛠️ Local Development Setup

Follow these steps to configure the source code and the database on your machine.

### 1. Clone the Repository

```bash
git clone [https://github.com/davidCrejenovschi/Duck.Messenger.git](https://github.com/davidCrejenovschi/Duck.Messenger.git)
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
DB_URL=jdbc:postgresql://localhost:5432/duckMan
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
*Alternatively, you can open the project in IntelliJ IDEA or Eclipse and run the main class directly.*
