# 1단계: 빌드 단계 (Maven + JDK 21)
FROM maven:4.0.0-eclipse-temurin-21 AS builder

# 프로젝트 복사 및 빌드
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# 2단계: 실행 이미지 (JDK 21)
FROM eclipse-temurin:21-jdk

# 빌드된 JAR 복사
COPY --from=builder /app/target/*.jar app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 4004
