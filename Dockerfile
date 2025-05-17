# 1단계: 빌드
FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# 2단계: 실제 실행용 이미지
FROM --platform=linux/amd64 openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]