
package ecom.BITM.QADemo.ShoppingCart;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import ecom.BITM.QADemo.baseClass.BaseClass;
import ecom.BITM.QADemo.objRepo.CartPage;
import ecom.BITM.QADemo.objRepo.ProductPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;
import ecom.BITM.QADemo.utilities.FlowUtility;

public class CartTest extends BaseClass {

    @Test(priority = 1, groups = {"FT", "IT"})
    public void verifyProductAddedToCart() {

        FlowUtility.loginAsValidUser(driver);

        String product =
                ExcelUtility.getCellData("CartData", 1, 1);

        System.out.println("Product to add: " + product);

        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart(product);
        productPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isProductInCart(product),
                "Product not found in cart: " + product
        );

        Reporter.log("Product added to cart successfully: " + product, true);
    }

    @Test(priority = 2, groups = {"YT"})
    public void verifyProductRemovedFromCart() {

        FlowUtility.loginAsValidUser(driver);

        String product =
                ExcelUtility.getCellData("CartData", 2, 1);

        System.out.println("Product from Excel: " + product);

        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart(product);
        productPage.clickCartIcon();

        CartPage cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.isProductInCart(product),
                "Product should be in cart before removal: " + product
        );

        System.out.println("Product found in cart: " + product);
        System.out.println("Attempting to remove product...");

        cartPage.removeProduct(product);

        System.out.println("Remove action completed.");

        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "Cart should be empty after removing product: " + product
        );

        Reporter.log("Product removed from cart successfully: " + product, true);
    }
}
