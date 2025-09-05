FROM ubuntu:latest
LABEL authors="mingi"

# Add authentication credentials for repository pull
ARG USERNAME
ARG PASSWORD
RUN echo $USERNAME:$PASSWORD > /root/.docker/config.json

ENTRYPOINT ["top", "-b"]

FROM openjdk:17-ea-11-jdk-slim AS BUILDER
RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*
RUN mkdir /app_source
COPY .. /app_source
WORKDIR /app_source
RUN chmod +x ./gradlew
RUN ./gradlew bootJar -x test

FROM openjdk:17-ea-11-jdk-slim AS RUNNER
RUN mkdir /app
COPY --from=BUILDER /app_source/build/libs /app
WORKDIR /app
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar","--ACTIVE=prod"]
