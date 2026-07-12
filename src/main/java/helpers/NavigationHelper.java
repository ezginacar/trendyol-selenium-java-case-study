package helpers;

import driver.DriverManager;
import io.qameta.allure.Step;
import utils.ExcelUtil;

public class NavigationHelper {

    @Step("Navigate to base URL")
    public static void navigateToBaseUrl() {
        DriverManager.getDriver().get(
                ExcelUtil.getValue("baseUrl"));

    }

    @Step("Navigate to url: {url}")
    public static void navigateToUrl(String url) {
        DriverManager.getDriver().get(url);

    }

}
