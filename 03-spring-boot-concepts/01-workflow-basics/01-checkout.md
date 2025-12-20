# 📥 Step 1: Checkout Code

## What Happens?

```yaml
- name: 📥 Checkout Code
  uses: actions/checkout@v4
```

---

## 🎯 Telugu Simple ga

> "GitHub repository lo unna code ni Runner VM loniki copy chesthundi"

---

## 📊 Before vs After

### Before Checkout

```
Runner VM:
/home/runner/work/
└── (EMPTY! Nothing here!)

Maven: "pom.xml ekkada? Build cheyalenu!" 😭
```

### After Checkout

```
Runner VM:
/home/runner/work/github-actions-learning/github-actions-learning/
├── 02-spring-boot-pipeline/
│   └── todo-backend/
│       ├── pom.xml        ✅ Maven can find this!
│       ├── src/           ✅ Java code
│       └── Dockerfile     ✅ Docker instructions
├── .github/
│   └── workflows/
│       └── spring-boot-build.yml
└── README.md
```

---

## 🤔 Why Double Folder Name?

```
/work/github-actions-learning/github-actions-learning/
      ↑                       ↑
      REPO folder             WORKSPACE folder
```

**Reason:** You can checkout MULTIPLE repos side-by-side!

```yaml
- uses: actions/checkout@v4  # Main repo at default path

- uses: actions/checkout@v4
  with:
    repository: other-org/helper-tools
    path: ./tools  # Different folder!
```

Result:

```
/work/github-actions-learning/
├── github-actions-learning/   ← Main repo (GITHUB_WORKSPACE)
└── tools/                      ← Secondary repo
```

---

## 🔑 Key Points

| Concept | Value |
|---------|-------|
| **Action** | `actions/checkout@v4` |
| **What it does** | `git clone` your repo |
| **Where** | `/home/runner/work/{repo}/{repo}` |
| **Environment Variable** | `$GITHUB_WORKSPACE` points here |
| **Time** | 2-3 seconds |

---

**Next: [02-setup-java.md](./02-setup-java.md)** 👉
