# 🔧 Maven Deep Dive - Overview

Understanding Maven for Java developers!

---

## 📚 This Folder Covers

| File | Topic | Time |
|------|-------|------|
| [01-pom-xml.md](./01-pom-xml.md) | Understanding pom.xml structure | 5 min |
| [02-lifecycle.md](./02-lifecycle.md) | clean, compile, test, package | 4 min |
| [03-dependencies.md](./03-dependencies.md) | How .m2 works | 5 min |

---

## 🆚 Maven vs npm Quick Reference

| Maven (Java) | npm (JavaScript) |
|--------------|------------------|
| `pom.xml` | `package.json` |
| `~/.m2/repository/` | `node_modules/` |
| `mvn install` | `npm install` |
| `mvn test` | `npm test` |
| `mvn package` → JAR | `npm run build` → dist/ |
| Maven Central | npmjs.com |

---

## 📂 Core Files

### pom.xml (Project Object Model)

```xml
<project>
  <groupId>com.example</groupId>
  <artifactId>todo-backend</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
  </dependencies>
</project>
```

---

## 🔄 Common Commands

```bash
# Clean build folder
mvn clean

# Compile code
mvn compile

# Run tests
mvn test

# Create JAR
mvn package

# Skip tests
mvn package -DskipTests
```

---

## 📁 For Detailed Content

Check the archive folder for the original Maven comparison document:
`../archive/02-maven-vs-npm-comparison.md`

---

**Related: [../01-workflow-basics/03-maven-commands.md](../01-workflow-basics/03-maven-commands.md)** 👉
