FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy Maven files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and install X11 dependencies
RUN chmod +x mvnw && \
    apk add --no-cache xvfb

# Copy source code and config
COPY src src
COPY config.properties .

# Build the application
RUN ./mvnw package -DskipTests

# Find the built jar file and use it
# This will use whatever name Maven gave to your jar
ENTRYPOINT java -jar $(find target -name "*.jar")