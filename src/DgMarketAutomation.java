import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DgMarketAutomation {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("webdriver.chromedriver.driver", "chromeDriver");
        ChromeDriver driver = new ChromeDriver(setUpChromeOptions());
        driver.get("https://www.dgmarket.com/tenders/list.do?sub=tenders-&locationISO=_s%2c_l%2c_b%2c_m%2c_n%2c_e%2caf%2cax%2cas%2cad%2cao%2cai%2caq%2cag%2cam%2caw%2cau%2caz%2cbs%2cbh%2cbb%2cby%2cbz%2cbj%2cbm%2cbt%2cbq%2cba%2cbw%2cbv%2cio%2cbn%2cbf%2cbi%2ccv%2ckh%2cky%2ccf%2ctd%2ccx%2ccc%2ckm%2ccd%2ccg%2cck%2ccr%2cci%2ccu%2ccw%2cdj%2cdm%2csv%2cgq%2cer%2cet%2cfk%2cfo%2cfj%2cgf%2cpf%2ctf%2cga%2cgm%2cge%2cgi%2cgl%2cgd%2cgp%2cgu%2cgg%2cgw%2cgy%2cht%2chm%2chn%2chk%2cin%2cid%2cim%2cjm%2cjp%2cje%2ckz%2cki%2ckp%2ckr%2ckv%2ckg%2cla%2cls%2clr%2cly%2cli%2cmo%2cmg%2cmw%2cmy%2cmv%2cml%2cmh%2cmq%2cmr%2cmu%2cyt%2cmx%2cfm%2cmd%2cmc%2cmn%2cme%2cms%2cmz%2cmm%2cnr%2cnp%2cnc%2cnz%2cni%2cne%2cng%2cnu%2cnf%2cmp%2cpw%2cps%2cpg%2cpy%2cpe%2cph%2cpn%2cpr%2cre%2cgs%2cbl%2csh%2ckn%2clc%2cmf%2cpm%2cvc%2cws%2csm%2cst%2csn%2csc%2csl%2csg%2csx%2csb%2cso%2css%2clk%2csd%2csr%2csj%2csz%2ctw%2ctj%2cth%2ctl%2ctg%2ctk%2cto%2ctt%2ctm%2ctc%2ctv%2cug%2cua%2cum%2cuz%2cvu%2cva%2cve%2cvn%2cvg%2cvi%2cwf%2ceh%2czw&language=en&noticeType=spn%2crei&d-446978-s=p&d-446978-p=2&d-446978-n=1&selPageNumber=2");//	    	driver.get("https://www.dgmarket.com/tenders/list.do?sub=info-communications-in-India-10&locationISO=in");
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.className("signin")).click();
        driver.findElement(By.name("username")).sendKeys("azghar@gisfy.co.in");
        driver.findElement(By.name("password")).sendKeys("dgmarket");
        driver.findElement(By.id("signin_submit")).click();
        System.out.println("Login successful");

        WebElement searchElements = driver.findElement(By.id("topKeywordQuery"));
        searchElements.sendKeys("GIS");
        searchElements.submit();
        List<WebElement> titles = driver.findElements(By.cssSelector("div.ln_notice_title a"));
        for (int i = 0; i < titles.size(); i++) {
            titles = driver.findElements(By.cssSelector("div.ln_notice_title a"));
            if (i >= titles.size()) {
                break;
            }

            WebElement title = titles.get(i);
            title.click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1:nth-child(1)")));
            //WebElement elementCheck = driver.findElement(By.xpath("//a[@href='/tender/85150760?full=t']"));

            try {
                WebElement we3 = driver.findElement(By.xpath("//a[@href='/tender/85150760?full=t']"));
                we3.click();
                WebElement biddingDocumentsLink = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a"));
                String check = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a")).getText();
                if (check.contains("Bidding")) {
                    biddingDocumentsLink.click();
                    startDownload(driver, wait);
                }
            } catch (Exception e) {
                System.out.println("Web element not found.");
                WebElement biddingDocumentsLink = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a"));
                String check = driver.findElement(By.xpath("//*[@id=\"content\"]/div[3]/ul/li[2]/a")).getText();
                if (check.contains("Bidding")) {
                    biddingDocumentsLink.click();
                    startDownload(driver, wait);
                }
            }
            driver.navigate().back();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".ui-tabs-nav")));
            driver.navigate().back();
            //wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.ln_notice_title a")));

        }

    }

    private static void startDownload(ChromeDriver driver, WebDriverWait wait) {
        List<WebElement> downloadLinks = driver.findElements(By.cssSelector("div.boxUpload a"));
        for (int i = 0; i < downloadLinks.size(); i++) {
            downloadLinks = driver.findElements(By.cssSelector("div.boxUpload a"));

            if (i >= downloadLinks.size()) {
                break;
            }

            WebElement downloadLink = downloadLinks.get(i);
            try {
                downloadLink.click();
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(3));

            } catch (TimeoutException ex) {
                System.out.println("Exception now..." + ex.getMessage());
                continue;
            } catch (StaleElementReferenceException es) {
                System.out.println("Encountered StaleElementReferenceException. Re-locating elements and continuing.");
                driver.navigate().back();
                continue;
                //startDownload(driver, iterator);
            } catch (Exception exception) {
                continue;
            }


        }
    }

    private static ChromeOptions setUpChromeOptions() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\Users\\pzdul\\Downloads");
        prefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

}
