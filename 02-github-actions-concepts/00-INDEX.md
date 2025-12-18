# 📚 GitHub Actions Concepts - Index

## 🎯 Based on Our Discussion

These notes are created from our interactive discussion. Each file covers a specific topic that came up during our learning session.

---

## 📖 Reading Order

| # | File | Topic | Time |
|---|------|-------|------|
| 1 | [01-what-is-github-actions.md](./01-what-is-github-actions.md) | Basics, CI/CD, why we use it | 5 min |
| 2 | [02-workflow-file-location.md](./02-workflow-file-location.md) | .github/workflows/ and YAML | 3 min |
| 3 | [03-on-triggers.md](./03-on-triggers.md) | push, pull_request, workflow_dispatch, paths | 8 min |
| 4 | [04-github-runners.md](./04-github-runners.md) | Where code runs, ubuntu-latest, WSL vs Native | 5 min |
| 5 | [05-jobs-and-steps.md](./05-jobs-and-steps.md) | jobs, runs-on, permissions, steps | 8 min |
| 6 | [06-actions-marketplace.md](./06-actions-marketplace.md) | uses, actions/checkout, docker/build-push-action | 5 min |
| 7 | [07-secrets-and-tokens.md](./07-secrets-and-tokens.md) | GITHUB_TOKEN, secrets management | 5 min |
| 8 | [08-caching-in-ci.md](./08-caching-in-ci.md) | type=gha, buildx, why caching matters | 5 min |
| 9 | [09-complete-workflow-explained.md](./09-complete-workflow-explained.md) | Full workflow line-by-line | 10 min |
| 10 | [10-interview-questions.md](./10-interview-questions.md) | Common GitHub Actions interview Q&A | 10 min |
| 11 | [11-visual-workflow-guide.md](./11-visual-workflow-guide.md) | 🎬 Complete visual ASCII guide - MUST READ! | 15 min |

---

## 🧠 Your Key Doubts We Covered

- ✅ "GitHub lo Docker ekkada run avuthundhi?" → File 04
- ✅ "workflow_dispatch enduku?" → File 03
- ✅ "Actions ekkada nunchi vasthay?" → File 06
- ✅ "Caching ela work avuthundhi?" → File 08
- ✅ "GITHUB_TOKEN ekkada nunchi?" → File 07

---

## 💡 Quick Reference

```yaml
# Minimum workflow structure
name: My Workflow
on: push
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: echo "Hello!"
```
