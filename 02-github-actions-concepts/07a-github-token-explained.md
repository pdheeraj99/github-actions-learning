# 🔐 Secrets & Tokens in GitHub Actions

Understanding automatic and manual secrets!

---

## 🎯 Telugu Simple ga

> "GITHUB_TOKEN = Automatic secret, nuvvu create cheyyakkarledu!"
> "Personal secrets = Nuvvu create cheyyali (API keys, passwords)"

---

## 🔑 GITHUB_TOKEN - The Magic Secret

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                         GITHUB_TOKEN EXPLAINED                                   │
│                                                                                  │
│   ❓ Doubt: "secrets.GITHUB_TOKEN ekkada setup cheyyali?"                       │
│                                                                                  │
│   ✅ Answer: NOWHERE! GitHub AUTO-GENERATES it!                                 │
│                                                                                  │
│   ════════════════════════════════════════════════════════════════════════════  │
│                                                                                  │
│   Every workflow run gets a FRESH token automatically:                          │
│                                                                                  │
│   git push                                                                       │
│       │                                                                          │
│       ▼                                                                          │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │  GitHub: "Okk, workflow trigger ayyindi. Token create chestha..."        │  │
│   │                                                                          │  │
│   │  GITHUB_TOKEN = ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx                 │  │
│   │                                                                          │  │
│   │  This token can:                                                         │  │
│   │  • Push to GHCR ✅                                                       │  │
│   │  • Read repository ✅                                                    │  │
│   │  • Create comments ✅                                                    │  │
│   │                                                                          │  │
│   │  Workflow complete ayyaka: Token EXPIRED! 🔒                             │  │
│   └──────────────────────────────────────────────────────────────────────────┘  │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 📝 How to Use GITHUB_TOKEN

```yaml
# In your workflow file:
- name: 🔐 Login to GHCR
  uses: docker/login-action@v3
  with:
    registry: ghcr.io
    username: ${{ github.actor }}           # Your GitHub username
    password: ${{ secrets.GITHUB_TOKEN }}   # Auto-provided!
```

**No setup needed!** Just use `${{ secrets.GITHUB_TOKEN }}`

---

## 🛡️ Permissions for GITHUB_TOKEN

```yaml
jobs:
  docker-build:
    permissions:
      contents: read     # Read repository files
      packages: write    # Push to GHCR
```

```
DEFAULT permissions = READ only
GHCR push kavali = packages: write add cheyyali!
```

---

## 🔐 Custom Secrets (You Must Create)

| Secret | Auto? | Example |
|--------|-------|---------|
| `GITHUB_TOKEN` | ✅ Auto | GHCR push |
| `SONAR_TOKEN` | ❌ Manual | SonarCloud analysis |
| `AWS_ACCESS_KEY` | ❌ Manual | AWS deployment |
| `DOCKER_HUB_TOKEN` | ❌ Manual | Docker Hub push |

---

## 🛠️ How to Create Manual Secrets

```
Repository → Settings → Secrets and variables → Actions → New repository secret

                    ┌──────────────────────────────────────┐
                    │  Name:  SONAR_TOKEN                  │
                    │  Secret: xxxxxxxxxxxxxxxxxxxxxx       │
                    │                                       │
                    │           [Add secret]                │
                    └──────────────────────────────────────┘
```

---

## 🎬 Telugu Summary

```
GITHUB_TOKEN:
─────────────
• Automatic - nuvvu create cheyyakkarledu!
• Every workflow ki fresh token vasthundi
• Workflow finish ayyaka expire avuthundi
• Use: ${{ secrets.GITHUB_TOKEN }}

Custom Secrets:
───────────────
• Nuvvu manually create cheyyali
• Repo Settings → Secrets lo add cheyyi
• Use: ${{ secrets.YOUR_SECRET_NAME }}
```

---

**Related: [07-secrets-and-tokens.md](./07-secrets-and-tokens.md)** 👉
