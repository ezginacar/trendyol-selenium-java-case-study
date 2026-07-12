package driver;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class DriverManager {
    private static final ThreadLocal<WebDriver> driverThread= new ThreadLocal<>();

    public static void setDriver(WebDriver driver) { driverThread.set(driver); }

    public static WebDriver getDriver() { return driverThread.get(); }

    public static void quitDriver() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
        }
        driverThread.remove();
    }

    public static String getCurrentUrl() {
        return driverThread.get().getCurrentUrl();
    }

    public static void switchToLastTab() {
        List<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(tabs.size() - 1));
    }
    


}
