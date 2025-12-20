# ⚙️ How Actions Work: From Repo to Runner

Mawa, this is the "magic" behind `uses: actions/setup-java@v4`. Let me reveal what actually happens behind the scenes!

---

## 🎭 The Illusion

When you write this in your YAML:

```yaml
- uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: 'maven'
```

It looks like magic. Java just... appears. But what's actually happening?

---

## 🔬 Behind The Curtain

### Step 1: The Runner Reads Your YAML

There's a software called **Runner Agent** running on the VM. Think of it as a **supervisor**.

```
The "Runner Agent" is like a waiter in a restaurant.
It reads your order (YAML) and coordinates everything.
```

When Runner Agent sees `uses: actions/setup-java@v4`, it triggers a sequence:

---

### Step 2: Download the Action Code

Every action is just **code stored on GitHub**!

When you write `uses: actions/setup-java@v4`:

- **Repository:** `github.com/actions/setup-java`
- **Version:** `v4` (a Git tag)

Runner downloads this code to a hidden location:

```
/home/runner/work/_actions/actions/setup-java/v4/
├── action.yml          ← Metadata file
├── dist/
│   └── index.js        ← THE BRAIN! 🧠
└── package.json
```

**Think of it like this:**

```
You ordered Biryani → Waiter goes to kitchen → Gets the recipe → Starts cooking
YAML says setup-java → Runner downloads action code → Runs the script
```

---

### Step 3: Run the Action Script

GitHub Actions are written in **JavaScript (Node.js)**.

Runner executes:

```bash
node /home/runner/work/_actions/actions/setup-java/v4/dist/index.js
```

Now, that `index.js` file starts doing its job!

---

### Step 4: The Script Logic

Here's what the setup-java script does internally:

```
📜 index.js starts running...

1️⃣ READ INPUTS
   └── "What did the user ask for?"
   └── java-version: '17'
   └── distribution: 'temurin'
   └── cache: 'maven'

2️⃣ CHECK TOOL CACHE
   └── Look in /opt/hostedtoolcache/Java/
   └── "Is Java 17 from Temurin already here?"
   
   ├── YES! Found Java 17! ✅
   │   └── Skip download, use this one
   │
   └── NO! Not found ❌
       └── Download from adoptium.net (Temurin)
       └── Install to /opt/hostedtoolcache/Java/17.x/

3️⃣ UPDATE ENVIRONMENT
   └── Set JAVA_HOME = /opt/hostedtoolcache/Java/17.x
   └── Add Java bin folder to PATH
   └── Now `java -version` works! 🎉

4️⃣ HANDLE CACHING (if cache: 'maven')
   └── Calculate cache key from pom.xml hash
   └── Look for cache in GitHub Cloud
   └── If found: Restore to /home/runner/.m2
   └── If not: Register for save at job end
```

---

## 🌉 The Input Bridge: Environment Variables

Here's something cool. Your YAML inputs reach the action through **environment variables**!

**Your YAML:**

```yaml
with:
  java-version: '17'
  distribution: 'temurin'
```

**What Runner does before running the action:**

```bash
export INPUT_JAVA-VERSION="17"
export INPUT_DISTRIBUTION="temurin"
```

**Inside the Action code:**

```javascript
const version = process.env['INPUT_JAVA-VERSION'];  // Gets "17"
const dist = process.env['INPUT_DISTRIBUTION'];     // Gets "temurin"

console.log(`Installing Java ${version} from ${dist}...`);
```

**The analogy:**

```
You (Customer) → Waiter → Kitchen
YAML (Order)   → Env Variables (Order Slip) → Action Code (Chef)

You don't talk directly to the chef.
The waiter writes your order on a slip, chef reads the slip.
That slip is the Environment Variables!
```

---

## 📊 Visual: The Complete Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                     YOUR WORKFLOW YAML                              │
│                                                                      │
│   - uses: actions/setup-java@v4                                     │
│     with:                                                            │
│       java-version: '17'                                             │
│       distribution: 'temurin'                                        │
│       cache: 'maven'                                                 │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                │
                                │ Runner Agent reads this
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     STEP 1: DOWNLOAD ACTION                         │
│                                                                      │
│   github.com/actions/setup-java                                     │
│         │                                                            │
│         ▼                                                            │
│   /home/runner/work/_actions/actions/setup-java/v4/                 │
│   └── dist/index.js  ← Downloaded!                                  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                │
                                │ Set up environment variables
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     STEP 2: SET INPUTS AS ENV VARS                  │
│                                                                      │
│   INPUT_JAVA-VERSION = "17"                                         │
│   INPUT_DISTRIBUTION = "temurin"                                    │
│   INPUT_CACHE = "maven"                                             │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                │
                                │ node index.js
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     STEP 3: ACTION SCRIPT RUNS                      │
│                                                                      │
│   index.js                                                          │
│   │                                                                  │
│   ├── Read inputs from environment                                  │
│   │                                                                  │
│   ├── Check /opt/hostedtoolcache/Java/17...                        │
│   │   ├── Found? → Use it!                                         │
│   │   └── Not found? → Download from adoptium.net                  │
│   │                                                                  │
│   ├── Set JAVA_HOME and PATH                                        │
│   │   └── Now `java` command works!                                │
│   │                                                                  │
│   └── Check cache for Maven                                         │
│       └── Check GitHub Cloud for cache key                          │
│           ├── Found? → Restore to ~/.m2 ✅                         │
│           └── Not found? → Mark for save later ❌                  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
                                │
                                │ Your next step runs
                                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     YOUR BUILD STEP                                  │
│                                                                      │
│   - name: Build with Maven                                          │
│     run: mvn clean package                                          │
│                                                                      │
│   ✅ Java is available (PATH was set)                               │
│   ✅ Maven dependencies are available (cache was restored)          │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Takeaways

1. **Actions are just code** - JavaScript files stored on GitHub repos
2. **Runner downloads actions** - To `/home/runner/work/_actions/`
3. **Inputs become Environment Variables** - With `INPUT_` prefix
4. **Actions read env vars** - Just like reading from a config file
5. **actions/setup-java does 2 things:**
   - Links Java from tool cache to PATH
   - Handles Maven cache (if specified)

---

## ⏭️ Next Up

Now you understand HOW actions work. But how does the cache know WHEN to refresh? What makes it decide "this is old, download fresh"?

**Next: [04-cache-key-magic.md](./04-cache-key-magic.md)** 👉
