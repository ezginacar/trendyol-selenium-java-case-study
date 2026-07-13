package helpers;

import driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocatorActionHelper {

    private static final Logger logger = LoggerFactory.getLogger(LocatorActionHelper.class);

    @Step("Click on element: {locator}")
    public static void click(By locator) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                WebElement element = WaitHelper.waitForClickable(locator);
                element.click();
                logger.info("Successful click on locator: " + locator + " (Attempt: " + attempt + ")");
                return;

            } catch (StaleElementReferenceException exception) {
                logger.warn("StaleElementReferenceException on locator: " + locator + ". Retrying... (" + attempt + "/" + maxAttempts + ")");
                if (attempt == maxAttempts) {
                    logger.error("Failed to click after max attempts due to Stale Element on locator: " + locator);
                    throw exception;
                }

            } catch (ElementClickInterceptedException exception) {
                logger.warn("ElementClickInterceptedException on locator: " + locator + ". Retrying... (" + attempt + "/" + maxAttempts + ")");
                if (attempt == maxAttempts) {
                    logger.error("Failed to click after max attempts due to Click Intercepted on locator: " + locator);
                    throw exception;
                }

            } catch (Exception exception) {
                logger.error("Unexpected error while clicking locator: " + locator + ". Error: " + exception.getMessage());
                throw exception;
            }
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

    @Step("Click if the element is present: {locator}")
    public static void clickIfPresent(By locator) {
        try {
            click(locator);
        } catch (TimeoutException ignored) {
            logger.info("Element was not present/clickable, skipped clickIfPresent for: " + locator);
        }
    }

    @Step("Click with JavaScript on element: {locator}")
    public static void clickWithJS(WebElement locator) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
            js.executeScript("arguments[0].click();", locator);
            logger.info("Successful JS click on: " + locator);
        } catch (Exception e) {
            logger.error("Could not click with JS on " + locator + ". Error: " + e.getMessage());
            throw e;
        }
    }

    @Step("Force click and dispatch change event on: {locator}")
    public static void forceClickAndDispatchEvent(By locator) {
        try {
            WebElement element = DriverManager.getDriver().findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();

            js.executeScript(
                    "arguments[0].click(); " +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element
            );

            logger.info("Successfully forced JS click and dispatched change event on: {}", locator);
        } catch (Exception e) {
            logger.error("Failed to force click on locator: {}. Error: {}", locator, e.getMessage());
            throw e;
        }
    }
}