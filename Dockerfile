# Use the official maven/Java 17 image to create a build artifact.
# This is based on Debian and sets the Maven version and the Java version.
FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package

# Use OpenJDK to run it
FROM openjdk:17-jdk-alpine
COPY --from=builder /app/target/jeuDeDames-1.jar /usr/local/lib/jeuDeDames-1.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/jeuDeDames-1.jar"]
