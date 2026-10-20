
package ecom.BITM.QADemo.objRepo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private By productName(String product) {
        return By.xpath(
            "//*[starts-with(@data-testid,'cart-item-name-') " +
            "and normalize-space(.)='" + product + "']"
        );
    }

    // Locate the cart row containing the exact product.
    private By cartItem(String product) {
        return By.xpath(
            "//*[starts-with(@data-testid,'cart-item-name-') " +
            "and normalize-space(.)='" + product + "']" +
            "/ancestor::*[@role='listitem'][1]"
        );
    }

    // This selector is searched relative to the selected cart item.
    private By removeButton() {
        return By.cssSelector(
            "button[data-testid^='cart-item-remove-']"
        );
    }

    private By emptyCartHeading() {
        return By.cssSelector(
            "[data-testid='cart-empty-heading']"
        );
    }

    private By cartItems() {
        return By.cssSelector(
            "[data-testid^='cart-item-'][role='listitem']"
        );
    }

    public boolean isProductInCart(String product) {
        try {
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    productName(product)
                )
            );
            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }

    public void removeProduct(String product) {

        WebElement item = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                cartItem(product)
            )
        );

        WebElement button = item.findElement(removeButton());

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            button
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(button)
        );

        try {
            button.click();

        } catch (ElementClickInterceptedException e) {
            // Retry using JavaScript only if the normal click is intercepted.
            WebElement currentItem = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    cartItem(product)
                )
            );

            WebElement currentButton =
                currentItem.findElement(removeButton());

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                currentButton
            );
        }

        // Wait for the selected product to disappear.
        if (!waitUntilGone(product, 6)) {

            // If it is still present, retry once using JavaScript.
            List<WebElement> remaining =
                driver.findElements(productName(product));

            if (!remaining.isEmpty()) {
                WebElement currentItem = driver.findElement(
                    cartItem(product)
                );

                WebElement currentButton =
                    currentItem.findElement(removeButton());

                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    currentButton
                );
            }

            ensureGone(product);
        }

        System.out.println(
            "Product removed from cart: " + product
        );
    }

    private boolean waitUntilGone(String product, int seconds) {
        try {
            return new WebDriverWait(
                driver, Duration.ofSeconds(seconds)
            ).until(
                ExpectedConditions.or(
                    ExpectedConditions.invisibilityOfElementLocated(
                        productName(product)
                    ),
                    ExpectedConditions.visibilityOfElementLocated(
                        emptyCartHeading()
                    )
                )
            );

        } catch (TimeoutException e) {
            return false;
        }
    }

    private void ensureGone(String product) {
        if (!waitUntilGone(product, 10)) {
            throw new AssertionError(
                "Remove action did not remove the product: " + product
            );
        }
    }

    public boolean isCartEmpty() {
        try {
            wait.until(
                ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(
                        emptyCartHeading()
                    ),
                    driver -> driver.findElements(cartItems()).isEmpty()
                )
            );
            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickCheckout() {
        By checkoutButton = By.xpath(
            "//button[contains(" +
            "translate(normalize-space(.)," +
            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
            "'checkout')]"
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(
                checkoutButton
            )
        ).click();
    }
}
