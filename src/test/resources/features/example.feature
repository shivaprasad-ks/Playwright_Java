Feature: Example domain

  Scenario: Open example.com and check title
    Given I open the example page
    When I navigate to "https://www.orangehrm.com/"
    Then the page title should contain "Human Resources Management Software | HRMS | OrangeHRM"

