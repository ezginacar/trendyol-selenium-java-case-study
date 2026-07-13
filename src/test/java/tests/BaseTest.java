package tests;

import driver.DriverFactory;
import driver.DriverManager;
import io.qameta.allure.Allure;
import listeners.AllureScreenshotListener;
import listeners.ExcelTestResultListener;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ExcelUtil;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

@Listeners({ExcelTestResultListener.class, AllureScreenshotListener.class})
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        driver = DriverFactory.createDriver();

        DriverManager.setDriver(driver);

        driver.manage().window().maximize();

        driver.get(ExcelUtil.getValue("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        try {
            if (result.getStatus() == ITestResult.FAILURE) {

                attachFailureDetails(result);
                attachScreenshot();
                attachCurrentUrl();
            }

        } finally {
            DriverManager.quitDriver();
        }
    }

    private void attachFailureDetails(
            ITestResult result
    ) {

        Throwable throwable =
                result.getThrowable();

        if (throwable == null) {
            return;
        }

        StringWriter writer =
                new StringWriter();

        throwable.printStackTrace(
                new PrintWriter(writer)
        );

        String failureDetails = """
                Test Class:
                %s

                Test Method:
                %s

                Exception:
                %s

                Error Message:
                %s

                Stack Trace:
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

                writer.toString()
        );

        Allure.addAttachment(
                "Failure Details and Stack Trace",
                "text/plain",
                failureDetails
        );
    }

    private void attachScreenshot() {

        WebDriver currentDriver =
                DriverManager.getDriver();

        if (currentDriver == null) {
            return;
        }

        if (!(currentDriver instanceof TakesScreenshot takesScreenshot)) {
            return;
        }

        byte[] screenshot =
                takesScreenshot
                        .getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(
                "Failure Screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }

    private void attachCurrentUrl() {

        WebDriver currentDriver =
                DriverManager.getDriver();

        if (currentDriver == null) {
            return;
        }

        Allure.addAttachment(
                "Current URL",
                "text/plain",
                currentDriver.getCurrentUrl()
        );
    }

}