package ecom.BITM.QADemo.objRepo;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

public class ProductPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By cartIcon = By.xpath(
        "//a[@data-testid='navbar-cart-link' or contains(@href,'cart')]");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private By productTitle(String productName) {
        return By.xpath(
            "//*[starts-with(@data-testid,'product-name-') " +
            "and normalize-space(.)='" + productName + "']");
    }

    private By productCard(String productName) {
        return By.xpath(
            "//div[starts-with(@data-testid,'product-card-') " +
            "and .//*[starts-with(@data-testid,'product-name-') " +
            "and normalize-space(.)='" + productName + "']]");
    }

    private By addToCartButton() {
        return By.cssSelector(
            "button[data-testid^='product-add-to-cart-']");
    }

    private By goToCartButton() {
        return By.cssSelector(
            "button[data-testid^='product-go-to-cart-']");
    }

    private By removeFromCartButton() {
        return By.cssSelector(
            "button[data-testid^='product-remove-from-cart-']");
    }

    public boolean isProductDisplayed(String productName) {
        try {
            wait.until( ExpectedConditions.visibilityOfElementLocated(productTitle(productName) ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isOutOfStock(WebElement card) {
        String text = card.getText().toLowerCase();

        return text.contains("out of stock")
                || text.contains("sold out")
                || text.contains("unavailable")
                || text.matches("(?s).*\\b0\\s+in stock\\b.*");
    }

    public void addToCart(String productName) {

        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(productCard(productName)));

        // If the product is already in the cart, do not add it again.
        if (!card.findElements(goToCartButton()).isEmpty()
                || !card.findElements(removeFromCartButton()).isEmpty()) {

            System.out.println(productName + " is already in the cart.");
            return;
        }

        // Skip when the page explicitly indicates that stock is unavailable.
        if (isOutOfStock(card)) {
            throw new SkipException( productName + " is currently out of stock." );
        }

        List<WebElement> buttons =
            card.findElements(addToCartButton());

        if (buttons.isEmpty()) {
            throw new NoSuchElementException("Add to Cart button not found for: " + productName);
        }

        WebElement button = buttons.get(0);

        if (!button.isEnabled()) {
            throw new SkipException(productName + " cannot be added because its Add to Cart button is disabled.");
        }

        // Scroll to the button so it is not hidden near the bottom of the page.
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",button);

        wait.until(ExpectedConditions.elementToBeClickable(button));

        try {
            // Prefer a normal Selenium click.
            button.click();

        } catch (ElementClickInterceptedException e) {

            // Retry using JavaScript only if the normal click is intercepted.
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});",button);

            ((JavascriptExecutor) driver).executeScript( "arguments[0].click();", button);
        }

        System.out.println("Add to Cart action performed for: " + productName);
    }

    public void clickCartIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
    }
}
