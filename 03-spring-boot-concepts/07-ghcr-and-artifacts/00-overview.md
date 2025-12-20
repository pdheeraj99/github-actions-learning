# 📦 GHCR & Artifacts - Overview

Understanding where Docker images and workflow files are stored!

---

## 📚 This Folder Covers

| File | Topic | Time |
|------|-------|------|
| [01-ghcr-explained.md](./01-ghcr-explained.md) | GitHub Container Registry | 5 min |
| [02-artifacts-explained.md](./02-artifacts-explained.md) | Workflow Artifacts | 5 min |
| [03-build-optimization.md](./03-build-optimization.md) | Build JAR once strategy | 7 min |
| [04-cleanup-workflow.md](./04-cleanup-workflow.md) | Delete caches & artifacts | 5 min |

---

## 🔑 Quick Comparison

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        STORAGE TYPES IN GITHUB                                   │
│                                                                                  │
│   ┌────────────────────────┐         ┌────────────────────────────┐             │
│   │    GHCR (Packages)     │         │   Artifacts (Workflow)     │             │
│   │    ════════════════    │         │   ════════════════════     │             │
│   │                        │         │                            │             │
│   │ WHERE:                 │         │ WHERE:                     │             │
│   │ Profile → Packages     │         │ Repo → Actions → Run       │             │
│   │                        │         │                            │             │
│   │ WHAT:                  │         │ WHAT:                      │             │
│   │ Docker images          │         │ Any files (JAR, logs)      │             │
│   │                        │         │                            │             │
│   │ LIFETIME:              │         │ LIFETIME:                  │             │
│   │ Permanent              │         │ 7-90 days                  │             │
│   │                        │         │                            │             │
│   │ USED FOR:              │         │ USED FOR:                  │             │
│   │ Deployment (K8s,       │         │ Share between jobs,        │             │
│   │ Docker run)            │         │ Download manually          │             │
│   │                        │         │                            │             │
│   └────────────────────────┘         └────────────────────────────┘             │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 When to Use What?

| Scenario | Use GHCR | Use Artifacts |
|----------|----------|---------------|
| Deploy to Kubernetes | ✅ | ❌ |
| Share JAR between jobs | ❌ | ✅ |
| Download build output | ❌ | ✅ |
| Run with `docker pull` | ✅ | ❌ |
| Test reports | ❌ | ✅ |

---

**Start with [01-ghcr-explained.md](./01-ghcr-explained.md)** 👉
