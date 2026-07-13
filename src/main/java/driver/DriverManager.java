package driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        driverThread.set(driver);
        logger.info("WebDriver instance successfully set for the current thread.");
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            try {
                driverThread.get().quit();
                logger.info("WebDriver instance successfully quitted.");
            } catch (Exception e) {
                logger.error("Error occurred while quitting the WebDriver: {}", e.getMessage());
            } finally {
                driverThread.remove();
                logger.info("WebDriver thread reference removed.");
            }
        } else {
            logger.warn("DriverManager.quitDriver() was called, but no active driver was found for this thread.");
        }
    }

    public static String getCurrentUrl() {
        String url = driverThread.get().getCurrentUrl();
        logger.debug("Current URL retrieved: {}", url);
        return url;
    }

    public static void switchToLastTab() {
        try {
            List<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
            String targetTab = tabs.get(tabs.size() - 1);
            getDriver().switchTo().window(targetTab);
            logger.info("Successfully switched to the last tab. Tab count: {}, Current URL now: {}", tabs.size(), getDriver().getCurrentUrl());
        } catch (Exception e) {
            logger.error("Failed to switch to the last tab. Error: {}", e.getMessage());
            throw e;
        }
    }

    public static void refreshPage() {
        try {
            driverThread.get().navigate().refresh();
            logger.info("Page refreshed successfully. Current URL: {}", driverThread.get().getCurrentUrl());
        } catch (Exception e) {
            logger.error("Failed to refresh the page. Error: {}", e.getMessage());
            throw e;
        }
    }
}