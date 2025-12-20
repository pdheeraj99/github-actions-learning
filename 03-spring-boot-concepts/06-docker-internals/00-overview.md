# 🐳 Docker Internals - Overview

Understanding Docker's architecture from the inside!

---

## 📚 This Folder Covers

| File | Topic | Time |
|------|-------|------|
| [01-architecture.md](./01-architecture.md) | Docker Desktop vs Docker Engine | 8 min |
| [02-wsl2-windows.md](./02-wsl2-windows.md) | How Docker runs on Windows | 7 min |
| [03-engine-components.md](./03-engine-components.md) | dockerd, containerd, runc | 10 min |

---

## 🎯 Why Learn This?

```
┌─────────────────────────────────────────────────────────────────┐
│  Most developers: "docker run works, that's enough"            │
│                                                                  │
│  Interview question: "How does Docker actually work?"           │
│                                                                  │
│  You after this folder: "Docker Engine consists of dockerd,    │
│  containerd, and runc. dockerd receives commands, containerd   │
│  manages container lifecycle, and runc makes system calls to   │
│  the Linux kernel for namespaces and cgroups..."               │
│                                                                  │
│  Interviewer: 🤯 "You're hired!"                                │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔑 Key Concepts Preview

| Concept | One-Line Telugu |
|---------|-----------------|
| **Docker Desktop** | Windows/Mac GUI app - whale icon in taskbar |
| **Docker Engine** | The actual container runtime (dockerd + containerd + runc) |
| **WSL 2** | Linux inside Windows - Docker engine runs here |
| **Hyper-V** | Windows virtualization - WSL 2 uses this |
| **dockerd** | Main daemon - receives your commands |
| **containerd** | Container lifecycle manager |
| **runc** | Low-level runtime - makes kernel system calls |

---

**Start with [01-architecture.md](./01-architecture.md)** 👉
