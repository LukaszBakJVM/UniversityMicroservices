FROM   openjdk:21-slim-bookworm
EXPOSE 8082
COPY target/Subject-*.jar /subject.jar
ENTRYPOINT ["java","-jar","/subject.jar"]