# Maven vs npm - Build Tool Comparison

## Overview

Understanding the differences between Maven (Java) and npm (JavaScript) helps understand why Spring Boot and React workflows differ.

---

## 🔄 Side-by-Side Comparison

### Package Definition Files

#### npm - `package.json`

```json
{
  "name": "todo-frontend",
  "version": "1.0.0",
  "dependencies": {
    "react": "^18.3.1",
    "react-dom": "^18.3.1"
  },
  "devDependencies": {
    "vite": "^7.3.0",
    "typescript": "^5.6.3"
  },
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build"
  }
}
```

#### Maven - `pom.xml`

```xml
<project>
  <groupId>com.example</groupId>
  <artifactId>todo-backend</artifactId>
  <version>1.0.0</version>
  
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>3.2.0</version>
    </dependency>
  </dependencies>
</project>
```

**Key Differences:**

| Feature | npm | Maven |
|---------|-----|-------|
| **Format** | JSON | XML |
| **Dependency Syntax** | Simple string | GroupId + ArtifactId + Version |
| **Scripts** | Custom (`npm run build`) | Standard lifecycle |
| **Version Prefix** | `^` (semver range) | Exact or range |

---

### Dependency Locking

#### npm - `package-lock.json`

```json
{
  "name": "todo-frontend",
  "lockfileVersion": 3,
  "packages": {
    "node_modules/react": {
      "version": "18.3.1",
      "resolved": "https://registry.npmjs.org/react/-/react-18.3.1.tgz",
      "integrity": "sha512-wS+hAgJShR0KhEvPJArfuPVN1+Hz1t0Y6n5jLrGQbkb4urgPE/0Rve+1kMB1v/oWgHgm4WIcV+i7F2pTVj+2iQ=="
    }
  }
}
```

**Purpose:**

- Locks exact versions of ALL dependencies (including transitive)
- Ensures team has identical dependency tree
- Committed to git

---

#### Maven - NO Lock File

Maven doesn't have a lock file by default. Why?

**Maven's approach:**

1. Dependencies declared in `pom.xml`
2. Maven downloads from Maven Central
3. Stores in `~/.m2/repository/`
4. Uses dependency resolution algorithm

**Version resolution:**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <version>3.2.0</version>  <!-- Exact version -->
</dependency>
```

**Transitive dependencies:**
Spring Boot manages versions via "Bill of Materials" (BOM):

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>3.2.0</version>
</parent>
```

This parent POM defines versions for ALL Spring dependencies!

**Reproducible builds?**
Maven doesn't guarantee exact same builds across time unless you:

1. Lock all versions explicitly
2. Use Maven Wrapper (`mvnw`)
3. Use Bill of Materials (BOM)

---

## 🔧 Installation & Execution

### npm/Node.js

**Installation:**

```bash
# Install Node.js (includes npm)
node --version
npm --version
```

**Running commands:**

```bash
npm install          # Install dependencies
npm run build        # Run custom script
npm test             # Run tests
```

**Where are packages stored?**

```
project/
  └── node_modules/    ← All dependencies here
      ├── react/
      ├── vite/
      └── ... (can be 100+ folders!)
```

**Size:** `node_modules/` can be 200-500MB!

---

### Maven

**Installation:**

```bash
# Install Maven separately (Java required)
mvn --version

# OR use Maven Wrapper (bundled with project)
./mvnw --version
```

**Running commands:**

```bash
mvn clean            # Clean target/ folder
mvn compile          # Compile Java files
mvn test             # Run tests
mvn package          # Create JAR
```

**Where are packages stored?**

```
~/.m2/repository/      ← Global cache (ALL projects)
  └── org/springframework/
      └── spring-boot-starter-web/
          └── 3.2.0/
              └── spring-boot-starter-web-3.2.0.jar
```

**Size:** `~/.m2/repository/` can be 1-2GB (but shared across projects!)

---

## 📦 Output Artifacts

### npm

**Build output:**

```bash
npm run build
```

**Creates:**

```
dist/
  ├── index.html
  ├── assets/
  │   ├── index-abc123.js  (bundled JS)
  │   └── index-xyz789.css (bundled CSS)
  └── ...
```

**Static files:** HTML, JS, CSS, images  
**Deployment:** Copy  `dist/` to web server (nginx, Apache, S3, etc.)

---

### Maven

**Build output:**

```bash
mvn package
```

**Creates:**

```
target/
  └── todo-backend-1.0.0.jar   ← Executable JAR
```

**JAR file structure:**

```
todo-backend-1.0.0.jar
  ├── BOOT-INF/
  │   ├── classes/          ← Your .class files
  │   ├── lib/              ← ALL dependency JARs!
  │   │   ├── spring-boot-3.2.0.jar
  │   │   ├── h2-2.2.224.jar
  │   │   └── ...
  └── META-INF/
      └── MANIFEST.MF       ← Entry point
```

**Executable:** `java -jar todo-backend-1.0.0.jar`  
**Self-contained:** Includes all dependencies!

---

## 🏃 Lifecycle / Scripts

### npm - Custom Scripts

**Defined in `package.json`:**

```json
"scripts": {
  "dev": "vite",
  "build": "tsc && vite build",
  "test": "vitest",
  "lint": "eslint .",
  "format": "prettier --write ."
}
```

**Run:** `npm run <script-name>`

**Flexibility:**

- Define ANY command
- Chain commands with `&&`
- Run shell scripts
- No standard lifecycle

---

### Maven - Standard Lifecycle

**Built-in phases (always same order):**

```
mvn clean
mvn compile
mvn test
mvn package
mvn install
mvn deploy
```

**Full lifecycle:**

```
validate → compile → test → package → verify → install → deploy
```

**Each phase automatically runs previous phases!**

```bash
mvn package
    ↓
Runs: clean → compile → test → package
```

**Advantages:**

- Standardized across all Maven projects
- Predictable behavior
- IDE integration
- CI/CD friendly

---

## 🔍 Dependency Resolution

### npm

**Flat dependency tree (npm 3+):**

```
node_modules/
  ├── react@18.3.1/
  ├── react-dom@18.3.1/
  ├── scheduler@0.23.2/    ← Shared by both
  └── ...
```

**Deduplication:** npm tries to flatten dependencies to avoid duplicates.

**Version conflicts:**

```
A needs lodash@4.17.0
B needs lodash@4.18.0
    ↓
npm installs BOTH versions!
```

---

### Maven

**Transitive dependency tree:**

```
Spring Boot Starter Web
  └── Spring Web
      └── Spring Core
          └── Commons Logging
```

**Version conflict resolution:**

```
A needs commons-lang@2.6
B needs commons-lang@3.12
    ↓
Maven chooses ONE version (nearest dependency wins)
```

**Explicit exclusion if needed:**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <exclusions>
    <exclusion>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

---

## 🚀 Performance Comparison

### Dependency Download

| Tool | First Install | With Cache |
|------|---------------|------------|
| **npm** | 1-2 min | 10-20s |
| **Maven** | 3-5 min | 30s-1min |

**Why Maven is slower?**

- Java dependencies are larger (JARs)
- More transitive dependencies
- Downloads source/javadoc optionally

---

### Build Speed

| Tool | Clean Build | Incremental |
|------|-------------|-------------|
| **npm (Vite)** | 30s-1min | 1-5s |
| **Maven** | 2-5min | 30s-1min |

**Why Maven is slower?**

- Java compilation is slower than JS bundling
- More files to compile
- Test execution is slower

---

## 🎓 Learning Curve

### npm

- ✅ JSON is easy to read
- ✅ Simple syntax
- ❌ Many tools (webpack, vite, rollup, etc.)
- ❌ Configuration can be complex

### Maven

- ❌ XML is verbose
- ❌ More concepts (groupId, artifactId, scope)
- ✅ Convention over configuration
- ✅ Standard lifecycle
- ✅ Less tooling choice confusion

---

## 📊 Quick Reference

| Feature | npm | Maven |
|---------|-----|-------|
| **Config Format** | JSON | XML |
| **Lock File** | package-lock.json | None (usually) |
| **Install Command** | `npm install` | `mvn dependency:resolve` |
| **Build Command** | `npm run build` | `mvn package` |
| **Test Command** | `npm test` | `mvn test` |
| **Output** | `dist/` | `target/*.jar` |
| **Cache Location** | `node_modules/` | `~/.m2/repository/` |
| **Execution** | `node dist/index.js` | `java -jar target/app.jar` |
| **Registry** | npmjs.com | Maven Central |
| **Dependencies in Git?** | No (`.gitignore`) | No (`.gitignore`) |
| **Lock file in Git?** | Yes | N/A |

---

## 🎯 In GitHub Actions

### npm

```yaml
- uses: actions/setup-node@v4
  with:
    node-version: '20'
    cache: 'npm'          # Caches node_modules/

- run: npm ci             # Clean install (faster)
- run: npm run build
```

### Maven

```yaml
- uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'maven'        # Caches ~/.m2/repository/

- run: mvn clean package -B
```

---

## 🤔 Which is Better?

**It depends on language/ecosystem:**

**npm is better for:**

- JavaScript/TypeScript projects
- Frontend development
- Quick prototyping
- Smaller dependency graphs

**Maven is better for:**

- Java projects
- Enterprise applications
- Standardized builds
- Large project structures

**Both are excellent tools for their ecosystems!**

---

## Key Takeaway

Understanding both helps you:

- ✅ Work with full-stack projects
- ✅ Compare CI/CD workflows
- ✅ Optimize build times
- ✅ Troubleshoot dependency issues

You now understand BOTH ecosystems! 🎉
