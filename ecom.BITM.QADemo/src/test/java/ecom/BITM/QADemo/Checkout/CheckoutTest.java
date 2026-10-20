
package ecom.BITM.QADemo.Checkout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import ecom.BITM.QADemo.baseClass.BaseClass;
import ecom.BITM.QADemo.objRepo.CartPage;
import ecom.BITM.QADemo.objRepo.CheckoutPage;
import ecom.BITM.QADemo.objRepo.ProductPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;
import ecom.BITM.QADemo.utilities.FlowUtility;

public class CheckoutTest extends BaseClass {

    private CheckoutPage openCheckoutPage() {

        FlowUtility.loginAsValidUser(driver);

        String product =
                ExcelUtility.getCellData("CartData", 1, 1);

        ProductPage productPage = new ProductPage(driver);

        productPage.addToCart(product);
        productPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isProductInCart(product),
                "Product not found in cart: " + product
        );

        cartPage.clickCheckout();

        return new CheckoutPage(driver);
    }

    private void fillCheckoutData(
            CheckoutPage checkoutPage, int row) {

        checkoutPage.fillCheckoutForm(
                ExcelUtility.getCellData("CheckoutData", row, 1),
                ExcelUtility.getCellData("CheckoutData", row, 2),
                ExcelUtility.getCellData("CheckoutData", row, 3),
                ExcelUtility.getCellData("CheckoutData", row, 5),
                ExcelUtility.getCellData("CheckoutData", row, 6),
                ExcelUtility.getCellData("CheckoutData", row, 7),
                ExcelUtility.getCellData("CheckoutData", row, 8)
        );
    }

    // Positive test case: Valid checkout
    @Test(priority = 1, groups = {"FT", "IT", "ST"})
    public void validCheckoutTest() {

        CheckoutPage checkoutPage = openCheckoutPage();

        fillCheckoutData(checkoutPage, 1);

        checkoutPage.clickPlaceOrder();

        Assert.assertTrue(
                checkoutPage.isOrderConfirmationDisplayed(),
                "Order confirmation was not displayed. Current URL: "
                        + driver.getCurrentUrl()
        );

        Reporter.log(
                "Valid checkout completed and order confirmation displayed",
                true
        );
    }

    // Negative test case: Blank shipping address
    @Test(priority = 2, groups = {"NT"})
    public void blankShippingAddressTest() {

        CheckoutPage checkoutPage = openCheckoutPage();

        // Excel row 2 contains a blank shipping address.
        fillCheckoutData(checkoutPage, 2);

        checkoutPage.clickPlaceOrder();

        captureScreenshot("TC_CHECKOUT_NEG_01");

        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout"),
                "Checkout should remain open when the address is blank."
        );

        Assert.assertTrue(
                checkoutPage.isValidationMessageDisplayed(),
                "Expected a validation message for the blank shipping address."
        );

        Reporter.log(
                "Blank shipping address correctly triggered validation",
                true
        );
    }

    // Negative test case: Blank first name and last name
    @Test(priority = 3, groups = {"NT"})
    public void blankNameTest() {

        CheckoutPage checkoutPage = openCheckoutPage();

        // Excel row 3 contains blank first name and last name.
        fillCheckoutData(checkoutPage, 3);

        checkoutPage.clickPlaceOrder();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("checkout"),
                "Checkout should remain open when the name fields are blank."
        );

        Assert.assertFalse(
                checkoutPage.isOrderConfirmationDisplayed(),
                "Order should not be completed when the name fields are blank."
        );

        Reporter.log(
                "Blank name test passed: order confirmation was not displayed",
                true
        );
    }

    // Screenshot helper method
    private void captureScreenshot(String fileName) {

        try {
            File folder = new File(
                "C:\\Users\\ASUS\\eclipse-workspace\\ecom.BITM.QADemo\\screenshot"
            );

            if (!folder.exists()) {
                throw new IOException(
                    "Screenshot folder not found: "
                            + folder.getAbsolutePath()
                );
            }

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            File destination = new File(
                    folder, fileName + ".png"
            );

            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "Screenshot saved: "
                            + destination.getAbsolutePath()
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to save screenshot", e
            );
        }
    }
}
