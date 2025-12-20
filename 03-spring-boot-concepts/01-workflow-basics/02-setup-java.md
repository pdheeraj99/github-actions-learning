# ☕ Step 2: Setup Java

## What Happens?

```yaml
- name: ☕ Setup JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'maven'        # 🔥 This is the magic!
```

---

## 🎯 Telugu Simple ga

> "Java 17 ni PATH lo add chesi, Maven dependencies ni cache nundi restore chesthundi"

---

## 📊 This Step Does THREE Things

```
Setup Java Step
│
├── 1️⃣ Find Java 17
│   └── Check /opt/hostedtoolcache/Java/17.x
│   └── Already installed? Great! Use it.
│
├── 2️⃣ Make Java Available
│   └── Set JAVA_HOME = /opt/hostedtoolcache/Java/17.x
│   └── Add to PATH
│   └── Now `java -version` works!
│
└── 3️⃣ Handle Maven Cache (if cache: 'maven')
    │
    ├── JOB START:
    │   └── Check GitHub Cloud for cache
    │   └── Found? DOWNLOAD → /home/runner/.m2 ✅
    │   └── Not found? "maven cache is not found" ❌
    │
    └── JOB END (Post step):
        └── ZIP /home/runner/.m2
        └── UPLOAD to GitHub Cloud
        └── Next run will use this cache!
```

---

## 🔄 Cache Flow Diagram

```
JOB START                                               JOB END
    │                                                      │
    ▼                                                      ▼
┌─────────────────────────────────────────────────────────────────┐
│ GitHub Cloud (Cache Storage)                                    │
│ ┌─────────────────────────┐     ┌─────────────────────────┐    │
│ │ maven-abc123            │     │ maven-abc123            │    │
│ │ 70 MB                   │ ──▶ │ 70 MB (Updated)         │    │
│ └─────────────────────────┘     └─────────────────────────┘    │
│           │                              ▲                      │
│           │ DOWNLOAD                     │ UPLOAD               │
│           ▼                              │                      │
│ ┌─────────────────────────────────────────┐                    │
│ │ /home/runner/.m2/repository             │                    │
│ │ ├── org/springframework/...             │                    │
│ │ ├── com/fasterxml/jackson/...           │                    │
│ │ └── (All your dependencies!)            │                    │
│ └─────────────────────────────────────────┘                    │
│                Runner VM                                        │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📸 Cache Messages in Logs

### First Run (Cache MISS)

```
Setup JDK 17
  Run actions/setup-java@v4
  Installed distribution: Temurin
  Java version: 17.0.5
  maven cache is not found  ← No cache yet!
```

### Second Run (Cache HIT)

```
Setup JDK 17  
  Run actions/setup-java@v4
  Installed distribution: Temurin
  Java version: 17.0.5
  Cache hit for: setup-java-Linux-maven-a9df54...  ← Found cache!
```

---

## 🧠 The `restore-keys` Magic

Even if pom.xml changes (new hash), old cache can partially match!

```
pom.xml BEFORE hash: abc123
pom.xml AFTER hash:  xyz789  (new dependency added)

Cache lookup:
1. Exact match "maven-xyz789"? ❌ Not found
2. Prefix match "maven-*"? ✅ Found "maven-abc123"!
3. Restore OLD cache → Only download NEW dependency!
```

**Result:** Add 1 new dependency = Download 1 JAR, not 200! ⚡

---

## 🔑 Key Points

| Concept | Details |
|---------|---------|
| **Cache key** | Based on pom.xml hash |
| **Cache location** | GitHub Cloud (persists 7 days) |
| **Restore location** | `/home/runner/.m2/repository` |
| **Download from** | GitHub Cloud at job START |
| **Upload to** | GitHub Cloud at job END ("Post" step) |
| **Time savings** | 60-90 seconds per run! |

---

**Next: [03-maven-commands.md](./03-maven-commands.md)** 👉
