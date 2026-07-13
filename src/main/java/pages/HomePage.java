package pages;

import helpers.LocatorActionHelper;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelUtil;

import static helpers.LocatorActionHelper.*;
import static helpers.LocatorQueryHelper.getAttribute;
import static helpers.LocatorQueryHelper.isDisplayed;
import static helpers.NavigationHelper.navigateToBaseUrl;

public class HomePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private By userMenu = By.cssSelector("[data-testid='user-menu']");
    private By searchField = By.cssSelector("[data-testid='browsing-search-input']");
    private By suggestionButton = By.cssSelector("[data-testid='suggestion-placeholder']");
    private By searchButton = By.cssSelector("[data-testid='browsing-search-submit-icon']");
    private By favoriteButton = By.cssSelector("[data-testid='favorite-menu']");
    private By basketIcon = By.cssSelector("[data-testid='basket-wrapper']");
    private By genderPopupCloseButton = By.className("modal-section-close");
    private By languageOnboardingMessage =  By.cssSelector("[data-testid='language-onboarding-close']");
    private By myAccountMenu =  By.className("navigation-menu-user");

    public void open(){
        navigateToBaseUrl();
        logger.info("User is at " + ExcelUtil.getValue("baseUrl"));
    }

    public boolean isLoginButtonDisplayed() {return isDisplayed(userMenu);}

    public void clickLoginButton() {
        click(userMenu);
        logger.info("Clicked on login button");}

    public boolean isLoggedIn() {
        String attr= getAttribute(myAccountMenu, "href");
        return attr.contains("/hesabim");
    }

    public void searchFromSuggestion(String searchText) {
        click(suggestionButton);
        sendKeys(searchField, searchText);
        click(searchButton);
        logger.info("Searched for: " + searchText);
    }

    public void clickFavoriteButton() {click(favoriteButton);}

    public void clickBasketIcon() {click(basketIcon);}

    public HomePage closeGenderPopup() {
        LocatorActionHelper.clickIfPresent(genderPopupCloseButton);
        logger.info("Gender selection popup closed");
        return  this;
    }

    public void closeLanguagePopup() {
        LocatorActionHelper.clickIfPresent(languageOnboardingMessage);
        logger.info("language popup closed");
    }






}
