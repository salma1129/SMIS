# Student Management Information System (SMIS)

## 📘 Project Overview
The **Student Management Information System (SMIS)** is a Jakarta EE web application developed for managing university academic data.
It allows administrators to manage students, courses, enrollments, and grades, while students can view their academic information.

This project is inspired by real-world university systems such as those used at the **University of Sousse**.

---

## 🛠️ Technologies Used

### Backend
- Jakarta EE
- Apache TomEE 10.1.2
- Servlets (authentication, session management)
- JSF (Jakarta Faces)
- JPA (Jakarta Persistence)
- REST API (JSON)
- Maven

### Frontend
- JSF (XHTML)
- Bootstrap 5
- EL & JSTL

### Database
- MySQL 8+
- JPA Entities (Student, Course, Enrollment)

---

## 📂 Project Structure

SMIS/
├── src/main/java/
│   ├── servlet/
│   ├── beans/
│   ├── entities/
├── src/main/webapp/
│   ├── WEB-INF/
│   ├── login.xhtml
│   ├── dashboard.xhtml
│   ├── students.xhtml
│   ├── courses.xhtml
│   ├── enrollments.xhtml
│   └── grades.xhtml
├── src/main/resources/
│   └── META-INF/persistence.xml
├── pom.xml
└── README.md

---

## 🔐 Functional Features
- Login & Logout with session management
- Admin CRUD for students and courses
- Student enrollment & grade viewing
- REST API for external access

---

## ⚙️ Setup Instructions

### Database
CREATE DATABASE smis;
CREATE USER 'smis_user'@'localhost' IDENTIFIED BY 'smis_pass123';
GRANT ALL PRIVILEGES ON smis.* TO 'smis_user'@'localhost';
FLUSH PRIVILEGES;

### TomEE
Configure DataSource in tomee.xml and copy MySQL Connector/J into TOMEE_HOME/lib.

### Build & Deploy
mvn clean package
Copy target/SMIS.war into TOMEE_HOME/webapps and start TomEE.

---

## 🚀 Access
http://localhost:8081/SMIS_war/login
---

## 👨‍🎓 Author
Student Management Information System (SMIS)
Jakarta EE University Project
