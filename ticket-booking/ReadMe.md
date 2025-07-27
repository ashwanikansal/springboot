# IRCTC Reservation Backend

A simplified backend system for railway reservations, allowing users to sign up, search trains, view available seats, book tickets, and download tickets.

## Table of Contents

- [Features](#features)
- [Entities](#entities)
- [Architecture & Services](#architecture--services)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Notes](#notes)

## Features

- User Signup & Login with password hashing (using BCrypt)
- Search trains between source and destination
- Show available seats for selected trains
- Book tickets and view booking history
- Download ticket functionality (fetch ticket details)

## Entities

### User
```java
String name
String password
String hashedPassword
List ticketsBooked
String userId
```

### Train
```java
String trainId
String trainNo
List<List> seats
Map stationsTimes
List stations
```

### Ticket
```java
String ticketId
String userId
String source
String destination
Date dateOfTravel
Train train
```

## Architecture & Services

### Services Implemented

- **UserBookingService**
    - `loginUser(User user)`
    - `signUp(User user)`
    - `fetchBookings(...)`
    - `cancelBooking(...)`
    - `bookTicket(String source, String destination)`

- **TrainService**
    - `searchTrain(String source, String destination)`
    - `getSeatsAvailable(Train train)`

### Data Handling

- In-memory local database (localDb) used for persisting data temporarily.
- Jackson library is used for serializing/deserializing Java objects to/from JSON.
    - Uses `ObjectMapper` (`readValue` and `writeValue`), and `TypeReference` for lists of objects.
- BCrypt library is used for secure password hashing.

## Tech Stack

- Java (JDK 8+)
- [Jackson](https://github.com/FasterXML/jackson) - JSON serialization/deserialization
- [BCrypt](https://www.mindrot.org/projects/jBCrypt/) - Password hashing

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone 
   cd irctc-reservation-backend
   ```

2. **Install Dependencies:**
    - Add Jackson and BCrypt libraries to your project (Maven/Gradle or manually).

3. **Compile the Code:**
   ```bash
   javac Main.java
   ```

4. **Run the Application:**
   ```bash
   java Main
   ```

## Usage

1. **User Signup/Login:**
    - Users can register or log in via the provided API/CLI.

2. **Search Trains:**
    - Fetch a list of trains between two stations with seat availability.

3. **Book Tickets:**
    - Select source/destination, choose a train, and book seats.

4. **View/Download Tickets:**
    - View booking history and fetch ticket details.

## Notes

- All data is stored in-memory; upon restart, the data will be lost.
- Jackson's `ObjectMapper` is configured to map between class fields and JSON keys (e.g., `userId` ↔ `user_id`).
- Type information for collections is provided at runtime via Jackson's `TypeReference`.


**Enjoy booking with our IRCTC Reservation Backend!**

---