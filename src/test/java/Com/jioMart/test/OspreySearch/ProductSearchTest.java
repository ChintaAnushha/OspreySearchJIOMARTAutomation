package Com.jioMart.test.OspreySearch;

import Com.jioMart.apiservice.Osprey.OspreyApiService;
import Com.jioMart.base.BaseScript;
import Com.jioMart.util.RetryListener;
import io.qameta.allure.*;
import io.qameta.allure.testng.AllureTestNg;
import jdk.jfr.Description;
//import net.serenitybdd.core.Serenity;
//import net.thucydides.core.annotations.WithTag;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
//import net.serenitybdd.testng.SerenityTestngTestCase;
//import net.serenitybdd.junit5.SerenityTest;
//import org.junit.jupiter.api.extension.ExtendWith;
//import net.serenitybdd.junit5.SerenityRunner;

    @Epic("Osprey Search API")
    @Feature("Product Search Operations")
    @Owner("Anusha")
//    @ExtendWith(SerenityRunner.class)
    @Listeners({AllureTestNg.class})
    @Test(retryAnalyzer = IRetryAnalyzer.class)
//    @SerenityTest
    public class ProductSearchTest extends BaseScript {
        OspreyApiService ospreyApiService ;//= new OspreyApiService();

//        @Test(priority =0)
//        public void OspreyApiServiceWithValidDetails() {
//            ospreyApiService.ospreyAPIWithValidDetails();
//        }

        @BeforeClass
        public void setUp() {
            ospreyApiService = new OspreyApiService();
        }


        @Test(priority = 1)
      //  @Test(retryAnalyzer = IRetryAnalyzer.class, priority = 0)
      //  @WithTag(name = "Type", value = "Smoke Test")
        @Story("Valid Search Scenarios")
        @Description("Verify search functionality with valid query parameters")
        @Severity(SeverityLevel.CRITICAL)
        public void OspreyApiServiceWithValidQuery() {
            Allure.step("Executing search with valid query");
            ospreyApiService.ospreyAPIWithValidQuery();
        }

        @Test(priority = 2)
        @Story("Error Handling")
        @Description("Verify API behavior with invalid search query")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithInValidQuery() {
            Allure.step("Executing search with invalid query");
            ospreyApiService.ospreyAPIWithInValidQuery();
        }

        @Test(priority = 3)
        @Story("Error Handling")
        @Description("Verify API behavior with empty search query")
        @Severity(SeverityLevel.CRITICAL)
        public void OspreyApiServiceWithEmptyQuery() {
            Allure.step("Executing search with empty query");
            ospreyApiService.ospreyAPIWithEmptyQuery();
        }

        @Test(priority = 4)
        @Story("Store Validation")
        @Description("Verify API behavior when an invalid store ID is provided in the request")
        @Severity(SeverityLevel.CRITICAL)
        public void OspreyApiServiceWithInvalidStore() {
            Allure.step("Executing search with invalid store ID");
            ospreyApiService.ospreyAPIWithInvalidStore();
        }

        @Test(priority = 5)
        @Story("Store Validation")
        @Description("Verify API behavior when store parameter is not provided in the request")
        @Severity(SeverityLevel.CRITICAL)
        public void OspreyApiServiceWithNoStore() {
            Allure.step("Executing search without store parameter");
            ospreyApiService.ospreyAPIWithNoStore();
        }

        @Test(priority = 6)
        @Story("Search Sorting")
        @Description("Verify product search results with ascending price sort order")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceSearchWithPriceAsc() {
            Allure.step("Executing search with ascending price sort");
            ospreyApiService.productSearchWithPriceAscSorting();
        }

        @Test(priority = 7)
        @Story("Search Sorting")
        @Description("Verify product search results with descending price sort order")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceSearchWithPriceDesc() {
            Allure.step("Executing search with descending price sort");
            ospreyApiService.productSearchWithPriceDescSorting();
        }

        @Test(priority = 8)
        @Story("Facet Management")
        @Description("Verify search behavior when facets are disabled")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceDisableFacets() {
            Allure.step("Executing search with disabled facets");
            ospreyApiService.ospreyAPIWithDisableFacets();
        }

        @Test(priority = 9)
        @Story("Facet Management")
        @Description("Verify search behavior when facets are enabled")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceEnableFacets() {
            Allure.step("Executing search with enabled facets");
            ospreyApiService.ospreyAPIWithEnabledFacets();
        }

        @Test(priority = 10)
        @Story("Facet Management")
        @Description("Verify search behavior when facet count values are disabled")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceDisableFacetsCountValues() {
            Allure.step("Executing search with disabled facet counts");
            ospreyApiService.ospreyAPIWithDisabledFacetCounts();
        }

        @Test(priority = 11)
        @Story("Facet Management")
        @Description("Verify search behavior when facet count values are enabled")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceEnableFacetsCountValues() {
            Allure.step("Executing search with enabled facet counts");
            ospreyApiService.ospreyAPIWithEnabledFacetCounts();
        }

        @Test(priority = 12)
        @Story("Attribute Management")
        @Description("Verify search with specific product attributes retrieval")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithSpecificAttributesRetrieval() {
            Allure.step("Executing search with specific attribute retrieval");
            ospreyApiService.ospreyAPIWithAttributesRetrieval();
        }

//        @Test(priority = 13)
//        @Story("Store Redirection")
//        @Description("Verify store redirection functionality in search API")
//        @Severity(SeverityLevel.CRITICAL)
//        public void OspreyApiServiceWithStoreRedirect() {
//            Allure.step("Executing search with store redirection");
//            ospreyApiService.ospreyAPIWithStoreRedirect();
//        }

        @Test(priority = 13)
        @Story("Query Parameters")
        @Description("Verify search behavior with only query parameter and no other attributes")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceOnlyQueryNoOtherAttribute() {
            Allure.step("Executing search with only query parameter");
            ospreyApiService.ospreyAPIExceptQueryNoOtherAttribute();
        }

        @Test(priority = 14)
        @Story("Pagination")
        @Description("Verify search results pagination with valid page number")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithPageNumber() {
            Allure.step("Executing search with page number");
            ospreyApiService.ospreyAPIPagination();
        }

//        @Test(priority = 16)
//        @Story("Pagination")
//        @Description("Verify API behavior with invalid page number")
//        @Severity(SeverityLevel.NORMAL)
//        public void OspreyApiServiceWithInValidPageNumber() {
//            Allure.step("Executing search with invalid page number");
//            ospreyApiService.ospreyAPIInvalidPageNumber();
//        }

        @Test(priority = 15)
        @Story("Pagination")
        @Description("Verify API behavior with invalid records offset")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithInValidRecordOffset() {
            Allure.step("Executing search with invalid records offset");
            ospreyApiService.ospreyAPIInvalidRecordsOffset();
        }

        @Test(priority = 16)
        @Story("Pagination")
        @Description("Verify search results with valid records offset")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithRecordOffset() {
            Allure.step("Executing search with valid records offset");
            ospreyApiService.ospreyAPIRecordsOffset();
        }

        @Test(priority = 17)
        @Story("Results Configuration")
        @Description("Verify search results when specific number of records per page is requested")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithRecordsPerPage() {
            Allure.step("Executing search with custom records per page value");
            ospreyApiService.ospreyAPIRecordsPerPage();
        }

        @Test(priority = 18)
        @Story("Results Configuration")
        @Description("Verify search results when specific number of records per page is requested")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithQueryInvalidDataType() {
            Allure.step("Executing search with invalid datatype as query ");
            ospreyApiService.ospreyAPIWithInvalidQueryType();
        }

        @Test(priority = 19)
        @Story("Results Configuration")
        @Description("Verify search results when specific number of records per page is requested")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithBooleanQueryInvalidDataType() {
            Allure.step("Executing search with invalid datatype as query ");
            ospreyApiService.ospreyAPIWithInvalidBooleanQueryType();
        }

        @Test(priority = 20)
        @Story("Pagination")
        @Description("Verify API behavior with invalid page number")
        @Severity(SeverityLevel.NORMAL)
        public void OspreyApiServiceWithBooleanAndStringPageNumber() {
            Allure.step("Executing search with invalid page number");
            ospreyApiService.ospreyAPIWithBooleanAndStringPageNumber();
        }
    }


