# 🚀 Build Optimization: Build JAR Once

Why build the same JAR twice when you can build it once?

---

## 🎯 Telugu Simple ga

> "Same dish two times cook cheyyadam waste!"
> "One time cook, two places lo serve cheyyochu!"

---

## 🤔 The Problem: Duplicate Builds

```
BEFORE (Inefficient):
═════════════════════

JOB 1: Build & Test
─────────────────────
┌────────────────────────────────────────────────────────────┐
│ mvn compile                                                 │
│ mvn test                                                    │
│ mvn package ──────────▶ target/app.jar  ← BUILD #1        │
│                                                             │
│ upload-artifact (JAR uploaded but...)                      │
└────────────────────────────────────────────────────────────┘

JOB 2: Docker Build
─────────────────────
┌────────────────────────────────────────────────────────────┐
│ Dockerfile:                                                 │
│   FROM maven:3.9 AS build                                  │
│   COPY pom.xml .                                           │
│   RUN mvn dependency:go-offline                            │
│   COPY src .                                               │
│   RUN mvn package ──────────▶ JAR  ← BUILD #2 (DUPLICATE!)│
│                                                             │
│   FROM eclipse-temurin:17-jre                              │
│   COPY --from=build /app/target/*.jar app.jar              │
└────────────────────────────────────────────────────────────┘

Time: ~5 minutes (building JAR TWICE!)
```

---

## ✅ The Solution: Build Once, Use Everywhere

```
AFTER (Optimized):
══════════════════

JOB 1: Build & Test
─────────────────────
┌────────────────────────────────────────────────────────────┐
│ mvn compile                                                 │
│ mvn test                                                    │
│ mvn package ──────────▶ target/app.jar  ← ONLY BUILD      │
│                              │                              │
│ upload-artifact ◀────────────┘                             │
└────────────────────────────────────────────────────────────┘
                               │
                     ARTIFACT STORAGE
                               │
                               ▼
JOB 2: Docker Build
─────────────────────
┌────────────────────────────────────────────────────────────┐
│ download-artifact ────────▶ target/app.jar                 │
│                                                             │
│ Dockerfile (SIMPLIFIED!):                                  │
│   FROM eclipse-temurin:17-jre                              │
│   COPY target/*.jar app.jar  ← Just copy pre-built JAR!   │
└────────────────────────────────────────────────────────────┘

Time: ~2 minutes (NO duplicate build!)
```

---

## 📝 Implementation

### Workflow Changes

```yaml
jobs:
  build-and-test:
    steps:
      # Build JAR
      - name: 📦 Package JAR
        run: mvn package -DskipTests -B

      # Upload JAR as artifact
      - name: 📤 Upload JAR
        uses: actions/upload-artifact@v4
        with:
          name: todo-backend-jar
          path: ./target/*.jar
          retention-days: 7

  docker-build:
    needs: build-and-test  # Wait for Job 1
    steps:
      - name: 📥 Checkout
        uses: actions/checkout@v4

      # Download pre-built JAR
      - name: 📥 Download JAR Artifact    # ← THE KEY!
        uses: actions/download-artifact@v4
        with:
          name: todo-backend-jar
          path: ./target/

      - name: 🐳 Build and Push
        uses: docker/build-push-action@v5
        # ... uses simplified Dockerfile
```

### Simplified Dockerfile

```dockerfile
# BEFORE: Multi-stage (builds JAR inside Docker)
# ═══════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B    # ← Building again!

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


# AFTER: Single-stage (uses pre-built JAR)
# ═══════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY target/*.jar app.jar         # ← Just copy pre-built JAR!

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📊 Time Savings

| Stage | Before | After | Savings |
|-------|--------|-------|---------|
| Job 1 Build | 2 min | 2 min | 0 |
| Job 2 Docker Build | 3 min | 30 sec | **2.5 min!** |
| **Total** | **5 min** | **2.5 min** | **50%** |

---

## 🤔 Trade-offs

| Approach | Pros | Cons |
|----------|------|------|
| **Build in Docker** | Reproducible, portable | Slower, duplicate work |
| **Build once** | Faster, less waste | Depends on artifact |

### When to use Build-in-Docker

- Open source projects (anyone can build)
- Multi-platform builds (arm64, amd64)
- No artifact dependency needed

### When to use Build-once

- Private CI/CD pipelines
- Speed is important
- Already testing in Job 1

---

## 🔄 Complete Optimized Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                      OPTIMIZED CI/CD PIPELINE                                    │
│                                                                                  │
│  git push                                                                        │
│      │                                                                           │
│      ▼                                                                           │
│  ┌──────────────────────────────────────────────────────────────────────────┐   │
│  │ JOB 1: Build & Test (2 min)                                               │   │
│  │                                                                           │   │
│  │ checkout → setup-java (cache maven) → mvn test → mvn package             │   │
│  │                                                         │                 │   │
│  │                                              upload-artifact              │   │
│  │                                                         │                 │   │
│  └─────────────────────────────────────────────────────────┼────────────────┘   │
│                                                            │                     │
│                                                            ▼                     │
│                              ┌───────────────────────────────────────────────┐  │
│                              │ ARTIFACT: todo-backend-jar (43 MB)            │  │
│                              └───────────────────────────┬───────────────────┘  │
│                                                          │                       │
│  ┌───────────────────────────────────────────────────────▼──────────────────┐   │
│  │ JOB 2: Docker Build (30 sec!)                                            │   │
│  │                                                                          │   │
│  │ checkout → download-artifact → docker build (just COPY jar) → push GHCR │   │
│  │                                                                          │   │
│  └──────────────────────────────────────────────────────────────────────────┘   │
│                                                                                  │
│  TOTAL TIME: ~2.5 minutes (was 5 minutes!)                                      │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎬 Telugu Summary

```
Problem:
────────
Traditional workflow lo JAR TWO times build avuthundi:
1. Job 1 lo mvn package
2. Dockerfile lo RUN mvn package

Waste of time! 😤

Solution:
─────────
JAR ONE time build cheyyi, artifact ga upload cheyyi
Job 2 lo download cheyyi, Docker lo just COPY cheyyi!

Implementation:
───────────────
1. Job 1: upload-artifact@v4 - JAR upload
2. Job 2: download-artifact@v4 - JAR download
3. Dockerfile: Simple COPY, no maven!

Result:
───────
• 50% faster builds! ⚡
• Less compute usage
• Same final image
```

---

**Next: [04-cleanup-workflow.md](./04-cleanup-workflow.md)** 👉
