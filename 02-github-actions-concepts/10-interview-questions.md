# 🎯 10 - Interview Questions

## Common GitHub Actions Interview Q&A

---

### Q1: What is GitHub Actions?

**Answer**: GitHub Actions is a CI/CD platform built into GitHub that automates workflows like testing, building, and deploying code when events like push or pull request occur.

---

### Q2: Where do workflow files go?

**Answer**: `.github/workflows/` directory. Files must be `.yml` or `.yaml`.

---

### Q3: What triggers a workflow?

**Answer**: Events defined in `on:` section - push, pull_request, workflow_dispatch (manual), schedule (cron), etc.

---

### Q4: What is `runs-on`?

**Answer**: Specifies which runner machine to use - `ubuntu-latest`, `windows-latest`, `macos-latest`. GitHub provides these VMs for free.

---

### Q5: How do you cache Docker builds in CI?

**Answer**: Use `docker/build-push-action` with `cache-from: type=gha` and `cache-to: type=gha` to store cache in GitHub's cache storage.

---

### Q6: What is GITHUB_TOKEN?

**Answer**: Automatically generated token by GitHub for each workflow run. No manual setup needed. Has permissions specified in workflow's `permissions:` section.

---

### Q7: What's the difference between `uses:` and `run:`?

```yaml
# uses: = Use a pre-built action (someone else's code)
- uses: actions/checkout@v4

# run: = Run a shell command directly
- run: npm install
```

---

### Q8: What is workflow_dispatch?

**Answer**: Adds manual "Run workflow" button in GitHub UI. Useful for retrying failed builds, testing, or on-demand deployments.

---

### Q9: How do you pass secrets to workflows?

**Answer**: Store in repo Settings → Secrets → Actions. Use as `${{ secrets.MY_SECRET }}`. Cannot be printed in logs.

---

### Q10: What is the `${{ }}` syntax?

**Answer**: Expression syntax to access context variables:

- `${{ github.actor }}` - Username who triggered
- `${{ github.repository }}` - repo name
- `${{ secrets.XXX }}` - secrets
- `${{ steps.id.outputs.XXX }}` - step outputs

---

## 🎯 Most Important Points

1. **Location**: `.github/workflows/*.yml`
2. **Trigger**: `on: push/pull_request/workflow_dispatch`
3. **Machine**: `runs-on: ubuntu-latest`
4. **Secret**: `${{ secrets.GITHUB_TOKEN }}` is auto-provided
5. **Cache**: `type=gha` for GitHub Actions cache
