package pages;

import driver.DriverManager;
import helpers.LocatorActionHelper;
import helpers.LocatorQueryHelper;
import helpers.NavigationHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelUtil;

import java.util.List;

import static driver.DriverManager.getCurrentUrl;
import static driver.DriverManager.refreshPage;


public class CartPage {

    private By basketSummaryContent = By.cssSelector("[data-testid='basket-summary']");
    private By removeButtons = By.className("remove-item-container");
    private By productCards = By.className("merchant-item-container");

    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    public boolean isUserNavigatedTheCartPage() {
        return LocatorQueryHelper.isDisplayed(basketSummaryContent);
    }

    public void clickDeleteButtonWithProductID(String productId) {
        List<WebElement> products = WaitHelper.waitForAllVisibility(productCards);
        String expectedId = String.format("basket-item@%s", productId);

        WebElement targetProductCard = products.stream()
                .filter(product -> {
                    String id = product.getAttribute("id");
                    return id != null && id.contains(expectedId);
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "C " + productId
                ));

        WebElement deleteButton = targetProductCard.findElement(removeButtons);
        LocatorActionHelper.clickWithJS(deleteButton);
        refreshPage();
    }


    public boolean isProductDisplayedOnBasket(String productId) {
        List<WebElement> products =  DriverManager.getDriver().findElements(productCards);

        if (products.isEmpty()) {
            return false;
        }

        String expectedId = String.format("basket-item@%s-", productId);
        for (WebElement product : products) {
            String id = product.getAttribute("id");
            if (id != null && id.contains(expectedId)) {
                return true;
            }
        }

        return false;

    }


    public boolean hasProducts() {
        return !LocatorQueryHelper.getElements(removeButtons).isEmpty();
    }

    public void deleteAllProducts() {

        while (hasProducts()) {
            List<WebElement> currentDeleteButtons = LocatorQueryHelper.getElements(removeButtons);

            int previousCount = currentDeleteButtons.size();
            WebElement firstDeleteButton = currentDeleteButtons.get(0);

            LocatorActionHelper.click(firstDeleteButton);
            refreshPage();
            WaitHelper.waitUntilElementCountDecreases(removeButtons, previousCount);

        }

    }

    public void navigateCart(){
        NavigationHelper.navigateToUrl(ExcelUtil.getValue("baseUrl") + "/sepetim");
    }


}
