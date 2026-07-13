package helpers;

import driver.DriverManager;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ExcelUtil;

public class NavigationHelper {

    private static final Logger logger = LoggerFactory.getLogger(NavigationHelper.class);

    @Step("Navigate to base URL")
    public static void navigateToBaseUrl() {
        try {
            String baseUrl = ExcelUtil.getValue("baseUrl");

            DriverManager.getDriver().get(baseUrl);
            logger.info("Successfully navigated to Base URL: {}", baseUrl);
        } catch (Exception e) {
            logger.error("Failed to navigate to Base URL. Error: {}", e.getMessage());
            throw e;
        }
    }

    @Step("Navigate to url: {url}")
    public static void navigateToUrl(String url) {
        try {
            DriverManager.getDriver().get(url);
            logger.info("Successfully navigated to custom URL: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to URL: {}. Error: {}", url, e.getMessage());
            throw e;
        }
    }
}