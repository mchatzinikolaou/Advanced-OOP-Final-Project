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

## Architecture

### Layered Architecture
```
Controller Layer → Service Layer → Repository Layer → Database
```

### Design Patterns Implemented

#### 1. Strategy Pattern (Shipping Cost Calculation)
Location: `com.logistics.strategy`
- `ShippingStrategy` - Interface
- `StandardShippingStrategy` - Weight × 2.5€
- `ExpressShippingStrategy` - (Weight × 2.5€) + 50% surcharge
- `FreeShippingStrategy` - 0€
- `ShippingStrategyFactory` - Runtime strategy selection

#### 2. Factory Method Pattern (Vehicle Creation)
Location: `com.logistics.factory`
- `VehicleFactory` - Creates vehicles with default characteristics
  - **Truck**: 10 tons capacity, 80 km/h
  - **Van**: 2 tons capacity, 90 km/h
  - **Drone**: 5 kg capacity, 50 km/h

### Async Processing
- Orders are saved with `PENDING` status and returned immediately (< 200ms)
- Heavy processing (inventory check, cost calculation, payment) runs in background thread
- Uses Spring's `@Async` with custom `ThreadPoolTaskExecutor`
- 3-second simulated processing delay to demonstrate non-blocking behavior

### Stream API Analytics
Location: `com.logistics.service.AnalyticsService`
- **Total Revenue**: `reduce()` operation
- **Orders by Destination**: `groupingBy()` operation
- **Most Expensive Order**: `max()` with comparator
- **Delayed Orders**: `filter()` and `map()` operations

## Project Structure
```
src/main/java/com/logistics/
├── LogisticsApplication.java       # Main application
├── config/
│   └── AsyncConfig.java            # Async executor configuration
├── controller/
│   ├── OrderController.java        # Order endpoints
│   ├── VehicleController.java      # Vehicle endpoints
│   └── AnalyticsController.java    # Analytics endpoint
├── dto/
│   ├── OrderRequest.java
│   ├── OrderResponse.java
│   └── AnalyticsResponse.java
├── factory/
│   └── VehicleFactory.java         # Factory Method Pattern
├── model/
│   ├── Order.java                  # Order entity
│   ├── OrderStatus.java            # PENDING, PROCESSING, COMPLETED, DELAYED
│   ├── Vehicle.java                # Vehicle entity
│   └── VehicleType.java            # TRUCK, VAN, DRONE
├── repository/
│   ├── OrderRepository.java        # JPA repository
│   └── VehicleRepository.java      # JPA repository
├── service/
│   ├── OrderService.java           # Async order processing
│   ├── VehicleService.java         # Vehicle management
│   └── AnalyticsService.java       # Stream API analytics
└── strategy/
    ├── ShippingStrategy.java       # Strategy interface
    ├── StandardShippingStrategy.java
    ├── ExpressShippingStrategy.java
    ├── FreeShippingStrategy.java
    └── ShippingStrategyFactory.java
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

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

## Testing Walkthrough

### Step 1: Verify Async & Strategy Pattern
```bash
# Create an order with EXPRESS shipping
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "weight": 10,
    "destination": "Athens",
    "shippingType": "EXPRESS"
  }'
```

**Expected Behavior**:
- API returns `202 ACCEPTED` immediately (< 200ms)
- Log shows: `[Main-Thread] Response returned in ~XXXms`
- Log shows: `[Async-Thread-1] Starting async processing...`
- After 3 seconds: Cost calculated as 37.5€ (10 × 2.5 × 1.5)

### Step 2: Verify Factory Method Pattern
```bash
# Initialize vehicle fleet
curl -X POST http://localhost:8080/vehicles/init
```

**Expected Behavior**:
- Logs show creation of vehicles using Factory
- Returns 18 vehicles with unique license plates
- Each vehicle type has correct default characteristics

### Step 3: Verify Stream API Analytics
```bash
# Create multiple orders first
curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" \
  -d '{"customerName": "Alice", "weight": 5, "destination": "Athens", "shippingType": "STANDARD"}'

curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" \
  -d '{"customerName": "Bob", "weight": 15, "destination": "Thessaloniki", "shippingType": "EXPRESS"}'

# Wait 4 seconds for async processing to complete

# Get analytics
curl http://localhost:8080/analytics
```

**Expected Behavior**:
- Returns aggregated metrics calculated using Stream API
- Logs show: "Generating analytics using Java Stream API"
- No SQL aggregation queries in logs

## Key Features Demonstrated

### ✅ Clean Code
- Clear separation of concerns
- Meaningful variable and method names
- SOLID principles
- No if-else/switch for strategy selection

### ✅ Concurrency (Async Processing)
- Non-blocking I/O with `@Async`
- Custom `ThreadPoolTaskExecutor`
- Thread-safe order processing
- Immediate API response with background processing

### ✅ Design Patterns
- **Strategy Pattern**: Runtime algorithm selection for shipping costs
- **Factory Method Pattern**: Centralized vehicle creation with default configurations

### ✅ Stream API
- All analytics calculated in Java (no SQL aggregations)
- Demonstrates `reduce()`, `groupingBy()`, `max()`, `filter()`, `map()`

## Database
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:logisticsdb`
  - Username: `sa`
  - Password: *(empty)*

## Logs
The application provides detailed logging showing:
- Thread names (Main-Thread vs Async-Thread-X)
- Processing times
- Strategy pattern usage
- Factory method vehicle creation
- Stream API operations

## Future Enhancements
- Add Spring Security for authentication
- Implement WebSocket for real-time order status updates
- Add integration tests
- Implement actual payment gateway integration
- Add Docker support
- Implement Redis for caching

## Author
Created as a demonstration of modern Java backend development with Spring Boot, focusing on clean code, design patterns, and concurrent processing.
