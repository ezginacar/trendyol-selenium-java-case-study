package pages;

import driver.DriverManager;
import helpers.LocatorHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelUtil;

import java.util.List;


public class CartPage {

    private By basketSummaryContent = By.cssSelector("[data-testid='basket-summary']");
    private By removeButtons = By.className("remove-item-container");

    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    public boolean isUserNavigatedTheCartPage() {
        return LocatorHelper.isDisplayed(basketSummaryContent);
    }

    public void clickDeleteButtonWithProductID(String productId) {
        By productCard = By.cssSelector(
                String.format("[id*='basket-item@%s-']", productId)
        );
        LocatorHelper.click(productCard);

    }


    public boolean isProductDisplayedOnBasket(String productId) {
        By productCard = By.cssSelector(
                String.format("[id*='basket-item@%s-']", productId)
        );

        return !DriverManager.getDriver()
                .findElements(productCard)
                .isEmpty();
    }


    private final By cartItems =

            By.cssSelector("[data-testid='basket-item']");

    private final By deleteButtons =

            By.cssSelector("[data-testid='basket-item-remove-button']");

    private final By emptyCartMessage =

            By.cssSelector("[data-testid='empty-basket']");

    public boolean hasProducts() {

        return !LocatorHelper.getElements(removeButtons).isEmpty();

    }

    public void deleteAllProducts() {

        while (hasProducts()) {

            List<WebElement> currentDeleteButtons = LocatorHelper.getElements(removeButtons);

            int previousCount = currentDeleteButtons.size();
            WebElement firstDeleteButton = currentDeleteButtons.get(0);

            LocatorHelper.click(firstDeleteButton);
            WaitHelper.waitUntilElementCountDecreases(removeButtons, previousCount);

        }

    }


}
