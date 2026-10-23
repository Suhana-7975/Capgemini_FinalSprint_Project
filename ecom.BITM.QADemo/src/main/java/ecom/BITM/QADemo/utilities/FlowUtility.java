package ecom.BITM.QADemo.utilities;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ecom.BITM.QADemo.objRepo.LoginPage;

public class FlowUtility {
public static void loginAsValidUser(WebDriver driver) {

        driver.get(PropertyUtility.getProperty("url") + "login");

        String user = ExcelUtility.getCellData("LoginData", 1, 1);
        String pass = ExcelUtility.getCellData("LoginData", 1, 2);

        new LoginPage(driver).login(user, pass);

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

  
    public static boolean urlChangedFrom(WebDriver driver, String oldUrl, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.not(ExpectedConditions.urlToBe(oldUrl)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


    public static boolean urlContains(WebDriver driver, String part, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.urlContains(part));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
