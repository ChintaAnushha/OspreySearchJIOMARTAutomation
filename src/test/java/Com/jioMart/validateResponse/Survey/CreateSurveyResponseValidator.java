package Com.jioMart.validateResponse.Survey;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.survey.CreateSurveyResponseWrapper;
import Com.jioMart.model.survey.global.TestData;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class CreateSurveyResponseValidator extends BaseScript {

    TestData testData = null;
    Map<String, String> testDataParams = null;

    CreateSurveyResponseWrapper createSurveyResponseWrapper = null;

    public CreateSurveyResponseValidator(CreateSurveyResponseWrapper response, TestData data) {
        this.createSurveyResponseWrapper = response;
        this.testData = data;
     //   this.testDataParams = data.getOtherParams();
    }

    public void validateCreatedSurveyResponse(SoftAssert softAssert){

        if (createSurveyResponseWrapper.getData().getId()==null){
            System.out.println("survey ID in response::::::"+createSurveyResponseWrapper.getData().getId());
            softAssert.assertTrue(false,"created survey id is null in the response");
        }
        if (null==createSurveyResponseWrapper.getData().getQuestions().get(0).getId()){
            softAssert.assertTrue(false,"created survey question id is null in the response");
        }
        if (null==createSurveyResponseWrapper.getData().getQuestions().get(0).getChoices().get(0).getId()){
            softAssert.assertTrue(false,"created survey choice id is null in the response");
        }
    }
}


