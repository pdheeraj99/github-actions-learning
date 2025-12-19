# Spring Boot Docker - Multi-Stage Build Deep Dive

## Overview

This document explains the Spring Boot Dockerfile in detail, focusing on multi-stage builds and differences from the React Dockerfile.

---

## 📄 Complete Dockerfile

```dockerfile
# ================================
# Stage 1: BUILD
# ================================
FROM maven:3.9-eclipse-temurin-17-alpine AS build

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
FROM eclipse-temurin:17-jre-alpine

LABEL org.opencontainers.image.source="https://github.com/pdheeraj99/github-actions-learning"
LABEL org.opencontainers.image.description="Todo Backend API - Spring Boot"
LABEL org.opencontainers.image.licenses="MIT"

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

EXPOSE 8080

# Health check for Kubernetes
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🎯 Stage 1: BUILD Stage

### Base Image Choice

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine AS build
```

**Breaking it down:**

| Component | Meaning |
|-----------|---------|
| `maven:3.9` | Maven version 3.9 pre-installed |
| `eclipse-temurin` | OpenJDK distribution (formerly AdoptOpenJDK) |
| `17` | Java 17 |
| `alpine` | Alpine Linux (minimal base) |
| `AS build` | Name this stage "build" |

**Size comparison:**

```
maven:3.9-eclipse-temurin-17         → ~600MB
maven:3.9-eclipse-temurin-17-alpine  → ~350MB  ← We use this!
```

**Why Alpine?**

- Smaller image (saves bandwidth)
- Faster builds
- Less attack surface
- Same functionality

---

### Step 1: Copy pom.xml First

```dockerfile
WORKDIR /app
COPY pom.xml .
```

**Why separate from source code?**

**Docker layer caching magic:**

```
pom.xml changes rarely → Dependencies cached 
Source code changes often → Don't re-download deps!
```

**Real example:**

```
Build 1 (clean):
COPY pom.xml → Changed
RUN mvn dependency:go-offline → 3 minutes (download all deps)

Build 2 (only code changed):
COPY pom.xml → Cached (unchanged)
RUN mvn dependency:go-offline → Cached! (0 seconds)
```

**This saves ~3 minutes every build!** ⚡

---

### Step 2: Download Dependencies

```dockerfile
RUN mvn dependency:go-offline -B
```

**What does `dependency:go-offline` do?**

Downloads ALL dependencies without building:

```
~/.m2/repository/
  ├── org/springframework/spring-boot/3.2.0/
  ├── com/h2database/h2/2.2.224/
  ├── org/hibernate/hibernate-core/6.4.1/
  └── ... (100+ JARs)
```

**`-B`** = Batch mode (no interactive prompts)

**Why not `mvn package` here?**

```dockerfile
# ❌ BAD - Downloads deps + builds code together
COPY pom.xml .
COPY src ./src
RUN mvn package

# ✅ GOOD - Separate layers
COPY pom.xml .
RUN mvn dependency:go-offline  ← Cached if pom.xml unchanged
COPY src ./src
RUN mvn package                ← Only runs if code changed
```

---

### Step 3: Copy Source Code

```dockerfile
COPY src ./src
```

**Source code structure:**

```
src/
  ├── main/
  │   ├── java/
  │   │   └── com/example/todo/
  │   │       ├── TodoApplication.java
  │   │       ├── controller/
  │   │       ├── service/
  │   │       ├── repository/
  │   │       └── entity/
  │   └── resources/
  │       └── application.properties
  └── test/
      └── java/
          └── com/example/todo/
              └── TodoApplicationTests.java
```

**All copied into Docker image for building**

---

### Step 4: Build JAR

```dockerfile
RUN mvn package -DskipTests -B
```

**Why `-DskipTests`?**

Tests already ran in GitHub Actions! No need to run again in Docker.

**Build output:**

```
target/
  └── todo-backend-1.0.0.jar  ← This is what we need!
```

**JAR file contents:**

```
todo-backend-1.0.0.jar
  ├── BOOT-INF/
  │   ├── classes/           ← Your compiled .class files
  │   │   └── com/example/todo/
  │   │       ├── TodoApplication.class
  │   │       └── ...
  │   └── lib/               ← ALL dependencies!
  │       ├── spring-boot-3.2.0.jar
  │       ├── spring-web-6.1.2.jar
  │       ├── h2-2.2.224.jar
  │       └── ... (50+ JARs)
  ├── META-INF/
  │   └── MANIFEST.MF
  └── org/springframework/boot/loader/
      └── JarLauncher.class  ← Spring Boot magic!
```

**Self-contained:** Everything needed to run!

---

## 🚀 Stage 2: PRODUCTION Stage

### Base Image - JRE Only

```dockerfile
FROM eclipse-temurin:17-jre-alpine
```

**Key difference from Stage 1:**

| Stage 1 | Stage 2 |
|---------|---------|
| `maven` | NO Maven |
| JDK 17 (Java **Development** Kit) | JRE 17 (Java **Runtime** Environment) |
| ~350MB | ~150MB |
| Can compile Java | Can only RUN Java |

**Why JRE?**

We already built the JAR! We just need to RUN it.

**Comparison:**

```
JDK = JRE + Compiler + Development Tools
JRE = Java Runtime (just enough to run JARs)
```

**Size savings:**

```
JDK: ~350MB
JRE: ~150MB
Savings: 200MB (57% smaller!)
```

---

### Labels for GHCR

```dockerfile
LABEL org.opencontainers.image.source="https://github.com/pdheeraj99/github-actions-learning"
LABEL org.opencontainers.image.description="Todo Backend API - Spring Boot"
LABEL org.opencontainers.image.licenses="MIT"
```

**Why labels?**

GitHub Container Registry (GHCR) uses these to:

- Link image to GitHub repo
- Show description
- Display license info

**Example in GHCR:**

```
📦 pdheeraj99/github-actions-learning/todo-backend
   ├── Description: Todo Backend API - Spring Boot
   ├── Source: github.com/pdheeraj99/github-actions-learning
   └── License: MIT
```

---

### Security: Non-Root User

```dockerfile
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
```

**Why not run as root?**

**Security principle:** Container should have minimal permissions.

**Attack scenario:**

```
Container running as root + vulnerability = Attacker has root access!
Container running as appuser + vulnerability = Attacker has limited access
```

**What this does:**

```
1. Create group: appgroup
2. Create user: appuser (in appgroup)
3. Later: Switch to appuser
```

**In Kubernetes:**

Some clusters **enforce** non-root containers for security!

---

### Copy JAR from Build Stage

```dockerfile
COPY --from=build /app/target/*.jar app.jar
```

**Multi-stage build magic:**

```
┌─────────────────────────────────────────────────────────┐
│  Stage 1 (build)                                        │
│  ────────────────                                       │
│  - Maven + JDK                                          │
│  - Source code                                          │
│  - Dependencies                                         │
│  - Compiled classes                                     │
│  - Tests                                                │
│  - Build tools                                          │
│                                                          │
│  target/todo-backend-1.0.0.jar  ← Copy ONLY this!       │
└─────────────────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────────────────┐
│  Stage 2 (production)                                   │
│  ─────────────────────                                  │
│  - JRE only                                             │
│  - app.jar (the JAR file)                               │
│  - Nothing else!                                        │
└─────────────────────────────────────────────────────────┘
```

**Final image does NOT include:**

- ❌ Maven
- ❌ Source code
- ❌ Test files
- ❌ Build tools

**Final image ONLY includes:**

- ✅ JRE
- ✅ JAR file
- ✅ Runtime dependencies (inside JAR)

---

### Change Ownership

```dockerfile
RUN chown -R appuser:appgroup /app
```

**Why?**

Files copied by root need to be owned by appuser.

**Without this:**

```
appuser tries to write logs → Permission denied!
```

**With this:**

```
/app/
  └── app.jar  (owner: appuser)
```

Now appuser can read/write as needed.

---

### Switch to Non-Root User

```dockerfile
USER appuser
```

**From this point, all commands run as `appuser` (not root)**

When container starts:

```bash
$ whoami
appuser  # Not root!
```

---

### Expose Port

```dockerfile
EXPOSE 8080
```

**What this does:**

Documents that the app listens on port 8080.

**Important:** This is **documentation only**!

Actual port mapping happens when running:

```bash
docker run -p 8080:8080 todo-backend
           ^^^^^^^^^^^
           Host:Container
```

---

### Health Check for Kubernetes

```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1
```

**Parameters explained:**

| Parameter | Value | Meaning |
|-----------|-------|---------|
| `--interval` | 30s | Check every 30 seconds |
| `--timeout` | 3s | Wait max 3s for response |
| `--start-period` | 30s | Grace period (app startup time) |
| `--retries` | 3 | Fail after 3 consecutive failures |

**Health check command:**

```bash
wget -qO- http://localhost:8080/actuator/health
      │││
      ││└─ Output to stdout (dash = stdout)
      │└── Output file (O = output)
      └─── Quiet (q = no progress bar)
```

**Expected response:**

```json
{
  "status": "UP"
}
```

**If health check fails:**

```
Kubernetes → Restarts container
Docker → Marks container as unhealthy
```

**Why `/actuator/health`?**

Spring Boot Actuator provides built-in health endpoint!

**Configured in `application.properties`:**

```properties
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

---

### Entry Point

```dockerfile
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**What happens when container starts:**

```bash
java -jar app.jar
```

**Spring Boot starts:**

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::             (v3.2.0)

...
Tomcat started on port 8080
Started TodoApplication in 8.215 seconds
```

**Container is now running and accepting requests on port 8080!**

---

## 📊 Complete Build Flow Visualization

```
┌─────────────────────────────────────────────────────────────┐
│                     Dockerfile Build                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Stage 1: BUILD (maven:3.9-eclipse-temurin-17-alpine)       │
│  ═══════════════════════════════════════════════════════     │
│                                                              │
│  1. COPY pom.xml                                            │
│     └─→ Layer 1 (cached if pom.xml unchanged)               │
│                                                              │
│  2. RUN mvn dependency:go-offline                           │
│     └─→ Layer 2 (downloads all JARs)                        │
│         ~/.m2/repository/ (~200MB)                          │
│         ↓                                                    │
│         Cached if Layer 1 unchanged! ⚡                      │
│                                                              │
│  3. COPY src                                                │
│     └─→ Layer 3 (source code)                               │
│                                                              │
│  4. RUN mvn package -DskipTests                             │
│     └─→ Compile → Create JAR                                │
│         target/todo-backend-1.0.0.jar                       │
│                                                              │
│  ──────────────────────────────────────────                 │
│                                                              │
│  Stage 2: PRODUCTION (eclipse-temurin:17-jre-alpine)        │
│  ════════════════════════════════════════════════            │
│                                                              │
│  5. COPY --from=build /app/target/*.jar app.jar             │
│     └─→ Extract ONLY the JAR from Stage 1                   │
│                                                              │
│  6. Setup non-root user (appuser)                           │
│  7. EXPOSE 8080                                             │
│  8. HEALTHCHECK (actuator/health)                           │
│  9. ENTRYPOINT ["java", "-jar", "app.jar"]                  │
│                                                              │
│  Final Image Size: ~150MB 🎉                                │
│  (Would be ~600MB without multi-stage!)                     │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🆚 React vs Spring Boot Dockerfile

### Side-by-Side Comparison

| Feature | React | Spring Boot |
|---------|-------|-------------|
| **Base (build)** | `node:20-alpine` | `maven:3.9-temurin-17-alpine` |
| **Base (runtime)** | `nginx:alpine` | `temurin:17-jre-alpine` |
| **Build Tool** | npm | Maven |
| **Dependencies File** | `package.json` | `pom.xml` |
| **Dependency Download** | `npm ci` | `mvn dependency:go-offline` |
| **Build Command** | `npm run build` | `mvn package` |
| **Build Output** | `dist/` (static files) | `target/*.jar` (executable) |
| **Runtime** | nginx serves files | Java runs JAR |
| **Port** | 80 (nginx default) | 8080 (Spring Boot) |
| **Health Check** | No (static files) | Yes (`/actuator/health`) |
| **Entry Point** | nginx | `java -jar` |
| **Final Size** | ~25MB | ~150MB |
| **Security** | nginx user | Custom appuser |

---

### Detailed Comparison

#### React Dockerfile

```dockerfile
# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Runtime stage
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**Serves static files (HTML/JS/CSS) via nginx**

---

#### Spring Boot Dockerfile

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Runs Java application server (Tomcat embedded)**

---

## ⚡ Optimization Techniques

### 1. Layer Order Optimization

**Least frequently changed → Most frequently changed**

```dockerfile
# ✅ GOOD - Optimal order
COPY pom.xml .           # Changes rarely → Layer cached
RUN mvn dependency...     # Depends on pom.xml → Cached
COPY src .               # Changes often → New layer
RUN mvn package          # Depends on src → New layer
```

```dockerfile
# ❌ BAD - Poor order
COPY . .                 # Everything changes → No caching
RUN mvn package          # Always re-downloads dependencies!
```

**Time saved:** 3-5 minutes per build!

---

### 2. Multi-Stage Build

**Single stage (bad):**

```dockerfile
FROM maven:3.9-eclipse-temurin-17-alpine
COPY . .
RUN mvn package
ENTRYPOINT ["java", "-jar", "target/*.jar"]
```

**Final image includes:**

- Maven (~200MB)
- JDK (~600MB total)
- Source code
- Tests
- Build cache

**Total: ~600-800MB** 😱

---

**Multi-stage (good):**

```dockerfile
FROM maven... AS build
# Build JAR

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
```

**Final image includes:**

- JRE only (~150MB)
- JAR file (~50MB)

**Total: ~150MB** 🎉

**Savings: 75-80% smaller!**

---

### 3. Use Alpine Linux

```
maven:3.9-eclipse-temurin-17         → ~600MB
maven:3.9-eclipse-temurin-17-alpine  → ~350MB
```

**Savings: ~250MB (42% smaller)**

**Trade-offs:**

- ✅ Smaller size
- ✅ Faster download
- ⚠️ Uses musl libc (not glibc)
- ⚠️ Some native libraries may have issues

**For most Spring Boot apps: Alpine works perfectly!**

---

## 🎓 Key Takeaways

### What We Learned

1. ✅ **Multi-stage builds** separate build and runtime
2. ✅ **Layer caching** speeds up builds dramatically
3. ✅ **JDK vs JRE** - use JRE for production
4. ✅ **Security** - run as non-root user
5. ✅ **Health checks** for Kubernetes readiness
6. ✅ **Dockerfile organization** for optimal caching
7. ✅ **Alpine Linux** for smaller images
8. ✅ **Fat JAR** contains all dependencies

### Why This Matters in CI/CD

```
GitHub Actions Workflow:
  ↓
Builds Dockerfile
  ↓
Optimized with caching (2-3 min instead of 8 min)
  ↓
Pushes to GHCR
  ↓
Ready for deployment!
```

**Fast builds = Faster feedback = Better developer experience!** 🚀

---

**Ippudu Docker kuda clear ga artham ayyinda?** (Is Docker also clear now?)

You now understand Spring Boot Docker concepts as deeply as React Docker concepts! 🎉
