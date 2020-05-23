FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/registry-service.jar target/app.jar
EXPOSE 8761
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","target/app.jar"]
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait