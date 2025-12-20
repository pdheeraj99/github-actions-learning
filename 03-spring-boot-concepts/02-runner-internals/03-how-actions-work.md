# ⚙️ How Actions Work: YAML to Execution

What happens when you write `uses: actions/setup-java@v4`?

---

## 🎯 Telugu Simple ga

> "Actions are just code! GitHub downloads that code and runs it."

---

## 🔄 The Complete Flow

```
Step 1: You Write YAML
┌─────────────────────────────────────────┐
│ - uses: actions/setup-java@v4           │
│   with:                                 │
│     java-version: '17'                  │
└─────────────────────────────────────────┘
         │
         │ Runner Agent sees this
         ▼
Step 2: Download Action Code
┌─────────────────────────────────────────┐
│ Git clone from:                         │
│ github.com/actions/setup-java           │
│ Tag: v4                                 │
│                                         │
│ Downloads to:                           │
│ /home/runner/work/_actions/             │
│   └── actions/setup-java/v4/            │
│       ├── action.yml  ← Metadata        │
│       └── dist/                         │
│           └── index.js ← THE CODE!      │
└─────────────────────────────────────────┘
         │
         │ Set up environment
         ▼
Step 3: Environment Variables Bridge
┌─────────────────────────────────────────┐
│ Your YAML inputs become ENV vars:       │
│                                         │
│ INPUT_JAVA-VERSION = "17"               │
│ INPUT_DISTRIBUTION = "temurin"          │
│ INPUT_CACHE = "maven"                   │
│                                         │
│ (Prefix INPUT_ added to each input!)    │
└─────────────────────────────────────────┘
         │
         │ node index.js
         ▼
Step 4: Action Runs
┌─────────────────────────────────────────┐
│ // Inside index.js (simplified)         │
│                                         │
│ const version = process.env['INPUT_...']│
│                                         │
│ 1. Check hostedtoolcache for Java 17    │
│ 2. If not found, download from Adoptium │
│ 3. Set JAVA_HOME environment variable   │
│ 4. Add Java bin to PATH                 │
│ 5. Handle Maven caching                 │
└─────────────────────────────────────────┘
         │
         ▼
Step 5: Your Next Step Runs
┌─────────────────────────────────────────┐
│ - run: mvn clean compile                │
│                                         │
│ Java is now available! ✅               │
│ Maven can use cached dependencies! ✅   │
└─────────────────────────────────────────┘
```

---

## 🌉 The INPUT_ Bridge

Your YAML `with:` inputs → Environment variables with `INPUT_` prefix

```yaml
# Your YAML
with:
  java-version: '17'
  distribution: 'temurin'
```

```bash
# Runner sets these before running action
export INPUT_JAVA-VERSION="17"
export INPUT_DISTRIBUTION="temurin"
```

```javascript
// Action reads them
const version = process.env['INPUT_JAVA-VERSION'];  // "17"
const dist = process.env['INPUT_DISTRIBUTION'];     // "temurin"
```

---

## 📂 Where Action Code Lives

```
/home/runner/work/_actions/
│
├── actions/
│   ├── checkout/v4/           ← actions/checkout
│   │   ├── action.yml
│   │   └── dist/index.js
│   │
│   ├── setup-java/v4/         ← actions/setup-java
│   │   ├── action.yml
│   │   └── dist/index.js
│   │
│   └── upload-artifact/v4/    ← actions/upload-artifact
│
└── docker/
    └── build-push-action/v5/  ← docker/build-push-action
```

---

## 🔑 Key Points

| Concept | Details |
|---------|---------|
| **Actions are code** | JavaScript/TypeScript files |
| **Downloaded to** | `/home/runner/work/_actions/` |
| **Inputs bridge** | `with:` → `INPUT_*` env vars |
| **Execution** | `node index.js` |
| **Post steps** | Some actions have cleanup code |

---

**Congratulations! You've completed Runner Internals!** 🎉

**Next folder: [../03-caching-explained/](../03-caching-explained/00-README.md)** 👉
