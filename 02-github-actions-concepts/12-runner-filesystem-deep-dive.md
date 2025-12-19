# GitHub Actions Runner - File System Deep Dive

## మీ Question చాలా బాగుంది! (Your question is excellent!)

**You're RIGHT** - Runners are Virtual Machines! కానీ setup actions ఎందుకు use చేస్తాం? Let's understand!

---

## 🖥️ GitHub Actions Runner - What is it?

### Simple Telugu Explanation

**Runner = Virtual Machine (VM)** అని అనుకోవచ్చు, కానీ exact ga చెప్పాలంటే:

```
Runner = Fresh Ubuntu VM (or Windows/Mac)
         └─→ Spins up for YOUR workflow
         └─→ Executes YOUR jobs
         └─→ Destroys after completion
```

**Key Point:** ప్రతి workflow run కి కొత్త VM create అవుతుంది! (New VM for each run!)

---

## 📦 What's Pre-Installed on `ubuntu-latest` Runner?

### GitHub Pre-Installs చాలా Software

```
ubuntu-latest runner includes:
  ├── Operating System: Ubuntu 22.04 LTS
  ├── Docker: 24.0.x
  ├── Node.js: 18.x, 20.x (multiple versions!)
  ├── Python: 3.9, 3.10, 3.11
  ├── Java: 8, 11, 17, 21 (Temurin)
  ├── Go, Ruby, PHP, .NET
  ├── Git, npm, yarn, pip, maven, gradle
  ├── AWS CLI, Azure CLI, Google Cloud SDK
  └── ... 100+ other tools!
```

**Full list:** <https://github.com/actions/runner-images/blob/main/images/ubuntu/Ubuntu2204-Readme.md>

---

## 🤔 మరి Setup Java ఎందుకు Use చేయాలి? (Why use setup-java then?)

### Excellent Question! Here's why

### Problem 1: Multiple Java Versions

```
Runner has:
  ├── Java 8 at /usr/lib/jvm/java-8-openjdk
  ├── Java 11 at /usr/lib/jvm/java-11-openjdk  
  ├── Java 17 at /usr/lib/jvm/java-17-openjdk ← We want this!
  └── Java 21 at /usr/lib/jvm/java-21-openjdk

Default: java --version → Java 11 (not 17!)
```

**Issue:** We need Java 17, but default is 11!

---

### Problem 2: JAVA_HOME Environment Variable

```bash
# Before setup-java
echo $JAVA_HOME
→ /usr/lib/jvm/java-11-openjdk  ❌ Wrong version!

# After setup-java@v4 with java-version: 17
echo $JAVA_HOME
→ /usr/lib/jvm/java-17-openjdk  ✅ Correct version!
```

**What setup-java does:**

1. Sets `JAVA_HOME` to Java 17
2. Updates `PATH` to use Java 17 first
3. Sets up Maven/Gradle to use Java 17

---

### Problem 3: Maven Cache

```yaml
- uses: actions/setup-java@v4
  with:
    cache: maven  # 🔥 This is the MAIN reason!
```

**Without cache:**

```
~/.m2/repository/ is EMPTY
Maven downloads ALL dependencies → 3-5 minutes ⏱️
```

**With `setup-java` cache:**

```
~/.m2/repository/ restored from cache
Maven skips downloading → 10-20 seconds ⚡

Savings: 3+ minutes EVERY build!
```

**GitHub charges by minute** → Faster builds = Less cost!

---

### Problem 4: Distribution Choice

```yaml
- uses: actions/setup-java@v4
  with:
    distribution: 'temurin'  # We want Eclipse Temurin
```

**Pre-installed Java might be:**

- OpenJDK (generic)
- We want: Temurin (specific distribution)

**Different distributions:**

- `temurin` - Eclipse Temurin (formerly AdoptOpenJDK)
- `zulu` - Azul Zulu
- `corretto` - Amazon Corretto
- `microsoft` - Microsoft Build of OpenJDK

**Each has slight differences in performance, licensing, support!**

---

## 📂 Runner File System - Step by Step Visualization

### Initial State (Empty Runner)

```
/home/runner/
  ├── work/
  │   └── github-actions-learning/     ← EMPTY (no code yet)
  │       └── github-actions-learning/ ← Will be created
  ├── .m2/
  │   └── repository/                  ← EMPTY (no dependencies)
  └── _temp/                           ← Temporary files

/usr/lib/jvm/
  ├── java-8-openjdk/
  ├── java-11-openjdk/   ← Default JAVA_HOME
  ├── java-17-openjdk/
  └── java-21-openjdk/

Environment:
JAVA_HOME=/usr/lib/jvm/java-11-openjdk
PATH=/usr/local/bin:/usr/bin:/bin
```

---

## 🔄 Step-by-Step File System Changes

### Step 1: Checkout Code

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

**What happens:**

```
/home/runner/work/github-actions-learning/github-actions-learning/
  ├── .git/                           ← Downloaded from GitHub
  ├── .github/
  │   └── workflows/
  │       └── spring-boot-build.yml
  ├── 02-spring-boot-pipeline/
  │   └── todo-backend/
  │       ├── pom.xml                 ← Maven config
  │       ├── src/
  │       │   ├── main/java/
  │       │   └── test/java/
  │       └── Dockerfile
  └── ... (all repo files)

Size: ~10-50 MB
```

**Telugu Explanation:**

GitHub nundi మీ code అంతా download chesindi runner ki. Git repository motham copy ayyindi!

---

### Step 2: Setup Java

```yaml
- name: ☕ Setup JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: maven
```

**File System Changes:**

```
Step 2.1: Check cache
─────────────────────
Looking for: ~/.m2/repository/ from previous runs
Key: Linux-maven-<hash of pom.xml>

If found:
  Restore from GitHub cache → ~/.m2/repository/
  Size: ~200-500 MB (all dependencies!)
  Time: 10-20 seconds ⚡

If NOT found:
  ~/.m2/repository/ remains EMPTY
  Will download later
  Time: 0 seconds (but will be slow later)

Step 2.2: Set JAVA_HOME
────────────────────────
JAVA_HOME=/usr/lib/jvm/java-17-openjdk  ← Changed!
PATH=/usr/lib/jvm/java-17-openjdk/bin:$PATH

Environment:
JAVA_HOME=/usr/lib/jvm/java-17-openjdk  ✅

Verification:
$ java -version
openjdk version "17.0.9" 2023-10-17  ✅
```

**Telugu Explanation:**

Java 17 ni default ga set chesindi. Plus, previous build lo download chesina dependencies (JARs) cache nundi restore chesindi. Idhi chala important - dependencies malli download cheyyakkarledu!

**ASCII Diagram:**

```
┌────────────────────────────────────────────────────────┐
│  ~/.m2/repository/ (Maven Local Repository)           │
│  ══════════════════════════════════════════════        │
│                                                         │
│  org/                                                   │
│    └── springframework/                                │
│        ├── spring-boot/3.2.0/                          │
│        │   └── spring-boot-3.2.0.jar     [2.1 MB]     │
│        ├── spring-web/6.1.2/                           │
│        │   └── spring-web-6.1.2.jar      [1.8 MB]     │
│        └── spring-core/6.1.2/                          │
│            └── spring-core-6.1.2.jar     [1.5 MB]     │
│                                                         │
│  com/                                                   │
│    └── h2database/                                     │
│        └── h2/2.2.224/                                 │
│            └── h2-2.2.224.jar           [2.3 MB]      │
│                                                         │
│  ... (100+ more dependencies)                          │
│                                                         │
│  Total Size: ~200-500 MB                               │
│  Status: ✅ RESTORED FROM CACHE                        │
│                                                         │
└────────────────────────────────────────────────────────┘
```

---

### Step 3: Build with Maven

```yaml
- name: 🔨 Build with Maven
  run: mvn clean compile -B
  working-directory: ./02-spring-boot-pipeline/todo-backend
```

**File System Changes:**

```
BEFORE:
/home/runner/work/.../todo-backend/
  ├── pom.xml
  ├── src/
  │   └── main/java/...
  └── target/                  ← Doesn't exist yet

AFTER:
/home/runner/work/.../todo-backend/
  ├── pom.xml
  ├── src/
  │   └── main/java/...
  └── target/                  ← CREATED!
      ├── classes/             ← Compiled .class files
      │   └── com/example/todo/
      │       ├── TodoApplication.class
      │       ├── controller/
      │       │   └── TodoController.class
      │       ├── service/
      │       │   └── TodoService.class
      │       ├── repository/
      │       │   └── TodoRepository.class
      │       └── entity/
      │           └── Todo.class
      └── maven-status/
          └── ... (build info)

Size added: ~5 MB
```

**Telugu Explanation:**

Maven `src/main/java/` lo unna `.java` files ni compile chesi `.class` files ga `target/classes/` lo pettindi.

**ASCII Diagram:**

```
┌──────────────────────────────────────────────────────┐
│  COMPILATION PROCESS                                 │
├──────────────────────────────────────────────────────┤
│                                                       │
│  src/main/java/                                      │
│    └── com/example/todo/                             │
│        └── TodoController.java                       │
│                                                       │
│            ↓  javac (Java Compiler)                  │
│            ↓                                          │
│                                                       │
│  target/classes/                                     │
│    └── com/example/todo/                             │
│        └── TodoController.class  ← Bytecode          │
│                                                       │
└──────────────────────────────────────────────────────┘
```

---

### Step 4: Run Tests

```yaml
- name: 🧪 Run Tests
  run: mvn test -B
```

**File System Changes:**

```
target/
  ├── classes/              (from previous step)
  ├── test-classes/         ← NEW! Compiled test files
  │   └── com/example/todo/
  │       ├── TodoApplicationTests.class
  │       └── controller/
  │           └── TodoControllerTest.class
  ├── surefire-reports/     ← NEW! Test results
  │   ├── TEST-com.example.todo.TodoApplicationTests.xml
  │   ├── com.example.todo.TodoApplicationTests.txt
  │   ├── TEST-com.example.todo.controller.TodoControllerTest.xml
  │   └── com.example.todo.controller.TodoControllerTest.txt
  └── maven-status/

Size added: ~10 MB
```

**Telugu Explanation:**

Test files compile ayyayi, tests run ayyayi, results `surefire-reports/` lo save ayyayi.

**Test Report Content:**

```xml
<!-- TEST-TodoControllerTest.xml -->
<?xml version="1.0" encoding="UTF-8"?>
<testsuite tests="10" failures="0" errors="0" skipped="0" time="10.94">
  <testcase name="getAllTodos_ShouldReturnTodoList" time="0.234"/>
  <testcase name="getTodoById_ShouldReturnTodo" time="0.156"/>
  <testcase name="createTodo_ShouldCreateAndReturnTodo" time="0.189"/>
  ...
</testsuite>
```

---

### Step 5: Package JAR

```yaml
- name: 📦 Package JAR
  run: mvn package -DskipTests -B
```

**File System Changes:**

```
target/
  ├── classes/
  ├── test-classes/
  ├── surefire-reports/
  ├── maven-archiver/
  │   └── pom.properties
  ├── maven-status/
  └── todo-backend-1.0.0.jar  ← NEW! The FAT JAR! 🎉

JAR file structure (internal):
┌────────────────────────────────────┐
│ todo-backend-1.0.0.jar             │
│ ══════════════════════════════     │
│                                     │
│ BOOT-INF/                           │
│   ├── classes/                      │
│   │   └── (your .class files)      │
│   └── lib/                          │
│       ├── spring-boot-3.2.0.jar     │
│       ├── spring-web-6.1.2.jar      │
│       ├── h2-2.2.224.jar            │
│       └── ... (50+ JARs)            │
│                                     │
│ META-INF/                           │
│   └── MANIFEST.MF                   │
│                                     │
│ org/springframework/boot/loader/    │
│   └── JarLauncher.class             │
│                                     │
└────────────────────────────────────┘

Size: ~45-55 MB (self-contained!)
```

**Telugu Explanation:**

Ee JAR file lo your code + all dependencies anni unnai. Idi "Fat JAR" or "Uber JAR". Standalone ga run avuddhi - `java -jar todo-backend.jar` chepthe start avuddhi!

---

### Step 6: Upload Test Results

```yaml
- name: 📊 Upload Test Results
  uses: actions/upload-artifact@v4
  with:
    name: test-results
    path: ./02-spring-boot-pipeline/todo-backend/target/surefire-reports/
```

**What happens:**

```
GitHub Actions stores artifact:
  ├── test-results.zip
  │   └── (surefire-reports/ contents)
  │
  Stored for: 7 days
  Location: GitHub servers (artifact storage)
  Accessible: From workflow run page

Runner file system: UNCHANGED
(Files are uploaded to GitHub, not stored on runner)
```

**Telugu Explanation:**

Test results GitHub ki upload ayyayi. 7 days varuku download cheyochu. Runner lo delete avvakunda GitHub lo safe ga unnai.

---

### Step 7: Upload JAR

```yaml
- name: 📦 Upload JAR
  uses: actions/upload-artifact@v4
  with:
    name: todo-backend-jar
    path: ./02-spring-boot-pipeline/todo-backend/target/*.jar
```

**What happens:**

```
GitHub Actions stores artifact:
  ├── todo-backend-jar.zip
  │   └── todo-backend-1.0.0.jar (45-55 MB)
  │
  Stored for: 7 days
  Location: GitHub servers
  Can download and run locally!

Runner file system: UNCHANGED
```

---

## 🐳 Job 2: Docker Build (NEW RUNNER!)

### Important: New VM for Job 2

```
Job 1 ends → Runner 1 DESTROYED! 💥
Job 2 starts → NEW Runner 2 created! 🆕
```

**Why?**

```yaml
docker-build:
  needs: build-and-test  # Waits for Job 1
  runs-on: ubuntu-latest  # NEW VM!
```

**File System: COMPLETELY FRESH!**

```
/home/runner/
  ├── work/
  │   └── (EMPTY - no code yet!)
  └── .m2/
      └── repository/ (EMPTY!)

All previous files GONE!
Must checkout code again!
```

**Telugu Explanation:**

Job 1 ayyaka aa runner destroy ayyindi. Job 2 ki kotta runner create ayyindi. Anduke, code malli checkout cheyyali, dependencies malli download avvachu (unless Docker cache works).

---

### Docker Build Steps

#### Step 1: Checkout (again!)

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

**File System:**

```
/home/runner/work/.../
  ├── 02-spring-boot-pipeline/
  │   └── todo-backend/
  │       ├── Dockerfile  ← We need this!
  │       ├── pom.xml
  │       └── src/
  └── ... (all repo files)
```

---

#### Step 2-4: Docker Setup

```yaml
- name: 🔐 Login to GHCR
- name: 🔧 Setup Docker Buildx
- name: 📋 Extract Metadata
```

**File System Changes:**

```
~/.docker/
  └── config.json  ← GHCR credentials

/var/lib/docker/
  └── buildx/
      └── (buildx configuration)

No major file system changes
```

---

#### Step 5: Build Docker Image

```yaml
- name: 🐳 Build and Push
  uses: docker/build-push-action@v5
  with:
    context: ./02-spring-boot-pipeline/todo-backend
    cache-from: type=gha
```

**What happens INSIDE Docker:**

```
┌──────────────────────────────────────────────────────────────┐
│  Docker Build Process (Multi-Stage)                          │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  STAGE 1: BUILD                                              │
│  ═══════════════                                             │
│                                                               │
│  FROM maven:3.9-eclipse-temurin-17-alpine                    │
│    ↓                                                          │
│  /app/                                                        │
│    ├── pom.xml (copied)                                      │
│    └── dependencies downloaded to:                           │
│        /root/.m2/repository/                                 │
│          ├── org/springframework/...                         │
│          ├── com/h2database/...                              │
│          └── ... (200+ MB)                                   │
│    ↓                                                          │
│  /app/                                                        │
│    ├── src/ (copied)                                         │
│    └── target/                                               │
│        └── todo-backend-1.0.0.jar (built!)                   │
│                                                               │
│  STAGE 2: PRODUCTION                                         │
│  ════════════════════                                        │
│                                                               │
│  FROM eclipse-temurin:17-jre-alpine                          │
│    ↓                                                          │
│  /app/                                                        │
│    └── app.jar (copied from Stage 1)                         │
│                                                               │
│  Final Image Size: ~150 MB                                   │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

**Runner File System:**

```
/var/lib/docker/
  ├── overlay2/
  │   └── (Docker image layers)
  │       ├── layer-1-base-jre/
  │       ├── layer-2-app-jar/
  │       └── layer-3-metadata/
  └── image/
      └── overlay2/
          └── imagedb/
              └── content/sha256/
                  └── abc123... (image manifest)

GitHub Actions Cache:
  └── docker-buildx-cache/
      └── (cached layers from previous builds)
```

---

#### Step 6: Push to GHCR

**File System: UNCHANGED**

Docker image pushed to:

```
ghcr.io/pdheeraj99/github-actions-learning/todo-backend:latest
ghcr.io/pdheeraj99/github-actions-learning/todo-backend:a70dce3

(Stored on GitHub servers, not runner!)
```

---

## 📊 Complete Runner Lifecycle

```
┌───────────────────────────────────────────────────────────────┐
│                                                                │
│  Workflow Triggered                                           │
│         ↓                                                      │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  RUNNER 1 (ubuntu-latest)                            │    │
│  │  ════════════════════════                            │    │
│  │                                                       │    │
│  │  Start: Clean Ubuntu VM                              │    │
│  │  Pre-installed: Java 8,11,17,21, Node, Python, etc. │    │
│  │                                                       │    │
│  │  Job 1: build-and-test                               │    │
│  │    Step 1: Checkout          → +10 MB (code)         │    │
│  │    Step 2: Setup Java 17     → +200 MB (cache)       │    │
│  │    Step 3: mvn compile       → +5 MB (classes)       │    │
│  │    Step 4: mvn test          → +10 MB (test results) │    │
│  │    Step 5: mvn package       → +50 MB (JAR)          │    │
│  │    Step 6: Upload artifacts  → (to GitHub)           │    │
│  │    Step 7: Upload JAR        → (to GitHub)           │    │
│  │                                                       │    │
│  │  Total disk used: ~275 MB                            │    │
│  │                                                       │    │
│  └──────────────────────────────────────────────────────┘    │
│         ↓                                                      │
│  RUNNER 1 DESTROYED 💥                                        │
│         ↓                                                      │
│  ┌──────────────────────────────────────────────────────┐    │
│  │  RUNNER 2 (ubuntu-latest) - FRESH VM!                │    │
│  │  ════════════════════════════════════                │    │
│  │                                                       │    │
│  │  Start: Clean Ubuntu VM (EMPTY!)                     │    │
│  │                                                       │    │
│  │  Job 2: docker-build                                 │    │
│  │    Step 1: Checkout          → +10 MB (code)         │    │
│  │    Step 2: Login GHCR        → (credentials)         │    │
│  │    Step 3: Setup Buildx      → (Docker config)       │    │
│  │    Step 4: Metadata          → (tags)                │    │
│  │    Step 5: Docker build      → +500 MB (layers)      │    │
│  │    Step 6: Push to GHCR      → (to GitHub)           │    │
│  │                                                       │    │
│  │  Total disk used: ~510 MB                            │    │
│  │                                                       │    │
│  └──────────────────────────────────────────────────────┘    │
│         ↓                                                      │
│  RUNNER 2 DESTROYED 💥                                        │
│         ↓                                                      │
│  Workflow Complete! ✅                                        │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

---

## 🎓 Summary - Telugu Explanation

### ప్రశ్న 1: Runners pre-installed undha?

**జవాబు:** Yes! కానీ exact version మనకు కావలసినట్టు set cheyyali.

```
Pre-installed: Java 8, 11, 17, 21
Default: Java 11
We need: Java 17

Setup-java action:
  └─→ Sets JAVA_HOME to Java 17
  └─→ Updates PATH
  └─→ Enables Maven cache (KEY BENEFIT!)
```

### ప్రశ్న 2: ప్రతి step తర్వాత file system ఎలా మారుతుంది?

**జవాబు:** ASCII diagrams చూడండి above!

**Key Points:**

1. Checkout → Code downloaded (~10 MB)
2. Setup Java → Cache restored (~200 MB)
3. Maven compile → .class files created (~5 MB)
4. Maven test → Test results (~10 MB)
5. Maven package → JAR created (~50 MB)
6. Upload → Files sent to GitHub (not on runner)
7. Docker build → Image layers (~500 MB)

### ముఖ్యమైన Point: Each job = NEW runner

```
Job 1 → Runner 1 → Destroyed
Job 2 → Runner 2 → Destroyed
```

**Total cost:** ~6-8 minutes of runner time
**Total data:** ~750 MB disk usage (across both runners)

---

## 🎯 Why We Use Setup Actions - Final Answer

### 1. **Version Control**

మనకు exact Java 17 కావాలి, Java 11 కాదు

### 2. **Caching** 🔥

Dependencies cache nundi restore → 3-5 minutes savings!

### 3. **Consistency**

All developers, all builds → same Java version

### 4. **Distribution Choice**

Temurin, Zulu, Corretto - we choose!

### 5. **Environment Setup**

JAVA_HOME, PATH, etc. automatically configured

---

**ఇప్పుడు clear అయ్యిందా?** (Is it clear now?)

You now understand:

- ✅ What runners are (VMs)
- ✅ What's pre-installed
- ✅ Why we still use setup actions
- ✅ Exact file system changes at each step
- ✅ Why each job gets a new runner

**Excellent question! This is advanced understanding! 🎉**
