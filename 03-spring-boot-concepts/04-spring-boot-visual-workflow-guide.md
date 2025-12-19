# 🎬 Spring Boot Workflow - Visual Step-by-Step Guide

## 🚀 INITIAL STATE: Before git push

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   YOUR LOCAL PC (Windows)                                                    │
│   ════════════════════════                                                   │
│                                                                              │
│   D:\Antigravity_Projects\Github_Actions\                                   │
│   ├── .github/                                                               │
│   │   └── workflows/                                                         │
│   │       └── spring-boot-build.yml  ← Workflow file!                       │
│   └── 02-spring-boot-pipeline/                                              │
│       └── todo-backend/                                                      │
│           ├── Dockerfile             ← Docker instructions                  │
│           ├── pom.xml                ← Maven dependencies                   │
│           └── src/                                                           │
│               ├── main/java/         ← Java source code                     │
│               │   └── com/example/todo/                                     │
│               │       ├── TodoApplication.java                              │
│               │       ├── entity/Todo.java                                  │
│               │       └── controller/TodoController.java                    │
│               └── test/java/         ← JUnit test files                     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GITHUB CLOUD                                                               │
│   ════════════                                                               │
│                                                                              │
│   GitHub Runners: 💤 Sleeping (no work yet)                                 │
│   GHCR: Empty (no images)                                                   │
│   GitHub Cache: Empty                                                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

Nee PC lo Spring Boot code undhi - `.github/workflows/` folder lo workflow file undhi. React laagane, ee file GitHub ki chepthundhi "code push ayyinappudu enti cheyalo".

**Key Differences from React:**

- `pom.xml` = `package.json` laanti file (Maven dependencies)
- `src/main/java/` = Java source code folder
- `src/test/java/` = JUnit test files

---

## ⚡ TRIGGER: git push

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   YOUR PC                              GITHUB                                │
│   ════════                             ══════                                │
│                                                                              │
│   ┌─────────────────┐                 ┌─────────────────────────────────┐   │
│   │                 │                 │                                 │   │
│   │  git add .      │                 │  GitHub receives push!          │   │
│   │  git commit -m  │ ═══════════════▶│                                 │   │
│   │  git push       │   Push event!   │  Checks:                        │   │
│   │                 │                 │  ✅ Branch = main?              │   │
│   │  (DONE!)        │                 │  ✅ Path = 02-spring-boot-*?    │   │
│   │                 │                 │  ✅ Workflow exists?            │   │
│   └─────────────────┘                 │                                 │   │
│                                       │  → ALL YES! TRIGGER WORKFLOW!  │   │
│                                       └─────────────────────────────────┘   │
│                                                                              │
│   Telugu: "Nee push GitHub ki reach ayyindhi.                               │
│            Path filter check - Spring Boot files change ayyay!               │
│            React files change aithe ee workflow run avvadu!"                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Path Filtering - Smart Triggering:**

```yaml
paths:
  - '02-spring-boot-pipeline/**'      # Only Spring Boot files
  - '.github/workflows/spring-boot-build.yml'
```

React files change chesthe → React workflow only runs
Spring Boot files change chesthe → Spring Boot workflow only runs

---

## 🖥️ JOB 1 START: Spin Up Runner

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GitHub Cloud Data Center                                                   │
│   ═══════════════════════════                                                │
│                                                                              │
│   runs-on: ubuntu-latest                                                    │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                     FRESH UBUNTU VM (Runner)                        │   │
│   │                                                                     │   │
│   │   OS: Ubuntu 22.04                                                  │   │
│   │   CPU: 2 cores                                                      │   │
│   │   RAM: 7 GB                                                         │   │
│   │   Disk: 14 GB                                                       │   │
│   │                                                                     │   │
│   │   Pre-installed:                                                    │   │
│   │   ✅ Git                                                            │   │
│   │   ✅ Docker                                                         │   │
│   │   ✅ Maven (but we'll use setup-java for caching!)                 │   │
│   │   ✅ Common tools                                                   │   │
│   │                                                                     │   │
│   │   Working Directory:                                                │   │
│   │   /home/runner/work/github-actions-learning/github-actions-learning/│   │
│   │   └── (EMPTY! No code yet!)                                        │   │
│   │                                                                     │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   Telugu: "GitHub fresh Ubuntu VM start chesindhi.                          │
│            Git, Docker ready unnay.                                          │
│            But nee Java code inka ledu - checkout cheyali!"                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📥 JOB 1 - STEP 1: Checkout Code

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

### BEFORE Checkout

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Runner VM Filesystem:                                                      │
│   ═══════════════════════                                                    │
│                                                                              │
│   /home/runner/work/github-actions-learning/github-actions-learning/       │
│   └── (COMPLETELY EMPTY!)                                                   │
│                                                                              │
│   Maven: "pom.xml ekkada? Dependencies download cheyyalenu!" 😭             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Checkout

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Runner VM Filesystem:                                                      │
│   ═══════════════════════                                                    │
│                                                                              │
│   /home/runner/work/github-actions-learning/github-actions-learning/       │
│   ├── .github/workflows/                                                    │
│   │   └── spring-boot-build.yml    ✅                                      │
│   └── 02-spring-boot-pipeline/                                              │
│       └── todo-backend/            ✅ ← IMPORTANT!                         │
│           ├── Dockerfile           ✅                                       │
│           ├── pom.xml              ✅ ← Maven can find this now!           │
│           └── src/                 ✅                                       │
│               ├── main/java/...    ✅ ← Java source code                   │
│               └── test/java/...    ✅ ← JUnit tests                        │
│                                                                              │
│   Telugu: "Nee code antha runner ki download ayyindhi!                      │
│            Ippudu pom.xml undhi - Maven build cheyachu!"                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ☕ JOB 1 - STEP 2: Setup Java

```yaml
- name: ☕ Setup JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: maven              # 🔥 Critical for speed!
```

### BEFORE Setup Java

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Runner VM:                                                                 │
│   ══════════                                                                 │
│                                                                              │
│   $ java -version                                                            │
│   ❌ command not found (or wrong version)                                   │
│                                                                              │
│   ~/.m2/repository/                                                          │
│   └── (EMPTY! No Maven cache!)                                              │
│                                                                              │
│   Problem: Without Java 17, Spring Boot code compile avvadu!                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Install Java + Restore Cache

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   actions/setup-java@v4:                                                    │
│   ═══════════════════════                                                    │
│                                                                              │
│   1. Download Eclipse Temurin JDK 17                                        │
│   2. Set JAVA_HOME environment variable                                     │
│   3. Add java/javac to PATH                                                 │
│   4. Check GitHub Cache for Maven dependencies...                           │
│                                                                              │
│   FIRST RUN:                                                                 │
│   ──────────                                                                 │
│   Cache: "No cache found!" 😕                                               │
│   Result: Maven will download dependencies fresh                            │
│                                                                              │
│   SUBSEQUENT RUNS:                                                           │
│   ────────────────                                                           │
│   Cache: "Found cache! Restoring ~/.m2/repository..." 🎉                    │
│   Result: Skip downloading 200+ dependencies!                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Setup Java

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Runner VM:                                                                 │
│   ══════════                                                                 │
│                                                                              │
│   $ java -version                                                            │
│   openjdk version "17.0.9" 2023-10-17                                       │
│   OpenJDK Runtime Environment Temurin-17.0.9+9 ✅                           │
│                                                                              │
│   $ mvn -version                                                             │
│   Apache Maven 3.9.x ✅                                                     │
│   Java version: 17.0.9 ✅                                                   │
│                                                                              │
│   ~/.m2/repository/ (if cached):                                            │
│   ├── org/springframework/boot/...    ✅ Spring Boot JARs                  │
│   ├── com/h2database/...              ✅ H2 Database                        │
│   ├── org/junit/...                   ✅ JUnit testing                      │
│   └── ... (200+ dependency JARs!)                                           │
│                                                                              │
│   Telugu: "Java 17 install ayyindhi!                                        │
│            Maven ready!                                                      │
│            Cache unte - dependencies already download ayyay!"               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Maven Cache

**Telugu lo simple ga cheppali ante:**

**React vs Spring Boot caching:**

```
React:   cache: npm   → caches node_modules/     (~500MB)
Spring:  cache: maven → caches ~/.m2/repository/ (~300MB)
```

**Cache enduku important?**

Without cache:

- Every run: Download 200+ JAR files = 2-3 minutes ⏱️

With cache:

- First run: Download + save to cache = 2-3 minutes
- Next runs: Restore cache = 10-20 seconds ⚡

**Savings: ~2 minutes per build!**

---

## 🔨 JOB 1 - STEP 3: Build with Maven

```yaml
- name: 🔨 Build with Maven
  run: mvn clean compile -B
  working-directory: ./02-spring-boot-pipeline/todo-backend
```

### BEFORE Maven Compile

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   todo-backend/ folder:                                                      │
│   ═════════════════════                                                      │
│                                                                              │
│   src/main/java/com/example/todo/                                           │
│   ├── TodoApplication.java         ← Human-readable Java                   │
│   ├── entity/Todo.java             ← Human-readable Java                   │
│   └── controller/TodoController.java                                       │
│                                                                              │
│   target/                                                                    │
│   └── (DOESN'T EXIST YET!)                                                  │
│                                                                              │
│   JVM: "I can't run .java files directly! Need .class files!"              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Maven Compile

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   mvn clean compile -B                                                       │
│   ═══════════════════════                                                    │
│                                                                              │
│   Phase 1: CLEAN                                                             │
│   ──────────────                                                             │
│   Delete target/ folder (if exists) → Fresh start!                          │
│                                                                              │
│   Phase 2: COMPILE                                                           │
│   ────────────────                                                           │
│   ┌─────────────────────┐         ┌─────────────────────┐                   │
│   │ TodoApplication.java│ ──────▶ │ TodoApplication.class│                  │
│   │ (Human readable)    │  javac  │ (Bytecode for JVM)   │                  │
│   └─────────────────────┘         └─────────────────────┘                   │
│                                                                              │
│   "-B" flag = Batch mode (no color, no interactive prompts)                 │
│               Perfect for CI - clean log output!                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Maven Compile

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   todo-backend/ folder:                                                      │
│   ═════════════════════                                                      │
│                                                                              │
│   src/main/java/com/example/todo/                                           │
│   ├── TodoApplication.java         (unchanged - source files)              │
│   ├── entity/Todo.java                                                      │
│   └── controller/TodoController.java                                       │
│                                                                              │
│   target/                          ← NEW! Created by Maven                  │
│   └── classes/                     ← Compiled bytecode                      │
│       └── com/example/todo/                                                 │
│           ├── TodoApplication.class     ✅                                  │
│           ├── entity/Todo.class         ✅                                  │
│           └── controller/TodoController.class ✅                            │
│                                                                              │
│   Telugu: ".java files compile ayyay!                                       │
│            .class files create ayyay - JVM run cheyagaladu!"                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Compilation

**React vs Spring Boot:**

```
React:  npm run build → JavaScript bundles (browser runs)
Spring: mvn compile   → .class bytecode (JVM runs)
```

**Enduku compile kavali?**

- Java = compiled language (human code → machine bytecode)
- JavaScript = interpreted (browser runs directly)
- JVM needs .class files to execute!

---

## 🧪 JOB 1 - STEP 4: Run Tests

```yaml
- name: 🧪 Run Tests
  if: ${{ github.event.inputs.skip_tests != 'true' }}
  run: mvn test -B
```

### BEFORE Tests

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Test Files:                                                                │
│   ════════════                                                               │
│                                                                              │
│   src/test/java/com/example/todo/                                           │
│   ├── TodoApplicationTests.java      ← Context loading test                │
│   └── TodoControllerTest.java        ← 10 REST API tests                   │
│                                                                              │
│   Question: "Does our code actually work? Tests haven't run yet!"           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Run JUnit Tests

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   mvn test -B                                                                │
│   ═════════════                                                              │
│                                                                              │
│   [INFO] Running TodoApplicationTests                                        │
│   [INFO] ✅ contextLoads - Spring context starts successfully               │
│                                                                              │
│   [INFO] Running TodoControllerTest                                          │
│   [INFO] ✅ whenGetAllTodos_thenReturnEmptyList                             │
│   [INFO] ✅ whenCreateTodo_thenReturnCreated                                │
│   [INFO] ✅ whenGetTodoById_thenReturnTodo                                  │
│   [INFO] ✅ whenUpdateTodo_thenReturnUpdated                                │
│   [INFO] ✅ whenDeleteTodo_thenReturnNoContent                              │
│   [INFO] ✅ whenToggleComplete_thenReturnToggled                            │
│   [INFO] ✅ whenGetNonExistent_thenReturn404                                │
│   [INFO] ✅ whenCreateInvalid_thenReturn400                                 │
│   [INFO] ✅ whenClearCompleted_thenRemoveCompleted                          │
│   [INFO] ✅ whenGetStats_thenReturnCounts                                   │
│                                                                              │
│   [INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0                  │
│   [INFO] BUILD SUCCESS ✅                                                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Tests

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Test Results:                                                              │
│   ══════════════                                                             │
│                                                                              │
│   target/surefire-reports/                 ← Test reports generated!        │
│   ├── TEST-TodoApplicationTests.xml        ← JUnit XML report              │
│   ├── TEST-TodoControllerTest.xml          ← JUnit XML report              │
│   ├── TodoApplicationTests.txt             ← Human-readable                │
│   └── TodoControllerTest.txt               ← Human-readable                │
│                                                                              │
│   Result: 11 tests passed! Code is working correctly! ✅                    │
│                                                                              │
│   Telugu: "11 tests run ayyay - anni PASS!                                  │
│            Code correctly work avuthundhi!                                   │
│            Ippudu confident ga JAR create cheyachu!"                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Conditional Testing

```yaml
if: ${{ github.event.inputs.skip_tests != 'true' }}
```

**Enti idi?**

- Manual trigger (workflow_dispatch) lo checkbox untundhi: "Skip tests"
- Check chesthe: Tests skip avutay
- Emergency ki useful - but use with caution!

---

## 📦 JOB 1 - STEP 5: Package JAR

```yaml
- name: 📦 Package JAR
  run: mvn package -DskipTests -B
```

### BEFORE Package

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   target/ folder:                                                            │
│   ══════════════                                                             │
│                                                                              │
│   target/                                                                    │
│   ├── classes/                     ← Compiled .class files                  │
│   │   └── com/example/todo/...                                              │
│   └── surefire-reports/            ← Test reports                           │
│       └── TEST-*.xml                                                        │
│                                                                              │
│   Problem: We have .class files, but can't deploy them easily!              │
│   Solution: Package everything into ONE executable JAR file!                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Create Fat JAR

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   mvn package -DskipTests -B                                                 │
│   ══════════════════════════                                                 │
│                                                                              │
│   Spring Boot Maven Plugin creates "FAT JAR":                               │
│   ─────────────────────────────────────────────                              │
│                                                                              │
│   ┌────────────────────────────────────────┐                                │
│   │       todo-backend-1.0.0.jar           │                                │
│   │       (Executable Fat JAR)             │                                │
│   ├────────────────────────────────────────┤                                │
│   │ BOOT-INF/                              │                                │
│   │   ├── classes/                         │ ← Your compiled code          │
│   │   │   └── com/example/todo/...         │                                │
│   │   └── lib/                             │ ← ALL dependencies!           │
│   │       ├── spring-boot-3.2.0.jar        │                                │
│   │       ├── spring-web-6.1.0.jar         │                                │
│   │       ├── h2-2.2.224.jar               │                                │
│   │       └── ... (50+ JARs!)              │                                │
│   ├────────────────────────────────────────┤                                │
│   │ META-INF/                              │                                │
│   │   └── MANIFEST.MF                      │ ← Main-Class info             │
│   ├────────────────────────────────────────┤                                │
│   │ org/springframework/boot/loader/       │ ← Spring Boot launcher        │
│   └────────────────────────────────────────┘                                │
│                                                                              │
│   "-DskipTests" → Tests already ran in Step 4, don't repeat!                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Package

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   target/ folder:                                                            │
│   ══════════════                                                             │
│                                                                              │
│   target/                                                                    │
│   ├── classes/                                                               │
│   ├── surefire-reports/                                                      │
│   ├── todo-backend-1.0.0.jar          ← FAT JAR! (~30MB)                   │
│   └── todo-backend-1.0.0.jar.original ← Thin JAR (without deps)            │
│                                                                              │
│   To run the application:                                                    │
│   $ java -jar todo-backend-1.0.0.jar                                        │
│                                                                              │
│   No need to install:                                                        │
│   ❌ Spring Framework                                                        │
│   ❌ H2 Database                                                             │
│   ❌ Jackson JSON                                                            │
│   Everything is INSIDE the JAR! 🎉                                          │
│                                                                              │
│   Telugu: "Fat JAR create ayyindhi!                                         │
│            Nee code + ALL dependencies = One file!                          │
│            java -jar tho anywhere run cheyachu!"                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Fat JAR Concept

**React vs Spring Boot output:**

```
React:  npm run build → dist/ folder (HTML, JS, CSS files)
Spring: mvn package   → .jar file (executable archive)
```

**Fat JAR ante enti?**

- "Fat" = All dependencies included inside
- Also called "Uber JAR" or "Executable JAR"
- Single file = easy to deploy!

**Thin JAR vs Fat JAR:**

```
Thin JAR:  Your code only (~500KB) - needs external dependencies
Fat JAR:   Your code + ALL deps (~30MB) - runs standalone!
```

---

## 📊 JOB 1 - STEP 6: Upload Test Results

```yaml
- name: 📊 Upload Test Results
  if: always()    # Upload even if tests fail!
  uses: actions/upload-artifact@v4
  with:
    name: test-results
    path: ./02-spring-boot-pipeline/todo-backend/target/surefire-reports/
    retention-days: 7
```

### BEFORE Upload

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   RUNNER VM:                                                                 │
│   ══════════                                                                 │
│   target/surefire-reports/                                                  │
│   ├── TEST-TodoApplicationTests.xml                                        │
│   └── TEST-TodoControllerTest.xml                                           │
│                                                                              │
│   Problem: When VM gets destroyed, these reports will be LOST!              │
│                                                                              │
│   GITHUB ARTIFACTS:                                                          │
│   ═════════════════                                                          │
│   (Empty - nothing uploaded yet)                                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Upload

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   GITHUB ARTIFACTS (Actions → Run → Artifacts):                             │
│   ═════════════════════════════════════════════                              │
│                                                                              │
│   📦 test-results                                                            │
│   ├── Size: ~50KB                                                           │
│   ├── Expires: 7 days                                                       │
│   └── Contains:                                                              │
│       ├── TEST-TodoApplicationTests.xml                                     │
│       ├── TEST-TodoControllerTest.xml                                       │
│       ├── TodoApplicationTests.txt                                          │
│       └── TodoControllerTest.txt                                            │
│                                                                              │
│   Telugu: "Test reports GitHub Artifacts lo save ayyay!                     │
│            VM destroy ayyina, reports 7 days available!                      │
│            Tests fail ayyina upload avutay (if: always)!"                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📦 JOB 1 - STEP 7: Upload JAR Artifact

```yaml
- name: 📦 Upload JAR
  uses: actions/upload-artifact@v4
  with:
    name: todo-backend-jar
    path: ./02-spring-boot-pipeline/todo-backend/target/*.jar
    retention-days: 7
```

### AFTER Upload

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   GITHUB ARTIFACTS (Actions → Run → Artifacts):                             │
│   ═════════════════════════════════════════════                              │
│                                                                              │
│   📦 test-results                                                            │
│   │   └── (Test XML files)                                                  │
│   │                                                                          │
│   📦 todo-backend-jar         ← NEW!                                        │
│   ├── Size: ~30MB                                                           │
│   ├── Expires: 7 days                                                       │
│   └── Contains:                                                              │
│       ├── todo-backend-1.0.0.jar              (Fat JAR)                     │
│       └── todo-backend-1.0.0.jar.original     (Thin JAR)                    │
│                                                                              │
│   You can:                                                                   │
│   • Download JAR from GitHub Actions page                                   │
│   • Run it locally: java -jar todo-backend-1.0.0.jar                       │
│   • Deploy manually if CI/CD fails                                          │
│                                                                              │
│   Telugu: "JAR file kuda GitHub Artifacts lo save ayyindhi!                 │
│            Download chesi locally run cheyachu!                              │
│            Emergency deployment ki useful!"                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Why Upload Artifacts?

**React vs Spring Boot artifacts:**

```
React:  Upload build folder (HTML/JS/CSS)
Spring: Upload JAR file (executable)
```

**Why save JAR as artifact?**

1. **Debugging:** Download and test exact version that was built
2. **Emergency deploy:** If Docker push fails, manual deploy backup
3. **Audit trail:** Keep history of what was built
4. **Local testing:** Download and run `java -jar todo-backend.jar`

---

## 🔄 JOB TRANSITION: Job 1 → Job 2

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   JOB 1: Build & Test                    JOB 2: Docker Build & Push         │
│   ═══════════════════════                ═══════════════════════════         │
│                                                                              │
│   ┌─────────────────────┐               ┌─────────────────────┐             │
│   │ ✅ Checkout         │               │                     │             │
│   │ ✅ Setup Java       │               │   WAITING...        │             │
│   │ ✅ Compile          │   needs:      │                     │             │
│   │ ✅ Test             │ ═════════════▶│   needs: build-test │             │
│   │ ✅ Package JAR      │ build-and-test│   means "wait for   │             │
│   │ ✅ Upload Artifacts │               │   Job 1 to finish!" │             │
│   │                     │               │                     │             │
│   │ STATUS: SUCCESS ✅  │               │                     │             │
│   └─────────────────────┘               └─────────────────────┘             │
│                                                                              │
│   Telugu: "Job 1 complete ayyindhi!                                         │
│            Tests pass ayyay - code is good!                                  │
│            Ippudu Job 2 start avuthundhi - Docker image build!"             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

**Why `needs: build-and-test`?**

- Don't build Docker image if tests fail!
- Sequential: Test first, then build image
- Saves time and resources - no point in Docker build for broken code

---

## 🐳 JOB 2: Docker Build & Push

### Job 2 Configuration

```yaml
docker-build:
  needs: build-and-test                    # Wait for Job 1
  if: github.event_name == 'push' || github.event_name == 'workflow_dispatch'
  
  permissions:
    contents: read      # Read code
    packages: write     # Push to GHCR
```

**When does Job 2 run?**

- ✅ `push` to main → Run (build and push image)
- ✅ Manual trigger → Run
- ❌ Pull request → Skip (only test, don't push)

---

## 🐳 JOB 2 - STEPS 1-4: Same as React

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   These steps are IDENTICAL to React workflow!                              │
│   ═══════════════════════════════════════════════                            │
│                                                                              │
│   Step 1: 📥 Checkout Code                                                  │
│           → Clone code to fresh runner VM                                   │
│                                                                              │
│   Step 2: 🔐 Login to GHCR                                                  │
│           → docker login ghcr.io with GITHUB_TOKEN                          │
│                                                                              │
│   Step 3: 🔧 Setup Docker Buildx                                            │
│           → Enable advanced caching (type=gha)                              │
│                                                                              │
│   Step 4: 📋 Extract Metadata                                               │
│           → Generate tags: :latest and :abc123 (SHA)                        │
│                                                                              │
│   Telugu: "Ee 4 steps React workflow lo already explain chesam!             │
│            Same concept - Docker login, Buildx setup, tags generate!"        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🐳 JOB 2 - STEP 5: Build and Push (Multi-Stage)

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

### Spring Boot Dockerfile (Multi-Stage Build)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   STAGE 1: BUILD (maven:3.9-eclipse-temurin-17-alpine)                      │
│   ═════════════════════════════════════════════════════                      │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ FROM maven:3.9-eclipse-temurin-17-alpine AS build                   │   │
│   │ ► Large image with Maven + JDK (~400MB)                             │   │
│   │ ► Has everything needed to COMPILE Java code                        │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                          │                                                   │
│                          ▼                                                   │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ COPY pom.xml .                                                      │   │
│   │ RUN mvn dependency:go-offline -B                                    │   │
│   │ ► Copy pom.xml FIRST → Dependencies cached!                       │   │
│   │ ► Download all dependencies (cached if pom.xml unchanged)          │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                          │                                                   │
│                          ▼                                                   │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ COPY src ./src                                                      │   │
│   │ RUN mvn package -DskipTests -B                                      │   │
│   │ ► Copy source code                                                  │   │
│   │ ► Build JAR (tests already ran in Job 1!)                          │   │
│   │ ► Result: /app/target/todo-backend-1.0.0.jar                       │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   STAGE 2: PRODUCTION (eclipse-temurin:17-jre-alpine)                       │
│   ════════════════════════════════════════════════════                       │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ FROM eclipse-temurin:17-jre-alpine                                  │   │
│   │ ► Tiny JRE only (~100MB) - no Maven, no JDK, no source!            │   │
│   │ ► Just enough to RUN Java applications                             │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                          │                                                   │
│                          ▼                                                   │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ COPY --from=build /app/target/*.jar app.jar                        │   │
│   │ ► Copy ONLY the JAR from Stage 1                                   │   │
│   │ ► Source code NOT copied!                                           │   │
│   │ ► Maven NOT copied!                                                 │   │
│   │ ► Entire build environment NOT copied!                             │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                          │                                                   │
│                          ▼                                                   │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ RUN addgroup -S appgroup && adduser -S appuser -G appgroup         │   │
│   │ USER appuser                                                        │   │
│   │ ► Create non-root user for security                                │   │
│   │ ► Never run as root in production!                                 │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                          │                                                   │
│                          ▼                                                   │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │ HEALTHCHECK --interval=30s CMD wget -qO- http://localhost:8080/... │   │
│   │ ENTRYPOINT ["java", "-jar", "app.jar"]                             │   │
│   │ ► Health check for Kubernetes                                      │   │
│   │ ► Start the Spring Boot app                                        │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   FINAL IMAGE SIZE: ~150MB (JRE + JAR only!)                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Multi-Stage Build

**Telugu lo simple ga cheppali ante:**

**React vs Spring Boot multi-stage:**

```
React:
  Stage 1: node:20-alpine (Build)    → npm run build
  Stage 2: nginx:alpine (Runtime)    → Serve static files
  Final:   ~25MB

Spring Boot:
  Stage 1: maven (Build)             → mvn package
  Stage 2: JRE-alpine (Runtime)      → java -jar app.jar
  Final:   ~150MB
```

**Enduku Spring Boot image pedda?**

- JRE (Java Runtime) = ~100MB minimum
- JAR file with dependencies = ~30MB
- Total = ~130-150MB

**Enduku React image chinna?**

- Nginx = ~10MB
- Static files = ~5-10MB
- Total = ~25MB

**But still optimized!**

- Without multi-stage: 500MB+ (Maven + JDK + source)
- With multi-stage: 150MB (JRE + JAR only)
- Savings: 70%+ smaller image!

---

## 🚀 JOB 2 - Push to GHCR

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Runner VM                        GHCR (GitHub Container Registry)         │
│   ══════════                       ═════════════════════════════════         │
│                                                                              │
│   ┌────────────────┐              ┌───────────────────────────────────┐     │
│   │                │              │                                   │     │
│   │ Built Image    │              │  ghcr.io/pdheeraj99/              │     │
│   │ (~150MB)       │═════════════▶│  github-actions-learning/         │     │
│   │                │  docker push │  todo-backend                     │     │
│   │                │              │                                   │     │
│   │ Tags:          │              │  Tags:                            │     │
│   │ :abc123        │              │  :abc123 ✅                       │     │
│   │ :latest        │              │  :latest ✅                       │     │
│   │                │              │                                   │     │
│   └────────────────┘              └───────────────────────────────────┘     │
│                                                                              │
│   Telugu: "Built image GHCR ki push ayyindhi!                               │
│            Eppudaina docker pull cheyachu!                                   │
│            Kubernetes lo deploy cheyachu!"                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ✅ FINAL STATE: After Workflow Complete

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   RUNNER VMs:                                                                │
│   ═══════════                                                                │
│   💀 Job 1 Runner: DESTROYED!                                               │
│   💀 Job 2 Runner: DESTROYED!                                               │
│   No traces left! Clean and secure! 🔒                                      │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GITHUB ARTIFACTS (7 days retention):                                      │
│   ═════════════════════════════════════                                      │
│   📦 test-results        → JUnit XML reports                                │
│   📦 todo-backend-jar    → Executable Fat JAR                               │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GHCR (GitHub Container Registry):                                         │
│   ══════════════════════════════════                                         │
│                                                                              │
│   ghcr.io/pdheeraj99/github-actions-learning/todo-backend                   │
│   ├── :abc123    ← Specific version (commit-based)                         │
│   └── :latest    ← Always points to newest                                 │
│                                                                              │
│   Pull command:                                                              │
│   docker pull ghcr.io/pdheeraj99/github-actions-learning/todo-backend       │
│                                                                              │
│   Run command:                                                               │
│   docker run -p 8080:8080 ghcr.io/.../todo-backend:latest                   │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GITHUB CACHE:                                                              │
│   ══════════════                                                             │
│   ✅ Maven dependencies (~300MB)                                            │
│   ✅ Docker build layers (~400MB)                                           │
│   → Next build will be MUCH faster!                                         │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   YOUR PC:                                                                   │
│   ═════════                                                                  │
│   (Nothing changed! You just did git push!)                                 │
│                                                                              │
│   Telugu: "Workflow complete!                                                │
│            Docker image GHCR lo ready!                                       │
│            JAR artifact download available!                                  │
│            Cache saved for next time!                                        │
│            VMs destroyed - clean slate!"                                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Complete Timeline

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   JOB 1: BUILD & TEST                                                        │
│   ═══════════════════                                                        │
│                                                                              │
│   TIME      EVENT                              DURATION                      │
│   ────      ─────                              ────────                      │
│   00:00     git push                           instant                       │
│   00:01     GitHub detects push                ~1 second                     │
│   00:02     Spin up runner VM                  ~20 seconds                   │
│   00:22     Step 1: Checkout                   ~5 seconds                    │
│   00:27     Step 2: Setup Java                 ~10 sec (cached)              │
│                                                ~2 min (first time)           │
│   00:37     Step 3: Maven compile              ~30 seconds                   │
│   01:07     Step 4: Run tests                  ~45 seconds                   │
│   01:52     Step 5: Package JAR                ~20 seconds                   │
│   02:12     Step 6: Upload test results        ~5 seconds                    │
│   02:17     Step 7: Upload JAR                 ~10 seconds                   │
│                                                                              │
│   JOB 1 TOTAL: ~2-3 minutes (cached)                                        │
│                ~4-5 minutes (first time)                                     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   JOB 2: DOCKER BUILD & PUSH                                                 │
│   ══════════════════════════                                                 │
│                                                                              │
│   02:30     Spin up new runner VM              ~20 seconds                   │
│   02:50     Steps 1-4: Checkout, Login, etc    ~30 seconds                   │
│   03:20     Step 5: Docker Build               ~3-5 min (first time)        │
│                                                ~1-2 min (cached)             │
│   06:20     Docker Push to GHCR                ~30 seconds                   │
│                                                                              │
│   JOB 2 TOTAL: ~2-3 minutes (cached)                                        │
│                ~5-6 minutes (first time)                                     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GRAND TOTAL:                                                               │
│   First run:  ~10-12 minutes                                                │
│   With cache: ~4-5 minutes! 🚀                                              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Takeaways

### What Happens in Each Step

| Step | What Happens | Why |
|------|--------------|-----|
| **Checkout** | Clone code to VM | VM starts empty |
| **Setup Java** | Install JDK 17 + restore Maven cache | Java apps need JDK |
| **Compile** | .java → .class | JVM needs bytecode |
| **Test** | Run JUnit tests | Verify code works |
| **Package** | Create Fat JAR | Single deployable file |
| **Upload Artifacts** | Save JAR + reports | Keep for 7 days |
| **Docker Build** | Multi-stage image creation | Optimized production image |
| **Docker Push** | Upload to GHCR | Anyone can pull and run |

### React vs Spring Boot Summary

| Aspect | React | Spring Boot |
|--------|-------|-------------|
| **Language** | JavaScript | Java |
| **Build Tool** | npm | Maven |
| **Cache** | node_modules | ~/.m2/repository |
| **Compilation** | Bundling | .java → .class |
| **Output** | Static files | Fat JAR |
| **Runtime** | Nginx | JRE |
| **Final Size** | ~25MB | ~150MB |
| **Build Time** | ~2-3 min | ~4-5 min |

### Summary Flow

```
git push
    ↓
GitHub triggers workflow
    ↓
JOB 1: Build & Test
    ├── Setup Java (with cache!)
    ├── Compile (.java → .class)
    ├── Test (JUnit)
    ├── Package (Fat JAR)
    └── Upload Artifacts
    ↓
JOB 2: Docker Build & Push
    ├── Multi-stage Dockerfile
    │   ├── Stage 1: Maven build
    │   └── Stage 2: JRE runtime
    └── Push to GHCR
    ↓
Done! Image ready to deploy! 🎉
```

**Ippudu clear ga artham ayyinda?** (Is it clear now?)

Each step has a specific purpose - from compiling Java to creating optimized Docker images. The key differences from React are the compilation step and larger JRE-based images!
