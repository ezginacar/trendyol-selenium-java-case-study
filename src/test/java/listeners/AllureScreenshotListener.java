package listeners;


import driver.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class AllureScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachScreenshot(result);
    }

    private void attachScreenshot(ITestResult result) {

        WebDriver driver = DriverManager.getDriver();

        if (driver == null) {
            return;
        }

        if (!(driver instanceof TakesScreenshot takesScreenshot)) {
            return;
        }

        try {
            byte[] screenshot =
                    takesScreenshot.getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    "Failure Screenshot - " + getTestName(result),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

        } catch (Exception exception) {
            System.err.println(
                    "Allure screenshot could not be attached: "
                            + exception.getMessage()
            );
        }
    }

    private String getTestName(ITestResult result) {
        return result.getTestClass()
                .getRealClass()
                .getSimpleName()
                + "."
                + result.getMethod()
                .getMethodName();
    }
}