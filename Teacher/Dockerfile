FROM   openjdk:21-slim-bookworm
EXPOSE 8083
COPY target/Teacher-*.jar /teacher.jar
ENTRYPOINT ["java","-jar","/teacher.jar"]