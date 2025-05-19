package Com.jioMart.apiservice.Osprey.apiService.productSearch.Suggestions.tasks;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.Suggestions.response.SuggestionsResponse;
import org.testng.asserts.SoftAssert;

public class ValidateSuggestionsResponse extends BaseScript {

    public SuggestionsResponse suggestionsResponse;
    SoftAssert softAssert;

    public ValidateSuggestionsResponse(SuggestionsResponse suggestionsResponse, SoftAssert softAssert) {
        this.suggestionsResponse = suggestionsResponse;
        this.softAssert = softAssert;
    }

    public void validateResponse() {
//        AssertUtil.AssertValues(addAppointmentPurposeResponse.getMeta().getData(),data, softAssert);
//        if (null == addAppointmentPurposeResponse.getMeta().getCode())
//            softAssert.assertTrue(false, "Failed! Got null codeId");
//        if (null == addAppointmentPurposeResponse.getMeta().getStatus())
//            softAssert.assertTrue(false, "Failed! Got null statusId");

    }
}
