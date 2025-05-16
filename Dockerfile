### 1-р үе: frontend build
FROM node:18 AS frontend
WORKDIR /app/front-end
COPY front-end/ .
RUN npm install && npm run build

### 2-р үе: backend build
FROM maven:3.9.0-eclipse-temurin-17 AS backend
WORKDIR /app/back-end
COPY back-end/ .
RUN mvn clean package -DskipTests

### 3-р үе: финал container
FROM eclipse-temurin:17-jdk
WORKDIR /app

# backend jar хуулна
COPY --from=backend /app/back-end/target/back-end-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# frontend static build-ийг хуулна
COPY --from=frontend /app/front-end/build /app/static

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
