# 🌐 Selenium Grid Testing Guide

Complete guide for running tests with Selenium Grid and analyzing results.

## 📋 Table of Contents

- [Prerequisites](#-prerequisites)
- [Grid Setup](#-grid-setup)
- [Running Tests on Grid](#-running-tests-on-grid)
- [Analyzing Results](#-analyzing-results)
- [Troubleshooting](#-troubleshooting)
- [Advanced Configuration](#-advanced-configuration)

## 🔧 Prerequisites

### Required Software
- **Docker** and **Docker Compose**
- **Java 17** or higher
- **Maven 3.6** or higher
- **Git** for version control

### Verify Installation
```bash
# Check Docker
docker --version
docker-compose --version

# Check Java
java -version

# Check Maven
mvn --version
```

## 🚀 Grid Setup

### Method 1: Docker Compose (Recommended)

#### Step 1: Start Selenium Grid
```bash
# Navigate to project directory
cd selenium4

# Start Selenium Grid with all browsers
docker-compose up -d

# Verify Grid is running
curl http://localhost:4444/status
```

#### Step 2: Check Grid Console
- **Grid Console**: http://localhost:4444/grid/console
- **VNC Chrome**: http://localhost:7900
- **VNC Firefox**: http://localhost:7901
- **VNC Edge**: http://localhost:7902

### Method 2: Manual Setup

#### Step 1: Download Selenium Server
```bash
# Download Selenium Server
wget https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.19.1/selenium-server-4.19.1.jar

# Start Hub
java -jar selenium-server-4.19.1.jar hub

# In another terminal, start Chrome node
java -jar selenium-server-4.19.1.jar node --hub http://localhost:4444 --browser chrome

# In another terminal, start Firefox node
java -jar selenium-server-4.19.1.jar node --hub http://localhost:4444 --browser firefox
```

## 🏃‍♂️ Running Tests on Grid

### Method 1: Using the Script (Easiest)

#### Step 1: Setup Grid
```bash
# Setup Selenium Grid
./run-tests.sh setup

# Wait for Grid to be ready
sleep 15
```

#### Step 2: Run Tests on Grid
```bash
# Run all tests on Grid
./run-tests.sh grid

# Run with specific environment
./run-tests.sh grid -e qa

# Run with specific browser
./run-tests.sh grid -b chrome
```

### Method 2: Using Maven Commands

#### Step 1: Run Basic Grid Tests
```bash
# Run tests on Grid with Chrome
mvn clean test -Pgrid -Dbrowser=chrome

# Run tests on Grid with Firefox
mvn clean test -Pgrid -Dbrowser=firefox

# Run tests on Grid with Edge
mvn clean test -Pgrid -Dbrowser=edge
```

#### Step 2: Run Parallel Tests on Grid
```bash
# Run parallel tests on Grid
mvn clean test -Pgrid -Dparallel=true -DthreadCount=4

# Run specific test suite on Grid
mvn clean test -Pgrid -DsuiteXmlFile=testng.xml
```

#### Step 3: Run Cross-Browser Tests
```bash
# Run tests on multiple browsers simultaneously
mvn clean test -Pgrid -Dbrowser=chrome -Dparallel=true -DthreadCount=2 &
mvn clean test -Pgrid -Dbrowser=firefox -Dparallel=true -DthreadCount=2 &
mvn clean test -Pgrid -Dbrowser=edge -Dparallel=true -DthreadCount=2 &
wait
```

### Method 3: Using TestNG XML

#### Step 1: Run Grid Tests via TestNG
```bash
# Run Grid test suite
mvn clean test -DsuiteXmlFile=testng.xml -Pgrid

# Run with custom parameters
mvn clean test -DsuiteXmlFile=testng.xml -Pgrid -Dbrowser=chrome -DthreadCount=4
```

## 📊 Analyzing Results

### Step 1: Generate Reports

#### Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report (interactive)
mvn allure:serve

# Open Allure report in browser
mvn allure:report allure:open
```

#### Extent Reports
```bash
# Reports are automatically generated
# Open in browser
open target/extent-reports/index.html
```

### Step 2: Analyze Test Results

#### Check Test Output
```bash
# View test results
cat target/surefire-reports/testng-results.xml

# View test logs
cat target/surefire-reports/TestSuite.txt
```

#### Analyze Screenshots
```bash
# View failure screenshots
ls -la target/screenshots/

# Open screenshot directory
open target/screenshots/
```

### Step 3: Performance Analysis

#### Check Execution Time
```bash
# View test execution times
grep "execution time" target/surefire-reports/TestSuite.txt

# Analyze parallel execution performance
echo "Parallel execution analysis:"
echo "Total tests: $(grep -c 'test name=' target/surefire-reports/testng-results.xml)"
echo "Passed: $(grep -c 'status="PASS"' target/surefire-reports/testng-results.xml)"
echo "Failed: $(grep -c 'status="FAIL"' target/surefire-reports/testng-results.xml)"
```

## 🔍 Detailed Analysis Steps

### Step 1: Grid Status Check
```bash
# Check Grid status
curl -s http://localhost:4444/status | jq '.'

# Check available nodes
curl -s http://localhost:4444/grid/api/hub | jq '.nodes[].browsers[]'

# Check active sessions
curl -s http://localhost:4444/grid/api/hub | jq '.slotCounts'
```

### Step 2: Test Execution Analysis
```bash
# Monitor test execution in real-time
watch -n 5 'curl -s http://localhost:4444/grid/api/hub | jq ".slotCounts"'

# Check browser utilization
echo "Browser utilization:"
curl -s http://localhost:4444/grid/api/hub | jq '.nodes[].browsers[] | {browser: .browserName, count: .count}'
```

### Step 3: Performance Metrics
```bash
# Calculate test execution metrics
echo "=== Test Execution Metrics ==="
echo "Total execution time: $(grep 'Total time' target/surefire-reports/TestSuite.txt | tail -1)"
echo "Average test time: $(grep 'execution time' target/surefire-reports/TestSuite.txt | awk '{sum+=$NF} END {print sum/NR}')"
echo "Tests per minute: $(grep -c 'test name=' target/surefire-reports/testng-results.xml | awk '{print $1/5}')"
```

## 🛠️ Troubleshooting

### Common Issues and Solutions

#### Issue 1: Grid Not Starting
```bash
# Check Docker status
docker ps

# Check Grid logs
docker-compose logs selenium-hub

# Restart Grid
docker-compose down
docker-compose up -d
```

#### Issue 2: Tests Failing on Grid
```bash
# Check browser node logs
docker-compose logs chrome
docker-compose logs firefox

# Check available browsers
curl -s http://localhost:4444/grid/api/hub | jq '.nodes[].browsers[]'

# Increase browser timeout
mvn test -Pgrid -Dgrid.browser.timeout=300
```

#### Issue 3: Performance Issues
```bash
# Reduce parallel threads
mvn test -Pgrid -DthreadCount=2

# Use headless mode
mvn test -Pgrid -Dheadless=true

# Increase memory allocation
mvn test -Pgrid -Xmx2g
```

#### Issue 4: Connection Issues
```bash
# Check Grid connectivity
curl http://localhost:4444/status

# Check firewall settings
sudo ufw status

# Test browser node connectivity
curl http://localhost:7900
```

## ⚙️ Advanced Configuration

### Custom Grid Configuration
```yaml
# docker-compose.yml custom configuration
services:
  selenium-hub:
    environment:
      - GRID_MAX_SESSION=20
      - GRID_BROWSER_TIMEOUT=600
      - GRID_TIMEOUT=600
      - GRID_CLEAN_UP_CYCLE=300
      - GRID_UNREGISTER_IF_STILL_DOWN_AFTER=300
      - GRID_NEW_SESSION_WAIT_TIMEOUT=300
      - GRID_ENABLE_LOGGING=true
      - GRID_LOG_LEVEL=DEBUG
```

### Browser-Specific Configuration
```yaml
  chrome:
    environment:
      - SE_NODE_MAX_SESSIONS=8
      - SE_NODE_OVERRIDE_MAX_SESSIONS=true
      - SE_VNC_NO_PASSWORD=1
      - SE_VNC_VIEW_ONLY=0
    shm_size: 4gb
```

### Performance Optimization
```bash
# Optimize for performance
mvn test -Pgrid \
  -Dparallel=true \
  -DthreadCount=8 \
  -Dheadless=true \
  -Dgrid.browser.timeout=300 \
  -Dgrid.new.session.wait.timeout=300
```

## 📈 Result Analysis Script

Create a script to analyze results automatically:

```bash
#!/bin/bash
# analyze-results.sh

echo "=== Selenium Grid Test Results Analysis ==="
echo ""

# Check Grid status
echo "1. Grid Status:"
curl -s http://localhost:4444/status | jq '.ready' && echo "Grid is ready" || echo "Grid is not ready"

# Test results summary
echo ""
echo "2. Test Results Summary:"
echo "Total tests: $(grep -c 'test name=' target/surefire-reports/testng-results.xml 2>/dev/null || echo '0')"
echo "Passed: $(grep -c 'status="PASS"' target/surefire-reports/testng-results.xml 2>/dev/null || echo '0')"
echo "Failed: $(grep -c 'status="FAIL"' target/surefire-reports/testng-results.xml 2>/dev/null || echo '0')"

# Performance metrics
echo ""
echo "3. Performance Metrics:"
echo "Execution time: $(grep 'Total time' target/surefire-reports/TestSuite.txt 2>/dev/null | tail -1 || echo 'N/A')"

# Screenshots
echo ""
echo "4. Screenshots:"
echo "Failure screenshots: $(ls target/screenshots/ 2>/dev/null | wc -l || echo '0')"

echo ""
echo "=== Analysis Complete ==="
```

## 🎯 Quick Commands Reference

### Setup and Start
```bash
# Quick setup
./run-tests.sh setup

# Start Grid manually
docker-compose up -d

# Check Grid status
curl http://localhost:4444/status
```

### Run Tests
```bash
# Quick Grid test
./run-tests.sh grid

# Parallel Grid test
mvn test -Pgrid -Dparallel=true -DthreadCount=4

# Cross-browser Grid test
mvn test -Pgrid -Dbrowser=chrome && mvn test -Pgrid -Dbrowser=firefox
```

### Analyze Results
```bash
# Generate reports
./run-tests.sh report

# View results
open target/extent-reports/index.html
mvn allure:serve

# Check Grid console
open http://localhost:4444/grid/console
```

## 🏆 Best Practices

1. **Always check Grid status** before running tests
2. **Use appropriate thread count** based on Grid capacity
3. **Monitor resource usage** during test execution
4. **Generate reports** after each test run
5. **Clean up resources** after testing
6. **Use headless mode** for better performance
7. **Implement proper error handling** in tests
8. **Monitor Grid logs** for issues

---

**Happy Grid Testing! 🚀** 