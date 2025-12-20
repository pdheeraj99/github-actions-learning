# 📦 JAR vs Artifact: What's the Difference?

## 🎯 Telugu Simple ga

> **JAR** = Java build output (like dist/ folder for React)
> **Artifact** = Anything uploaded to GitHub for later download

**JAR is a type of file. Artifact is a GitHub Actions concept!**

---

## 📊 Side-by-Side Comparison

| Aspect | JAR File | GitHub Artifact |
|--------|----------|-----------------|
| **What is it?** | Java archive file | Any file uploaded to GitHub |
| **Created by** | `mvn package` | `actions/upload-artifact` |
| **Contains** | Code + dependencies | Can be ANYTHING! |
| **File extension** | `.jar` | No extension |
| **Where stored** | `target/` folder | GitHub Actions storage |
| **Use case** | Run the application | Share between jobs/download |

---

## 🔄 The Flow in Your Workflow

```
mvn package
    │
    ▼
target/todo-backend-0.0.1-SNAPSHOT.jar  ← This is the JAR (build output)
    │
    │ upload-artifact
    ▼
GitHub Artifact Storage  ← Now it's ALSO an Artifact!
    │
    │ download-artifact (in another job)
    ▼
Docker build uses the JAR
```

---

## 📝 Your Workflow Code

### Step 1: Create JAR

```yaml
- name: 📦 Package JAR
  run: mvn package -DskipTests
  # Creates: target/todo-backend-0.0.1-SNAPSHOT.jar
```

### Step 2: Upload as Artifact

```yaml
- name: 📦 Upload JAR
  uses: actions/upload-artifact@v4
  with:
    name: todo-backend-jar          # Artifact name (your choice)
    path: ./target/*.jar            # Path to JAR file
    retention-days: 7               # Keep for 7 days
```

### Step 3: Download in Docker Job

```yaml
# In Job 2: Docker Build
- name: 📥 Download JAR
  uses: actions/download-artifact@v4
  with:
    name: todo-backend-jar          # Same name as upload!
    path: ./02-spring-boot-pipeline/todo-backend/target/
```

---

## 🤔 Why Upload JAR as Artifact?

### Reason 1: Jobs Don't Share Files

```
Job 1 (Build)          Job 2 (Docker)
┌─────────────┐        ┌─────────────┐
│ Different   │        │ Different   │
│ Runner VM!  │   ❌   │ Runner VM!  │
│             │ ─────▶ │             │
│ Has JAR     │        │ No JAR!     │
└─────────────┘        └─────────────┘
         │                    │
         │ SOLUTION:          │
         ▼                    ▼
    Upload                Download
   Artifact               Artifact
         │                    │
         └────▶ GitHub ◀──────┘
               Storage
```

### Reason 2: Download Later

You can download artifacts from GitHub UI:

```
Actions → Workflow Run → Artifacts section → Download
```

---

## 📸 Where to See Artifacts in GitHub?

```
GitHub → Actions → Your Workflow Run
                          │
                          ▼
    ┌─────────────────────────────────────────┐
    │ Artifacts                                │
    │ ─────────                                │
    │ 📦 todo-backend-jar        Download      │
    │ 📊 test-results            Download      │
    └─────────────────────────────────────────┘
```

---

## 🔑 Key Points

| Concept | Description |
|---------|-------------|
| **JAR is a file** | Created by Maven, contains your app |
| **Artifact is a concept** | Anything uploaded to GitHub Actions |
| **JAR can be Artifact** | Upload JAR to share between jobs |
| **Not all Artifacts are JARs** | Test reports, logs are also artifacts |
| **Retention** | Artifacts expire (7-90 days) |

---

## 🎬 Telugu Summary

```
mvn package → JAR file create avuthundi (target/ folder lo)
            ↓
upload-artifact → GitHub ki upload avuthundi (7 days untundi)
            ↓
download-artifact → Next job lo download chesi use cheyochu

JAR = Build output file
Artifact = GitHub lo share cheyyadaniki upload chesina file
```

---

**Congratulations! You've completed the Workflow Basics module!** 🎉

**Next folder: [../02-runner-internals/](../02-runner-internals/00-overview.md)** 👉
