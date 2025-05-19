package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearchWithFilter.tasks;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.response.ProductSearchWithFiltersResponse;
import org.testng.asserts.SoftAssert;

public class ValidateProductSearchWithFiltersResponse extends BaseScript {

    public ProductSearchWithFiltersResponse productSearchWithFiltersResponse;
    SoftAssert softAssert;

    public ValidateProductSearchWithFiltersResponse(ProductSearchWithFiltersResponse productSearchWithFiltersResponse, SoftAssert softAssert) {
        this.productSearchWithFiltersResponse = productSearchWithFiltersResponse;
        this.softAssert = softAssert;
    }


    public void validateResultsCount() {
        if (productSearchWithFiltersResponse.getNumFound() < 0) {
            softAssert.assertTrue(false, "Failed! No results in the response");
        }
        if (productSearchWithFiltersResponse.getNumFound() > 12) {
            softAssert.assertTrue(false, "Failed! default page size count is mismatch ");
        }
    }

    public void validateResponse() {
        if (productSearchWithFiltersResponse.getNumFound()<0) {
            softAssert.assertTrue(false, "Failed! Got null hits");
        }
        if (productSearchWithFiltersResponse.getDocs().get(0).getNameTextEn() == null) {
            softAssert.assertTrue(false, "Failed! Got null name");
        }
//        if (productSearchWithFiltersResponse.getDocs().get(0).getTitle() == null) {
//            softAssert.assertTrue(false, "Failed! Got null name");
//        }
        validateResultsCount();
    }

    public void validateDiscountFilter(Integer AppliedDiscountValue){
        for (int i=0; i<10; i++){
            Double discount = productSearchWithFiltersResponse.getDocs().get(0).getDiscountDouble();
        if (discount>AppliedDiscountValue==true){
            softAssert.assertTrue(false, "Failed! Applied discount filter related results are not coming");
        }
        }
    }

    public void validatePriceFilter(Integer AppliedPriceValue){
        for (int i=0; i<10; i++){
            Double price = productSearchWithFiltersResponse.getDocs().get(0).getPriceInrDouble();
            if (price>AppliedPriceValue==true){
                softAssert.assertTrue(false, "Failed! Applied price filter related results are not coming");
            }
        }
    }

}
