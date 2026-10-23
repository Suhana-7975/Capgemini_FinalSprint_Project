package ecom.BITM.QADemo.objRepo;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class MyOrdersPage extends BasePage {

    private By myOrdersLink =
            By.xpath("//a[normalize-space()='My Orders']");

    public MyOrdersPage(WebDriver driver) {
        super(driver);
    }

    public void openMyOrders() {
        WebElement link = wait.until(ExpectedConditions.presenceOfElementLocated(myOrdersLink));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);

        wait.until(ExpectedConditions.visibilityOf(link));

        try {
            wait.until(ExpectedConditions.elementToBeClickable(myOrdersLink)).click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
    }

    public boolean isOrderDisplayed(String orderId) {
        if (orderId == null || orderId.trim().isEmpty()) {
            return false;
        }

        By orderLocator = By.cssSelector("[data-testid='order-number-" + orderId.trim() + "']");

        return isPresent(orderLocator, 5);
    }
}