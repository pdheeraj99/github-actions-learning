# 🗄️ The Tool Cache: /opt/hostedtoolcache

Pre-installed tools on the Runner!

---

## 🎯 Telugu Simple ga

> "Hotel room lo TV, AC, Fridge already installed undi - just switch ON cheyali!"
> "Similarly, Java, Node.js already installed - just PATH ki add cheyali!"

---

## 📂 What's Inside?

```
/opt/hostedtoolcache/
│
├── Java/
│   ├── 11.0.20/x64/          ← Java 11
│   │   ├── bin/
│   │   │   ├── java
│   │   │   └── javac
│   │   └── lib/
│   ├── 17.0.8/x64/           ← Java 17 (we use this!)
│   └── 21.0.1/x64/           ← Java 21
│
├── node/
│   ├── 18.19.0/x64/          ← Node.js 18
│   └── 20.10.0/x64/          ← Node.js 20
│
├── Python/
│   ├── 3.10.0/x64/
│   └── 3.11.0/x64/
│
└── Maven/
    └── 3.9.0/
```

---

## 🔄 How Setup Actions Work

### Before setup-java

```bash
$ java -version
java: command not found  ❌
```

Java EXISTS at `/opt/hostedtoolcache/Java/17.0.8/x64/bin/java`
But it's NOT in PATH!

### After setup-java

```bash
$ java -version
openjdk version "17.0.8" ✅
```

The action did:

1. Found Java 17 in `/opt/hostedtoolcache/Java/17.0.8`
2. Set `JAVA_HOME=/opt/hostedtoolcache/Java/17.0.8/x64`
3. Added to PATH: `export PATH="$JAVA_HOME/bin:$PATH"`

---

## 📊 Flow Diagram

```
Your YAML:
┌─────────────────────────────────────────┐
│ - uses: actions/setup-java@v4           │
│   with:                                 │
│     java-version: '17'                  │
│     distribution: 'temurin'             │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│ Action checks /opt/hostedtoolcache      │
│                                         │
│ "Is Java 17 Temurin already here?"      │
│                                         │
│ ├── YES! ✅ → Just add to PATH          │
│ │                                       │
│ └── NO ❌ → Download from Adoptium      │
│            → Install to hostedtoolcache │
│            → Then add to PATH           │
└─────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│ Environment Variables Set:              │
│                                         │
│ JAVA_HOME=/opt/hostedtoolcache/Java/17.x│
│ PATH=$JAVA_HOME/bin:$PATH               │
│                                         │
│ Now `java -version` works! 🎉           │
└─────────────────────────────────────────┘
```

---

## 🤔 Why Pre-Install Tools?

| Without Hostedtoolcache | With Hostedtoolcache |
|------------------------|---------------------|
| Download Java (300MB) | Already there! |
| 30-60 seconds | 1-2 seconds |
| Internet bandwidth | Local file only |

**GitHub saves time by pre-installing common tools!**

---

## 🔑 Key Points

| Concept | Details |
|---------|---------|
| **Location** | `/opt/hostedtoolcache/{tool}/{version}` |
| **Pre-installed** | Java, Node, Python, Ruby, Go |
| **setup-* actions** | Don't download - just add to PATH |
| **Custom versions** | If not present, action downloads |

---

**Next: [03-how-actions-work.md](./03-how-actions-work.md)** 👉
