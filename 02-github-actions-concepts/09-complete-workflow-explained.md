# 📝 09 - Complete Workflow Explained

## Our Workflow - Line by Line

```yaml
# 1️⃣ WORKFLOW NAME (just display name in UI)
name: 🐳 React Docker Build

# 2️⃣ WHEN TO RUN
on:
  push:
    branches: [main]           # Only main branch
    paths:                      # Only if these files change
      - '03-react-pipeline/todo-frontend/**'
  workflow_dispatch:           # Manual run button

# 3️⃣ GLOBAL VARIABLES
env:
  REGISTRY: ghcr.io           # GitHub Container Registry
  IMAGE_NAME: ${{ github.repository }}/todo-frontend

# 4️⃣ THE WORK!
jobs:
  build-and-push:
    runs-on: ubuntu-latest    # Use Linux VM
    permissions:
      contents: read          # Can read code
      packages: write         # Can push images

    steps:
      # STEP 1: Clone repo to runner
      - name: 📥 Checkout
        uses: actions/checkout@v4

      # STEP 2: Login to GHCR
      - name: 🔐 Login
        uses: docker/login-action@v3
        with:
          registry: ${{ env.REGISTRY }}
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      # STEP 3: Setup advanced Docker builder
      - name: 🔧 Setup Buildx
        uses: docker/setup-buildx-action@v3

      # STEP 4: Generate image tags
      - name: 📋 Metadata
        id: meta
        uses: docker/metadata-action@v5
        with:
          images: ${{ env.REGISTRY }}/${{ env.IMAGE_NAME }}
          tags: |
            type=sha,prefix=     # Git commit SHA
            type=raw,value=latest

      # STEP 5: THE MAIN EVENT! Build & Push!
      - name: 🐳 Build and Push
        uses: docker/build-push-action@v5
        with:
          context: ./03-react-pipeline/todo-frontend
          push: true
          tags: ${{ steps.meta.outputs.tags }}
          cache-from: type=gha   # Use GitHub cache
          cache-to: type=gha     # Save to GitHub cache
```

---

## 🔄 Flow Visualization

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   git push                                                                   │
│      │                                                                       │
│      ▼                                                                       │
│   GitHub: "Push to main? Path matches? → YES! Run workflow!"                │
│      │                                                                       │
│      ▼                                                                       │
│   Spin up Ubuntu VM (runner)                                                │
│      │                                                                       │
│      ▼                                                                       │
│   Step 1: Checkout → Clone your code                                        │
│      │                                                                       │
│      ▼                                                                       │
│   Step 2: Login → docker login ghcr.io                                      │
│      │                                                                       │
│      ▼                                                                       │
│   Step 3: Setup Buildx → Advanced Docker builder                            │
│      │                                                                       │
│      ▼                                                                       │
│   Step 4: Metadata → Generate tags (sha + latest)                           │
│      │                                                                       │
│      ▼                                                                       │
│   Step 5: Build & Push → docker build + docker push to GHCR!               │
│      │                                                                       │
│      ▼                                                                       │
│   ✅ Image available at: ghcr.io/pdheeraj99/.../todo-frontend:latest       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Point

This workflow: Push code → Auto build Docker → Auto push to GHCR! Magic! ✨
