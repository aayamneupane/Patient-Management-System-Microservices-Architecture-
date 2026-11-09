# ============================================================
# STAGE 1: BUILD
# Uses an image that has Maven 3.9 + JDK 21 (Eclipse Temurin)
# to compile the source and package it into a runnable JAR.
# This stage is named "builder" so the next stage can copy from it.
# ============================================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set /app as the working directory inside the build container
# (created automatically if it doesn't exist).
WORKDIR /app

# Copy ONLY pom.xml first, before the source code.
# Docker caches each layer, so as long as pom.xml doesn't change,
# the dependency download below is reused from cache on later builds.
COPY pom.xml .

# Download all project dependencies and plugins ahead of time.
# -B = batch mode (non-interactive, cleaner logs for CI/Docker).
# Because this layer depends only on pom.xml, code-only changes
# won't trigger a full dependency re-download.
RUN mvn dependency:go-offline -B

# Now copy the application source code into the container.
# Changes here only invalidate the layers from this point onward.
COPY src ./src

# Clean any previous build output, compile the code, run tests,
# and package the app into a JAR under /app/target/.
RUN mvn clean package

# ============================================================
# STAGE 2: RUNTIME
# Starts from a fresh JDK 21 image. Nothing from Stage 1
# (Maven, source code, downloaded dependencies) is carried over
# except what we explicitly copy, so the final image stays smaller
# and cleaner.
# ============================================================
FROM eclipse-temurin:21-jdk AS runner

# Set /app as the working directory for the running container.
WORKDIR /app

# Copy only the built JAR from the "builder" stage and rename it
# to app.jar so the startup command doesn't depend on the version.
COPY --from=builder /app/target/patient-service-0.0.1-SNAPSHOT.jar ./app.jar
 
# Document that the application listens on port 4000.
# (This doesn't publish the port; you still map it with -p 4000:4000
# or in docker-compose/Kubernetes.)
EXPOSE 4000

# Command that runs when the container starts: launch the
# Spring Boot application from the packaged JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]