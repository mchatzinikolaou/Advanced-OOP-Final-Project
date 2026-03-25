# Logistics Supply Chain Management System

## Overview
A modern REST API backend system for logistics and supply chain management, built with **Java 17+** and **Spring Boot 3.x**. This project demonstrates:
- **Clean Code** principles
- **Concurrency** and async processing
- **Design Patterns** (Strategy, Factory Method)
- **Stream API** for analytics
- **Layered Architecture** (Controller → Service → Repository)

## Technologies
- **Java 17**
- **Spring Boot 3.2.1** (Web, Data JPA)
- **H2 Database** (in-memory)
- **Maven** (build tool)
- **Jakarta Validation**

### Build and Run
```bash
# Navigate to project directory
cd "C:\Users\Michalis\Desktop\Msc\Lessons\Java\Exc\Final Project"

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Orders
#### Create Order (Async Processing)
```http
POST /orders
Content-Type: application/json

{
  "customerName": "John Doe",
  "weight": 10.0,
  "destination": "Athens",
  "shippingType": "EXPRESS"
}
```

**Response (202 ACCEPTED)**
```json
{
  "orderId": 1,
  "status": "PENDING",
  "message": "Order accepted and is being processed"
}
```

#### Get All Orders
```http
GET /orders
```

#### Get Order by ID
```http
GET /orders/{id}
```

### 2. Vehicles
#### Initialize Fleet (Factory Method)
```http
POST /vehicles/init
```

**Response**: Creates 18 vehicles (3 trucks, 5 vans, 10 drones)

#### Get All Vehicles
```http
GET /vehicles
```

#### Get Vehicle by ID
```http
GET /vehicles/{id}
```

### 3. Analytics
#### Get Analytics (Stream API)
```http
GET /analytics
```

**Response**
```json
{
  "totalRevenue": 375.0,
  "ordersByDestination": {
    "Athens": 5,
    "Thessaloniki": 3,
    "Patras": 2
  },
  "mostExpensiveOrder": {
    "id": 1,
    "customerName": "John Doe",
    "cost": 37.5
  },
  "delayedOrderIds": [3, 7]
}
```
