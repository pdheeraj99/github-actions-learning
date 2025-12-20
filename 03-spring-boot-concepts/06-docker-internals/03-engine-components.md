# ⚙️ Docker Engine Components: dockerd, containerd, runc

The three musketeers of container runtime!

---

## 🎯 Telugu Simple ga

> "dockerd = Hotel Manager - orders receive chesthadu"
> "containerd = Kitchen Manager - cooking manage chesthadu"  
> "runc = Cook - actually cooking chesthadu"

---

## 📊 The Complete Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           DOCKER ENGINE                                          │
│                                                                                  │
│   YOU: docker run nginx                                                          │
│              │                                                                   │
│              │ (1) Command goes to Docker CLI                                   │
│              ▼                                                                   │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │                        Docker CLI                                         │  │
│   │  Just a client - sends commands via REST API / Unix socket              │  │
│   └──────────────────────────────────┬───────────────────────────────────────┘  │
│                                      │                                           │
│                                      │ REST API / /var/run/docker.sock          │
│                                      ▼                                           │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │                        dockerd (Docker Daemon)                           │  │
│   │                                                                          │  │
│   │   🏨 THE HOTEL MANAGER                                                   │  │
│   │                                                                          │  │
│   │   • Listens on port 2375 (TCP) or /var/run/docker.sock (Unix)           │  │
│   │   • Receives ALL Docker commands from CLI                                │  │
│   │   • Manages: images, networks, volumes, plugins                          │  │
│   │   • Delegates container work to containerd                               │  │
│   │   • Provides Docker API                                                  │  │
│   │                                                                          │  │
│   └──────────────────────────────────┬───────────────────────────────────────┘  │
│                                      │                                           │
│                                      │ gRPC                                      │
│                                      ▼                                           │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │                        containerd                                        │  │
│   │                                                                          │  │
│   │   👨‍🍳 THE KITCHEN MANAGER                                               │  │
│   │                                                                          │  │
│   │   • Container lifecycle manager                                          │  │
│   │   • Pulls images from registries                                         │  │
│   │   • Creates containers (but doesn't run them directly)                  │  │
│   │   • Manages container storage                                            │  │
│   │   • Delegates low-level work to runc                                    │  │
│   │   • Industry standard (used by Kubernetes too!)                         │  │
│   │                                                                          │  │
│   └──────────────────────────────────┬───────────────────────────────────────┘  │
│                                      │                                           │
│                                      │ OCI Runtime Spec                         │
│                                      ▼                                           │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │                        runc (OCI Runtime)                                │  │
│   │                                                                          │  │
│   │   🧑‍🍳 THE COOK                                                          │  │
│   │                                                                          │  │
│   │   • Low-level container runtime                                          │  │
│   │   • Makes SYSTEM CALLS to Linux kernel                                  │  │
│   │   • Creates namespaces (PID, network, mount, etc.)                      │  │
│   │   • Sets up cgroups (CPU, memory limits)                                │  │
│   │   • Actually RUNS the container process                                 │  │
│   │   • Open Container Initiative (OCI) reference implementation            │  │
│   │                                                                          │  │
│   └──────────────────────────────────┬───────────────────────────────────────┘  │
│                                      │                                           │
│                                      │ System Calls                             │
│                                      ▼                                           │
│   ┌──────────────────────────────────────────────────────────────────────────┐  │
│   │                        LINUX KERNEL                                      │  │
│   │                                                                          │  │
│   │   🏗️ THE FOUNDATION                                                     │  │
│   │                                                                          │  │
│   │   • namespaces: Process isolation                                        │  │
│   │   • cgroups: Resource limits (CPU, RAM)                                 │  │
│   │   • overlayfs: Layered filesystem                                       │  │
│   │   • seccomp: Security profiles                                          │  │
│   │                                                                          │  │
│   └──────────────────────────────────────────────────────────────────────────┘  │
│                                                                                  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 🏨 Hotel Analogy in Detail

```
┌─────────────────────────────────────────────────────────────────┐
│                      DOCKER = HOTEL                             │
│                                                                  │
│  YOU (Guest) → "I want biryani for Room 101"                    │
│       │                                                          │
│       ▼                                                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  HOTEL MANAGER (dockerd)                                  │   │
│  │                                                           │   │
│  │  • Receives your order                                    │   │
│  │  • Knows which room you're in                            │   │
│  │  • Manages all room assignments                           │   │
│  │  • Delegates cooking to kitchen                          │   │
│  │  • "Kitchen, make biryani for Room 101"                  │   │
│  └──────────────────────────────────────────────────────────┘   │
│       │                                                          │
│       ▼                                                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  KITCHEN MANAGER (containerd)                             │   │
│  │                                                           │   │
│  │  • Gets ingredients ready (pulls images)                  │   │
│  │  • Assigns cook to make biryani                          │   │
│  │  • Manages all cooking stations                          │   │
│  │  • "Cook #3, make biryani now"                           │   │
│  └──────────────────────────────────────────────────────────┘   │
│       │                                                          │
│       ▼                                                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  COOK (runc)                                              │   │
│  │                                                           │   │
│  │  • Actually cooks the biryani!                           │   │
│  │  • Uses stove (kernel) directly                          │   │
│  │  • Knows the recipe (OCI spec)                           │   │
│  │  • Finishes and hands off                                │   │
│  └──────────────────────────────────────────────────────────┘   │
│       │                                                          │
│       ▼                                                          │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │  KITCHEN EQUIPMENT (Linux Kernel)                         │   │
│  │                                                           │   │
│  │  • Stove = namespaces (isolated cooking area)            │   │
│  │  • Gas meter = cgroups (resource limits)                 │   │
│  │  • Recipe book = overlayfs (layers)                      │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Responsibilities Breakdown

### dockerd (Docker Daemon)

| Responsibility | Details |
|---------------|---------|
| **API Server** | Listens on /var/run/docker.sock |
| **Image Management** | docker pull, docker images, docker rmi |
| **Network Management** | docker network create/connect |
| **Volume Management** | docker volume create/mount |
| **Plugin System** | docker plugin install |
| **Logs & Events** | docker logs, docker events |

```bash
# Check dockerd process
ps aux | grep dockerd

# dockerd listens on this socket
ls -la /var/run/docker.sock
# srw-rw---- 1 root docker 0 Dec 20 10:00 /var/run/docker.sock
```

### containerd

| Responsibility | Details |
|---------------|---------|
| **Image Pull** | Downloads from registries |
| **Container Create** | Sets up container from image |
| **Container Start/Stop** | Lifecycle management |
| **Storage** | Manages snapshots |
| **Tasks** | Running container instances |

```bash
# Check containerd process
ps aux | grep containerd

# containerd socket
ls -la /run/containerd/containerd.sock
```

### runc

| Responsibility | Details |
|---------------|---------|
| **Create namespaces** | PID, network, mount, user, UTS, IPC |
| **Setup cgroups** | CPU, memory, IO limits |
| **Execute process** | Actually starts container's main process |
| **Apply seccomp** | Security syscall filtering |

```bash
# runc is a binary, not a daemon
which runc
# /usr/bin/runc

# runc version
runc --version
# runc version 1.1.x
```

---

## 🔄 Command Flow: `docker run nginx`

```
STEP 1: You type command
─────────────────────────
$ docker run nginx

STEP 2: Docker CLI sends to dockerd
────────────────────────────────────
POST /containers/create HTTP/1.1
Host: /var/run/docker.sock
Body: { "Image": "nginx", ... }

STEP 3: dockerd processes request
──────────────────────────────────
• Checks if nginx image exists locally
• If not, pulls from Docker Hub
• Creates container config
• Calls containerd via gRPC

STEP 4: containerd creates container
───────────────────────────────────
• Prepares container filesystem (overlayfs)
• Creates container metadata
• Calls runc with OCI bundle

STEP 5: runc creates namespaces
───────────────────────────────
clone(CLONE_NEWPID | CLONE_NEWNET | CLONE_NEWNS | ...)
• Creates isolated PID namespace
• Creates isolated network namespace
• Creates isolated mount namespace

STEP 6: runc sets up cgroups
────────────────────────────
mkdir /sys/fs/cgroup/cpu/docker/<container_id>
echo 50000 > /sys/fs/cgroup/cpu/docker/<container_id>/cpu.cfs_quota_us
• Sets CPU limits
• Sets memory limits

STEP 7: runc executes container process
───────────────────────────────────────
execve("/usr/sbin/nginx", ["nginx", "-g", "daemon off;"], env)
• Container is now running!
• nginx process starts inside isolated environment
```

---

## 🔧 Key Linux Kernel Features

```
┌─────────────────────────────────────────────────────────────────┐
│                     LINUX KERNEL FEATURES                        │
│                                                                  │
│   NAMESPACES (Isolation)                                         │
│   ══════════════════════                                         │
│   • PID Namespace - Process sees only its own processes         │
│   • NET Namespace - Container has its own network stack         │
│   • MNT Namespace - Container has its own filesystem view       │
│   • UTS Namespace - Container has its own hostname              │
│   • IPC Namespace - Container has isolated shared memory        │
│   • USER Namespace - Container can have different user mappings │
│                                                                  │
│   CGROUPS (Resource Limits)                                      │
│   ═════════════════════════                                      │
│   • cpu - How much CPU time                                     │
│   • memory - How much RAM                                       │
│   • blkio - Disk I/O limits                                     │
│   • pids - Max number of processes                              │
│                                                                  │
│   OVERLAYFS (Layered Filesystem)                                │
│   ══════════════════════════════                                │
│   • Lower layers - Read-only image layers                       │
│   • Upper layer - Writable container layer                      │
│   • Merged view - What container sees                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🎬 Telugu Summary

```
Docker Engine = dockerd + containerd + runc

dockerd (Hotel Manager):
──────────────────────────
• API listen chesthundi
• "docker run" command receive chesthundi
• Images, networks, volumes manage chesthundi
• Actually container create cheyadu - delegate chesthundi

containerd (Kitchen Manager):
──────────────────────────────
• Container lifecycle manage chesthundi
• Images pull chesthundi
• Container filesystem prepare chesthundi
• runc ki work assign chesthundi

runc (Cook):
──────────────────
• Kernel tho directly maatladthundi
• Namespaces create chesthundi (isolation)
• Cgroups setup chesthundi (limits)
• Actually container PROCESS start chesthundi!

Linux Kernel (Kitchen Equipment):
──────────────────────────────────
• namespaces = isolation tech
• cgroups = resource limits
• overlayfs = layered filesystem
```

---

## ✅ Key Takeaways

1. **Docker is NOT one thing** - It's multiple components working together
2. **runc talks to kernel** - Makes syscalls for namespaces/cgroups
3. **containerd is industry standard** - Kubernetes uses it directly!
4. **dockerd is Docker-specific** - You can use containerd without dockerd

---

**Next: [../07-ghcr-and-artifacts/](../07-ghcr-and-artifacts/)** 👉
