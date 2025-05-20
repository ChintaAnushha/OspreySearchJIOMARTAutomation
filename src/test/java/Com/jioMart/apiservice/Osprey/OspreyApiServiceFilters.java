package Com.jioMart.apiservice.Osprey;

import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.validateResponse.OspreySearch.OspreyAPIFiltersResponseValidator;
import Com.jioMart.validateResponse.OspreySearch.OspreyAPIResponseValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import Com.jioMart.base.ApiUrls;
import Com.jioMart.base.BaseScript;
import Com.jioMart.model.OspreySearch.OspreyApiRequest;
import Com.jioMart.model.survey.global.TestData;
import Com.jioMart.util.MicrositeYamlPathConstants;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import java.io.IOException;
import java.util.*;

@Listeners({ AllureTestNg.class })
public class OspreyApiServiceFilters extends BaseScript {

    private SoftAssert softAssert;

    ApiUrls apiUrls = new ApiUrls();
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    HttpResponse httpResponse = null;
    TestData testData = null;

    OspreyApiRequest ospreyAPIRequest = null;

    String ospreyApiRequestString = null;

    public OspreyApiResponse ospreyApiResponse = new OspreyApiResponse();
    OspreyAPIFiltersResponseValidator ospreyAPIFiltersResponseValidator = null;
   // OspreyAPIResponseValidator ospreyAPIResponseValidator = null;


//    @Step("Validate Osprey API with valid details")
//    @Description("Validates the Osprey API response with valid input parameters and verifies all response fields")
//    public void productSearchWithFilters() {
//        try {
//            SoftAssert softAssert = new SoftAssert();
//            this.testData = getYMLData(MicrositeYamlPathConstants.SEARCH_KEYWORDS_FILEPATH, MicrositeYamlPathConstants.SEARCH_KEYWORDS_DATAKEY);
//            ExcelReader excelReader = new ExcelReader();
//            //    List<String> queries = excelReader.readExcel("./OspreySearchAJIO/testdata/keywords.xlsx");
//       //     List<String> queries = excelReader.readExcel("./testdata/keywords.xlsx");
//            for (String query : queries) {
//                Map<String, String> testDataParams = testData.getOtherParams();
//                ospreyAPIRequest = new OspreyApiRequest();
//                ospreyAPIRequest.setQuery(query);
//                ospreyAPIRequest.setStore(testDataParams.get("store"));
//                String recordsPerPageString = testDataParams.get("records_per_page");
//
//// Convert to integer and set it
//                //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
//                ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
//
//                ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
//                System.out.println("Search request body: " + ospreyApiRequestString);
//                //  createRequest(softAssert);
//                executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
//                ospreyAPIResponseValidator = new OspreyAPIResponseValidator(ospreyApiResponse,testData);
//                ospreyAPIResponseValidator.validateSearchqueryResults(query,softAssert,query);
//                ospreyAPIResponseValidator.validateTop10Results(query);
//                //   OspreyAPIResponseValidator = new CreateSurveyResponseValidator(createSurveyResponseWrapper,testData);
//                //    createSurveyResponseValidator.validateCreatedSurveyResponse(softAssert);
//                softAssert.assertAll();
////                int MatchedCount = 0;
////                int UnMatchedCount = 0;
////                List<String> keywords = Collections.singletonList("");
////
////                createDoughnutChart(MatchedCount, UnMatchedCount, keywords);
////                String doughnutChartFile = "target/pie_chart.png";
////                attachPieChart(doughnutChartFile);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

@Step("Verify Osprey API product search with brand filter")
@Description("This test validates the API response when searching products with brand filter 'adidas'. Validates: brand filter application, result count, pagination, and product brand matching.")
    public void productSearchWithBrandFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
     //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
       //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setBrandFilter(testDataParams.get("values"));


            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
        //    executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
         //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateBrandFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with color filter")
    @Description("Test to verify product search functionality with color filter")
    public void productSearchWithColorFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_COLORFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set color filter
            ospreyAPIRequest.setColorFilter(testDataParams.get("values"));

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
        //    ospreyAPIFiltersResponseValidator.validateBasicResponse();
       //     ospreyAPIFiltersResponseValidator.validateColorFilterResponse();

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with price range filter")
    @Description("Test to verify product search functionality with price range filter")
    public void productSearchWithPriceRangeFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_PRICERANGEFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set price range filter
            ospreyAPIRequest.setPriceRangeFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
            //ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validatePriceRangeResponse();

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    @Step("Verify Osprey API product search with discount filter")
    @Description("Test to verify product search functionality with discount filter")
    public void productSearchWithDiscountFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_DISCOUNTFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(450);
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set discount filter
//            ospreyAPIRequest.setDiscountFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);


           // executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
      //      ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateDiscountResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with L0 category filter")
    @Description("Test to verify product search functionality with category L0 filter")
    public void productSearchWithL0CategoryFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_L0CATEGORY_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setL0CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
            //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateL0CategoryFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with L1 category filter")
    @Description("Test to verify product search functionality with category L1 filter")
    public void productSearchWithL1CategoryFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_L1CATEGORY_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setL1CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));


            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
            //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateL1CategoryFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with L2 category filter")
    @Description("Test to verify product search functionality with category L2 filter")
    public void productSearchWithL2CategoryFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_L2CATEGORY_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setL2CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
            //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateL2CategoryFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with L3 category filter")
    @Description("Test to verify product search functionality with category L3 filter")
    public void productSearchWithL3CategoryFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_L3CATEGORY_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setL3CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
            //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateL3CategoryFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with L4 category filter")
    @Description("Test to verify product search functionality with category L4 filter")
    public void productSearchWithL4CategoryFilter() {
        try {
            softAssert  = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_L4CATEGORY_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set brand filter using the new method
            ospreyAPIRequest.setL4CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));


            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }


            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            //  createRequest(softAssert);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse,testData);
            //   ospreyAPIFiltersResponseValidator.validateBasicResponse();
            ospreyAPIFiltersResponseValidator.validateL4CategoryFilterResponse(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);
            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with gender filter")
    @Description("Test to verify product search functionality with gender filter")
    public void productSearchWithGenderFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH,
                    MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_GENDERFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set gender filter
            ospreyAPIRequest.setGenderFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
        //    ospreyAPIFiltersResponseValidator.validateBasicResponse();
        //    ospreyAPIFiltersResponseValidator.validateGenderFilterResponse();

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with category filter")
    @Description("Test to verify product search functionality with category filter")
    public void productSearchWithL1L3CategoryFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_CATEGORYFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set L1L3 category filter
            ospreyAPIRequest.setL1L3CategoryFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
        //    ospreyAPIFiltersResponseValidator.validateBasicResponse();
        //    ospreyAPIFiltersResponseValidator.validateL1L3CategoryFilterResponse();

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API product search with Size filter")
    @Description("Test to verify product search functionality with Size filter")
    public void productSearchWithSizeFilter() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_SIZEFILTERS_DATAKEY);
            Map<String, String> testDataParams = testData.getOtherParams();

            ospreyAPIRequest = new OspreyApiRequest();
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));

            // Set Size filter
            ospreyAPIRequest.setSizeFilter(testDataParams.get("fieldName"), testDataParams.get("values"));

            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

            executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
        //    ospreyAPIFiltersResponseValidator.validateBasicResponse();
//            ospreyAPIFiltersResponseValidator.validateSizeFilterResponse();

            softAssert.assertAll();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API multiple filters")
    @Description("This test verifies the API response with multiple filters")
    public void productSearchWithMultipleFilters() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_MULTIPLEFILTERS_DATAKEY);

            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();

            // Set basic parameters
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
            //     ospreyAPIRequest.setRecordsperpage(Integer.parseInt(recordsPerPageString));
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            // Set page number
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));
           // Set filters

            List<OspreyApiRequest.Filter> filters = new ArrayList<>();
          //  OspreyApiRequest.Filter categoryfilter = new OspreyApiRequest.Filter();
           // ospreyAPIRequest.setFilters(filters);

            // category filter
            OspreyApiRequest.Filter categoryFilter = new OspreyApiRequest.Filter();
            categoryFilter.setFieldName(testDataParams.get("l0Category_field_name"));
            categoryFilter.setValues(Arrays.asList(testDataParams.get("category_values").split(",")));
            filters.add(categoryFilter);

            // brand filter
            OspreyApiRequest.Filter brandFilter = new OspreyApiRequest.Filter();
            brandFilter.setFieldName(testDataParams.get("brand_field_name"));
            brandFilter.setValues(Collections.singletonList(testDataParams.get("brand_value")));
            filters.add(brandFilter);

            ospreyAPIRequest.setFilters(filters);

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }

            // Execute request
            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Search request body: " + ospreyApiRequestString);

       //     executeRequestAndGetResponse(softAssert, ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);

            // Validate response
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
            //  ospreyAPIFiltersResponseValidator.validateBasicResponse();
             ospreyAPIFiltersResponseValidator.validateMultipleFilters(allDocs,
                     firstPageResponse.getFacetData(),
                     firstPageResponse.getNumFound(),
                     testData);

            softAssert.assertAll();
        } catch (Exception ex) {
            Assert.fail("Multiple filters test failed: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Step("Verify Osprey API One filter multiple values")
    @Description("This test verifies the API response with One filter multiple values")
    public void productSearchWithOneFilterMultipleValues() {
        try {
            softAssert = new SoftAssert();
            this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_ONEFILTERMULTIPLEVALUES_DATAKEY);

            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();

            // Set basic parameters
            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            ospreyAPIRequest.setStore(testDataParams.get("store"));
            //       String recordsPerPageString = testDataParams.get("records_per_page");
            ospreyAPIRequest.setRecordsperpage(450); // Fixed page size for proper pagination
            ospreyAPIRequest.setSortfield(testDataParams.get("sort_field"));
            ospreyAPIRequest.setSortOrder(testDataParams.get("sort_order"));

// Convert to integer and set it
            ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            ospreyAPIRequest.setPageNumber(Integer.parseInt(testDataParams.get("page_number")));    // Set page number

            // Set up gender filter
            List<OspreyApiRequest.Filter> filters = new ArrayList<>();
            OspreyApiRequest.Filter categoryFilter = new OspreyApiRequest.Filter();
            categoryFilter.setFieldName(testDataParams.get("categoryl3_field_name"));
            categoryFilter.setValues(Arrays.asList(testDataParams.get("category_values").split(",")));
//            genderFilter.setFieldName("genderfilter_en_string_mv");
//            genderFilter.setValues(Arrays.asList(testDataParams.get("gender_values").split(",")));
//            genderFilter.setOperator("OR");
            filters.add(categoryFilter);

            ospreyAPIRequest.setFilters(filters);

            String applicableRegions = testDataParams.get("applicable_regions");
            if (applicableRegions != null && !applicableRegions.trim().isEmpty()) {
                List<String> regionsList = Arrays.asList(applicableRegions.split("\\s*,\\s*"));
                ospreyAPIRequest.setApplicableRegions(regionsList);
                System.out.println("Setting applicable regions: " + String.join(", ", regionsList));
            } else {
                System.out.println("No applicable regions found in test data");
            }

            // Execute request
            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            System.out.println("Gender filter request body: " + ospreyApiRequestString);
         //   executeRequestAndGetResponse(softAssert, ospreyApiRequestString);

            OspreyApiResponse firstPageResponse = executeInitialOspreyRequest(softAssert);
            List<OspreyApiResponse.Doc> allDocs = executeOspreyApiRequest(softAssert,ospreyAPIRequest, ospreyApiRequestString, testData);


            // Validate response
            ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
            ospreyAPIFiltersResponseValidator.validateOneFilterMultipleValues(allDocs,
                    firstPageResponse.getFacetData(),
                    firstPageResponse.getNumFound(),
                    testData);

            softAssert.assertAll();
        } catch (Exception ex) {
            Assert.fail("Gender filter test failed: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    @Description("Verify Osprey API response when filters parameter is invalid type")
    public void ospreyAPIWithEmptyFilterListType() {
        try {
            softAssert = new SoftAssert();
            Allure.step("Loading test data", () -> {
                this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH,
                        MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_EMPTYFILTERS_DATAKEY);
            });

            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();
            ObjectMapper objectMapper = new ObjectMapper();
           ObjectNode rootNode = objectMapper.createObjectNode();

            // Convert empty string to empty list
          //  String filterStr = testDataParams.get("filters");
            List<OspreyApiRequest.Filter> filters = new ArrayList<>();
          //  String filters = testDataParams.get("filters");
//            if (filterStr != null && filterStr.trim().isEmpty()) {
//                ospreyAPIRequest.setFilters(filters);
//            }
            String store = testDataParams.get("store");

            Allure.step("Setting request parameters", () -> {
                ospreyAPIRequest.setQuery(testDataParams.get("query"));
                ospreyAPIRequest.setStore(testDataParams.get("store"));
                ospreyAPIRequest.setFilters(filters);
            });


//            // Custom serialization to handle filters as "" instead of []
//            ObjectNode requestNode = objectMapper.valueToTree(ospreyAPIRequest);
//            if (filterStr != null && filterStr.trim().isEmpty()) {
//                requestNode.put("filters", "");  // Set as empty string
//            }

          //  ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);

//// Mandatory fields from test data
//            rootNode.put("query", testDataParams.get("query"));
//            rootNode.put("store", testDataParams.get("store"));
//
//// Handle filters dynamically based on test data
            String filterStr = testDataParams.get("filters");
            String invalidNumericFilter = testDataParams.get("filters");
            if (filterStr != null) {
                if (filterStr.trim().isEmpty()) {
                    // Case: filters = "" in YAML -> we want filters: ""
                    rootNode.put("filters", "");
                } else if (filterStr.equals(invalidNumericFilter) || filterStr.matches("\\d+")) {
                    rootNode.put("filters", Integer.parseInt(filterStr));
                } else if (filterStr.trim().startsWith("[")) {
                    // Case: filters = [some list] as JSON string in YAML -> parse it
                    try {
                        JsonNode filterArray = objectMapper.readTree(filterStr);
                        rootNode.set("filters", filterArray);
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid filter list JSON: " + filterStr);
                    }
                } else {
                    // Case: fallback to string value
                    rootNode.put("filters", filterStr);
                }
            }



// Convert to JSON string for request
           ospreyApiRequestString = objectMapper.writeValueAsString(rootNode);
            System.out.println("Search request body: " + ospreyApiRequestString);
            Allure.attachment("Request Body", ospreyApiRequestString);

            Allure.step("Executing API request", () -> {
                executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            });

            Allure.step("Validating response", () -> {
                ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
          //      ospreyAPIFiltersResponseValidator.validateInvalidFilterListTypeResponse(filterStr, store);
            });

            softAssert.assertAll();
        } catch (Exception ex) {
            Allure.step("Test failed: " + ex.getMessage(), Status.FAILED);
            throw new RuntimeException(ex);
        }
    }

    @Description("Verify Osprey")
    public void ospreyAPIWithBooleanFilterValue(){
        try {
            softAssert = new SoftAssert();
            Allure.step("Loading test data", () -> {
                this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH,
                        MicrositeYamlPathConstants.PRODUCT_SEARCH_WITH_INVALIDBOOLEANDATA_DATAKEY);
            });

            Map<String, String> testDataParams = testData.getOtherParams();
            ospreyAPIRequest = new OspreyApiRequest();

            ospreyAPIRequest.setQuery(testDataParams.get("query"));
            String filterFromData = testDataParams.get("filters");
            Boolean filterValue = Boolean.valueOf(filterFromData);

            String store = testDataParams.get("store");

            // Set request parameters
            Allure.step("Setting request parameters", () -> {

                ospreyAPIRequest.setStore(store);
                //ospreyAPIRequest.setRecordsperpage(Integer.parseInt(testDataParams.get("records_per_page")));
            });

            // Create ObjectMapper and root node for custom JSON
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();

            rootNode.put("filters", filterValue);  // This will set as actual boolean

            // Execute request
         //   ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            // Convert to request string
            ospreyApiRequestString = objectMapper.writeValueAsString(rootNode);
            System.out.println("Search request body: " + ospreyApiRequestString);
            Allure.attachment("Request Body", ospreyApiRequestString);

            Allure.step("Executing API request", () -> {
                executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
            });

            Allure.step("Validating response", () -> {
                ospreyAPIFiltersResponseValidator = new OspreyAPIFiltersResponseValidator(ospreyApiResponse, testData);
            //    ospreyAPIFiltersResponseValidator.validateInvalidBooleanFilterTypeResponse(filterValue, store);
            });

            Allure.attachment("Response Body", ospreyApiResponse.toString());

            softAssert.assertAll();
        } catch (Exception ex) {
            Allure.step("Test failed: " + ex.getMessage(), Status.FAILED);
            Assert.fail("Invalid query type test failed: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }


    public void createRequest(SoftAssert softAssert) {
        Map<String, String> testDataParams = testData.getOtherParams();
        ospreyAPIRequest =new OspreyApiRequest();
        ospreyAPIRequest.setQuery(testDataParams.get("query"));
        ospreyAPIRequest.setStore(testDataParams.get("store"));
        try {
            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
            Allure.step("Add OspreySearch Request Body: " + ospreyApiRequestString);
        } catch (JsonProcessingException e) {
            softAssert.assertTrue(false, "Failed! - Error while parsing request object. Please refer Stack trace: " + e.getMessage());
            Allure.step(e.getMessage());
        }
    }

    public void executeRequestAndGetResponse(SoftAssert softAssert,String request) {
        String response = null;
//        queryParam = new HashMap<String, String>();
        httpResponse=apiUrls.postOspreyAPI(testData,ospreyApiRequestString);
        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), Integer.parseInt(testData.getOtherParams().get("expectedStatusCode")), "FAILED! API did not respond with status 200");
        try {

            response = EntityUtils.toString(httpResponse.getEntity());
            Allure.step("API Response::::: " + response);
            if (httpResponse.getStatusLine().getStatusCode() == Integer.parseInt(testData.getOtherParams().get("expectedStatusCode"))) {
                Allure.step("OspreySearch ResponseCODE:::::  " + httpResponse.getStatusLine().getStatusCode());
                ospreyApiResponse = objectMapper.readValue(response,
                        OspreyApiResponse.class);
            } else {
                Allure.step("Http Response is not OK: " + httpResponse.getStatusLine().getStatusCode());
                softAssert.assertTrue(false, "Failed! UnExpected error code:::::" + httpResponse.getStatusLine().getStatusCode());
            }

        } catch (final IOException e) {
            softAssert.assertTrue(false, "Failed! - Error while processing OspreySearch response. Please refer Stack trace: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public OspreyApiResponse executeInitialOspreyRequest(SoftAssert softAssert) {
        try {
            httpResponse = apiUrls.postOspreyAPI(testData, ospreyApiRequestString);

            int expectedStatusCode = Integer.parseInt(testData.getOtherParams().get("expectedStatusCode"));
            int actualStatusCode = httpResponse.getStatusLine().getStatusCode();

            softAssert.assertEquals(actualStatusCode, expectedStatusCode,
                    "FAILED! API did not respond with expected status code");

            if (actualStatusCode == expectedStatusCode) {
                String response = EntityUtils.toString(httpResponse.getEntity());
                Allure.step("Initial API Response::::: " + response);
                return objectMapper.readValue(response, OspreyApiResponse.class);
            } else {
                softAssert.fail("Unexpected status code: " + actualStatusCode);
            }
        } catch (Exception e) {
            softAssert.fail("Failed during initial API request: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public List<OspreyApiResponse.Doc> executeOspreyApiRequest(
            SoftAssert softAssert,
            OspreyApiRequest ospreyAPIRequest,
            String ospreyApiRequestString,
            TestData testData) {

        List<OspreyApiResponse.Doc> allDocs = new ArrayList<>();

        try {
            // First page
            HttpResponse httpResponse = apiUrls.postOspreyAPI(testData, ospreyApiRequestString);
            int actualStatusCode = httpResponse.getStatusLine().getStatusCode();
            softAssert.assertEquals(actualStatusCode, 200, "First page call failed!");

            String response = EntityUtils.toString(httpResponse.getEntity());
            OspreyApiResponse firstPageResponse = objectMapper.readValue(response, OspreyApiResponse.class);

            List<OspreyApiResponse.Doc> firstPageDocs = firstPageResponse.getDocs();
            int numFoundCount = firstPageResponse.getNumFound();
            int totalPages = (int) Math.ceil((double) numFoundCount / 450);

            allDocs.addAll(firstPageDocs);

            // Subsequent pages
            for (int page = 2; page <= totalPages; page++) {
                ospreyAPIRequest.setStart((page - 1) * 450);
                String paginatedRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
                httpResponse = apiUrls.postOspreyAPI(testData, paginatedRequestString);

                actualStatusCode = httpResponse.getStatusLine().getStatusCode();
                if (actualStatusCode == 200) {
                    response = EntityUtils.toString(httpResponse.getEntity());
                    OspreyApiResponse paginatedResponse = objectMapper.readValue(response, OspreyApiResponse.class);
                    allDocs.addAll(paginatedResponse.getDocs());
                } else {
                    softAssert.fail("Pagination request failed at page " + page + " with status: " + actualStatusCode);
                    break;
                }
            }

            // After all pages
            Allure.step("Total documents fetched before trimming: " + allDocs.size());
            System.out.println("Total Documents before trimming: " + allDocs.size());

            // Trim to match numFound
            if (allDocs.size() > numFoundCount) {
                allDocs = allDocs.subList(0, numFoundCount);
            }

            Allure.step("Final trimmed document count: " + allDocs.size());
            System.out.println("Total Documents after trimming: " + allDocs.size());

        } catch (Exception e) {
            softAssert.fail("Pagination error: " + e.getMessage());
            e.printStackTrace();
        }

        return allDocs;
    }
}




