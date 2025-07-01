# 📱 Mobile Testing Guide - Wikipedia App

This guide covers mobile testing implementation for the Wikipedia mobile app using Appium.

---

## 🎯 **Overview**

The mobile testing framework supports:
- **Android** (Emulator & Real Device)
- **iOS** (Simulator & Real Device)
- **Wikipedia Mobile App** testing
- **Cross-platform** test execution
- **Performance** monitoring
- **Gesture** testing

---

## 📋 **Prerequisites**

### **For Android Testing**
```bash
# Required Software
- Java 17+
- Android Studio
- Android SDK
- Appium Server
- Wikipedia APK file

# Environment Variables
export ANDROID_HOME=/path/to/android/sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### **For iOS Testing**
```bash
# Required Software (macOS only)
- Xcode
- iOS Simulator
- Appium Server
- Wikipedia IPA file

# Environment Variables
export XCODE_PATH=/Applications/Xcode.app
```

### **Appium Setup**
```bash
# Install Appium
npm install -g appium

# Install Appium Doctor
npm install -g appium-doctor

# Verify setup
appium-doctor --android
appium-doctor --ios
```

---

## 📱 **Wikipedia App Setup**

### **1. Download Wikipedia App**
```bash
# Android APK
# Download from: https://f-droid.org/packages/org.wikipedia/

# iOS IPA
# Download from App Store or build from source
```

### **2. Install App**
```bash
# Android
adb install wikipedia.apk

# iOS
# Install via Xcode or App Store
```

---

## ⚙️ **Configuration**

### **1. Update config.properties**
```properties
# Mobile Testing Configuration
mobile.platform=android
mobile.real.device=false
mobile.device.name=Android Device
mobile.device.udid=
mobile.emulator.name=Pixel_6_API_30
mobile.emulator.avd=Pixel_6_API_30
mobile.simulator.name=iPhone 14
mobile.simulator.version=16.0
mobile.app.path=/path/to/wikipedia.apk
mobile.no.reset=false
mobile.full.reset=true
mobile.automation.name=UiAutomator2
```

### **2. Environment-Specific Configs**
```properties
# dev.properties
mobile.platform=android
mobile.real.device=false

# qa.properties
mobile.platform=android
mobile.real.device=true

# prod.properties
mobile.platform=ios
mobile.real.device=true
```

---

## 🚀 **Running Mobile Tests**

### **1. Basic Mobile Test Execution**
```bash
# Run all mobile tests
./run-tests.sh mobile

# Run specific test class
mvn test -Dtest=WikipediaMobileTest

# Run specific test method
mvn test -Dtest=WikipediaMobileTest#testSearchFunctionality
```

### **2. Platform-Specific Execution**
```bash
# Android Emulator
./run-tests.sh mobile-emulator

# Android Real Device
./run-tests.sh mobile-device

# iOS Simulator
./run-tests.sh mobile-ios
```

### **3. Maven Commands**
```bash
# Run with TestNG XML
mvn clean test -DsuiteXmlFile=testng-mobile.xml

# Run with custom parameters
mvn test -Dmobile.platform=android -Dmobile.real.device=false

# Run specific test groups
mvn test -Dgroups=basic,performance
```

---

## 🧪 **Test Scenarios**

### **1. Basic Functionality**
- ✅ App Launch
- ✅ Search Functionality
- ✅ Article Reading
- ✅ Navigation Menu

### **2. Advanced Features**
- ✅ Article Bookmarking
- ✅ Article Sharing
- ✅ Language Switching
- ✅ Content Validation

### **3. Performance Testing**
- ✅ App Launch Time
- ✅ Search Response Time
- ✅ Memory Usage
- ✅ Battery Consumption

### **4. Gesture Testing**
- ✅ Swipe Navigation
- ✅ Tap Interactions
- ✅ Pinch to Zoom
- ✅ Long Press

---

## 📊 **Test Reports**

### **1. Extent Reports**
```bash
# Location: target/extent-reports/index.html
# Features:
- Device information
- Screenshots on failure
- Performance metrics
- Test execution timeline
```

### **2. Allure Reports**
```bash
# Generate report
mvn allure:report

# Serve report
mvn allure:serve
```

### **3. Screenshots**
```bash
# Location: target/screenshots/
# Automatic capture on test failure
```

---

## 🔧 **Troubleshooting**

### **Common Issues**

#### **1. Appium Connection Issues**
```bash
# Check Appium server
appium --log appium.log

# Verify device connection
adb devices  # Android
xcrun simctl list devices  # iOS
```

#### **2. Device Not Found**
```bash
# Android
adb kill-server
adb start-server
adb devices

# iOS
xcrun simctl boot "iPhone 14"
```

#### **3. App Installation Issues**
```bash
# Uninstall and reinstall
adb uninstall org.wikipedia
adb install wikipedia.apk
```

#### **4. Element Not Found**
```bash
# Check element locators
# Update page objects
# Verify app version compatibility
```

### **Debug Commands**
```bash
# Enable Appium logs
mvn test -Dappium.log.level=DEBUG

# Enable device logs
adb logcat  # Android
xcrun simctl spawn booted log stream  # iOS
```

---

## 📱 **Device Management**

### **Android Emulator Setup**
```bash
# List available AVDs
emulator -list-avds

# Start emulator
emulator -avd Pixel_6_API_30

# Create new AVD
avdmanager create avd -n "Test_Device" -k "system-images;android-30;google_apis;x86"
```

### **iOS Simulator Setup**
```bash
# List available simulators
xcrun simctl list devices

# Boot simulator
xcrun simctl boot "iPhone 14"

# Install app
xcrun simctl install booted /path/to/wikipedia.ipa
```

---

## 🎯 **Best Practices**

### **1. Test Organization**
```java
// Group related tests
@Test(groups = "search")
public void testSearchFunctionality() { }

@Test(groups = "performance")
public void testAppPerformance() { }
```

### **2. Element Locators**
```java
// Use reliable locators
@AndroidFindBy(id = "search_container")
@iOSXCUITFindBy(accessibility = "Search Wikipedia")
private WebElement searchContainer;
```

### **3. Wait Strategies**
```java
// Use explicit waits
wait.until(ExpectedConditions.elementToBeClickable(element));

// Avoid Thread.sleep()
```

### **4. Test Data Management**
```java
// Use test data utilities
Map<String, Object> testData = TestDataManager.loadMap("data/search-terms.json");
```

---

## 📈 **Performance Monitoring**

### **1. Response Time Tracking**
```java
long startTime = System.currentTimeMillis();
// Perform action
long endTime = System.currentTimeMillis();
long responseTime = endTime - startTime;
```

### **2. Memory Usage**
```java
// Log memory usage
Runtime runtime = Runtime.getRuntime();
long memoryUsage = runtime.totalMemory() - runtime.freeMemory();
```

### **3. Battery Monitoring**
```java
// Monitor battery level (device-specific)
// Implement battery monitoring utilities
```

---

## 🔄 **CI/CD Integration**

### **1. Jenkins Pipeline**
```groovy
stage('Mobile Tests') {
    steps {
        sh './run-tests.sh mobile-emulator'
    }
    post {
        always {
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/extent-reports',
                reportFiles: 'index.html',
                reportName: 'Mobile Test Report'
            ])
        }
    }
}
```

### **2. GitHub Actions**
```yaml
- name: Run Mobile Tests
  run: |
    ./run-tests.sh mobile-emulator
  env:
    ANDROID_HOME: ${{ secrets.ANDROID_HOME }}
```

---

## 📚 **Additional Resources**

### **1. Appium Documentation**
- [Appium Java Client](http://appium.io/docs/en/about-appium/intro/)
- [Appium Capabilities](http://appium.io/docs/en/writing-running-appium/caps/)

### **2. Wikipedia App**
- [GitHub Repository](https://github.com/wikimedia/wikipedia-android)
- [F-Droid](https://f-droid.org/packages/org.wikipedia/)

### **3. Testing Tools**
- [Appium Inspector](http://appium.io/docs/en/2.0/tools/inspector/)
- [Android Studio](https://developer.android.com/studio)
- [Xcode](https://developer.apple.com/xcode/)

---

## 🎉 **Success Metrics**

### **Test Coverage**
- ✅ 90%+ feature coverage
- ✅ Cross-platform compatibility
- ✅ Performance benchmarks

### **Execution Time**
- ✅ Full suite: < 30 minutes
- ✅ Individual test: < 2 minutes
- ✅ Setup time: < 5 minutes

### **Reliability**
- ✅ 95%+ pass rate
- ✅ < 5% flaky tests
- ✅ Stable CI/CD pipeline

---

**Happy Mobile Testing! 📱🚀** 