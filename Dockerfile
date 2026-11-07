# Build Stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# ১. প্যারেন্ট pom এবং র‍্যাপার ফাইল কপি (mvnw.cmd বাদ দেওয়া হয়েছে)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

COPY mynote-library/pom.xml mynote-library/pom.xml
COPY mynote-service/pom.xml mynote-service/pom.xml

COPY . .

RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/mynote-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]