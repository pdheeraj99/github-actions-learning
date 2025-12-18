# 🐳 01 - Docker Basics: What is Docker?

## 1. Docker Ante Enti? (What is Docker?)

Docker ante oka **containerization platform** - nee application ni dependencies tho kalipesi oka portable package ga create chesthundhi.

### 1.1 Real World Problem - "Works on my machine!" 😭

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   DOCKER LENI WORLD:                         DOCKER UNNA WORLD:              │
│   ═══════════════════                        ═══════════════════             │
│                                                                              │
│   Developer: "Naa machine lo work avuthundhi!"                              │
│   Tester: "Naa machine lo error vasthundhi!"                                │
│   Server: "Naa dheggara different version undhi!"                           │
│                                                                              │
│   Enduku ilaa avuthundhi?                    Docker tho solution:           │
│   ─────────────────────                      ────────────────────            │
│   Developer PC:                              Same Container:                 │
│   • Windows 11                               • Runs on Windows ✅            │
│   • Node.js v20                              • Runs on Linux ✅              │
│   • React 18.2.0                             • Runs on Cloud ✅              │
│                                              • Runs on any server ✅         │
│   Tester PC:                                                                 │
│   • Mac OS                                   Environment same!               │
│   • Node.js v18 ← Different!                 Bugs taguguthay!               │
│                                                                              │
│   Production Server:                                                         │
│   • Ubuntu Linux                                                             │
│   • Node.js v21 ← Different!                                                │
│                                                                              │
│   RESULT: Bugs! 🐛                           RESULT: Same everywhere! ✅     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 1.2 Simple Telugu Explanation

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   Docker ante oka DABBA (Container) lanti dhi:                              │
│   ═════════════════════════════════════════════                              │
│                                                                              │
│   🚢 Real World Example:                                                     │
│   ──────────────────────                                                     │
│   Ship lo goods transport chesthe...                                        │
│   • Different shapes, sizes unna goods                                      │
│   • Handling difficult                                                       │
│   • Damage avvachu                                                           │
│                                                                              │
│   Shipping Container tho:                                                    │
│   • All goods oka standard dabba lo                                         │
│   • Easy to load/unload                                                      │
│   • Ship, truck, train - anywhere fits!                                     │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   Docker Container = Same concept for SOFTWARE!                              │
│   ─────────────────────────────────────────────                              │
│   • Nee app + dependencies = oka package                                    │
│   • Ekkada ayina run avuthundhi                                              │
│   • "Naa machine lo work avuthundhi" problem solved!                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Image vs Container - Ee Rendu Different! 🎯

### 2.1 Simple ga cheppali ante

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   IMAGE (Chitra)                           CONTAINER (Dabba)                │
│   ══════════════                           ══════════════════                │
│                                                                              │
│   📀 Template / Blueprint                  🏃 Running instance              │
│   Static, disk lo stored                   Live, memory lo running          │
│   READ-ONLY                                READ-WRITE                        │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   TELUGU ANALOGIES:                                                          │
│   ─────────────────                                                          │
│                                                                              │
│   📀 CD/DVD album            →    🎵 Music player lo play chesthe           │
│      (stored)                        (running)                               │
│                                                                              │
│   📱 WhatsApp .apk file      →    📱 Install chesaka open chesthe           │
│      (download chesav)               (use chesthunav)                        │
│                                                                              │
│   🏠 House blueprint/plan    →    🏠 Actual house lo family living          │
│      (paper meeda)                   (real ga jeevistunnaru)                 │
│                                                                              │
│   📋 Biryani recipe          →    🍳 Actually cook chesina biryani          │
│      (book lo undhi)                 (plate lo serve chesav)                 │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   JAVA/SPRING BOOT analogy (neeku easy ga artham avvadam kosam):            │
│   ────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   class Car {                              Car myCar = new Car();            │
│       String color;                        myCar.color = "red";              │
│       void drive() {}                      myCar.drive();                    │
│   }                                                                          │
│       ↑                                           ↑                          │
│   IMAGE (Class definition)                 CONTAINER (Object instance)      │
│   Template maatrame                        Actually running                  │
│                                                                              │
│   Oka Image nunchi MULTIPLE Containers create cheyachu!                     │
│   (Oka class nunchi multiple objects create chestham kada, same concept!)  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.2 Visual: Image nunchi Container create avvadam

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   ONE IMAGE ───────────▶ MANY CONTAINERS                                    │
│   ═══════════════════════════════════════                                    │
│                                                                              │
│        ┌─────────────┐                                                       │
│        │   IMAGE     │                                                       │
│        │  "todo:v1"  │                                                       │
│        │  (template) │                                                       │
│        └──────┬──────┘                                                       │
│               │                                                              │
│               ▼  docker run todo:v1                                         │
│        ┌──────┴──────┐                                                       │
│        │             │                                                       │
│   ┌────▼────┐  ┌────▼────┐  ┌────▼────┐                                     │
│   │Container│  │Container│  │Container│                                     │
│   │   #1    │  │   #2    │  │   #3    │                                     │
│   │Port:8080│  │Port:8081│  │Port:8082│                                     │
│   └─────────┘  └─────────┘  └─────────┘                                     │
│                                                                              │
│   Same image nunchi 3 containers run chesthunnam!                           │
│   (Like 3 biryani plates from same recipe!)                                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### 2.3 Important Commands

| Action (Telugu) | Command |
|-----------------|---------|
| Images list chudu | `docker images` |
| Running containers chudu | `docker ps` |
| All containers (stopped kuda) | `docker ps -a` |
| Image nunchi container start | `docker run <image>` |
| Container aapeyyi | `docker stop <container_id>` |
| Container delete | `docker rm <container_id>` |
| Image delete | `docker rmi <image_id>` |

---

## 3. Hotel Room Analogy 🏨 (Chala Important!)

Ee analogy proper ga artham chesuko mama, Docker internals easy ga vasthay!

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   NEE WINDOWS PC = 🏠 INDEPENDENT HOUSE (Swaantha Illu)                     │
│   ══════════════════════════════════════════════════════                     │
│                                                                              │
│   • Own land undhi (Hardware - nee computer)                                │
│   • Own water connection (Full Operating System)                            │
│   • Own electricity (All drivers, tools installed)                          │
│   • Nee istam - emi install cheskunna okay                                  │
│   • HEAVY - full responsibility needi                                        │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   VIRTUAL MACHINE (VM) = 🏢 FULL APARTMENT (Apartment lo oka flat)          │
│   ═══════════════════════════════════════════════════════════════            │
│                                                                              │
│   • Apartment building lo oka full flat needi (Virtual hardware)            │
│   • Own kitchen, bathroom untay (Full OS - Ubuntu, Windows, etc.)           │
│   • Building infrastructure share chesthav (Physical hardware share)       │
│   • But flat lopala full setup kavali - HEAVY! (5GB OS install!)           │
│   • Start avvadam slow (2-3 minutes to boot)                                │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   DOCKER CONTAINER = 🏨 HOTEL ROOM (Hotel lo oka room)                       │
│   ══════════════════════════════════════════════════════                     │
│                                                                              │
│   • Just room kavali, adhe theeskunnav (Just your app!)                     │
│   • Hotel infrastructure share (Kitchen, water - Host OS kernel share)     │
│   • LIGHTWEIGHT - room ready ga undhi, just checkin cheyyi!                 │
│   • Fast check-in/check-out (1 SECOND lo start/stop!)                       │
│   • Nee baggage maatrame teeskovadame (Your app + minimal deps)             │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   COMPARISON:                                                                │
│   ───────────                                                                │
│                                                                              │
│   Independent House: Nuvvu anni build cheskovali     → 5GB+ (Full OS)       │
│   Apartment: Flat ready but full furniture kavali    → 3-5GB (VM)           │
│   Hotel Room: Room ready, just bags pettuko          → 200MB (Container!)   │
│                                                                              │
│   Boot Time:                                                                 │
│   House: Days to build                               → PC boot: 1-2 mins    │
│   Apartment: Hours to setup                          → VM boot: 2-3 mins    │
│   Hotel: Instant checkin!                            → Container: 1 SECOND! │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Kitchen & Dining Analogy 🍽️ (Docker Desktop Behavior)

Nuvvu `docker images` lo base images kanipinchaledu ani confuse ayyav kada? Idhi explain chesthundhi:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                                                                              │
│   NEE DOCKER DESKTOP = RESTAURANT! 🍽️                                       │
│   ═══════════════════════════════════                                        │
│                                                                              │
│   Restaurant lo TWO areas untay:                                             │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   🍽️ DINING TABLE (docker images command output)                            │
│   ────────────────────────────────────────────────                           │
│   • Guests ki serve chese FINAL DISHES ikkada untay                         │
│   • Nee built images: todo-frontend:v1, my-app:latest                       │
│   • Clean ga, presentable ga untundhi                                        │
│   • Guest-ready, polished!                                                   │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   🍳 KITCHEN (Build Cache - hidden area)                                    │
│   ──────────────────────────────────────                                     │
│   • "Raw Ingredients" ikkada store untay                                    │
│   • Base images: node:20-alpine, nginx:alpine                               │
│   • Guests ki direct ga chupinchamu (visible kaavu docker images lo)       │
│   • But available for cooking next dish! (Cached for speed!)               │
│                                                                              │
│   ════════════════════════════════════════════════════════════════════════  │
│                                                                              │
│   NEE DOUBT ANSWER:                                                          │
│   ─────────────────                                                          │
│   "node:20-alpine ekkada undhi? docker images lo kanipinchaledu!"           │
│                                                                              │
│   ANSWER: Kitchen lo undhi! (Build cache lo)                                │
│   Dining table (docker images) lo final dishes maatrame chupistham!         │
│   Raw ingredients (base images) kitchen lo (cache lo) undhi!                │
│                                                                              │
│   Check cheyyalante: docker buildx du                                       │
│   (Idi kitchen inventory check chesthe la - cache lo emi undo chepthundhi) │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Key Takeaways (Gurthu Pettuko!)

| Concept | Telugu Explanation |
|---------|-------------------|
| Docker | Nee app ni dabba lo petti ekkadikaina teeskelthav |
| Image | Blueprint/Recipe - disk lo stored, run avvadhu |
| Container | Actually running instance - memory lo live! |
| Why Docker? | "Naa machine lo work avuthundhi" problem END! |
| Speed | Container 1 second lo start (VM 2-3 minutes!) |
| Size | Container 200MB (VM 5GB!) |

---

## 👉 Next: [02-dockerfile-anatomy.md](./02-dockerfile-anatomy.md) - Dockerfile lo commands nerchukondham
