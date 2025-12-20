# 🐳 Docker Caching: The Connection

Mawa, now let's connect everything together. You've learned about Maven caching - but Docker has its **own caching system**. Let me explain how it fits in.

---

## 🤔 The Docker Problem

Remember: Every workflow runs on a **fresh VM**.

On your local machine:

```
First docker build: 5 minutes (downloads base image, builds layers)
Second docker build: 30 seconds (uses cached layers) ✅
```

On GitHub Actions (without special handling):

```
First build: 5 minutes
Second build: 5 minutes AGAIN! ❌
```

Why? Because the VM is destroyed! Docker's local cache goes with it.

---

## 🔧 The Solution: BuildX + GitHub Actions Cache

Docker BuildX is an **advanced builder** that can store layer cache externally - outside the VM!

```yaml
# Your workflow
- name: Set up Docker Buildx
  uses: docker/setup-buildx-action@v3

- name: Build and push
  uses: docker/build-push-action@v5
  with:
    context: ./todo-backend
    push: true
    tags: ghcr.io/yourname/app:latest
    cache-from: type=gha          # ← Read cache from GitHub
    cache-to: type=gha,mode=max   # ← Write cache to GitHub
```

**What this does:**

- `cache-from: type=gha` → Before building, fetch cached layers from GitHub
- `cache-to: type=gha,mode=max` → After building, save layers to GitHub

---

## 📦 How Docker Layers Work

Every Dockerfile instruction creates a **layer**:

```dockerfile
FROM eclipse-temurin:17-jre-alpine    # Layer 1: Base image (50 MB)
WORKDIR /app                          # Layer 2: Create folder
COPY target/*.jar app.jar             # Layer 3: Copy JAR
EXPOSE 8080                           # Layer 4: Document port
ENTRYPOINT ["java", "-jar", "app.jar"] # Layer 5: Set command
```

**Caching rule:** If a layer hasn't changed, use cached version. If it DID change, rebuild that layer + ALL layers after it!

---

## 📊 Layer Caching Scenarios

### Scenario 1: Nothing Changed

```
Dockerfile unchanged, JAR file unchanged

Layer 1: FROM eclipse-temurin:17  → CACHED ✅
Layer 2: WORKDIR /app             → CACHED ✅
Layer 3: COPY target/*.jar        → CACHED ✅ (same JAR!)
Layer 4: EXPOSE 8080              → CACHED ✅
Layer 5: ENTRYPOINT               → CACHED ✅

Result: Almost instant! Just uses all cached layers.
```

### Scenario 2: Code Changed (Different JAR)

```
You changed src/TodoController.java
This means target/app.jar is DIFFERENT

Layer 1: FROM eclipse-temurin:17  → CACHED ✅
Layer 2: WORKDIR /app             → CACHED ✅
Layer 3: COPY target/*.jar        → REBUILD ❌ (JAR changed!)
Layer 4: EXPOSE 8080              → REBUILD ❌ (after changed layer)
Layer 5: ENTRYPOINT               → REBUILD ❌

Result: Rebuilds Layer 3, 4, 5. But Layer 1, 2 from cache!
         Still faster than starting from scratch.
```

### Scenario 3: Base Image Changed

```
You changed: FROM eclipse-temurin:17 to FROM eclipse-temurin:21

Layer 1: FROM eclipse-temurin:21  → REBUILD ❌ (new image!)
Layer 2: WORKDIR /app             → REBUILD ❌
Layer 3: COPY target/*.jar        → REBUILD ❌
Layer 4: EXPOSE 8080              → REBUILD ❌
Layer 5: ENTRYPOINT               → REBUILD ❌

Result: Everything rebuilds. No caching possible.
```

---

## 🏗️ Optimal Dockerfile Structure

For **maximum caching**, put things that change **LEAST at the TOP**, things that change **MOST at the BOTTOM**.

### ❌ Bad Structure (for Node.js example)

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY . .                    # ← Copies EVERYTHING
RUN npm install             # ← Runs EVERY time something changes!
RUN npm run build
```

Problem: Even a tiny code change invalidates `npm install` layer!

### ✅ Good Structure

```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./       # ← Copy ONLY package files first
RUN npm install             # ← Only rebuilds if package.json changes!
COPY . .                    # ← Then copy source code
RUN npm run build
```

Now source code changes don't affect `npm install` layer!

---

## 🔄 The Complete Docker Caching Flow

```
┌──────────────────────────────────────────────────────────────────┐
│                   WORKFLOW STARTS                                │
└──────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│ STEP: Setup Buildx                                               │
│                                                                  │
│ Creates a builder that supports external cache                   │
└──────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│ STEP: Build and Push                                             │
│                                                                  │
│ 1. cache-from: type=gha                                         │
│    └── "GitHub, do you have cached layers for this project?"    │
│        ├── Yes → Download layer cache                            │
│        └── No → Start from scratch                               │
│                                                                  │
│ 2. Build starts...                                               │
│    ├── Layer 1: Check cache → Use if available                  │
│    ├── Layer 2: Check cache → Use if available                  │
│    ├── Layer 3: Hash of source changed? Rebuild : use cache     │
│    └── ...                                                       │
│                                                                  │
│ 3. Push image to registry                                        │
│                                                                  │
│ 4. cache-to: type=gha,mode=max                                  │
│    └── "GitHub, save these layers for next time!"               │
│        └── All layers uploaded to GitHub cache                   │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────────┐
│                   VM DESTROYED                                   │
│                                                                  │
│   But Docker layer cache is SAFE in GitHub Cloud! ✅            │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🔗 Maven Cache vs Docker Cache

| Aspect | Maven Cache | Docker Cache |
|--------|-------------|--------------|
| **What's cached** | JAR dependencies | Image layers |
| **Where stored** | GitHub Actions Cache | GitHub Actions Cache |
| **Triggered by** | `cache: 'maven'` in setup-java | `cache-from/to: type=gha` |
| **Invalidation** | pom.xml hash changes | Dockerfile/content changes |
| **Size limit** | 10 GB per repo | 10 GB per repo (shared!) |

Both use the same GitHub Actions cache storage!

---

## 📊 Your Workflow: Both Caches Working Together

```
Your Spring Boot CI/CD:

1. Maven Cache
   └── Dependencies: spring-boot, jackson, etc.
   └── Stored in: GitHub Cache
   └── Restored to: /home/runner/.m2

2. Docker Cache  
   └── Layers: base image, WORKDIR, COPY, etc.
   └── Stored in: GitHub Cache
   └── Used by: BuildX during docker build

Total Cache Used: ~150 MB
Total Time Savings: 30-60 seconds per run!
```

---

## 🎯 Key Takeaways

1. **Docker normally loses cache** when VM dies
2. **BuildX + GHA cache** solves this by storing layers externally
3. **Layer order matters** - put stable things first!
4. **Both Maven and Docker** use the same GitHub cache storage
5. **10 GB limit shared** - don't cache unnecessary things

---

## 🎉 Congratulations

You've completed the Caching Masterclass! You now understand:

- ✅ Why caching matters (real numbers!)
- ✅ Where things live in the runner
- ✅ How actions work behind the scenes
- ✅ How cache keys and hashes work
- ✅ How Docker caching connects

**Go back to [00-README.md](./00-README.md) to review or share with others!**
