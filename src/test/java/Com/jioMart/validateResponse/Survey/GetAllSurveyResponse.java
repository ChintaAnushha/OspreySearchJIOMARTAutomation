package Com.jioMart.validateResponse.Survey;

import Com.jioMart.model.survey.GetAllSurveysResponseWrapper;
import Com.jioMart.model.survey.global.TestData;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class GetAllSurveyResponse {
    TestData testData = null;
    Map<String, String> testDataParams = null;
    GetAllSurveysResponseWrapper getAllSurveysResponseWrapper = null;

    public GetAllSurveyResponse(GetAllSurveysResponseWrapper response, TestData data) {
        this.getAllSurveysResponseWrapper = response;
        this.testData = data;
        this.testDataParams = data.getOtherParams();
    }

    public void getAllSurveysAPIResponseOk(SoftAssert softAssert) {
        System.out.println("data is:::::::::"+getAllSurveysResponseWrapper.getData());
        if(null==getAllSurveysResponseWrapper.getData())
            softAssert.assertTrue(false, "Failed! get all survey api Response is null");
        if(null==getAllSurveysResponseWrapper.getError())
            softAssert.assertTrue(false, "Failed! get all survey api Response is null");
    }
}

