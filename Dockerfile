# 1단계: 빌드 단계
FROM maven:4.0.0-jdk21 AS builder

# 필요한 파일 복사
COPY --chown=gradle:gradle . /home/gradle/AnalysisAPIServer
WORKDIR /home/gradle/AnalysisAPIServer

# 빌드 수행
RUN gradle build -x test

# 2단계: 실행 이미지
FROM eclipse-temurin:17-jdk

# JAR 복사
COPY --from=builder /home/gradle/AnalysisAPIServer/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 4004
