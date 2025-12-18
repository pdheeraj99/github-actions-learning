# 🔐 07 - Secrets and GITHUB_TOKEN

## 1. Your Doubt: "Password ekkada nunchi vasthundhi?"

```yaml
password: ${{ secrets.GITHUB_TOKEN }}
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   GITHUB_TOKEN = Magic token! ✨                                            │
│   ═══════════════════════════════                                            │
│                                                                              │
│   • GitHub AUTOMATICALLY provides this!                                     │
│   • No manual setup needed!                                                 │
│   • Fresh token for each workflow run                                       │
│   • Expires after workflow ends                                             │
│   • Has permissions we specified in workflow                                │
│                                                                              │
│   Telugu:                                                                    │
│   ────────                                                                   │
│   "Nuvvu emi cheyakkarledu - GitHub automatic ga password ichindhi!"       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. How It Works

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   When workflow starts:                                                      │
│   ──────────────────────                                                     │
│                                                                              │
│   GitHub: "Here's a fresh token for this run!"                              │
│           Token: ghp_xxxxxxxxxxxxxxxx                                       │
│           Permissions: contents:read, packages:write                        │
│                                                                              │
│   Workflow uses token to:                                                   │
│   • Checkout code (needs contents:read)                                     │
│   • Push image to GHCR (needs packages:write)                              │
│                                                                              │
│   After workflow ends:                                                       │
│   ────────────────────                                                       │
│   Token = EXPIRED! Cannot be used again!                                    │
│   Security! 🔒                                                              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Custom Secrets (For Other Services)

```
If you need Docker Hub, AWS, etc.:

1. Go to: Repo → Settings → Secrets → Actions
2. Add secret: DOCKER_HUB_TOKEN = your-token
3. Use in workflow: ${{ secrets.DOCKER_HUB_TOKEN }}

ONLY YOU can see these secrets! Others see: ***
```

---

## 4. The ${{ }} Syntax

```yaml
username: ${{ github.actor }}
password: ${{ secrets.GITHUB_TOKEN }}
```

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   ${{ ... }} = Expression syntax                                            │
│                                                                              │
│   Like JavaScript template literals:                                         │
│   `Hello ${name}` → Hello John                                              │
│                                                                              │
│   In GitHub Actions:                                                         │
│   ${{ github.actor }} → pdheeraj99                                          │
│   ${{ github.repository }} → pdheeraj99/github-actions-learning            │
│   ${{ secrets.GITHUB_TOKEN }} → ghp_xxxxx (hidden!)                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🎯 Key Point

GITHUB_TOKEN is auto-provided! No setup needed. Use `secrets.` for custom secrets!
