# 🐳 05 - Build Cache vs Images: The Mystery Solved

## 1. The Mystery: "Base Image Ekkada Undhi?" 🕵️

Nuvvu `docker images` run chesthe, `node:20-alpine` kanipinchaledu - but caching work avuthundhi! Enduku?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   NEE QUESTION:                                                              │
│   ══════════════                                                             │
│                                                                              │
│   "Docker build chesthe node:20-alpine vadanu.                              │
│    Caching work avuthundhi - second build fast ga avuthundhi.               │
│    But docker images lo node:20-alpine kanipinchaledu!                      │
│    Ekkadiki poyindhi? Cache lo undha? How does this work?"                  │
│                                                                              │
│   ANSWER:                                                                    │
│   ════════                                                                   │
│   Docker has TWO SEPARATE storage areas!                                    │
│   1. Image Storage (docker images lo chustham)                              │
│   2. Build Cache (hidden, but caching kosam use avuthundhi)                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Two Storage Areas - Restaurant Analogy 🍽️

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   DOCKER = RESTAURANT!                                                       │
│   ════════════════════                                                       │
│                                                                              │
│   ┌───────────────────────────────┐    ┌───────────────────────────────┐    │
│   │     🍽️ DINING TABLE            │    │      🍳 KITCHEN                │    │
│   │     (Image Storage)           │    │      (Build Cache)            │    │
│   ├───────────────────────────────┤    ├───────────────────────────────┤    │
│   │                               │    │                               │    │
│   │ What shows here:              │    │ What shows here:              │    │
│   │ • docker images command       │    │ • docker buildx du command   │    │
│   │ • Docker Desktop GUI          │    │ • Hidden from normal view    │    │
│   │                               │    │                               │    │
│   │ Contains:                     │    │ Contains:                     │    │
│   │ • todo-frontend:v1 ✅          │    │ • node:20-alpine layers      │    │
│   │ • my-app:latest ✅             │    │ • nginx:alpine layers        │    │
│   │ • YOUR built images           │    │ • npm ci result (cached)     │    │
│   │                               │    │ • Multi-stage intermediate   │    │
│   │                               │    │                               │    │
│   │ Purpose:                      │    │ Purpose:                      │    │
│   │ RUN containers!               │    │ SPEED UP builds!             │    │
│   │                               │    │                               │    │
│   └───────────────────────────────┘    └───────────────────────────────┘    │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   Telugu Explanation:                                                        │
│   ────────────────────                                                       │
│   🍽️ DINING TABLE = Guests ki SERVE chese final dishes                      │
│                     (nee todo-frontend:v1 image)                            │
│                     Clean, presentable!                                      │
│                                                                              │
│   🍳 KITCHEN = Raw ingredients store                                        │
│                (node:20-alpine, nginx:alpine layers)                        │
│                Guests ki direct ga chupinchamu!                             │
│                But next cooking ki ready ga untayi!                         │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. When Does Image Show in docker images?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   METHOD 1: docker pull nginx:alpine                                        │
│   ════════════════════════════════════                                       │
│                                                                              │
│   > docker pull nginx:alpine                                                │
│                                                                              │
│   → Docker Hub nunchi download chesthundhi                                  │
│   → IMAGE STORAGE lo store chesthundhi                                      │
│   → VISIBLE in docker images ✅                                              │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   METHOD 2: docker build (multi-stage)                                      │
│   ═══════════════════════════════════                                        │
│                                                                              │
│   > docker build -t todo:v1 .                                               │
│                                                                              │
│   Dockerfile:                                                                │
│   FROM node:20-alpine AS builder   ← Downloaded, but BUILD CACHE lo!       │
│   ...                                                                        │
│   FROM nginx:alpine                ← Downloaded, but BUILD CACHE lo!       │
│   ...                                                                        │
│                                                                              │
│   → Base images internally download                                         │
│   → Layers BUILD CACHE lo store                                             │
│   → ONLY final image "todo:v1" in IMAGE STORAGE                            │
│   → Base images NOT visible in docker images! ❌                            │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   ENDUKU?                                                                    │
│   ────────                                                                   │
│   Multi-stage discards intermediate stages!                                 │
│   Docker says: "Nkiu final image maatrame kavali,                           │
│                 intermediate stuff kitchen lo pettesta,                     │
│                 dining table clean ga unchesta!"                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Proof: Check Build Cache

Nee build cache emi undo chudalante:

```bash
docker buildx du
```

### 4.1 Output Example

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   > docker buildx du                                                        │
│                                                                              │
│   ID              RECLAIMABLE     SIZE            DESCRIPTION                │
│   abc123          true            1.2GB           regular                    │
│   def456          true            800MB           git-checksum               │
│   ...                                                                        │
│                                                                              │
│   Reclaimable:    4.347GB   ← node:20-alpine, nginx:alpine IKKADE UNDHI!   │
│   Total:          4.347GB                                                   │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   Telugu:                                                                    │
│   ─────────                                                                  │
│   "Nee kitchen lo 4.3GB worth ingredients unnay!"                           │
│   "Base images, cached npm ci results - anni ikkada!"                       │
│   "docker images lo kanipinchavu, but ikkada safe ga unnay!"               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. How Caching Works - Step by Step

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   FIRST BUILD: docker build -t todo:v1 .                                    │
│   ═══════════════════════════════════════                                    │
│                                                                              │
│   Step 1: FROM node:20-alpine                                               │
│   Docker: "node:20-alpine kavali...                                         │
│            Docker Hub nunchi download chesta...                             │
│            BUILD CACHE lo petta (not as separate image)!"                   │
│                                                                              │
│   Step 2-6: Rest of commands...                                             │
│   Docker: "Anni layers BUILD CACHE lo store!"                               │
│                                                                              │
│   Final: Tagging                                                             │
│   Docker: "Final image 'todo:v1' ga tag chesta,                             │
│            idi IMAGE STORAGE lo petta!"                                     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   SECOND BUILD: docker build -t todo:v2 . (App.tsx changed)                 │
│   ═════════════════════════════════════════════════════════                  │
│                                                                              │
│   Step 1: FROM node:20-alpine                                               │
│   Docker: "node:20-alpine kavali...                                         │
│            Wait! BUILD CACHE lo already unnay ee layers!                    │
│            Download avasaram ledu - CACHE use chesta!" ⚡                   │
│                                                                              │
│   Result: INSTANT! No download!                                             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. BuildKit - Modern Docker Engine

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Docker latest versions use BUILDKIT (not legacy builder)                  │
│   ════════════════════════════════════════════════════════                   │
│                                                                              │
│   LEGACY BUILDER (Old):              BUILDKIT (New - default now):          │
│   ─────────────────────              ────────────────────────────            │
│   • Saves intermediate images        • Smart hidden cache                   │
│     as visible images                • Cleaner docker images list          │
│   • docker images cluttered          • Better caching algorithm            │
│   • Less efficient                   • Parallel execution! ⚡               │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   BuildKit keeps your "Dining Table" clean!                                 │
│   Only final dishes visible, raw ingredients hidden in kitchen!            │
│                                                                              │
│   Nee docker images list lo:                                                │
│   • todo-frontend:v1 ✅ (nee built image)                                   │
│   • node:20-alpine ❌ (kitchen lo hidden)                                   │
│   • nginx:alpine ❌ (kitchen lo hidden)                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. Make Base Image Visible (If You Want)

Neeku base image explicitly visible kavali ante:

```bash
# Explicitly download - then it shows in docker images
docker pull node:20-alpine

# Now check
docker images
# node:20-alpine will be visible!
```

---

## 8. Important Commands

| Command | Purpose (Telugu) |
|---------|-----------------|
| `docker images` | Final images list (Dining Table) |
| `docker buildx du` | Build cache size check (Kitchen inventory) |
| `docker pull node:20-alpine` | Explicitly download (Visible in images) |
| `docker system prune` | Clean unused data |
| `docker builder prune` | Clean build cache (Kitchen clean) |

---

## 🎯 Summary

| Question | Answer |
|----------|--------|
| node:20-alpine docker images lo enduku ledu? | Build cache lo undhi, separate image ga save avvadhu |
| Cache ekkada undhi? | `docker buildx du` lo chudu |
| Caching work avuthundha? | YES! Layers cached avuthayi |
| Visible cheyalante? | `docker pull node:20-alpine` explicitly run cheyyi |
| Why this design? | Keep docker images list clean! |

---

## 👉 Next: [06-wsl2-architecture.md](./06-wsl2-architecture.md) - Windows lo Docker ela work avuthundhi?
