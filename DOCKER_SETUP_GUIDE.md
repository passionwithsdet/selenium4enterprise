# 🐳 Docker Setup Guide for Enterprise Selenium Framework

This guide will help you install and configure Docker for running Selenium Grid with our framework.

---

## 📋 Table of Contents

- [Why Docker?](#-why-docker)
- [Installation](#-installation)
- [Verification](#-verification)
- [Framework Integration](#-framework-integration)
- [Troubleshooting](#-troubleshooting)
- [Best Practices](#-best-practices)

---

## 🤔 Why Docker?

### **Benefits for Selenium Grid**
- ✅ **Consistent Environment**: Same setup across all machines
- ✅ **Easy Setup**: No manual browser/driver installation
- ✅ **Isolation**: Tests run in isolated containers
- ✅ **Scalability**: Easy to add more browser nodes
- ✅ **Debugging**: VNC access for visual debugging
- ✅ **Resource Management**: Controlled memory and CPU usage

### **Framework Requirements**
- **Selenium Grid**: Requires Docker for containerized browser nodes
- **Parallel Testing**: Docker enables multiple browser instances
- **Cross-Browser Testing**: Chrome, Firefox, Edge in containers
- **CI/CD Integration**: Docker works seamlessly with Jenkins/GitHub Actions

---

## 📦 Installation

### **Windows Installation**

#### Step 1: Download Docker Desktop
```bash
# Download from official website
https://www.docker.com/products/docker-desktop/
```

#### Step 2: Install Docker Desktop
1. Run the installer
2. Follow the installation wizard
3. Restart your computer when prompted
4. Start Docker Desktop

#### Step 3: Verify Installation
```bash
# Open Command Prompt or PowerShell
docker --version
docker-compose --version
```

### **macOS Installation**

#### Step 1: Install Docker Desktop
```bash
# Using Homebrew
brew install --cask docker

# Or download from official website
https://www.docker.com/products/docker-desktop/
```

#### Step 2: Start Docker Desktop
1. Open Docker Desktop from Applications
2. Wait for Docker to start (whale icon in menu bar)
3. Accept terms and conditions

#### Step 3: Verify Installation
```bash
# Open Terminal
docker --version
docker-compose --version
```

### **Linux Installation (Ubuntu/Debian)**

#### Step 1: Update Package Index
```bash
sudo apt-get update
```

#### Step 2: Install Prerequisites
```bash
sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
```

#### Step 3: Add Docker's Official GPG Key
```bash
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
```

#### Step 4: Set Up Stable Repository
```bash
echo \
  "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

#### Step 5: Install Docker Engine
```bash
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
```

#### Step 6: Install Docker Compose
```bash
sudo curl -L "https://github.com/docker/compose/releases/download/v2.20.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### Step 7: Add User to Docker Group
```bash
sudo usermod -aG docker $USER
# Log out and log back in for changes to take effect
```

#### Step 8: Verify Installation
```bash
docker --version
docker-compose --version
```

---

## ✅ Verification

### **Step 1: Check Docker Installation**
```bash
# Check Docker version
docker --version

# Check Docker Compose version
docker-compose --version

# Expected output:
# Docker version 20.10.x, build xxxxxxx
# Docker Compose version v2.x.x
```

### **Step 2: Test Docker Functionality**
```bash
# Run hello-world container
docker run hello-world

# Expected output:
# Hello from Docker!
# This message shows that your installation appears to be working correctly.
```

### **Step 3: Check Docker Service**
```bash
# Check if Docker daemon is running
docker info

# Check Docker images
docker images

# Check running containers
docker ps
```

---

## 🔧 Framework Integration

### **Step 1: Verify Framework Requirements**
```bash
# Navigate to framework directory
cd selenium4

# Check if docker-compose.yml exists
ls -la docker-compose.yml

# Check framework prerequisites
java -version
mvn --version
git --version
```

### **Step 2: Start Selenium Grid**
```bash
# Method 1: Using framework script (Recommended)
./run-tests.sh setup

# Method 2: Manual Docker Compose
docker-compose up -d

# Method 3: Check Grid status
curl http://localhost:4444/status
```

### **Step 3: Verify Grid Setup**
```bash
# Check running containers
docker ps

# Expected output:
# CONTAINER ID   IMAGE                    COMMAND   CREATED         STATUS         PORTS                    NAMES
# xxxxxxxxxx     selenium/hub:4.19.1      ...       ...            Up 2 minutes   0.0.0.0:4442-4444->4442-4444/tcp   selenium-hub
# xxxxxxxxxx     selenium/node-chrome:4.19.1 ...    ...            Up 2 minutes   0.0.0.0:7900->7900/tcp           selenium-chrome
# xxxxxxxxxx     selenium/node-firefox:4.19.1 ...   ...            Up 2 minutes   0.0.0.0:7901->7900/tcp           selenium-firefox
# xxxxxxxxxx     selenium/node-edge:4.19.1 ...      ...            Up 2 minutes   0.0.0.0:7902->7900/tcp           selenium-edge
```

### **Step 4: Access Grid Console**
- **Grid Console**: http://localhost:4444/grid/console
- **VNC Chrome**: http://localhost:7900
- **VNC Firefox**: http://localhost:7901
- **VNC Edge**: http://localhost:7902

---

## 🧪 Test Docker Setup

### **Step 1: Run Framework Tests**
```bash
# Run tests on Grid
./run-tests.sh grid

# Or using Maven
mvn test -Pgrid -Dbrowser=chrome
```

### **Step 2: Monitor Test Execution**
```bash
# Watch containers during test execution
watch -n 5 'docker ps'

# Check Grid console for active sessions
open http://localhost:4444/grid/console
```

### **Step 3: Verify Test Results**
```bash
# Check test results
ls -la target/surefire-reports/

# Open reports
./run-tests.sh report
```

---

## 🛠️ Troubleshooting

### **Issue 1: Docker Not Starting**
```bash
# Check Docker service status
sudo systemctl status docker

# Start Docker service
sudo systemctl start docker

# Enable Docker on boot
sudo systemctl enable docker
```

### **Issue 2: Permission Denied**
```bash
# Add user to docker group
sudo usermod -aG docker $USER

# Log out and log back in
# Or run this command to apply changes immediately
newgrp docker
```

### **Issue 3: Port Already in Use**
```bash
# Check what's using port 4444
sudo lsof -i :4444

# Kill the process
sudo kill -9 <PID>

# Or use different ports in docker-compose.yml
```

### **Issue 4: Insufficient Memory**
```bash
# Check available memory
free -h

# Increase Docker memory limit
# In Docker Desktop: Settings > Resources > Memory
# Set to at least 4GB for Grid testing
```

### **Issue 5: Grid Not Responding**
```bash
# Check container logs
docker-compose logs selenium-hub
docker-compose logs chrome
docker-compose logs firefox

# Restart Grid
docker-compose down
docker-compose up -d

# Check Grid status
curl http://localhost:4444/status
```

### **Issue 6: Browser Nodes Not Registering**
```bash
# Check network connectivity
docker network ls
docker network inspect selenium4_selenium-grid

# Restart specific node
docker-compose restart chrome
docker-compose restart firefox
```

---

## 📊 Performance Optimization

### **Memory Allocation**
```yaml
# In docker-compose.yml
services:
  chrome:
    shm_size: 2gb  # Shared memory for browser
    mem_limit: 2g  # Memory limit per container
```

### **Resource Limits**
```bash
# Run with resource limits
docker-compose up -d --scale chrome=2 --scale firefox=2

# Monitor resource usage
docker stats
```

### **Cleanup**
```bash
# Clean up unused containers
docker container prune

# Clean up unused images
docker image prune

# Clean up everything
docker system prune -a
```

---

## 🎯 Best Practices

### **1. Resource Management**
- ✅ Allocate sufficient memory (4GB+ for Grid)
- ✅ Use shared memory for browser containers
- ✅ Monitor resource usage during tests
- ✅ Clean up unused containers regularly

### **2. Network Configuration**
- ✅ Use custom networks for isolation
- ✅ Configure proper port mappings
- ✅ Test network connectivity between containers

### **3. Security**
- ✅ Don't run containers as root
- ✅ Use specific image versions
- ✅ Keep Docker images updated
- ✅ Scan images for vulnerabilities

### **4. Monitoring**
- ✅ Monitor container health
- ✅ Check logs regularly
- ✅ Use Docker stats for performance
- ✅ Set up alerts for failures

---

## 🔗 Useful Commands

### **Docker Management**
```bash
# List containers
docker ps -a

# List images
docker images

# View container logs
docker logs <container_name>

# Execute commands in container
docker exec -it <container_name> /bin/bash

# Stop all containers
docker stop $(docker ps -q)

# Remove all containers
docker rm $(docker ps -aq)
```

### **Grid Management**
```bash
# Start Grid
./run-tests.sh setup

# Stop Grid
./run-tests.sh stop-grid

# Check Grid status
curl http://localhost:4444/status

# View Grid console
open http://localhost:4444/grid/console
```

---

## 📚 Additional Resources

- [Docker Official Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Selenium Docker Images](https://github.com/SeleniumHQ/docker-selenium)
- [Framework Documentation](./README.md)

---

**Happy Docker Testing! 🐳🚀** 