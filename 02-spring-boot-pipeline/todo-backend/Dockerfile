# ================================
# OPTIMIZED Dockerfile - No Build Stage!
# ================================
# Uses pre-built JAR from CI pipeline
# Build happens in GitHub Actions, not in Docker

FROM eclipse-temurin:17-jre-alpine

# Add labels for GitHub Container Registry
LABEL org.opencontainers.image.source="https://github.com/pdheeraj99/github-actions-learning"
LABEL org.opencontainers.image.description="Todo Backend API - Spring Boot"
LABEL org.opencontainers.image.licenses="MIT"

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy pre-built JAR from CI pipeline
# JAR is downloaded from Job 1 artifact before Docker build
COPY target/*.jar app.jar

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
