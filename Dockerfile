FROM openjdk:8
ADD target/ROOT.jar file-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "file-api.jar"]