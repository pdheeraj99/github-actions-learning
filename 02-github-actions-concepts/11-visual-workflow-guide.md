# 🎬 GitHub Actions Workflow - Visual Step-by-Step Guide

## 🚀 INITIAL STATE: Before git push

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   YOUR LOCAL PC (Windows)                                                    │
│   ════════════════════════                                                   │
│                                                                              │
│   D:\Antigravity_Projects\Github_Actions\                                   │
│   ├── .github/                                                               │
│   │   └── workflows/                                                         │
│   │       └── docker-build.yml    ← Workflow file!                          │
│   ├── 01-concepts/                ← Documentation                           │
│   ├── 02-github-actions-concepts/ ← More docs                               │
│   └── 03-react-pipeline/                                                    │
│       └── todo-frontend/                                                    │
│           ├── Dockerfile           ← Docker instructions                    │
│           ├── nginx.conf           ← Nginx config                           │
│           ├── package.json         ← Dependencies                           │
│           └── src/                 ← React source code                      │
│               ├── App.tsx                                                   │
│               └── ...                                                       │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GITHUB CLOUD                                                               │
│   ════════════                                                               │
│                                                                              │
│   GitHub Runners: 💤 Sleeping (no work yet)                                 │
│   GHCR: Empty (no images)                                                   │
│   GitHub Cache: Empty                                                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

Nee PC lo code undhi - `.github/workflows/` folder lo workflow file undhi. Ee file GitHub ki chepthundhi "code push ayyinappudu enti cheyalo". Ippudu Github cloud lo emi ledu - runners sleeping, registry empty, cache empty. Git push chesthe magic start avuthundhi!

**Key Point:** Workflow file nee repo lo undhi, but it runs on GitHub's servers - not on your PC!

---

## ⚡ TRIGGER: git push

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   YOUR PC                              GITHUB                                │
│   ════════                             ══════                                │
│                                                                              │
│   ┌─────────────────┐                 ┌─────────────────────────────────┐   │
│   │                 │                 │                                 │   │
│   │  git add .      │                 │  GitHub receives push!          │   │
│   │  git commit -m  │ ═══════════════▶│                                 │   │
│   │  git push       │   Push event!   │  Checks:                        │   │
│   │                 │                 │  ✅ Branch = main?              │   │
│   │  (DONE!)        │                 │  ✅ Path matches?               │   │
│   │                 │                 │  ✅ Workflow exists?            │   │
│   └─────────────────┘                 │                                 │   │
│                                       │  → ALL YES! TRIGGER WORKFLOW!  │   │
│                                       │                                 │   │
│                                       └─────────────────────────────────┘   │
│                                                                              │
│   Telugu: "Nee push GitHub ki reach ayyindhi.                               │
│            GitHub check chesindhi - conditions match!                        │
│            Workflow start avuthundhi!"                                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🖥️ JOB START: Spin Up Runner

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GitHub Cloud Data Center (US/Europe/Asia)                                 │
│   ═════════════════════════════════════════                                  │
│                                                                              │
│   runs-on: ubuntu-latest                                                    │
│                                                                              │
│   GitHub: "Let me spin up a fresh Ubuntu VM!"                               │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                     FRESH UBUNTU VM (Runner)                        │   │
│   │                                                                     │   │
│   │   OS: Ubuntu 22.04                                                  │   │
│   │   CPU: 2 cores                                                      │   │
│   │   RAM: 7 GB                                                         │   │
│   │   Disk: 14 GB                                                       │   │
│   │                                                                     │   │
│   │   Pre-installed:                                                    │   │
│   │   ✅ Git                                                            │   │
│   │   ✅ Docker                                                         │   │
│   │   ✅ Node.js                                                        │   │
│   │   ✅ npm                                                            │   │
│   │   ✅ Common tools                                                   │   │
│   │                                                                     │   │
│   │   Current filesystem:                                               │   │
│   │   /home/runner/                                                     │   │
│   │   └── work/                                                         │   │
│   │       └── github-actions-learning/                                 │   │
│   │           └── (EMPTY! No code yet!)                                │   │
│   │                                                                     │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   Telugu: "GitHub oka fresh computer start chesindhi.                       │
│            Akkada Docker, Git anni ready unnay.                              │
│            But nee code inka ledu - checkout cheyali!"                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

GitHub oka **brand new computer** (VM = Virtual Machine) start chesindhi. Idi nee PC kaadu - GitHub cloud lo undi. Ee computer lo already Docker, Git, Node.js install ayi unnay - nuvvu install cheyakkarledu!

**\"runs-on: ubuntu-latest\" ante:**

- *\"ubuntu\"* = Linux operating system (Windows kaadu!)
- *\"latest\"* = most recent stable version

**Endhuku Ubuntu?**

- Docker Linux lo native ga run avuthundhi
- Windows lo Docker ki WSL2 kavali, but Linux lo direct run avuthundhi
- So faster builds!

**Important:** Ee VM **completely empty** - nee code inka akkada ledu! Anduke Step 1 lo checkout chesthunnam.

---

## 📥 STEP 1: Checkout Code

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

### BEFORE Checkout

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GitHub Runner VM:                                                          │
│   ══════════════════                                                         │
│                                                                              │
│   /home/runner/work/github-actions-learning/github-actions-learning/       │
│   └── (COMPLETELY EMPTY!)                                                   │
│                                                                              │
│   Docker: "Dockerfile ekkada? Build cheyyalenu!" 😭                         │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: git clone (internally)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   actions/checkout@v4 internally runs:                                      │
│   ═══════════════════════════════════════                                    │
│                                                                              │
│   git clone https://github.com/pdheeraj99/github-actions-learning.git      │
│   git checkout 8b9f323... (specific commit that triggered workflow)        │
│                                                                              │
│   GITHUB                           RUNNER                                    │
│   ══════                           ══════                                    │
│   ┌──────────────┐                ┌──────────────────────────────┐          │
│   │              │                │                              │          │
│   │  Repository  │ ══════════════▶│  Download all files!        │          │
│   │  (your code) │   git clone    │                              │          │
│   │              │                │                              │          │
│   └──────────────┘                └──────────────────────────────┘          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Checkout

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GitHub Runner VM:                                                          │
│   ══════════════════                                                         │
│                                                                              │
│   /home/runner/work/github-actions-learning/github-actions-learning/       │
│   ├── .github/                                                               │
│   │   └── workflows/                                                         │
│   │       └── docker-build.yml  ✅                                          │
│   ├── 01-concepts/              ✅                                          │
│   │   ├── 01-what-is-docker.md                                              │
│   │   └── ...                                                               │
│   ├── 02-github-actions-concepts/ ✅                                        │
│   └── 03-react-pipeline/        ✅                                          │
│       └── todo-frontend/        ✅ ← IMPORTANT!                             │
│           ├── Dockerfile        ✅ ← Docker can now find this!             │
│           ├── nginx.conf        ✅                                          │
│           ├── package.json      ✅                                          │
│           └── src/              ✅                                          │
│               ├── App.tsx                                                   │
│               └── ...                                                       │
│                                                                              │
│   Telugu: "Nee code antha runner ki download ayyindhi!                      │
│            Ippudu Dockerfile undhi - Docker build cheyachu!"                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

**Checkout ante enti?**

- "Checkout" ante library book teeskovadam laaga - GitHub nunchi nee code teskuntunnam
- Internally `git clone` run avuthundhi
- Nee repo lo unna ALL files VM ki download avtay

**Enduku first step lo checkout chesthunnam?**

- Runner VM fresh ga start ayyindhi - emi ledu akkada!
- Docker build cheyali ante Dockerfile kavali
- Dockerfile nee repo lo undi - so poyyi teskoraadu!

**"uses: actions/checkout@v4" ante:**

- *"uses"* = oka pre-built action use chesthunnam
- *"actions/checkout"* = GitHub official checkout action
- *"@v4"* = version 4

**After this step:** Runner lo nee code antha undi - Dockerfile, package.json, src folder, everything!

---

## 🔐 STEP 2: Login to GHCR

```yaml
- name: 🔐 Login to GHCR
  uses: docker/login-action@v3
  with:
    registry: ghcr.io
    username: pdheeraj99
    password: ${{ secrets.GITHUB_TOKEN }}
```

### BEFORE Login

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Runner VM's Docker:                                                        │
│   ═══════════════════                                                        │
│                                                                              │
│   $ docker push ghcr.io/pdheeraj99/...                                      │
│   ❌ ERROR: unauthorized: authentication required!                          │
│                                                                              │
│   GHCR: "You kaun ho? Login cheyyi first!"                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: docker login

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GitHub: "Here's a fresh GITHUB_TOKEN for this run!"                       │
│           Token: ghp_xxxxxxxxxxxxxxxxxxxx                                   │
│           Permissions: packages:write ✅                                    │
│                                                                              │
│   docker/login-action runs:                                                 │
│   ══════════════════════════                                                 │
│                                                                              │
│   $ docker login ghcr.io -u pdheeraj99 -p ghp_xxxxxxxxxxxxxxxxxxxx          │
│                                                                              │
│   Output: Login Succeeded! ✅                                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Login

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Runner VM's Docker:                                                        │
│   ═══════════════════                                                        │
│                                                                              │
│   ~/.docker/config.json:                                                    │
│   {                                                                          │
│     "auths": {                                                              │
│       "ghcr.io": {                                                          │
│         "auth": "cGRoZWVyYWo5OTpnaHBfeHh4eHh4..."  ← Stored!              │
│       }                                                                      │
│     }                                                                        │
│   }                                                                          │
│                                                                              │
│   Docker: "Now I can push to ghcr.io!" ✅                                   │
│                                                                              │
│   Telugu: "Docker ki GHCR access mil gaya!                                  │
│            Ab image push kar sakte hain!"                                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

**GHCR ante enti?**

- GHCR = **G**it**H**ub **C**ontainer **R**egistry
- Docker images store cheyyataniki oka place - like Docker Hub, but GitHub vaalla
- Nee GitHub account tho connected

**Login enduku kavali?**

- GHCR oka private locker laanti thing
- Image push cheyali ante permission kavali
- Login lekunda push chesthe: "Access Denied!" error vasthundhi

**secrets.GITHUB_TOKEN ante enti?**

- GitHub **automatically** provide chesina password
- Nuvvu emi setup cheyakkarledu!
- Every workflow run ki fresh token vasthundhi
- Workflow end lo token expire avuthundhi (security!)

**After this step:** Docker ki GHCR push cheyyadaniki permission vachindhi!

---

## 🔧 STEP 3: Setup Docker Buildx

```yaml
- name: 🔧 Setup Docker Buildx
  uses: docker/setup-buildx-action@v3
```

### BEFORE Buildx

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Runner VM's Docker:                                                        │
│   ═══════════════════                                                        │
│                                                                              │
│   Available builders:                                                        │
│   • default (basic docker build)                                            │
│                                                                              │
│   Limitations:                                                               │
│   ❌ No cloud caching (type=gha)                                            │
│   ❌ No multi-platform builds                                               │
│   ❌ Basic features only                                                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Create Buildx builder

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   docker/setup-buildx-action runs:                                          │
│   ═══════════════════════════════════                                        │
│                                                                              │
│   $ docker buildx create --name builder --driver docker-container --use     │
│                                                                              │
│   Creates advanced builder with:                                            │
│   ✅ GitHub Actions cache support (type=gha)                                │
│   ✅ Multi-platform support                                                 │
│   ✅ Parallel layer builds                                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Buildx

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Runner VM's Docker:                                                        │
│   ═══════════════════                                                        │
│                                                                              │
│   Available builders:                                                        │
│   • default                                                                  │
│   • builder * (ACTIVE - Buildx!)  ← Using this now!                        │
│                                                                              │
│   Capabilities:                                                              │
│   ✅ Cloud caching (type=gha)                                               │
│   ✅ Multi-platform (linux/amd64, linux/arm64)                              │
│   ✅ Advanced features                                                      │
│                                                                              │
│   Telugu: "Upgraded Docker builder ready!                                   │
│            Ippudu GitHub cache use cheyachu!"                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

**Buildx ante enti? Normal docker build tho problem enti?**

Normal `docker build` oka **basic car** laaga:

- Image build avuthundhi ✅
- But cloud caching support ledu ❌

Buildx oka **luxury car** laaga:

- Same image build avuthundhi ✅
- PLUS cloud caching (type=gha) ✅
- PLUS multi-platform builds ✅

**"type=gha" ante enti?**

- GHA = **G**it**H**ub **A**ctions
- Meaning: Cache ni GitHub cloud lo store cheyyi
- Next run lo aa cache use avuthundi - FAST builds!

**"Multi-platform" ante enti?**

- Same image lo different computers run avthundhi:
  - Intel/AMD laptops (linux/amd64)
  - Mac M1/M2 chips (linux/arm64)
  - Raspberry Pi (linux/arm/v7)
- Manaki ippudu avasaram ledu, but production lo useful!

**"Parallel layer builds" ante enti?**

- Dockerfile lo 10 steps unte, konni steps parallel ga run avtay
- Faster build times!

**After this step:** Docker Buildx ready - ippudu GitHub cache tho fast builds cheyachu!

---

## 📋 STEP 4: Extract Metadata (Generate Tags)

```yaml
- name: 📋 Extract Metadata
  id: meta
  uses: docker/metadata-action@v5
  with:
    images: ghcr.io/pdheeraj99/github-actions-learning/todo-frontend
    tags: |
      type=sha,prefix=
      type=raw,value=latest
```

### BEFORE Metadata

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Question: "What tag should the image have?"                               │
│                                                                              │
│   Options:                                                                   │
│   • :latest                                                                  │
│   • :v1.0.0                                                                  │
│   • :8b9f323 (commit SHA)                                                   │
│   • :pr-42                                                                   │
│                                                                              │
│   We don't know yet! Need to generate!                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### ACTION: Generate tags automatically

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   docker/metadata-action analyzes:                                          │
│   ══════════════════════════════════                                         │
│                                                                              │
│   Current Context:                                                           │
│   • Commit SHA: 8b9f323abc...                                               │
│   • Branch: main                                                            │
│   • Event: push                                                             │
│                                                                              │
│   Tag Rules:                                                                 │
│   • type=sha,prefix=    → Take commit SHA, no prefix                       │
│   • type=raw,value=latest → Add "latest" tag                               │
│                                                                              │
│   OUTPUT GENERATED:                                                          │
│   ══════════════════                                                         │
│   ghcr.io/pdheeraj99/github-actions-learning/todo-frontend:8b9f323         │
│   ghcr.io/pdheeraj99/github-actions-learning/todo-frontend:latest          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### AFTER Metadata

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Step Output (id: meta):                                                   │
│   ═══════════════════════                                                    │
│                                                                              │
│   steps.meta.outputs.tags =                                                 │
│     ghcr.io/pdheeraj99/github-actions-learning/todo-frontend:8b9f323       │
│     ghcr.io/pdheeraj99/github-actions-learning/todo-frontend:latest        │
│                                                                              │
│   Next step can use: ${{ steps.meta.outputs.tags }}                         │
│                                                                              │
│   Telugu: "Image ki 2 tags generate ayyay:                                  │
│            1. Commit SHA tag (unique identifier)                            │
│            2. latest tag (for easy pulling)"                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham (Explanation)

**Telugu lo simple ga cheppali ante:**

**Tag ante enti?**

- Tag = Image ki name/version
- Like: `my-app:v1.0.0` or `my-app:latest`
- Same image ki multiple tags undachu

**Endhuku 2 tags?**

1. **SHA tag (`:8b9f323`):**
   - SHA = Commit ID (unique identifier)
   - Ee tag change avvadu - always same version point chesthundhi
   - Debugging ki useful: "Ee exact code tho build ayyindhi!"

2. **Latest tag (`:latest`):**
   - "Latest" ante most recent
   - Easy to remember: `docker pull my-app:latest`
   - But careful - idi maripothe different version ochestundhi!

**"id: meta" ante enti?**

- Ee step ki oka ID ichham: "meta"
- Next step lo ee step output use cheyachu: `${{ steps.meta.outputs.tags }}`
- Steps madhya data pass cheyyadaniki useful!

**After this step:** Tags ready - ghcr.io/username/app:8b9f323 and ghcr.io/username/app:latest

---

## 🐳 STEP 5: Build and Push (THE MAIN EVENT!)

```yaml
- name: 🐳 Build and Push
  uses: docker/build-push-action@v5
  with:
    context: ./03-react-pipeline/todo-frontend
    push: true
    tags: ${{ steps.meta.outputs.tags }}
    cache-from: type=gha
    cache-to: type=gha,mode=max
```

### 5A: Check GitHub Cache

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   cache-from: type=gha                                                       │
│   ═══════════════════════                                                    │
│                                                                              │
│   FIRST RUN:                                                                 │
│   ──────────                                                                 │
│   Action: "GitHub, do you have cache for this build?"                       │
│   GitHub: "Nope, no cache found!" 😕                                        │
│   Action: "OK, I'll build from scratch!"                                    │
│                                                                              │
│   SUBSEQUENT RUNS:                                                           │
│   ────────────────                                                           │
│   Action: "GitHub, do you have cache for this build?"                       │
│   GitHub: "Yes! Here's cached layers!" 🎉                                   │
│   Action: "Great! Skipping npm ci step - already cached!"                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5B: Docker Build Process

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Building from Dockerfile:                                                  │
│   ══════════════════════════                                                 │
│                                                                              │
│   Stage 1: BUILD (node:20-alpine)                                           │
│   ─────────────────────────────────                                          │
│                                                                              │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ FROM node:20-alpine                                               │     │
│   │ ► Pulling node:20-alpine from Docker Hub...                       │     │
│   │   (Docker Hub CDN - fast worldwide!)                              │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ WORKDIR /app                                                      │     │
│   │ ► Created /app directory                                          │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ COPY package*.json ./                                             │     │
│   │ ► Copying package.json and package-lock.json                      │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ RUN npm ci                                                        │     │
│   │ ► Installing dependencies... (2-3 minutes if no cache!)          │     │
│   │   OR: Cache hit! Using cached npm ci result! ⚡ (30 seconds!)    │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ COPY . .                                                          │     │
│   │ ► Copying all source code                                         │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ RUN npm run build                                                 │     │
│   │ ► Compiling React → /app/dist folder created!                    │     │
│   │   Contains: index.html, bundle.js, assets/                       │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   Stage 2: PRODUCTION (nginx:alpine)                                        │
│   ─────────────────────────────────────                                      │
│                                                                              │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ FROM nginx:alpine                                                 │     │
│   │ ► Fresh, tiny nginx image (just web server!)                     │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ COPY --from=build /app/dist /usr/share/nginx/html                │     │
│   │ ► Copy ONLY the compiled files from Stage 1!                     │     │
│   │   node_modules NOT copied! (Saves 500MB+!)                       │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ COPY nginx.conf /etc/nginx/conf.d/default.conf                   │     │
│   │ ► Custom nginx configuration for React SPA                       │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                          │                                                   │
│                          ▼                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ BUILD COMPLETE! ✅                                                │     │
│   │                                                                   │     │
│   │ Final Image Size: ~25MB (tiny!)                                  │     │
│   │ Contains: nginx + compiled React files ONLY!                     │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Build Process (Explanation)

**Telugu lo simple ga cheppali ante:**

**Multi-stage build ante enti?**

Manaki final image SIZE chala important! Small image = fast download, less storage.

**Stage 1 (BUILD stage):**

- Node.js environment use chesthunnam (500MB+)
- npm ci run chesthunnam (dependencies install)
- npm run build run chesthunnam (React compile to HTML/JS)
- Result: `/app/dist` folder lo compiled files

**Stage 2 (PRODUCTION stage):**

- Fresh nginx image (25MB only!)
- Stage 1 nunchi ONLY `/app/dist` copy chesthunnam
- node_modules, source code KOPAM! Final image lo ledu!

**Enduku 2 stages?**

- Stage 1: Big, development tools anni unnay
- Stage 2: Small, production ki kavalsindhe undi
- Final image: nginx + compiled files = ~25MB only!

**Multi-stage lekunda?**

- One stage: node + npm + source + node_modules = 500MB+ image!
- Multi-stage: nginx + compiled files = 25MB image!
- 20x smaller! 🎉

### 5C: Docker Push to GHCR

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   push: true                                                                 │
│   ══════════                                                                 │
│                                                                              │
│   Runner VM                        GHCR (GitHub Container Registry)         │
│   ══════════                       ═════════════════════════════════         │
│                                                                              │
│   ┌────────────────┐              ┌───────────────────────────────────┐     │
│   │                │              │                                   │     │
│   │ Built Image    │              │  ghcr.io/pdheeraj99/              │     │
│   │ (25MB)         │═════════════▶│  github-actions-learning/         │     │
│   │                │  docker push │  todo-frontend                    │     │
│   │                │              │                                   │     │
│   │ Tags:          │              │  Tags:                            │     │
│   │ :8b9f323       │              │  :8b9f323 ✅                      │     │
│   │ :latest        │              │  :latest ✅                       │     │
│   │                │              │                                   │     │
│   └────────────────┘              └───────────────────────────────────┘     │
│                                                                              │
│   Telugu: "Built image GHCR ki push ayyindhi!                               │
│            Eppudaina download cheyachu!"                                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 5D: Save Cache for Next Run

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   cache-to: type=gha,mode=max                                               │
│   ═══════════════════════════════                                            │
│                                                                              │
│   Saving to GitHub Cache:                                                   │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │                                                                   │     │
│   │   Layer 1: node:20-alpine base          [CACHED] ✅              │     │
│   │   Layer 2: npm ci result (node_modules) [CACHED] ✅              │     │
│   │   Layer 3: npm run build result         [CACHED] ✅              │     │
│   │   Layer 4: nginx base                   [CACHED] ✅              │     │
│   │                                                                   │     │
│   │   Total cache size: ~400MB                                       │     │
│   │   Saved to: GitHub Actions Cache (your repo only!)              │     │
│   │                                                                   │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                                                                              │
│   Next run benefit:                                                         │
│   • First run: 5 minutes                                                    │
│   • Cached run: 1 minute! 🚀                                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Caching (Explanation)

**Telugu lo simple ga cheppali ante:**

**Cache enduku important?**

Problem: Every workflow run ki FRESH VM start avuthundhi. Old VM destroy ayyindhi - dani lo unna npm install result poyindi!

Without cache:

- Run 1: npm ci (3 min) → VM destroyed
- Run 2: npm ci AGAIN (3 min) → Same work repeat!

With cache (type=gha):

- Run 1: npm ci (3 min) → Result saved to GitHub Cloud
- Run 2: Download cache (30 sec) → Skip npm ci!

**\"mode=max\" ante enti?**

- `mode=max` = Maximum caching
- All layers cache avutay - intermediate steps kuda
- More cache = faster builds!

**Cache ekkada store avuthundhi?**

- GitHub Cloud lo (nee repo connected)
- 10GB limit per repo
- 7 days expiry (used lekunda unte delete)
- Private - only nee repo access cheyagaladu

**After this step:** Cache stored - next build super fast avuthundhi! 🚀

---

## ✅ FINAL STATE: After Workflow Complete

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GITHUB RUNNER VM:                                                          │
│   ══════════════════                                                         │
│   💀 DESTROYED! (Deleted after job ends)                                    │
│   No traces left! Security! 🔒                                              │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GHCR (GitHub Container Registry):                                         │
│   ══════════════════════════════════                                         │
│                                                                              │
│   ghcr.io/pdheeraj99/github-actions-learning/todo-frontend                 │
│   ├── :8b9f323    ← Specific version (commit-based)                        │
│   └── :latest     ← Always points to newest                                │
│                                                                              │
│   Anyone can pull:                                                          │
│   docker pull ghcr.io/pdheeraj99/github-actions-learning/todo-frontend     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   GITHUB ACTIONS CACHE:                                                      │
│   ═══════════════════════                                                    │
│                                                                              │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │ Repo: pdheeraj99/github-actions-learning                         │     │
│   │ Cache: ~400MB of Docker layers                                   │     │
│   │ Expires: 7 days (or when limit reached)                         │     │
│   │ Re-used by: Next workflow runs! ⚡                               │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   YOUR PC:                                                                   │
│   ═════════                                                                  │
│   (Nothing changed! You just pushed code!)                                  │
│                                                                              │
│   Now you can run locally:                                                  │
│   docker pull ghcr.io/pdheeraj99/.../todo-frontend:latest                  │
│   docker run -p 80:80 ghcr.io/pdheeraj99/.../todo-frontend:latest          │
│                                                                              │
│   Telugu: "Workflow complete!                                               │
│            Image GHCR lo ready!                                              │
│            Cache saved for next time!                                        │
│            Runner destroyed - clean slate!"                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 📝 Ardham - Final State (Explanation)

**Telugu lo simple ga cheppali ante:**

**Runner VM enduku destroy avuthundhi?**

- Security! Previous build secrets, code, everything delete avthundhi
- Each run fresh start - no old data interference
- GitHub 1000s of workflows run chesthundhi - VMs reuse kaadu, destroy and create fresh

**GHCR lo enti undi ippudu?**

- Nee built Docker image undi!
- 2 tags: `:8b9f323` (commit SHA) AND `:latest`
- Evaraina download cheyachu: `docker pull ghcr.io/...`
- Kubernetes, cloud servers - all can use this image!

**NEE PC lo enti ayyindhi?**

- NOTHING! 🎉
- Nuvvu just `git push` chesav
- Baaki antha GitHub chesindhi
- NEE PC resources waste avvaledu

**Summary of full flow:**

1. Nuvvu: `git push` → Done!
2. GitHub: VM start → Code download → Build → Push → VM destroy
3. Result: Image ready to use worldwide!

**This is CI/CD magic!** Push cheste anni automatic! 🚀

---

## 📊 Complete Timeline

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   TIME     EVENT                           DURATION                          │
│   ════     ═════                           ════════                          │
│                                                                              │
│   00:00    git push                        instant                          │
│   00:01    GitHub detects push             ~1 second                        │
│   00:02    Spin up runner VM               ~20 seconds                      │
│   00:22    Step 1: Checkout                ~5 seconds                       │
│   00:27    Step 2: Login GHCR              ~2 seconds                       │
│   00:29    Step 3: Setup Buildx            ~5 seconds                       │
│   00:34    Step 4: Extract Metadata        ~2 seconds                       │
│   00:36    Step 5: Build & Push            ~3-5 minutes (first time)       │
│                                            ~30-60 sec (cached!)             │
│   05:00    Workflow Complete!              ✅                               │
│   05:01    Runner VM destroyed             💀                               │
│                                                                              │
│   TOTAL: ~5 minutes first time, ~2 minutes with cache!                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Takeaways

1. **Checkout** = Clone code to VM (VM starts empty!)
2. **Login** = Get permission to push to GHCR
3. **Buildx** = Enable advanced Docker features + caching
4. **Metadata** = Generate smart tags automatically
5. **Build+Push** = THE actual work - build image, push to registry
6. **Cache** = Saves time on subsequent runs (5 min → 1 min!)
7. **Runner destroyed** = Clean, secure, no traces!
