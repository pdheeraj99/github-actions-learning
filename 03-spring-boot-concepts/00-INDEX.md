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
│     [01-workflow-basics/](./01-workflow-basics/00-overview.md)                          │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 2️⃣  RUNNER INTERNALS                                        │
│     Where things live in the VM                             │
│     [02-runner-internals/](./02-runner-internals/00-overview.md)                        │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 3️⃣  CACHING EXPLAINED                                       │
│     Maven cache + Docker cache                              │
│     [03-caching-explained/](./03-caching-explained/00-README.md)                       │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 4️⃣  MAVEN DEEP DIVE                                         │
│     pom.xml, lifecycle, dependencies                        │
│     [04-maven-deep-dive/](./04-maven-deep-dive/00-overview.md)                         │
└─────────────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────────────┐
│ 5️⃣  DOCKER FOR SPRING BOOT                                  │
│     Multi-stage builds, JDK vs JRE                          │
│     [05-docker-for-springboot/](./05-docker-for-springboot/00-overview.md)                   │
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
│   ├── 04-artifacts.md              ⭐ JAR vs Artifact
│   └── screenshots/
│
├── 02-runner-internals/             ← 4 files
│   ├── 00-overview.md
│   ├── 01-filesystem.md             ⭐ Where code lives
│   ├── 02-hostedtoolcache.md        ⭐ Pre-installed tools
│   ├── 03-how-actions-work.md       ⭐ YAML → Script
│   └── diagrams/                    ← Your Excalidraw!
│
├── 03-caching-explained/            ← 6 files
│   ├── 00-README.md                 
│   ├── 01-the-problem.md            ⭐ Live demo results!
│   ├── 02-runner-filesystem.md
│   ├── 03-how-actions-work.md
│   ├── 04-cache-key-magic.md        ⭐ Hash/fingerprint
│   ├── 05-docker-caching.md
│   └── screenshots/                 ← Live recordings!
│
├── 04-maven-deep-dive/              ← Reference
│   └── 00-overview.md
│
├── 05-docker-for-springboot/        ← Reference
│   └── 00-overview.md
│
└── archive/                         ← Old files (reference)
    ├── 01-spring-boot-workflow-explained.md
    ├── 02-maven-vs-npm-comparison.md
    ├── 03-spring-boot-docker-explained.md
    ├── 04-spring-boot-visual-workflow-guide.md
    ├── 05-github-actions-caching-deep-dive.md
    └── 06-caching-masterclass/
```

---

## ⭐ Key Files (Start Here!)

| Topic | File |
|-------|------|
| **Maven commands** | [03-maven-commands.md](./01-workflow-basics/03-maven-commands.md) |
| **JAR vs Artifact** | [04-artifacts.md](./01-workflow-basics/04-artifacts.md) |
| **Where code lives** | [01-filesystem.md](./02-runner-internals/01-filesystem.md) |
| **Cache key magic** | [04-cache-key-magic.md](./03-caching-explained/04-cache-key-magic.md) |

---

## ✅ Learning Checklist

- [ ] Read workflow basics (Job 1 steps)
- [ ] Understand runner filesystem
- [ ] Master caching concepts
- [ ] Learn Maven commands
- [ ] Understand Docker multi-stage

---

## 🔗 Related Resources

- **React Pipeline:** `../01-react-pipeline/`
- **Actual Workflow File:** `../.github/workflows/spring-boot-build.yml`
- **Spring Boot Code:** `../02-spring-boot-pipeline/todo-backend/`

---

**Start with [01-workflow-basics/00-overview.md](./01-workflow-basics/00-overview.md)!** 🚀
