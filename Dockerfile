FROM openjdk:11-jdk-slim
COPY target/TP-Final-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

#FROM openjdk:11-jdk-slim
#CMD ["mkdir", "app"]
#WORKDIR app/
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#COPY "./target/TP-Final-0.0.1-SNAPSHOT.jar" "app.jar"
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","app.jar"]
