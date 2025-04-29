# MakeMyTrip Test Automation with Selenium

## Overview

This project contains an automated Selenium test for the **MakeMyTrip** website. The test case simulates a user journey of searching for a flight from Delhi to Mumbai, selecting a date, and then proceeding with the search. The test also handles the login popup, captures screenshots, and logs progress throughout the test.

## Prerequisites

Before running the test, ensure you have the following:
1. **Java** installed on your machine.
2. **Selenium WebDriver** set up for your browser. This project uses **ChromeDriver**.
3. **Apache Commons IO** library for file operations (e.g., saving screenshots).
4. **Chrome browser** installed.

To get started, download and set up the **ChromeDriver** that is compatible with your Chrome version.

## Dependencies

- **Selenium WebDriver** (for web automation)
- **Apache Commons IO** (for file handling)
- **ChromeDriver** (for running tests on Chrome)

You can add the necessary dependencies to your **pom.xml** if you're using Maven:

```xml
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>3.141.59</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.8.0</version>
</dependency>
```

## How It Works

### 1. Initial Setup
The program starts by setting the `webdriver.chrome.driver` system property with the path to `chromedriver`. It uses `ChromeOptions` to set various configurations, including the user agent and the exclusion of automation flags to make the browser look more like a real user.

### 2. Browser Initialization
The `ChromeDriver` is initialized with the options set earlier. The browser window is maximized.

A `WebDriverWait` is used to manage waits for elements to be visible or clickable on the webpage. This improves test stability by waiting for elements instead of relying on hardcoded delays.

### 3. Test Case Execution
The test opens the **MakeMyTrip** homepage.

If a login popup appears, it is dismissed.

The test then selects the following:
- **From city**: Delhi
- **To city**: Mumbai
- **Departure date**: The next day (calculated dynamically).

After selecting the cities and date, the program clicks the **Search** button.

### 4. Handle Results
If the search results page is successfully loaded, the program clicks a **proceed** button.

If the page does not load within the specified timeout, an error message is printed, and a screenshot is captured to show the current state of the page.

Screenshots are saved to a specified directory for verification.

### 5. Test Completion
After completing the steps, a screenshot of the final state of the page is captured.

The browser is closed, and a message indicating that the test run is finished is logged.

### 6. Error Handling
The program handles potential errors, including `WebDriverException` and `TimeoutException`.

In case of errors or unexpected results, appropriate messages are printed, and screenshots are taken to assist with debugging.

## Program Flow

Here is the sequence of operations performed by the program:

### 1. Setup and Initialize WebDriver:
- Set system property for `ChromeDriver`.
- Initialize `ChromeDriver` with options.

### 2. Navigate to MakeMyTrip:
- Open the **MakeMyTrip** website.

### 3. Dismiss Login Popup (if any):
- Wait for the login popup and close it if it appears.

### 4. Select From City (Delhi):
- Click on the 'From' input field.
- Enter "Delhi" and select the first suggestion.

### 5. Select To City (Mumbai):
- Click on the 'To' input field.
- Enter "Mumbai" and select the first suggestion.

### 6. Select Date (Tomorrow):
- Calculate tomorrow’s date dynamically.
- Click on the date in the calendar that corresponds to tomorrow.

### 7. Click on Search Button:
- Click the search button to initiate the flight search.

### 8. Handle Search Results:
- Wait for the results page to load. If the page loads, proceed with clicking the final button to continue.
- If the results page does not load, print an error and capture a screenshot.

### 9. Capture Screenshots:
- Take screenshots at various points in the test to document the state of the page.

### 10. Test Completion:
- Close the browser and log a message indicating that the test has finished.

## Screenshot Capture

The program captures screenshots at key points in the test to validate the execution:
- After the search is performed.
- If the search results page does not load correctly.

Screenshots are saved in the `screenshots/` directory with names reflecting the test scenario.

### Screenshot Capture
The program captures screenshots at key points in the test to validate the execution:
- After the search is performed. 
- If the search results page does not load correctly.

Screenshots are saved in the screenshots/ directory with names reflecting the test scenario.

### Example output:

```declarative
Starting test case...
The source location as 'Delhi' is selected
The destination location as 'Mumbai' is selected
Test completed and screenshot taken.
Browser closed. Test run finished.

```

### Handling Errors
The program handles errors gracefully by:
- Catching WebDriverException for issues with the WebDriver.
- Handling TimeoutException when waiting for elements to become available.
- Capturing screenshots for debugging purposes.