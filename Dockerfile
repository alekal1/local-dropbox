FROM openjdk:11
ADD build/libs/fileserver-0.0.1-SNAPSHOT.jar fileserver-0.0.1-SNAPSHOT.jar
EXPOSE 8080
RUN mkdir -p src/main/resources/localStorage
ENTRYPOINT ["java", "-jar", "fileserver-0.0.1-SNAPSHOT.jar"]