# 📂 Runner File System: Where Things Live

Mawa, idi most confusing part for many people. But once you understand this, everything clicks!

Let me explain exactly **where your code, dependencies, and tools live** inside the GitHub Runner.

---

## 🖥️ The Big Picture

When GitHub Actions runs your workflow, it creates a **fresh Ubuntu VM** (Virtual Machine). This VM has a standard Linux file system.

```
The Runner is like a fresh hotel room.
Every time you check in, it's clean and empty.
You bring your stuff (code), use the facilities (tools),
and when you leave (job ends), everything is cleaned up.
```

---

## 🗂️ The File System Map

Here's the exact structure. I'm highlighting the **important folders** you need to know:

```
/ (Root)
│
├── /home
│   └── /runner                    ← The "user" running your job
│       │
│       ├── /.m2                   ⭐ HIDDEN! Maven dependencies go here
│       │   └── /repository
│       │       ├── org/springframework/...
│       │       └── com/fasterxml/jackson/...
│       │
│       └── /work                  ⭐ MAIN WORKING DIRECTORY
│           └── /github-actions-learning    (Your repo name)
│               │
│               └── /github-actions-learning  ⭐ $GITHUB_WORKSPACE
│                   ├── src/
│                   ├── pom.xml
│                   ├── target/         (Build output goes here)
│                   └── .github/workflows/
│
├── /opt
│   └── /hostedtoolcache           ⭐ PRE-INSTALLED TOOLS
│       ├── Java/
│       │   ├── 17.0.x/
│       │   └── 21.0.x/
│       ├── node/
│       │   ├── 18.x/
│       │   └── 20.x/
│       └── Python/
│
└── /tmp                           ← Temporary files
```

---

## 🔍 Let's Break Down Each Location

### 1. `$GITHUB_WORKSPACE` - Your Code Lives Here

**Path:** `/home/runner/work/{repo-name}/{repo-name}`

This is where your repository code is checked out during the `actions/checkout` step.

```yaml
- uses: actions/checkout@v4
# Your code is NOW at $GITHUB_WORKSPACE
# All subsequent commands run HERE by default
```

**What's inside:**

- Your `src/` folder
- Your `pom.xml` or `package.json`
- Your `.github/workflows/` folder
- Everything from your repository!

**Why double folder?**
The structure is `/work/{repo}/{repo}` because GitHub allows **multiple repos** to be checked out side-by-side. Example:

```
/work/github-actions-learning/
├── /github-actions-learning/    ← Main repo (GITHUB_WORKSPACE)
└── /some-helper-repo/           ← Secondary checkout
```

---

### 2. `/home/runner/.m2` - The Hidden Maven Cache

**Path:** `/home/runner/.m2/repository`

This folder is **hidden** (starts with `.`). Maven stores all downloaded dependencies here.

**When cache RESTORE happens:**

```
GitHub Cloud Cache → Downloads → Extracts to /home/runner/.m2/repository
```

**When cache SAVE happens:**

```
/home/runner/.m2/repository → Zipped → Uploaded to GitHub Cloud
```

**What's inside:**

```
/.m2/repository/
├── org/
│   └── springframework/
│       └── boot/
│           └── spring-boot-starter-web/
│               └── 3.2.0/
│                   └── spring-boot-starter-web-3.2.0.jar
├── com/
│   └── fasterxml/
│       └── jackson/
│           └── core/
│               └── jackson-core-2.15.0.jar
└── ... (hundreds more JARs!)
```

---

### 3. `/opt/hostedtoolcache` - The Tool Store Room

**Path:** `/opt/hostedtoolcache`

GitHub pre-installs common tools here. But they're **not active** by default!

Think of it like this:

```
Hotel has AC, TV, Fridge in a storage room.
They're installed but not connected to your room.
You need to ask housekeeping to connect them.
```

**What's stored:**

```
/opt/hostedtoolcache/
├── Java/
│   ├── 17.0.1/x64/     ← Java 17 binary files
│   └── 21.0.1/x64/     ← Java 21 binary files
├── node/
│   ├── 18.19.0/        ← Node.js 18
│   └── 20.10.0/        ← Node.js 20
└── Python/
    ├── 3.10.0/
    └── 3.11.0/
```

**How they get activated:**

```yaml
- uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
```

This action goes to `/opt/hostedtoolcache`, finds Java 17, and **links it to your PATH**!

---

## 🔄 The Complete Flow

Let me tell you the story of what happens when your workflow runs:

```
1️⃣ BOOT UP
   └── Fresh Ubuntu VM starts
   └── Empty file system (mostly)
   └── /opt/hostedtoolcache has pre-installed tools (but inactive)

2️⃣ CHECKOUT
   └── actions/checkout downloads your repo
   └── Places it at /home/runner/work/repo/repo
   └── This becomes $GITHUB_WORKSPACE

3️⃣ SETUP TOOLS  
   └── actions/setup-java runs
   └── Finds Java 17 in /opt/hostedtoolcache
   └── Adds it to PATH (now `java` command works!)
   └── ALSO checks for Maven cache...

4️⃣ CACHE RESTORE (if setup-java has cache: 'maven')
   └── Looks for cache in GitHub Cloud
   └── If found: Downloads → Extracts to /home/runner/.m2
   └── If not found: Nothing happens (Maven will download fresh)

5️⃣ BUILD
   └── Maven runs from $GITHUB_WORKSPACE
   └── Reads pom.xml
   └── Looks for dependencies in /home/runner/.m2/repository
   └── If found: Uses them!
   └── If not found: Downloads to /home/runner/.m2/repository

6️⃣ CACHE SAVE (Post job)
   └── If cache was MISS: Zip /home/runner/.m2 → Upload to GitHub Cloud
   └── If cache was HIT: Skip (already exists)

7️⃣ CLEANUP
   └── VM is DESTROYED 💀
   └── All local data is GONE
   └── But cache is SAFE in GitHub Cloud!
```

---

## 🎯 Key Takeaways

| Location | What | Persistent? |
|----------|------|-------------|
| `/home/runner/work/repo/repo` | Your code | ❌ Gone after job |
| `/home/runner/.m2/repository` | Maven dependencies | ❌ Gone, but cached! |
| `/opt/hostedtoolcache` | Pre-installed tools | ❌ Gone after job |
| **GitHub Cloud** | Cache storage | ✅ Persists 7 days! |

**The Magic:** Even though the VM is destroyed, the cache is saved to **GitHub Cloud**. Next run, it's restored!

---

## ⏭️ Next Up

Now you know WHERE things live. But HOW do Actions work? How does `uses: actions/setup-java` actually do its magic?

**Next: [03-how-actions-work.md](./03-how-actions-work.md)** 👉
