FROM amazoncorretto:21-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/patient-service*.jar /app/patient-service.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the command to run the application
CMD ["java", "-jar", "patient-service.jar"]
