# Spring Boot CI/CD Concepts - Index

This folder contains comprehensive documentation on Spring Boot GitHub Actions workflows, Maven, and Docker concepts.

---

## 📚 Reading Order

### Start Here

1. [**Spring Boot Workflow Explained**](01-spring-boot-workflow-explained.md)
   - Complete breakdown of `spring-boot-build.yml`
   - Job-by-job, step-by-step analysis
   - Environment variables, triggers, and workflow structure
   - Performance optimizations explained

### Supporting Concepts

2. [**Maven vs npm Comparison**](02-maven-vs-npm-comparison.md)
   - Side-by-side comparison of Java and JavaScript build tools
   - Dependency management differences
   - Lock files explained
   - Lifecycle and performance comparison

3. [**Spring Boot Docker Explained**](03-spring-boot-docker-explained.md)
   - Multi-stage Dockerfile deep dive
   - JDK vs JRE explained
   - Layer caching strategies
   - Security best practices (non-root user)
   - Health checks for Kubernetes
   - Comparison with React Dockerfile

4. [**🎬 Visual Workflow Guide**](04-spring-boot-visual-workflow-guide.md) ⭐
   - Complete ASCII visualizations of every workflow step
   - BEFORE/ACTION/AFTER diagrams for each step
   - What happens inside the runner VM
   - Multi-stage Docker build visualization
   - Telugu explanations throughout
   - React vs Spring Boot comparisons

5. [**🔄 Caching Deep Dive**](05-github-actions-caching-deep-dive.md) ⭐ NEW!
   - Real screenshots from your workflow runs
   - Video recordings of cache flow
   - Cache MISS and HIT explained visually
   - Maven cache + Docker cache explanation
   - Where cache is stored (GitHub Cloud)
   - Telugu explanations (Ardham)

---

## 🎯 What You'll Learn

After reading these docs, you'll understand:

- ✅ How Spring Boot CI/CD workflows differ from React workflows
- ✅ Maven build lifecycle and dependency management
- ✅ Docker multi-stage builds for Java applications
- ✅ Performance optimization techniques
- ✅ Security best practices for production images
- ✅ Kubernetes readiness (health checks)
- ✅ Comparison between npm and Maven ecosystems

---

## 📊 Quick Reference

### Workflow Structure

```yaml
name: 🚀 Spring Boot CI/CD

on: [push, pull_request, workflow_dispatch]

jobs:
  build-and-test:
    - Setup Java + Maven
    - Build & Test
    - Upload artifact

  docker-build:
    needs: build-and-test
    - Build Docker image
    - Push to GHCR
```

### Maven Lifecycle

```bash
mvn clean compile    # Compile Java files
mvn test             # Run tests
mvn package          # Create JAR
```

### Docker Multi-Stage

```dockerfile
# Stage 1: Build with Maven + JDK
FROM maven:3.9-eclipse-temurin-17-alpine AS build
RUN mvn package

# Stage 2: Runtime with JRE only
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🔗 Related Documentation

- **Docker Concepts:** `../01-docker-concepts/`
- **GitHub Actions Concepts:** `../02-github-actions-concepts/`
- **React Pipeline:** `../03-react-pipeline/`
- **Spring Boot Code:** `../02-spring-boot-pipeline/todo-backend/`

---

## 📝 Telugu Explanations Included

All documents include Telugu-English mixed explanations for key concepts:

- **Adhi enti?** (What is this?)
- **Enduku?** (Why?)
- **Ela panichestundi?** (How does it work?)

---

## ✅ Completion Checklist

Track your learning progress:

- [ ] Read Spring Boot workflow explanation
- [ ] Understand Maven vs npm differences
- [ ] Learn Docker multi-stage builds
- [ ] Compare React and Spring Boot pipelines
- [ ] Review actual workflow file
- [ ] Check GHCR for published images

---

**Happy Learning!** 🎉

Once you complete these docs, you'll have mastered both React and Spring Boot CI/CD pipelines!
