package pages;

import driver.DriverManager;
import helpers.NavigationHelper;
import helpers.WaitHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


import static helpers.LocatorHelper.*;

public class ProductListPage {
    private static final Logger logger = LoggerFactory.getLogger(ProductListPage.class);

    private By categoryFilterSearchInput = By.xpath("//*[@data-aggregationtype='LeafCategory']//*[@data-testid='search-input-container']/input");
    private By categoryFilterMatchedSingleCheckboxItem = By.xpath("//*[@data-aggregationtype='LeafCategory']//*[@data-testid='checkbox-list-item']");
    private By searchResultTitleText = By.cssSelector("[data-testid='web-search-result-header'] .title");
    private By noResultFoundContainer = By.cssSelector("[data-testid='not-found-content']");
    private By productCards = By.className("product-card");



    public void selectMatchesSingleItemFromCategoryFilter(String checkBoxItemName) {
        sendKeys(categoryFilterSearchInput, checkBoxItemName);
        click(categoryFilterMatchedSingleCheckboxItem);
    }

    public String getTextOfSearchResultTitle() {
        return searchResultTitleText.toString();
    }

    public void navigateNthPage(int pageNumber) {
        String url = DriverManager.getDriver().getCurrentUrl();

        if (url.contains("&pi=")) {
            url = url.replaceAll("([&?]pi=)\\d+", "$1" + pageNumber);
        } else {
            if (url.contains("?")) {
                url += "&pi=" + pageNumber;
            } else {
                url += "?pi=" + pageNumber;
            }
        }
        logger.info("Navigating to page number: " + pageNumber + " with URL: " + url);
        NavigationHelper.navigateToUrl(url);
    }

    public boolean isNoResultFoundDisplayed() {
        return isDisplayed(noResultFoundContainer);
    }

    public String clickNthProductAndGetProductID(int nthProduct) {
        List<WebElement> productList = WaitHelper.waitForAllVisibility(productCards);
        int productIndex = nthProduct - 1;

        if (productIndex < 0 || productIndex >= productList.size()) {
            throw new IllegalArgumentException(
                    "Product number is out of range: " + nthProduct
            );
        }

        WebElement nthProductElement = productList.get(productIndex);

        String expectedProductId = nthProductElement.getAttribute("id");

        click(nthProductElement);
        DriverManager.switchToLastTab();

        logger.info(
                "Clicked {}th product and switched to product detail tab. Product ID: {}",
                nthProduct,
                expectedProductId
        );

        return expectedProductId;
    }




}
