# Simple CRUD Online Shop Using Spring Boot + Jasper Report + MinIO and Angular + Tailwind CSS

A simple CRUD online shop web application developed using Spring Boot for the backend and Angular for the frontend. The application allows users to manage customers, items, and orders in an online shop environment.

## Features
- Customer Management: Add, view, edit, and delete customer details.
- Item Management: Add, view, edit, and delete items.
- Order Management: Create, view, and manage orders.

The project is divided into two main parts:

## Backend (Spring Boot)
The backend handles the business logic, data persistence, and API endpoints.
Main Technologies: Spring Boot, Jasper Report, MinIO
Key Components:
- Controllers: Handle incoming HTTP requests and map them to the appropriate service methods.
- Services: Contain business logic and interact with repositories.
- Repositories: Interact with the database to perform CRUD operations.
- DTOs (Data Transfer Objects): Used to transfer data between the frontend and backend.
- Exception Handling: Handles errors and exceptions globally.

## Frontend (Angular)
The frontend is responsible for the user interface and client-side logic.
Main Technologies: Angular, Tailwind CSS
Key Components:
- Components: Modular UI elements like customer management, item management, etc.
- Routing: Manages the navigation between different pages in the application.
- Styles: Global and component-specific styles.

## Getting Started
### Prerequisites
- Java 17 or higher
- Node.js and npm
- Angular CLI (Optional for frontend development)
### Backend Setup
1. Configure all the credentials for database and minio server in application.properties
2. Navigate to the backend directory:
```console
cd Backend
```
3. Build the Spring Boot application:
```console
mvn clean install
```
4. Run the application:
```console
mvn spring-boot:run
```
The backend will be accessible at http://localhost:8080.
### Frontend Setup
1. Navigate to the frontend directory:
2. Install the dependencies:
```console
npm install
```
3. Run the Angular application:
```console
npm start
```
The frontend will be accessible at http://localhost:4200.
