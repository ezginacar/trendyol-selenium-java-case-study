package driver;

import org.openqa.selenium.WebDriver;
import utils.ConfigUtil;

public class DriverFactory {

    public static WebDriver createDriver() {
        String browser = ConfigUtil.get("browser");
        if ("chrome".equals(browser)) {
            return new ChromeBrowser().create();
        }

        throw new IllegalArgumentException(
                "Unsupported browser: " + browser
        );


    }


}
