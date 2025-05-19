package Com.jioMart.base;

import Com.jioMart.model.survey.global.TestData;
import Com.jioMart.model.survey.global.TestDataListYMLWrapper;
import Com.jioMart.util.TestResultListener;
import Com.jioMart.util.TestdataLoader;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.general.DefaultPieDataset;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.yaml.snakeyaml.Yaml;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * @author Anusha created on 17-April-2025
 */
@Listeners({TestResultListener.class})
public class BaseScript {

    public static ExtentReports extent;
    public static ExtentTest test;


    @AfterSuite(alwaysRun = true)
    public void tearDownExtentReport() {
        if (extent != null) {
            extent.flush(); // Write everything to the report
        }
    }


    @BeforeSuite
    public static void clearReportsBeforeExecution() {
        deleteOldReports();
    }

    public static void deleteOldReports() {
        File allureResults = new File("/Users/chinta.anusha/Desktop/OspreySearchAJIOAutomation/allure-results");
        File allureReport = new File("/Users/chinta.anusha/Desktop/OspreySearchAJIOAutomation/target/allure-report");

        System.out.println("🔍 Checking folders...");
        System.out.println("📂 allure-results exists? " + allureResults.exists() + " | Path: " + allureResults.getAbsolutePath());
        System.out.println("📂 allure-report exists? " + allureReport.exists() + " | Path: " + allureReport.getAbsolutePath());

        deleteFolder(allureResults);
        deleteFolder(allureReport);

        System.out.println("✅ Old Allure reports deleted successfully.");
    }

    private static void deleteFolder(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file); // Recursively delete subfolders
                    }
                    if (!file.delete()) {
                        System.out.println("❌ Failed to delete: " + file.getAbsolutePath());
                    }
                }
            }
            if (!folder.delete()) {
                System.out.println("❌ Failed to delete folder: " + folder.getAbsolutePath());
            }
        } else {
            System.out.println("⚠️ No old Allure reports found to delete: " + folder.getAbsolutePath());
        }
    }

    protected Properties prop;
    protected FileInputStream fis;
    static Map<String, String> map = new HashMap<String, String>();
    public static HashMap<String,String> headerMap = new HashMap<String,String>();
    public String propertyVar = "";
    public Map<String, String> getEnv;
    public static Map<String,String> reqHeaderMap = new HashMap<>();
    public static String fqdn = "";
    public static TestData testData = null;
    public static String cookie = "";
    public static String auth_token = "";
    public static String apiKey = "";
    public static String productId=null;
    public static String token_type = "";
    public static int expStatusCode = 200;
    public static String httpResponseEntity = null;
    protected String baseUrl;
    protected static final String ENVIRONMENT = System.getProperty("env", "SIT");

    static Connection con = null;
    // Statement object
    public static Statement stmt;

    public String DB_URL;
    public String DB_USER;
    public String DB_PASSWORD;



    @BeforeSuite(alwaysRun = true)
    public void cleanAllureResults() {
        try {
            File allureResults = new File("allure-results");
            if (allureResults.exists()) {
                FileUtils.deleteDirectory(allureResults);
            }
            System.out.println("Cleared previous Allure results");
            allureResults.mkdirs();
        } catch (IOException e) {
            System.err.println("Failed to clean Allure results: " + e.getMessage());
        }
    }

    @BeforeSuite
    public void setupReport() {
        // Set Allure properties
        System.setProperty("allure.results.directory", "allure-results");

        // Add environment info
        Map<String, String> env = new HashMap<>();
        env.put("Environment", System.getProperty("env", "SIT"));
        env.put("API Version", "v1");
        env.put("Test Suite", "Osprey Search API");

        try (FileWriter writer = new FileWriter("allure-results/environment.properties")) {
            Properties props = new Properties();
            props.putAll(env);
            props.store(writer, "Environment Variables");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  //  @BeforeSuite
//    public void setupAllureEnvironment(Map<String, String> config) {
//        Properties props = new Properties();
//        // Test Data Paths
//        props.setProperty("Test Data Location","/Users/chinta.anusha/Desktop/OspreySearchAJIOAutomation/src/test/resources/SearchKeywords/Productsearch.yml");
//        props.setProperty("Test Data Files", "Productsearch.yml, SearchWithFilters.yml");
////        Environment.addEnvironmentValue("Test Data Location", "/Users/chinta.anusha/Desktop/OspreySearchAJIOAutomation/src/test/resources/SearchKeywords/Productsearch.yml");
////        Environment.addEnvironmentValue("Test Data Files", "Productsearch.yml, SearchWithFilters.yml");
//
//        // Project Configuration
//        props.setProperty("Project Name", "Osprey Search API Automation");
//        props.setProperty("Module", "Product Search");
//        props.setProperty("Test Environment", "SIT");
////        Environment.addEnvironmentValue("Project Name", "Osprey Search API Automation");
////        Environment.addEnvironmentValue("Module", "Product Search");
////        Environment.addEnvironmentValue("Test Environment", "QA");
//
//        // API Details
//        Environment.addEnvironmentValue("API Version", "v1");
//        Environment.addEnvironmentValue("Base URL", baseUrl);
//
//        // Test Execution Details
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Environment.addEnvironmentValue("Execution Start Time", dateFormat.format(new Date()));
//        Environment.addEnvironmentValue("Java Version", System.getProperty("java.version"));
      //  Properties props = new Properties();

        // Environment specific configuration
//        props.setProperty("Environment", ENVIRONMENT);
//        props.setProperty("Base URL", baseUrl);
//        props.setProperty("Database URL", config.get("database"));
//        props.setProperty("Connector", config.get("connectorURL"));
//
//        // Test Data Paths
//        props.setProperty("Test Data Location", "/Users/chinta.anusha/Desktop/OspreySearchAJIOAutomation/src/test/resources/SearchKeywords/Productsearch.yml");
//        props.setProperty("Test Data Files", "Productsearch.yml, SearchWithFilters.yml");
//
//        // Add to Allure report
//        Allure.addAttachment("Environment Variables", props.toString());
//    }


    @BeforeClass
    public void launchApp() throws FileNotFoundException {
        try {

            //propertyVar = System.getProperty("URL");
            propertyVar = "SIT";
            getEnv = Com.jioMart.base.Config.getProp(propertyVar);
            fqdn = getEnv.get("URL");
            org.apache.log4j.PropertyConfigurator
                    .configure(System.getProperty("user.dir") + "/src/test/java/log4j.properties");
            prop = new Properties();
            fis = new FileInputStream(System.getProperty("user.dir") + "/testdata/testdata.properties");
            prop.load(fis);
            cookie=prop.getProperty("api-key");
            map.put("baseUrl", prop.getProperty("baseUrl"));
            map.put("dbUrl", prop.getProperty("dbUrl"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public TestData getYMLData(String path, String testCaseName) {
        Yaml yaml = new Yaml();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        TestDataListYMLWrapper testData = yaml.loadAs(inputStream, TestDataListYMLWrapper.class);
        return testData.getTestDataMap().get(testCaseName);
    }

    public TestData readDataFromDataLoader(String path, String testCaseName) {
        testData = new TestdataLoader().getYMLData(path, testCaseName);
        return testData;
    }

    public static String getGoogleAPIBearerToken(){
        String bearerToken=null;
        try {
            String serviceAccountPath = System.getProperty("GOOGLE_APPLICATION_CREDENTIALS");
       //     GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new FileInputStream(serviceAccountPath);
            List<String> scopes = Arrays.asList("https://www.googleapis.com/auth/cloud-platform");
       //     credentials = credentials.createScoped(scopes);
      //      credentials.refreshIfExpired();
      //      credentials.refreshAccessToken();
       //     bearerToken= String.valueOf(credentials.getAccessToken().getTokenValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bearerToken;
    }

//        @BeforeSuite
//        public void setUp() throws Exception {
//            try{
//                // Make the database connection
//                String dbClass = "com.mysql.jdbc.Driver";
//                Class.forName(dbClass).newInstance();
//                // Get connection to DB
//                Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//                // Statement object to send the SQL statement to the Database
//                stmt = con.createStatement();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//    public void test() {
//        try{
//            String query = "select * from userinfo";
//            // Get the contents of userinfo table from DB
//            ResultSet res = stmt.executeQuery(query);
//            // Print the result untill all the records are printed
//            // res.next() returns true if there is any next record else returns false
//            while (res.next())
//            {
//                System.out.print(res.getString(1));
//                System.out.print("\t" + res.getString(2));
//                System.out.print("\t" + res.getString(3));
//                System.out.println("\t" + res.getString(4));
//            }
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Step("Generate pie chart for matched and unmatched counts")
    public static String createDoughnutChart(int MatchedCount, int UnMatchedCount,String keywords) throws IOException {

        int TotalItems = MatchedCount + UnMatchedCount;
        double matchedPercentage = ((double) MatchedCount / TotalItems) * 100;
        double unmatchedPercentage = ((double) UnMatchedCount / TotalItems) * 100;

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Matched Count (" + MatchedCount + ")", MatchedCount);
        dataset.setValue("Unmatched Count(" + UnMatchedCount + ")", UnMatchedCount);
        // dataset.setValue("Matched", MatchedCount);
        // dataset.setValue("Unmatched", UnMatchedCount);

        JFreeChart pieChart = ChartFactory.createRingChart("Product Name Verification for Keyword : " + keywords, // Chart
                // title

                dataset, // Dataset
                true, // include legend
                true, // Use tooltips
                false // Generate URLs?
        );

        RingPlot plot = (RingPlot) pieChart.getPlot();
        plot.setSectionDepth(0.30); // Set the thickness of the doughnut
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}")); // Label format

        plot.setSectionPaint("Matched (" + MatchedCount + ")", new ChartColor(0, 204, 51)); // Green
        plot.setSectionPaint("Unmatched (" + UnMatchedCount + ")", new ChartColor(255, 193, 7)); // Yellow

        // Customize the legend (side panel)
        LegendTitle legend = pieChart.getLegend();
        legend.setItemFont(new Font("Arial", Font.BOLD, 14)); // Set font for the legend
        legend.setPosition(RectangleEdge.RIGHT); // Move the legend to the right side

        String filePath = "target/pie_chart.png";
        ChartUtils.saveChartAsPNG(new File(filePath), pieChart, 700, 600);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Draw combined percentage in the center
        drawCombinedPercentage(filePath, matchedPercentage, TotalItems);// , unmatchedPercentage);
        addTotalCountAnnotation(pieChart, TotalItems);

        return filePath;
    }

    private static void addTotalCountAnnotation(JFreeChart pieChart, int totalItems) throws IOException {
        TextTitle totalCountSubtitle = new TextTitle("Total Items: " + totalItems);
        totalCountSubtitle.setFont(new Font("Arial", Font.BOLD, 10));
        totalCountSubtitle.setPaint(Color.BLACK);
        totalCountSubtitle.setPosition(RectangleEdge.TOP);
    }

    @Attachment(value = "Doughnut Chart", type = "image/png")
    public void attachPieChart(String doughnutChartFile) throws FileNotFoundException {
        File chart = new File(doughnutChartFile);
        Allure.addAttachment("Doughnut Chart", "image/png", new FileInputStream(chart), "png");
    }

    private static void drawCombinedPercentage(String ringChart, double matchedPercentage, int totalItems)
            throws IOException {

        BufferedImage chartImage = ImageIO.read(new File(ringChart));
        Graphics2D g2 = chartImage.createGraphics();

        // Set the font and color for the text
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.BLACK);
        String percentageText = String.format("%.1f%%", matchedPercentage);
        FontMetrics metrics = g2.getFontMetrics();

        int x = (chartImage.getWidth() - metrics.stringWidth(percentageText)) / 3;
        int y = (chartImage.getHeight() / 2) + metrics.getHeight() / 4; // Vertical center

        // Draw the text on the image
        g2.drawString(percentageText, x, y);
        g2.dispose(); // Release the graphics context

        // Save the modified image back to the file

        ImageIO.write(chartImage, "png", new File(ringChart));

    }

    // Method to convert the pie chart to a byte array
    // Method to convert chart to byte array
    private byte[] convertChartToByteArray(JFreeChart chart) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 600); // Adjust size if needed
        return baos.toByteArray();
    }

    // Attach the pie chart as an image in the Allure report
    @Attachment(value = "Pie Chart - Matched vs Unmatched", type = "image/png")
    public byte[] attachPieChart(byte[] chartBytes) {
        return chartBytes;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            Allure.addAttachment("Failure Details", result.getThrowable().getMessage());
        }
    }
}




