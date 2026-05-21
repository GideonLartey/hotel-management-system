


<div align="center">

![Java](https://img.shields.io/badge/Java-11+-blue?logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green)

</div>









# Hotel Management System - Architecture

## System Overview

The Hotel Management System is built using a **Layered Architecture** pattern, separating concerns into distinct layers for maintainability and scalability.

## Architecture Diagram

```
┌──────────────────────────────────────────────┐
│     UI Layer (CLI & GUI Interfaces)          │
│  - HotelManagementCLI (Command-line)         │
│  - HotelManagementGUI (JavaFX GUI)           │
│  - Guest Portal / Admin Dashboard            │
└────────────────┬─────────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│    Business Logic Layer (Managers)      │
│  - BookingManager                       │
│  - StaffManager                         │
│  - RoomRateManager                      │
│  - HousekeepingTracker                  │
│  - FinancialReport                      │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       Model Layer (Domain Objects)      │
│  - Guest, GuestBook                     │
│  - Booking, BookingList                 │
│  - GuestRoom, RoomInventory             │
│  - AdminUser                            │
│  - Enums (RoomType, Status, etc.)       │
└─────────────────────────────────────────┘
```

## Layer Details

### 1. UI Layer (`com.lantel.ui`)

**Responsibility:** Handle user interaction and display

**Key Components:**
- `HotelManagementCLI.java` - CLI menu system
  - Implements dual-portal access (Guest & Admin)
  - Scanner-based CLI input handling
  - Menu navigation logic
  - Sequential menu options
- `HotelManagementGUI.java` - JavaFX GUI application
  - Professional graphical interface
  - Event-driven architecture
  - Windows-like user experience
  - Responsive button controls



### 2. Business Logic Layer (`com.lantel.admin`, `com.lantel.booking`, etc.)

**Responsibility:** Core application logic and domain rules

**Key Managers:**

#### **BookingManager** (`com.lantel.booking`)
- Manages all booking operations
- Calculates room costs with seasonal adjustments
- Detects room conflicts/availability
- Handles check-in/check-out state transitions
- Applies discounts and catering costs

```
Booking Flow:
Customer → Search Availability → Create Booking → Check-in → Check-out
           (date/room type)      (cost calc)     (state)    (cleanup)
```

#### **StaffManager** (`com.lantel.admin`)
- Authentication and authorization
- Staff directory management
- Activate/deactivate staff accounts
- Filter staff by role

**Optimization:** Uses HashMap for O(1) staff lookup by ID, which is more faster than using an ArrayList

#### **RoomRateManager** (`com.lantel.admin`)
- Dynamic pricing based on season
- Discount management
- Rate adjustments

#### **HousekeepingTracker** (`com.lantel.admin`)
- Schedule room cleaning
- Track maintenance tasks
- Update room status

#### **FinancialReport** (`com.lantel.admin`)
- Revenue calculation
- Expense tracking
- Profitability analysis
- Generate reports

### 3. Model Layer (`com.lantel.guest`, `com.lantel.room`, `com.lantel.booking`)

**Responsibility:** Represent domain entities and business rules

**Core Models:**

#### **Guest** (`com.lantel.guest`)
```java
- guestID: String (unique identifier)
- name: String
- age: int (validated ≥ 18)
- phoneNumber: String
- Implements: Serializable
```

#### **Booking** (`com.lantel.booking`)
```java
- bookingID: String (unique)
- guest: Guest
- room: GuestRoom
- checkInDate: LocalDate
- checkOutDate: LocalDate
- roomCost: double (daily rate)
- totalStay: int (nights)
- guestType: GuestType
- seasonType: SeasonType
- isCheckedIn: boolean
- isCheckedOut: boolean
- Implements: Serializable
```

#### **GuestRoom** (`com.lantel.room`)
```java
- roomID: String
- roomType: RoomType (enum)
- status: GuestRoomStatus (enum)
- isOccupied: boolean
- lastCleaningTime: LocalDateTime
```

#### **AdminUser** (`com.lantel.admin`)
```java
- adminID: String
- username: String
- password: String (hashed in production)
- fullName: String
- role: String (Manager, Receptionist, Housekeeper)
- isActive: boolean
+ authenticate(username, password): boolean
```

#### **Enums**

**RoomType:**
- STANDARD ($80/night)
- DELUXE ($150/night)
- EXECUTIVE_SUITE ($300/night)
- VIP_SUITE ($500/night)
- VVIP_PENTHOUSE_SUITE ($1000/night)

**GuestRoomStatus:**
- AVAILABLE
- OCCUPIED
- UNDER_MAINTENANCE
- CLEANING

**SeasonType:**
- OFF_SEASON
- REGULAR
- PEAK_SEASON
- FESTIVAL

**GuestType:**
- INDIVIDUAL
- GROUP
- CORPORATE

## Data Flow

### Booking Process Flow

```
1. Guest searches available rooms
   ├─ Input: check-in date, check-out date, room type
   └─ Process: BookingManager.findAvailableRooms()
   
2. System validates and retrieves available rooms
   ├─ Check room status (must be AVAILABLE)
   ├─ Check for date conflicts
   └─ Filter by room type
   
3. Guest selects room and completes booking
   ├─ Create new Booking object
   ├─ Calculate cost (base rate + season adjustments + discounts)
   ├─ Update room status to OCCUPIED
   └─ Store booking in BookingList
   
4. Guest checks in
   ├─ Verify booking exists
   ├─ Update isCheckedIn = true
   └─ Confirm room access
   
5. Guest checks out
   ├─ Calculate final charges
   ├─ Update isCheckedOut = true
   ├─ Change room status to CLEANING
   └─ Trigger housekeeping notification
```



### Authentication Flow

```
Admin Login
    ↓
Input username/password
    ↓
StaffManager.login(username, password)
    ↓
Search staffMap HashMap
    ↓
AdminUser.authenticate(username, password)
    ↓
Success → Set loggedInUser / Failure → Deny access
    ↓
Access Admin Dashboard
```


## Design Patterns Used

### 1. **Manager Pattern**
Multiple specialized manager classes handle specific domains:
- `BookingManager` for reservations
- `StaffManager` for authentication
- `RoomRateManager` for pricing
- `HousekeepingTracker` for maintenance

**Benefit:** Clear separation of concerns, easy to test and maintain

### 2. **Enum-Based Configuration**
Use enums for type-safe constants:
- `RoomType`, `GuestRoomStatus`, `SeasonType`, `GuestType`

**Benefit:** Prevents invalid state, compile-time safety

### 3. **Dual Collection Strategy**
StaffManager uses both ArrayList and HashMap:
- `ArrayList` for iteration (display, filter)
- `HashMap` for fast lookups by ID

**Benefit:** O(1) lookup performance without sacrificing iteration capabilities

### 4. **Validation Pattern**
Input validation in constructors:
```java
if (age < 18) {
    throw new IllegalArgumentException("Guest must be 18 or older");
}
```


### 5. **Date Range Validation**
Booking objects validate date constraints:
```java
if (!checkOutDate.isAfter(checkInDate)) {
    throw new IllegalArgumentException("Check-out must be after check-in");
}
```


## Key Business Rules

| Rule | Implementation |
|------|-----------------|
| Minimum guest age | Guest constructor validates age ≥ 18 |
| No double-booking | BookingManager checks date conflicts |
| Room status updates | GuestRoomStatus enum enforces valid transitions |
| Check-out after check-in | Booking constructor validates dates |
| Seasonal pricing | SeasonType determines rate multiplier |
| Staff authentication | StaffManager verifies username/password |
| Active staff only | Methods check isActive flag |


## Performance Considerations

### Current Implementation
- **In-memory storage** using ArrayList/HashMap
- **O(1) staff lookup** via HashMap by ID
- **O(n) room availability check** (acceptable for typical hotel size)
- **O(n) booking search** (could be optimized with tree structure)



### Future Optimizations
```
// Replace in-memory with:
- Relational Database (MySQL, PostgreSQL etc)
  - Indexed queries on date ranges
  - SQL for complex searches
  
- Add caching layer
  - Room availability cache
  - Recent bookings cache
  
- Consider data structures:
  - B-trees for date range queries
  - Trie for username autocomplete
```



## Security Considerations

### Current Limitations
- Passwords stored in plain text (vulnerable)
- No encryption for sensitive data
- No input sanitization for SQL injection (N/A - no DB)
- CLI-only, no network security

### Production Improvements
1. **Hash passwords** - bcrypt, SHA-256
2. **Encrypt data** - TLS for network, AES for storage
3. **Input validation** - Sanitize all user inputs
4. **Access control** - Role-based authorization
5. **Audit logging** - Track all admin actions
6. **SQL injection prevention** - Use prepared statements (when DB added)
7. **Rate limiting** - Prevent brute-force login attempts

## Scalability Strategy

### Current State (Single Server)
- ✓ Suitable for small hotels (< 200 rooms)
- ✓ In-memory data sufficient

### Phase 2: Database Migration
- Replace in-memory ArrayList/HashMap with database tables
- Implement connection pooling
- Add query optimization with indexes

### Phase 3: Distributed System
- Separate service layer
- API Gateway for multiple clients
- Load balancing for concurrent bookings
- Message queue for async operations (e.g., email notifications)

### Phase 4: Microservices
- Booking Service
- Room Service
- Payment Service
- Notification Service
- Reporting Service


## Testing Strategy

### Unit Tests
- Test Manager classes with mock data
- Validate business rule enforcement
- Edge case testing (invalid dates, duplicate bookings)

### Integration Tests
- End-to-end booking flow
- Staff authentication
- Report generation

### Test Structure
```
src/test/java/com/lantel/
├── booking/
│   └── BookingManagerTest.java
├── admin/
│   └── StaffManagerTest.java
├── guest/
│   └── GuestTest.java
└── room/
    └── GuestRoomTest.java
```

## Configuration & Constants

### Stored in Model Classes
- Room pricing (RoomType enum)
- Check-in/check-out times (Booking class)
- Age requirements (Guest class)

### Future: Externalize to Config File
```properties
# hotel-config.properties
check.in.time=14:00
check.out.time=11:00
min.guest.age=18
room.standard.price=80
room.deluxe.price=150
```


## Dependencies

```xml
<!-- pom.xml -->
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```


## Future Enhancements

1. **Database Integration** - Persist data to MySQL/PostgreSQL
2. **REST API** - Web service for mobile/web clients
3. **Payment Gateway** - Credit card processing
4. **Email Service** - Booking confirmations, receipts
5. **Advanced Reporting** - Analytics, occupancy rates, revenue trends
6. **Loyalty Program** - Guest rewards and discounts
7. **Multi-language Support** - Internationalization (i18n)
8. **Analytics Dashboard** - Real-time metrics and KPIs

---


