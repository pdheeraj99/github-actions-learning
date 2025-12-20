# 🔨 Maven Commands: compile vs test vs package

## 🎯 Telugu Simple ga

> `compile` = Code ni .class files lo convert chesukundi
> `test` = Tests run cheyyadam
> `package` = Everything ni JAR file lo zip chesukundi

---

## 📊 What Each Command Creates

```
mvn clean    → Delete target/ folder (fresh start)

mvn compile  → target/classes/
              ├── com/example/TodoController.class
              ├── com/example/TodoService.class
              └── ... (compiled bytecode)
              ❌ NO JAR FILE!

mvn test     → Runs tests + creates reports
              target/surefire-reports/
              ├── TEST-TodoControllerTest.xml
              └── TEST-TodoApplicationTests.xml
              ❌ STILL NO JAR FILE!

mvn package  → target/todo-backend-0.0.1-SNAPSHOT.jar ✅
              THIS IS THE JAR! 📦
```

---

## 🔄 Maven Lifecycle (Order Matters!)

```
clean ──▶ compile ──▶ test ──▶ package ──▶ install ──▶ deploy
  │          │          │          │
  │          │          │          └── Creates JAR + runs all above
  │          │          └── Runs tests + runs compile
  │          └── Compiles code only
  └── Deletes target folder
```

**Key Rule:** Each phase runs ALL previous phases!

```bash
mvn package   # This ALSO runs: compile → test → package
mvn test      # This ALSO runs: compile → test
mvn compile   # This ONLY runs: compile
```

---

## 💡 Why Our Workflow Uses Separate Commands?

```yaml
# Step 1: Build only
- run: mvn clean compile

# Step 2: Run tests (reports needed separately)  
- run: mvn test

# Step 3: Create JAR (skip tests - already done!)
- run: mvn package -DskipTests
```

**Why not just `mvn package`?**

1. **Better error messages** - Know exactly which step failed
2. **Parallel jobs possible** - Compile → Test → Package can show progress
3. **Skip redundant work** - `-DskipTests` because tests already ran!

---

## 📂 What Goes in the JAR?

```
todo-backend-0.0.1-SNAPSHOT.jar (Fat JAR / Uber JAR)
│
├── BOOT-INF/
│   ├── classes/          ← Your compiled code
│   │   └── com/example/TodoController.class
│   ├── lib/              ← ALL dependencies!
│   │   ├── spring-boot-3.2.0.jar
│   │   ├── spring-web-6.1.0.jar
│   │   └── ... (200+ JARs packed inside!)
│   └── classpath.idx
│
├── META-INF/
│   ├── MANIFEST.MF       ← Entry point (main class)
│   └── maven/            ← Build metadata
│
└── org/springframework/boot/loader/  ← Spring Boot launcher
```

**This is a "Fat JAR" - contains EVERYTHING needed to run!**

```bash
java -jar todo-backend-0.0.1-SNAPSHOT.jar
# That's it! No other dependencies needed!
```

---

## 🆚 Compare with React

| Maven (Java) | npm (React) |
|--------------|-------------|
| `mvn compile` | `npm run build` (partial) |
| `mvn test` | `npm test` |
| `mvn package` → JAR | `npm run build` → dist/ folder |
| JAR = single file | dist/ = multiple files |
| `java -jar app.jar` | Need nginx to serve |

---

## 🔑 Key Points

| Command | Creates | Contains |
|---------|---------|----------|
| `mvn compile` | `target/classes/` | .class files only |
| `mvn test` | `target/surefire-reports/` | Test results |
| `mvn package` | `target/*.jar` | Everything (Fat JAR) |

---

**Next: [04-artifacts.md](./04-artifacts.md)** 👉
