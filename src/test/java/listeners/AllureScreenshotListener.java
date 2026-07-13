package listeners;

import driver.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class AllureScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        attachFailureDetails(result);
        attachScreenshot(result);
    }

    private void attachFailureDetails(ITestResult result) {

        Throwable throwable = result.getThrowable();

        if (throwable == null) {
            return;
        }

        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(
                new PrintWriter(stringWriter)
        );

        String failureDetails = """
                Test Class:
                %s

                Test Method:
                %s

                Exception Type:
                %s

                Error Message:
                %s

                Full Stack Trace:
                %s
                """.formatted(
                result.getTestClass()
                        .getRealClass()
                        .getName(),

                result.getMethod()
                        .getMethodName(),

                throwable.getClass()
                        .getName(),

                throwable.getMessage(),

                stringWriter
        );

        Allure.addAttachment(
                "Failure Details and Stack Trace",
                "text/plain",
                failureDetails
        );
    }

    private void attachScreenshot(ITestResult result) {

        WebDriver driver =
                DriverManager.getDriver();

        if (driver == null) {
            return;
        }

        if (!(driver instanceof TakesScreenshot takesScreenshot)) {
            return;
        }

        try {
            byte[] screenshot =
                    takesScreenshot
                            .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                    "Failure Screenshot - "
                            + getTestName(result),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

            Allure.addAttachment(
                    "Current URL",
                    "text/plain",
                    driver.getCurrentUrl()
            );

        } catch (Exception exception) {
            System.err.println(
                    "Allure failure evidence could not be attached: "
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