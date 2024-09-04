import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DgMarketAutomationTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chromedriver.driver", "chromeDriver");
        ChromeDriver driver = new ChromeDriver(setUpChromeOptions());
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            driver.get("https://www.dgmarket.com/");
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("signin")));
            loginButton.click();
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
            emailField.sendKeys("azghar@gisfy.co.in");
            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("dgmarket");
            WebElement submitButton = driver.findElement(By.id("signin_submit"));
            submitButton.click();
            System.out.println("Login successful.");

            WebElement searchBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("topKeywordQuery")));
            searchBar.sendKeys("GIS");
            searchBar.sendKeys(Keys.ENTER);

            List<WebElement> titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.ln_notice_title a")));
            for (int i = 0; i < titles.size(); i++) {
                try {
                    titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.ln_notice_title a")));
                    WebElement title = titles.get(i);
                    title.click();

                    try {
                        WebElement biddingDocsByViewFullNotice = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/tender/85150760?full=t']")));
                        biddingDocsByViewFullNotice.click();
                        startGettingBiddingDocs(driver, wait);
                    } catch (Exception biddingException) {
                        System.out.println("View full notice not found...:" + biddingException.getMessage());
                        startGettingBiddingDocs(driver, wait);
                    }

                } catch (Exception titleException) {
                    System.out.println("Error in processing titles: " + titleException.getMessage());
                    driver.navigate().back();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static ChromeOptions setUpChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\Users\\pzdul\\Downloads");
        prefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        return options;
    }

    public static void startGettingBiddingDocs(ChromeDriver driver, WebDriverWait wait) {
        WebElement biddingDocs = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a")));
        biddingDocs.click();

        List<WebElement> downloadLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.boxUpload a")));
        for (int j = 0; j < downloadLinks.size(); j++) {
            try {
                downloadLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.boxUpload a")));
                WebElement download = downloadLinks.get(j);
                download.click();
                Thread.sleep(5000);
            } catch (Exception downloadException) {
                System.out.println("Error in downloading: " + downloadException.getMessage());
                driver.navigate().back();
            }
        }
        driver.navigate().back();
    }
}
