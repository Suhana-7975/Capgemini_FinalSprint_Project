
package ecom.BITM.QADemo.SignUp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import org.testng.annotations.Test;

import ecom.BITM.QADemo.baseClass.BaseClass;
import ecom.BITM.QADemo.objRepo.SignUpPage;
import ecom.BITM.QADemo.utilities.ExcelUtility;

public class SignUpTest extends BaseClass {

    @Test
    public void validSignUpTest() throws InterruptedException {

        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.openSignUpPage();

        Thread.sleep(2000);

        String email = ExcelUtility.getCellData("SignUpData", 1, 2);
        String phone = ExcelUtility.getCellData("SignUpData", 1, 3);
        String password = ExcelUtility.getCellData("SignUpData", 1, 4);
        String username = ExcelUtility.getCellData("SignUpData", 1, 6);
        String confirmPassword = ExcelUtility.getCellData("SignUpData", 1, 7);

        signUpPage.enterEmail(email);
        signUpPage.enterUsername(username);
        signUpPage.enterPassword(password);
        signUpPage.enterConfirmPassword(confirmPassword);
        signUpPage.enterPhone(phone);

        Thread.sleep(1000);
        signUpPage.clickSignUp();
        Thread.sleep(3000);

        System.out.println("Valid signup test execution completed.");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        Reporter.log("Valid signup form submitted", true);
    }

    @Test
    public void invalidEmailSignUpTest() throws InterruptedException {

        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.openSignUpPage();

        Thread.sleep(2000);

        String email = ExcelUtility.getCellData("SignUpData", 2, 2);
        String phone = ExcelUtility.getCellData("SignUpData", 2, 3);
        String password = ExcelUtility.getCellData("SignUpData", 2, 4);
        String username = ExcelUtility.getCellData("SignUpData", 2, 6);
        String confirmPassword = ExcelUtility.getCellData("SignUpData", 2, 7);

        signUpPage.enterEmail(email);
        signUpPage.enterUsername(username);
        signUpPage.enterPassword(password);
        signUpPage.enterConfirmPassword(confirmPassword);
        signUpPage.enterPhone(phone);

        signUpPage.clickSignUp();

        Thread.sleep(2000);

        captureScreenshot("TC_SIGNUP_NEG_01");

        System.out.println("Invalid email test executed.");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        Reporter.log("Invalid email signup scenario executed", true);
    }

    @Test
    public void blankPhoneSignUpTest() throws InterruptedException {

        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.openSignUpPage();

        Thread.sleep(2000);

        String email = ExcelUtility.getCellData("SignUpData", 3, 2);
        String password = ExcelUtility.getCellData("SignUpData", 3, 4);
        String username = ExcelUtility.getCellData("SignUpData", 3, 6);
        String confirmPassword = ExcelUtility.getCellData("SignUpData", 3, 7);

        signUpPage.enterEmail(email);
        signUpPage.enterUsername(username);
        signUpPage.enterPassword(password);
        signUpPage.enterConfirmPassword(confirmPassword);

        signUpPage.clickSignUp();

        Thread.sleep(2000);

        System.out.println("Blank phone test executed.");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        Reporter.log("Blank phone signup scenario executed", true);
    }

    private void captureScreenshot(String fileName) {

        try {
            File folder = new File(
                "C:\\Users\\ASUS\\eclipse-workspace\\ecom.BITM.QADemo\\screenshot"
            );

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File source = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            File destination = new File(folder, fileName + ".png");

            Files.copy(
                source.toPath(),
                destination.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                "Screenshot saved: " + destination.getAbsolutePath()
            );

        } catch (IOException e) {
            throw new RuntimeException(
                "Unable to save screenshot", e
            );
        }
    }
}
