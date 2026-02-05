# Micronaut Charity Project

This project is a **Charity Management System** built with **Micronaut 4.9.3**. It demonstrates a modern backend using gRPC, Micronaut features like serialization and discovery, and supports integration with frontend clients via **gRPC-Web**.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [gRPC-Web Proxy Setup](#grpc-web-proxy-setup)
- [Documentation](#documentation)
- [License](#license)

---

## Project Overview

This project is designed to manage charitable donations, track donors, and process contributions efficiently. It uses **Micronaut** for high-performance backend development and **gRPC** for communication between services.

---

## Features

- **REST & gRPC Endpoints** for donations, users, and campaigns
- **Service Discovery** using Micronaut Discovery Client
- **Ahead-of-Time (AOT) Compilation** for faster startup
- **gRPC-Web Support** for frontend integration
- Easily deployable with **Envoy / grpcwebproxy**

---

## Prerequisites

Before running the project, ensure you have:

- [Java 21+](https://adoptium.net/)
- [Gradle](https://gradle.org/install/)
- [Postgres](https://www.postgresql.org/)
- [Micronaut CLI](https://micronaut.io/download.html)
- [gRPC-Web Proxy](https://github.com/improbable-eng/grpc-web)
---

## Getting Started

1. **Clone the repository**:

```bash
git clone https://github.com/yourusername/micronaut-charity.git
cd micronaut-charity
```

2**Generate Proto**:

```bash
./gradlew generateProto
```


3**Run the backend**:

```bash
./gradlew clean run
```

## gRPC-Web Proxy Setup

To allow a frontend client (like a web app) to communicate with the gRPC backend, run the following command:

```bash
grpcwebproxy \
  --backend_addr=localhost:50051 \
  --run_tls_server=false \
  --allow_all_origins \
  --server_http_debug_port=8000
```

If not installed install using

```
    go install github.com/improbable-eng/grpc-web/go/grpcwebproxy
```


