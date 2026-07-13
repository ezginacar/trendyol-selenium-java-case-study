# Trendyol Selenium Java Test Automation Framework

## Overview

This project is a UI test automation framework developed as part of a Selenium Java Case Study.

The framework is built with maintainability, readability and scalability in mind by applying Page Object Model (POM) and reusable automation components.

---

## Technology Stack

- Java 21
- Selenium 4
- TestNG
- Maven
- Apache POI
- Allure Report
- SLF4J + Log4j2

---

## Project Structure

```text

src

├── main

│   └── java

│       ├── driver       // Driver management

│       ├── helpers      // Reusable helper methods

│       ├── pages        // Page Object classes

│       └── utils        // Utility classes

│

└── test

    ├── java

    │   ├── listeners    // TestNG listeners

    │   └── tests        // Test classes

    │

    └── resources

        ├── config.properties

        └── TestData.xlsx

```

---

## Framework Features

- Selenium 4 with Java 21
- TestNG Test Execution
- Page Object Model (POM)
- Driver Factory Pattern
- Thread-safe Driver Management
- Explicit Wait Strategy
- Excel Based Test Data
- Environment Support
- Allure Reporting
- Screenshot on Failure
- Excel Test Execution Report
- Log4j2 Logging

---

## Test Data

Test data is stored in:

```
src/test/resources/TestData.xlsx
```

Environment selection is configured via:

```
config.properties
```

Example:

```
environment=Prod
browser=chrome
```

---

## Prerequisites

Before running the project, make sure the following are installed:

- JDK 21
- Maven 3.9+
- Google Chrome

---

## Running Tests

Run all tests:

```bash
mvn clean test
```

Or run individual TestNG test classes directly from IntelliJ IDEA.

---

## Test Reports

### Excel Test Report

After execution an Excel report is automatically generated under:

```
target/TestResults.xlsx
```

The report contains:

- Execution Time
- Environment
- Browser
- Test Name
- Duration
- Status
- Failure Reason

A Summary sheet is also generated including:

- Total Tests
- Passed
- Failed
- Skipped
- Success Rate
- Total Execution Time
- Average Test Duration

---

### Allure Report

Test results are generated under:

```
target/allure-results
```

To generate and open the report:

```bash
mvn allure:serve
```

Failed test screenshots are automatically attached to the Allure report.

---

## Design Principles

The framework follows:

- Page Object Model (POM)
- Single Responsibility Principle
- Reusable Helper Classes
- Centralized Driver Management
- Explicit Wait Strategy
- Clean Code Principles

---

## Author

Ezgi Nacar
Senior QA Automation Engineer
