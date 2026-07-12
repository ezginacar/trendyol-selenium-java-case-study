package pages;

import helpers.LocatorHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelUtil;

import static helpers.LocatorHelper.*;
import static helpers.NavigationHelper.navigateToBaseUrl;

public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private By userMenu = By.cssSelector("[data-testid='user-menu']");
    private By searchField = By.cssSelector("[data-testid='suggestion-placeholder']");
    private By searchButton = By.cssSelector("[data-testid='browsing-search-submit-icon']");
    private By favoriteButton = By.cssSelector("[data-testid='favorite-button']");
    private By basketIcon = By.linkText("/sepetim");
    private By genderPopupCloseButton = By.className("modal-section-close");
    private By languageOnboardingMessage =  By.cssSelector("[data-testid='language-onboarding-close']");

    public void open(){
        navigateToBaseUrl();
        logger.info("User is at " + ExcelUtil.getValue("baseUrl"));
    }

    public boolean isLoginButtonDisplayed() {return isDisplayed(userMenu);}

    public void clickLoginButton() {
        click(userMenu);
        logger.info("Clicked on login button");}

    public boolean isLoggedIn() {
        String attr= getAttribute(userMenu, "href");
        return attr.contains("hesabim/siparislerim");
    }

    public void searchFromSuggestion(String searchText) {
        sendKeys(searchField, searchText);
        click(searchButton);
        logger.info("Searched for: " + searchText);
    }

    public void clickFavoriteButton() {click(favoriteButton);}

    public void clickBasketIcon() {click(basketIcon);}

    public HomePage closeGenderPopup() {
        LocatorHelper.clickIfPresent(genderPopupCloseButton);
        logger.info("Gender selection popup closed");
        return  this;
    }

    public void closeLanguagePopup() {
        LocatorHelper.clickIfPresent(languageOnboardingMessage);
        logger.info("language popup closed");
    }






}
