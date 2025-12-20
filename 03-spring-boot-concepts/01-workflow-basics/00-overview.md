# 🚀 Workflow Basics - Overview

Welcome to the Spring Boot CI/CD workflow breakdown!

---

## 📚 This Folder Covers

| File | Topic | Time |
|------|-------|------|
| [01-checkout.md](./01-checkout.md) | Step 1: How code reaches the runner | 3 min |
| [02-setup-java.md](./02-setup-java.md) | Step 2: Java setup + cache magic | 5 min |
| [03-maven-commands.md](./03-maven-commands.md) | compile vs test vs package | 5 min |
| [04-artifacts.md](./04-artifacts.md) | JAR vs GitHub Artifact | 4 min |

---

## 🗺️ Job 1: Build & Test - The Journey

```
Your Code (GitHub) ──── Checkout ────▶ Runner VM
                                           │
                                           ▼
                           Setup Java (with Maven cache restore)
                                           │
                          ┌────────────────┼────────────────┐
                          ▼                ▼                ▼
                     mvn compile      mvn test       mvn package
                      (.class)       (Run tests)       (JAR!)
                                           │
                                           ▼
                              Upload Artifacts to GitHub
                              (JAR file + Test reports)
```

---

## ⏱️ Typical Timings

| Step | First Run | Cached Run |
|------|-----------|------------|
| Checkout | 2-3s | 2-3s |
| Setup Java | 10s + 60s download | 10s + 5s restore |
| Maven Build | 30-40s | 20-30s |
| Maven Test | 15-20s | 15-20s |
| Package JAR | 3-5s | 3-4s |
| **Total** | **~2 min** | **~1 min** |

**Cache saves ~1 minute per run!** ⚡

---

**Start with [01-checkout.md](./01-checkout.md)** 👉
