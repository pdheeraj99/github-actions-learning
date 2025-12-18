# ⚡ 03 - ON: Triggers (When Does Workflow Run?)

## 1. The `on:` Keyword

```yaml
on:
  push:
    branches: [main]
```

**Meaning**: "ON what event should this run?"

---

## 2. Common Triggers

### 2.1 Push Trigger

```yaml
on:
  push:
    branches: [main]
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   When someone pushes to main branch → Run workflow!                        │
│                                                                              │
│   git push origin main → Workflow triggers! ✅                              │
│   git push origin feature → Workflow SKIPS! ❌ (not main)                   │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.2 Paths Filter (SMART!)

```yaml
on:
  push:
    paths:
      - '03-react-pipeline/todo-frontend/**'
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   ONLY run when files in this path change!                                  │
│                                                                              │
│   Change App.tsx (in todo-frontend/) → Workflow RUNS! ✅                    │
│   Change README.md (in root)         → Workflow SKIPS! ❌                   │
│                                                                              │
│   BENEFIT: Save CI minutes! Only run when relevant! 💰                      │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.3 workflow_dispatch (Manual Button!)

```yaml
on:
  workflow_dispatch:
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│   Adds "Run workflow" button in GitHub UI!                                  │
│                                                                              │
│   USE CASES (Your question!):                                               │
│   • Build failed due to network? → Click button, retry!                    │
│   • Need rebuild without code change? → Click button!                      │
│   • Testing workflow changes? → Click button!                              │
│   • Emergency deploy? → Click button!                                      │
│                                                                              │
│   "Push cheyakkarledu - button click cheste run avuthundhi!"               │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.4 Pull Request

```yaml
on:
  pull_request:
    branches: [main]
```

When PR is opened/updated against main → Run workflow!

---

## 3. Multiple Triggers

```yaml
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]
  workflow_dispatch:
```

**Any of these** triggers the workflow!

---

## 🎯 Key Point

`on:` = WHEN to run. Use `paths:` to save CI minutes, `workflow_dispatch:` for manual runs!
