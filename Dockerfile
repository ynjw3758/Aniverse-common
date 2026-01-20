# JDK 17 기반 이미지 사용
FROM eclipse-temurin:17-jdk

# .jar 파일을 컨테이너로 복사 (Gradle 빌드 시 생성된 경로 사용)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]