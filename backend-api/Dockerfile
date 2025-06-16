# 1단계: Build Stage
FROM gradle:8.12.1-jdk17 AS builder
WORKDIR /home/gradle/project

COPY --chown=gradle:gradle . .

RUN ./gradlew clean bootJar --no-daemon

# 2단계: Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 필수 패키지 설치 + Chrome 설치
RUN apt-get update && \
    apt-get install -y wget unzip curl gnupg2 ca-certificates && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt install -y ./google-chrome-stable_current_amd64.deb || apt-get -f install -y && \
    rm google-chrome-stable_current_amd64.deb

# ChromeDriver 설치 (버전 137 고정)
RUN wget https://storage.googleapis.com/chrome-for-testing-public/137.0.7151.70/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip && \
    mv chromedriver-linux64/chromedriver /usr/local/bin/chromedriver && \
    chmod +x /usr/local/bin/chromedriver && \
    rm -rf chromedriver-linux64*

# 환경 변수 설정 (선택사항)
ENV CHROME_BIN=/usr/bin/google-chrome
ENV CHROMEDRIVER_PATH=/usr/local/bin/chromedriver

# 애플리케이션 복사
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

COPY src/main/resources/springboot-*.json /app/credentials.json
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]