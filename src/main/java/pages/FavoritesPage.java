package pages;

import driver.DriverManager;
import helpers.LocatorHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static helpers.LocatorHelper.click;
import static helpers.LocatorHelper.isDisplayed;

public class FavoritesPage {

    private By productCards = By.className("product-card");
    private By addBasketButton = By.className("add-to-basket-button");
    private By successAlert = By.cssSelector("[.success-alert.show");
    private By actionMenu = By.cssSelector("data-testid=\"action-menu\"");
    private By removeFavoritesOption = By.cssSelector(".action-menu-dropdown>button.danger");

    public boolean isAtFavoritePage() {
        String url = DriverManager.getCurrentUrl();
        return url.contains("hesabim/favoriler");
    }

    public boolean isTheProductInFavorites(String productId) {
        List<WebElement> products = WaitHelper.waitForAllVisibility(productCards);
        String expectedHref = String.format("p-%s?", productId);

        for (WebElement product : products) {
            String href = LocatorHelper.getAttribute(product, "href");

            if (href != null && href.contains(expectedHref)) {
                return true;
            }
        }

        return false;
    }



    public void clickAddBasketButtonWithID(String productId) {
        List<WebElement> products = WaitHelper.waitForAllVisibility(productCards);
        String expectedHref = String.format("p-%s?", productId);

        for (WebElement product : products) {

            WebElement productLink = product.findElement(By.tagName("a"));
            String href = productLink.getAttribute("href");

            if (href != null && href.contains(expectedHref)) {

                WebElement addBasket =
                        product.findElement(addBasketButton);

                LocatorHelper.click(addBasket);
                return;
            }
        }

        throw new NoSuchElementException(
                "Product with id " + productId + " was not found.");
    }

    public boolean isSuccessAlertDisplayed() {
        return isDisplayed(successAlert);
    }

    public boolean hasFavouriteActionIcon() {
        return !LocatorHelper.isEmpty(actionMenu);

    }

    public void removeAllFavourites(){
        while (hasFavouriteActionIcon()) {

            List<WebElement> actionIcons = LocatorHelper.getElements(actionMenu);

            int previousCount = actionIcons.size();
            WebElement firstActionMenu = actionIcons.get(0);
            click(firstActionMenu);
            click(removeFavoritesOption);
            WaitHelper.waitUntilElementCountDecreases(removeFavoritesOption, previousCount);

        }
    }
}





