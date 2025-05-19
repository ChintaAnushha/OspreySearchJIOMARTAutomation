package Com.jioMart.apiservice.Osprey.apiService.productSearch.ProductSearch.tasks;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchResponse;
import lombok.extern.log4j.Log4j;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static java.util.Arrays.stream;

@Log4j
public class ValidateProductSearchResponse extends BaseScript {
    public ProductSearchResponse productSearchResponse;
    OspreyApiResponse ospreyApiResponse = null;

    SoftAssert softAssert;

   // String boost = "LYF";

  //  String categoryL4 = "Citrus Fruits";

    public ValidateProductSearchResponse(ProductSearchResponse productSearchResponse, SoftAssert softAssert) {
        this.productSearchResponse = productSearchResponse;
        this.softAssert = softAssert;
    }

    public void validateResultsCount() {
        if (ospreyApiResponse.getNumFound() <= 0) {
            softAssert.assertTrue(false, "Failed! No results in the response");
        }
        if (ospreyApiResponse.getNumFound()> 10) {
            softAssert.assertTrue(false, "Failed! default page size count is mismatch ");
        }
    }

    public void validateResponse() {
        if (ospreyApiResponse.getDocs().get(0)== null) {
            softAssert.assertTrue(false, "Failed! Got null hits");
        }
        if (ospreyApiResponse.getDocs().get(0).getProductName() == null) {
            softAssert.assertTrue(false, "Failed! Got null name");
        }
//        if (ospreyApiResponse.getDocs().get(0).getPrice()<=0) {
//            softAssert.assertTrue(false, "Failed! No price in the response");
//        }
        validateResultsCount();
    }

    public boolean validateResponseforWithOutQuery() {
        // Check if response contains an error message
        if (ospreyApiResponse.getDocs() != null && !ospreyApiResponse.getDocs().isEmpty()) {
            if (ospreyApiResponse.getDocs().equals("500: hasattr(): attribute name must be string")) {
                System.out.println("True - API returned expected error: " + ospreyApiResponse.getDocs());
                return true; // Test case failed
            } else {
                System.out.println("False - Unexpected error: " + ospreyApiResponse.getDocs());
                return false; // Unexpected error
            }
        }
        return false;
    }
    public void validateTop12Results(String query) {
        int count = ospreyApiResponse.getNumFound();
        System.out.println(count);

        String name = null;
        for (int i = 0; i < 12; i++) {
            name = ospreyApiResponse.getDocs().get(0).getProductName();//get(i).getTitle();
            if (name.contains(query)== true) {
                log.info("Result " + i + " is::::" + ospreyApiResponse.getDocs().get(0).getProductName());
            } else {
                log.info("Result " + i + " " + name + " is mismatch");
            }
        }
    }

    public void validateSearchQueryResult(String query, String typeOfQuery) {
      //  if (productSearchResponse.getResults().get(0).getHits().get(0).getTitle().contains(query) == true)
        if(ospreyApiResponse.getDocs().get(0).getProductName().contains(query) == true){
            softAssert.assertTrue(true, typeOfQuery + " query is working fine");
        } else {
            softAssert.assertTrue(false, typeOfQuery + " misspelled query is not giving relevant details");
        }
    }

//    public void validateBoostFlow() {
//        for (int i = 0; i < 3; i++) {
//            String brandName = productSearchResponse.getResults().get(0).getHits().get(i).getBrandsList().get(0);
//            log.info("boosted brand name " + i + brandName);
//        //    if (brandName.equals(boost)) {
//                softAssert.assertTrue(true, "Product boost is working fine");
//            } else {
//                softAssert.assertTrue(false, "Product boost is not working");
//            }
//        }
//    }
//
//    public void validateBuryFlow() {
//        long hitsCount = productSearchResponse.getResults().get(0).getHits().stream().count();
//        for (int i = (int) hitsCount - 1; i > hitsCount - 4; i--) {
//            String category = productSearchResponse.getResults().get(0).getHits().get(i).getAttributes().getCategoryLevelL4().getTextList().get(0);
//            if (category.equals(categoryL4)) {
//                log.info("bury category name " + i + category);
//                softAssert.assertTrue(true, "Product bury is working fine");
//            } else {
//                softAssert.assertTrue(false, "Product bury is not working");
//            }
//        }
  //  }

    public void validateSearchResults() {
        validateBasicResponse();
        validateQueryResults();
        validateProductDetails();
    }

    private void validateBasicResponse() {
        softAssert.assertNotNull(productSearchResponse, "Response should not be null");
        softAssert.assertNotNull(productSearchResponse.getNumFound(), "NumFound should not be null");
        softAssert.assertTrue(productSearchResponse.getNumFound() > 0, "Search should return results");
    }

    private void validateQueryResults() {
        List<ProductSearchResponse.Doc> products = productSearchResponse.getDocs();
        if (products != null) {
            // Validate result count
            softAssert.assertTrue(products.size() <= 1000,
                    "Results should not exceed maximum records_per_page");

            // Validate each product
            for (ProductSearchResponse.Doc product : products) {
                validateProductRelevance(product);
            }
        }
    }

    private void validateProductRelevance(ProductSearchResponse.Doc product) {
        boolean isRelevant = false;
        String searchQuery = "Shirt"; // Get from test data

        // Check product name
        if (product.getNameTextEn() != null) {
            isRelevant = product.getNameTextEn().toLowerCase()
                    .contains(searchQuery.toLowerCase());
        }

        // Check categories
        if (!isRelevant && product.getL1l3categoryEnStringMv() != null) {
            isRelevant = product.getL1l3categoryEnStringMv().stream()
                    .anyMatch(category -> category.toLowerCase()
                            .contains(searchQuery.toLowerCase()));
        }

        softAssert.assertTrue(isRelevant,
                "Product " + product.getCodeString() + " should be relevant to search query");
    }

    private void validateProductDetails() {
        if (productSearchResponse.getDocs() != null) {
            for (ProductSearchResponse.Doc product : productSearchResponse.getDocs()) {
                softAssert.assertNotNull(product.getCodeString(), "Product code should not be null");
                softAssert.assertNotNull(product.getNameTextEn(), "Product name should not be null");
                softAssert.assertNotNull(product.getPriceInrDouble(), "Price should not be null");
                softAssert.assertNotNull(product.getCatalogId(), "Catalog ID should not be null");

                // Validate store
                softAssert.assertEquals(product.getCatalogId(), "rilfnlProductCatalog",
                        "Product should belong to correct store");
            }
        }
    }
}
