package ecom.BITM.QADemo.Product;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import ecom.BITM.QADemo.baseClass.BaseClass;
import ecom.BITM.QADemo.objRepo.CartPage;
import ecom.BITM.QADemo.objRepo.ProductPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;
import ecom.BITM.QADemo.utilities.FlowUtility;

public class ProductTest extends BaseClass {

    @Test(priority = 1, groups = {"ST", "FT"})
    public void verifyProductIsDisplayed() {

        FlowUtility.loginAsValidUser(driver);

        ProductPage productPage = new ProductPage(driver);

        String product = ExcelUtility.getCellData("ProductData", 1, 1);

        Assert.assertTrue(
                productPage.isProductDisplayed(product),
                "Product not displayed: " + product);

        Reporter.log("Product displayed successfully: " + product, true);
    }

    @Test(priority = 2, groups = {"IT", "YT", "FT"})
    public void verifyAddToCartAndOpenCart() {

        FlowUtility.loginAsValidUser(driver);

        ProductPage productPage = new ProductPage(driver);

        String product =ExcelUtility.getCellData("ProductData", 1, 1);

        productPage.addToCart(product);
        productPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isProductInCart(product),
                "Added product is not in the cart: " + product);

        Reporter.log("Product added to cart successfully: " + product, true);
    }

    @Test(priority = 3, groups = {"NT"})
    public void verifyInvalidProductSearch() {

        FlowUtility.loginAsValidUser(driver);

        ProductPage productPage = new ProductPage(driver);

        String invalidProduct =
                ExcelUtility.getCellData("ProductData", 2, 1);

        Assert.assertFalse(
                productPage.isProductDisplayed(invalidProduct),
                "Invalid product should not be displayed: " + invalidProduct);

        Reporter.log("Invalid product correctly not displayed: " + invalidProduct, true);
    }
}
