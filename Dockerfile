FROM openjdk:11.0.11-jre
ARG JAR_FILE=build/libs/*-all.jar
ADD ${JAR_FILE} app.jar
CMD ["java", "-jar", "/app.jar"]