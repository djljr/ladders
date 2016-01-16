FROM java:8-jre

RUN mkdir /app
COPY target/ladders.jar /app
WORKDIR /app
EXPOSE 3000

CMD ["java", "-jar", "/app/ladders.jar"]