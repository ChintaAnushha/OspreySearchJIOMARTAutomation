package Com.jioMart.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
//import net.serenitybdd.core.Serenity;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestResultListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private int passCount = 0;
    private int failCount = 0;
    private int skipCount = 0;
    private long startTime;


    @Override
    public void onStart(ITestContext context) {
        startTime = System.currentTimeMillis();
        System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        int totalTests = passCount + failCount + skipCount;
        double successRate = totalTests > 0 ? (double) passCount / totalTests * 100 : 0;

        String resultSummary = String.format(
//                "Test Execution Summary\n" +
//                        "====================\n" +
//                        "Total Tests: %d\n" +
//                        "Passed: %d\n" +
//                        "Failed: %d\n" +
//                        "Skipped: %d\n" +
//                        "Success Rate: %.2f%%\n" +
//                        "Duration: %d seconds\n" +
//                        "====================",
                "<b>Test Execution Summary</b><br>" +
                        "<b>Total Tests:</b> %d<br>" +
                        "<b>Passed:</b> %d<br>" +
                        "<b>Failed:</b> %d<br>" +
                        "<b>Skipped:</b> %d<br>" +
                        "<b>Success Rate:</b> %.2f%%<br>" +
                        "<b>Duration:</b> %d seconds",
                totalTests, passCount, failCount, skipCount, successRate, duration
        );


        extent.setSystemInfo("Test Summary", resultSummary);

        // Add the summary to the Extent report

        // 🥳 Attach the Pie Chart in the report
     //   String pieChart = generatePieChart(passCount, failCount, skipCount);
     //   ExtentTest chartTest = extent.createTest("Test Result Distribution");
       // chartTest.info("Pass/Fail/Skip Distribution");
     //   chartTest.info(pieChart);

        //   System.out.println(resultSummary);
        ExtentTest extentTest = extent.createTest("Test Execution Summary");
          extentTest.info(resultSummary);

        extent.flush();  // Write the results to the report
        System.out.println("Test Suite Finished: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test entry in Extent Reports
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Log PASS status to the Extent report
        test.get().pass("Test Passed: " + result.getMethod().getMethodName());
        Object responseObj = result.getAttribute("apiResponse");
        if (responseObj != null) {
       //     Serenity.recordReportData().withTitle("API Response").andContents(responseObj.toString());
            test.get().info("API Response: <pre>" + responseObj.toString() + "</pre>");
        }
        passCount++;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log FAILURE status to the Extent report
        test.get().fail("Test Failed: " + result.getMethod().getMethodName());
        test.get().fail(result.getThrowable());
        Object responseObj = result.getAttribute("apiResponse");
        if (responseObj != null) {
        //    Serenity.recordReportData().withTitle("API Response").andContents(responseObj.toString());
            test.get().info("API Response: <pre>" + responseObj.toString() + "</pre>");
        }
        failCount++;
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Log SKIPPED status to the Extent report
        test.get().skip("Test Skipped: " + result.getMethod().getMethodName());

        Object responseObj = result.getAttribute("apiResponse");
        if (responseObj != null) {
         //   Serenity.recordReportData().withTitle("API Response").andContents(responseObj.toString());
            test.get().info("API Response: <pre>" + responseObj.toString() + "</pre>");
        }
        skipCount++;
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Log failure but within success percentage (if applicable)
        test.get().warning("Test failed but within success percentage: " + result.getMethod().getMethodName());
        failCount++;
    }
}

//public class TestResultListener implements ITestListener {

//    private int passCount = 0;
//    private int failCount = 0;
//    private int skipCount = 0;
//    private long startTime;
//
//    @Override
//    public void onStart(ITestContext context) {
//        startTime = System.currentTimeMillis();
//        System.out.println("Test Suite Started: " + context.getName());
//    }
//
//    @Override
//    public void onFinish(ITestContext context) {
//        long duration = (System.currentTimeMillis() - startTime) / 1000;
//        int totalTests = passCount + failCount + skipCount;
//        double successRate = totalTests > 0 ? (double) passCount / totalTests * 100 : 0;
//
//        String resultSummary = String.format(
//                "<b>Test Execution Summary</b><br>" +
//                        "<b>Total Tests:</b> %d<br>" +
//                        "<b>Passed:</b> %d<br>" +
//                        "<b>Failed:</b> %d<br>" +
//                        "<b>Skipped:</b> %d<br>" +
//                        "<b>Success Rate:</b> %.2f%%<br>" +
//                        "<b>Duration:</b> %d seconds",
//                totalTests, passCount, failCount, skipCount, successRate, duration
//        );
//
//        // Log the summary to Serenity
//    //    Serenity.recordReportData().withTitle("Test Execution Summary").andContents(resultSummary);
//        System.out.println("Test Suite Finished: " + context.getName());
//    }
//
//    @Override
//    public void onTestStart(ITestResult result) {
//        // Log the test start to Serenity (you can customize this if needed)
//   //     Serenity.recordReportData().withTitle("Test Started").andContents("Test started: " + result.getMethod().getMethodName());
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//        // Log PASS status to Serenity report
//   //     Serenity.recordReportData().withTitle("Test Passed").andContents("Test Passed: " + result.getMethod().getMethodName());
//     //   logApiResponse(result);
//        passCount++;
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//        // Log FAILURE status to Serenity report
//       logApiResponse(result);
//        failCount++;
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//        // Log SKIPPED status to Serenity report
//       logApiResponse(result);
//        skipCount++;
//    }
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        // Log failure but within success percentage (if applicable) to Serenity
//      //  Serenity.recordReportData().withTitle("Test Failed Within Success Percentage").andContents("Test failed but within success percentage: " + result.getMethod().getMethodName());
//        failCount++;
//    }
//
//    private void logApiResponse(ITestResult result) {
//        // Handle logging API Response to Serenity
//        Object responseObj = result.getAttribute("apiResponse");
//
//        if (responseObj != null) {
//            try {
//                // Try serializing the response object into a string (JSON format)
//                String responseString = new ObjectMapper().writeValueAsString(responseObj);
//
//                // Log API Response to Serenity
//                Serenity.recordReportData().withTitle("API Response").andContents(responseString);
//
//            } catch (Exception e) {
//                // If serialization fails, log a basic message to Serenity
//                Serenity.recordReportData().withTitle("API Response Error").andContents("Error serializing response: " + e.getMessage());
//            }
//        }
//    }
//}

//    private static ExtentReports extent = ExtentManager.getInstance();
//    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
//
//    private int passCount = 0;
//    private int failCount = 0;
//    private int skipCount = 0;
//    private long startTime;
//    private Map<String, String> testStatusMap = new HashMap<>();
//
//    @Override
//    public void onTestStart(ITestResult result) {
//        //Allure.step("Starting test: " + result.getName());
//        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("path_to_report/extent-report.html");
//        String testName = result.getMethod().getMethodName();
//        Allure.epic("Osprey Search API Testing");
//        Allure.feature(result.getTestClass().getName());
//        Allure.step("Starting test: " + testName);
//    }
//
//    @Override
//    public void onStart(ITestContext context) {
//        startTime = System.currentTimeMillis();
//        Allure.epic("Osprey Search API Testing");
//        Allure.label("Test Suite", context.getName());
//      //  Allure.addAttachment("Test Suite", "Starting test suite: " + context.getName());
//        // Test suite start
//    }
//
//    @Override
//    public void onFinish(ITestContext context) {
//        long duration = (System.currentTimeMillis() - startTime) / 1000;
//        int totalTests = passCount + failCount + skipCount;
//        double successRate = totalTests > 0 ? (double) passCount / totalTests * 100 : 0; //(double) passCount / (passCount + failCount + skipCount) * 100;
//
//        String resultSummary = String.format(
//                "Test Execution Summary\n" +
//                        "====================\n" +
//                        "Total Tests: %d\n" +
//                        "Passed: %d\n" +
//                        "Failed: %d\n" +
//                        "Skipped: %d\n" +
//                        "Success Rate: %.2f%%\n" +
//                        "Duration: %d seconds\n" +
//                        "====================",
//                (passCount + failCount + skipCount),
//                passCount,
//                failCount,
//                skipCount,
//                successRate,
//                duration
//        );
//
//        Allure.description(resultSummary);
//        Allure.addAttachment("Test Results Summary", resultSummary);
//    }
//
//    @Override
//    public void onTestSuccess(ITestResult result) {
//        String testName = result.getMethod().getMethodName();
//        testStatusMap.put(testName, "PASSED");
//        updateTestMetrics();
//        addTestDetails(result, "PASSED");
//    }
//
//    @Override
//    public void onTestFailure(ITestResult result) {
//        failCount++;
//       // updateTestMetrics();
//        String testName = result.getMethod().getMethodName();
//        testStatusMap.put(testName, "FAILED");
//        updateTestMetrics();
//        addTestDetails(result, "FAILED");
//
//        // Add failure details
//        String errorMessage = result.getThrowable() != null ?
//                result.getThrowable().getMessage() : "No error message available";
//        Allure.addAttachment("Failure Details", errorMessage);
//    }
//
//    @Override
//    public void onTestSkipped(ITestResult result) {
//        skipCount++;
//       // updateTestMetrics();
//        String testName = result.getMethod().getMethodName();
//        testStatusMap.put(testName, "SKIPPED");
//        updateTestMetrics();
//        addTestDetails(result, "SKIPPED");
//    }
//
//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        // Handle test failed within success percentage
//        failCount++;
//        updateTestMetrics();
//    }
//
//    private void addTestDetails(ITestResult result, String status) {
//        String testName = result.getMethod().getMethodName();
//        String testClass = result.getTestClass().getName();
//        long duration = result.getEndMillis() - result.getStartMillis();
//
//        String details = String.format(
//                "Test Details\n" +
//                        "============\n" +
//                        "Name: %s\n" +
//                        "Class: %s\n" +
//                        "Status: %s\n" +
//                        "Duration: %d ms\n" +
//                        "============",
//                testName, testClass, status, duration
//        );
//
//        Allure.addAttachment("Test Execution Details", details);
//    }
//
//    private void updateTestMetrics() {
//        String metrics = String.format(
//                "Current Test Metrics\n" +
//                        "==================\n" +
//                        "Passed: %d\n" +
//                        "Failed: %d\n" +
//                        "Skipped: %d\n" +
//                        "Total: %d\n" +
//                        "==================",
//                passCount, failCount, skipCount,
//                (passCount + failCount + skipCount)
//        );
//        Allure.addAttachment("Test Metrics", metrics);
//    }
//}

