    package Com.jioMart.util;

    import com.aventstack.extentreports.ExtentReports;
    import com.aventstack.extentreports.reporter.ExtentSparkReporter;

    public class ExtentManager {
        private static ExtentReports extent;

        public static ExtentReports getInstance() {
            if (extent == null) {
                createInstance();
            }
            return extent;
        }
        private static void createInstance() {
            ExtentSparkReporter spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");

            spark.config().setReportName("OspreySearch Automation Test Report");
            spark.config().setDocumentTitle("OspreySearch Test Results");
            spark.config().setTheme(com.aventstack.extentreports.reporter.configuration.Theme.DARK); // Added dark theme

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Environment", "SIT");
            extent.setSystemInfo("Tester", "Anusha");
            extent.setSystemInfo("Browser", "Chrome"); // Optional
        }
    }

