# Multithreaded Webserver

A basic implementation of a webserver demonstrating both single-threaded and multithreaded (thread pool) server-client architecture using Java sockets and multi-threading concepts.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
  - [Single-threaded Server](#single-threaded-server)
  - [Multithreaded Server and Thread Pool](#multithreaded-server-and-thread-pool)
- [Client Perspective](#client-perspective)
- [Load Testing](#load-testing)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Notes](#notes)

---

## Overview

This project demonstrates a webserver handling client connections using:

- **Single-threaded approach:** handling one client request at a time.
- **Multithreaded server:** spawning a new thread per client request.
- **Thread pool:** managing a fixed number of threads to efficiently handle multiple client connections.

---

## Architecture

### Single-threaded Server

- The server creates a `ServerSocket` listening on a specified port (e.g., `8080`).
- For every client request, the server calls `accept()` to establish a `Socket` connection.
- Communication channels:
  - **Server to client:** via `PrintWriter` (for output stream).
  - **Client to server:** via `BufferedReader` (for input stream).
- Only one client request is processed at a time, blocking others until it finishes.

Example snippet:

```java
ServerSocket socket = new ServerSocket(8080);
while (true) {
Socket acceptedConnection = socket.accept();
PrintWriter out = new PrintWriter(acceptedConnection.getOutputStream(), true);
BufferedReader in = new BufferedReader(new InputStreamReader(acceptedConnection.getInputStream()));
// Process request here (single-threaded)
}
```

---

### Multithreaded Server and Thread Pool

- Instead of processing requests sequentially, the server creates a new thread for each accepted connection.
- Alternatively, a **thread pool** can be used to manage and reuse a fixed number of threads for client handling, improving resource usage and scalability.
- Each thread handles input/output streams (`PrintWriter` and `BufferedReader`) independently, allowing concurrent client servicing.

Benefits:
- Improved throughput.
- Better handling of multiple simultaneous client requests.

---

## Client Perspective

- Each client initiates a socket connection to the server's address and listening port (e.g., `8080`).
- The client and server communicate via:
  - Client **output** stream (`PrintWriter`) to send data to server.
  - Client **input** stream (`BufferedReader`) to receive data from server.

Example snippet:

```java
Socket socket = new Socket("server-address", 8080);
PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
// Send and receive data
```

---

## Load Testing

- Load testing of the multithreaded webserver is performed with **Apache JMeter**.
- JMeter simulates multiple concurrent client requests to evaluate server performance under various load conditions.

---

## Tech Stack

- **Java** - Core language used for server and client socket programming.
- **Java Threads and Executors** - For multithreading and thread pooling.
- **Apache JMeter** - Load testing tool.

---

## Getting Started

1. **Clone the repository**

```
git clone
cd multithreaded-webserver
```

2. **Set up Java SDK**

Ensure Java 8+ is installed and configured on your system.

3. **Compile the project**

```
javac *.java
```

4. **Run the server**

```
java Server
```

5. **Run the client**

```
java Client
```

---

## Usage

- Start the server (`ServerMain`) to listen for client connections.
- Run multiple client instances (`ClientMain`) to send requests.
- Observe server handling requests serially (single-threaded) or concurrently (multithreaded/thread pool).
- Use Apache JMeter to simulate load and measure throughput, response times, and resource utilization.

---

## Notes

- In single-threaded mode, the server blocks on any client interaction until the current request completes.
- Multithreading increases concurrency but requires careful resource management.
- Thread pooling efficiently limits the number of active threads to prevent resource exhaustion.
- Always enable autoflush on `PrintWriter` for real-time client-server communication.

---

Enjoy exploring multithreaded client-server communication in Java!

```