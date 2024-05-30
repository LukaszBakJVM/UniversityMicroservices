FROM   openjdk:21-slim-bookworm
EXPOSE 8080
COPY target/Student-*.jar /student.jar
ENTRYPOINT ["java","-jar","/student.jar"]