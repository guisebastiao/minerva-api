FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/minerva-api-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Dserver.address=0.0.0.0", "-jar", "app.jar"]
