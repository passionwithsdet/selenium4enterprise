#!/bin/bash

# Enterprise Selenium Framework - Test Runner Script
# This script provides easy commands to run tests with different configurations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Enterprise Selenium Framework - Test Runner"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  local           Run tests locally"
    echo "  parallel        Run tests in parallel"
    echo "  grid            Run tests on Selenium Grid"
    echo "  browser         Run tests on specific browser"
    echo "  report          Generate and open reports"
    echo "  clean           Clean project and reports"
    echo "  setup           Setup Selenium Grid with Docker"
    echo "  help            Show this help message"
    echo ""
    echo "Options:"
    echo "  -b, --browser   Browser to use (chrome, firefox, edge, safari)"
    echo "  -e, --env       Environment (dev, qa, prod)"
    echo "  -t, --threads   Number of threads for parallel execution"
    echo "  -h, --headless  Run in headless mode"
    echo ""
    echo "Examples:"
    echo "  $0 local"
    echo "  $0 parallel -t 4"
    echo "  $0 browser -b firefox"
    echo "  $0 grid -e qa"
    echo "  $0 report"
}

# Function to run tests locally
run_local() {
    print_status "Running tests locally..."
    mvn clean test
    print_success "Local tests completed!"
}

# Function to run tests in parallel
run_parallel() {
    local threads=${THREADS:-4}
    print_status "Running tests in parallel with $threads threads..."
    mvn clean test -Dparallel=true -DthreadCount=$threads
    print_success "Parallel tests completed!"
}

# Function to run tests on Selenium Grid
run_grid() {
    local env=${ENV:-qa}
    print_status "Running tests on Selenium Grid (Environment: $env)..."
    mvn clean test -P$env -Dgrid.enabled=true
    print_success "Grid tests completed!"
}

# Function to run tests on specific browser
run_browser() {
    local browser=${BROWSER:-chrome}
    print_status "Running tests on $browser..."
    mvn clean test -Dbrowser=$browser
    print_success "Browser tests completed!"
}

# Function to generate and open reports
generate_reports() {
    print_status "Generating reports..."
    
    # Generate Allure report
    print_status "Generating Allure report..."
    mvn allure:report
    
    # Open Extent report
    print_status "Opening Extent report..."
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open target/extent-reports/index.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open target/extent-reports/index.html
    else
        start target/extent-reports/index.html
    fi
    
    print_success "Reports generated and opened!"
}

# Function to clean project
clean_project() {
    print_status "Cleaning project..."
    mvn clean
    rm -rf target/
    rm -rf logs/
    rm -rf screenshots/
    print_success "Project cleaned!"
}

# Function to setup Selenium Grid
setup_grid() {
    print_status "Setting up Selenium Grid with Docker..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
    
    print_status "Starting Selenium Grid..."
    docker-compose up -d
    
    print_status "Waiting for Grid to be ready..."
    sleep 10
    
    # Check if Grid is ready
    if curl -s http://localhost:4444/status | grep -q "ready"; then
        print_success "Selenium Grid is ready!"
        print_status "Grid Console: http://localhost:4444/grid/console"
        print_status "VNC Chrome: http://localhost:7900"
        print_status "VNC Firefox: http://localhost:7901"
        print_status "VNC Edge: http://localhost:7902"
    else
        print_error "Selenium Grid failed to start properly."
        exit 1
    fi
}

# Function to stop Selenium Grid
stop_grid() {
    print_status "Stopping Selenium Grid..."
    docker-compose down
    print_success "Selenium Grid stopped!"
}

# Parse command line arguments
COMMAND=""
BROWSER=""
ENV=""
THREADS=""
HEADLESS=""

while [[ $# -gt 0 ]]; do
    case $1 in
        local|parallel|grid|browser|report|clean|setup|stop-grid|help)
            COMMAND="$1"
            shift
            ;;
        -b|--browser)
            BROWSER="$2"
            shift 2
            ;;
        -e|--env)
            ENV="$2"
            shift 2
            ;;
        -t|--threads)
            THREADS="$2"
            shift 2
            ;;
        -h|--headless)
            HEADLESS="true"
            shift
            ;;
        *)
            print_error "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Set default values
BROWSER=${BROWSER:-chrome}
ENV=${ENV:-qa}
THREADS=${THREADS:-4}

# Execute command
case $COMMAND in
    local)
        run_local
        ;;
    parallel)
        run_parallel
        ;;
    grid)
        run_grid
        ;;
    browser)
        run_browser
        ;;
    report)
        generate_reports
        ;;
    clean)
        clean_project
        ;;
    setup)
        setup_grid
        ;;
    stop-grid)
        stop_grid
        ;;
    help|"")
        show_usage
        ;;
    *)
        print_error "Unknown command: $COMMAND"
        show_usage
        exit 1
        ;;
esac 