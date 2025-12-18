# 🏪 06 - Actions Marketplace (Your Key Doubt!)

## 1. Your Question: "Actions ekkada nunchi vasthay?"

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   uses: docker/build-push-action@v5                                          │
│         ──────────────────────────                                           │
│         │      │              │                                              │
│         │      │              └── Version (v5)                               │
│         │      └── Action name                                              │
│         └── Owner/Organization                                              │
│                                                                              │
│   THIS IS A GITHUB REPOSITORY!                                               │
│   URL: https://github.com/docker/build-push-action                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Actions = Like npm Packages

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   NPM (JavaScript):                GitHub Actions:                           │
│   ═════════════════                ════════════════                          │
│                                                                              │
│   npm install axios              uses: docker/build-push-action@v5        │
│   │           │                        │      │                 │           │
│   │           └── Package              │      └── Action        │           │
│   └── Command                          └── Owner           Version          │
│                                                                              │
│   Someone wrote the code, published it, we just USE it!                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Common Actions We Use

| Action | Purpose |
|--------|---------|
| `actions/checkout@v4` | Clone your repo to runner |
| `docker/login-action@v3` | Login to container registry |
| `docker/setup-buildx-action@v3` | Setup advanced Docker builder |
| `docker/build-push-action@v5` | Build & push Docker image |

---

## 4. How to Find Actions?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   METHOD 1: GitHub Marketplace                                               │
│   ═════════════════════════════                                              │
│   https://github.com/marketplace?type=actions                               │
│   Search → Find → Copy usage!                                               │
│                                                                              │
│   METHOD 2: Google                                                           │
│   ═══════════════════                                                        │
│   "github actions docker build" → First result usually official!           │
│                                                                              │
│   METHOD 3: Copy from Examples                                               │
│   ═════════════════════════════                                              │
│   Other repos, tutorials - copy their workflow!                             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Version Question: v5 vs v6.18.0?

```
Both work! ✅

v5 = Stable, tested, many production workflows use it
v6 = Latest features, but same core functionality

"If it ain't broke, don't fix it!" - Enterprise mindset
```

---

## 🎯 Key Point

Actions are reusable code published on GitHub. Use `uses:` to use them!
