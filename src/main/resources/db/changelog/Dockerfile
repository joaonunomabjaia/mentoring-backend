# Use a base image with GraalVM and Java 17
FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-21.3.0

# Set the working directory
WORKDIR /his/mentoring/backend

# Create a directory for logs
RUN mkdir -p /his/mentoring/backend/log

# Define a volume for logs
VOLUME ["/his/mentoring/backend/log"]

# Copy the jar file into the container
COPY mentoring-backend-1.1-all.jar /his/mentoring/backend/mentoring-backend-1.1-all.jar

# Expose the port that the application will run on
EXPOSE 8087

# Command to run the jar file
CMD ["java", "-jar", "/his/mentoring/backend/mentoring-backend-1.1-all.jar"]
