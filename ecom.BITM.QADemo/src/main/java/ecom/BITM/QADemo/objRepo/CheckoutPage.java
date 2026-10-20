package ecom.BITM.QADemo.objRepo;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutPage extends BasePage {

    private By firstNameField =
            By.cssSelector("[data-testid='checkout-first-name']");

    private By lastNameField =
            By.cssSelector("[data-testid='checkout-last-name']");

    private By addressField =
            By.cssSelector("[data-testid='checkout-address']");

    private By cardNumberField =
            By.cssSelector("[data-testid='checkout-card-number']");

    private By expiryField =
            By.cssSelector("[data-testid='checkout-expiry']");

    private By cvvField =
            By.cssSelector("[data-testid='checkout-cvv']");

    private By cardholderNameField =
            By.cssSelector("[data-testid='checkout-cardholder-name']");

    private By placeOrderButton =
            By.cssSelector("[data-testid='place-order-button']");

    private By checkoutErrorMessage =
            By.cssSelector("[data-testid='checkout-error-message']");

    private By validationMessage =
            By.cssSelector("[role='alert']");

    private By orderConfirmationHeading =
            By.cssSelector("[data-testid='order-confirmation-heading']");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

    public void enterAddress(String address) {
        type(addressField, address);
    }

    public void enterCardNumber(String cardNumber) {
        type(cardNumberField, cardNumber);
    }

    public void enterExpiry(String expiry) {
        type(expiryField, expiry);
    }

    public void enterCVV(String cvv) {
        type(cvvField, cvv);
    }

    public void enterCardholderName(String cardholderName) {
        type(cardholderNameField, cardholderName);
    }

    public void fillCheckoutForm(
            String firstName,
            String lastName,
            String address,
            String cardNumber,
            String expiry,
            String cvv,
            String cardholderName) {

        enterFirstName(firstName);
        enterLastName(lastName);
        enterAddress(address);
        enterCardNumber(cardNumber);
        enterExpiry(expiry);
        enterCVV(cvv);
        enterCardholderName(cardholderName);
    }

    public void clickPlaceOrder() {

        WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(
                        placeOrderButton));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                button);

        // Prefer a normal Selenium click.
        try {
            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            placeOrderButton)).click();

        } catch (ElementClickInterceptedException e) {

            // Use JavaScript only if the normal click is intercepted.
            WebElement currentButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            placeOrderButton));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    currentButton);
        }
    }

    public boolean isOrderConfirmationDisplayed() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            orderConfirmationHeading));
            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isValidationMessageDisplayed() {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            validationMessage));
            return true;

        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getCheckoutErrorMessage() {
        try {
            return visible(checkoutErrorMessage).getText();

        } catch (TimeoutException e) {
            return "";
        }
    }

    public boolean isCheckoutErrorDisplayed() {
        return isPresent(checkoutErrorMessage, 3);
    }
}