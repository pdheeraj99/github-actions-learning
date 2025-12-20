# 🧹 Cleanup Workflow: Delete Caches & Artifacts

Keep your GitHub Actions storage clean!

---

## 🎯 Telugu Simple ga

> "Garbage clear cheyyakunda house messy avuthundi!"
> "Same with GitHub - caches and artifacts clear cheyyi!"

---

## 🤔 Why Cleanup?

```
GitHub Free Tier Limits:
════════════════════════

Storage Limit: 500 MB for GitHub Packages (GHCR)
               2 GB for Actions artifacts
               10 GB for Actions cache

If you don't cleanup:
• Old caches fill up storage ❌
• Old artifacts waste space ❌
• Might hit free tier limits ❌
```

---

## 🔧 The Cleanup Workflow

```yaml
name: 🧹 Clear Caches & Artifacts
on:
  workflow_dispatch:
    inputs:
      delete_caches:
        description: 'Delete all caches'
        required: true
        default: true
        type: boolean
      delete_artifacts:
        description: 'Delete all artifacts'
        required: true
        default: true
        type: boolean

jobs:
  cleanup:
    runs-on: ubuntu-latest
    permissions:
      actions: write    # Required for deletion!
    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: 🗑️ Delete All Caches
        if: ${{ inputs.delete_caches }}
        run: |
          gh cache delete --all --repo ${{ github.repository }}
        env:
          GH_TOKEN: ${{ github.token }}

      - name: 🗑️ Delete All Artifacts
        if: ${{ inputs.delete_artifacts }}
        run: |
          ARTIFACTS=$(gh api repos/${{ github.repository }}/actions/artifacts --jq '.artifacts[].id')
          for ARTIFACT_ID in $ARTIFACTS; do
            gh api --method DELETE repos/${{ github.repository }}/actions/artifacts/$ARTIFACT_ID
          done
        env:
          GH_TOKEN: ${{ github.token }}
```

---

## 📋 Key Points

### 1. Permission Required

```yaml
permissions:
  actions: write    # ← Without this, deletion fails!
```

Error without permission:

```
HTTP 403: Resource not accessible by integration
```

### 2. Using GitHub CLI

```bash
# List caches
gh cache list --repo <owner>/<repo>

# Delete all caches
gh cache delete --all --repo <owner>/<repo>

# Delete specific cache
gh cache delete <cache-key> --repo <owner>/<repo>
```

### 3. Artifacts API

```bash
# List artifacts
gh api repos/<owner>/<repo>/actions/artifacts

# Delete specific artifact
gh api --method DELETE repos/<owner>/<repo>/actions/artifacts/<artifact-id>
```

---

## 📍 Where to See Storage Usage

```
Method 1: Repository Settings
─────────────────────────────
github.com/<user>/<repo>/settings
→ Scroll to "Actions" section
→ See cache usage

Method 2: Actions → Caches
─────────────────────────
github.com/<user>/<repo>/actions/caches
→ See all cached items with sizes
```

---

## 🎬 Telugu Summary

```
Why Cleanup?
────────────
• GitHub free tier limits untayi
• Old caches space occupy chesthay
• Old artifacts waste untundi

How to Cleanup:
───────────────
1. Workflow dispatch - Manual trigger
2. permissions: actions: write - Required!
3. gh cache delete --all - Delete caches
4. gh api DELETE artifacts - Delete artifacts

When to Run:
────────────
• Before fresh demo/testing
• When hitting storage limits
• Monthly cleanup
```

---

## ✅ Quick Reference

| Command | What it does |
|---------|--------------|
| `gh cache list` | List all caches |
| `gh cache delete --all` | Delete all caches |
| `gh api .../artifacts` | List artifacts |
| `gh api --method DELETE .../artifacts/ID` | Delete artifact |

---

**🎉 This completes the GHCR & Artifacts section!**

Go back to [00-INDEX.md](../00-INDEX.md) for the complete learning path 👉
