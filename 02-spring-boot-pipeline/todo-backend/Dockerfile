# ================================
# Stage 1: BUILD
# ================================
# Use Maven with JDK 17 for building
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (for dependency caching)
COPY pom.xml .

# Download dependencies (cached if pom.xml unchanged)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests - already run in CI)
RUN mvn package -DskipTests -B

# ================================
# Stage 2: PRODUCTION
# ================================
# Use lightweight JRE for running
FROM eclipse-temurin:17-jre-alpine

# Add labels for GitHub Container Registry
LABEL org.opencontainers.image.source="https://github.com/pdheeraj99/github-actions-learning"
LABEL org.opencontainers.image.description="Todo Backend API - Spring Boot"
LABEL org.opencontainers.image.licenses="MIT"

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check for Kubernetes
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
