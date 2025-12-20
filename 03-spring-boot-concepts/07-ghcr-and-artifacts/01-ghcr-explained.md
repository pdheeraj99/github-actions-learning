# 🐳 GitHub Container Registry (GHCR) Explained

Where Docker images live in GitHub!

---

## 🎯 Telugu Simple ga

> "Docker Hub lanti registry - but GitHub lo!"
> "Free for public repos, linked to your GitHub account"

---

## 📍 What is GHCR?

```
GHCR = GitHub Container Registry

• Docker image storage
• Part of GitHub Packages
• Free for public repos
• Linked to GitHub account/org
• URL: ghcr.io/<username>/<image>
```

---

## 📍 Where to Find It

```
Method 1: Profile → Packages
─────────────────────────────
https://github.com/<username>?tab=packages

Method 2: Repository → Packages (right sidebar)
──────────────────────────────────────────────
https://github.com/<username>/<repo> 
→ Look at right sidebar → "Packages"
```

---

## 🔄 How Images Get There

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        DOCKER BUILD & PUSH FLOW                                  │
│                                                                                  │
│   GitHub Actions Workflow                                                        │
│   ════════════════════════                                                       │
│                                                                                  │
│   ┌────────────────────────────────────────────────────────────────────────┐    │
│   │  Step 1: Login to GHCR                                                 │    │
│   │                                                                        │    │
│   │  - uses: docker/login-action@v3                                        │    │
│   │    with:                                                               │    │
│   │      registry: ghcr.io                                                 │    │
│   │      username: ${{ github.actor }}      ← Your GitHub username        │    │
│   │      password: ${{ secrets.GITHUB_TOKEN }}  ← Auto-provided!          │    │
│   │                                                                        │    │
│   │  Result: ~/.docker/config.json has credentials                        │    │
│   └────────────────────────────────────────────────────────────────────────┘    │
│                │                                                                 │
│                ▼                                                                 │
│   ┌────────────────────────────────────────────────────────────────────────┐    │
│   │  Step 2: Build & Push                                                  │    │
│   │                                                                        │    │
│   │  - uses: docker/build-push-action@v5                                   │    │
│   │    with:                                                               │    │
│   │      context: ./my-app                                                 │    │
│   │      push: true                                                        │    │
│   │      tags: ghcr.io/pdheeraj99/todo-backend:latest                     │    │
│   │                                                                        │    │
│   └────────────────────────────────────────────────────────────────────────┘    │
│                │                                                                 │
│                ▼                                                                 │
│   ┌────────────────────────────────────────────────────────────────────────┐    │
│   │  GHCR (GitHub Cloud)                                                   │    │
│   │                                                                        │    │
│   │  📦 ghcr.io/pdheeraj99/todo-backend                                   │    │
│   │     ├── :latest       (20MB)                                          │    │
│   │     ├── :abc1234      (20MB)  ← commit SHA                            │    │
│   │     └── :v1.0.0       (20MB)  ← version tag                           │    │
│   │                                                                        │    │
│   │  Stored permanently until you delete!                                  │    │
│   └────────────────────────────────────────────────────────────────────────┘    │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🔐 Permissions Required

```yaml
jobs:
  docker-build:
    permissions:
      contents: read    # Read repo files
      packages: write   # Push to GHCR! ← THIS IS KEY
```

---

## 🏷️ Common Tagging Strategies

```yaml
# Using docker/metadata-action
- uses: docker/metadata-action@v5
  with:
    images: ghcr.io/${{ github.repository }}/todo-backend
    tags: |
      type=sha,prefix=          # abc1234 (commit SHA)
      type=raw,value=latest     # latest
      type=semver,pattern=v{{version}}  # v1.0.0

# Results in:
# ghcr.io/pdheeraj99/todo-backend:ab12cd34
# ghcr.io/pdheeraj99/todo-backend:latest
# ghcr.io/pdheeraj99/todo-backend:v1.0.0
```

---

## 📊 GHCR vs Docker Hub

| Feature | GHCR | Docker Hub |
|---------|------|------------|
| **URL** | ghcr.io | docker.io |
| **Free tier** | Unlimited public | 1 private repo |
| **Rate limits** | Higher for GitHub users | 100 pulls/6hr (anonymous) |
| **Integration** | GitHub Actions native | Needs secrets setup |
| **Visibility** | Linked to repo/org | Separate account |

---

## 🎬 Telugu Summary

```
GHCR = GitHub Container Registry

Location:
─────────
github.com/<username> → Packages tab
OR
ghcr.io/<username>/<image>

Features:
─────────
• Docker images store cheyochu
• Public repos ki FREE
• GitHub Actions tho direct integration
• GITHUB_TOKEN automatic ga work avuthundi

Push Cheyadaniki:
────────────────
1. docker/login-action - Login to ghcr.io
2. docker/build-push-action - Build & Push
3. permissions: packages: write - Required!
```

---

**Next: [02-artifacts-explained.md](./02-artifacts-explained.md)** 👉
