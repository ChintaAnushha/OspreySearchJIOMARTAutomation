package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearchWithFilter.tasks;

import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import Com.jioMart.base.BaseScript;
import Com.jioMart.enums.HttpMethod;
import Com.jioMart.enums.YamlKeys;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchErrorResponse;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.request.ProductSearchRequestWithFiltersRequest;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.response.ProductSearchWithFiltersResponse;
import Com.jioMart.util.ApiMethodCalls;
import Com.jioMart.util.ObjectMapperWrapper;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.testng.asserts.SoftAssert;

import static Com.jioMart.base.BaseScript.testData;

@Log4j
public class CreateAndExecuteProductSearchWithFiltersRequest extends BaseScript {

    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    ApiMethodCalls apiMethodCalls = new ApiMethodCalls();
    HttpResponse httpResponse = null;
    String requestBodyString = "";
    String apiCompleteURL = "";
    public ProductSearchWithFiltersResponse productSearchWithFiltersResponse = null;

    ProductSearchErrorResponse productSearchErrorResponse = null;
    ProductSearchRequestWithFiltersRequest productSearchRequestWithFiltersRequest;
    OspreyApiResponse ospreyApiResponse = null;

    public void createAPIURL() {
        fqdn = testData.getUrls().get("searchBaseUrl");
        apiCompleteURL = fqdn + testData.getUrls().get(YamlKeys.APIENDPOINT.toString());
    }

    public void createRequest(SoftAssert softAssert) {
        requestBodyString = testData.getOtherParams().get(YamlKeys.REQUESTJSONSTRING.toString());
        productSearchRequestWithFiltersRequest = new ObjectMapperWrapper<ProductSearchRequestWithFiltersRequest>(requestBodyString, ProductSearchRequestWithFiltersRequest.class, softAssert).getObj();

//        if (value != "" && setKey != "") {
//            List<ProductSearchRequestWithFiltersRequest.Facet> facetList = new ArrayList<>();
//            ProductSearchRequestWithFiltersRequest.Facet facet = new ProductSearchRequestWithFiltersRequest.Facet();
//            facet.setKey(setKey);
//            facet.setValues(Collections.singletonList(value));
//            facetList.add(facet);
//            productSearchRequestWithFiltersRequest.setFacets(facetList);
//        }
//        List<ProductSearchRequestWithFiltersRequest.NumericFacet> numericFacetList = new ArrayList<>();
//        if (numericFacetValue != "" && numericKey != "") {
//            ProductSearchRequestWithFiltersRequest.NumericFacet numericFacet = new ProductSearchRequestWithFiltersRequest.NumericFacet();
//            numericFacet.setKey(numericKey);
//            numericFacet.setMax(numericFacetValue);
//            numericFacetList.add(numericFacet);
//            productSearchRequestWithFiltersRequest.setNumericFacets(numericFacetList);
//        }
//        productSearchRequestWithFiltersRequest.setQuery(query);
//        productSearchRequestWithFiltersRequest.setJcsVisitorId(visitorId);

        requestBodyString = new ObjectMapperWrapper<ProductSearchRequestWithFiltersRequest>(productSearchRequestWithFiltersRequest, softAssert).getStringValue();
        System.out.println("request body string is:::" + requestBodyString);
    }

    public void executeRequestAndGetResponse(SoftAssert softAssert) {
        reqHeaderMap = testData.getHeadersMap();
        int expectedStatusCode = Integer.parseInt(testData.getOtherParams().get(YamlKeys.EXPECTEDSTATUSCODE.toString()));
        httpResponse = apiMethodCalls.perfromAPICall(requestBodyString, apiCompleteURL, HttpMethod.POST);

        // Expected status code check
        softAssert.assertEquals(httpResponse.getStatusLine().getStatusCode(), expectedStatusCode, "FAILED! API did not respond with expected status code");
        if (httpResponse.getStatusLine().getStatusCode() == expectedStatusCode) {
//            Header[] headers = httpResponse.getAllHeaders();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                productSearchWithFiltersResponse = new ObjectMapperWrapper<ProductSearchWithFiltersResponse>(httpResponseEntity, ProductSearchWithFiltersResponse.class, softAssert).getObj();
            } else {
                productSearchErrorResponse = new ObjectMapperWrapper<ProductSearchErrorResponse>(httpResponseEntity, ProductSearchErrorResponse.class, softAssert).getObj();
            }
        } else {
            log.error("Received unexpected status code !!!");
            softAssert.assertTrue(false, "Failed! Received unexpected status code::: " + httpResponse.getStatusLine().getStatusCode());
        }
    }

}
