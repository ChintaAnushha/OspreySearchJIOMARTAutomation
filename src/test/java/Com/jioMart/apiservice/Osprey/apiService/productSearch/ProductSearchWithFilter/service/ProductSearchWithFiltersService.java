package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearchWithFilter.service;

import Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearchWithFilter.tasks.CreateAndExecuteProductSearchWithFiltersRequest;
import Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearchWithFilter.tasks.ValidateProductSearchWithFiltersResponse;
import Com.jioMart.base.BaseScript;
import Com.jioMart.constants.CentralisedSearchConstants;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.response.ProductSearchWithFiltersResponse;
import lombok.extern.log4j.Log4j;
import org.testng.asserts.SoftAssert;

@Log4j
public class ProductSearchWithFiltersService extends BaseScript {

    private final CreateAndExecuteProductSearchWithFiltersRequest productSearchRequest;
    private ValidateProductSearchWithFiltersResponse validateProductSearchWithFiltersResponse;
    private ProductSearchWithFiltersResponse productSearchWithFiltersResponse;

    public ProductSearchWithFiltersService() {
        this.productSearchRequest = new CreateAndExecuteProductSearchWithFiltersRequest();
    }

//    CreateAndExecuteProductSearchWithFiltersRequest productSearchRequest = new CreateAndExecuteProductSearchWithFiltersRequest();
//    ValidateProductSearchWithFiltersResponse validateProductSearchWithFiltersResponse;
//    ProductSearchWithFiltersResponse productSearchWithFiltersResponse;
    OspreyApiResponse ospreyApiResponse = null;
    Integer price=125000;
    Integer discount=10;

    public void executeProductSearchWithFiltersAPIOk() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
      //  try{
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
       //     productSearchWithFiltersResponse = productSearchRequest.getProductSearchWithFiltersResponse();
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
      //  ospreyApiResponse =
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeProductSearchWithCategoryL4Filter() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_CATEGORY_L4_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeProductSearchWithPriceFilter() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_PRICE_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        validateProductSearchWithFiltersResponse.validatePriceFilter(price);
        softAssert.assertAll();
    }

    public void executeProductSearchWithDiscountFilter() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_DISCOUNT_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        validateProductSearchWithFiltersResponse.validateDiscountFilter(discount);
        softAssert.assertAll();
    }

    public void executeProductSearchWithOnlyPriceFilter() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_ONLY_PRICE_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        validateProductSearchWithFiltersResponse.validatePriceFilter(price);
        softAssert.assertAll();
    }

    public void executeProductSearchWithOnlyDiscountFilter() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_ONLY_DISCOUNT_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        validateProductSearchWithFiltersResponse.validateDiscountFilter(discount);
        softAssert.assertAll();
    }

    public void executeProductSearchWithInvalidFilterDataTypes() {
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.PRODUCT_SEARCH_WITH_FILTERS_FILEPATH, CentralisedSearchConstants.PRODUCT_SEARCH_WITH_PRICE_FILTER_DATAKEY);
        productSearchRequest.createAPIURL();
        productSearchRequest.createRequest(softAssert);
        productSearchRequest.executeRequestAndGetResponse(softAssert);
        productSearchWithFiltersResponse = productSearchRequest.productSearchWithFiltersResponse;
        validateProductSearchWithFiltersResponse = new ValidateProductSearchWithFiltersResponse(productSearchWithFiltersResponse, softAssert);
        validateProductSearchWithFiltersResponse.validateResponse();
        validateProductSearchWithFiltersResponse.validatePriceFilter(price);
        softAssert.assertAll();
    }

}
