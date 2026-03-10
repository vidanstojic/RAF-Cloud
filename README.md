# RAF Cloud - Virtual Machine Management System
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-AOP_Reflection-blue?style=for-the-badge)

RAF Cloud is a full-stack web application developed as a simulation of a cloud infrastructure platform. The system allows users to provision, control, and schedule operations on virtual machines through a secure, permission-based environment.

---

## Key Features

### Advanced Security and User Management
* **Custom Security Framework:** Implemented authentication and authorization using JWT and Reflection/AOP, specifically avoiding built-in solutions like Spring Security to ensure a custom-tailored architecture.
* **Granular RBAC:** A robust Role-Based Access Control system where every action (Create, Read, Update, Delete) for both users and machines is governed by specific permissions.
* **Secure Data Handling:** User passwords are encrypted, and email addresses serve as unique system identifiers.
* **Permission-Driven UI:** The frontend dynamically adapts, hiding or disabling features based on the logged-in user's specific rights.

### Machine Lifecycle and Real-time Control
* **Asynchronous Operations:** The Start, Stop, and Restart commands are non-blocking. The server responds immediately with a 2XX status, while the actual operation runs in the background with simulated execution times of 10+ seconds.
* **WebSocket Integration:** Utilizes WebSockets to push real-time status updates from the server to the client as soon as a background operation completes.
* **Machine Search:** A comprehensive search engine allowing users to filter active machines by name, status, and creation date ranges.
* **Soft Deletion:** The Destroy operation implements soft delete logic, marking machines as inactive rather than removing them from the database.

### Scheduling and Error Tracking
* **Operation Scheduling:** Users can schedule Start, Stop, or Restart actions for specific future timestamps.
* **Automated Execution:** An autonomous background service attempts to execute these tasks at the designated time, provided the machine is in the required state.
* **Error Logging:** A dedicated history system tracks failed scheduled operations, logging the date, machine ID, attempted operation, and the specific cause of failure.

---

## Tech Stack

* **Frontend:** Angular 16
* **Backend:** Spring Framework
* **Database:** Relational Database (SQL)
* **Communication:** REST API and WebSockets
* **Security:** JSON Web Tokens (JWT) and Custom AOP-based Authorization

---

## Architecture Details

### The Restart Logic
Unlike simple status toggles, the Restart operation is a multi-stage process. After half of the execution time passes, the machine transitions to a Stopped state, and upon completion, it automatically moves back to Running.

### Admin vs. User Views
The system maintains strict data isolation. Standard users can only view and manage machines they created. Administrators, however, have full visibility across all machines and system-wide error logs.

---
*Developed as part of the academic curriculum for the Computer Science Faculty (RAF).*
