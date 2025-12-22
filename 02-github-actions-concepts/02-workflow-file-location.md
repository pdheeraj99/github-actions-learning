# 📁 02 - Workflow File Location

## 1. Where Does GitHub Look for Workflows?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   your-repo/                                                                 │
│   ├── .github/                   ← Hidden folder (. tho start)              │
│   │   └── workflows/             ← MUST be exactly "workflows"              │
│   │       ├── ci.yml             ← Workflow 1                               │
│   │       ├── deploy.yml         ← Workflow 2                               │
│   │       └── docker-build.yml   ← Workflow 3                               │
│   ├── src/                                                                   │
│   └── package.json                                                           │
│                                                                              │
│   RULE: .github/workflows/*.yml files = GitHub detects as workflows!       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Common Mistakes


```
❌ github/workflows/ci.yml        → Missing dot!
❌ .github/workflow/ci.yml        → "workflow" not "workflows"!
❌ workflows/ci.yml               → Not inside .github!
❌ .github/workflows/ci.txt       → Wrong extension!

✅ .github/workflows/ci.yml       → Correct!
✅ .github/workflows/anything.yaml → Correct! (.yaml also works)
```

---

## 3. What is YAML?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   YAML = Yet Another Markup Language                                        │
│                                                                              │
│   Like JSON but more readable!                                              │
│   Uses INDENTATION (spaces) instead of { }                                  │
│                                                                              │
│   JSON:                           YAML:                                      │
│   {                               name: John                                 │
│     "name": "John",               age: 25                                   │
│     "age": 25                     skills:                                   │
│   }                                 - React                                 │
│                                     - Docker                                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Point

Workflow files go in `.github/workflows/` - GitHub automatically detects them!
