package pages;

import org.openqa.selenium.By;

import static driver.DriverManager.getCurrentUrl;
import static helpers.LocatorHelper.click;
import static helpers.LocatorHelper.isDisplayed;

public class ProductDetailPage {
    private By favoriteButton = By.cssSelector("button[data-testid='favorite-button']");
    private By favouriteButtonSelected = By.cssSelector("button[data-testid='favorite-button']>[class*='orange']");


    public boolean isUserNavigatedTheRightProductDetailPage(String productId ) {
        return getCurrentUrl().contains("p-" + productId);
    }

    public void clickFavoriteButton() {
        click(favoriteButton);
    }

    public boolean isFavoriteButtonSelected() {
        return isDisplayed(favouriteButtonSelected);
    }


}
