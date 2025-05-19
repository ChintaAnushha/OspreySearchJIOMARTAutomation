package Com.jioMart.apiservice.Osprey.apiService.productSearch.GetProduct.service;

import Com.jioMart.apiservice.Osprey.apiService.productSearch.GetProduct.tasks.CreateAndExecuteGetProductRequest;
import Com.jioMart.base.BaseScript;
import Com.jioMart.constants.CentralisedSearchConstants;
import Com.jioMart.model.productSearch.GetProduct.response.GetProductResponse;
import lombok.extern.log4j.Log4j;
import org.testng.asserts.SoftAssert;

@Log4j
public class GetProductService extends BaseScript {

    CreateAndExecuteGetProductRequest getProductRequest = new CreateAndExecuteGetProductRequest();
    GetProductResponse getProductResponse;

    public GetProductResponse executeGetProductAPIOk(){
        log.info("Inside::: " + new Throwable().getStackTrace()[0].getMethodName());
        SoftAssert softAssert = new SoftAssert();
        readDataFromDataLoader(CentralisedSearchConstants.GET_PRODUCT_FILEPATH, CentralisedSearchConstants.GET_PRODUCT_DATAKEY);
        getProductRequest.createAPIURL();
        getProductRequest.executeRequestAndGetResponse(softAssert);
        getProductResponse = getProductRequest.getProductResponse;
        softAssert.assertAll();
        return getProductResponse;
    }
}
