package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearch.tasks;

import Com.jioMart.base.ApiUrls;
import Com.jioMart.model.OspreySearch.OspreyApiRequest;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.survey.global.TestData;
import Com.jioMart.validateResponse.OspreySearch.OspreyAPIResponseValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import Com.jioMart.base.BaseScript;
import Com.jioMart.enums.HttpMethod;
import Com.jioMart.enums.YamlKeys;
import Com.jioMart.model.productSearch.ProductSearch.request.ProductSearchRequestWrapper;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchErrorResponse;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchResponse;
import Com.jioMart.util.ApiMethodCalls;
import Com.jioMart.util.ObjectMapperWrapper;
import io.qameta.allure.Allure;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.HashMap;

@Log4j
public class CreateAndExecuteProductSearchRequest extends BaseScript {

      ApiUrls apiUrls = new ApiUrls();
//    this.ApiUrls = new ApiUrls();
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);


//    HttpResponse httpResponse = null;
    TestData testData = null;


  //  OspreyApiRequest ospreyAPIRequest = null;

  //  String ospreyApiRequestString = null;

 //   public OspreyApiResponse ospreyApiResponse = new OspreyApiResponse();
 //   OspreyAPIResponseValidator ospreyAPIResponseValidator = null;

 //   ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

 //   ApiMethodCalls apiMethodCalls = new ApiMethodCalls();
    HttpResponse httpResponse = null;
    String requestBodyString = "";
    String apiCompleteURL = "";
    public ProductSearchResponse productSearchResponse = null;

    public ProductSearchErrorResponse productSearchErrorResponse = null;

    ProductSearchRequestWrapper productSearchRequestWrapper;

    public void createAPIURL() {
        fqdn = testData.getUrls().get("searchBaseUrl");
        apiCompleteURL = fqdn + testData.getUrls().get(YamlKeys.APIENDPOINT.toString());
        log.info("API URL created: " + apiCompleteURL);
    }


    public void createRequest( SoftAssert softAssert)  {
        try{
        requestBodyString = testData.getOtherParams().get(YamlKeys.REQUESTJSONSTRING.toString());
    //    ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
      //  System.out.println("Search request body: " + ospreyApiRequestString);
            log.info("Request body created: " + requestBodyString);
       // try {
         //   ospreyApiRequestString = objectMapper.writeValueAsString(ospreyAPIRequest);
        } catch (Exception e) {
            log.error("Error creating request: " + e.getMessage());
            softAssert.fail("Failed to create request: " + e.getMessage());
        }
    //    productSearchRequestWrapper = new ObjectMapperWrapper<ProductSearchRequestWrapper>(requestBodyString, ProductSearchRequestWrapper.class, softAssert).getObj();
////        productSearchRequestWrapper.setQuery(query);
////        productSearchRequestWrapper.setJcsVisitorId(visitorId);
    //    requestBodyString = new ObjectMapperWrapper<ProductSearchRequestWrapper>(productSearchRequestWrapper, softAssert).getStringValue();
      //  System.out.println("request body string is:::" + requestBodyString);
    }

  //  public void executeRequestAndGetResponse(SoftAssert softAssert) {
    //   reqHeaderMap = testData.getHeadersMap();
//        int expectedStatusCode = Integer.parseInt(testData.getOtherParams().get(YamlKeys.EXPECTEDSTATUSCODE.toString()));
  //      httpResponse = apiMethodCalls.perfromAPICall(requestBodyString, apiCompleteURL, HttpMethod.POST);
//
//        // Expected status code check
//        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), expectedStatusCode, "FAILED! API did not respond with expected status code");
//        if (httpResponse.getStatusLine().getStatusCode() == expectedStatusCode) {
////            Header[] headers = httpResponse.getAllHeaders();
//            if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                productSearchResponse = new ObjectMapperWrapper<ProductSearchResponse>(httpResponseEntity, ProductSearchResponse.class, softAssert).getObj();
//            } else {
//                productSearchErrorResponse = new ObjectMapperWrapper<ProductSearchErrorResponse>(httpResponseEntity, ProductSearchErrorResponse.class, softAssert).getObj();
//            }
//        } else {
//            log.error("Received unexpected status code !!!");
//            softAssert.assertTrue(false, "Failed! Received unexpected status code::: " + httpResponse.getStatusLine().getStatusCode());
//        }
//    }
//
     //      try{
     //    String response = null;

       // queryParam = new HashMap<String, String>();
      //  httpResponse=apiUrls.postOspreyAPI(testData,ospreyApiRequestString);
//              httpResponse=apiUrls.postOspreyAPI(testData,requestBodyString);
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

public void executeRequestAndGetResponse(SoftAssert softAssert) {
    try {
        httpResponse = apiUrls.postOspreyAPI(testData, requestBodyString);
        validateResponseStatus(softAssert);
        processResponse(softAssert);
    } catch (Exception e) {
        log.error("Error executing request: " + e.getMessage());
        softAssert.fail("Request execution failed: " + e.getMessage());
    }
}

    private void validateResponseStatus(SoftAssert softAssert) {
        int actualStatus = httpResponse.getStatusLine().getStatusCode();
        int expectedStatus = Integer.parseInt(testData.getOtherParams().get("expectedStatusCode"));

        softAssert.assertEquals(actualStatus, expectedStatus,
                "FAILED! API did not respond with expected status code");

        Allure.step("Response Status Code: " + actualStatus);
    }

    private void processResponse(SoftAssert softAssert) throws IOException {
        String response = EntityUtils.toString(httpResponse.getEntity());
        Allure.step("API Response: " + response);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            productSearchResponse = objectMapper.readValue(response, ProductSearchResponse.class);
        } else {
            log.error("Unexpected response status: " + httpResponse.getStatusLine().getStatusCode());
            softAssert.fail("Failed! Unexpected response status");
        }
    }

    public void setTestData(TestData testData) {
        this.testData = testData;
    }

    public String getRequestString() {
        try {
            if (requestBodyString == null || requestBodyString.isEmpty()) {
                requestBodyString = testData.getOtherParams().get(YamlKeys.REQUESTJSONSTRING.toString());
            }

            // Validate JSON format
            objectMapper.readTree(requestBodyString);

            log.info("Request body: " + requestBodyString);
            return requestBodyString;

        } catch (JsonProcessingException e) {
            log.error("Error processing request string: " + e.getMessage());
            throw new RuntimeException("Invalid JSON request string", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
      //  return requestBodyString;
    }

