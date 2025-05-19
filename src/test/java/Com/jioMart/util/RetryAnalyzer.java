package Com.jioMart.util;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 1;
    private static final int MAX_RETRY_COUNT = 2;
    private static final long WAIT_TIME_MS = 20000;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            // Log error on first failure attempt
            if (retryCount == 1 && result.getThrowable() != null) {
                captureFailureDetails(result);
            }
            try {
                Allure.step("Retrying test - Attempt " + (retryCount + 1));
                retryCount++;
                Thread.sleep(WAIT_TIME_MS);
                return true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        // Final failure - optionally you can add another log here
        return false;
    }

    private void captureFailureDetails(ITestResult result) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String testName = result.getMethod().getMethodName();
        String safeTestName = testName.replaceAll("[^a-zA-Z0-9\\-_]", "_"); // Sanitize filename

        // Log error message
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Allure.addAttachment("Failure Exception (Attempt " + retryCount + ")", throwable.getMessage());
        }

        // Attach failed API response
        Object response = result.getAttribute("apiResponse");
        if (response != null) {
            Allure.addAttachment("Failed API Response", response.toString());
        }

        // Create output folder if not exist
        File failureDir = new File("test-output/failures");
        if (!failureDir.exists()) {
            failureDir.mkdirs();
        }

        // Build report content
        try {
            String reportPath = String.format("test-output/failures/%s_%s_failure_report.txt",safeTestName, timestamp);
            String reportContent = String.format(
                    "Test: %s\nTimestamp: %s\nError: %s\nResponse: %s",
                    testName,
                    timestamp,
                    throwable != null ? throwable.getMessage() : "No error message",
                    response != null ? response.toString() : "No response available"
            );

            FileUtils.writeStringToFile(new File(reportPath), reportContent, "UTF-8");
            System.out.println("Failure report saved at: " + reportPath);
            Allure.addAttachment("Failure Report", "text/plain", reportContent);
        } catch (IOException e) {
            Allure.addAttachment("Report Creation Failed", e.getMessage());
        }
    }
}

