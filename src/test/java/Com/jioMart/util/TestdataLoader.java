package Com.jioMart.util;

import Com.jioMart.model.AutoComplete.request.AutoCompleteRequest;
import Com.jioMart.model.survey.global.TestData;
import Com.jioMart.model.survey.global.TestDataListYMLWrapper;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
//import com.jio.jiomarket.model.Search.AutoComplete.request.AutoCompleteRequest;
//import com.jio.jiomarket.model.global.TestData;
//import com.jio.jiomarket.model.testdata.TestDataListYMLWrapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaml.snakeyaml.Yaml;

import Com.jioMart.util.RandomUtil;
import java.io.*;
import java.util.ArrayList;

public class TestdataLoader {

    public JsonObject loadTestdata() {
        JsonParser parser = new JsonParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(System.getProperty("user.dir")
                    + "/testdata/testdata.JSON"));
        } catch (JsonIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (JsonObject) obj;
    }

    // Returns TestData object stored in YAML.
    // Using YAML to store test data in a formatted manner with multiple test data as object.
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

    public ArrayList<AutoCompleteRequest> getAutoCompleteExcelData() {
        String filePath = "src/test/resources/xlsx/AutoCompleteTestData.xlsx";
        if (null != System.getProperty("filePath"))
            filePath = System.getProperty("filePath");
        ArrayList<AutoCompleteRequest> autoCompleteRequestList = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(new File(filePath));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //I've Header and I'm ignoring header for that I've +1 in loop
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                reviewsIntelligenceRequestWrapper = new ReviewsIntelligenceRequestWrapper();
                AutoCompleteRequest autoCompleteRequest = new AutoCompleteRequest();
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                autoCompleteRequest.setQuery(cell.getStringCellValue());
                autoCompleteRequest.setPartnerId("jiomart");
                autoCompleteRequest.setVisitorId(RandomUtil.getAlphaNumericString(5));
                autoCompleteRequestList.add(autoCompleteRequest);
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return autoCompleteRequestList;
    }
}
