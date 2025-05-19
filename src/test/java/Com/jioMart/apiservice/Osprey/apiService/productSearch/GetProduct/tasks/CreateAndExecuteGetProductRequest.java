package Com.jioMart.apiservice.Osprey.apiService.productSearch.GetProduct.tasks;

import Com.jioMart.base.BaseScript;
import Com.jioMart.enums.HttpMethod;
import Com.jioMart.enums.YamlKeys;
import Com.jioMart.model.productSearch.GetProduct.response.GetProductResponse;
import Com.jioMart.util.ApiMethodCalls;
import Com.jioMart.util.ObjectMapperWrapper;
import lombok.extern.log4j.Log4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.testng.asserts.SoftAssert;

@Log4j
public class CreateAndExecuteGetProductRequest extends BaseScript {
    ApiMethodCalls apiMethodCalls = new ApiMethodCalls();
    HttpResponse httpResponse = null;
    String requestBodyString = "";
    String apiCompleteURL = "";
    public GetProductResponse getProductResponse =null;
    public void createAPIURL() {
        fqdn = testData.getUrls().get("searchBaseUrl");
        apiCompleteURL = fqdn + testData.getUrls().get(YamlKeys.APIENDPOINT.toString())+productId;
    }

    public void createRequest() {
    }

    public void executeRequestAndGetResponse(SoftAssert softAssert) {

        reqHeaderMap = testData.getHeadersMap();
        int expectedStatusCode = Integer.parseInt(testData.getOtherParams().get(YamlKeys.EXPECTEDSTATUSCODE.toString()));
        httpResponse = apiMethodCalls.perfromGoogleAPICall(requestBodyString, apiCompleteURL, HttpMethod.GET);
        // Expected status code check
        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), expectedStatusCode, "FAILED! API did not respond with expected status code");
        if (httpResponse.getStatusLine().getStatusCode() == expectedStatusCode) {
            Header[] headers = httpResponse.getAllHeaders();
            getProductResponse = new ObjectMapperWrapper<GetProductResponse>(httpResponseEntity, GetProductResponse.class,softAssert).getObj();
        } else {
            log.error("Received unexpected status code !!!");
            softAssert.assertTrue(false, "Failed! Received unexpected status code::: " + httpResponse.getStatusLine().getStatusCode());
        }
    }
}
