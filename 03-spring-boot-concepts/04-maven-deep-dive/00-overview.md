# 🔧 Maven Deep Dive

Understanding Maven build tool for Java developers!

---

## 🎯 Telugu Simple ga

> "Maven = Java lo npm!"
> "pom.xml = package.json equivalent"

---

## 🆚 Maven vs npm Quick Reference

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        MAVEN vs NPM COMPARISON                                   │
│                                                                                  │
│   ┌─────────────────────────────┐       ┌─────────────────────────────┐         │
│   │          MAVEN              │       │           NPM               │         │
│   │        (Java)               │       │       (JavaScript)          │         │
│   └─────────────────────────────┘       └─────────────────────────────┘         │
│                                                                                  │
│   pom.xml              ←───────────▶    package.json                            │
│   ~/.m2/repository/    ←───────────▶    node_modules/                           │
│   mvn install          ←───────────▶    npm install                             │
│   mvn test             ←───────────▶    npm test                                │
│   mvn package → JAR    ←───────────▶    npm run build → dist/                   │
│   Maven Central        ←───────────▶    npmjs.com                               │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📄 pom.xml Structure

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <!-- Project Identity -->
    <groupId>com.example</groupId>        <!-- Company/org name -->
    <artifactId>todo-backend</artifactId> <!-- Project name -->
    <version>0.0.1-SNAPSHOT</version>     <!-- Version -->
    <packaging>jar</packaging>            <!-- Output type -->

    <!-- Parent (Spring Boot) -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
    </parent>

    <!-- Dependencies (Your libraries) -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- Plugins (Build tools) -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🔄 Maven Lifecycle

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           MAVEN BUILD LIFECYCLE                                  │
│                                                                                  │
│   mvn clean ────────▶ Delete target/ folder                                    │
│       │                                                                          │
│       ▼                                                                          │
│   mvn compile ──────▶ Compile .java → .class files                             │
│       │               src/main/java → target/classes                            │
│       ▼                                                                          │
│   mvn test ─────────▶ Run unit tests                                           │
│       │               src/test/java                                              │
│       ▼                                                                          │
│   mvn package ──────▶ Create JAR/WAR file                                       │
│       │               target/todo-backend-0.0.1-SNAPSHOT.jar                    │
│       ▼                                                                          │
│   mvn install ──────▶ Copy JAR to ~/.m2/repository                              │
│       │               (local repository)                                         │
│       ▼                                                                          │
│   mvn deploy ───────▶ Upload to remote repository                               │
│                       (Maven Central, Nexus, etc.)                               │
│                                                                                  │
│   IMPORTANT: Each phase runs ALL previous phases!                               │
│   mvn package = mvn compile + test + package                                    │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📂 .m2 Repository

```
~/.m2/repository/                          ← GLOBAL cache (shared across projects!)
├── org/
│   └── springframework/
│       └── boot/
│           └── spring-boot-starter-web/
│               └── 3.2.0/
│                   └── spring-boot-starter-web-3.2.0.jar
├── com/
│   └── fasterxml/
│       └── jackson/
│           └── core/
│               └── jackson-databind/
│                   └── 2.15.0/
│                       └── jackson-databind-2.15.0.jar
└── ...

Size: Can be 1-2GB (but shared across ALL Java projects!)
```

---

## 🔧 Common Commands

| Command | What It Does |
|---------|--------------|
| `mvn clean` | Delete target/ folder |
| `mvn compile` | Compile source code |
| `mvn test` | Run unit tests |
| `mvn package` | Create JAR file |
| `mvn package -DskipTests` | Create JAR, skip tests |
| `mvn dependency:tree` | Show dependency tree |
| `mvn dependency:resolve` | Download dependencies |
| `mvn dependency:go-offline` | Download ALL for offline use |

---

## 🎯 In CI/CD Pipeline

```yaml
# What we use in GitHub Actions:
- name: 📦 Build and Test
  run: mvn test -B
  # -B = Batch mode (no interactive prompts)

- name: 📦 Package JAR
  run: mvn package -DskipTests -B
  # -DskipTests = Already tested above!
```

---

## 🎬 Telugu Summary

```
Maven = Java build tool

pom.xml = Project config:
─────────────────────────
• groupId - Company name
• artifactId - Project name
• version - Your version
• dependencies - Libraries
• plugins - Build tools

~/.m2/repository = Cache
─────────────────────────
• All downloaded JARs here
• Shared across projects
• Like global node_modules

Lifecycle:
──────────
clean → compile → test → package → install → deploy

CI/CD lo use:
─────────────
mvn test     ← Run tests
mvn package -DskipTests  ← Build JAR
```

---

**Related: [Workflow Maven Commands](../01-workflow-basics/03-maven-commands.md)** 👉
