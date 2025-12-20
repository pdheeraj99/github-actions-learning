# 🎓 GitHub Actions Caching - Masterclass

Mawa, welcome to the **Caching Masterclass!**

Ee folder lo caching gurinchi step-by-step, concept-by-concept cheptha - so clear aipothundi that you'll never forget it.

---

## 📚 Reading Order

| File | Focus | Time |
|------|-------|------|
| [01-the-problem.md](./01-the-problem.md) | **WHY** caching matters (with LIVE demo!) | 5 min |
| [02-runner-filesystem.md](./02-runner-filesystem.md) | **WHERE** things are stored in the Runner | 8 min |
| [03-how-actions-work.md](./03-how-actions-work.md) | **HOW** actions connect your repo to the runner | 8 min |
| [04-cache-key-magic.md](./04-cache-key-magic.md) | **WHEN** cache hits/misses happen (the hash secret) | 5 min |
| [05-docker-caching.md](./05-docker-caching.md) | **HOW** Docker caching connects to everything | 7 min |

---

## 🎬 Live Demo Results

I actually ran this demo LIVE! These are real numbers from **your** repository:

| Run | Cache Status | Total Time | Difference |
|-----|--------------|------------|------------|
| First (Run #3) | ❌ MISS | **2m 9s** | Baseline |
| Second (Run #4) | ✅ HIT | **1m 40s** | 29s saved! |

**Watch the recordings:**

- [00-delete-caches-recording.webp](./screenshots/00-delete-caches-recording.webp) - Deleting caches
- [01-first-run-recording.webp](./screenshots/01-first-run-recording.webp) - First run (Cache Miss)
- [02-second-run-recording.webp](./screenshots/02-second-run-recording.webp) - Second run (Cache Hit)

---

## 🎯 What You'll Understand After This

By the end of this masterclass:

1. ✅ **File System** - Exactly where your code, dependencies, and tools live in the runner
2. ✅ **The Flow** - How your code travels from GitHub → Runner → Build
3. ✅ **Cache Magic** - Why cache hits/misses happen
4. ✅ **Docker Connection** - How Docker caching fits into the picture
5. ✅ **Optimization** - How to structure your workflow for maximum caching

---

## 🗂️ Folder Structure

```
06-caching-masterclass/
├── 00-README.md          ← You are here
├── 01-the-problem.md     ← Start here!
├── 02-runner-filesystem.md
├── 03-how-actions-work.md
├── 04-cache-key-magic.md
├── 05-docker-caching.md
└── screenshots/
    ├── 00-delete-caches-recording.webp
    ├── 01-first-run-cache-miss.png
    ├── 01-first-run-recording.webp
    └── 02-second-run-recording.webp
```

---

**Ready? Start with [01-the-problem.md](./01-the-problem.md)!** 🚀
