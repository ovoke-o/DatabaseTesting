#Database BDD Automation Framework

This is a Database test automation framework using **JAVA**, **TestNG**, **Cucumber**, and **Allure Reports** to Validate MySQL Stored Procedures, etc....

---

## 🧰 Tech Stack

- [JAVA 8 or later
- mysql-connector-j
- TestNG
- Cucumber (Gherkin BDD)
- Allure Reporting
- Intelli J IDE
- MySQL (AWS RDS)
---

## 📦 Installation Instructions

### 1. Prerequisites

- [JAVA 8 or later](https://www.oracle.com/africa/java/technologies/javase/jdk11-archive-downloads.html) installed and configured in system `PATH`
- Internet connection to download MAVEN plugins (mysql-connector-j, TestNG, Cucumber)
- Install MySQL
---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
https://github.com/ovoke-o/DatabaseTesting.git
cd DatabaseTesting
```

### 2. Setup the Environment

```MySQL
Download and Install MySQL (Download link: https://dev.mysql.com/downloads or use free tier aws RDS (MYSQL) 
Download MySQL Sample Database (classicmodels): https://www.mysqltutorial.org/wp-content/uploads/2023/10/mysqlsampledatabase.zip

```

---

## 🧪 Running Tests

### Run All Tests with tag via MAVEN 

```bash
mvn clean test -Dcucumber.filter.tags="@Regression"
```

---

## 🧪 Generate and open the report

### Open the report via bash / cmd

```bash
allure serve target/allure-results
```

---

## 🧾 Example Scenario

```gherkin
 @Regression
Feature: Validate MySQL Stored Procedures

  Background:
    Given I am connected to the database

  Scenario Outline: Verify that the SelectAllCustomers stored procedure exists
    When I check if the stored procedure "<procedureName>" exists
    Then the procedure "<procedureName>" should be available in the database
    Examples:
      | procedureName      |
      | SelectAllCustomers |

  Scenario Outline: Verify SelectAllCustomers returns correct data
    When I execute the SelectAllCustomers stored procedure
    Then the result should match the "<queryName>" query
    Examples:
      | queryName              |
      | SelectAllFromCustomers |

  Scenario Outline: Verify SelectAllCustomersByCity returns correct data
    When I execute the SelectAllCustomersByCity stored procedure with city "<cityName>"
    Then the result should match the "<queryName>" query
    Examples:
      | queryName                | cityName  |
      | selectAllCustomersByCity | Singapore |

  Scenario Outline: Verify SelectAllCustomersByCityAndPinCode returns correct data
    When I execute the SelectAllCustomersByCityAndPinCode stored procedure with city "<cityName>" and pin "<postalCode>"
    Then the result should match the "<queryName>" query
    Examples:
      | queryName                      | cityName  | postalCode |
      | selectAllCustomersByCityAndPin | Singapore | 079903     |

  Scenario Outline: Verify GetOrderByCustomer returns correct data
    When I execute the GetOrderByCustomer stored procedure and GetOrderByCustomerQuery with customer Number "<customerNo>"
    Then the stored procedure result should match the query result
    Examples:
      | customerNo |
      | 141        |


  Scenario Outline: Verify GetCustomerShipping returns correct data
    When I execute the GetCustomerShipping stored procedure and GetCustomerShippingQuery with customer Number "<customerNo>"
    Then the stored procedure result should match the query results
    Examples:
      | customerNo |
      | 112        |
      | 260        |
      | 353        |

    Examples:
      | queryName                      | cityName  | postalCode |
      | selectAllCustomersByCityAndPin | Singapore | 079903     |
