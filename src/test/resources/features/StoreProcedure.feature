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
      | customerNo  |
      | 141             |


