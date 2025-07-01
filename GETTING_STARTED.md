# 🚀 Getting Started: Enterprise Selenium Framework

Welcome! This guide will help you set up, execute, and analyze tests using the Enterprise Selenium Framework, even if you're a beginner.

---

## 1️⃣ Prerequisites
- **Java 17+**
- **Maven 3.6+**
- **Git**
- **Docker** and **Docker Compose** (for Selenium Grid)
  - [Complete Docker Setup Guide](./DOCKER_SETUP_GUIDE.md)
  - Required for Grid testing and parallel execution
- **MongoDB** (for DB tests, optional)
- **Node.js** (for Allure reports, optional)
- **Android Studio** (for mobile testing, optional)

---

## 2️⃣ Clone the Repository
```bash
git clone <your-repo-url>
cd selenium4
```

---

## 3️⃣ Install Dependencies
```bash
mvn clean install
```

---

## 4️⃣ Configuration
- Edit `src/main/resources/config.properties` for global settings.
- Use `dev.properties`, `qa.properties`, `prod.properties` for environment-specific overrides.
- Set browser, grid, parallel, and reporting options as needed.

---

## 5️⃣ Selenium Grid Setup
### a. **Local Grid with Docker**
```bash
./run-tests.sh setup
# or manually:
docker-compose up -d
```
- Grid Console: http://localhost:4444/grid/console

### b. **Stop Grid**
```bash
./run-tests.sh stop-grid
# or manually:
docker-compose down
```

---

## 6️⃣ Running Tests
### a. **Local Execution**
```bash
./run-tests.sh local
```

### b. **Parallel Execution**
```bash
./run-tests.sh parallel -t 4
```

### c. **Grid Execution**
```bash
./run-tests.sh grid -e qa
```

### d. **Specific Browser**
```bash
./run-tests.sh browser -b firefox
```

### e. **Rerun Failed Tests**
```bash
./run-tests.sh rerun-failed
```

---

## 7️⃣ API Testing (RestAssured)
- See `src/test/java/tests/RestAssuredDemoTest.java` for examples.
- Run API tests:
```bash
mvn test -Dtest=RestAssuredDemoTest
```

---

## 8️⃣ Database Testing (MongoDB)
- See `src/main/java/utils/MongoDbUtil.java` for utility.
- Add your MongoDB URI and DB name in your test.
- Example usage:
```java
MongoDbUtil db = new MongoDbUtil("mongodb://localhost:27017", "testdb");
// db.insertOne, db.find, db.updateOne, db.deleteOne
```

---

## 9️⃣ Centralized Test Data
- Place your test data in `src/main/resources/data/` as JSON.
- Use `TestDataManager.loadMap("data/yourfile.json")` or `loadList` in your tests.

---

## 🔟 Reports & Analysis
### a. **Extent HTML Report**
- After test run, open:
```bash
open target/extent-reports/index.html
```

### b. **Allure Report**
```bash
mvn allure:report
mvn allure:serve
```

### c. **Screenshots & Logs**
- Screenshots: `target/screenshots/`
- Logs: `target/logs/`

---

## 1️⃣1️⃣ Analyzing & Rerunning Failed Tests
- After a suite run, failed tests are listed in `target/surefire-reports/testng-failed.xml`.
- Rerun them:
```bash
./run-tests.sh rerun-failed
```

---

## 1️⃣2️⃣ Troubleshooting
- **Grid not starting?**
  - Check Docker is running.
  - Run `docker-compose ps` and `docker-compose logs`.
- **Browser not launching?**
  - Check browser driver versions in `config.properties`.
- **No report generated?**
  - Check `target/extent-reports/` and `target/allure-results/`.
- **MongoDB issues?**
  - Ensure MongoDB is running and URI is correct.
- **API test failures?**
  - Check endpoint URLs and network connectivity.

---

## 1️⃣3️⃣ CI/CD Integration
- Use the provided `Jenkinsfile` or `.github/workflows/ci-cd.yml` for automated pipeline execution.
- Reports and artifacts are archived after each run.

---

## 1️⃣4️⃣ Support & Contribution
- Raise issues or PRs on the repo.
- See `README.md` for more details and best practices.

---

**Happy Testing! 🚀** 