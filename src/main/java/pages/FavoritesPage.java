package pages;

import driver.DriverManager;
import helpers.LocatorActionHelper;
import helpers.LocatorQueryHelper;
import helpers.NavigationHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import utils.ExcelUtil;

import java.util.List;

import static helpers.LocatorActionHelper.click;
import static helpers.LocatorQueryHelper.isDisplayed;

public class FavoritesPage {

    private By productCards = By.cssSelector(".favorites-product-list .product-card");
    private By addBasketButton = By.className("add-to-basket-button");
    private By successAlert = By.cssSelector(".success-alert.show");
    private By actionMenu = By.cssSelector("[data-testid='action-menu-trigger']");
    private By removeFavoritesOption = By.cssSelector(".action-menu-dropdown>button.danger");

    public boolean isAtFavoritePage() {
        String url = DriverManager.getCurrentUrl();
        return url.contains("hesabim/favoriler");
    }

    public boolean isTheProductInFavorites(String productId) {
        List<WebElement> products = WaitHelper.waitForAllVisibility(productCards);
        String expectedHref = String.format("p-%s", productId);

        for (WebElement product : products) {
            String href = LocatorQueryHelper.getAttribute(product, "href");
            if (href != null && href.contains(expectedHref)) {
                return true;
            }
        }

        return false;
    }



    public void clickAddBasketButtonWithID(String productId) {
        List<WebElement> products = WaitHelper.waitForAllVisibility(productCards);
        String expectedHref = String.format("p-%s", productId);

        for (WebElement product : products) {
            String href = product.getAttribute("href");

            if (href != null && href.contains(expectedHref)) {

                WebElement addBasket =
                        product.findElement(addBasketButton);

                LocatorActionHelper.click(addBasket);
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
        return !LocatorQueryHelper.isEmpty(actionMenu);

    }

    public void removeAllFavourites(){
        while (hasFavouriteActionIcon()) {

            List<WebElement> actionIcons = LocatorQueryHelper.getElements(actionMenu);
            int previousCount = actionIcons.size();

            WebElement firstActionMenu = actionIcons.get(0);

            click(firstActionMenu);
            click(removeFavoritesOption);
            WaitHelper.waitUntilElementCountDecreases(actionMenu, previousCount);
        }
    }

    public void navigateFavourites(){
         NavigationHelper.navigateToUrl(ExcelUtil.getValue("baseUrl") + "/hesabim/favoriler");
    }
}





