# 베이스 이미지
FROM eclipse-temurin:21

# 작업 디렉토리 설정
WORKDIR /app

# 로컬에서 빌드한 JAR 파일을 이미지 내부로 복사
# 이 부분에서는 로컬 빌드 파일 경로를 정확히 지정해야 합니다.
COPY build/libs/*.jar app.jar

# 사용자 생성 및 권한 설정 (선택 사항)
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]