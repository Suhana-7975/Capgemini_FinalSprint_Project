package ecom.BITM.QADemo.SignIn;

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
import ecom.BITM.QADemo.objRepo.LoginPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;
import ecom.BITM.QADemo.utilities.FlowUtility;

public class LoginTest extends BaseClass {

    private static final String LOGIN_URL = "https://qademo.com/login";

    private void captureScreenshot(String fileName) {
        try {
            File folder = new File(
                    "C:\\Users\\ASUS\\eclipse-workspace\\ecom.BITM.QADemo\\screenshot");

            if (!folder.exists()) {
                throw new IOException(
                        "Screenshot folder not found: "
                                + folder.getAbsolutePath());
            }

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            File destination = new File(folder, fileName + ".png");

            Files.copy(
                    source.toPath(),
                    destination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Screenshot saved: "
                    + destination.getAbsolutePath());

        } catch (IOException e) {
            throw new RuntimeException("Unable to save screenshot", e);
        }
    }

    @Test(priority = 1, groups = {"ST", "FT", "IT", "YT"})
    public void validLoginTest() {

        driver.get(LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                ExcelUtility.getCellData("LoginData", 1, 1),
                ExcelUtility.getCellData("LoginData", 1, 2));

        Assert.assertTrue(
                FlowUtility.urlChangedFrom(driver, LOGIN_URL, 15),
                "Valid login failed. Still on: " + driver.getCurrentUrl());

        Reporter.log("Valid login successful", true);
    }

    @Test(priority = 2, groups = {"NT", "YT"})
    public void invalidLoginTest() {

        driver.get(LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                ExcelUtility.getCellData("LoginData", 2, 1),
                ExcelUtility.getCellData("LoginData", 2, 2));

        Assert.assertFalse(
                FlowUtility.urlChangedFrom(driver, LOGIN_URL, 5),
                "Invalid credentials should not log in. URL: "
                        + driver.getCurrentUrl());

        Reporter.log("Invalid login test passed: login was rejected", true);
    }

    @Test(priority = 3, groups = {"NT"})
    public void blankLoginTest() {

        driver.get(LOGIN_URL);

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                ExcelUtility.getCellData("LoginData", 3, 1),
                ExcelUtility.getCellData("LoginData", 3, 2));

        // Screenshot for the negative test case
        captureScreenshot("TC_LOGIN_NEG_01");

        Assert.assertFalse(
                FlowUtility.urlChangedFrom(driver, LOGIN_URL, 5),
                "Blank credentials should not log in. URL: "
                        + driver.getCurrentUrl());

        Reporter.log("Blank login test passed: login was rejected", true);
    }
}