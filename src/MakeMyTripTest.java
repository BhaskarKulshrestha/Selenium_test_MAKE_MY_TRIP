import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MakeMyTripTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/Users/I528989/Downloads/chromedriver-mac-x64/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get("https://www.makemytrip.com");
            Thread.sleep(5000);

            // Dismiss login popup
            try {
                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From Delhi to Mumbai ===
            System.out.println("Starting Valid test case...");

            Thread.sleep(5000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='fromCity']"))).click();
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='From']")));
            fromInput.sendKeys("Delhi");
            System.out.println("The source location as 'Delhi' is selected");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='toCity']"))).click();
            WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='To']")));
            toInput.sendKeys("Mumbai");
            System.out.println("The destination location as 'Mumbai' is selected");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            // Choose a date
            // Get tomorrow's date formatted to match aria-label in MakeMyTrip
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dayOfWeek = tomorrow.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String month = tomorrow.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = dayOfWeek + " " + month + " " + tomorrow.getDayOfMonth() + " " + tomorrow.getYear();

            // Click on tomorrow's date in calendar
            WebElement tomorrowDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@aria-label='" + dateStr + "']")));
            tomorrowDate.click();

            // Click search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'primaryBtn')]")));
            searchButton.click();

            // Wait for result page or redirection
            try {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div/div[2]/div/div/div/div/div[2]/a")));
                proceedButton.click();
                System.out.println("Clicked final search button.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );
            } catch (TimeoutException e) {
                System.out.println("Search results page did not load in time.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );

                // Open the result URL in Safari browser
                try {
                    // Step 1: Open Safari with the result URL
                    Runtime.getRuntime().exec(new String[]{"open", "-a", "Safari", driver.getCurrentUrl()});
                    System.out.println("Result URL opened in Safari.");

                    // Step 2: Wait for 15 seconds
                    Thread.sleep(15000);

                    // Step 3: Close Safari after 15 seconds
                    Runtime.getRuntime().exec(new String[]{"osascript", "-e", "quit app \"Safari\""});
                    System.out.println("Safari closed automatically after 15 seconds.");
                } catch (IOException | InterruptedException ex) {
                    System.err.println("Failed to handle Safari automation: " + ex.getMessage());
                }

                captureScreenshot(driver, "screenshots/TC01_valid_city_search_no_results.png");
            }


            captureScreenshot(driver, "screenshots/TC01_valid_city_search.png");
            System.out.println("Test completed and screenshot taken.");



        } catch (WebDriverException e) {
            System.err.println("WebDriver Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.out.println("Browser closed. Test run finished for Valid Scenarios.");
        }


        System.out.println("===============================================================================");

        //Handling Invalid Case 2: From is NULL


        Thread.sleep(2000);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            driver.get("https://www.makemytrip.com");

            // Dismiss login popup
            try {
                Thread.sleep(5000);

                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From NULL to Mumbai ===
            System.out.println("Starting Invalid test case 1...");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='fromCity']"))).click();
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='From']")));
            fromInput.sendKeys("     ");
            System.out.println("The source location as 'NULL' is selected");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='toCity']"))).click();
            WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='To']")));
            toInput.sendKeys("Mumbai");
            System.out.println("The destination location as 'Mumbai' is selected");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            // Choose a date
            // Get tomorrow's date formatted to match aria-label in MakeMyTrip
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dayOfWeek = tomorrow.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String month = tomorrow.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = dayOfWeek + " " + month + " " + tomorrow.getDayOfMonth() + " " + tomorrow.getYear();

            // Click on tomorrow's date in calendar
            WebElement tomorrowDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@aria-label='" + dateStr + "']")));
            tomorrowDate.click();

            // Click search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'primaryBtn')]")));
            searchButton.click();

            // Wait for result page or redirection
            try {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div/div[2]/div/div/div/div/div[2]/a")));
                proceedButton.click();
                System.out.println("Clicked final search button.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );
            } catch (TimeoutException e) {
                System.out.println("Search results page did not load in time.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );

                // Open the result URL in Safari browser
                try {
                    // Step 1: Open Safari with the result URL
                    Runtime.getRuntime().exec(new String[]{"open", "-a", "Safari", driver.getCurrentUrl()});
                    System.out.println("Result URL opened in Safari.");

                    // Step 2: Wait for 15 seconds
                    Thread.sleep(15000);

                    // Step 3: Close Safari after 15 seconds
                    Runtime.getRuntime().exec(new String[]{"osascript", "-e", "quit app \"Safari\""});
                    System.out.println("Safari closed automatically after 15 seconds.");
                } catch (IOException | InterruptedException ex) {
                    System.err.println("Failed to handle Safari automation: " + ex.getMessage());
                }

                captureScreenshot(driver, "screenshots/TC02_Invalid_city_NULL_search_no_results.png");
            }


            captureScreenshot(driver, "screenshots/TC02_Invalid_city_NULL_search.png");
            System.out.println("Test completed and screenshot taken.");



        } catch (WebDriverException e) {
            System.err.println("WebDriver Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.out.println("Browser closed. Test run finished for invalid From city = NULL.");
        }

        System.out.println("===============================================================================");

        //Handling Invalid Case 3: From is INVALID CITY.

        Thread.sleep(2000);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            driver.get("https://www.makemytrip.com");

            // Dismiss login popup
            try {
                Thread.sleep(5000);

                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From 12345 to Mumbai ===
            System.out.println("Starting Invalid test case 2...");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='fromCity']"))).click();
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='From']")));
            fromInput.sendKeys("12345");
            System.out.println("The source location as '12345' is selected");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='toCity']"))).click();
            WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='To']")));
            toInput.sendKeys("Mumbai");
            System.out.println("The destination location as 'Mumbai' is selected");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            // Choose a date
            // Get tomorrow's date formatted to match aria-label in MakeMyTrip
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dayOfWeek = tomorrow.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String month = tomorrow.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = dayOfWeek + " " + month + " " + tomorrow.getDayOfMonth() + " " + tomorrow.getYear();

            // Click on tomorrow's date in calendar
            WebElement tomorrowDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@aria-label='" + dateStr + "']")));
            tomorrowDate.click();

            // Click search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'primaryBtn')]")));
            searchButton.click();

            // Wait for result page or redirection
            try {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div/div[2]/div/div/div/div/div[2]/a")));
                proceedButton.click();
                System.out.println("Clicked final search button.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );
            } catch (TimeoutException e) {
                System.out.println("Search results page did not load in time.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );

                // Open the result URL in Safari browser
                try {
                    // Step 1: Open Safari with the result URL
                    Runtime.getRuntime().exec(new String[]{"open", "-a", "Safari", driver.getCurrentUrl()});
                    System.out.println("Result URL opened in Safari.");

                    // Step 2: Wait for 15 seconds
                    Thread.sleep(15000);

                    // Step 3: Close Safari after 15 seconds
                    Runtime.getRuntime().exec(new String[]{"osascript", "-e", "quit app \"Safari\""});
                    System.out.println("Safari closed automatically after 15 seconds.");
                } catch (IOException | InterruptedException ex) {
                    System.err.println("Failed to handle Safari automation: " + ex.getMessage());
                }

                captureScreenshot(driver, "screenshots/TC03_Invalid_city_search_no_results.png");
            }


            captureScreenshot(driver, "screenshots/TC03_Invalid_valid_city_search.png");
            System.out.println("Test completed and screenshot taken.");



        } catch (WebDriverException e) {
            System.err.println("WebDriver Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.out.println("Browser closed. Test run finished for Invalid from city = INVALID_CITY.");
        }

        System.out.println("===============================================================================");

        //Handling Invalid Case 4: To is NULL


        Thread.sleep(2000);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            driver.get("https://www.makemytrip.com");

            // Dismiss login popup
            try {
                Thread.sleep(5000);

                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From NULL to Mumbai ===
            System.out.println("Starting Invalid test case 3...");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='fromCity']"))).click();
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='From']")));
            fromInput.sendKeys("Delhi");
            System.out.println("The source location as 'Delhi' is selected");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='toCity']"))).click();
            WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='To']")));
            toInput.sendKeys("        ");
            System.out.println("The destination location as 'NULL' is selected");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            // Choose a date
            // Get tomorrow's date formatted to match aria-label in MakeMyTrip
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dayOfWeek = tomorrow.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String month = tomorrow.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = dayOfWeek + " " + month + " " + tomorrow.getDayOfMonth() + " " + tomorrow.getYear();

            // Click on tomorrow's date in calendar
            WebElement tomorrowDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@aria-label='" + dateStr + "']")));
            tomorrowDate.click();

            // Click search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'primaryBtn')]")));
            searchButton.click();

            // Wait for result page or redirection
            try {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div/div[2]/div/div/div/div/div[2]/a")));
                proceedButton.click();
                System.out.println("Clicked final search button.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );
            } catch (TimeoutException e) {
                System.out.println("Search results page did not load in time.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );

                // Open the result URL in Safari browser
                try {
                    // Step 1: Open Safari with the result URL
                    Runtime.getRuntime().exec(new String[]{"open", "-a", "Safari", driver.getCurrentUrl()});
                    System.out.println("Result URL opened in Safari.");

                    // Step 2: Wait for 15 seconds
                    Thread.sleep(15000);

                    // Step 3: Close Safari after 15 seconds
                    Runtime.getRuntime().exec(new String[]{"osascript", "-e", "quit app \"Safari\""});
                    System.out.println("Safari closed automatically after 15 seconds.");
                } catch (IOException | InterruptedException ex) {
                    System.err.println("Failed to handle Safari automation: " + ex.getMessage());
                }

                captureScreenshot(driver, "screenshots/TC04_Invalid_city_search_no_results.png");
            }


            captureScreenshot(driver, "screenshots/TC04_Invalid_city_search.png");
            System.out.println("Test completed and screenshot taken.");



        } catch (WebDriverException e) {
            System.err.println("WebDriver Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.out.println("Browser closed. Test run finished for invalid To city = NULL.");
        }


        System.out.println("===============================================================================");


        //Handling Invalid Case 5: TO city is INVALID.


        Thread.sleep(2000);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        try {
            driver.get("https://www.makemytrip.com");

            // Dismiss login popup
            try {
                Thread.sleep(5000);

                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From NULL to Mumbai ===
            System.out.println("Starting Invalid test case...");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='fromCity']"))).click();
            WebElement fromInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='From']")));
            fromInput.sendKeys("Delhi");
            System.out.println("The source location as 'Delhi' is selected");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='toCity']"))).click();
            WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='To']")));
            toInput.sendKeys("12345");
            System.out.println("The destination location as '12345' is selected");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@role='listbox']//li[1]"))).click();

            // Choose a date
            // Get tomorrow's date formatted to match aria-label in MakeMyTrip
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dayOfWeek = tomorrow.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String month = tomorrow.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String dateStr = dayOfWeek + " " + month + " " + tomorrow.getDayOfMonth() + " " + tomorrow.getYear();

            // Click on tomorrow's date in calendar
            WebElement tomorrowDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@aria-label='" + dateStr + "']")));
            tomorrowDate.click();

            // Click search
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@class,'primaryBtn')]")));
            searchButton.click();

            // Wait for result page or redirection
            try {
                WebElement proceedButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("/html/body/div/div/div[2]/div/div/div/div/div[2]/a")));
                proceedButton.click();
                System.out.println("Clicked final search button.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );
            } catch (TimeoutException e) {
                System.out.println("Search results page did not load in time.");
                System.out.println("The Result URL is:" + driver.getCurrentUrl() );

                // Open the result URL in Safari browser
                try {
                    // Step 1: Open Safari with the result URL
                    Runtime.getRuntime().exec(new String[]{"open", "-a", "Safari", driver.getCurrentUrl()});
                    System.out.println("Result URL opened in Safari.");

                    // Step 2: Wait for 15 seconds
                    Thread.sleep(15000);

                    // Step 3: Close Safari after 15 seconds
                    Runtime.getRuntime().exec(new String[]{"osascript", "-e", "quit app \"Safari\""});
                    System.out.println("Safari closed automatically after 15 seconds.");
                } catch (IOException | InterruptedException ex) {
                    System.err.println("Failed to handle Safari automation: " + ex.getMessage());
                }

                captureScreenshot(driver, "screenshots/TC05_Invalid_city_search_no_results.png");
            }


            captureScreenshot(driver, "screenshots/TC05_Invalid_city_search.png");
            System.out.println("Test completed and screenshot taken.");



        } catch (WebDriverException e) {
            System.err.println("WebDriver Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
            System.out.println("Browser closed. Test run finished for invalid TO city = 12345.");
        }


        System.out.println("===============================================================================");

        System.out.println("All the positive and negative test cases completed!!");


    }

    public static void captureScreenshot(WebDriver driver, String path) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(path);
        dest.getParentFile().mkdirs(); // Ensure folder exists
        FileUtils.copyFile(src, dest);
    }
}
