package helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static driver.DriverManager.getDriver;

public class LocatorQueryHelper {

    private static final Logger logger = LoggerFactory.getLogger(LocatorQueryHelper.class);

    @Step("Check if element is displayed: {locator}")
    public static boolean isDisplayed(By locator) {
        try {
            boolean displayed = WaitHelper.waitForVisibility(locator).isDisplayed();
            logger.info("Element is displayed: " + locator); // 🌟 Başarı logu eklendi
            return displayed;
        } catch (Exception e) {
            logger.warn("Element was not displayed: " + locator);
            return false;
        }
    }

    @Step("Get attribute '{attributeName}' from element: {locator}")
    public static String getAttribute(By locator, String attributeName) {
        try {
            String value = WaitHelper.waitForVisibility(locator).getAttribute(attributeName);
            logger.info("Successfully got attribute '" + attributeName + "' with value: " + value + " from locator: " + locator);
            return value;
        } catch (Exception e) {
            logger.error("Could not get attribute '" + attributeName + "' from locator: " + locator + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Check {locators} is empty")
    public static boolean isEmpty(By locators){
        boolean empty = getElements(locators).isEmpty();
        logger.info("Is element list empty for locator " + locators + "? -> " + empty); // 🌟 Durum logu eklendi
        return empty;
    }

    @Step("Get webelement list from {locator}")
    public static List<WebElement> getElements(By locator) {
        List<WebElement> elements = getDriver().findElements(locator);
        logger.info("Found " + elements.size() + " element(s) for locator: " + locator); // 🌟 Liste boyut logu eklendi
        return elements;
    }

    @Step("Get attribute '{attributeName}' from web element")
    public static String getAttribute(WebElement element, String attributeName) {
        try {
            String value = element.getAttribute(attributeName);
            logger.info("Successfully got attribute '" + attributeName + "' with value: " + value + " from web element: " + element);
            return value;
        } catch (Exception e) {
            logger.error("Could not get attribute '" + attributeName + "' from web element: " + element + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Get text from element: {locator}")
    public static String getText(By locator) {
        try {
            WebElement element = WaitHelper.waitForVisibility(locator);
            String text = element.getText().trim();
            logger.info("Successfully retrieved text: '" + text + "' from locator: " + locator); // 🌟 Kritik başarı logu eklendi
            return text;
        } catch (Exception e) {
            logger.error("Could not get text from locator: " + locator + ". Error: " + e.getMessage()); // 🌟 Hata logu eklendi
            throw e;
        }
    }
}