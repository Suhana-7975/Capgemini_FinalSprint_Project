package ecom.BITM.QADemo.objRepo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    // Sign In page ke placeholder aur button text se
    private By username = By.xpath("//input[@placeholder='Enter your username or email']");
    private By password = By.xpath("//input[@placeholder='Enter your password']");

    // last() taaki navbar me "Sign In" ho to form wala button chune
    private By signInButton = By.xpath("(//button[normalize-space()='Sign In'])[last()]");

    private By createAccountLink = By.xpath("//*[normalize-space()='Create Account']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getUsername() {
        return visible(username);
    }

    public WebElement getPassword() {
        return visible(password);
    }

    public WebElement getSignInButton() {
        return clickable(signInButton);
    }

    public WebElement getCreateAccountLink() {
        return clickable(createAccountLink);
    }

    public void enterUsername(String user) {
        type(username, user);
    }

    public void enterPassword(String pass) {
        type(password, pass);
    }

    public void clickSignIn() {
        click(signInButton);
    }

    public void clickCreateAccount() {
        click(createAccountLink);
    }

    public void login(String user, String pass) {
        enterUsername(user);
        enterPassword(pass);
        clickSignIn();
    }
}