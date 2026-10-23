package ecom.BITM.QADemo.objRepo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SignUpPage extends BasePage {

    private By signUpLink =
            By.cssSelector("[data-testid='navbar-signup-link']");

    private By email =
            By.xpath("//input[@placeholder='Enter your email' or @name='email' or @id='email']");

    private By username =
            By.xpath("//input[@placeholder='Enter your username' or @name='username' or @id='username']");

    private By password =
            By.xpath("//input[@placeholder='Enter your password' or @name='password' or @id='password']");

    private By confirmPassword =
            By.xpath("//input[@placeholder='Confirm your password' or @name='confirmPassword' or @id='confirmPassword']");

    private By phone =
            By.xpath("//input[@placeholder='Enter your phone number' or @name='phone' or @id='phone']");

    private By signUpButton =
            By.xpath("//button[normalize-space()='Sign Up' or normalize-space()='Create Account']");

    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    public void openSignUpPage() {
        click(signUpLink);
    }

    public WebElement getEmail() {
        return visible(email);
    }

    public WebElement getUsername() {
        return visible(username);
    }

    public WebElement getPassword() {
        return visible(password);
    }

    public WebElement getConfirmPassword() {
        return visible(confirmPassword);
    }

    public WebElement getPhone() {
        return visible(phone);
    }

    public void enterEmail(String value) {
        type(email, value);
    }

    public void enterUsername(String value) {
        type(username, value);
    }

    public void enterPassword(String value) {
        type(password, value);
    }

    public void enterConfirmPassword(String value) {
        type(confirmPassword, value);
    }

    public void enterPhone(String value) {
        type(phone, value);
    }

    public void clickSignUp() {
        click(signUpButton);
    }

    public void signUp(String e, String u, String p,
                       String confirm, String ph) {
        enterEmail(e);
        enterUsername(u);
        enterPassword(p);
        enterConfirmPassword(confirm);
        enterPhone(ph);
        clickSignUp();
    }
}