package helpers;


import driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static driver.DriverManager.getDriver;

public class LocatorHelper {

    private static final Logger logger = LoggerFactory.getLogger(LocatorHelper.class);

    @Step("Click on element: {locator}")
    public static void click(By locator) {
        try {
            WaitHelper.waitForClickable(locator).click();
            logger.info("Successful click : " + locator);
        } catch (Exception e) {
            logger.error("Could not click " + locator + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Send keys: {locator} -> Text: {text}")
    public static void sendKeys(By locator, String text) {
        try {
            var element = WaitHelper.waitForVisibility(locator);
            element.clear();
            element.sendKeys(text);
            logger.info(text + " sent on " + locator);
        } catch (Exception e) {
            logger.error(text + " could not send on: " + locator + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Check if element is displayed: {locator}")
    public static boolean isDisplayed(By locator) {
        try {
            return WaitHelper.waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            logger.warn("Element was not displayed: " + locator);
            return false;
        }
    }

    @Step("Get attribute '{attributeName}' from element: {locator}")
    public static String getAttribute(By locator, String attributeName) {
        try {
            String value = WaitHelper.waitForVisibility(locator).getAttribute(attributeName);
            logger.info("Successfully got attribute '" + attributeName + "' with value: " + value + " from " + locator);
            return value;
        } catch (Exception e) {
            logger.error("Could not get attribute '" + attributeName + "' from " + locator + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Click on element: {element}")
    public static void click(WebElement element) {
        try {
            WaitHelper.waitForClickableElement(element).click();
            logger.info("Successful click : " + element);
        } catch (Exception e) {
            logger.error("Could not click element. Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Get attribute '{attributeName}' from element: {element}")
    public static String getAttribute(WebElement element, String attributeName) {
        try {
            String value = WaitHelper.waitForVisibilityElement(element).getAttribute(attributeName);
            logger.info("Successfully got attribute '" + attributeName + "' with value: " + value + " from " + element);
            return value;
        } catch (Exception e) {
            logger.error("Could not get attribute '" + attributeName + "' from " + element + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Click if the element is present {locator} ")
    public static void clickIfPresent(By locator) {
        try {
            click(WaitHelper.waitForClickable(locator));
        } catch (TimeoutException ignored) {
        }

    }

    public static boolean isEmpty(By locators){
        return getElements(locators).isEmpty();
    }

    public static List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);

    }


}