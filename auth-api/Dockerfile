
FROM openjdk:21-slim AS build

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
COPY auth-api/pom.xml ./auth-api/

RUN mvn dependency:go-offline -f ./auth-api/pom.xml -B

COPY . .

RUN mvn clean package -f ./auth-api/pom.xml -Dmaven.test.skip=true

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /app/auth-api/target/auth-api-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "auth-api-0.0.1-SNAPSHOT.jar"]