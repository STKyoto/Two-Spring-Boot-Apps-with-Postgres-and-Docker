# Two Spring Boot Apps with Postgres and Docker

## Prerequisites
* **Docker** and **Docker Compose**: Make sure you have the latest versions of Docker and Docker Compose installed on your computer.

## Running the Application

To run all services, follow these steps from the project's root directory (where the `docker-compose.yml` file is located).

1. **Clone the repository**

git clone https://github.com/STKyoto/Two-Spring-Boot-Apps-with-Postgres-and-Docker.git
cd demo/demo

2. **Use Docker Compose to build the images and start all services (database, auth-api, and data-api).**

docker compose up --build

3. **Verify the application**

After a successful startup, you can check the services' availability:

auth-api: http://localhost:8080

data-api: http://localhost:8081
