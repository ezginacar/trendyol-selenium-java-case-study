package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExcelTestResultUtil;

public class TestResultListener
        implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        ExcelTestResultUtil.initializeResultFile();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExcelTestResultUtil.finalizeSummary();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExcelTestResultUtil.writeResult(
                getTestName(result),
                "PASS",
                "",
                getDuration(result)
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExcelTestResultUtil.writeResult(
                getTestName(result),
                "FAIL",
                getFailureReason(result),
                getDuration(result)
        );
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExcelTestResultUtil.writeResult(
                getTestName(result),
                "SKIPPED",
                getFailureReason(result),
                getDuration(result)
        );
    }

    private long getDuration(
            ITestResult result
    ) {
        return result.getEndMillis()
                - result.getStartMillis();
    }

    private String getTestName(
            ITestResult result
    ) {
        return result.getTestClass()
                .getRealClass()
                .getSimpleName()
                + "."
                + result.getMethod().getMethodName();
    }

    private String getFailureReason(
            ITestResult result
    ) {
        Throwable throwable =
                result.getThrowable();

        if (throwable == null) {
            return "";
        }

        String message =
                throwable.getMessage();

        if (message == null || message.isBlank()) {
            return throwable.getClass()
                    .getSimpleName();
        }

        return throwable.getClass()
                .getSimpleName()
                + ": "
                + message;
    }
}