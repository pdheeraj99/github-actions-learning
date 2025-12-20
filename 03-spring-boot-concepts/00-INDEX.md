# 📚 Spring Boot CI/CD Concepts - Master Index

Welcome to the reorganized Spring Boot learning path!

---

## 🗺️ Learning Path

```
START HERE!
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 1️⃣  WORKFLOW BASICS                                         │
│     Understanding Job 1: Build & Test                       │
│     [01-workflow-basics/](./01-workflow-basics/)            │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 2️⃣  RUNNER INTERNALS                                        │
│     Where things live in the VM                             │
│     [02-runner-internals/](./02-runner-internals/)          │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 3️⃣  CACHING EXPLAINED                                       │
│     Maven cache + Docker cache                              │
│     [03-caching-explained/](./03-caching-explained/)        │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 4️⃣  MAVEN DEEP DIVE                                         │
│     pom.xml, lifecycle, dependencies                        │
│     [04-maven-deep-dive/](./04-maven-deep-dive/)            │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 5️⃣  DOCKER FOR SPRING BOOT                                  │
│     Multi-stage builds, JDK vs JRE                          │
│     [05-docker-for-springboot/](./05-docker-for-springboot/)│
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 6️⃣  DOCKER INTERNALS (NEW!)                                 │
│     Docker Desktop vs Engine, WSL2, dockerd/containerd/runc │
│     [06-docker-internals/](./06-docker-internals/)          │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 7️⃣  GHCR & ARTIFACTS (NEW!)                                 │
│     Storage locations, build optimization, cleanup          │
│     [07-ghcr-and-artifacts/](./07-ghcr-and-artifacts/)      │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 Folder Structure

```
03-spring-boot-concepts/
│
├── 00-INDEX.md                      ← You are here!
│
├── 01-workflow-basics/              ← 5 files
│   ├── 00-overview.md
│   ├── 01-checkout.md
│   ├── 02-setup-java.md
│   ├── 03-maven-commands.md         ⭐ compile vs test vs package
│   └── 04-artifacts.md              ⭐ JAR vs Artifact
│
├── 02-runner-internals/             ← 4 files
│   ├── 00-overview.md
│   ├── 01-filesystem.md             ⭐ Where code lives
│   ├── 02-hostedtoolcache.md        ⭐ Pre-installed tools
│   └── 03-how-actions-work.md       ⭐ YAML → Script
│
├── 03-caching-explained/            ← 6 files
│   ├── 00-README.md                 
│   ├── 01-the-problem.md            ⭐ Live demo results!
│   ├── 02-runner-filesystem.md
│   ├── 03-how-actions-work.md
│   ├── 04-cache-key-magic.md        ⭐ Hash/fingerprint
│   └── 05-docker-caching.md
│
├── 04-maven-deep-dive/              
│   └── 00-overview.md
│
├── 05-docker-for-springboot/        
│   └── 00-overview.md
│
├── 06-docker-internals/             ← 4 files (NEW!)
│   ├── 00-overview.md
│   ├── 01-architecture.md           ⭐ Docker Desktop vs Engine
│   ├── 02-wsl2-windows.md           ⭐ WSL2, Hyper-V
│   └── 03-engine-components.md      ⭐ dockerd, containerd, runc
│
├── 07-ghcr-and-artifacts/           ← 5 files (NEW!)
│   ├── 00-overview.md
│   ├── 01-ghcr-explained.md         ⭐ GitHub Container Registry
│   ├── 02-artifacts-explained.md    ⭐ Sharing files between jobs
│   ├── 03-build-optimization.md     ⭐ Build JAR once strategy
│   └── 04-cleanup-workflow.md       ⭐ Delete caches & artifacts
│
└── archive/                         ← Old files (reference)
```

---

## ⭐ Key Files (Must Read!)

| Topic | File |
|-------|------|
| **Maven commands** | [03-maven-commands.md](./01-workflow-basics/03-maven-commands.md) |
| **JAR vs Artifact** | [04-artifacts.md](./01-workflow-basics/04-artifacts.md) |
| **Where code lives** | [01-filesystem.md](./02-runner-internals/01-filesystem.md) |
| **Cache key magic** | [04-cache-key-magic.md](./03-caching-explained/04-cache-key-magic.md) |
| **Docker Desktop vs Engine** | [01-architecture.md](./06-docker-internals/01-architecture.md) |
| **dockerd/containerd/runc** | [03-engine-components.md](./06-docker-internals/03-engine-components.md) |
| **GHCR explained** | [01-ghcr-explained.md](./07-ghcr-and-artifacts/01-ghcr-explained.md) |
| **Build optimization** | [03-build-optimization.md](./07-ghcr-and-artifacts/03-build-optimization.md) |

---

## ✅ Learning Checklist

- [ ] Read workflow basics (Job 1 steps)
- [ ] Understand runner filesystem
- [ ] Master caching concepts
- [ ] Learn Maven commands
- [ ] Understand Docker multi-stage
- [ ] Learn Docker internal architecture
- [ ] Understand GHCR vs Artifacts
- [ ] Know build optimization strategies

---

## 🔗 Related Resources

- **Docker Concepts:** `../01-docker-concepts/`
- **GitHub Actions Concepts:** `../02-github-actions-concepts/`
- **Actual Workflow File:** `../.github/workflows/spring-boot-build.yml`
- **Spring Boot Code:** `../02-spring-boot-pipeline/todo-backend/`

---

**Start with [01-workflow-basics/00-overview.md](./01-workflow-basics/00-overview.md)!** 🚀
