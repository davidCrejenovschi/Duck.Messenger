# 🦆 Duck.Messenger


<h2 align="center">📸 App Showcase</h2>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white" alt="Gradle">
</p>

<br>

<table align="center">
  <tr>
    <td align="center" width="50%">
      <b>Registration</b><br><br>
      <img src="https://github.com/user-attachments/assets/1ae4e2e3-e32b-4349-ba38-ba1c1fb609be" width="90%" alt="Login Screen">
    </td>
    <td align="center" width="50%">
      <b>Login</b><br><br>
      <img src="https://github.com/user-attachments/assets/fa593da8-b89a-401b-8c91-8c9039cd2fe4" width="90%" alt="Friends List">
    </td>
  </tr>
  <tr>
    <td colspan="2" align="center">
      <b>Main Chat Interface</b><br><br>
      <img src="https://github.com/user-attachments/assets/72e80ab4-50e4-4ec0-b9b4-8f012e676565" width="95%" alt="Main Chat Window">
    </td>
  </tr>
  <tr>
     <td colspan="2" align="center">
      <b>Conversation View</b><br><br>
      <img src="https://github.com/user-attachments/assets/82e881bf-1d8a-4356-9392-156d25dff4c6" width="95%" alt="Conversation View">
    </td>
  </tr>
  <tr>
     <td colspan="2" align="center">
      <b>Real-Time Notifications</b><br><br>
      <img src="https://github.com/user-attachments/assets/773e87c7-7dc8-4882-92a4-ed1e07114509" width="40%" alt="Notification Toast">
    </td>
  </tr>
</table>>


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
