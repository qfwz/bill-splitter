# FAWWAZ BANK Bill Splitter

A REST API for managing shared bills, expenses, payments, and bill splitting between users.

---

## Table of Contents

- [Built With](#built-with)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Main Entities](#main-entities)
- [API Endpoints](#api-endpoints)
    - [Users](#users)
    - [Bill Groups](#bill-groups)
    - [Group Members](#group-members)
    - [Expenses](#expenses)
    - [Payments](#payments)
    - [Split Results](#split-results)
    - [Settlements](#settlements)
- [Swagger Documentation](#swagger-documentation)
- [Database Configuration](#database-configuration)
- [Docker Compose](#docker-compose)
- [Getting Started](#getting-started)
- [Typical Usage Flow](#typical-usage-flow)
- [Splitting Methods](#splitting-methods)
- [Error Handling](#error-handling)
- [Development](#development)
- [Testing the API](#testing-the-api)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## Built With

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA
- Hibernate
- MySQL 8
- Maven
- Docker & Docker Compose
- Swagger / OpenAPI

---

## Features

- Create and manage users
- Create bill groups
- Add users to groups
- Create expenses
- Record payments
- Split expenses equally
- Split expenses by percentage
- Track split results
- Manage settlements
- MySQL database integration
- Dockerized application and database
- API documentation with Swagger UI

---

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate

### Database

- MySQL 8.0

### Build Tool

- Maven

### Containerization

- Docker
- Docker Compose

### API Documentation

- Swagger UI
- OpenAPI

---

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── fawwaz_bank/
    │           └── bill_splitter/
    │               ├── controller/
    │               ├── dto/
    │               ├── model/
    │               ├── repository/
    │               ├── service/
    │               └── BillSplitterApplication.java
    │
    └── resources/
        └── application.properties
```

---

## Main Entities

| Entity | Description |
|---|---|
| **User** | Represents a user who participates in bill splitting. |
| **BillGroup** | Represents a group of users who share expenses. |
| **GroupMember** | Represents the relationship between a user and a bill group. |
| **Expense** | Represents an expense that needs to be split between group members. |
| **Payment** | Represents an amount paid by a user toward an expense. |
| **SplitResult** | Represents how much each participant is responsible for from an expense. |
| **Settlement** | Represents the amount that one user needs to pay to another user to settle the remaining balance. |

---

## API Endpoints

All example `curl` commands below assume the application is running locally at `http://localhost:4110`.

### Users

#### Get All Users
**GET** `/api/users`

Returns a list of all users registered in the system.

**Response Example:**
```json
[
  {
    "id": 1,
    "username": "Fawwaz"
  },
  {
    "id": 2,
    "username": "Nirmala"
  }
]
```

**curl:**
```bash
curl -X GET http://localhost:4110/api/users
```

#### Create User
**POST** `/api/users`

Creates a new user.

**Request Body:**
```json
{
  "username": "fawwaz_gaul"
}
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "fawwaz_gaul"
  }'
```

---

### Bill Groups

#### Get All Bill Groups
**GET** `/api/bill-groups`

Returns all bill groups.

**curl:**
```bash
curl -X GET http://localhost:4110/api/bill-groups
```

#### Create Bill Group
**POST** `/api/bill-groups`

Creates a new bill group.

**Request Body:**
```json
{
  "name": "Trip to Dayeuhkolot"
}
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/bill-groups \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Trip to Dayeuhkolot"
  }'
```

---

### Group Members

#### Get All Group Members
**GET** `/api/group-members`

Returns all group members.

**curl:**
```bash
curl -X GET http://localhost:4110/api/group-members
```

#### Add User to Group
**POST** `/api/group-members`

Adds a user to a bill group.

**Request Body:**
```json
{
  "groupId": 1,
  "userId": 1
}
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/group-members \
  -H "Content-Type: application/json" \
  -d '{
    "groupId": 1,
    "userId": 1
  }'
```

---

### Expenses

#### Get All Expenses
**GET** `/api/expenses`

Returns all expenses.

**curl:**
```bash
curl -X GET http://localhost:4110/api/expenses
```

#### Create Expense
**POST** `/api/expenses`

Creates a new expense.

**Request Body:**
```json
{
  "description": "Bakso Beranak",
  "amount": 300000,
  "groupId": 1
}
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Bakso Beranak",
    "amount": 300000,
    "groupId": 1
  }'
```

---

### Payments

#### Get All Payments
**GET** `/api/payments`

Returns all payments.

**curl:**
```bash
curl -X GET http://localhost:4110/api/payments
```

#### Create Payment
**POST** `/api/payments`

Records a payment made by a user.

**Request Body:**
```json
{
  "expense": {
    "id": 1
  },
  "user": {
    "id": 1
  },
  "amountPaid": 140000
}
```

[//]: # (> **Note:** `amountPaid` uses `BigDecimal` and can be provided as a plain numeric JSON value.)

**curl:**
```bash
curl -X POST http://localhost:4110/api/payments \
  -H "Content-Type: application/json" \
  -d '{
    "expense": { "id": 1 },
    "user": { "id": 1 },
    "amountPaid": 140000
  }'
```

---

### Split Results

#### Get All Split Results
**GET** `/api/split-result`

Returns all split results.

**curl:**
```bash
curl -X GET http://localhost:4110/api/split-result
```

#### Create Split Result Manually
**POST** `/api/split-result`

Creates a split result manually.

**Request Body:**
```json
{
  "expenseId": 1,
  "userId": 1,
  "shareAmount": 100000
}
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/split-result \
  -H "Content-Type: application/json" \
  -d '{
    "expenseId": 1,
    "userId": 1,
    "shareAmount": 100000
  }'
```
> Before generating settlements, the total `shareAmount` for each expense must exactly match the expense amount. If the total `shareAmount` does not match the expense amount, settlement generation will return an error.
#### Equal Split
**POST** `/api/split-result/expense/{expenseId}/equal`

Splits an expense equally between the participants.

**Example:**
```
POST /api/split-result/expense/1/equal
```
The API calculates the share amount automatically based on the expense amount and the number of participants.

**curl:**
```bash
curl -X POST http://localhost:4110/api/split-result/expense/1/equal
```

#### Percentage Split
**POST** `/api/split-result/expense/{expenseId}/percentage`

Splits an expense based on percentages provided in the request.

**Example:**
```
POST /api/split-result/expense/1/percentage
```

**Request Body:**
```json
{
  "percentages": {
    "1": 60,
    "2": 40
  }
}
```

> The percentage values represent each user's share of the expense. The total percentage should equal 100%.

**curl:**
```bash
curl -X POST http://localhost:4110/api/split-result/expense/1/percentage \
  -H "Content-Type: application/json" \
  -d '{
    "percentages": {
      "1": 60,
      "2": 40
    }
  }'
```

---

### Settlements

#### Get All Settlements
**GET** `/api/groups/{groupId}/settlements`

Returns the settlements for a bill group.

**Example:**
```
GET /api/groups/1/settlements
```

**curl:**
```bash
curl -X GET http://localhost:4110/api/groups/1/settlements
```

#### Generate Settlements
**POST** `/api/groups/{groupId}/settlements/generate`

Calculates settlements for a bill group based on its expenses, split results, and payments. This endpoint does not require a request body.

**Example:**
```
POST /api/groups/1/settlements/generate
```

**curl:**
```bash
curl -X POST http://localhost:4110/api/groups/1/settlements/generate
```

---

## Swagger Documentation

Swagger UI is available after the application is running. It will provide you with the required JSON body for each request.

**Open:**
```
http://localhost:4110/swagger-ui/index.html
```
---

## Database Configuration

- The application uses **MySQL 8**.
- When running with Docker Compose, the application connects to the MySQL container using the Docker service name.
- The database configuration uses environment variables instead of storing the database password directly in the source code.
- Make sure to create a `.env` file in root directory before running Docker Compose.

**Example environment variable/.env file:**
```
DB_PASSWORD=put_your_db_password_here
```

---

## Docker Compose

The project includes Docker Compose configuration for running both the Spring Boot application and MySQL database.

**Start the application:**
```bash
docker compose up --build
```

The application will be available at:
```
http://localhost:4110
```

The MySQL database is exposed on:
```
localhost:3307
```

Inside the Docker network, the application connects to MySQL using:
```
jdbc:mysql://db:3306/bill_splitter
```

**Stop the application:**
```bash
docker compose down
```

---

## Getting Started

1. Clone the repository.
2. Open the project directory.
3. Make sure Docker Desktop is running.
4. Configure the required environment variables.
5. Start the application:
   ```bash
   docker compose up --build
   ```
6. Wait until Spring Boot reports that the application has started successfully.
7. Open Swagger UI:
   ```
   http://localhost:4110/swagger-ui/index.html
   ```
8. Use Swagger UI to test the available endpoints.

---

## Typical Usage Flow

A typical bill splitting process can be performed in the following order:

1. Create users.
2. Create a bill group.
3. Add users to the group.
4. Create an expense.
5. Choose a splitting method.
6. Generate the split result.
7. Record payments.
8. Calculate or manage settlements using `POST /api/groups/{groupId}/settlements/generate`.

---

## Splitting Methods

The API currently supports two automatic splitting methods such as:

### Equal Split

This method will divide the expense equally between all participants.

**Example:**
```
Total expense: 300000
Participants: 3

Each participant: 100000
```

### Percentage Split

The expense is divided according to percentages that is provided.

**Example:**
```
Total expense: 300000

User 1: 60%
User 2: 40%

Result:
User 1: 180000
User 2: 120000
```

---

## Error Handling

The application uses Spring Boot's default HTTP error handling.

| Status Code | Description |
|---|---|
| `200 OK` | Request was successfully processed. |
| `201 Created` | Resource was successfully created. |
| `400 Bad Request` | The request body or parameters are invalid. |
| `404 Not Found` | The requested resource does not exist. |
| `500 Internal Server Error` | An unexpected server-side error occurred. |

---

## Development

To run the application locally without Docker, make sure Java, Maven, and MySQL are installed and configured.

**Build the project:**
```bash
mvn clean package
```

**Run the application:**
```bash
mvn spring-boot:run
```

---

## Testing the API

The API can be tested using Swagger UI, Postman, or another REST API client but Swagger should be easier to use; with it the available endpoints and request structures can be tested directly from the browser.

---

## Future Improvements

Possible improvements for future versions include:

- Add authentication and authorization
- Improve global exception handling
- Add validation for request bodies
- Expand existig automated unit test coverage using JUnit Jupiter and Mockito

---

## Author

**Qmaz Fawwaz Syafta**

## Submission Requirements:

***Personalization:***

As a requirement, every settlement response also includes two additional fields: `service_charge_pct` and `service_charge_amount`. Below is how these values are calculated for my GitHub username (`qfwz`), computed dynamically in code instead of being hardcoded.

**GitHub Username:** `qfwz`

**Calculation:**
1. Unicode (ASCII) sum of `qfwz`: `113 + 102 + 119 + 122 = 456`
2. `service_charge_pct = 456 % 10 = 6` → **6%**
3. `service_charge_amount` is this 6% applied to the total group expenses after generating the settlement.


***Submission Question***:
### What was the hardest design decision you made while building this, and what trade-off did you accept?

The hardest design decision was how to model the bill-splitting flow while keeping the API simple and easy to use. I chose to separate expenses, payments, split results, and settlements into different entities instead of putting all of the information into a single model. The trade-off was having more entities and relationships to manage, which makes the implementation more complex. However, this approach makes each part of the system easier to understand, maintain, and extend. For example, different splitting methods can generate their own split results without changing the expense or payment structure.