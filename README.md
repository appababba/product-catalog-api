# Product Catalog REST API

## Project Overview

This project is a RESTful API for managing a simple product catalog. It was built using Java and the Spring Boot framework.


## Technologies Used

* **Language:** Java 21
* **Framework:** Spring Boot 3.2.5
    * Spring Web (for REST Controllers)
    * Spring Data JPA (for database interaction)
* **Database:** PostgreSQL
* **ORM:** Hibernate (via Spring Data JPA)
* **Build Tool:** Maven
* **Containerization:**
    * Docker
    * Docker Compose
* **Testing:**
    * JUnit 5
    * Mockito
    * AssertJ (or Hamcrest)
    * Spring Test (MockMvc)

## Features

* **CRUD Operations:** Create, Read (all and by ID), Update, and Delete products.
* **RESTful API:** Exposes functionality through standard HTTP methods (GET, POST, PUT, DELETE).
* **Data Persistence:** Uses PostgreSQL database accessed via Spring Data JPA repositories.
* **Containerized:** Includes `Dockerfile` and `docker-compose.yml` for easy setup and deployment using Docker.

## API Endpoints

The base path for the API is `/api/v1/products`.

| Method | Path                   | Description                     | Request Body Example                | Response Body Example                  |
| :----- | :--------------------- | :------------------------------ | :---------------------------------- | :------------------------------------- |
| POST   | `/`                    | Create a new product            | `{"name":" Gadget", "price":19.99}` | `{"id":1, "name":" Gadget", ...}`      |
| GET    | `/`                    | Retrieve all products           | (None)                              | `[{"id":1,...}, {"id":2,...}]`        |
| GET    | `/{id}`                | Retrieve a single product by ID | (None)                              | `{"id":1, "name":" Gadget", ...}`      |
| PUT    | `/{id}`                | Update a product by ID          | `{"name":"New Name", "price":25}`   | `{"id":1, "name":"New Name", ...}` |
| DELETE | `/{id}`                | Delete a product by ID          | (None)                              | (Empty Body, Status 204)               |

*(Note: `description` field is optional in requests)*

## Setup and Running

### Prerequisites

* Java Development Kit (JDK) 21
* Apache Maven
* Docker Desktop

### Running with Docker Compose (Recommended)

This is the easiest way to run the application and the database together.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/appababba/product-catalog-api.git
    cd product-catalog-api
    ```
2.  **Create Environment File:**
    Create a file named `.env` in the project root directory (`product-catalog-api/`). Add the database password to it:
    ```env
    POSTGRES_PASSWORD=0000
    ```
    *(Note: `.env` is included in `.gitignore` to prevent committing secrets.)*
3.  **Build and Run:**
    Make sure Docker Desktop is running. From the project root directory, run:
    ```bash
    docker-compose up --build
    ```
    This will build the application's Docker image and start both the application container and a PostgreSQL container.
4.  **Access API:** The API will be available at `http://localhost:8080/api/v1/products`.
5.  **Access Database (Optional):** The PostgreSQL database inside the container is mapped to port `5433` on your host machine. You can connect to it using a tool like pgAdmin or DBeaver with host `localhost`, port `5433`, database `product_catalog_db`, user `postgres`, and the password set in your `.env` file.
6.  **Stop:** Press `Ctrl+C` in the terminal where compose is running. To remove the containers, run `docker-compose down`. Add `-v` (`docker-compose down -v`) if you want to remove the database data volume too.

### (Optional) Running Locally without Docker

1.  Ensure you have a local PostgreSQL instance running.
2.  Create a database named `product_catalog_db`.
3.  Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` in `src/main/resources/application.properties` to match your local PostgreSQL setup.
4.  Run the application using Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

## Testing

Unit tests for the service layer and integration tests for the controller layer are included.

* **Run all tests:**
    ```bash
    ./mvnw test
    ```
* **Note on Controller Tests:** The controller integration tests (`ProductControllerTest.java`) are currently marked with `@Disabled`. This is due to an incompatibility encountered trying to run Spring Boot 3.2.5 tests within the development environment's Java 24 runtime, which prevented the Spring test context from loading correctly. The service layer unit tests (`ProductServiceTest.java`) pass successfully.