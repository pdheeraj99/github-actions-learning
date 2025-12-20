# 🐳 Docker for Spring Boot

Multi-stage builds, JDK vs JRE, and layer caching for Java apps!

---

## 🎯 Telugu Simple ga

> "JDK = Compiler + Runtime (heavy)"
> "JRE = Runtime only (light) - production lo idhi chaalu!"

---

## 📄 Complete Dockerfile Explained

```dockerfile
# ════════════════════════════════════════════════════════════════════════════════
# STAGE 1: BUILD (JDK + Maven needed to compile)
# ════════════════════════════════════════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Step 1: Copy pom.xml FIRST (layer caching magic!)
COPY pom.xml .

# Step 2: Download ALL dependencies
RUN mvn dependency:go-offline -B

# Step 3: Copy source code
COPY src ./src

# Step 4: Build JAR (skip tests - already ran in CI!)
RUN mvn package -DskipTests -B


# ════════════════════════════════════════════════════════════════════════════════
# STAGE 2: PRODUCTION (JRE only - no compiler needed!)
# ════════════════════════════════════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Security: Run as non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

# Health check for Kubernetes
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🎭 Multi-Stage Build Magic

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         MULTI-STAGE BUILD                                        │
│                                                                                  │
│   ┌───────────────────────────────────────────────────────────────────────┐     │
│   │  STAGE 1: BUILD (maven:3.9-eclipse-temurin-17-alpine)                 │     │
│   │                                                                        │     │
│   │  Contains:                                                             │     │
│   │  • Maven (build tool)                                                  │     │
│   │  • JDK 17 (compiler + runtime)                                        │     │
│   │  • pom.xml + src/ + target/                                           │     │
│   │  • All dependencies in ~/.m2                                          │     │
│   │                                                                        │     │
│   │  Size: ~350 MB                                                        │     │
│   │                                                                        │     │
│   │  OUTPUT: target/app.jar ─────────────────────────┐                    │     │
│   │                                                  │                    │     │
│   └──────────────────────────────────────────────────┼────────────────────┘     │
│                                                      │                           │
│                              COPY --from=build      ▼                           │
│                                                      │                           │
│   ┌──────────────────────────────────────────────────▼────────────────────┐     │
│   │  STAGE 2: PRODUCTION (eclipse-temurin:17-jre-alpine)                  │     │
│   │                                                                        │     │
│   │  Contains:                                                             │     │
│   │  • JRE 17 ONLY (runtime, no compiler)                                 │     │
│   │  • app.jar (just the built JAR!)                                      │     │
│   │                                                                        │     │
│   │  Size: ~150 MB (57% smaller!)                                         │     │
│   │                                                                        │     │
│   │  STAGE 1 IS DISCARDED! Only JAR is kept!                              │     │
│   │                                                                        │     │
│   └───────────────────────────────────────────────────────────────────────┘     │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🆚 JDK vs JRE

| Aspect | JDK | JRE |
|--------|-----|-----|
| **Full Name** | Java Development Kit | Java Runtime Environment |
| **Purpose** | Compile + Run | Run only |
| **Contains** | Compiler (javac) + JRE | Just runtime |
| **Size** | ~350 MB | ~150 MB |
| **Use Case** | Build stage | Production stage |

```
JDK = JRE + Development Tools (javac, jdb, etc.)
JRE = Just enough to run JAR files

Production lo JDK waste! JRE chaalu!
```

---

## ⚡ Layer Caching Strategy

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         LAYER CACHING                                            │
│                                                                                  │
│   ORDER MATTERS! Less changing files FIRST, more changing files LAST!           │
│                                                                                  │
│   COPY pom.xml                    ← Changes RARELY (Layer 1)                    │
│   RUN mvn dependency:go-offline   ← Cached until pom.xml changes! (Layer 2)    │
│   COPY src                        ← Changes FREQUENTLY (Layer 3)               │
│   RUN mvn package                 ← Runs every build (Layer 4)                  │
│                                                                                  │
│   ═══════════════════════════════════════════════════════════════════════════   │
│                                                                                  │
│   BUILD 1 (First time):                                                          │
│   ─────────────────────                                                          │
│   COPY pom.xml               → Layer created                                    │
│   RUN mvn dependency:...     → 3 minutes download! Layer created               │
│   COPY src                   → Layer created                                    │
│   RUN mvn package            → Layer created                                    │
│                                                                                  │
│   BUILD 2 (Only code changed):                                                   │
│   ────────────────────────────                                                   │
│   COPY pom.xml               → ⚡ CACHED! (pom.xml same)                        │
│   RUN mvn dependency:...     → ⚡ CACHED! (no need to download)                 │
│   COPY src                   → New layer (code changed)                         │
│   RUN mvn package            → New layer                                        │
│                                                                                  │
│   RESULT: ~3 minutes saved! 🎉                                                   │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🆚 React vs Spring Boot Docker

| Aspect | React | Spring Boot |
|--------|-------|-------------|
| **Build Base** | `node:20-alpine` | `maven:3.9-temurin-17` |
| **Run Base** | `nginx:alpine` | `temurin:17-jre-alpine` |
| **Build Tool** | npm | Maven |
| **Config File** | `package.json` | `pom.xml` |
| **Output** | Static files (dist/) | JAR file |
| **Web Server** | nginx serves files | Java runs app |
| **Final Size** | ~30 MB | ~150 MB |

---

## 🔐 Security Best Practice

```dockerfile
# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Change ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root
USER appuser
```

**Why?**

- Container runs as `appuser`, not `root`
- If hacked, attacker has limited permissions
- Kubernetes clusters often REQUIRE non-root!

---

## 🎬 Telugu Summary

```
Multi-Stage Build:
──────────────────
Stage 1 (BUILD): JDK + Maven → Compile code → Create JAR
Stage 2 (PROD):  JRE only → Copy JAR → Run app

Stage 1 is DISCARDED! Only JAR survives!
Result: 350 MB → 150 MB (57% smaller!)

JDK vs JRE:
───────────
JDK = Compiler + Runtime (development lo use)
JRE = Runtime only (production lo use)

Layer Caching:
──────────────
pom.xml FIRST copy cheyyi → Dependencies cache avtayi
src/ LAST copy cheyyi → Frequent changes here

Security:
─────────
Non-root user create cheyyi
Production lo root user use cheyyaddu!
```

---

**Related: [Docker Caching](../03-caching-explained/05-docker-caching.md)** 👉
