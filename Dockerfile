# 1. Eclipse Temurin 17 기반 (OpenJDK 대체)
FROM eclipse-temurin:17-jre

# 2. 작업 디렉터리 설정
WORKDIR /app

# 3. jar 파일 복사
COPY build/libs/*.jar app.jar

# 4. 업로드 이미지 디렉토리 생성
RUN mkdir -p /app/uploaded-images/default

# 5. 실행 (한국 시간대 설정)
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]
