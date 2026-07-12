package tests;


import driver.DriverFactory;
import driver.DriverManager;
import listeners.AllureScreenshotListener;
import listeners.TestResultListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ExcelUtil;

@Listeners({TestResultListener.class, AllureScreenshotListener.class})
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
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
