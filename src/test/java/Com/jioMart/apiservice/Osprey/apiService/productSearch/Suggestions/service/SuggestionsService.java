package Com.jioMart.apiservice.Osprey.apiService.productSearch.Suggestions.service;

import Com.jioMart.apiservice.Osprey.apiService.productSearch.Suggestions.tasks.CreateAndExecuteSuggestionsRequest;
import Com.jioMart.apiservice.Osprey.apiService.productSearch.Suggestions.tasks.ValidateSuggestionsResponse;
import Com.jioMart.base.BaseScript;
import Com.jioMart.constants.CentralisedSearchConstants;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.Suggestions.response.SuggestionsResponse;
import lombok.extern.log4j.Log4j;
import org.testng.asserts.SoftAssert;

@Log4j
public class SuggestionsService extends BaseScript {

    CreateAndExecuteSuggestionsRequest suggestionsRequest = new CreateAndExecuteSuggestionsRequest();
    ValidateSuggestionsResponse validateSuggestionsResponse;
    SuggestionsResponse suggestionsResponse;

    public void executeSuggestionsAPIOk(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithoutUserId(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithInvalidUserId(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithoutPartnerId(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithInvalidPartnerId(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithInvalidQuery(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }

    public void executeSuggestionsAPIWithoutQuery(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.INITIATE_CATALOG_SYNC_FILEPATH, CentralisedSearchConstants.INITIATE_CATALOG_SYNC_DATAKEY);
        suggestionsRequest.createAPIURL();
        suggestionsRequest.createRequest();
        suggestionsRequest.executeRequestAndGetResponse(softAssert);
        suggestionsResponse= suggestionsRequest.suggestionsResponse;
        validateSuggestionsResponse = new ValidateSuggestionsResponse(suggestionsResponse, softAssert);
        validateSuggestionsResponse.validateResponse();
        softAssert.assertAll();
    }
}
