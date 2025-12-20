# 🔑 Cache Key Magic: When Does Cache Refresh?

Mawa, this is the secret sauce! The "hash" or "fingerprint" concept that makes caching intelligent.

---

## ❓ The Question

> "If I add a new dependency to pom.xml, how does cache know it's outdated?"

Great question! The answer is the **Cache Key**.

---

## 🔐 What is a Cache Key?

A cache key is like an **ID card** for your cache.

```
Cache Key = A unique string that identifies this specific cache

Example:
setup-java-Linux-x64-maven-a9df54bf4d9a1e2b3c4d5e6f7g8h9i0j
                          ↑______________________________↑
                                    This is a HASH!
```

The hash is calculated from your **pom.xml** (or package.json for Node.js) content!

---

## 📝 How Hash Works

A hash function takes any input and produces a fixed-length string.

**Key property:** Same input → Same output. Different input → Different output.

```
pom.xml content:                    Hash Output:
┌───────────────────────┐           
│ spring-boot-web       │  ───────►  abc123def456
│ spring-data-jpa       │
└───────────────────────┘

Change the content:
┌───────────────────────┐           
│ spring-boot-web       │  ───────►  xyz789uvw012
│ spring-data-jpa       │              ↑
│ lombok  ← NEW!        │              DIFFERENT!
└───────────────────────┘
```

**Think of it like a fingerprint:**

- Your fingerprint is unique to YOU
- Change anything (even slightly) → Fingerprint changes
- Same person → Same fingerprint

---

## 🎬 Three Scenarios Explained

### Scenario 1: First Run (No Cache Exists)

```
pom.xml:
├── spring-boot-web
└── spring-data-jpa

Hash calculated: abc123
Cache Key: setup-java-Linux-maven-abc123

GitHub says: "Never seen this key before!" ❌
Result: CACHE MISS

→ Maven downloads all dependencies
→ After job: Cache is SAVED with key abc123
```

---

### Scenario 2: Code Change Only (Cache Hit!)

```
You changed: src/TodoController.java
You DID NOT change: pom.xml

pom.xml (unchanged):
├── spring-boot-web
└── spring-data-jpa

Hash calculated: abc123 (SAME!)
Cache Key: setup-java-Linux-maven-abc123 (SAME!)

GitHub says: "I know this key! Here's your cache!" ✅
Result: CACHE HIT

→ Cache restored in seconds
→ No download needed
→ Build runs fast!
```

---

### Scenario 3: New Dependency (Cache Invalidates!)

```
You changed: pom.xml (added lombok)

pom.xml (changed):
├── spring-boot-web
├── spring-data-jpa
└── lombok  ← NEW!

Hash calculated: xyz789 (DIFFERENT!)
Cache Key: setup-java-Linux-maven-xyz789 (NEW KEY!)

GitHub says: "Never seen xyz789! I have abc123 but that's different!" ❌
Result: CACHE MISS

→ Maven downloads ALL dependencies fresh
→ After job: NEW cache saved with key xyz789
→ Old cache abc123 still exists but won't be used
```

---

## 🧮 Cache Key Structure

Let's decode the full cache key:

```
setup-java-Linux-x64-maven-a9df54bf4d9a...
   │         │     │    │       │
   │         │     │    │       └── Hash of pom.xml content
   │         │     │    └────────── Build tool (maven/gradle)
   │         │     └─────────────── Architecture
   │         └───────────────────── Operating system
   └─────────────────────────────── Action name
```

The hash part is what makes each cache unique to your specific dependencies!

---

## 📊 Visual: The Decision Flow

```
Workflow starts
      │
      ▼
┌─────────────────────────────────────┐
│ Calculate Hash of pom.xml           │
│ Hash = abc123                        │
└─────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────┐
│ Build Cache Key:                     │
│ setup-java-Linux-maven-abc123        │
└─────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────┐
│ Ask GitHub Cloud:                    │
│ "Do you have cache with this key?"   │
└─────────────────────────────────────┘
      │
      ├── YES → CACHE HIT! ✅
      │         └── Restore cache → Fast build
      │
      └── NO → CACHE MISS ❌
                └── Download fresh → Slow build
                └── After job: Save new cache
```

---

## 💡 Why This Design?

1. **Automatic invalidation** - No manual cache clearing!
2. **Content-based** - Only refreshes when dependencies actually change
3. **Efficient** - Code changes don't trigger re-download

---

## 🧪 Experiment: Force Cache Refresh

Want to force a fresh cache? You have options:

**Option 1: Change pom.xml** (adds a comment)

```xml
<!-- Cache bust: 2024-01-15 -->
<dependencies>
  ...
</dependencies>
```

**Option 2: Delete cache from GitHub UI**

- Go to Settings → Actions → Caches
- Delete the specific cache entry

**Option 3: Use workflow input**

```yaml
on:
  workflow_dispatch:
    inputs:
      clear-cache:
        description: 'Clear dependency cache'
        type: boolean
        default: false

jobs:
  build:
    steps:
      - uses: actions/setup-java@v4
        with:
          cache: ${{ inputs.clear-cache && '' || 'maven' }}
```

---

## 🎯 Key Takeaways

| You Change | Hash Changes? | Cache Result |
|------------|---------------|--------------|
| Only src/ code | ❌ No | ✅ HIT |
| pom.xml (add dependency) | ✅ Yes | ❌ MISS |
| pom.xml (change version) | ✅ Yes | ❌ MISS |
| README.md | ❌ No | ✅ HIT |
| Nothing (re-run) | ❌ No | ✅ HIT |

**The rule is simple:**
> pom.xml changes → New hash → New cache key → Cache miss → Fresh download

---

## ⏭️ Next Up

You now understand Maven/dependency caching. But what about **Docker**? The layers, the images - how does that connect?

**Next: [05-docker-caching.md](./05-docker-caching.md)** 👉
