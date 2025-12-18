# 🏗️ 05 - Jobs and Steps

## 1. Jobs = The Work Definition

```yaml
jobs:
  build-and-push:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write
    steps:
      - ...
```

---

## 2. Job Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   jobs:                                                                      │
│     build-and-push:          ← Job ID (internal name, must be unique)      │
│       name: 🏗️ Build App     ← Display name (shows in UI)                  │
│       runs-on: ubuntu-latest ← Which machine                               │
│       permissions:           ← What access                                  │
│       steps:                 ← Individual tasks                            │
│                                                                              │
│   Multiple jobs example:                                                    │
│   jobs:                                                                      │
│     test:                    ← Job 1: Run tests                            │
│     build:                   ← Job 2: Build app                            │
│     deploy:                  ← Job 3: Deploy to server                     │
│                                                                              │
│   Jobs can run in PARALLEL or SEQUENTIAL (using needs:)                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Permissions (Security!)

```yaml
permissions:
  contents: read
  packages: write
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   contents: read                                                             │
│   ───────────────                                                            │
│   Repository code READ cheyachu ✅                                           │
│   But code ki WRITE kaadu (can't push commits)                              │
│                                                                              │
│   packages: write                                                            │
│   ────────────────                                                           │
│   GHCR ki images PUSH cheyachu ✅                                            │
│   Without this, image push FAILS!                                           │
│                                                                              │
│   BEST PRACTICE: Give MINIMUM permissions needed!                           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Steps = Individual Tasks

```yaml
steps:
  - name: Step 1
    uses: actions/checkout@v4
  - name: Step 2
    run: npm install
  - name: Step 3
    run: npm test
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Steps are like cooking recipe:                                            │
│                                                                              │
│   Step 1: Buy ingredients    (checkout code)                                │
│   Step 2: Marinate chicken   (install dependencies)                        │
│   Step 3: Cook               (build)                                        │
│   Step 4: Serve              (push to registry)                            │
│                                                                              │
│   Steps run ONE BY ONE (sequential)!                                        │
│   If Step 2 fails, Step 3 won't start!                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Point

Jobs = containers for work. Steps = individual tasks. Permissions = security access!
