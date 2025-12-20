# 🐳 Docker for Spring Boot - Overview

Multi-stage builds and caching for Java applications!

---

## 📚 This Folder Covers

| File | Topic | Time |
|------|-------|------|
| [01-multi-stage.md](./01-multi-stage.md) | Why multi-stage builds? | 4 min |
| [02-jdk-vs-jre.md](./02-jdk-vs-jre.md) | Build vs Runtime | 3 min |
| [03-layer-caching.md](./03-layer-caching.md) | Dockerfile optimization | 5 min |

---

## 🆚 React vs Spring Boot Docker

| Aspect | React | Spring Boot |
|--------|-------|-------------|
| **Build tool** | npm | Maven |
| **Runtime** | nginx | java -jar |
| **Base image (build)** | node:20-alpine | maven:3.9-temurin-17 |
| **Base image (run)** | nginx:alpine | temurin:17-jre-alpine |
| **Output** | dist/ folder | JAR file |
| **Final size** | ~30MB | ~150MB |

---

## 📄 Your Dockerfile Structure

```dockerfile
# Stage 1: BUILD (JDK + Maven needed)
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: RUN (JRE only needed)
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🔑 Key Concepts

| Concept | Details |
|---------|---------|
| **Multi-stage** | Build stage discarded, only JAR kept |
| **JDK vs JRE** | JDK for compile, JRE for run |
| **Size savings** | 350MB → 150MB |
| **Layer caching** | Copy pom.xml first, then src |

---

## 📁 For Detailed Content

Check the archive folder for the original Docker document:
`../archive/03-spring-boot-docker-explained.md`

---

**Related: [../03-caching-explained/05-docker-caching.md](../03-caching-explained/05-docker-caching.md)** 👉
