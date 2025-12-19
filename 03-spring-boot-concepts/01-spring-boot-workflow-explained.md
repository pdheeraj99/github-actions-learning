# Spring Boot Workflow - Complete Breakdown

## Overview

This document explains the Spring Boot CI/CD workflow (`spring-boot-build.yml`) step by step, similar to how we learned Docker and React concepts.

---

## 📋 Workflow Structure

```yaml
name: 🚀 Spring Boot CI/CD

on:                    # When does this run?
  push:               # On push to main
  pull_request:       # On PR to main  
  workflow_dispatch:  # Manual trigger
```

### Triggers Explained

| Trigger | When | Why |
|---------|------|-----|
| `push.branches: [main]` | Code pushed to main | Auto-deploy latest changes |
| `push.paths` | Only if Spring Boot files change | Don't run if React files change |
| `pull_request` | PR created | Test before merging |
| `workflow_dispatch` | Manual button click | Test anytime, skip tests option |

**Smart Path Filtering:**

```yaml
paths:
  - '02-spring-boot-pipeline/**'
  - '.github/workflows/spring-boot-build.yml'
```

**Adhi enti?** (What is this?)

- Workflow runs ONLY if these folders/files change
- If you change React code, Spring Boot workflow won't run (saves time + money)

---

## 🌍 Environment Variables

```yaml
env:
  REGISTRY: ghcr.io
  IMAGE_NAME: ${{ github.repository }}/todo-backend
  JAVA_VERSION: '17'
```

**Why separate environment variables?**

- **Reusability:** Use `${{ env.JAVA_VERSION }}` everywhere
- **Easy updates:** Change Java version in one place
- **Consistency:** Same values across all steps

**`${{ github.repository }}`** → Expands to `pdheeraj99/github-actions-learning`

---

## 🔨 Job 1: Build & Test

### Architecture

```
Runner (Ubuntu)
    ↓
Checkout Code
    ↓
Setup Java 17 + Maven
    ↓
Build (compile Java files)
    ↓
Test (run JUnit tests)
    ↓
Package (create JAR file)
    ↓
Upload Artifacts
```

---

### Step-by-Step Breakdown

#### Step 1: Checkout Code

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

**Same as React workflow** - Downloads your code to the runner.

---

#### Step 2: Setup Java ☕

```yaml
- name: ☕ Setup JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'     # Eclipse Temurin (OpenJDK)
    cache: maven                 # 🔥 Important!
```

**Key Concepts:**

**`distribution: 'temurin'`**

Similar to how we choose Node version, we choose JDK distribution:

- **Temurin** = Eclipse's free OpenJDK (recommended)
- Alternatives: `zulu`, `corretto`, `microsoft`

**`cache: maven`** 🚀

**This is CRITICAL for speed!**

```
First Run:
Maven downloads dependencies → 2-3 minutes ⏱️

Subsequent Runs (with cache):
Maven uses cached dependencies → 10-20 seconds ⚡
```

**How Maven caching works:**

```
~/.m2/repository/
  ├── org/springframework/...
  ├── com/h2database/...
  └── ... (all dependencies)
```

GitHub Actions caches this folder!

**Compare with React:**

```
React:  cache: npm  → caches node_modules/
Spring: cache: maven → caches ~/.m2/repository/
```

---

#### Step 3: Build with Maven 🔨

```yaml
- name: 🔨 Build with Maven
  run: mvn clean compile -B
```

**Maven Lifecycle:**

```
mvn clean compile
    │
    ├─→ clean    (delete old /target folder)
    └─→ compile  (Java → .class files)
```

**`-B` flag** = "Batch mode" (no interactive prompts, CI-friendly)

**What happens:**

```
src/main/java/com/example/todo/
  ├── TodoApplication.java
  ├── entity/Todo.java
  └── controller/TodoController.java
         ↓  (Maven compile)
target/classes/com/example/todo/
  ├── TodoApplication.class
  ├── entity/Todo.class
  └── controller/TodoController.class
```

---

#### Step 4: Run Tests 🧪

```yaml
- name: 🧪 Run Tests
  if: ${{ github.event.inputs.skip_tests != 'true' }}
  run: mvn test -B
```

**Conditional execution:**

- Normal push/PR → Always runs tests
- Manual trigger → Can skip tests (checkbox)

**Maven Test Lifecycle:**

```
mvn test
  ↓
Runs all JUnit tests in src/test/
  ↓
Generates reports in target/surefire-reports/
```

**Our tests:**

- `TodoApplicationTests.java` → Context loading
- `TodoControllerTest.java` → 10 REST API tests

---

#### Step 5: Package JAR 📦

```yaml
- name: 📦 Package JAR
  run: mvn package -DskipTests -B
```

**Why `-DskipTests`?**

We already ran tests in Step 4! No need to run again.

**What Maven package does:**

```
1. Compiles code (if not done)
2. Runs tests (skipped with -DskipTests)
3. Creates JAR file:

target/
  └── todo-backend-1.0.0.jar  ← Executable JAR!
```

**Spring Boot JAR structure:**

```
todo-backend-1.0.0.jar
  ├── BOOT-INF/
  │   ├── classes/          ← Your compiled code
  │   └── lib/              ← All dependencies!
  ├── META-INF/
  └── org/springframework/boot/loader/  ← Spring Boot magic
```

**"Fat JAR" / "Uber JAR":**

- Contains your code + ALL dependencies
- Can run standalone: `java -jar todo-backend.jar`
- No need to install Spring, H2, etc. separately!

---

#### Step 6: Upload Test Results 📊

```yaml
- name: 📊 Upload Test Results
  if: always()
  uses: actions/upload-artifact@v4
  with:
    name: test-results
    path: ./02-spring-boot-pipeline/todo-backend/target/surefire-reports/
    retention-days: 7
```

**`if: always()`** → Upload even if tests FAIL!

**Why?**

- See which tests failed
- Download detailed reports
- Debug failures

**Test reports include:**

- XML files (machine-readable)
- TXT files (human-readable)
- Test timing info

---

#### Step 7: Upload JAR 📦

```yaml
- name: 📦 Upload JAR
  uses: actions/upload-artifact@v4
  with:
    name: todo-backend-jar
    path: ./02-spring-boot-pipeline/todo-backend/target/*.jar
    retention-days: 7
```

**Why upload JAR?**

1. Download and run locally
2. Deploy manually if needed
3. Archive for 7 days
4. Debug production issues

**Artifact location:**
GitHub Actions → Workflow run → Artifacts section

---

## 🐳 Job 2: Docker Build & Push

### Job Dependencies

```yaml
docker-build:
  needs: build-and-test    # Wait for Job 1 to finish
  if: github.event_name == 'push' || github.event_name == 'workflow_dispatch'
```

**Why `needs`?**

- Don't build Docker image if tests fail!
- Sequential execution: Test → Build → Push

**Why the `if` condition?**

- PRs: Only test, don't push images
- Main branch: Test + push images
- Manual: Can do both

---

### Permissions

```yaml
permissions:
  contents: read      # Read repo code
  packages: write     # Push to GHCR
```

**Principle of Least Privilege:**

- Only give permissions needed for this job
- Can't accidentally delete code
- Can only push packages

---

### Docker Build Steps

#### Steps 1-4: Same as React

```yaml
1. Checkout code
2. Login to GHCR
3. Setup Buildx
4. Extract metadata (tags)
```

**Reusing knowledge:**
These are identical to React workflow. We already understand them!

---

#### Step 5: Build and Push

```yaml
- name: 🐳 Build and Push
  uses: docker/build-push-action@v5
  with:
    context: ./02-spring-boot-pipeline/todo-backend
    push: true
    tags: ${{ steps.meta.outputs.tags }}
    cache-from: type=gha
    cache-to: type=gha,mode=max
```

**Key difference from React:**

| Aspect | React | Spring Boot |
|--------|-------|-------------|
| **Context** | `todo-frontend/` | `todo-backend/` |
| **Base image** | Node Alpine | Maven + JRE Alpine |
| **Build tool** | npm | Maven |
| **Final size** | ~50MB | ~150MB (JRE larger) |

**Docker Cache Strategy:**

```
First build:
- Download Maven
- Download dependencies
- Compile Java
- Total: 5-8 minutes

Subsequent builds (cached):
- Use cached layers
- Only recompile changed files
- Total: 1-2 minutes
```

---

## 🎯 Complete Flow Visualization

```
┌─────────────────────────────────────────────────────────────┐
│                     GitHub Actions Runner                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Job 1: Build & Test                                         │
│  ════════════════════                                        │
│                                                              │
│  1. ✅ Checkout code                                         │
│  2. ☕ Install Java 17 (with Maven cache)                    │
│  3. 🔨 mvn clean compile (Java → .class)                     │
│  4. 🧪 mvn test (run JUnit tests)                            │
│  5. 📦 mvn package (create JAR)                              │
│  6. 📊 Upload test-results.zip                               │
│  7. 📦 Upload todo-backend.jar                               │
│                                                              │
│  ↓ (needs: build-and-test)                                   │
│                                                              │
│  Job 2: Docker Build & Push                                  │
│  ═══════════════════════                                     │
│                                                              │
│  1. ✅ Checkout code                                         │
│  2. 🔐 Login to GHCR                                         │
│  3. 🔧 Setup Buildx                                          │
│  4. 📋 Generate tags (latest, SHA)                           │
│  5. 🐳 Build Docker image (multi-stage)                      │
│     ├─ Stage 1: Maven build                                  │
│     └─ Stage 2: JRE runtime                                  │
│  6. 🚀 Push to ghcr.io                                       │
│  7. 📝 Print summary                                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚡ Performance Optimizations

### 1. Maven Dependency Caching

**Without cache:**

```
Step: Setup Java - 2m 45s
```

**With cache:**

```
Step: Setup Java - 8s
```

**Savings: ~2m 30s per build!**

---

### 2. Docker Layer Caching

**Dockerfile strategy:**

```dockerfile
# Copy pom.xml FIRST
COPY pom.xml .
RUN mvn dependency:go-offline

# Then copy source code
COPY src ./src
RUN mvn package
```

**Why this order?**

- `pom.xml` rarely changes → Dependencies cached
- Source code changes often → Only recompile code
- Docker reuses cached dependency layer!

---

### 3. Path Filtering

**Workflow only runs when Spring Boot files change:**

```yaml
paths:
  - '02-spring-boot-pipeline/**'
```

**Real example:**

- Change React code → Only React workflow runs
- Change Spring Boot code → Only Spring Boot workflow runs
- Change both → Both workflows run

**Savings: ~50% fewer unnecessary builds!**

---

## 📊 Comparison: React vs Spring Boot

| Feature | React Workflow | Spring Boot Workflow |
|---------|----------------|---------------------|
| **Language** | JavaScript/TypeScript | Java |
| **Runtime** | Node.js | JVM (Java Virtual Machine) |
| **Package Manager** | npm | Maven |
| **Dependency File** | `package.json` | `pom.xml` |
| **Lock File** | `package-lock.json` | None (Maven downloads latest compatible) |
| **Build Command** | `npm run build` | `mvn package` |
| **Test Command** | `npm test` | `mvn test` |
| **Output** | Static files (`dist/`) | JAR file (`target/*.jar`) |
| **Docker Base (build)** | `node:20-alpine` | `maven:3.9-eclipse-temurin-17-alpine` |
| **Docker Base (runtime)** | `nginx:alpine` | `eclipse-temurin:17-jre-alpine` |
| **Final Image Size** | ~25MB | ~150MB |
| **Caching Strategy** | `cache: npm` | `cache: maven` |
| **Build Time (no cache)** | 2-3 min | 5-8 min |
| **Build Time (cached)** | 30s | 1-2 min |

---

## 🎓 Key Takeaways

### What We Learned

1. ✅ **Spring Boot workflow structure** (triggers, jobs, steps)
2. ✅ **Maven lifecycle** (compile, test, package)
3. ✅ **Java setup and caching** mechanisms
4. ✅ **JAR file packaging** (Fat JAR concept)
5. ✅ **Artifact upload** (test results + JAR)
6. ✅ **Docker multi-stage builds** for Java
7. ✅ **Job dependencies** (`needs`)
8. ✅ **Conditional execution** (`if`)
9. ✅ **Path filtering** optimization
10. ✅ **Comparison with React** workflow

### Ready for Next Level:

Now that we understand the workflow completely, we can:

- Add code coverage reports
- Add integration tests
- Deploy to OpenShift
- Add database migrations
- Implement blue-green deployments

---

**Ippudu clear ga artham ayyinda?** (Is it clear now?)

The Spring Boot workflow is more complex than React (Java compilation, Maven lifecycle, JAR packaging) but follows similar CI/CD principles!
