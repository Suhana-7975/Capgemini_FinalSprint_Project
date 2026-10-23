package ecom.BITM.QADemo.OrderManagement;
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
import ecom.BITM.QADemo.objRepo.MyOrdersPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;
import ecom.BITM.QADemo.utilities.FlowUtility;

public class MyOrdersTest extends BaseClass {

    @Test(priority = 1, groups = {"FT", "YT"})
    public void verifyMyOrdersPageOpens() {

        FlowUtility.loginAsValidUser(driver);

        MyOrdersPage myOrdersPage = new MyOrdersPage(driver);
        myOrdersPage.openMyOrders();

        Assert.assertTrue(
                FlowUtility.urlContains(driver, "orders", 10),"My Orders page did not open. URL: "+ driver.getCurrentUrl());

        Reporter.log("My Orders page opened successfully", true);
    }

    @Test(priority = 2, groups = {"FT", "IT", "ST", "YT"})
    public void verifyValidOrderIsDisplayed() {

        FlowUtility.loginAsValidUser(driver);

        MyOrdersPage myOrdersPage = new MyOrdersPage(driver);
        myOrdersPage.openMyOrders();

        String orderId =ExcelUtility.getCellData("MyOrdersData", 1, 1);

        Assert.assertTrue(
                myOrdersPage.isOrderDisplayed(orderId),
                "Valid order was not displayed: " + orderId);

        Reporter.log("Valid order is displayed: " + orderId, true);
    }

    // Negative test case: Invalid order ID
    @Test(priority = 3, groups = {"NT"})
    public void verifyInvalidOrderIsNotDisplayed() {

        FlowUtility.loginAsValidUser(driver);

        MyOrdersPage myOrdersPage = new MyOrdersPage(driver);
        myOrdersPage.openMyOrders();

        String invalidOrder =ExcelUtility.getCellData("MyOrdersData", 2, 1);

        boolean orderDisplayed = myOrdersPage.isOrderDisplayed(invalidOrder);

        // Screenshot for the negative test
        captureScreenshot("TC_ORDER_NEG_01");

        Assert.assertFalse(orderDisplayed,"Invalid order should not be displayed: " + invalidOrder);

        Reporter.log("Invalid order correctly not displayed: " + invalidOrder,true);
    }

    // Screenshot helper method
    private void captureScreenshot(String fileName) {

        try {
            File folder = new File(
                "C:\\Users\\ASUS\\eclipse-workspace\\ecom.BITM.QADemo\\screenshot");

            if (!folder.exists()) {
                throw new IOException("Screenshot folder not found: "+ folder.getAbsolutePath());
            }

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            File destination = new File(folder, fileName + ".png");

            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: " + destination.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Unable to save screenshot", e);
        }
    }
}
