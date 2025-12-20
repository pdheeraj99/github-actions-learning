# 📂 Filesystem Structure

Where exactly does your code, dependencies, and tools live?

---

## 🗂️ Complete Directory Map

```
/ (Root)
│
├── /tmp                           ← Temporary files
│
├── /home
│   └── /runner                    ← The user running your job
│       │
│       ├── /.m2                   ⭐ HIDDEN! Maven cache
│       │   └── /repository
│       │       ├── org/springframework/...
│       │       └── com/fasterxml/jackson/...
│       │
│       └── /work                  ⭐ MAIN WORKING AREA
│           └── /github-actions-learning    (Your repo name)
│               │
│               └── /github-actions-learning  ⭐ $GITHUB_WORKSPACE
│                   ├── src/
│                   ├── pom.xml
│                   ├── target/         (Build output)
│                   └── .github/workflows/
│
├── /opt
│   └── /hostedtoolcache           ⭐ PRE-INSTALLED TOOLS
│       ├── Java/
│       │   ├── 17.0.x/
│       │   └── 21.0.x/
│       ├── node/
│       └── Python/
│
└── /var, /root, /etc              ← System folders (rarely used)
```

---

## 🔑 Key Locations Explained

### 1. `$GITHUB_WORKSPACE`

**Path:** `/home/runner/work/{repo-name}/{repo-name}`

```
Your repository code lives here!
All `run:` commands execute from this location by default.
```

### 2. `/home/runner/.m2/repository`

**Path:** `/home/runner/.m2/repository`

```
Maven dependencies are cached here.
When cache restores, it extracts TO this folder.
When cache saves, it zips FROM this folder.
```

### 3. `/opt/hostedtoolcache`

**Path:** `/opt/hostedtoolcache/{tool}/{version}`

```
Pre-installed tools live here.
setup-java, setup-node just ADD THESE TO PATH.
They don't download (usually) - tools are already here!
```

---

## 🤔 Why Double Folder Name?

```
/work/github-actions-learning/github-actions-learning/
      └── REPO folder           └── WORKSPACE folder
```

This allows multiple repo checkouts:

```
/work/my-repo/
├── my-repo/          ← Main repo
└── helper-tools/     ← Secondary checkout
```

---

## 📊 What Happens When VM Boots?

```
1️⃣ Fresh Ubuntu VM starts
   └── /opt/hostedtoolcache ready (Java, Node pre-installed)
   └── /home/runner/.m2 is EMPTY (no cache yet)
   └── /home/runner/work is EMPTY (no code yet)

2️⃣ Checkout step
   └── Git clones your repo to /home/runner/work/repo/repo

3️⃣ Setup Java step
   └── Adds Java from /opt/hostedtoolcache to PATH
   └── Restores cache to /home/runner/.m2

4️⃣ Build steps
   └── Maven runs from $GITHUB_WORKSPACE
   └── Uses dependencies from /home/runner/.m2

5️⃣ Job ends
   └── Cache saves from /home/runner/.m2 to GitHub Cloud
   └── VM is DESTROYED 💀 (all local data gone!)
```

---

**Next: [02-hostedtoolcache.md](./02-hostedtoolcache.md)** 👉
