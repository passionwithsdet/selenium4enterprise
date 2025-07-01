pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9.6'
        jdk 'JDK-17'
    }
    
    environment {
        BROWSER = 'chrome'
        ENVIRONMENT = 'qa'
        PARALLEL_THREADS = '4'
        GRID_ENABLED = 'false'
        HEADLESS = 'true'
        REPORT_PATH = 'target/extent-reports/'
        ALLURE_RESULTS = 'target/allure-results/'
    }
    
    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge', 'safari'],
            description: 'Select browser for test execution'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'qa', 'prod'],
            description: 'Select environment for test execution'
        )
        booleanParam(
            name: 'PARALLEL_EXECUTION',
            defaultValue: true,
            description: 'Enable parallel test execution'
        )
        string(
            name: 'THREAD_COUNT',
            defaultValue: '4',
            description: 'Number of parallel threads'
        )
        booleanParam(
            name: 'GRID_ENABLED',
            defaultValue: false,
            description: 'Enable Selenium Grid execution'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests in headless mode'
        )
        booleanParam(
            name: 'GENERATE_REPORTS',
            defaultValue: true,
            description: 'Generate test reports'
        )
    }
    
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from ${env.BRANCH_NAME ?: 'main'}"
                    checkout scm
                }
            }
        }
        
        stage('Setup Environment') {
            steps {
                script {
                    echo "Setting up test environment..."
                    echo "Browser: ${params.BROWSER}"
                    echo "Environment: ${params.ENVIRONMENT}"
                    echo "Parallel: ${params.PARALLEL_EXECUTION}"
                    echo "Threads: ${params.THREAD_COUNT}"
                    echo "Grid: ${params.GRID_ENABLED}"
                    echo "Headless: ${params.HEADLESS}"
                    
                    // Set environment variables
                    env.BROWSER = params.BROWSER
                    env.ENVIRONMENT = params.ENVIRONMENT
                    env.PARALLEL_THREADS = params.THREAD_COUNT
                    env.GRID_ENABLED = params.GRID_ENABLED.toString()
                    env.HEADLESS = params.HEADLESS.toString()
                }
            }
        }
        
        stage('Start Selenium Grid') {
            when {
                expression { params.GRID_ENABLED == true }
            }
            steps {
                script {
                    echo "Starting Selenium Grid..."
                    sh 'docker-compose up -d'
                    
                    // Wait for Grid to be ready
                    sh '''
                        echo "Waiting for Selenium Grid to be ready..."
                        timeout 60 bash -c 'until curl -s http://localhost:4444/status | grep -q "ready"; do sleep 2; done'
                        echo "Selenium Grid is ready!"
                    '''
                }
            }
        }
        
        stage('Dependency Check') {
            steps {
                script {
                    echo "Checking project dependencies..."
                    sh 'mvn dependency:resolve'
                    sh 'mvn dependency:tree'
                }
            }
        }
        
        stage('Code Quality') {
            parallel {
                stage('SonarQube Analysis') {
                    steps {
                        script {
                            echo "Running SonarQube analysis..."
                            withSonarQubeEnv('SonarQube') {
                                sh 'mvn sonar:sonar'
                            }
                        }
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        script {
                            echo "Running security scan..."
                            sh 'mvn dependency:check'
                        }
                    }
                }
            }
        }
        
        stage('Unit Tests') {
            steps {
                script {
                    echo "Running unit tests..."
                    sh 'mvn test -Dtest=*UnitTest -DfailIfNoTests=false'
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                script {
                    echo "Running integration tests..."
                    sh 'mvn test -Dtest=*IntegrationTest -DfailIfNoTests=false'
                }
            }
        }
        
        stage('UI Tests') {
            parallel {
                stage('Chrome Tests') {
                    when {
                        expression { params.BROWSER == 'chrome' || params.BROWSER == 'all' }
                    }
                    steps {
                        script {
                            echo "Running Chrome tests..."
                            runUITests('chrome')
                        }
                    }
                }
                
                stage('Firefox Tests') {
                    when {
                        expression { params.BROWSER == 'firefox' || params.BROWSER == 'all' }
                    }
                    steps {
                        script {
                            echo "Running Firefox tests..."
                            runUITests('firefox')
                        }
                    }
                }
                
                stage('Edge Tests') {
                    when {
                        expression { params.BROWSER == 'edge' || params.BROWSER == 'all' }
                    }
                    steps {
                        script {
                            echo "Running Edge tests..."
                            runUITests('edge')
                        }
                    }
                }
            }
        }
        
        stage('Cross Browser Tests') {
            when {
                expression { params.BROWSER == 'all' }
            }
            steps {
                script {
                    echo "Running cross-browser tests..."
                    sh '''
                        mvn clean test -Pqa -Dbrowser=chrome -Dparallel=true -DthreadCount=${PARALLEL_THREADS} -Dheadless=${HEADLESS} -Dgrid.enabled=${GRID_ENABLED}
                        mvn clean test -Pqa -Dbrowser=firefox -Dparallel=true -DthreadCount=${PARALLEL_THREADS} -Dheadless=${HEADLESS} -Dgrid.enabled=${GRID_ENABLED}
                        mvn clean test -Pqa -Dbrowser=edge -Dparallel=true -DthreadCount=${PARALLEL_THREADS} -Dheadless=${HEADLESS} -Dgrid.enabled=${GRID_ENABLED}
                    '''
                }
            }
        }
        
        stage('Performance Tests') {
            steps {
                script {
                    echo "Running performance tests..."
                    sh 'mvn test -Dtest=*PerformanceTest -DfailIfNoTests=false'
                }
            }
        }
        
        stage('Generate Reports') {
            when {
                expression { params.GENERATE_REPORTS == true }
            }
            steps {
                script {
                    echo "Generating test reports..."
                    
                    // Generate Allure report
                    sh 'mvn allure:report'
                    
                    // Archive reports
                    archiveArtifacts artifacts: 'target/extent-reports/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/allure-results/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/screenshots/**/*', allowEmptyArchive: true
                    archiveArtifacts artifacts: 'target/logs/**/*', allowEmptyArchive: true
                    
                    // Publish Allure report
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
        
        stage('Quality Gates') {
            steps {
                script {
                    echo "Checking quality gates..."
                    
                    // Check test results
                    def testResults = currentBuild.getAction(hudson.tasks.junit.TestResultAction.class)
                    if (testResults) {
                        def totalTests = testResults.totalCount
                        def failedTests = testResults.failCount
                        def skippedTests = testResults.skipCount
                        def passRate = ((totalTests - failedTests - skippedTests) / totalTests) * 100
                        
                        echo "Test Results:"
                        echo "Total: ${totalTests}"
                        echo "Failed: ${failedTests}"
                        echo "Skipped: ${skippedTests}"
                        echo "Pass Rate: ${passRate}%"
                        
                        if (passRate < 80) {
                            error "Test pass rate (${passRate}%) is below threshold (80%)"
                        }
                    }
                }
            }
        }
        
        stage('Deploy Reports') {
            when {
                expression { params.GENERATE_REPORTS == true }
            }
            steps {
                script {
                    echo "Deploying reports..."
                    
                    // Deploy to web server (example)
                    sh '''
                        if [ -d "target/extent-reports" ]; then
                            echo "Deploying Extent reports..."
                            # Add deployment logic here
                        fi
                        
                        if [ -d "target/site/allure-maven-plugin" ]; then
                            echo "Deploying Allure reports..."
                            # Add deployment logic here
                        fi
                    '''
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo "Cleaning up..."
                
                // Stop Selenium Grid
                if (params.GRID_ENABLED) {
                    sh 'docker-compose down'
                }
                
                // Clean workspace
                cleanWs()
            }
        }
        
        success {
            script {
                echo "Pipeline completed successfully!"
                
                // Send success notification
                emailext (
                    subject: "Pipeline SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        Pipeline: ${env.JOB_NAME}
                        Build: #${env.BUILD_NUMBER}
                        Status: SUCCESS
                        Browser: ${params.BROWSER}
                        Environment: ${params.ENVIRONMENT}
                        Duration: ${currentBuild.durationString}
                        
                        View results: ${env.BUILD_URL}
                    """,
                    to: 'team@example.com'
                )
            }
        }
        
        failure {
            script {
                echo "Pipeline failed!"
                
                // Send failure notification
                emailext (
                    subject: "Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        Pipeline: ${env.JOB_NAME}
                        Build: #${env.BUILD_NUMBER}
                        Status: FAILED
                        Browser: ${params.BROWSER}
                        Environment: ${params.ENVIRONMENT}
                        Duration: ${currentBuild.durationString}
                        
                        View results: ${env.BUILD_URL}
                        Console log: ${env.BUILD_URL}console
                    """,
                    to: 'team@example.com'
                )
            }
        }
        
        unstable {
            script {
                echo "Pipeline is unstable!"
            }
        }
    }
}

// Helper function to run UI tests
def runUITests(browser) {
    sh """
        mvn clean test -P${params.ENVIRONMENT} \
        -Dbrowser=${browser} \
        -Dparallel=${params.PARALLEL_EXECUTION} \
        -DthreadCount=${params.THREAD_COUNT} \
        -Dheadless=${params.HEADLESS} \
        -Dgrid.enabled=${params.GRID_ENABLED} \
        -DsuiteXmlFile=testng.xml
    """
} 