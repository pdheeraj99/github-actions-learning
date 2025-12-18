# 🐳 10 - Docker Interview Questions (40LPA+ Level)

## Quick Reference - Interviews lo Mouth nundi Vasthundhi Style

---

## 1. Basic Concepts

### Q: Docker ante enti? (What is Docker?)

```
A: "Docker is a containerization platform that packages applications 
    with all their dependencies into portable containers.
    
    Analogy: Shipping container lanti dhi - ekkadiki ayina transport 
    cheyachu, content same ga untundhi!"
```

### Q: Image vs Container difference?

```
A: "Image is a template (like a Class in Java) - read-only, stored on disk.
    Container is running instance (like an Object) - live, in memory.
    
    One image → Many containers create cheyachu!
    Like: One biryani recipe → Many biryani plates!"
```

### Q: Dockerfile ante enti?

```
A: "Dockerfile is a text file with instructions to build Docker image.
    Each instruction creates a layer in the image.
    
    Like: Recipe book - step by step cooking instructions!"
```

---

## 2. Multi-Stage Builds

### Q: Multi-stage build explain cheyyi? Enduku use chestharu?

```
A: "Multi-stage uses multiple FROM statements in Dockerfile.
    Stage 1: Build environment (Node.js, npm, compile tools)
    Stage 2: Runtime environment (just Nginx + built files)
    
    Benefits:
    • 95% smaller image size (700MB → 30MB!)
    • No source code in production (security!)
    • No dev dependencies in final image
    • Smaller attack surface"
```

### Q: How does multi-stage reduce image size?

```
A: "Stage 1 (builder) has Node.js, npm, node_modules - heavy stuff!
    But we ONLY copy the /dist folder to Stage 2.
    Stage 1 is thrown away - not in final image!
    
    Final image: Nginx (25MB) + dist files (2MB) = ~30MB only!
    Instead of: Node.js + node_modules + source = 700MB+!"
```

---

## 3. Layer Caching

### Q: Docker build time optimize ela chestharu?

```
A: "We order Dockerfile commands by CHANGE FREQUENCY!
    
    Top (First):
    • Least frequently changing (base image, dependencies)
    
    Bottom (Last):
    • Most frequently changing (source code)
    
    Example:
    COPY package.json first → npm ci → COPY source code
    
    This way, when only source code changes, npm ci is CACHED!
    Saves 2-3 minutes per build!"
```

### Q: Why copy package.json before source code?

```
A: "Docker caching golden rule: If a layer changes, ALL subsequent 
    layers MUST rebuild!
    
    If we COPY . . first (with source) and change App.tsx:
    → COPY layer changes → npm ci FORCED to re-run! (2-3 mins wasted!)
    
    If we COPY package.json first, then npm ci, then source:
    → Source changes → But package.json SAME → npm ci CACHED! ⚡
    
    Daily savings: 50 builds × 2.5 min = 2+ hours saved!"
```

---

## 4. npm ci vs npm install

### Q: CI/CD lo npm ci enduku use chestharu, npm install kaadu?

```
A: "npm ci = Clean Install (NOT Continuous Integration!)
    
    npm ci:
    • Uses EXACT versions from package-lock.json
    • Every build gets IDENTICAL dependencies
    • Faster (doesn't resolve versions)
    
    npm install:
    • May get different versions (^18.0.0 could be 18.1.0 or 18.2.0)
    • 'Works on my machine' problems!
    
    For reproducible production builds, npm ci is MUST!"
```

---

## 5. Alpine Linux

### Q: Alpine enduku use chestharu?

```
A: "Alpine is tiny Linux - only 5MB base vs 70MB for Ubuntu!
    
    Benefits:
    • 82% smaller images
    • Faster CI/CD pipelines
    • Smaller attack surface (less vulnerabilities)
    
    node:20-alpine = 180MB
    node:20 (Debian) = 1GB!"
```

### Q: When would you NOT use Alpine?

```
A: "When using npm packages with C++ native code like:
    • bcrypt (password hashing)
    • sharp (image processing)
    
    These expect glibc, but Alpine uses musl libc.
    In such cases, use node:20-slim (Debian-based, smaller, glibc)."
```

---

## 6. Architecture Questions

### Q: Windows lo Docker ela work avuthundhi?

```
A: "Docker Desktop uses WSL2 - Windows Subsystem for Linux 2.
    
    WSL2 provides REAL Linux kernel inside Windows.
    Docker Engine runs inside WSL2.
    All containers share this Linux kernel.
    
    That's why it 'just works' - actual Linux kernel undhi!"
```

### Q: Docker images enduku chinna? (VM 5GB, Container 200MB?)

```
A: "Containers share host OS kernel - no separate kernel per container!
    VMs need their own kernel - 100MB+ each!
    
    Also, containers don't include:
    • GUI (no monitor needed for servers!)
    • Drivers (host provides)
    • Unnecessary tools
    
    Only what's NEEDED - minimal approach!"
```

---

## 7. Build Cache Question

### Q: docker images lo base images kanipinchavu - cache lo unnaya?

```
A: "Yes! Docker BuildKit stores base images in BUILD CACHE, 
    not as separate visible images.
    
    Multi-stage discards intermediate stages too.
    Only FINAL image shows in docker images.
    
    To check cache: docker buildx du
    (Shows all cached layers including base images!)"
```

---

## 8. Security Questions

### Q: Docker images secure ela chestharu?

```
A: "Multiple approaches:
    
    1. Multi-stage builds (no source code exposure)
    2. Minimal base images (Alpine/slim - less vulnerabilities)
    3. Run as non-root user (USER directive)
    4. Scan images for CVEs (Trivy, Snyk)
    5. Never hardcode secrets (use env vars, secrets management)
    6. Keep base images updated (security patches!)"
```

---

## 9. Scenario-Based Questions

### Q: "Build time 5 minutes - ela reduce chesthav?"

```
A: "Four approaches:
    
    1. Optimize layer caching:
       COPY package.json → npm ci → COPY source
    
    2. Use .dockerignore:
       Exclude node_modules, .git, test files
    
    3. Multi-stage build:
       Smaller build context in final stage
    
    4. Use BuildKit cache mounts:
       --mount=type=cache for npm cache"
```

### Q: "Production image 1GB - ela reduce chesthav?"

```
A: "Step by step:
    
    1. Multi-stage build:
       Build stage → copy only artifacts → runtime stage
    
    2. Alpine base image:
       node:20-alpine instead of node:20
    
    3. Only COPY dist/ to final stage:
       No node_modules, no source code
    
    4. npm ci with --production:
       Skip devDependencies if Node.js runtime needed
    
    Result: 1GB → 30-50MB easily!"
```

---

## 🎯 Quick Answer Cheatsheet

| Topic | Key Points to Mention |
|-------|----------------------|
| Multi-stage | Separate build & run, final stage only, 95% smaller |
| Layer cache | package.json first, change frequency order |
| npm ci | Clean Install, exact versions, reproducible |
| Alpine | 5MB base, musl (may have native deps issues) |
| WSL2 | Linux kernel inside Windows |
| Image size | No kernel (shared), no GUI, minimal tools |
| Build cache | Base images hidden in cache, docker buildx du |

---

## 💪 Pro Tip for Interviews

```
"Don't just answer WHAT - explain WHY and IMPACT!"

❌ "We use multi-stage builds"
✅ "We use multi-stage builds to reduce our image from 700MB to 30MB,
    which speeds up deployments by 10x and reduces cloud costs by 95%."
```

---

## 🎯 You're Ready

Ee concepts anni clear aithe, Docker-related questions lo **confident ga answer** cheyyachu! Good luck! 🚀
