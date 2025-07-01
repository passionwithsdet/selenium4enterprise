# 🚀 Enterprise Selenium 4 Framework

A comprehensive, enterprise-grade automation framework built with Selenium 4, TestNG, and Java. This framework provides robust testing capabilities with parallel execution, cross-browser support, Selenium Grid integration, and comprehensive reporting.

## 📋 Table of Contents

- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Configuration](#-configuration)
- [Running Tests](#-running-tests)
- [Parallel Execution](#-parallel-execution)
- [Selenium Grid Setup](#-selenium-grid-setup)
- [Reports](#-reports)
- [Test Structure](#-test-structure)
- [Best Practices](#-best-practices)
- [Troubleshooting](#-troubleshooting)

## ✨ Features

### 🏗️ Core Framework
- ✅ **Maven-based** project structure
- ✅ **Page Object Model** implementation
- ✅ **Thread-safe** driver management
- ✅ **Multi-environment** configuration
- ✅ **Secure credential** handling

### 🌐 Browser Support
- ✅ **Chrome, Firefox, Safari, Edge** support
- ✅ **Headless mode** execution
- ✅ **Mobile emulation** testing
- ✅ **Cross-browser** compatibility

### ⚡ Performance & Scalability
- ✅ **Parallel execution** (up to 8 threads)
- ✅ **Selenium Grid** support
- ✅ **Docker container** integration
- ✅ **Load balancing** capabilities

### 📊 Reporting & Debugging
- ✅ **Allure Reports** (interactive HTML)
- ✅ **Extent Reports** (detailed analysis)
- ✅ **Custom logging** system
- ✅ **Screenshot on failure**
- ✅ **Video recording** support

### 🧪 Testing Capabilities
- ✅ **Data-driven testing** (Excel/JSON)
- ✅ **Accessibility testing**
- ✅ **Visual regression testing**
- ✅ **Localization testing**
- ✅ **Form validation**
- ✅ **API integration**

## 🔧 Prerequisites

### Required Software
- **Java 17** or higher
- **Maven 3.6** or higher
- **Git** for version control

### Browsers
- **Chrome** (latest version)
- **Firefox** (latest version)
- **Edge** (latest version)
- **Safari** (macOS only)

### Docker Requirements
- **Docker** and **Docker Compose** (for Selenium Grid)
  - [Docker Setup Guide](./DOCKER_SETUP_GUIDE.md)
  - Required for parallel testing and cross-browser execution
  - Enables containerized browser environments

### Optional
- **Node.js** (for Allure reports)
- **MongoDB** (for database testing)
- **Android Studio** (for mobile testing)

## 📦 Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd selenium4
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Installation
```bash
mvn test -Dtest=HomePageTest
```

## 🚀 Quick Start

### 1. Basic Test Execution
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=HomePageTest

# Run with specific browser
mvn test -Dbrowser=firefox
```

### 2. Parallel Execution
```bash
# Run tests in parallel (4 threads)
mvn test -Dparallel=true -DthreadCount=4

# Run with TestNG XML
mvn test -DsuiteXmlFile=testng.xml
```

### 3. Environment-Specific Execution
```bash
# Development environment
mvn test -Pdev

# QA environment
mvn test -Pqa

# Production environment
mvn test -Pprod
```

## ⚙️ Configuration

### Environment Configuration
The framework supports multiple environments through profile-based configuration:

```properties
# config.properties (main configuration)
base.url=https://demoqa.com
browser=chrome
parallel.execution=true
thread.count=4

# dev.properties (development)
environment=dev
headless=false
log.level=DEBUG

# qa.properties (QA)
environment=qa
headless=true
log.level=INFO

# prod.properties (production)
environment=prod
headless=true
grid.enabled=true
```

### Browser Configuration
```properties
# Supported browsers
browser=chrome|firefox|edge|safari

# Browser options
headless=false
implicit.wait=10
explicit.wait=20
page.load.timeout=30
```

## 🏃‍♂️ Running Tests

### 1. Local Execution
```bash
# Run all tests locally
mvn clean test

# Run specific test suite
mvn test -DsuiteXmlFile=testng.xml

# Run with custom parameters
mvn test -Dbrowser=firefox -Dheadless=true
```

### 2. Parallel Execution
```bash
# Parallel execution with 4 threads
mvn test -Dparallel=true -DthreadCount=4

# Parallel execution with TestNG XML
mvn test -DsuiteXmlFile=testng.xml
```

### 3. Cross-Browser Testing
```bash
# Run tests on multiple browsers
mvn test -Dbrowser=chrome
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
mvn test -Dbrowser=safari
```

## ⚡ Parallel Execution

### TestNG XML Configuration
```xml
<suite name="Parallel Suite" parallel="methods" thread-count="4">
    <test name="Test Suite">
        <classes>
            <class name="tests.HomePageTest"/>
            <class name="tests.ElementsFeatureTest"/>
        </classes>
    </test>
</suite>
```

### Maven Configuration
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
    </configuration>
</plugin>
```

### Best Practices
- ✅ Use thread-safe utilities
- ✅ Avoid static variables
- ✅ Use proper driver management
- ✅ Implement proper cleanup

## 🌐 Selenium Grid Setup

### 1. Docker Setup (Recommended)
```bash
# Start Selenium Grid with Chrome and Firefox
docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome:latest

# Start with multiple browsers
docker run -d -p 4444:4444 -p 7900:7900 --shm-size="2g" selenium/standalone-chrome:latest
docker run -d -p 4445:4444 -p 7901:7900 --shm-size="2g" selenium/standalone-firefox:latest
```

### 2. Manual Setup
```bash
# Download Selenium Server
wget https://github.com/SeleniumHQ/selenium/releases/download/selenium-4.19.1/selenium-server-4.19.1.jar

# Start Selenium Hub
java -jar selenium-server-4.19.1.jar hub

# Start Chrome Node
java -jar selenium-server-4.19.1.jar node --hub http://localhost:4444
```

### 3. Grid Configuration
```properties
# Enable grid
grid.enabled=true
grid.url=http://localhost:4444/wd/hub
grid.browser.count=4
grid.max.sessions=4
```

### 4. Run Tests on Grid
```bash
# Run with grid profile
mvn test -Pgrid

# Run with custom grid URL
mvn test -Dgrid.url=http://selenium-grid:4444/wd/hub
```

## 📊 Reports

### 1. Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report
mvn allure:serve

# Generate and open report
mvn allure:report allure:open
```

### 2. Extent Reports
```bash
# Reports are automatically generated
# Location: target/extent-reports/
open target/extent-reports/index.html
```

### 3. Custom Reports
```bash
# Screenshots location
target/screenshots/

# Logs location
target/logs/

# Test results
target/surefire-reports/
```

## 🧪 Test Structure

### Page Object Model
```
src/main/java/pages/
├── HomePage.java
├── ElementsPage.java
└── BasePage.java
```

### Test Classes
```
src/test/java/tests/
├── HomePageTest.java
├── ElementsFeatureTest.java
├── AdvancedElementsTest.java
├── ActionsClassTest.java
├── DataDrivenTest.java
└── MultiBrowserTest.java
```

### Utilities
```
src/main/java/utils/
├── ConfigReader.java
├── LogUtil.java
├── WaitUtil.java
├── ScreenShotUtil.java
├── DataProviderUtil.java
├── AccessibilityUtil.java
├── VisualTestingUtil.java
└── LocalizationUtil.java
```

## 📝 Best Practices

### 1. Test Organization
- ✅ Use descriptive test names
- ✅ Group related tests
- ✅ Implement proper setup/teardown
- ✅ Use data providers for multiple scenarios

### 2. Page Objects
- ✅ Keep locators in page objects
- ✅ Implement reusable methods
- ✅ Use PageFactory for initialization
- ✅ Follow single responsibility principle

### 3. Configuration
- ✅ Use environment-specific configs
- ✅ Encrypt sensitive data
- ✅ Use system properties for overrides
- ✅ Implement proper logging

### 4. Reporting
- ✅ Add meaningful test descriptions
- ✅ Include screenshots on failure
- ✅ Log important steps
- ✅ Use proper assertion messages

## 🔧 Troubleshooting

### Common Issues

#### 1. Driver Issues
```bash
# Clear driver cache
mvn clean
rm -rf ~/.cache/selenium

# Update WebDriverManager
mvn dependency:resolve
```

#### 2. Parallel Execution Issues
```bash
# Reduce thread count
mvn test -DthreadCount=2

# Check for thread safety
# Ensure no static variables in test classes
```

#### 3. Grid Connection Issues
```bash
# Check grid status
curl http://localhost:4444/status

# Verify browser nodes
curl http://localhost:4444/grid/console
```

#### 4. Memory Issues
```bash
# Increase JVM memory
mvn test -Xmx2g -XX:MaxPermSize=512m

# Use headless mode
mvn test -Dheadless=true
```

### Debug Mode
```bash
# Enable debug logging
mvn test -Dlog.level=DEBUG

# Run single test with debug
mvn test -Dtest=HomePageTest -Dlog.level=DEBUG
```

## 📞 Support

### Getting Help
- 📧 **Email**: support@example.com
- 💬 **Slack**: #selenium-framework
- 📖 **Documentation**: [Wiki](https://github.com/your-repo/wiki)

### Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new features
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Happy Testing! 🚀** 