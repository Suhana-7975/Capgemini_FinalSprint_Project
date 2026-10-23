package ecom.BITM.QADemo.baseClass;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ecom.BITM.QADemo.utilities.PropertyUtility;

public class BaseClass {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BaseClass() {
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browserParameter) {

        String browser = browserParameter;

        if (browser == null || browser.trim().isEmpty()) {
            browser = PropertyUtility.getProperty("browser");
        }

        if (browser == null || browser.trim().isEmpty()) {
            browser = "chrome";
        }

        switch (browser.trim().toLowerCase()) {

            case "chrome":
                driver = new ChromeDriver();
                break;

            case "firefox":
                driver = new FirefoxDriver();
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            default:
                throw new RuntimeException("Unsupported browser: " + browser+ ". Use chrome, firefox, or edge.");
        }

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        String url = PropertyUtility.getProperty("url");
        driver.get(url);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();

        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected boolean isPresent(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.visibilityOfElementLocated(locator) );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        try {
            if (driver != null) {
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
