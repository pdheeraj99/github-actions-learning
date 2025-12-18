# ============================================
# 🐳 DOCKERFILE - React Todo App
# ============================================
# This file tells Docker how to package your app
# 
# We use "Multi-Stage Build" - 2 stages:
#   Stage 1: Build the React app (heavy, with Node.js)
#   Stage 2: Serve the app (lightweight, with Nginx)
#
# Why multi-stage?
#   - Final image is MUCH smaller (from 1GB to ~25MB!)
#   - Production-ready with Nginx server
#   - No Node.js needed in production
# ============================================

# ====== STAGE 1: BUILD ======
# This stage builds our React app
FROM node:20-alpine AS builder

# Set working directory inside container
WORKDIR /app

# Copy package files first (for better caching)
# If package.json didn't change, Docker reuses cached layer
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy all source code
COPY . .

# Build the production bundle
# This creates the 'dist' folder with optimized files
RUN npm run build

# ====== STAGE 2: SERVE ======
# This stage creates the final lightweight image
FROM nginx:alpine

# Copy custom nginx config for SPA routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy built files from builder stage
# Only the 'dist' folder - no node_modules, no source code!
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose port 80 (standard HTTP port)
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
