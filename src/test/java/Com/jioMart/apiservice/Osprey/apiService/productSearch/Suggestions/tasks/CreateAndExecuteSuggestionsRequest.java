package Com.jioMart.apiservice.Osprey.apiService.productSearch.Suggestions.tasks;

import Com.jioMart.base.BaseScript;
import Com.jioMart.enums.HttpMethod;
import Com.jioMart.enums.YamlKeys;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.Suggestions.response.SuggestionsResponse;
import Com.jioMart.util.ApiMethodCalls;
import Com.jioMart.util.ObjectMapperWrapper;
import lombok.extern.log4j.Log4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.testng.asserts.SoftAssert;

@Log4j
public class CreateAndExecuteSuggestionsRequest extends BaseScript {

    ApiMethodCalls apiMethodCalls = new ApiMethodCalls();
    HttpResponse httpResponse = null;
    String requestBodyString = "";
    String apiCompleteURL = "";
    public SuggestionsResponse suggestionsResponse = null;

    public void createAPIURL(){
        fqdn = testData.getUrls().get("searchBaseUrl");
        apiCompleteURL = fqdn + testData.getUrls().get(YamlKeys.APIENDPOINT.toString());
    }

    public void createRequest(){
        requestBodyString = testData.getOtherParams().get(YamlKeys.REQUESTJSONSTRING.toString());
    }

    public void executeRequestAndGetResponse(SoftAssert softAssert){
        reqHeaderMap = testData.getHeadersMap();
        int expectedStatusCode = Integer.parseInt(testData.getOtherParams().get(YamlKeys.EXPECTEDSTATUSCODE.toString()));
        httpResponse = apiMethodCalls.perfromAPICall(requestBodyString, apiCompleteURL, HttpMethod.GET);

        // Expected status code check
        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), expectedStatusCode, "FAILED! API did not respond with expected status code");
        if (httpResponse.getStatusLine().getStatusCode() == expectedStatusCode) {
            Header[] headers = httpResponse.getAllHeaders();
            suggestionsResponse = new ObjectMapperWrapper<SuggestionsResponse>(httpResponseEntity, SuggestionsResponse.class,softAssert).getObj();
        } else {
            log.error("Received unexpected status code !!!");
            softAssert.assertTrue(false, "Failed! Received unexpected status code::: " + httpResponse.getStatusLine().getStatusCode());
        }
    }
}
