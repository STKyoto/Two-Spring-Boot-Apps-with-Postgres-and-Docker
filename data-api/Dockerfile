
FROM openjdk:21-slim AS build

RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml .
COPY data-api/pom.xml ./data-api/

RUN mvn dependency:go-offline -f ./data-api/pom.xml -B

COPY . .

RUN mvn clean package -f ./data-api/pom.xml -Dmaven.test.skip=true

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /app/data-api/target/data-api-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

CMD ["java", "-jar", "data-api-0.0.1-SNAPSHOT.jar"]