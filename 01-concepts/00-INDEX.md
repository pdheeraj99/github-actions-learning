# 🐳 Docker Concepts Documentation - INDEX

## Read This First

Ee documentation lo Docker concepts anni **Telugu-English mix** lo explain chesanu.
Step-by-step chadhuvuko - chala clarity vasthundhi!

---

## 📚 Reading Order (Isthe Order lo Chadhivthe Best!)

| # | File | Topic | Time |
|---|------|-------|------|
| 1 | [01-what-is-docker.md](./01-what-is-docker.md) | Docker basics, Image vs Container, Hotel analogy | 10 min |
| 2 | [02-dockerfile-anatomy.md](./02-dockerfile-anatomy.md) | FROM, WORKDIR, COPY, RUN, EXPOSE, CMD - each command | 15 min |
| 3 | [03-multistage-build.md](./03-multistage-build.md) | Multi-stage magic - 95% smaller images! | 15 min |
| 4 | [04-layer-caching.md](./04-layer-caching.md) | Why package.json first? Build time optimize! | 15 min |
| 5 | [05-build-cache-vs-images.md](./05-build-cache-vs-images.md) | Kitchen & Dining analogy - base images ekkada? | 10 min |
| 6 | [06-wsl2-architecture.md](./06-wsl2-architecture.md) | Windows lo Docker ela work avuthundhi? | 10 min |
| 7 | [07-why-images-small.md](./07-why-images-small.md) | 5GB OS vs 200MB Image - enduku chinna? | 10 min |
| 8 | [08-alpine-explained.md](./08-alpine-explained.md) | Alpine Linux, glibc vs musl gotcha | 10 min |
| 9 | [09-npm-ci-vs-install.md](./09-npm-ci-vs-install.md) | CI/CD lo npm ci enduku? | 10 min |
| 10 | [10-interview-questions.md](./10-interview-questions.md) | 40LPA+ Interview prep! 🎯 | 20 min |
| 11 | [11-github-runners.md](./11-github-runners.md) | GitHub Actions - Docker ekkada run avuthundhi? | 10 min |

---

## 🎯 Key Concepts Quick Reference

| Concept | One-Line Telugu |
|---------|-----------------|
| Docker | App ni dabba lo petti ekkadikaina teeskelthav |
| Image | Template/Recipe - disk lo stored, run avvadhu |
| Container | Running instance - memory lo live! |
| Multi-stage | Build stage throw, run stage keep - chinna image! |
| Layer caching | package.json first - 2 mins save! |
| Build cache | Kitchen lo ingredients - hidden but available |
| WSL2 | Windows lo Linux kernel - Docker ikkada run |
| Alpine | Tiny Linux - 5MB - but musl gotcha undhi |
| npm ci | EXACT versions - reproducible builds! |

---

## 📂 Folder Structure

```
01-concepts/
├── 00-INDEX.md              ← YOU ARE HERE!
├── 01-what-is-docker.md
├── 02-dockerfile-anatomy.md
├── 03-multistage-build.md
├── 04-layer-caching.md
├── 05-build-cache-vs-images.md
├── 06-wsl2-architecture.md
├── 07-why-images-small.md
├── 08-alpine-explained.md
├── 09-npm-ci-vs-install.md
└── 10-interview-questions.md
```

---

## ✅ All Concepts Covered

- [x] Docker basics (Image vs Container)
- [x] Dockerfile commands (FROM, WORKDIR, COPY, RUN, EXPOSE, CMD)
- [x] Multi-stage builds (95% size reduction)
- [x] Layer caching (package.json first optimization)
- [x] Build cache vs Image storage (Kitchen & Dining analogy)
- [x] WSL2 architecture (Docker on Windows)
- [x] Why images are small (Kernel sharing)
- [x] Alpine Linux (glibc vs musl gotcha)
- [x] npm ci vs npm install (Reproducible builds)
- [x] Interview questions (40LPA+ level)

---

**Happy Learning! 🚀**
