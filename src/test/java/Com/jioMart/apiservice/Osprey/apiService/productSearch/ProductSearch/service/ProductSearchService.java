package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearch.service;

import Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearch.tasks.ValidateProductSearchResponse;
import Com.jioMart.base.ApiUrls;
import Com.jioMart.base.BaseScript;
import Com.jioMart.model.OspreySearch.OspreyApiRequest;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.productSearch.ProductSearch.request.ProductSearchRequestWrapper;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchResponse;
import Com.jioMart.model.survey.global.TestData;
import Com.jioMart.util.MicrositeYamlPathConstants;
import Com.jioMart.validateResponse.OspreySearch.OspreyAPIResponseValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Map;

@Log4j
public class ProductSearchService extends BaseScript {


//    public ProductSearchService() {
//        this.productSearchRequest = new CreateAndExecuteProductSearchRequest();
//    //    this.ospreyApiService = new OspreyApiService();
//    }
//
//    CreateAndExecuteProductSearchRequest productSearchRequest = new CreateAndExecuteProductSearchRequest();
//    ValidateProductSearchResponse validateProductSearchResponse;
//    ProductSearchResponse productSearchResponse;
//    String query = "Shirts";
//    String misspelledQuery = "Potato";
//    String synonymsQuery = "fridge";
//    String contextualQuery = "Mouse Pad";
//    String vernacularQuery = "Karam";
//
//  //  OspreyApiRequest ospreyAPIRequest = null;
//
//  //  String ospreyApiRequestString = null;
//   // TestData testData = null;
//
// //   public OspreyApiResponse ospreyApiResponse = new OspreyApiResponse();
//  //  OspreyAPIResponseValidator ospreyAPIResponseValidator = null;
//  //  OspreyApiService ospreyApiService = new OspreyApiService();
//
//    public void executeProductSearchAPIOk() throws JsonProcessingException {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        try{
//            // Load test data
//      //  testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_DATAKEY);
//        readDataFromDataLoader(MicrositeYamlPathConstants.PRODUCT_SEARCH_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_DATAKEY);
//
//            // Create and execute request
//        productSearchRequest.createAPIURL();
//        productSearchRequest.createRequest(softAssert);
//        System.out.println("Search request body: " + productSearchRequest);
//
//            // Execute API and get response
//       //    HttpResponse httpResponse = ospreyApiService.executeRequestAndGetResponse(testData,
//         //           CreateAndExecuteProductSearchRequest.getRequestString());
//
//        //  createRequest(softAssert);
//   //     ospreyApiService.executeRequestAndGetResponse(softAssert, ospreyApiRequestString);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//      //  ospreyAPIResponseValidator = new OspreyAPIResponseValidator(ospreyApiResponse,testData);
//      //  validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//
//            // Validate response status
//        //    validateResponseStatus(httpResponse, softAssert);
//
//            // Validate response
//            validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//            validateProductSearchResponse.validateSearchResults();
//     //       validateProductSearchResponse.
//      //  validateProductSearchResponse.validateResponse();
//      //  validateProductSearchResponse.validateTop12Results(query);
//        softAssert.assertAll();
//    } catch (Exception e) {
//            log.error("Error in product search execution: " + e.getMessage());
//            softAssert.fail("Test execution failed: " + e.getMessage());
//        } finally {
//            softAssert.assertAll();
//        }
//   }
//
//    public void executeProductSearchAPIWithInvalidQuery() {
//
////        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        //  this.testData = getYMLData(MicrositeYamlPathConstants.PRODUCT_SEARCH_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_DATAKEY);
//        readDataFromDataLoader(MicrositeYamlPathConstants.PRODUCT_SEARCH_FILEPATH, MicrositeYamlPathConstants.PRODUCT_SEARCH_INVALID_DATAKEY);
//        productSearchRequest.createAPIURL();
//        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        validateProductSearchResponse.validateTop12Results(query);
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithoutQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_EMPTY_DATAKEY);
//        productSearchRequest.createAPIURL();
//     //   productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponseforWithOutQuery();
//        softAssert.assertAll();
//    }
//
//     public void executeProductSearchAPIWithInvalidStore() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_INVALID_STORE_DATAKEY);
//        productSearchRequest.createAPIURL();
//        //   productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithNoStore() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_NO_STORE_DATAKEY);
//        productSearchRequest.createAPIURL();
//        //   productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchWithoutVisitorId() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITHOUT_VISITOR_ID_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithMisspelledQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_MISSPELLED_QUERY_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        validateProductSearchResponse.validateSearchQueryResult(misspelledQuery,"Misspelled");
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithSynonymsQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_SYNONYMS_QUERY_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        validateProductSearchResponse.validateSearchQueryResult(synonymsQuery,"Synonyms");
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithContextualQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_CONTEXTUAL_QUERY_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        validateProductSearchResponse.validateSearchQueryResult(contextualQuery,"Contextual");
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIWithVernacularQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_VERNACULAR_QUERY_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
//        validateProductSearchResponse.validateResponse();
//        validateProductSearchResponse.validateSearchQueryResult(vernacularQuery,"Vernacular");
//        softAssert.assertAll();
//    }
//
//
//    public void executeProductSearchAPIBoostQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_BOOST_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
////        validateProductSearchResponse.validateResponse();
//      //  validateProductSearchResponse.validateBoostFlow();
//        softAssert.assertAll();
//    }
//
//    public void executeProductSearchAPIBuryQuery() {
//        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
//        SoftAssert softAssert = new SoftAssert();
//        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_BURY_DATAKEY);
////        productSearchRequest.createAPIURL();
////        productSearchRequest.createRequest(softAssert);
//        productSearchRequest.executeRequestAndGetResponse(softAssert);
//        productSearchResponse = productSearchRequest.productSearchResponse;
//        validateProductSearchResponse = new ValidateProductSearchResponse(productSearchResponse, softAssert);
    /// /        validateProductSearchResponse.validateResponse();
//     //   validateProductSearchResponse.validateBuryFlow();
//        softAssert.assertAll();
//    }

    ApiUrls apiUrls = new ApiUrls();
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    HttpResponse httpResponse = null;
    TestData testData = null;

    ProductSearchRequestWrapper productSearchRequestWrapper = null;
    //   OspreyApiRequest ospreyAPIRequest = null;

    String productSearchRequestWrapperString = null;
    //  String ospreyApiRequestString = null;

//    public OspreyApiResponse ospreyApiResponse = new OspreyApiResponse();
//    OspreyAPIResponseValidator ospreyAPIResponseValidator = null;

    public ProductSearchResponse productSearchResponse = new ProductSearchResponse();
    ValidateProductSearchResponse validateProductSearchResponse = null;
}

//    public void ospreyAPIWithValidDetails() {
//        try {
//            SoftAssert softAssert = new SoftAssert();
//            this.testData = getYMLData(MicrositeYamlPathConstants.SEARCH_KEYWORDS_FILEPATH, MicrositeYamlPathConstants.SEARCH_KEYWORDS_DATAKEY);
//       //     ExcelReader excelReader = new ExcelReader();
//            //    List<String> queries = excelReader.readExcel("./OspreySearchAJIO/testdata/keywords.xlsx");
//         //   List<String> queries = excelReader.readExcel("./testdata/keywords.xlsx");
//   //         for (String query : queries) {
//                Map<String, String> testDataParams = testData.getOtherParams();
//            productSearchRequestWrapper = new productSearchRequestWrapper();
//            productSearchRequestWrapper.setQuery(query);
//            productSearchRequestWrapper.setStore(testDataParams.get("store"));
//                String recordsPerPageString = testDataParams.get("records_per_page");
//
//// Convert to integer and set it
//                //    ospreyAPIRequest.setRecordsPerPage(Integer.parseInt(recordsPerPageString));
//            productSearchRequestWrapper.setRecordsperpage(Integer.parseInt(recordsPerPageString));
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
//
//    public void createRequest(SoftAssert softAssert) {
//        Map<String, String> testDataParams = testData.getOtherParams();
//        ospreyAPIRequest =new OspreyApiRequest();
//        ospreyAPIRequest.setQuery(testDataParams.get("query"));
//        ospreyAPIRequest.setStore(testDataParams.get("store"));
//
//        try {
//            ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
//            Allure.step("Add OspreySearch Request Body: " + ospreyApiRequestString);
//        } catch (JsonProcessingException e) {
//            softAssert.assertTrue(false, "Failed! - Error while parsing request object. Please refer Stack trace: " + e.getMessage());
//            Allure.step(e.getMessage());
//        }
//    }
//
//    public void executeRequestAndGetResponse(SoftAssert softAssert,String request) {
//        String response = null;
////        queryParam = new HashMap<String, String>();
//        httpResponse=apiUrls.postOspreyAPI(testData,ospreyApiRequestString);
//        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), Integer.parseInt(testData.getOtherParams().get("expectedStatusCode")), "FAILED! API did not respond with status 200");
//        try {
//
//            response = EntityUtils.toString(httpResponse.getEntity());
//            Allure.step("API Response::::: " + response);
//            if (httpResponse.getStatusLine().getStatusCode() == Integer.parseInt(testData.getOtherParams().get("expectedStatusCode"))) {
//                Allure.step("OspreySearch ResponseCODE:::::  " + httpResponse.getStatusLine().getStatusCode());
//                ospreyApiResponse = objectMapper.readValue(response,
//                        OspreyApiResponse.class);
//            } else {
//                Allure.step("Http Response is not OK: " + httpResponse.getStatusLine().getStatusCode());
//                softAssert.assertTrue(false, "Failed! UnExpected error code:::::" + httpResponse.getStatusLine().getStatusCode());
//            }
//
//        } catch (final IOException e) {
//            softAssert.assertTrue(false, "Failed! - Error while processing OspreySearch response. Please refer Stack trace: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
//
//}
