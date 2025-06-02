FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN apk add --no-cache maven && mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/minerva-api-1.0.0.jar app.jar

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

ENV PORT=8080
EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar app.jar"]