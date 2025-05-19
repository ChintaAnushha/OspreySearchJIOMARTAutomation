package Com.jioMart.base;

import Com.jioMart.util.HttpClientCaller;
import Com.jioMart.model.survey.global.TestData;
import org.apache.http.*;

import Com.jioMart.base.BaseScript;


public class ApiUrls extends BaseScript {

    public HttpResponse httpResponse = null;
    static String xfpDate = null;
    static String xfpSignature = null;


// ########################### SURVEY_APIS ################################

    public HttpResponse createSurveyApi(TestData testData, String createSurveyRequest){
        String getAllSurveysEndpoint = testData.getUrls().get("CreateSurveyEndpoint");
        httpResponse=HttpClientCaller.micrositeAPIPostCall(fqdn,getAllSurveysEndpoint,createSurveyRequest,cookie);
        return httpResponse;
    }


    public HttpResponse getAllSurveysAPI(TestData testData) {
        String getAllSurveysEndpoint = testData.getUrls().get("surveyEndpoint");
      //  getAllSurveysEndpoint = getAllSurveysEndpoint+"?offset=" + testData.getOtherParams().get("offset") + "&pageSize=" + testData.getOtherParams().get("pageSize");
        System.out.printf("endpoint is::::::"+getAllSurveysEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn, getAllSurveysEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse updateSurveyAPI(TestData testData, String updateSurveyRequest) {
        String updateSurveysEndpoint = testData.getUrls().get("updateSurveyEndpoint");
//        updateSurveysEndpoint = updateSurveysEndpoint+id;
        System.out.printf("endpoint is::::::"+updateSurveysEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIPutCall(fqdn, updateSurveysEndpoint,updateSurveyRequest, cookie);
        return httpResponse;
    }

    public HttpResponse postOspreyAPI(TestData testData, String updateSurveyRequest) {
        String updateSurveysEndpoint = testData.getUrls().get("ospreySearchEndpoint");
//        updateSurveysEndpoint = updateSurveysEndpoint+id;
        System.out.printf("endpoint is::::::"+updateSurveysEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn, updateSurveysEndpoint,updateSurveyRequest, cookie);
        return httpResponse;
    }

    public HttpResponse deleteSurveyAPI(TestData testData) {
        String deleteSurveyEndpoint = testData.getUrls().get("deleteSurveyEndpoint");
        System.out.printf("endpoint is::::::"+deleteSurveyEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIDeleteCall(fqdn, deleteSurveyEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse assignSurveyApi(TestData testData, String assignSurveyRequest){
        String getAllSurveysEndpoint = testData.getUrls().get("assignSurveyEndpoint");
        httpResponse=HttpClientCaller.micrositeAPIPostCall(fqdn,getAllSurveysEndpoint,assignSurveyRequest,cookie);
        return httpResponse;
    }

    public HttpResponse getAssigneeSurveysAPI(TestData testData) {
        String getAssigneeSurveysEndpoint = testData.getUrls().get("getAssignSurveyEndpoint");
   //     getAssigneeSurveysEndpoint = getAssigneeSurveysEndpoint+"?completed=" + testData.getOtherParams().get("completed");
        System.out.printf("endpoint is::::::"+getAssigneeSurveysEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn, getAssigneeSurveysEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse getAssigneeSurveyResponseAPI(TestData testData) {
        String getAssigneeSurveyResponseEndpoint = testData.getUrls().get("getAssigneeSurveyResponseEndpoint");
        System.out.printf("endpoint is::::::"+getAssigneeSurveyResponseEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn, getAssigneeSurveyResponseEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse getSurveyDashboardDataAPI(TestData testData) {
        String getSurveyDashboardDataEndpoint = testData.getUrls().get("getSurveyDashboardDataEndpoint");
   //     getSurveyDashboardDataEndpoint = getSurveyDashboardDataEndpoint+"?pageNumber=" + testData.getOtherParams().get("pageNumber");
        System.out.printf("endpoint is::::::"+getSurveyDashboardDataEndpoint);
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn, getSurveyDashboardDataEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse getSurveySubmittedCountAPI(TestData testData) {
        String getSurveySubmittedCountEndpoint = testData.getUrls().get("getSurveySubmittedCountEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn, getSurveySubmittedCountEndpoint, cookie);
        return httpResponse;
    }

    public HttpResponse submittedSurveyDataExportToExcelAPI(TestData testData, String exportToExcelRequest ) {
        String getSurveySubmittedCountEndpoint = testData.getUrls().get("getSurveySubmittedCountEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn, getSurveySubmittedCountEndpoint,exportToExcelRequest, cookie);
        return httpResponse;
    }



//     ########################### FAQ_APIS ################################

    public HttpResponse addFaq(TestData testData, String addFaqRequest){
        String addFaqEndpoint = testData.getUrls().get("addFaqEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,addFaqEndpoint,addFaqRequest,cookie);
        return httpResponse;
    }

    public HttpResponse editFaq(TestData testData, String addFaqRequest){
        String addFaqEndpoint = testData.getUrls().get("editFaqEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,addFaqEndpoint,addFaqRequest,cookie);
        return httpResponse;
    }

    public HttpResponse getFaqCategories(TestData testData){
        String getFaqCategoriesEndpoint = testData.getUrls().get("getFaqCategoriesEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getFaqCategoriesEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse getFaq(TestData testData){
        String getFaqEndpoint = testData.getUrls().get("getFaqEndpoint");
    //    getFaqEndpoint = getFaqEndpoint+"?offset=" + testData.getOtherParams().get("offset") + "&pageSize=" + testData.getOtherParams().get("pageSize");
        httpResponse =HttpClientCaller.micrositeAPIGetCall(fqdn,getFaqEndpoint,cookie);
        return httpResponse;
    }
    
    public HttpResponse deleteFaq(TestData testData, String deleteFaqRequest){
        String deleteFaqEndpoint = testData.getUrls().get("deleteFaqEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPutCall(fqdn,deleteFaqEndpoint,deleteFaqRequest,cookie);
        return httpResponse;
    }


//     ########################### L&D ################################

    public HttpResponse getInductionJourney(TestData testData){
        String getInductionJourneyEndpoint = testData.getUrls().get("InductionJourneyEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getInductionJourneyEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse getJourneyDetails(TestData testData){
        String getJourneyDetailsEndpoint = testData.getUrls().get("GetJourneyDetailsEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getJourneyDetailsEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse searchJourney(TestData testData,String searchKey){
        String getSearchJourneyEndpoint = testData.getUrls().get("GetSearchJourneysEndpoint");
     //   getSearchJourneyEndpoint=getSearchJourneyEndpoint+"?key="+ searchKey+"&page="+testData.getOtherParams().get("page")+"&size="+testData.getOtherParams().get("size");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getSearchJourneyEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse setWelcomeMessage(TestData testData, String setWelcomeMessageRequest){
        String getSearchJourneyEndpoint = testData.getUrls().get("setWelcomeMessageEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,getSearchJourneyEndpoint,setWelcomeMessageRequest,cookie);
        return httpResponse;
    }

    public HttpResponse getActiveWelcomeMessage(TestData testData){
        String getSearchJourneyEndpoint = testData.getUrls().get("getActiveWelcomeMessageEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getSearchJourneyEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse createNewJourney(TestData testData, String createNewJourneyRequest){
        String getSearchJourneyEndpoint = testData.getUrls().get("createJourneyEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,getSearchJourneyEndpoint,createNewJourneyRequest,cookie);
        return httpResponse;
    }

    public HttpResponse editNewJourney(TestData testData, String editNewJourneyRequest){
        String editJourneyEndpoint = testData.getUrls().get("editJourneyEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,editJourneyEndpoint,editNewJourneyRequest,cookie);
        return httpResponse;
    }

    public HttpResponse createNewProgram(TestData testData, String createNewProgramRequest){
        String createNewProgramEndpoint = testData.getUrls().get("createNewProgramEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,createNewProgramEndpoint,createNewProgramRequest,cookie);
        return httpResponse;
    }

    public HttpResponse editProgram(TestData testData, String editProgramRequest){
        String editProgramEndpoint = testData.getUrls().get("editProgramEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,editProgramEndpoint,editProgramRequest,cookie);
        return httpResponse;
    }

    public HttpResponse getProgramDetails(TestData testData){
        String getProgramEndpoint = testData.getUrls().get("getProgramEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getProgramEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse searchProgram(TestData testData, String searchKey){
        String searchProgramEndpoint = testData.getUrls().get("searchProgramEndpoint");
    //    searchProgramEndpoint = searchProgramEndpoint+"?key="+searchKey + "&page=" + testData.getOtherParams().get("page") + "&size=" + testData.getOtherParams().get("size");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,searchProgramEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse getCurrentProgram(TestData testData){
        String currentProgramsEndpoint = testData.getUrls().get("currentProgramsEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,currentProgramsEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse getCarouselData(TestData testData){
        String getCarouselDataEndpoint = testData.getUrls().get("getCarouselDataEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getCarouselDataEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse editCarouselData(TestData testData,String editCarouselDataRequest){
        String editCarouselEndpoint = testData.getUrls().get("editCarouselEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,editCarouselEndpoint,editCarouselDataRequest,cookie);
        return httpResponse;
    }

    public HttpResponse searchCourse(TestData testData,String searchText){
        String searchCourseEndpoint = testData.getUrls().get("searchCourseEndpoint");
     //   searchCourseEndpoint = searchCourseEndpoint+"?key="+searchText + "&page="+testData.getOtherParams().get("page")+ "&size=" + testData.getOtherParams().get("size");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,searchCourseEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse createCourse(TestData testData,String createCourseRequest){
        String CreateCourseEndpoint = testData.getUrls().get("CreateCourseEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,CreateCourseEndpoint,createCourseRequest,cookie);
        return httpResponse;
    }

    public HttpResponse createTag(TestData testData,String createTagRequest){
        String createTagEndpoint = testData.getUrls().get("createTagEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIPostCall(fqdn,createTagEndpoint,createTagRequest,cookie);
        return httpResponse;
    }

    public HttpResponse getHomeEvents(TestData testData){
        String getHomeEventsEndpoint = testData.getUrls().get("getHomeEventsEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getHomeEventsEndpoint,cookie);
        return httpResponse;
    }



//    ################################# Download center ######################################

    public HttpResponse getDepartments(TestData testData){
        String getDepartmentEndpoint = testData.getUrls().get("getDepartmentEndpoint");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getDepartmentEndpoint,cookie);
        return httpResponse;
    }

    public HttpResponse getNewEmployeeDownloads(TestData testData){
        String getNewEmployeeDownloadsEndpoint = testData.getUrls().get("getNewEmployeeDownloadsEndpoint");
   //     getNewEmployeeDownloadsEndpoint = getNewEmployeeDownloadsEndpoint+"?offset="+testData.getOtherParams().get("offset")+ "&size=" + testData.getOtherParams().get("size");
        httpResponse = HttpClientCaller.micrositeAPIGetCall(fqdn,getNewEmployeeDownloadsEndpoint,cookie);
        return httpResponse;
    }

}

