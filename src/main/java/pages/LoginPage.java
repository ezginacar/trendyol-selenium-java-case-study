package pages;

import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.LocatorActionHelper.click;
import static helpers.LocatorActionHelper.sendKeys;

public class LoginPage {

    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);


    private By emailInput = By.id("login-email");
    private By emailCheckButton = By.cssSelector("[data-testid='email-check-button']");
    private By passwordInput = By.id("login-password");
    private By loginButton = By.cssSelector("[data-testid='login-button']");
    private final By poseidonLoader = By.cssSelector("div.poseidon-loader-container.fixed");





    public void validLoginWithEmail(String email, String password) {
        sendKeys(emailInput, email);
        logger.info("Email entered: " + email);
        click(emailCheckButton);
        sendKeys(passwordInput, password);
        logger.info("Password entered: " + password);
        click(loginButton);
        WaitHelper.waitForDisappear(poseidonLoader);
    }


}
