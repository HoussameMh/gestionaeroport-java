# ✈️ Gestion Aéroport - JavaFX Application

![Java](https://img.shields.io/badge/Language-Java-blue)
![JavaFX](https://img.shields.io/badge/UI-JavaFX-orange)
![Maven](https://img.shields.io/badge/Build-Maven-C71A22)
![JDBC](https://img.shields.io/badge/Database-JDBC%20%7C%20H2%20%7C%20MySQL-lightgrey)

A desktop application designed to manage airport operations. Built with Java and JavaFX, it provides an intuitive graphical interface to handle flights, passengers, aircraft, and runways. Data is persisted using JDBC, supporting both H2 (in-memory) and MySQL databases.

---

## 📑 Table of Contents
- [Features](#-features)
- [Project Architecture](#-project-architecture)
- [Database Configuration](#-database-configuration)
- [Prerequisites](#-prerequisites)
- [Installation & Setup](#-installation--setup)
- [Technologies Used](#-technologies-used)

---

## ✨ Features

- **Flight Management (Vols)**: Plan, edit, and track flights and their destinations.
- **Passenger Management (Passagers)**: Register passengers with passport details, baggage weight validation, and flight assignment.
- **Aircraft Management (Avions)**: Track aircraft availability, seating capacity, and weight limits.
- **Runway Management (Pistes)**: Allocate and monitor runways for arriving and departing flights.
- **Custom Exception Handling**: Robust business logic using custom exceptions like `AvionIndisponibleException`, `BagageTropLourdException`, and `VolCompletException`.

---

## 🏗 Project Architecture

The project follows a modular structure leveraging Maven:

```text
📦 gestionaeroport-java
 ┣ 📂 src/main/java/com/example/javafx2
 ┃ ┣ 📂 logic            # Core business logic, domain models (Vol, Passager, etc.) and exceptions
 ┃ ┣ 📜 ...Tab.java      # Controllers for the different JavaFX tabs (Passagers, Vols, etc.)
 ┃ ┣ 📜 AeroportApplication.java # JavaFX Application entry point
 ┃ ┗ 📜 Launcher.java    # Application launcher wrapper
 ┣ 📂 src/main/resources
 ┃ ┣ 📂 com/example/javafx2 # FXML view definitions (`hello-view.fxml`)
 ┃ ┗ 📂 database         # SQL schema and database configuration properties
 ┣ 📜 DATABASE_SETUP.md  # Detailed instructions for JDBC setup
 ┗ 📜 pom.xml            # Maven dependencies and build configuration
```

---

## 🗄 Database Configuration

The application uses **JDBC** for database interaction and uses the **Singleton pattern** to manage connections.

It supports two modes configured in `src/main/resources/database/config.properties`:

### 1. H2 In-Memory Database (Default)
Ideal for testing and development. No installation required. The schema is automatically initialized from `schema.sql` on startup.

```properties
db.url=jdbc:h2:mem:aeroport_db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:database/schema.sql'
db.user=sa
db.password=
db.type=h2
```

### 2. MySQL (Production)
To use MySQL:
1. Create a database `aeroport_db` in MySQL.
2. Execute the `src/main/resources/database/schema.sql` script to create the tables.
3. Update `config.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/aeroport_db?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password
db.type=mysql
```

---

## 🛠 Prerequisites

Ensure you have the following installed to compile and run the application:
- **Java Development Kit (JDK)**: Version 11 or higher (JDK 17 recommended)
- **Maven**: 3.6+
- **MySQL Server** *(Optional, if using MySQL instead of H2)*

---

## 🚀 Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/HoussameMh/gestionaeroport-java.git
   cd gestionaeroport-java
   ```

2. **Clean and compile using Maven**:
   Depending on your OS, you can use the Maven wrapper provided:
   ```bash
   # Windows
   ./mvnw.cmd clean compile

   # macOS / Linux
   ./mvnw clean compile
   ```

3. **Run the Application**:
   You can run the application directly through the Maven JavaFX plugin:
   ```bash
   # Windows
   ./mvnw.cmd javafx:run

   # macOS / Linux
   ./mvnw javafx:run
   ```
   *(Alternatively, run the `Launcher` class directly from your IDE).*

---

## 💻 Technologies Used

- **Java**: Core programming language.
- **JavaFX & FXML**: UI layout and component rendering.
- **Maven**: Dependency and build management.
- **JDBC**: Native Java Database Connectivity.
- **H2 Database**: Lightweight memory SQL database.
- **MySQL**: Relational database for production deployment.
