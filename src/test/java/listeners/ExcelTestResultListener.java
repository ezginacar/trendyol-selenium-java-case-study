package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExcelTestResultUtil;

public class ExcelTestResultListener
        implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        ExcelTestResultUtil.initializeResultFile();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeResult(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeResult(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeResult(result, "SKIPPED");
    }

    @Override
    public void onFinish(ISuite suite) {
        ExcelTestResultUtil.finalizeSummary();
    }

    private void writeResult(
            ITestResult result,
            String status
    ) {
        ExcelTestResultUtil.writeResult(
                getTestName(result),
                status,
                getFailureReason(result),
                getDuration(result)
        );
    }

    private long getDuration(ITestResult result) {
        return Math.max(
                0,
                result.getEndMillis()
                        - result.getStartMillis()
        );
    }

    private String getTestName(ITestResult result) {
        return result.getTestClass()
                .getRealClass()
                .getSimpleName()
                + "."
                + result.getMethod()
                .getMethodName();
    }

    private String getFailureReason(
            ITestResult result
    ) {
        Throwable throwable = result.getThrowable();

        if (throwable == null) {
            return "";
        }

        String message = throwable.getMessage();

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