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

    public static void main(String[] args) throws IOException {
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

            // Dismiss login popup
            try {
                WebElement closeBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".commonModal__close")));
                closeBtn.click();
            } catch (Exception ignored) {}

            // === Test Case: From Delhi to Mumbai ===
            System.out.println("Starting test case...");

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
            System.out.println("Browser closed. Test run finished.");
        }
    }

    public static void captureScreenshot(WebDriver driver, String path) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File(path);
        dest.getParentFile().mkdirs(); // Ensure folder exists
        FileUtils.copyFile(src, dest);
    }
}
