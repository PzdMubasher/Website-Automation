import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DgMarketAutomationTest {
    public static void main(String[] args) {
        System.setProperty("webdriver.chromedriver.driver", "chromeDriver");
        String folderName = "Dg Market_" + getCurrentDateTimeStamp();
        ChromeDriver driver = new ChromeDriver(setUpChromeOptions(folderName));
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
            searchBar.submit();

            WebElement filterCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='IT services: consulting, software development, Internet and support tenders']")));
            filterCategory.click();

            Thread.sleep(3000);

            WebElement filterCategory1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[title='Software programming and consultancy services tenders']")));
            filterCategory1.click();

            boolean hasNextPage = true;

            while (hasNextPage) {

                List<WebElement> titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.ln_notice_title a")));
                System.out.println("Size of titles: " + titles.size());
                for (int i = 0; i < titles.size(); i++) {

                    if (i == titles.size() - 1)
                        System.out.println("This is last Title: " + (i + 1));

                    if (driver.getPageSource().contains("Confirm Form Resubmission")) {
                        driver.navigate().refresh();
                    }

                    try {
                        titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.ln_notice_title a")));
                        WebElement title = titles.get(i);
                        System.out.println("Title Name is: " + title.getText());
                        title.click();

                        String pageText = driver.findElement(By.tagName("body")).getText();
                        if (pageText.contains("GEM")) {
                            System.out.println("Page contains GEM.");
                        } else if (pageText.contains("EMD")) {
                            System.out.println("Page contains EMD.");
                        } else {
                            System.out.println("Page doesn't contain EMD or GEM.");
                            try {
                                WebElement biddingDocsByViewFullNotice = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(text(),'View full notice >>>')])[1]")));
                                biddingDocsByViewFullNotice.click();
                                startGettingBiddingDocs(driver, wait);
                            } catch (Exception biddingException) {
                                System.out.println("View full notice not found...:" + biddingException.getMessage());
                                startGettingBiddingDocs(driver, wait);
                            }
                        }

                    } catch (Exception titleException) {
                        System.out.println("Error in processing titles: " + titleException.getMessage());
                        driver.navigate().back();
                    }

                }

                driver.navigate().back();

                List<WebElement> nextButton = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("(//img[@class='toppg_forward_active'])[1]")));
                System.out.println("Next Button size: " + nextButton.size());
                if (nextButton.size() > 0 && nextButton.get(0).isEnabled()) {
                    System.out.println("Next button is clicked.");
                    nextButton.get(0).click();
                } else {
                    hasNextPage = false;
                }

//                WebElement pageNumbers = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id='ntc_tbl_tools_links'] li[class='toppg-t']")));
//                String pageNumberText = pageNumbers.getText();
//                String[] pNArray = pageNumberText.split(" "); //Example: Page 1 of 2
//                String currentPage = pNArray[1];
//                String lastPage = pNArray[3];
//                System.out.println("Pages: " + pageNumberText);
//                System.out.println("Current Page: " + currentPage);
//                System.out.println("Last Page: " + lastPage);
//                if (currentPage.equals(lastPage)) {
//                    System.out.println("Pages equal: " + pageNumberText);
//                    hasNextPage = false;
//                } else {
//                    System.out.println("Pages are not equal: " + pageNumberText);
//                    // Check if there is a next page
//                    WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//img[@class='toppg_forward_active'])[1]")));
//                    if (nextButton.isEnabled()) {
//                        System.out.println("Next button is clicked.");
//                        nextButton.click();
//                    } else {
//                        hasNextPage = false;
//                    }
//                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Process Ended.");
        }
    }

    private static ChromeOptions setUpChromeOptions(String folderName) {
        Map<String, Object> prefs = new HashMap<>();
        String customFolderPath = "C:\\Users\\pzdul\\Downloads\\" + folderName;
        File customFolder = new File(customFolderPath);
        if (!customFolder.exists()) {
            customFolder.mkdirs();
        }
        prefs.put("download.default_directory", customFolderPath);
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("profile.default_content_setting_values.popups", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.default_content_setting_values.protocol_handlers", 2);
        prefs.put("safebrowsing.enabled", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        return options;
    }


    private static String getCurrentDateTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HHmm dd-MM-yyyy");
        String formattedDateTime = now.format(dateTimeFormatter);
        System.out.println("Date Time stamp: " + formattedDateTime);
        return formattedDateTime;
    }

    public static void startGettingBiddingDocs(ChromeDriver driver, WebDriverWait wait) {
        WebElement biddingDocs = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a")));
        biddingDocs.click();

        List<WebElement> downloadLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[contains(text(),'download')]")));
        System.out.println("Download size: " + downloadLinks.size());
        for (int j = 0; j < downloadLinks.size(); j++) {
            try {
                downloadLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//a[contains(text(),'download')]")));
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
