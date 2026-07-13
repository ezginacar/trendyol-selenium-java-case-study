package helpers;

import driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitHelper {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private WaitHelper() {}

    private static WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(), DEFAULT_TIMEOUT);
    }


    public static WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibilityElement(WebElement element) {
        return getWait().until(ExpectedConditions.visibilityOf(element));
    }


    public static WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitForAllVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public static WebElement waitForClickableElement(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitUntilElementCountDecreases(By locator, int previousCount) {
        getWait().until(driver ->
                DriverManager.getDriver().findElements(locator).size() < previousCount
        );
    }

    public static void waitForDisappear(By locator) {
        getWait().until( ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}