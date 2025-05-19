package Com.jioMart.validateResponse.OspreySearch;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.survey.global.TestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.util.*;

@Listeners({ AllureTestNg.class })
public class OspreyAPIFiltersResponseValidator extends BaseScript {

    private static final Logger log = Logger.getLogger(OspreyAPIFiltersResponseValidator.class);
    TestData testData = null;
    Map<String, String> testDataParams = null;

    OspreyApiResponse ospreyApiResponse = null;
    private SoftAssert softAssert;

    //  GetCarouselDataResponseWrapper getCarouselDataResponseWrapper = null;
    //  public OspreyApiResponse ospreyApiResponse;


    public OspreyAPIFiltersResponseValidator(OspreyApiResponse response, TestData data) {
        this.ospreyApiResponse = response;
        this.testData = data;
        this.testDataParams = data.getOtherParams();
        this.softAssert = new SoftAssert();
    }



//    public void validateTop10Results(String query) {
//
//        int count = ospreyApiResponse.getNumFound();
//        System.out.println(count);
//        //  String title = null;
//        //   for (int i = 0; i < 10; i++) {
//        //    title = String.valueOf(ospreyApiResponse.getNumFound());//.brandStringMv.get(0).name.equalsIgnoreCase(query));//getDocs().get(0).getNameTextEn();//getdocs().get(0).getHits().get(i).getTitle();
//        //  if (title.contains(query) == true) {
//        //  logger.info("Result " + i + " is::::" + ospreyApiResponse.getDocs().get(0).getNameTextEn());//getHits().get(i).getTitle());
//        //     System.out.println("Result " + i + " is::::" + ospreyApiResponse.getDocs().get(0).getNameTextEn());//getHits().get(i).getTitle());
//        //   } else {
//        //        System.out.println("Result " + i + " " + title + " is mismatch");
//    }
//    //}
////    }
//
//
//    @Step("Validating basic response")
//    public void validateBasicResponse() {
//        log.info("Starting basic response validation");
//        softAssert.assertNotNull(ospreyApiResponse, "Response should not be null");
//
//        try {
//            //   String responseStr = ospreyApiResponse.asString();
//            //    String responseStr1 = ospreyApiResponse.asStringForStore();
//
//            // Check for different error responses
////        if (ospreyApiResponse.contains("Search query cannot be empty or invalid")) {
////            log.info("Received empty query error response as expected");
////            softAssert.assertTrue(true, "Received expected empty query error response");
////            Allure.addAttachment("Error Response", ospreyApiResponse.asString());
////           // return;
////        }
////        if (responseStr.contains("Invalid store")) {
////            log.info("Received invalid store error response as expected");
////            softAssert.assertTrue(true, "Received expected invalid store error response");
////            Allure.addAttachment("Error Response", ospreyApiResponse.asString());
////             //   return;
////            }
//            if (ospreyApiResponse.getDocs() != null) {
//                // For successful search response
//                log.info("Number of results found: " + ospreyApiResponse.getNumFound());
//                Allure.addAttachment("Response Count", String.valueOf(ospreyApiResponse.getNumFound()));
//                softAssert.assertNotNull(ospreyApiResponse.getNumFound(), "NumFound should not be null");
//                softAssert.assertTrue(ospreyApiResponse.getNumFound() > 0, "Search should return results");
//            }
//        } catch (Exception e) {
//            log.error("Error during response validation: " + e.getMessage());
//            softAssert.fail("Response validation failed: " + e.getMessage());
//        }
//        log.info("Basic response validation completed");
//        softAssert.assertAll();
//    }
//
         @Step("Validate brand filter response, count and pagination")
         public void validateBrandFilterResponse(List<OspreyApiResponse.Doc> allDocs, Map<String,Object> facetData, int numFound, TestData testData) throws JsonProcessingException {

             String expectedBrand = this.testData.getOtherParams().get("values");
             if (expectedBrand == null || expectedBrand.trim().isEmpty()) {
                 throw new IllegalArgumentException("Brand value is missing in test data");
             }
             expectedBrand = expectedBrand.toLowerCase().trim();
             log.info("Validating brand: " + expectedBrand);

             //   int numFoundCount = numFound;
             int brandMatchCount = 0;
             StringBuilder productDetails = new StringBuilder();

             // Calculate brand match count
//        int brandMatchCount = 0;
//        StringBuilder productDetails = new StringBuilder();

             for (OspreyApiResponse.Doc product : allDocs) {
                 List<String> productBrands = Collections.singletonList(product.getBrandName());
                 String productName = product.getProductName();

                 boolean matches = false;
                 if (productBrands != null) {
                     for (String brand : productBrands) {
                         if (brand != null && brand.toLowerCase().trim().equals(expectedBrand)) {
                             matches = true;
                             break;
                         }
                     }
                 }

                 if (matches) {
                     brandMatchCount++;
                 }

                 productDetails.append(String.format(
                         "Product: %s\nBrands: %s\nMatches: %s\n---\n",
                         productName,
                         productBrands != null ? String.join(", ", productBrands) : "No brand",
                         matches ? "Yes" : "No"
                 ));
             }

             // Get facet count
             //    Map<String, List<Map<String, Object>>> facetData = ospreyApiResponse.getFacetData();
             int facetCount = numFound; // Default to numFound

             if (facetData != null) {
                 List<Map<String, Object>> brandFacets = (List<Map<String, Object>>) facetData.get("brand_string_mv");
                 if (brandFacets != null) {
                     for (Map<String, Object> facet : brandFacets) {
                         String brandName = String.valueOf(facet.get("name")).toLowerCase().trim();
                         if (brandName.equals(expectedBrand)) {
                             Object valueObj = facet.get("value");
                             if (valueObj instanceof Number) {
                                 facetCount = ((Number) valueObj).intValue();
                                 break;
                             }
                         }
                     }
                 }
             }

             // Validate counts
             int docsCount = allDocs.size();
             boolean docsMatchFilter = (brandMatchCount == docsCount);
             boolean docsMatchNumFound = (docsCount == numFound);
             boolean facetMatchesNumFound = (facetCount == numFound);
             boolean allCountsMatch = docsMatchFilter && docsMatchNumFound && facetMatchesNumFound;

             log.info("\n========== Validation Results ==========");
             log.info(String.format("Brand: %s", expectedBrand));
             log.info(String.format("NumFound Count: %d", numFound));
             log.info(String.format("Facet Count: %d", facetCount));
             log.info(String.format("Docs Count: %d", docsCount));
             log.info(String.format("Matching Products: %d", brandMatchCount));

             if (!allCountsMatch) {
                 String errorMessage = String.format(
                         "Brand Filter Validation Failed:\n" +
                                 "NumFound Count: %d\n" +
                                 "Facet Count: %d\n" +
                                 "Docs Count: %d\n" +
                                 "Matching Products: %d",
                         numFound, facetCount, docsCount, brandMatchCount
                 );
                 log.error(errorMessage);
                 Assert.fail(errorMessage);
             }
             // Create Allure report
             Allure.addAttachment("Brand Filter Results", String.format(
                     "Test Summary:\n" +
                             "-------------\n" +
                             "Brand: %s\n" +
                             "NumFound Count: %d\n" +
                             "Facet Count: %d\n" +
                             "Docs Count: %d\n" +
                             "Matching Products: %d\n\n" +
                             "Validation Status:\n" +
                             "----------------\n" +
                             "All Products Match Filter: %s\n" +
                             "Facet Matches NumFound: %s\n" +
                             "Docs Match NumFound: %s\n" +
                             "Overall Status: %s\n\n" +
                             "Product Details:\n" +
                             "---------------\n%s",
                     expectedBrand,
                     numFound,
                     facetCount,
                     docsCount,
                     brandMatchCount,
                     docsMatchFilter ? "Yes" : "No",
                     docsMatchNumFound ? "Yes" : "No",
                     facetMatchesNumFound ? "Yes" : "No",
                     allCountsMatch ? "PASSED" : "FAILED",
                     productDetails.toString()
             ));

             log.info("=================== Brand Filter Validation Completed ===================");
         }


    @Step("Validate price range filter response")
    public void validatePriceRangeResponse() {
        log.info("Price Range Filter Validation Started");

        // Get price range from test data
        String priceRange = testData.getOtherParams().get("values");
        String[] rangeLimits = priceRange.split("\\|");
        double minPrice = Double.parseDouble(rangeLimits[0]);
        double maxPrice = Double.parseDouble(rangeLimits[1]);

        log.info(String.format("Validating price range: %.2f to %.2f INR", minPrice, maxPrice));

        // Get counts for validation
        int numFound = ospreyApiResponse.getNumFound();
        Map<String, Object> facetCounts = ospreyApiResponse.getFacetData();
        int facetCount = getFacetCountForPriceRange(facetCounts, priceRange);
        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        int docsCount = products.size();
        //   boolean allProductsInRange = true;
        int productsInRange = 0;


        log.info("\nProduct Price Range Analysis");
        StringBuilder priceDetails = new StringBuilder();

        //  for (int i = 0; i < products.size(); i++) {
        for (OspreyApiResponse.Doc product : products) {  // .get(i);
            double productPrice = product.getAvgSellingPrice();
            String productName = product.getProductName();

            // Add debug logging to see actual prices
            log.info(String.format("Checking product price: %.2f against range: %.2f - %.2f",
                    productPrice, minPrice, maxPrice));

            // Updated range check with tolerance for floating point comparison
            boolean inRange = false;
            if (productPrice >= 0) {  // Validate price
                double roundedPrice = Math.round(productPrice * 100.0) / 100.0;
                inRange = roundedPrice >= minPrice && roundedPrice <= maxPrice;
            }

            if (inRange) {
                productsInRange++;
            }

            String priceLog = String.format(
                    "Product:\n" +
                            "Name: %s\n" +
                            "Price (INR): %.2f\n" +
                            "Expected Range: %.2f - %.2f\n" +
                            "In Range: %s\n" +
                            "------------------------",
                    productName, productPrice, minPrice, maxPrice, inRange ? "Yes" : "No"
            );
            log.info(priceLog);
        }

// Add debug logging after the loop
        log.info(String.format("\nPrice Range Analysis Debug:\n" +
                        "Min Price: %.2f\n" +
                        "Max Price: %.2f\n" +
                        "Total Products: %d\n" +
                        "Products in Range: %d\n",
                minPrice, maxPrice, products.size(), productsInRange));

        // Strict validation checks
        boolean productsExactMatch = (productsInRange == products.size());
        boolean countsMatch = (numFound == facetCount);
        boolean docsMatch = (numFound == docsCount);
        boolean exactMatch = productsExactMatch && countsMatch && docsMatch;

        log.info("\n========== Validation Results ==========");
        log.info(String.format("Total Products: %d", products.size()));
        log.info(String.format("Products in Range: %d", productsInRange));
        log.info(String.format("Products Match Status: %s", productsExactMatch ? "PASSED" : "FAILED"));
        log.info(String.format("Overall Status: %s", exactMatch ? "PASSED" : "FAILED"));

        // Force test failure if not exact match
        if (!exactMatch) {
            String errorMessage = String.format(
                    "Validation Failed:\n" +
                            "Products in range: %d/%d\n" +
                            "NumFound: %d\n" +
                            "FacetCount: %d\n" +
                            "DocsCount: %d",
                    productsInRange, products.size(),
                    numFound, facetCount, docsCount
            );
            log.error(errorMessage);
            Assert.fail(errorMessage);
        }

        // Calculate statistics
        DoubleSummaryStatistics priceStats = products.stream()
                .mapToDouble(OspreyApiResponse.Doc::getAvgSellingPrice)
                .summaryStatistics();

        // Update Allure report
        Allure.addAttachment("Price Range Filter Results", String.format(
                "Test Summary:\n" +
                        "-------------\n" +
                        "Price Range: %.2f - %.2f INR\n" +
                        "Total Products: %d\n" +
                        "Products in Range: %d\n\n" +
                        "Count Validation:\n" +
                        "----------------\n" +
                        "Products Match Status: %s\n" +
                        "NumFound: %d\n" +
                        "Facet Count: %d\n" +
                        "Docs Count: %d\n" +
                        "Overall Status: %s\n\n" +
                        "Price Statistics:\n" +
                        "----------------\n" +
                        "Minimum Price: %.2f INR\n" +
                        "Maximum Price: %.2f INR\n" +
                        "Average Price: %.2f INR\n\n" +
                        "Validation Message: %s",
                minPrice, maxPrice,
                products.size(), productsInRange,
                productsExactMatch ? "MATCHED" : String.format("FAILED (%d/%d)", productsInRange, products.size()),
                numFound, facetCount, docsCount,
                exactMatch ? "PASSED" : "FAILED",
                priceStats.getMin(),
                priceStats.getMax(),
                priceStats.getAverage(),
                exactMatch ?
                        "All validations passed successfully" :
                        String.format("Failed: %d products outside range, count mismatches found",
                                products.size() - productsInRange)
        ));

        log.info("=================== Price Range Filter Validation Completed ===================");
    }

    @Step("Validate discount filter response")
    public void validateDiscountResponse() {
        log.info("=================== Discount Filter Validation Started ===================");

        // Get discount range from test data
        String discountRange = testData.getOtherParams().get("values");
        String[] rangeLimits = discountRange.split("-");
        int minDiscount = Integer.parseInt(rangeLimits[0]);
        int maxDiscount = Integer.parseInt(rangeLimits[1].replace("%", ""));

//        double minDiscount = Double.parseDouble(rangeLimits[0]);
//        double maxDiscount = Double.parseDouble(rangeLimits[1].replace("%", ""));

        //log.info(String.format("Validating discount range:%.1%% to %.1f%%", minDiscount, maxDiscount));
        log.info(String.format("Validating discount range: %d%% to %d%%", minDiscount, maxDiscount));

        int numFound = ospreyApiResponse.getNumFound();
        Map<String, Object> facetCounts = ospreyApiResponse.getFacetData();
        int facetCount = getFacetCountForDiscount(facetCounts, discountRange,minDiscount,maxDiscount);
        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        int docsCount = products.size();
        //   boolean allProductsInRange = true;
        int productsInRange = 0;
        boolean testFailed = false;

        log.info("\n========== Product Discount Analysis ==========");
        StringBuilder discountDetails = new StringBuilder();

        //   for (int i = 0; i < products.size(); i++) {
        for(OspreyApiResponse.Doc product : products) {
            Double productDiscount = product.getAvgDiscount();
            //   double productDiscount = extractDiscountPercentage(Stri)
            //  double productDiscount = extractDiscountPercentage(String.valueOf(discountString));
            String productName = product.getProductName();

            boolean inRange = false;
            if (productDiscount != null && productDiscount >= 0) {  // Ensure discount is valid
                // Round to handle floating point precision
                inRange = productDiscount >= minDiscount && productDiscount <= maxDiscount;
//                 double roundedDiscount = Math.round(productDiscount * 100.0) / 100.0;
//                 inRange = roundedDiscount >= minDiscount && roundedDiscount <= maxDiscount;
            }

            if (inRange) {
                productsInRange++;
            }

            String discountLog = String.format(
                    "Product:\n" +
                            "Name: %s\n" +
                            "Discount: %.1f%%\n" +
                            "In Range: %s\n" +
                            "------------------------",
                    productName,
                    productDiscount,
                    inRange ? "Yes" : "No"
            );
            log.info(discountLog);
            discountDetails.append(discountLog).append("\n");
        }

        log.info(String.format("\nDiscount Range Debug:\n" +
                        "Min Discount: %.1f%%\n" +
                        "Max Discount: %.1f%%\n" +
                        "Total Products: %d\n" +
                        "Products in Range: %d\n",
                minDiscount, maxDiscount, products.size(), productsInRange));

        // Strict validation checks
        boolean productsExactMatch = (productsInRange == products.size());
        boolean countsMatch = (numFound == facetCount);
        boolean docsMatch = (numFound == docsCount);
        boolean exactMatch = productsExactMatch && countsMatch && docsMatch;

        log.info("\n========== Validation Results ==========");
        log.info(String.format("Total Products: %d", products.size()));
        log.info(String.format("Products in Range: %d", productsInRange));
        log.info(String.format("Products Match Status: %s", productsExactMatch ? "PASSED" : "FAILED"));
        log.info(String.format("Overall Status: %s", exactMatch ? "PASSED" : "FAILED"));

        // Force test failure if not exact match
        if (!exactMatch) {
            String errorMessage = String.format(
                    "Validation Failed:\n" +
                            "Products in range: %d/%d\n" +
                            "NumFound: %d\n" +
                            "FacetCount: %d\n" +
                            "DocsCount: %d",
                    productsInRange, products.size(),
                    numFound, facetCount, docsCount
            );
            log.error(errorMessage);
            Assert.fail(errorMessage);
        }

        DiscountStats stats = calculateDiscountStats(products);

        Allure.addAttachment("Discount Filter Results", String.format(
                "Test Summary:\n" +
                        "-------------\n" +
                        "Discount Range: %.1f%% - %.1f%%\n" +
                        "Total Products: %d\n" +
                        "Products in Range: %d\n\n" +
                        "Count Validation:\n" +
                        "----------------\n" +
                        "Products Match Status: %s\n" +
                        "NumFound: %d\n" +
                        "Facet Count: %d\n" +
                        "Docs Count: %d\n" +
                        "Overall Status: %s\n\n" +
                        "Discount Statistics:\n" +
                        "-------------------\n" +
                        "Minimum Discount: %.1f%%\n" +
                        "Maximum Discount: %.1f%%\n" +
                        "Average Discount: %.1f%%\n\n" +
                        "Validation Message: %s",
                minDiscount,
                maxDiscount,
                products.size(),
                productsInRange,
                productsExactMatch ? "MATCHED" : String.format("FAILED (%d/%d)", productsInRange, products.size()),
                numFound,
                facetCount,
                docsCount,
                exactMatch ? "PASSED" : "FAILED",
                stats.getMinDiscount(),
                stats.getMaxDiscount(),
                stats.getAverageDiscount(),
                exactMatch ? "All validations passed successfully":
                        String.format("Failed: %d products outside range, count mismatches found",
                                products.size() - productsInRange)  // %s
        ));
        log.info("=================== Discount Filter Validation Completed ===================");
    }

    private int getFacetCountForSize(Map<String, List<Map<String, Object>>> facetData, String size) {
        if (facetData == null || size == null) {
            return 0;
        }

        try {
            // Debug logging
            log.debug("Looking for size: " + size);
            log.debug("Facet Data: " + facetData);

            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
            if (facetFields != null) {
                for (Map<String, Object> field : facetFields) {
                    if ("verticalsizegroupformat_en_string_mv".equals(field.get("name"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
                        if (values != null) {
                            for (Map<String, Object> value : values) {
                                String facetValue = (String) value.get("name");
                                if (size.equals(facetValue)) {
                                    return ((Number) value.get("value")).intValue();
                                }
                            }
                        }
                    }
                }
            }

            log.debug("No matching size facet found");
            return ospreyApiResponse.numFound; // Fallback to numFound if facet not found

        } catch (Exception e) {
            log.error("Error extracting size facet count: " + e.getMessage());
            log.error("Facet Data structure: " + facetData);
            return ospreyApiResponse.numFound; // Fallback to numFound on error
        }
    }

    private int getFacetCountForL1L3Category(Map<String, List<Map<String, Object>>> facetData, String category) {
        if (facetData == null || category == null) {
            return 0;
        }

        try {
            // Debug logging
            log.debug("Looking for category: " + category);
            log.debug("Facet Data: " + facetData);

            // Try to get l1l3nestedcategory facets directly
            List<Map<String, Object>> l1l3Facets = facetData.get("l1l3nestedcategory_en_string_mv");
            if (l1l3Facets != null) {
                for (Map<String, Object> facet : l1l3Facets) {
                    String name = (String) facet.get("name");
                    if (category.equals(name)) {
                        Object value = facet.get("value");
                        if (value instanceof Number) {
                            return ((Number) value).intValue();
                        }
                    }
                }
            }

            // Alternative structure check
            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
            if (facetFields != null) {
                for (Map<String, Object> field : facetFields) {
                    if ("l1l3nestedcategory_en_string_mv".equals(field.get("name"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
                        if (values != null) {
                            for (Map<String, Object> value : values) {
                                String facetValue = (String) value.get("name");
                                if (category.equals(facetValue)) {
                                    return ((Number) value.get("value")).intValue();
                                }
                            }
                        }
                    }
                }
            }

            // If no match found, return numFound as fallback
            return ospreyApiResponse.getNumFound();

        } catch (Exception e) {
            log.error("Error extracting L1L3 category facet count: " + e.getMessage());
            log.error("Facet Data structure: " + facetData);
            return ospreyApiResponse.getNumFound(); // Fallback to numFound on error
        }
    }

    private int getFacetCountForGender(Map<String, List<Map<String, Object>>> facetData, String expectedgender) {
        if (facetData == null || expectedgender == null) {
            return 0;
        }
        try{
            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
            if (facetFields != null) {
                // Find the l1l2category_en_string_mv facet
                for (Map<String, Object> field : facetFields) {
                    String fieldName = (String) field.get("name");
                    if ("l1l2category_en_string_mv".equals(fieldName)) {
                        // Get the values list
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
                        if (values != null) {
                            // Find the matching gender count
                            for (Map<String, Object> value : values) {
                                String facetValue = (String) value.get("name");
                                if (expectedgender.equals(facetValue)) {
                                    return ((Number) value.get("value")).intValue();
                                }
                            }
                        }
                    }
                }
            }
            log.debug("Facet structure: " + facetData);
            log.debug("Expected gender: " + expectedgender);

        } catch (Exception e) {
            log.error("Error extracting facet count: " + e.getMessage());
        }

        return ospreyApiResponse.getNumFound();
    }


    private int getFacetCountForDiscount(Map<String, Object> facetData, String discountRange, double minDiscount, double maxDiscount)  {

        if (facetData == null) {
            return ospreyApiResponse.numFound; // Return numFound if facet data is not available
        }

        //     List<Map<String, Object>> discountFacets = facetData.get("trade_discounted_value_double");
        List<Map<String, Object>> discountFacets = (List<Map<String, Object>>) facetData.get("discount_string");
        if (discountFacets == null || discountFacets.isEmpty()) {
            return ospreyApiResponse.numFound; // Return numFound if discount facets are not available
        }

        int totalCount = 0;
        for (OspreyApiResponse.Doc product : ospreyApiResponse.getDocs()) {
            Double productDiscount = product.getAvgDiscount();
            if (productDiscount >= minDiscount && productDiscount <= maxDiscount) {
                totalCount++;
            }
            }
        return totalCount;
    }

    private int getFacetCountForPriceRange(Map<String, Object> facetData, String priceRange) {
        if (facetData == null || priceRange == null) {
            return 0;
        }

        try {
            // Debug the input
            log.debug("Price Range to find: " + priceRange);
            log.debug("Facet Data: " + facetData);

            List<Map<String, Object>> facetFields = (List<Map<String, Object>>) facetData.get("facet_fields");
            if (facetFields != null) {
                // Find price range facets
                for (Map<String, Object> field : facetFields) {
                    if ("pricerange_inr_string".equals(field.get("name"))) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
                        if (values != null) {
                            // Format the price range to match facet format
                            String formattedRange = priceRange.replace("|", " TO ");

                            // Find matching facet
                            for (Map<String, Object> value : values) {
                                String facetValue = (String) value.get("name");
                                if (formattedRange.equals(facetValue)) {
                                    Number count = (Number) value.get("count");
                                    return count != null ? count.intValue() : 0;
                                }
                            }
                        }
                    }
                }
            }

            // If no match found, try alternative facet structure
            List<Map<String, Object>> priceFacets = (List<Map<String, Object>>) facetData.get("pricerange_inr_string");
            if (priceFacets != null) {
                String formattedRange = priceRange.replace("|", " TO ");
                for (Map<String, Object> facet : priceFacets) {
                    if (formattedRange.equals(facet.get("name"))) {
                        Number count = (Number) facet.get("count");
                        return count != null ? count.intValue() : 0;
                    }
                }
            }

            log.warn("No matching price range facet found for: " + priceRange);

        } catch (Exception e) {
            log.error("Error extracting price range facet count: " + e.getMessage(), e);
            log.error("Facet Data: " + facetData);
        }

        // If no facet count found, return numFound as fallback
        return ospreyApiResponse.getNumFound();
    }

    private static class DiscountStats {
        private Double minDiscount = Double.MAX_VALUE;
        private Double maxDiscount = Double.MIN_VALUE;
        private Double totalDiscount = (double) 0;
        private int count = 0;

        public void addDiscount(Double discount) {
            if (discount > 0) {
                minDiscount = Math.min(minDiscount, discount);
                maxDiscount = Math.max(maxDiscount, discount);
                totalDiscount += discount;
                count++;
            }
        }

        public double getMinDiscount() {
            return minDiscount == Double.MAX_VALUE ? 0 : minDiscount;
        }

        public double getMaxDiscount() {
            return maxDiscount == Double.MIN_VALUE ? 0 : maxDiscount;
        }

        public double getAverageDiscount() {
            return count > 0 ? totalDiscount / count : 0;
        }
    }

    private DiscountStats calculateDiscountStats(List<OspreyApiResponse.Doc> products) {
        DiscountStats stats = new DiscountStats();
        for (OspreyApiResponse.Doc product : products) {
            Double discount = product.getAvgDiscount(); //extractDiscountPercentage(String.valueOf(product.getTradeDiscountedValueDouble()));
            stats.addDiscount(discount);
        }
        return stats;
    }

}
//    private int getBrandFacetCount(Map<String, List<Map<String, Object>>> facetData, String expectedBrand) {
//        if (facetData == null) {
//            log.warn("Facet data is null");
//            return 0;
//        }
//
//        List<Map<String, Object>> brandFacets = facetData.get("brand_string_mv");
//        if (brandFacets == null) {
//            log.warn("Brand facets not found");
//            return 0;
//        }
//
//        log.info("Processing brand facets, size: " + brandFacets.size());
//        for (Map<String, Object> facet : brandFacets) {
//            String brandName = String.valueOf(facet.get("name"));
//            log.debug("Checking brand facet: " + brandName);
//
//            if (brandName.toLowerCase().contains(expectedBrand)) {
//                Object valueObj = facet.get("value");
//                if (valueObj instanceof Number) {
//                    int count = ((Number) valueObj).intValue();
//                    log.info("Found matching brand facet count: " + count);
//                    return count;
//                } else if (valueObj != null) {
//                    try {
//                        int count = Integer.parseInt(valueObj.toString());
//                        log.info("Parsed brand facet count: " + count);
//                        return count;
//                    } catch (NumberFormatException e) {
//                        log.warn("Could not parse facet value: " + valueObj);
//                    }
//                }
//            }
//        }
//
//    log.warn("No matching brand facet found for: " + expectedBrand);
//    return ospreyApiResponse.numFound; // Use numFound as fallback
//}
//
//
//    @Step("Validate color filter response")
//    public void validateColorFilterResponse() {
//        log.info("Starting color filter validation");
//
//        int numFoundCount = ospreyApiResponse.getNumFound();
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//        String expectedColor = getExpectedColor().toLowerCase();
//
//        // Calculate matching products count
//        int matchingProductsCount = 0;
//        StringBuilder productAnalysis = new StringBuilder();
//
//        for (OspreyApiResponse.Doc product : products) {
//            List<String> productColors = new ArrayList<>();
//            String colorGroup = product.getColorGroup_string();
//            if (colorGroup != null) {
//                productColors.add(colorGroup);
//            }
//         //   List<String> productColors = product.getColorGroup_string();
//            boolean hasMatchingColor = false;
//
//            if (!productColors.isEmpty()) {
//                hasMatchingColor = productColors.stream()
//                        .map(String::toLowerCase)
//                        .anyMatch(color -> color.contains(expectedColor));
//            }
//
//            if (hasMatchingColor) {
//                matchingProductsCount++;
//            }
//
//            // Log product details
//            productAnalysis.append(String.format(
//                    "Product: %s\nColors: %s\nMatches Filter: %s\n---\n",
//                    product.getNameTextEn(),
//                    productColors != null ? String.join(", ", productColors) : "No colors",
//                    hasMatchingColor ? "Yes" : "No"
//            ));
//        }
//
//        // Calculate facet count
//        Map<String, List<Map<String, Object>>> facetData = ospreyApiResponse.getFacetData();
//        int facetCount = calculateFacetCount(facetData, expectedColor);
//
//        // Validation checks
//        boolean allProductsMatch = (matchingProductsCount == docsCount);
//        boolean facetMatchesNumFound = (facetCount == numFoundCount);
//        boolean docsMatchNumFound = (docsCount == numFoundCount);
//        boolean allCountsMatch = allProductsMatch && facetMatchesNumFound && docsMatchNumFound;
//
//        // Log validation results
//        log.info("\n=== Color Filter Validation Results ===");
//        log.info(String.format("Expected Color: %s", expectedColor));
//        log.info(String.format("NumFound Count: %d", numFoundCount));
//        log.info(String.format("Docs Count: %d", docsCount));
//        log.info(String.format("Matching Products: %d", matchingProductsCount));
//        log.info(String.format("Facet Count: %d", facetCount));
//
//        if (!allCountsMatch) {
//            String errorMessage = String.format(
//                    "Color Filter Validation Failed:\n" +
//                            "NumFound Count: %d\n" +
//                            "Docs Count: %d\n" +
//                            "Matching Products: %d\n" +
//                            "Facet Count: %d\n\n" +
//                            "Product Analysis:\n%s",
//                    numFoundCount, docsCount, matchingProductsCount, facetCount,
//                    productAnalysis.toString()
//            );
//            log.error(errorMessage);
//            Assert.fail(errorMessage);
//        }
//
//        // Add Allure report
//        Allure.addAttachment("Color Filter Validation", String.format(
//                "Test Summary:\n" +
//                        "-------------\n" +
//                        "Color Filter: %s\n" +
//                        "Store: %s\n" +
//                        "Page Number: %s\n\n" +
//
//                        "Count Validation:\n" +
//                        "----------------\n" +
//                        "NumFound Count: %d\n" +
//                        "Docs Count: %d\n" +
//                        "Matching Products: %d\n" +
//                        "Facet Count: %d\n\n" +
//
//                        "Validation Status:\n" +
//                        "----------------\n" +
//                        "All Counts Match: %s\n" +
//                        "Products Match Filter: %s\n" +
//                        "Facet Count Match: %s\n" +
//                        "Docs Count Match: %s\n\n" +
//
//                        "Validation Details:\n" +
//                        "-----------------\n" +
//                        "Expected Color: %s\n" +
//                        "Total Products Found: %d\n" +
//                        "Products with Color: %d\n" +
//                        "Products without Color: %d\n" +
//                        "Match Percentage: %.2f%%\n\n" +
//
//                        "Product Analysis:\n" +
//                        "----------------\n%s",
//
//                expectedColor,
//                testData.getOtherParams().get("store"),
//                testData.getOtherParams().get("page_number"),
//
//                numFoundCount,
//                docsCount,
//                matchingProductsCount,
//                facetCount,
//
//                allCountsMatch ? "PASSED" : "FAILED",
//                (matchingProductsCount == docsCount) ? "PASSED" : "FAILED",
//                (facetCount == numFoundCount) ? "PASSED" : "FAILED",
//                (docsCount == numFoundCount) ? "PASSED" : "FAILED",
//
//                expectedColor,
//                docsCount,
//                matchingProductsCount,
//                (docsCount - matchingProductsCount),
//                ((double) matchingProductsCount / docsCount) * 100,
//
//                productAnalysis.toString()
//        ));
//
//
//    }
//
//    private int calculateFacetCount(Map<String, List<Map<String, Object>>> facetData, String expectedColor) {
//        if (facetData == null) {
//            log.warn("Facet data is null");
//            return ospreyApiResponse.getNumFound();
//        }
//
//        List<Map<String, Object>> colorFacets = facetData.get("verticalcolorfamily_en_string_mv");
//        if (colorFacets == null) {
//            log.warn("Color facets not found in facet data, trying alternative field");
//            colorFacets = facetData.get("verticalcolorfamily_en_string_mv");
//        }
//        if (colorFacets != null) {
//            for (Map<String, Object> facet : colorFacets) {
//                String colorName = String.valueOf(facet.get("name"));
//                log.debug("Checking facet color: " + colorName + " against expected: " + expectedColor);
//                if (colorName.toLowerCase().contains(expectedColor.toLowerCase())) {
//                    Object valueObj = facet.get("value");
//                    if (valueObj != null) {
//                        if (valueObj instanceof Number) {
//                            int count = ((Number) valueObj).intValue();
//                            log.info("Found matching color facet count: " + count);
//                            return count;
//                        } else {
//                            try {
//                                int count = Integer.parseInt(valueObj.toString());
//                                log.info("Found matching color facet count: " + count);
//                                return count;
////                        return Integer.parseInt(valueObj.toString());
//                            } catch (NumberFormatException e) {
//                                log.warn("Could not parse facet value: " + valueObj);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//                log.warn("No matching color facet found, using numFound as fallback");
//                return ospreyApiResponse.getNumFound();
//            }
//
//    private String getExpectedColor() {
//        Map<String, String> params = testData.getOtherParams();
//        if (params == null) {
//            throw new IllegalStateException("Test data parameters are null");
//        }
//
//        String expectedColor = params.get("values");
//        if (expectedColor == null || expectedColor.trim().isEmpty()) {
//            throw new IllegalArgumentException("Color value is missing in test data");
//        }
//        return expectedColor.toLowerCase();
//    }
//
//    private int countMatchingProducts(Map<String, List<Map<String, Object>>> facetData,String expectedColor) {
////        int matchCount = 0;
////        log.info("\nProduct Color Analysis:");
////
////        for (OspreyApiResponse.Doc product : ospreyApiResponse.getDocs()) {
////            String productColor = product.getColorGroup_string();
////            if (productColor != null && !productColor.isEmpty()) {
////                log.info("Checking product color: " + productColor);
////                if (productColor.toLowerCase().contains(expectedColor)) {
////                    matchCount++;
////                    log.info("✓ Match found - Current count: " + matchCount);
////                }
////            }
////        }
////        return matchCount;
////    }
//        if (facetData == null || expectedColor == null) {
//            log.warn("Facet data or expected color is null");
//            return countProductsWithColor(expectedColor);
//        }
//
//        try {
//            // Debug the input
//            log.debug("Expected Color to find: " + expectedColor);
//            log.debug("Facet Data: " + facetData);
//
//            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
//            if (facetFields != null) {
//                // Find color facets
//                for (Map<String, Object> field : facetFields) {
//                    if ("verticalcolorfamily_en_string_mv".equals(field.get("name"))) {
//                        @SuppressWarnings("unchecked")
//                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
//                        if (values != null) {
//                            // Find matching color facet
//                            for (Map<String, Object> value : values) {
//                                String facetColor = String.valueOf(value.get("name"));
//                                if (facetColor.toLowerCase().contains(expectedColor.toLowerCase())) {
//                                    Number count = (Number) value.get("count");
//                                    return count != null ? count.intValue() : 0;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // If no match found, try alternative facet structure
//            List<Map<String, Object>> colorFacets = facetData.get("verticalcolorfamily_en_string_mv");
//            if (colorFacets != null) {
//                for (Map<String, Object> facet : colorFacets) {
//                    String facetColor = String.valueOf(facet.get("name"));
//                    if (facetColor.toLowerCase().contains(expectedColor.toLowerCase())) {
//                        Object valueObj = facet.get("value");
//                        if (valueObj instanceof Number) {
//                            return ((Number) valueObj).intValue();
//                        } else if (valueObj instanceof String) {
//                            try {
//                                return Integer.parseInt((String) valueObj);
//                            } catch (NumberFormatException e) {
//                                log.warn("Could not parse facet value: " + valueObj);
//
//                            }
//                        }
//                    }
//                }
//            }
//
//            log.warn("No matching color facet found for: " + expectedColor);
//            //  log.warn("No matching color facet found for: " + expectedColor);
//            return countProductsWithColor(expectedColor);
//
//        } catch (Exception e) {
//            log.error("Error extracting color facet count: " + e.getMessage(), e);
//            log.error("Facet Data: " + facetData);
//            return countProductsWithColor(expectedColor);
//        }
//    }
//        private int countProductsWithColor(String expectedColor) {
//            if (ospreyApiResponse == null || ospreyApiResponse.getDocs() == null) {
//                return 0;
//            }
//
//            return (int) ospreyApiResponse.getDocs().stream()
//                    .filter(doc -> doc.getColorStringMv() != null)
//                    .filter(doc -> doc.getColorStringMv().stream()
//                            .map(String::toLowerCase)
//                            .anyMatch(color -> color.contains(expectedColor.toLowerCase())))
//                    .count();
//        }
//        // If no facet count found, count matching products
//      //  return countMatchingProducts(expectedColor);
//
//
//    @Step("Validate price range filter response")
//    public void validatePriceRangeResponse() {
//        log.info("Price Range Filter Validation Started");
//
//        // Get price range from test data
//        String priceRange = testData.getOtherParams().get("values");
//        String[] rangeLimits = priceRange.split("\\|");
//        double minPrice = Double.parseDouble(rangeLimits[0]);
//        double maxPrice = Double.parseDouble(rangeLimits[1]);
//
//        log.info(String.format("Validating price range: %.2f to %.2f INR", minPrice, maxPrice));
//
//        // Get counts for validation
//        int numFound = ospreyApiResponse.getNumFound();
//        Map<String, List<Map<String, Object>>> facetCounts = ospreyApiResponse.getFacetData();
//        int facetCount = getFacetCountForPriceRange(facetCounts, priceRange);
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//        //   boolean allProductsInRange = true;
//        int productsInRange = 0;
//
//        // Validate counts match
////        boolean countsMatch = (numFound == facetCount);
////        boolean docsMatch = (numFound == docsCount);
////
////        log.info("\n========== Count Validation ==========");
////        log.info(String.format("NumFound: %d", numFound));
////        log.info(String.format("Facet Count: %d", facetCount));
////        log.info(String.format("Docs Count: %d", docsCount));
////        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
////        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
//
//
//        log.info("\nProduct Price Range Analysis");
//        StringBuilder priceDetails = new StringBuilder();
//
//      //  for (int i = 0; i < products.size(); i++) {
//            for (OspreyApiResponse.Doc product : products) {  // .get(i);
//                double productPrice = product.getPriceInrDouble();
//                String productName = product.getNameTextEn();
//
//                // Add debug logging to see actual prices
//                log.info(String.format("Checking product price: %.2f against range: %.2f - %.2f",
//                        productPrice, minPrice, maxPrice));
//
//                // Updated range check with tolerance for floating point comparison
//                boolean inRange = false;
//                if (productPrice >= 0) {  // Validate price
//                    double roundedPrice = Math.round(productPrice * 100.0) / 100.0;
//                    inRange = roundedPrice >= minPrice && roundedPrice <= maxPrice;
//                }
//
//                if (inRange) {
//                    productsInRange++;
//                }
//
//                String priceLog = String.format(
//                        "Product:\n" +
//                                "Name: %s\n" +
//                                "Price (INR): %.2f\n" +
//                                "Expected Range: %.2f - %.2f\n" +
//                                "In Range: %s\n" +
//                                "------------------------",
//                        productName, productPrice, minPrice, maxPrice, inRange ? "Yes" : "No"
//                );
//                log.info(priceLog);
//            }
//
//// Add debug logging after the loop
//        log.info(String.format("\nPrice Range Analysis Debug:\n" +
//                        "Min Price: %.2f\n" +
//                        "Max Price: %.2f\n" +
//                        "Total Products: %d\n" +
//                        "Products in Range: %d\n",
//                minPrice, maxPrice, products.size(), productsInRange));
//
//            // Strict validation checks
//            boolean productsExactMatch = (productsInRange == products.size());
//            boolean countsMatch = (numFound == facetCount);
//            boolean docsMatch = (numFound == docsCount);
//            boolean exactMatch = productsExactMatch && countsMatch && docsMatch;
//
//            log.info("\n========== Validation Results ==========");
//            log.info(String.format("Total Products: %d", products.size()));
//            log.info(String.format("Products in Range: %d", productsInRange));
//            log.info(String.format("Products Match Status: %s", productsExactMatch ? "PASSED" : "FAILED"));
//            log.info(String.format("Overall Status: %s", exactMatch ? "PASSED" : "FAILED"));
//
//            // Force test failure if not exact match
//            if (!exactMatch) {
//                String errorMessage = String.format(
//                        "Validation Failed:\n" +
//                                "Products in range: %d/%d\n" +
//                                "NumFound: %d\n" +
//                                "FacetCount: %d\n" +
//                                "DocsCount: %d",
//                        productsInRange, products.size(),
//                        numFound, facetCount, docsCount
//                );
//                log.error(errorMessage);
//                Assert.fail(errorMessage);
//            }
//
//            // Calculate statistics
//            DoubleSummaryStatistics priceStats = products.stream()
//                    .mapToDouble(OspreyApiResponse.Doc::getPriceInrDouble)
//                    .summaryStatistics();
//
//            // Update Allure report
//            Allure.addAttachment("Price Range Filter Results", String.format(
//                    "Test Summary:\n" +
//                            "-------------\n" +
//                            "Price Range: %.2f - %.2f INR\n" +
//                            "Total Products: %d\n" +
//                            "Products in Range: %d\n\n" +
//                            "Count Validation:\n" +
//                            "----------------\n" +
//                            "Products Match Status: %s\n" +
//                            "NumFound: %d\n" +
//                            "Facet Count: %d\n" +
//                            "Docs Count: %d\n" +
//                            "Overall Status: %s\n\n" +
//                            "Price Statistics:\n" +
//                            "----------------\n" +
//                            "Minimum Price: %.2f INR\n" +
//                            "Maximum Price: %.2f INR\n" +
//                            "Average Price: %.2f INR\n\n" +
//                            "Validation Message: %s",
//                    minPrice, maxPrice,
//                    products.size(), productsInRange,
//                    productsExactMatch ? "MATCHED" : String.format("FAILED (%d/%d)", productsInRange, products.size()),
//                    numFound, facetCount, docsCount,
//                    exactMatch ? "PASSED" : "FAILED",
//                    priceStats.getMin(),
//                    priceStats.getMax(),
//                    priceStats.getAverage(),
//                    exactMatch ?
//                            "All validations passed successfully" :
//                            String.format("Failed: %d products outside range, count mismatches found",
//                                    products.size() - productsInRange)
//            ));
//
//            log.info("=================== Price Range Filter Validation Completed ===================");
//        }
//
//
//    @Step("Validate discount filter response")
//    public void validateDiscountResponse() {
//        log.info("=================== Discount Filter Validation Started ===================");
//
//        // Get discount range from test data
//        String discountRange = testData.getOtherParams().get("values");
//        String[] rangeLimits = discountRange.split("-");
//        double minDiscount = Double.parseDouble(rangeLimits[0]);
//        double maxDiscount = Double.parseDouble(rangeLimits[1].replace("%", ""));
//
//        log.info(String.format("Validating discount range:%.1f%% to %.1f%%", minDiscount, maxDiscount));
//
//        int numFound = ospreyApiResponse.getNumFound();
//        Map<String, List<Map<String, Object>>> facetCounts = ospreyApiResponse.getFacetData();
//        int facetCount = getFacetCountForDiscount(facetCounts, discountRange,minDiscount,maxDiscount);
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//        //   boolean allProductsInRange = true;
//        int productsInRange = 0;
//        boolean testFailed = false;
//
////        // Validate counts match
////        boolean countsMatch = (numFound == facetCount);
////        boolean docsMatch = (numFound == docsCount);
////
////        log.info("\n========== Count Validation ==========");
////        log.info(String.format("NumFound: %d", numFound));
////        log.info(String.format("Facet Count: %d", facetCount));
////        log.info(String.format("Docs Count: %d", docsCount));
////        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
////        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
//
//        log.info("\n========== Product Discount Analysis ==========");
//        StringBuilder discountDetails = new StringBuilder();
//
//     //   for (int i = 0; i < products.size(); i++) {
//         for(OspreyApiResponse.Doc product : products) {
//            double productDiscount = product.getTradeDiscountedValueDouble();
//            //   double productDiscount = extractDiscountPercentage(Stri)
//            //  double productDiscount = extractDiscountPercentage(String.valueOf(discountString));
//            String productName = product.getNameTextEn();
//
////            boolean inRange = productDiscount >= minDiscount && productDiscount <= maxDiscount;
////            if (!inRange) {
////                //  allProductsInRange = false;
////                productsInRange++;
////            } else {
////                productsInRange++;
////            }
//             boolean inRange = false;
//             if (productDiscount >= 0) {  // Ensure discount is valid
//                 // Round to handle floating point precision
//                 double roundedDiscount = Math.round(productDiscount * 100.0) / 100.0;
//                 inRange = roundedDiscount >= minDiscount && roundedDiscount <= maxDiscount;
//             }
//
//             if (inRange) {
//                 productsInRange++;
//             }
//
//             String discountLog = String.format(
//                     "Product:\n" +
//                             "Name: %s\n" +
//                             "Discount: %.2f%%\n" +
//                             "In Range: %s\n" +
//                             "------------------------",
//                     productName,
//                     productDiscount,
//                     inRange ? "Yes" : "No"
//             );
//                log.info(discountLog);
//                discountDetails.append(discountLog).append("\n");
//            }
//
//        log.info(String.format("\nDiscount Range Debug:\n" +
//                        "Min Discount: %.2f%%\n" +
//                        "Max Discount: %.2f%%\n" +
//                        "Total Products: %d\n" +
//                        "Products in Range: %d\n",
//                minDiscount, maxDiscount, products.size(), productsInRange));
//
//            // Strict validation checks
//            boolean productsExactMatch = (productsInRange == products.size());
//            boolean countsMatch = (numFound == facetCount);
//            boolean docsMatch = (numFound == docsCount);
//            boolean exactMatch = productsExactMatch && countsMatch && docsMatch;
//
//            log.info("\n========== Validation Results ==========");
//            log.info(String.format("Total Products: %d", products.size()));
//            log.info(String.format("Products in Range: %d", productsInRange));
//            log.info(String.format("Products Match Status: %s", productsExactMatch ? "PASSED" : "FAILED"));
//            log.info(String.format("Overall Status: %s", exactMatch ? "PASSED" : "FAILED"));
//
//            // Force test failure if not exact match
//            if (!exactMatch) {
//                String errorMessage = String.format(
//                        "Validation Failed:\n" +
//                                "Products in range: %d/%d\n" +
//                                "NumFound: %d\n" +
//                                "FacetCount: %d\n" +
//                                "DocsCount: %d",
//                        productsInRange, products.size(),
//                        numFound, facetCount, docsCount
//                );
//                log.error(errorMessage);
//                Assert.fail(errorMessage);
//            }
//
//            DiscountStats stats = calculateDiscountStats(products);
//
//            Allure.addAttachment("Discount Filter Results", String.format(
//                    "Test Summary:\n" +
//                            "-------------\n" +
//                            "Discount Range: %.2f%% - %.2f%%\n" +
//                            "Total Products: %d\n" +
//                            "Products in Range: %d\n\n" +
//                            "Count Validation:\n" +
//                            "----------------\n" +
//                            "Products Match Status: %s\n" +
//                            "NumFound: %d\n" +
//                            "Facet Count: %d\n" +
//                            "Docs Count: %d\n" +
//                            "Overall Status: %s\n\n" +
//                            "Discount Statistics:\n" +
//                            "-------------------\n" +
//                            "Minimum Discount: %.2f%%\n" +
//                            "Maximum Discount: %.2f%%\n" +
//                            "Average Discount: %.2f%%\n\n" +
//                            "Validation Message: %s",
//                    minDiscount,                // %.2f
//                    maxDiscount,               // %.2f
//                    products.size(),           // %d
//                    productsInRange,           // %d
//                    productsExactMatch ? "MATCHED" : String.format("FAILED (%d/%d)", productsInRange, products.size()),  // %s
//                    numFound,                  // %d
//                    facetCount,               // %d
//                    docsCount,                // %d
//                    exactMatch ? "PASSED" : "FAILED",  // %s
//                    stats.getMinDiscount(),    // %.2f
//                    stats.getMaxDiscount(),    // %.2f
//                    stats.getAverageDiscount(), // %.2f
//                    exactMatch ?
//                            "All validations passed successfully" :
//                            String.format("Failed: %d products outside range, count mismatches found",
//                                    products.size() - productsInRange)  // %s
//            ));
//            log.info("=================== Discount Filter Validation Completed ===================");
//        }
//
//
//    @Step("Validate gender filter response")
//    public void validateGenderFilterResponse() {
//        log.info("=================== Gender Filter Validation Started ===================");
//
//        // Get expected gender from test data
//        String expectedGender = testData.getOtherParams().get("values");
//        log.info("Validating gender filter: " + expectedGender);
//
//        // Get all counts for validation
//        int numFound = ospreyApiResponse.getNumFound();
//        Map<String, List<Map<String, Object>>> facetCounts = ospreyApiResponse.getFacetData();
//        int facetCount = getFacetCountForGender(facetCounts, expectedGender);
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//       // boolean allProductsMatchGender = true;
//        int matchingProducts = 0;
//        boolean testFailed = false;
//
//        log.info("\n========== Count Validation ==========");
//        log.info(String.format("NumFound: %d", numFound));
//        log.info(String.format("Facet Count: %d", facetCount));
//        log.info(String.format("Docs Count: %d", docsCount));
//
//        StringBuilder genderDetails = new StringBuilder();
//
//        for (OspreyApiResponse.Doc product : products) {
//            String productGender = product.getL1l2categoryEnStringMv() != null && !product.getL1l2categoryEnStringMv().isEmpty() ?
//                    product.getL1l2categoryEnStringMv().get(0) : "";
//            String l1l2Category = product.getL1l2categoryEnStringMv() != null && !product.getL1l2categoryEnStringMv().isEmpty() ?
//                    product.getL1l2categoryEnStringMv().get(0) : "";
//            String productName = product.getNameTextEn() != null ? product.getNameTextEn() : "";
//
//            boolean matches = (productGender != null && productGender.equalsIgnoreCase(expectedGender)) ||
//                    (l1l2Category != null && l1l2Category.contains(expectedGender));
//
//            if (matches) {
//                matchingProducts++;
//            }
//
//
//            // Validate all counts match
////        boolean countsMatch = (numFound == facetCount);
////        boolean docsMatch = (numFound == docsCount); // docs might be paginated
////        boolean productsExactMatch = (matchingProducts == products.size());
////        boolean exactMatch = countsMatch && docsMatch && productsExactMatch;
////        boolean productsExactMatch = (matchingProducts == products.size());
////        boolean countsMatch = (numFound == facetCount);
////        boolean docsMatch = (numFound == docsCount);
////        boolean exactMatch = productsExactMatch && countsMatch && docsMatch;
////
////        // Force test failure when product counts don't match
////        if (matchingProducts != products.size()) {
////            allProductsMatchGender = false;
////            softAssert.fail(String.format(
////                    "Product count mismatch - Total: %d, Matching: %d, Difference: %d",
////                    products.size(), matchingProducts, products.size() - matchingProducts));
////        }
////
////        // Update assertions
////        softAssert.assertTrue(allProductsMatchGender,
////                String.format("All products must match gender: %s. Found %d/%d matching products",
////                        expectedGender, matchingProducts, products.size()));
//////        log.info("\n========== Count Validation ==========");
//////        log.info(String.format("NumFound: %d", numFound));
//////        log.info(String.format("Facet Count: %d", facetCount));
//////        log.info(String.format("Docs Count: %d", docsCount));
//////        log.info(String.format("Exact Match Status: %s", exactMatch ? "PASSED" : "FAILED"));
//////        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
//////        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
////
////
////        log.info("\n========== Validation Results ==========");
////        log.info(String.format("Total Products: %d", products.size()));
////        log.info(String.format("Matching Products: %d", matchingProducts));
////        log.info(String.format("Products Exact Match: %s", productsExactMatch ? "Yes" : "No"));
////        log.info(String.format("Overall Exact Match: %s", exactMatch ? "PASSED" : "FAILED"));
////
////        log.info("\n========== Product Gender Analysis ==========");
////        StringBuilder genderDetails = new StringBuilder();
////
////        int matchingProducts = 0;
////        boolean allProductsMatchGender = true;
////
////        for (int i = 0; i < products.size(); i++) {
////            OspreyApiResponse.Doc product = products.get(i);
////
////            // Fix List<String> handling
////            String productGender = product.getL1l2categoryEnStringMv() != null && !product.getL1l2categoryEnStringMv().isEmpty() ?
////                    product.getL1l2categoryEnStringMv().get(0) : "";
////            String l1l2Category = product.getL1l2categoryEnStringMv() != null && !product.getL1l2categoryEnStringMv().isEmpty() ?
////                    product.getL1l2categoryEnStringMv().get(0) : "";
////            String productName = product.getNameTextEn() != null ? product.getNameTextEn() : "";
////
////
////            //  boolean matches = productGender != null && productGender.equalsIgnoreCase(expectedGender);
////            boolean matches = (productGender != null && productGender.equalsIgnoreCase(expectedGender)) ||
////                    (l1l2Category != null && l1l2Category.contains(expectedGender));
////
////            if (!matches) {
////                allProductsMatchGender = false;
////            } else {
////                matchingProducts++;
////            }
//
//            String genderLog = String.format(
//                    "Product %d:%n" +
//                            "Name: %s%n" +
//                            "Gender Filter: %s%n" +
//                            "L1L2 Category: %s%n" +
//                            "Matches Filter: %s%n" +
//                            "------------------------",
//                    products.indexOf(product) + 1, productName, productGender, l1l2Category, matches ? "Yes" : "No"
//            );
//            log.info(genderLog);
//            genderDetails.append(genderLog).append("\n");
//        }
//
//        boolean productsExactMatch = (matchingProducts == products.size());
//        boolean countsMatch = (numFound == facetCount);
//        boolean docsMatch = (numFound == docsCount);
//        boolean exactMatch = productsExactMatch && countsMatch && docsMatch;
//
//        log.info("\n========== Validation Results ==========");
//        log.info(String.format("Total Products: %d", products.size()));
//        log.info(String.format("Matching Products: %d", matchingProducts));
//        log.info(String.format("Products Match Status: %s", productsExactMatch ? "PASSED" : "FAILED"));
//        log.info(String.format("Overall Status: %s", exactMatch ? "PASSED" : "FAILED"));
//
//
////        if (!productsExactMatch) {
////            String errorMessage = String.format(
////                    "Product count mismatch - Total: %d, Matching: %d, Non-matching: %d",
////                    products.size(), matchingProducts, products.size() - matchingProducts);
////            log.error(errorMessage);
////            softAssert.fail(errorMessage);
////        }
//        if (matchingProducts != products.size()) {
//            testFailed = true;
//            String errorMessage = String.format(
//                    "VALIDATION FAILED: Product count mismatch\n" +
//                            "Total Products: %d\n" +
//                            "Matching Products: %d\n" +
//                            "Non-matching Products: %d",
//                    products.size(), matchingProducts, products.size() - matchingProducts);
//            log.error(errorMessage);
//            Assert.fail(errorMessage);  // This will immediately fail the test
//        }
//
//        // Additional validations if needed
//        if (numFound != facetCount || numFound != docsCount) {
//            testFailed = true;
//            String errorMessage = String.format(
//                    "VALIDATION FAILED: Count mismatch\n" +
//                            "NumFound: %d\n" +
//                            "FacetCount: %d\n" +
//                            "DocsCount: %d",
//                    numFound, facetCount, docsCount);
//            log.error(errorMessage);
//            Assert.fail(errorMessage);
//        }
//
//        softAssert.assertTrue(countsMatch,
//                String.format("Count mismatch: NumFound=%d, FacetCount=%d", numFound, facetCount));
//        softAssert.assertTrue(docsMatch,
//                String.format("Docs count mismatch: NumFound=%d, DocsCount=%d", numFound, docsCount));
//        softAssert.assertTrue(productsExactMatch,
//                String.format("Product match failure: %d/%d products match the gender filter",
//                        matchingProducts, products.size()));
//
////        log.info("\nGender Filter Analysis:");
////        log.info("Expected Gender: " + expectedGender);
////        log.info("Total Products: " + products.size());
////        log.info("Matching Products: " + matchingProducts);
//
//        Allure.addAttachment("Gender Filter Results", String.format(
//                "Test Summary:\n" +
//                        "-------------\n" +
//                        "Expected Gender: %s\n" +
//                        "Total Products: %d\n" +
//                        "Matching Products: %d\n\n" +
//                        "Count Validation:\n" +
//                        "----------------\n" +
//                        "Products Match Status: %s\n"    +
//                        "NumFound: %d\n" +
//                        "Facet Count: %d\n" +
//                        "Docs Count: %d\n" +
//                        "Counts Match Status: %s\n" +
//                        "Docs Match Status: %s\n\n" +
//                        "Filter Statistics:\n" +
//                        "----------------\n" +
//                        "Match Percentage: %.2f%%\n\n" +
//                        "Validation:\n" +
//                        "----------\n" +
//                        "Filter Compliance: %s\n" +
//                        "Validation Message: %s\n" +
//                        "Pagination Note: %s",
//                expectedGender,
//                products.size(),
//                matchingProducts,
//                productsExactMatch ? "MATCHED" : String.format("FAILED (%d,%d)",
//                        matchingProducts, products.size()),
//                numFound,
//                facetCount,
//                docsCount,
//                countsMatch ? "MATCHED" : "MISMATCH",
//                docsMatch ? "VALID" : "INVALID",
//                (matchingProducts * 100.0 / products.size()),
//                exactMatch ? "PASSED" : "FAILED",
//                exactMatch ?
//                        "All products match the gender filter" :
//                        String.format("Found %d products with incorrect gender", products.size() - matchingProducts),
//                exactMatch ? "PASSED" : "FAILED"
//        ));
//
//        log.info("=================== Gender Filter Validation Completed ===================");
//    }
//
//    @Step("Validate L1L3 category filter response")
//    public void validateL1L3CategoryFilterResponse() {
//        log.info("=================== L1L3 Category Filter Validation Started ===================");
//
//        // Get expected category from test data
//        String expectedCategory = testData.getOtherParams().get("values");
//        log.info("Validating L1L3 category filter: " + expectedCategory);
//
//        // Get all counts for validation
//        int numFound = ospreyApiResponse.getNumFound();
//        Map<String, List<Map<String, Object>>> facetCounts = ospreyApiResponse.getFacetData();
//        int facetCount = getFacetCountForL1L3Category(facetCounts, expectedCategory);
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//        boolean allProductsMatchCategory = true;
//        int matchingProducts = 0;
//
//        log.info("\n========== Count Validation ==========");
//        log.info(String.format("NumFound: %d", numFound));
//        log.info(String.format("Facet Count: %d", facetCount));
//        log.info(String.format("Docs Count: %d", docsCount));
//
//        // Validate all counts match
//        boolean countsMatch = (numFound == facetCount);
//        boolean docsMatch = (numFound == docsCount); // docs might be paginated
//        boolean exactMatch = countsMatch && docsMatch && allProductsMatchCategory;
//
//        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
//        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
//        log.info(String.format("Exact Match: %s", exactMatch ? "Yes" : "No"));
//
//        log.info("\n========== Product Category Analysis ==========");
//        StringBuilder categoryDetails = new StringBuilder();
//
//        for (int i = 0; i < products.size(); i++) {
//            OspreyApiResponse.Doc product = products.get(i);
//            List<String> productCategories = product.getL1l3categoryEnStringMv();
//            String productName = product.getNameTextEn();
//            String categoryList = productCategories != null ? String.join(", ", productCategories) : "";
//
//            boolean matches = productCategories != null && productCategories.contains(expectedCategory);
//            if (!matches) {
//                allProductsMatchCategory = false;
//            } else {
//                matchingProducts++;
//            }
//
//            String categoryLog = String.format(
//                    "Product %d:\n" +
//                            "Name: %s\n" +
//                            "Categories: %s\n" +
//                            "Matches Filter: %s\n" +
//                            "------------------------",
//                    (i + 1),
//                    productName,
//                    categoryList,
//                    matches ? "Yes" : "No"
//            );
//            log.info(categoryLog);
//            categoryDetails.append(categoryLog).append("\n");
//        }
//
//        // Assertions
//        softAssert.assertTrue(countsMatch,
//                String.format("Facet count (%d) must exactly match numFound (%d)", facetCount, numFound));
//        softAssert.assertTrue(docsMatch,
//                String.format("Docs count (%d) must exactly match numFound (%d)", docsCount, numFound));
//        softAssert.assertTrue(allProductsMatchCategory,
//                String.format("All products must be in category: %s", expectedCategory));
//
//        // Add exact match validation result
//        if (!exactMatch) {
//            log.error(String.format(
//                    "Exact Match Validation Failed:\n" +
//                            "NumFound: %d\n" +
//                            "Facet Count: %d (Match: %s)\n" +
//                            "Docs Count: %d (Match: %s)\n" +
//                            "Category Matches: %d/%d (Match: %s)",
//                    numFound,
//                    facetCount, countsMatch ? "Yes" : "No",
//                    docsCount, docsMatch ? "Yes" : "No",
//                    matchingProducts, products.size(), allProductsMatchCategory ? "Yes" : "No"
//            ));
//        }
//
//        log.info("\nCategory Filter Analysis:");
//        log.info("Expected Category: " + expectedCategory);
//        log.info("Total Products: " + products.size());
//        log.info("Matching Products: " + matchingProducts);
//
//        Allure.addAttachment("L1L3 Category Filter Results", String.format(
//                "Test Summary:\n" +
//                        "-------------\n" +
//                        "Expected Category: %s\n" +
//                        "Total Products: %d\n" +
//                        "Matching Products: %d\n\n" +
//                        "Count Validation:\n" +
//                        "----------------\n" +
//                        "Exact Match Status: %s\n" +
//                        "NumFound: %d\n" +
//                        "Facet Count: %d\n" +
//                        "Docs Count: %d\n" +
//                        "Counts Match Status: %s\n" +
//                        "Docs Match Status: %s\n\n" +
//                        "Filter Statistics:\n" +
//                        "----------------\n" +
//                        "Match Percentage: %.2f%%\n\n" +
//                        "Validation:\n" +
//                        "----------\n" +
//                        "Filter Compliance: %s\n" +
//                        "Validation Message: %s\n" +
//                        "Pagination Note: %s",
//                expectedCategory,
//                products.size(),
//                matchingProducts,
//                exactMatch ? "PASSED" : "FAILED",  // Added exactMatch status
//                numFound,
//                facetCount,
//                docsCount,
//                countsMatch ? "MATCHED" : "MISMATCH",
//                docsMatch ? "VALID" : "INVALID",
//                (matchingProducts * 100.0 / products.size()),
//                allProductsMatchCategory ? "PASSED" : "FAILED",
//                allProductsMatchCategory ?
//                        "All products match the specified category filter" :
//                        String.format("Found %d products with incorrect category", products.size() - matchingProducts),
//                docsCount < numFound ?
//                        String.format("Showing %d of %d total results (paginated)", docsCount, numFound) :
//                        "Showing all results"
//        ));
//
//        log.info("=================== L1L3 Category Filter Validation Completed ===================");
//    }
//
//    @Step("Validate size filter response")
//    public void validateSizeFilterResponse() {
////        log.info("=================== Size Filter Validation Started ===================");
////
////        String expectedSize = testData.getOtherParams().get("values");
////        log.info("Validating size filter: " + expectedSize);
////
////        // Get counts for validation
////        int numFound = ospreyApiResponse.getNumFound();
////        Map<String, List<Map<String, Object>>> facetData = ospreyApiResponse.getFacetData();
////        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
////        int docsCount = products.size();
////
////        // Get facet count and available sizes
////        int facetCount = 0;
////        StringBuilder sizesBuilder = new StringBuilder();
////        String availableSizes = "";
////
////        if (facetData != null) {
////            List<Map<String, Object>> sizeFacets = facetData.get("verticalsizegroupformat_en_string_mv");
////            if (sizeFacets != null) {
////                for (Map<String, Object> facet : sizeFacets) {
////                    String size = (String) facet.get("name");
////                    Number count = (Number) facet.get("value");
////                    if (expectedSize.equals(size) && count != null) {
////                        facetCount = count.intValue();
////                    }
////                    if (size != null && count != null) {
////                        sizesBuilder.append(size).append("(").append(count).append("), ");
////                    }
////                }
////                availableSizes = sizesBuilder.toString().replaceAll(", $", "");
////            }
////        }
//////                    availableSizes += size + "(" + count + "), ";
//////                }
//////                availableSizes = availableSizes.replaceAll(", $", "");
//////            }
//////        }
////
////        log.info("\n========== Count Validation ==========");
////        log.info(String.format("NumFound: %d", numFound));
////        log.info(String.format("Facet Count: %d", facetCount));
////        log.info(String.format("Docs Count: %d", docsCount));
////        log.info("Available Sizes with Counts: " + availableSizes);
////
////        boolean countsMatch = (numFound == facetCount);
////        boolean docsMatch = (numFound == docsCount);
////        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
////        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
////
////        // Product level validation
////        int matchingProducts = 0;
////        boolean allProductsMatchSize = true;
////
////        for (OspreyApiResponse.Doc product : products) {
////            String productName = product.getNameTextEn();
////            boolean matches = false;
////            String productSizes = "";
////
////            if (facetData != null) {
////                List<Map<String, Object>> sizeFacets = facetData.get("verticalsizegroupformat_en_string_mv");
////                if (sizeFacets != null) {
////                    productSizes = sizeFacets.stream()
////                            .map(facet -> (String) facet.get("name"))
////                            .filter(Objects::nonNull)
////                            .collect(Collectors.joining(", "));
////
////                    matches = sizeFacets.stream()
////                            .map(facet -> (String) facet.get("name"))
////                            .filter(Objects::nonNull)
////                            .anyMatch(size -> size.trim().equalsIgnoreCase(expectedSize.trim()));
////                }
////            }
////
////            if (matches) {
////                matchingProducts++;
////            } else {
////                allProductsMatchSize = false;
////            }
////
////            log.info(String.format(
////                    "Product: %s\n" +
////                            "Sizes: %s\n" +
////                            "Matches Expected Size (%s): %s\n",
////                    productName,
////                    productSizes,
////                    expectedSize,
////                    matches ? "Yes" : "No"
////            ));
////        }
////
////        // Assertions
////        softAssert.assertTrue(countsMatch,
////                String.format("Facet count (%d) must exactly match numFound (%d)", facetCount, numFound));
////        softAssert.assertTrue(docsMatch,
////                String.format("Docs count (%d) must exactly match numFound (%d)", docsCount, numFound));
////        softAssert.assertTrue(allProductsMatchSize,
////                String.format("All products must have size: %s", expectedSize));
////
////        if (!countsMatch || !docsMatch || !allProductsMatchSize) {
////            log.error(String.format(
////                    "Validation Failed:\n" +
////                            "NumFound: %d\n" +
////                            "Facet Count: %d\n" +
////                            "Docs Count: %d\n" +
////                            "Matching Products: %d\n" +
////                            "Expected Size: %s",
////                    numFound, facetCount, docsCount, matchingProducts, expectedSize));
////        }
////
////        // Allure report
////        Allure.addAttachment("Size Filter Results", String.format(
////                "Test Summary:\n" +
////                        "-------------\n" +
////                        "Expected Size: %s\n" +
////                        "Available Sizes: %s\n" +
////                        "Total Products: %d\n" +
////                        "Matching Products: %d\n\n" +
////                        "Count Validation:\n" +
////                        "----------------\n" +
////                        "NumFound: %d\n" +
////                        "Facet Count: %d\n" +
////                        "Docs Count: %d\n" +
////                        "Counts Match Status: %s\n" +
////                        "Docs Match Status: %s\n\n" +
////                        "Validation Results:\n" +
////                        "-----------------\n" +
////                        "Count Validation: %s\n" +
////                        "Size Filter Validation: %s",
////                expectedSize,
////                availableSizes,
////                products.size(),
////                matchingProducts,
////                numFound,
////                facetCount,
////                docsCount,
////                countsMatch ? "MATCHED" : "MISMATCH",
////                docsMatch ? "VALID" : "INVALID",
////                countsMatch ? "PASSED" : "FAILED",
////                allProductsMatchSize ? "PASSED" : "FAILED"
////        ));
////
////        log.info("=================== Size Filter Validation Completed ===================");
//
//        log.info("=================== Size Filter Validation Started ===================");
//
//        // Get expected size from test data
//        String expectedSize = testData.getOtherParams().get("values");
//        log.info("Validating size filter: " + expectedSize);
//
//        // Get all counts for validation
//        int numFound = ospreyApiResponse.getNumFound();
//        Map<String, List<Map<String, Object>>> facetCounts = ospreyApiResponse.getFacetData();
//        int facetCount = getFacetCountForSize(facetCounts, expectedSize);
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        int docsCount = products.size();
//        boolean allProductsMatchSize = true;
//        int matchingProducts = 0;
//
//        log.info("\n========== Count Validation ==========");
//        log.info(String.format("NumFound: %d", numFound));
//        log.info(String.format("Facet Count: %d", facetCount));
//        log.info(String.format("Docs Count: %d", docsCount));
//
//        // Validate all counts match
//        boolean countsMatch = (numFound == facetCount);
//        boolean docsMatch = (numFound == docsCount);
//        boolean exactMatch = countsMatch && docsMatch && allProductsMatchSize;
//
//        log.info(String.format("Counts Match: %s", countsMatch ? "Yes" : "No"));
//        log.info(String.format("Docs Match: %s", docsMatch ? "Yes" : "No"));
//        log.info(String.format("Exact Match: %s", exactMatch ? "Yes" : "No"));
//
//        log.info("\n========== Product Size Analysis ==========");
//        StringBuilder sizeDetails = new StringBuilder();
//
//        for (int i = 0; i < products.size(); i++) {
//            OspreyApiResponse.Doc product = products.get(i);
//            List<String> productSizes = product.getSizeEnString();
//            String productName = product.getNameTextEn();
//            String sizeList = productSizes != null ? String.join(", ", productSizes) : "";
//
//            boolean matches = productSizes != null && productSizes.contains(expectedSize);
//            if (!matches) {
//                allProductsMatchSize = false;
//            } else {
//                matchingProducts++;
//            }
//
//            String sizeLog = String.format(
//                    "Product %d:\n" +
//                            "Name: %s\n" +
//                            "Sizes: %s\n" +
//                            "Matches Filter: %s\n" +
//                            "------------------------",
//                    (i + 1),
//                    productName,
//                    sizeList,
//                    matches ? "Yes" : "No"
//            );
//            log.info(sizeLog);
//            sizeDetails.append(sizeLog).append("\n");
//        }
//
//        // Assertions
//        softAssert.assertTrue(countsMatch,
//                String.format("Facet count (%d) must exactly match numFound (%d)", facetCount, numFound));
//        softAssert.assertTrue(docsMatch,
//                String.format("Docs count (%d) must exactly match numFound (%d)", docsCount, numFound));
//        softAssert.assertTrue(allProductsMatchSize,
//                String.format("All products must have size: %s", expectedSize));
//
//        // Add exact match validation result
//        if (!exactMatch) {
//            log.error(String.format(
//                    "Exact Match Validation Failed:\n" +
//                            "NumFound: %d\n" +
//                            "Facet Count: %d (Match: %s)\n" +
//                            "Docs Count: %d (Match: %s)\n" +
//                            "Size Matches: %d/%d (Match: %s)",
//                    numFound,
//                    facetCount, countsMatch ? "Yes" : "No",
//                    docsCount, docsMatch ? "Yes" : "No",
//                    matchingProducts, products.size(), allProductsMatchSize ? "Yes" : "No"
//            ));
//        }
//
//        log.info("\nSize Filter Analysis:");
//        log.info("Expected Size: " + expectedSize);
//        log.info("Total Products: " + products.size());
//        log.info("Matching Products: " + matchingProducts);
//
//        Allure.addAttachment("Size Filter Results", String.format(
//                "Test Summary:\n" +
//                        "-------------\n" +
//                        "Expected Size: %s\n" +
//                        "Total Products: %d\n" +
//                        "Matching Products: %d\n\n" +
//                        "Count Validation:\n" +
//                        "----------------\n" +
//                        "Exact Match Status: %s\n" +
//                        "NumFound: %d\n" +
//                        "Facet Count: %d\n" +
//                        "Docs Count: %d\n" +
//                        "Counts Match Status: %s\n" +
//                        "Docs Match Status: %s\n\n" +
//                        "Filter Statistics:\n" +
//                        "----------------\n" +
//                        "Match Percentage: %.2f%%\n\n" +
//                        "Validation:\n" +
//                        "----------\n" +
//                        "Filter Compliance: %s\n" +
//                        "Validation Message: %s\n" +
//                        "Pagination Note: %s",
//                expectedSize,
//                products.size(),
//                matchingProducts,
//                exactMatch ? "PASSED" : "FAILED",
//                numFound,
//                facetCount,
//                docsCount,
//                countsMatch ? "MATCHED" : "MISMATCH",
//                docsMatch ? "VALID" : "INVALID",
//                (matchingProducts * 100.0 / products.size()),
//                allProductsMatchSize ? "PASSED" : "FAILED",
//                allProductsMatchSize ? "All products match the specified size filter" :
//                        "Some products do not match the size filter",
//                "Note: If pagination is enabled, docs count might be less than total records"
//        ));
//
//        log.info("=================== Size Filter Validation Completed ===================");
//
//    }
//
//    @Step("Validating Multiple Filters")
//    public void validateMultipleFilters() {
////        log.info("=================== Multiple Filters Validation Started ===================");
////
////        try {
////            // Get expected values from test data
////            String[] expectedGenderValues = testData.getOtherParams().get("gender_values").split(",");
////            String expectedCategoryValue = testData.getOtherParams().get("category_value");
////
////            // Get response data
////            List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
////            int numFound = ospreyApiResponse.getNumFound();
////            Map<String, List<Map<String, Object>>> facetData = ospreyApiResponse.getFacetData();
////            int docsCount = products != null ? products.size() : 0;
////        //    int docsCount = products.size();
////
////            // Get facet counts
////            int genderFacetCount = getFacetCount(facetData, "genderfilter_en_string_mv", expectedGenderValues);
////            int categoryFacetCount = getFacetCount(facetData, "l1l3nestedcategory_en_string_mv", expectedCategoryValue);
////
////            // Log counts
////            log.info("\n========== Count Validation ==========");
////            log.info(String.format("NumFound: %d", numFound));
////            log.info(String.format("Gender Facet Count: %d", genderFacetCount));
////            log.info(String.format("Category Facet Count: %d", categoryFacetCount));
////            log.info(String.format("Docs Count: %d", docsCount));
////
////            // Validate counts
////            boolean countsMatch = (numFound == genderFacetCount && numFound == categoryFacetCount);
////            boolean docsMatch = (numFound == docsCount);
////
////            // Product level validation
////            int matchingProducts = 0;
////            StringBuilder productAnalysis = new StringBuilder();
////            boolean allProductsMatch = true;
////
////            for (OspreyApiResponse.Doc product : products) {
////                boolean genderMatch = validateGenderFilter(product.getL1l2categoryEnStringMv(), expectedGenderValues);
////                boolean categoryMatch = validateCategoryFilter(product.getL1l3categoryEnStringMv(), expectedCategoryValue);
////                boolean productMatches = genderMatch && categoryMatch;
////
////                if (productMatches) {
////                    matchingProducts++;
////                } else {
////                    allProductsMatch = false;
////                }
////
////                productAnalysis.append(String.format(
////                        "\nProduct: %s\n" +
////                                "Gender: %s (Match: %s)\n" +
////                                "Category: %s (Match: %s)\n" +
////                                "Overall Match: %s\n" +
////                                "----------------\n",
////                        product.getNameTextEn(),
////                        product.getL1l2categoryEnStringMv(),
////                        genderMatch ? "✓" : "✗",
////                        product.getL1l3categoryEnStringMv(),
////                        categoryMatch ? "✓" : "✗",
////                        productMatches ? "✓" : "✗"
////                ));
////            }
////
////            // Assertions
////            softAssert.assertTrue(countsMatch,
////                    String.format("Facet counts must match numFound (%d)", numFound));
////            softAssert.assertTrue(docsMatch,
////                    String.format("Docs count (%d) must match numFound (%d)", docsCount, numFound));
////            softAssert.assertTrue(allProductsMatch,
////                    "All products must match both gender and category filters");
////
////            String validationStatus = (countsMatch && docsMatch && allProductsMatch) ?
////                    "✅ MULTIPLE FILTERS VALIDATION - PASSED" :
////                    "❌ MULTIPLE FILTERS VALIDATION - FAILED";
////
////            // Detailed Allure report
////            Allure.addAttachment("Multiple Filters Results", String.format(
////                    "Test Summary:\n" +
////                            "-------------\n" +
////                            "Expected Gender: %s\n" +
////                            "Expected Category: %s\n" +
////                            "Total Products: %d\n" +
////                            "Matching Products: %d\n\n" +
////                            "Count Validation:\n" +
////                            "----------------\n" +
////                            "NumFound: %d\n" +
////                            "Gender Facet Count: %d\n" +
////                            "Category Facet Count: %d\n" +
////                            "Docs Count: %d\n" +
////                            "Counts Match Status: %s\n" +
////                            "Docs Match Status: %s\n\n" +
////                            "Validation Results:\n" +
////                            "-----------------\n" +
////                            "Count Validation: %s\n" +
////                            "Filter Validation: %s\n\n" +
////                            "Product Analysis:\n" +
////                            "----------------\n%s",
////                    Arrays.toString(expectedGenderValues),
////                    expectedCategoryValue,
////                    products.size(),
////                    matchingProducts,
////                    numFound,
////                    genderFacetCount,
////                    categoryFacetCount,
////                    docsCount,
////                    countsMatch ? "MATCHED" : "MISMATCH",
////                    docsMatch ? "VALID" : "INVALID",
////                    countsMatch ? "PASSED" : "FAILED",
////                    allProductsMatch ? "PASSED" : "FAILED",
////                    productAnalysis.toString()
////            ));
////
////        } catch (Exception e) {
////            log.error("Multiple filters validation failed: " + e.getMessage());
////            Assert.fail("Validation failed: " + e.getMessage());
////        }
////
////        log.info("=================== Multiple Filters Validation Completed ===================");
//        log.info("=================== Multiple Filters Validation Started ===================");
//
//        try {
//            String[] expectedGenderValues = testData.getOtherParams().get("gender_values").split(",");
//            String expectedCategoryValue = testData.getOtherParams().get("category_value");
//
//            // Get response data
//            List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//            int numFound = ospreyApiResponse.getNumFound();
//            Map<String, List<Map<String, Object>>> facetData = ospreyApiResponse.getFacetData();
//            int docsCount = products != null ? products.size() : 0;
//
//            log.info("\n========== Initial Counts ==========");
//            log.info("NumFound Count: " + numFound);
//            log.info("Docs Count: " + docsCount);
//            log.info("Expected Gender Values: " + Arrays.toString(expectedGenderValues));
//            log.info("Expected Category: " + expectedCategoryValue);
//
//            // Validate products
//            int matchingProducts = 0;
//            StringBuilder validationResults = new StringBuilder();
//
//            if (products != null) {
//                for (OspreyApiResponse.Doc product : products) {
//                    boolean genderMatch = false;
//                    boolean categoryMatch = false;
//
//                    // Check gender
////                    List<String> productGenders = product.getL1l2categoryEnStringMv();
////                    if (productGenders != null) {
////                        for (String expectedGender : expectedGenderValues) {
////                            if (productGenders.contains(expectedGender.trim())) {
////                                genderMatch = true;
////                                break;
////                            }
////                        }
////                    }
//                    List<String> productGenders = product.getL1l2categoryEnStringMv();
//                    if (productGenders != null) {
//                        for (String expectedGender : expectedGenderValues) {
//                            String normalizedExpectedGender = expectedGender.trim().toLowerCase();
//                            for (String productGender : productGenders) {
//                                if (productGender != null && productGender.toLowerCase().contains(normalizedExpectedGender)) {
//                                    genderMatch = true;
//                                    log.debug("Gender match found: " + productGender + " contains " + expectedGender);
//                                    break;
//                                }
//                            }
//                            if (genderMatch) break;
//                        }
//                    }
//
//                    // Check category
//                    List<String> productCategories = product.getL1l3categoryEnStringMv();
//                    if (productCategories != null) {
//                        categoryMatch = productCategories.contains(expectedCategoryValue.trim());
//                    }
//
//                    if (genderMatch && categoryMatch) {
//                        matchingProducts++;
//                    } else {
//                        validationResults.append(String.format(
//                                "\nProduct: %s\n" +
//                                        "Gender Match: %s (Expected: %s, Found: %s)\n" +
//                                        "Category Match: %s (Expected: %s, Found: %s)\n",
//                                product.getNameTextEn(),
//                                genderMatch ? "✓" : "✗", Arrays.toString(expectedGenderValues), productGenders,
//                                categoryMatch ? "✓" : "✗", expectedCategoryValue, productCategories
//                        ));
//                    }
//                }
//            }
//
//            log.info("\n========== Validation Results ==========");
//            log.info("Total Products Found: " + docsCount);
//            log.info("Products Matching All Filters: " + matchingProducts);
//            log.info("Match Percentage: " + String.format("%.2f%%", docsCount > 0 ? (matchingProducts * 100.0 / docsCount) : 0));
//          //  log.info("Validation Status: " + (allMatch ? "PASSED" : "FAILED"));
//
//
//            boolean allMatch = (matchingProducts == docsCount);
//
//            // Assertions
//            softAssert.assertTrue(allMatch,
//                    String.format("All products should match filters. Matching: %d/%d", matchingProducts, docsCount));
//
//            // Allure report
//            String status = allMatch ? "✅ PASSED" : "❌ FAILED";
//            Allure.addAttachment("Multiple Filters Validation", String.format(
////                    "Status: %s\n\n" +
////                            "Filter Criteria:\n" +
////                            "- Gender: %s\n" +
////                            "- Category: %s\n\n" +
////                            "Results:\n" +
////                            "Total Products: %d\n" +
////                            "Matching Products: %d\n" +
////                            "Match Percentage: %.2f%%\n\n" +
////                            "Mismatched Products:\n%s",
////                    status,
////                    Arrays.toString(expectedGenderValues),
////                    expectedCategoryValue,
////                    docsCount,
////                    matchingProducts,
////                    docsCount > 0 ? (matchingProducts * 100.0 / docsCount) : 0,
////                    validationResults.toString()
//                    "Test Summary:\n" +
//                            "-------------\n" +
//                            "Status: %s\n\n" +
//                            "Response Counts:\n" +
//                            "---------------\n" +
//                            "NumFound: %d\n" +
//                            "Docs Count: %d\n\n" +
//                            "Filter Criteria:\n" +
//                            "---------------\n" +
//                            "Gender Values: %s\n" +
//                            "Category Value: %s\n\n" +
//                            "Results:\n" +
//                            "--------\n" +
//                            "Total Products: %d\n" +
//                            "Matching Products: %d\n" +
//                            "Match Percentage: %.2f%%\n\n" +
//                            "Validation Details:\n" +
//                            "-----------------\n" +
//                            "Gender Match Required: Yes\n" +
//                            "Category Match Required: Yes\n" +
//                            "Overall Match Status: %s\n\n" +
//                            "Mismatched Products:\n" +
//                            "------------------\n%s",
//                    status,
//                    numFound,
//                    docsCount,
//                    Arrays.toString(expectedGenderValues),
//                    expectedCategoryValue,
//                    docsCount,
//                    matchingProducts,
//                    docsCount > 0 ? (matchingProducts * 100.0 / docsCount) : 0,
//                    allMatch ? "All products match filters" : "Some products don't match filters",
//                    validationResults.toString()
//            ));
//
//            // Add separate Allure attachments for different aspects
//            Allure.addAttachment("Filter Configuration", String.format(
//                    "Gender Filter: %s\nCategory Filter: %s",
//                    Arrays.toString(expectedGenderValues),
//                    expectedCategoryValue
//            ));
//
//            Allure.addAttachment("Response Statistics", String.format(
//                    "NumFound: %d\nDocs Count: %d\nMatching Products: %d",
//                    numFound, docsCount, matchingProducts
//            ));
//
//        } catch (Exception e) {
//            log.error("Multiple filters validation failed: " + e.getMessage());
//            Assert.fail("Validation failed: " + e.getMessage());
//        }
//
//        log.info("=================== Multiple Filters Validation Completed ===================");
//
//    }
//
//    @Step("Validating Gender Filter Response")
//    public void validateOneFilterMultipleValues() {
//        log.info("=================== Gender Filter Validation Started ===================");
//
//        try {
//            // Get expected values and response data
//            String[] expectedGenders = testData.getOtherParams().get("gender_values").split(",");
//            List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//            int numFound = ospreyApiResponse.getNumFound();
//            int docsCount = products != null ? products.size() : 0;
//            int matchingProducts = 0;
//            StringBuilder validationResults = new StringBuilder();
//
//            // Log initial counts
//            log.info("\n========== Response Statistics ==========");
//            log.info("NumFound Count: " + numFound);
//            log.info("Docs Count: " + docsCount);
//            log.info("Expected Genders: " + Arrays.toString(expectedGenders));
//
//            // Validate each product
//            if (products != null) {
//                matchingProducts = 0; // Reset counter
//                for (OspreyApiResponse.Doc product : products) {
//                    List<String> productGenders = product.getL1l2categoryEnStringMv();
//                    String productName = product.getNameTextEn();
//                    boolean genderMatch = false;
//
////                if (productGenders != null) {
////                    for (String expectedGender : expectedGenders) {
////                        String normalizedExpectedGender = expectedGender.trim();
////                        if (productGenders.contains(normalizedExpectedGender)) {
////                            genderMatch = true;
////                            break;
////                        }
////                    }
////                }
////
////                if (genderMatch) {
////                    matchingProducts++;
////                }
//                    // Inside validateGenderFilter method
////                    if (productGenders != null) {
////                        String[] expectedGenderValues = testData.getOtherParams().get("gender_values").split(",");
////                        for (String expectedGender : expectedGenders) {
////                            String normalizedExpectedGender = expectedGender.trim().toLowerCase();
////                            for (String productGender : productGenders) {
////                                if (productGender != null &&
////                                        productGender.toLowerCase().contains(normalizedExpectedGender)) {
////                                    genderMatch = true;
////                                  //  matchingProducts++;
////                                    log.info("Match found - Product: " + productName +
////                                            ", Gender: " + productGender);
////                                    break;
////                                }
////                            }
////                            if (genderMatch) {
////                                matchingProducts++; // Increment counter only once per product
////                            }
////                        }
////                    }
//                    // if (genderMatch) break;
//                    //  }
//                    //}
//                    if (productGenders != null) {
//                        String[] expectedGenderValues = testData.getOtherParams().get("gender_values").split(",");
//
//                        log.debug("Checking product: " + productName);
//                        log.debug("Product genders: " + productGenders);
//                        log.debug("Expected genders: " + Arrays.toString(expectedGenderValues));
//
//
//                        for (String productGender : productGenders) {
//                            if (productGender != null) {
//                                String normalizedProductGender = productGender.toLowerCase().trim();
//
//                                for (String expectedGender : expectedGenderValues) {
//                                    String normalizedExpectedGender = expectedGender.trim().toLowerCase();
//                                    if (normalizedProductGender.contains(normalizedExpectedGender)) { // &&
//                                          //  !hasOtherGenders(normalizedProductGender, expectedGenderValues, normalizedExpectedGender)) {
//                                        log.debug("Found match: " + productGender + " contains " + expectedGender);
//                                        genderMatch = true;
//                                        break;
//                                    }
//                                }
//                            }
//                            if (genderMatch) break;
//                        }
//                    }
//                    if (genderMatch) {
//                        matchingProducts++;
//                        log.info("Match found for product: " + productName);
//                    }
//                        // Build validation results
//                        validationResults.append(String.format(
//                                "Product: %s\n" +
//                                        "Gender Match: %s\n" +
//                                        "Found Genders: %s\n" +
//                                        "------------------------\n",
//                                productName != null ? productName : "N/A",
//                                genderMatch ? "✓" : "✗",
//                                productGenders != null ? productGenders.toString() : "No genders"
//                        ));
//                    }
//                }
//                // Calculate match percentage
//                double matchPercentage = docsCount > 0 ? (matchingProducts * 100.0 / docsCount) : 0;
//                //  boolean allMatch = (matchingProducts == docsCount);
//                boolean allMatch = (matchingProducts == docsCount && matchingProducts == numFound);
//
//                // Log validation results
//                log.info("\n========== Validation Results ==========");
//                log.info("Total Products: " + docsCount);
//                log.info("Matching Products: " + matchingProducts);
//                log.info("Match Percentage: " + String.format("%.2f%%", matchPercentage));
//                log.info("Validation Status: " + (allMatch ? "PASSED" : "FAILED"));
//
//                // Assertions
//                softAssert.assertTrue(allMatch,
//                        String.format("Gender Filter Validation Failed. Matching Products: %d/%d", matchingProducts, docsCount));
//
//                // Create Allure report
//                Allure.addAttachment("Gender Filter Validation Results", String.format(
//                        "Test Summary:\n" +
//                                "=============\n\n" +
//                                "Response Statistics:\n" +
//                                "------------------\n" +
//                                "NumFound: %d\n" +
//                                "Docs Count: %d\n" +
//                                "Matching Products: %d\n\n" +
//                                "Filter Details:\n" +
//                                "--------------\n" +
//                                "Expected Genders: %s\n\n" +
//                                "Validation Results:\n" +
//                                "------------------\n" +
//                                "Match Percentage: %.2f%%\n" +
//                                "Status: %s\n\n" +
//                                "Product Details:\n" +
//                                "---------------\n%s",
//                        numFound,
//                        docsCount,
//                        matchingProducts,
//                        Arrays.toString(expectedGenders),
//                        matchPercentage,
//                        allMatch ? "✅ PASSED" : "❌ FAILED",
//                        validationResults.toString()
//                ));
//
//            } catch(Exception e){
//                log.error("Gender filter validation failed: " + e.getMessage());
//                Assert.fail("Gender Filter Validation Failed: " + e.getMessage());
//            }
//
//            log.info("=================== Gender Filter Validation Completed ===================");
//        }
//
//    @Step("Validate invalid filter list type error response")
//    public void validateInvalidFilterListTypeResponse(String filters,String store) {
//        String responseStr = ospreyApiResponse.asFilterString(filters,store);
//        String testDataFilter = testData.getOtherParams().get("filters");
//       // String expectedError;
////        String expectedError = "{\"detail\":[{\"type\":\"list_type\",\"loc\":["body\",\"filters\"],\"msg\":\"Input should be a valid list\",\"input\":\"\"}]}";
////
////        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
////                expectedError.replaceAll("\\s+", ""),
////                "Error message should match exactly for invalid filter list type");
////        String expectedEmptyError = "{\"detail\":[{\"type\":\"list_type\",\"loc\":["body\",\"filters\"],\"msg\":\"Input should be a valid list\",\"input\":\"\"}]}";
////        String expectedNumericError = "{\"detail\":[{\"type\":\"list_type\",\"loc\":["body\",\"filters\"],\"msg\":\"Input should be a valid list\",\"input\":" + testData.getOtherParams().get("filters") + "}]}";
//
//        // Validate based on input type from testData
////        if (testDataFilter.equals(testData.getOtherParams().get("filters"))) {
////            softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
////                    expectedEmptyError.replaceAll("\\s+", ""),
////                    "Error message should match exactly for empty filter input");
////        } else if (filters.equals(testData.getOtherParams().get("filters"))) {
////            softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
////                    expectedNumericError.replaceAll("\\s+", ""),
////                    "Error message should match exactly for numeric filter input");
////        }
//          String expectedError;
//        if (testDataFilter.matches("\\d+")) {
//            expectedError = "{\"detail\":[{\"type\":\"list_type\",\"loc\":[\"body\",\"filters\"],\"msg\":\"Input should be a valid list\",\"input\":" + testDataFilter + "}]}";
//        } else {
//            expectedError = "{\"detail\":[{\"type\":\"list_type\",\"loc\":[\"body\",\"filters\"],\"msg\":\"Input should be a valid list\",\"input\":\"\"}]}";
//        }
//
//        // Validate response matches expected error
//        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
//                expectedError.replaceAll("\\s+", ""),
//                "Error message should match exactly for filter input");
//
//        softAssert.assertTrue(responseStr.contains("Input should be a valid list"),
//                "Response should contain invalid list message");
//        softAssert.assertTrue(responseStr.contains("list_type"),
//                "Response should contain list_type type");
//        softAssert.assertTrue(responseStr.contains("body"),
//                "Response should contain body location");
//        softAssert.assertTrue(responseStr.contains("filters"),
//                "Response should contain filters location");
//
//        log.info("Invalid filter list type response: " + responseStr);
//        Allure.addAttachment("Error Response", responseStr);
//
//        softAssert.assertAll();
//    }
//
//    @Step("Validate invalid query type error response")
//    public void validateInvalidBooleanFilterTypeResponse(boolean filter,String store) {
//        // String responseStr = ospreyApiResponse.asString(query.toString(), store);
//        String responseStr = ospreyApiResponse.handleBooleanFilterResponse(filter,store);
//
//      //  String expectedError =  "{\"detail\":[{" + "\"type\":\"list_type\"," + "\"loc\":[\"body\",\"filters\"]," + "\"msg\":\"Input should be a valid list\"," + "\"input\":%s}]}",filter);
//        String expectedError = String.format("{\"detail\":[{" +
//                "\"type\":\"list_type\"," +
//                "\"loc\":[\"body\",\"filters\"]," +
//                "\"msg\":\"Input should be a valid list\"," +
//                "\"input\":%s" +
//                "}]}", filter);
//        // Determine expected error based on query type
////        if (query instanceof Boolean) {
////            expectedError = String.format("{\"detail\":[{\"type\":\"string_type\",\"loc\":["body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":%s}]}", query);
////        } else {
////            expectedError = String.format("{\"detail\":[{\"type\":\"string_type\",\"loc\":["body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":%d}]}", query);
////        }
//
//        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
//                expectedError.replaceAll("\\s+", ""),
//                "Error message should match exactly for invalid query type");
//
//        // Common validations
//        softAssert.assertTrue(responseStr.contains("Input should be a valid list"),
//                "Response should contain invalid list message");
//        softAssert.assertTrue(responseStr.contains("list_type"),
//                "Response should contain list_type type");
//        softAssert.assertTrue(responseStr.contains("body"),
//                "Response should contain body location");
//        softAssert.assertTrue(responseStr.contains("filters"),
//                "Response should contain filters location");
//
//        log.info("Invalid filter list type response: " + responseStr);
//        Allure.addAttachment("Error Response", responseStr);
//
//        softAssert.assertAll();
//    }
//
//
//
//    private boolean hasOtherGenders(String productGender, String[] allGenders, String currentGender) {
//        for (String gender : allGenders) {
//            String normalizedGender = gender.trim().toLowerCase();
//            if (!normalizedGender.equals(currentGender) && productGender.contains(normalizedGender)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private int getFacetCount(Map<String, List<Map<String, Object>>> facetData, String facetField, String... expectedValues) {
//        if (facetData == null || !facetData.containsKey(facetField)) return 0;
//
//        List<Map<String, Object>> facets = facetData.get(facetField);
//        int totalCount = 0;
//
//        for (Map<String, Object> facet : facets) {
//            String name = (String) facet.get("name");
//            Number count = (Number) facet.get("value");
//
//            if (count != null && Arrays.asList(expectedValues).contains(name)) {
//                totalCount += count.intValue();
//            }
//        }
//        return totalCount;
//    }
//
//    private boolean validateGenderFilter(List<String> actualGenders, String[] expectedGenders) {
//        if (actualGenders == null || actualGenders.isEmpty()) return false;
//        for (String expectedGender : expectedGenders) {
//            if (actualGenders.contains(expectedGender)) return true;
//        }
//        return false;
//    }
//
//    private boolean validateCategoryFilter(List<String> actualCategories, String expectedCategory) {
//        return actualCategories != null && !actualCategories.isEmpty() &&
//                actualCategories.contains(expectedCategory);
//    }
//
//
//
//    private int getFacetCountForSize(Map<String, List<Map<String, Object>>> facetData, String size) {
//        if (facetData == null || size == null) {
//            return 0;
//        }
//
//        try {
//            // Debug logging
//            log.debug("Looking for size: " + size);
//            log.debug("Facet Data: " + facetData);
//
//            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
//            if (facetFields != null) {
//                for (Map<String, Object> field : facetFields) {
//                    if ("verticalsizegroupformat_en_string_mv".equals(field.get("name"))) {
//                        @SuppressWarnings("unchecked")
//                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
//                        if (values != null) {
//                            for (Map<String, Object> value : values) {
//                                String facetValue = (String) value.get("name");
//                                if (size.equals(facetValue)) {
//                                    return ((Number) value.get("value")).intValue();
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            log.debug("No matching size facet found");
//            return ospreyApiResponse.numFound; // Fallback to numFound if facet not found
//
//        } catch (Exception e) {
//            log.error("Error extracting size facet count: " + e.getMessage());
//            log.error("Facet Data structure: " + facetData);
//            return ospreyApiResponse.numFound; // Fallback to numFound on error
//        }
//    }
//
//    private int getFacetCountForL1L3Category(Map<String, List<Map<String, Object>>> facetData, String category) {
//        if (facetData == null || category == null) {
//            return 0;
//        }
//
//        try {
//            // Debug logging
//            log.debug("Looking for category: " + category);
//            log.debug("Facet Data: " + facetData);
//
//            // Try to get l1l3nestedcategory facets directly
//            List<Map<String, Object>> l1l3Facets = facetData.get("l1l3nestedcategory_en_string_mv");
//            if (l1l3Facets != null) {
//                for (Map<String, Object> facet : l1l3Facets) {
//                    String name = (String) facet.get("name");
//                    if (category.equals(name)) {
//                        Object value = facet.get("value");
//                        if (value instanceof Number) {
//                            return ((Number) value).intValue();
//                        }
//                    }
//                }
//            }
//
//            // Alternative structure check
//            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
//            if (facetFields != null) {
//                for (Map<String, Object> field : facetFields) {
//                    if ("l1l3nestedcategory_en_string_mv".equals(field.get("name"))) {
//                        @SuppressWarnings("unchecked")
//                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
//                        if (values != null) {
//                            for (Map<String, Object> value : values) {
//                                String facetValue = (String) value.get("name");
//                                if (category.equals(facetValue)) {
//                                    return ((Number) value.get("value")).intValue();
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // If no match found, return numFound as fallback
//            return ospreyApiResponse.getNumFound();
//
//        } catch (Exception e) {
//            log.error("Error extracting L1L3 category facet count: " + e.getMessage());
//            log.error("Facet Data structure: " + facetData);
//            return ospreyApiResponse.getNumFound(); // Fallback to numFound on error
//        }
//    }
//
//    private int getFacetCountForGender(Map<String, List<Map<String, Object>>> facetData, String expectedgender) {
//        if (facetData == null || expectedgender == null) {
//            return 0;
//        }
//        try{
//            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
//            if (facetFields != null) {
//                // Find the l1l2category_en_string_mv facet
//                for (Map<String, Object> field : facetFields) {
//                    String fieldName = (String) field.get("name");
//                    if ("l1l2category_en_string_mv".equals(fieldName)) {
//                        // Get the values list
//                        @SuppressWarnings("unchecked")
//                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
//                        if (values != null) {
//                            // Find the matching gender count
//                            for (Map<String, Object> value : values) {
//                                String facetValue = (String) value.get("name");
//                                if (expectedgender.equals(facetValue)) {
//                                    return ((Number) value.get("value")).intValue();
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            log.debug("Facet structure: " + facetData);
//            log.debug("Expected gender: " + expectedgender);
//
//        } catch (Exception e) {
//            log.error("Error extracting facet count: " + e.getMessage());
//        }
//
//        return ospreyApiResponse.getNumFound();
//    }
//
//
//    private int getFacetCountForDiscount(Map<String, List<Map<String, Object>>> facetData, String discountRange, double minDiscount, double maxDiscount)  {
////        if (facetData == null || discountRange == null) {
////            return 0;
////        }
////
////        try {
////            List<Map<String, Object>> facetFields = facetData.get("discount_string");
////            if (facetFields != null) {
////                for (Map<String, Object> facet : facetFields) {
////                    String name = (String) facet.get("name");
////                    if (discountRange.equals(name)) {
////                        Object value = facet.get("value");
////                        if (value instanceof Number) {
////                            return ((Number) value).intValue();
////                        }
////                    }
////                }
////            }
////
////            log.debug("Facet Data structure: " + facetData);
////            log.debug("Looking for discount range: " + discountRange);
////
////        } catch (Exception e) {
////            log.error("Error extracting facet count: " + e.getMessage());
////        }
////
////        return 0;
//
//            if (facetData == null) {
//                return ospreyApiResponse.numFound; // Return numFound if facet data is not available
//            }
//
//            List<Map<String, Object>> discountFacets = facetData.get("trade_discounted_value_double");
//            if (discountFacets == null || discountFacets.isEmpty()) {
//                return ospreyApiResponse.numFound; // Return numFound if discount facets are not available
//            }
//
//            int totalCount = 0;
//            for (OspreyApiResponse.Doc product : ospreyApiResponse.getDocs()) {
//                double productDiscount = product.getTradeDiscountedValueDouble();
//                if (productDiscount >= minDiscount && productDiscount <= maxDiscount) {
//                    totalCount++;
//                }
//            }
//
//            return totalCount;
//    }
//
//    private int getFacetCountForPriceRange(Map<String, List<Map<String, Object>>> facetData, String priceRange) {
//        if (facetData == null || priceRange == null) {
//            return 0;
//        }
//
//        try {
//            // Debug the input
//            log.debug("Price Range to find: " + priceRange);
//            log.debug("Facet Data: " + facetData);
//
//            List<Map<String, Object>> facetFields = facetData.get("facet_fields");
//            if (facetFields != null) {
//                // Find price range facets
//                for (Map<String, Object> field : facetFields) {
//                    if ("pricerange_inr_string".equals(field.get("name"))) {
//                        @SuppressWarnings("unchecked")
//                        List<Map<String, Object>> values = (List<Map<String, Object>>) field.get("values");
//                        if (values != null) {
//                            // Format the price range to match facet format
//                            String formattedRange = priceRange.replace("|", " TO ");
//
//                            // Find matching facet
//                            for (Map<String, Object> value : values) {
//                                String facetValue = (String) value.get("name");
//                                if (formattedRange.equals(facetValue)) {
//                                    Number count = (Number) value.get("count");
//                                    return count != null ? count.intValue() : 0;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            // If no match found, try alternative facet structure
//            List<Map<String, Object>> priceFacets = facetData.get("pricerange_inr_string");
//            if (priceFacets != null) {
//                String formattedRange = priceRange.replace("|", " TO ");
//                for (Map<String, Object> facet : priceFacets) {
//                    if (formattedRange.equals(facet.get("name"))) {
//                        Number count = (Number) facet.get("count");
//                        return count != null ? count.intValue() : 0;
//                    }
//                }
//            }
//
//            log.warn("No matching price range facet found for: " + priceRange);
//
//        } catch (Exception e) {
//            log.error("Error extracting price range facet count: " + e.getMessage(), e);
//            log.error("Facet Data: " + facetData);
//        }
//
//        // If no facet count found, return numFound as fallback
//        return ospreyApiResponse.getNumFound();
//
////            log.debug("Facet Data structure: " + facetData);
////            log.debug("Looking for price range: " + priceRange);
////
////        } catch (Exception e) {
////            log.error("Error extracting facet count: " + e.getMessage());
////        }
////
////        return 0;
//    }
//
//    private static class DiscountStats {
//        private double minDiscount = Double.MAX_VALUE;
//        private double maxDiscount = Double.MIN_VALUE;
//        private double totalDiscount = 0;
//        private int count = 0;
//
//        public void addDiscount(double discount) {
//            if (discount > 0) {
//                minDiscount = Math.min(minDiscount, discount);
//                maxDiscount = Math.max(maxDiscount, discount);
//                totalDiscount += discount;
//                count++;
//            }
//        }
//
//        public double getMinDiscount() {
//            return minDiscount == Double.MAX_VALUE ? 0 : minDiscount;
//        }
//
//        public double getMaxDiscount() {
//            return maxDiscount == Double.MIN_VALUE ? 0 : maxDiscount;
//        }
//
//        public double getAverageDiscount() {
//            return count > 0 ? totalDiscount / count : 0;
//        }
//    }
//
//    private DiscountStats calculateDiscountStats(List<OspreyApiResponse.Doc> products) {
//        DiscountStats stats = new DiscountStats();
//        for (OspreyApiResponse.Doc product : products) {
//            double discount = product.getTradeDiscountedValueDouble();//extractDiscountPercentage(String.valueOf(product.getTradeDiscountedValueDouble()));
//            stats.addDiscount(discount);
//        }
//        return stats;
//    }
//
//}
////    @Step("Validate price sorting response")
////    public void validatePriceSortingResponse() {
////        log.info("=================== Price Sorting Validation Started ===================");
////
////        // Get sorting parameters
////        String sortField = testData.getOtherParams().get("sort_field");
////        String sortOrder = testData.getOtherParams().get("sort_order");
////        log.info("Validating price sorting - Field: " + sortField + ", Order: " + sortOrder);
////
////        // Get products and validate sorting
////        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
////        boolean isSorted = true;
////
////        for (int i = 0; i < products.size() - 1; i++) {
////            double currentPrice = products.get(i).getPriceInrDouble();
////            double nextPrice = products.get(i + 1).getPriceInrDouble();
////
////            log.info(String.format("Product %d - Price INR: %.2f", i + 1, currentPrice));
////
////            if (sortOrder.equalsIgnoreCase("asc")) {
////                if (currentPrice > nextPrice) {
////                    isSorted = false;
////                    //  log.error("Price sorting error at index " + i + ": " + currentPrice + " > " + nextPrice);
////                    log.error(String.format("Price sorting error at index %d: %.2f > %.2f",
////                            i, currentPrice, nextPrice));
////                    break;
////                }
////            } else {
////                if (currentPrice < nextPrice) {
////                    isSorted = false;
////                    log.error("Price sorting error at index " + i + ": " + currentPrice + " < " + nextPrice);
////                    break;
////                }
////            }
////        }
////
////        // Log last product price
////        if (!products.isEmpty()) {
////            log.info(String.format("Product %d - Price INR: %.2f",
////                    products.size(), products.get(products.size() - 1).getPriceInrDouble()));
////        }
////
////        // Validation
////        softAssert.assertTrue(isSorted, "Products should be sorted by price in " + sortOrder + " order");
////
////        // Log price analysis
////        log.info("\nPrice Analysis:");
////        log.info("Total products analyzed: " + products.size());
////        log.info(String.format("Price range (INR): %.2f to %.2f",
////                products.get(0).getPriceInrDouble(),
////                products.get(products.size() - 1).getPriceInrDouble()));
////        //   log.info("Price range: " + products.get(0).getPriceInrDouble() + " to " +
////        //         products.get(products.size()-1).getPriceInrDouble());
////
////        Allure.addAttachment("Price Sorting Results", String.format(
////                "Test Summary:\n" +
////                        "-------------\n" +
////                        "Sort Field: %s\n" +
////                        "Sort Order: %s\n" +
////                        "Total Products: %d\n\n" +
////                        "Price Range (INR):\n" +
////                        "------------\n" +
////                        "First Product: %.2f\n" +
////                        "Last Product: %.2f\n\n" +
////                        "Price Statistics:\n" +
////                        "---------------\n" +
////                        "Minimum Price: %.2f\n" +
////                        "Maximum Price: %.2f\n\n" +
////                        "Validation:\n" +
////                        "----------\n" +
////                        "Sorting Status: %s",
////                sortField,
////                sortOrder,
////                products.size(),
////                products.get(0).getPriceInrDouble(),
////                products.get(products.size() - 1).getPriceInrDouble(),
////                getMinPrice(products),
////                getMaxPrice(products),
////                isSorted ? "PASSED" : "FAILED"
////        ));
////
////        log.info("Price Sorting Validation Completed ");
////    }
////
////    private double getMinPrice(List<OspreyApiResponse.Doc> products) {
////        return products.stream()
////                .mapToDouble(OspreyApiResponse.Doc::getPriceInrDouble)
////                .min()
////                .orElse(0.0);
////    }
////
////    private double getMaxPrice(List<OspreyApiResponse.Doc> products) {
////        return products.stream()
////                .mapToDouble(OspreyApiResponse.Doc::getPriceInrDouble)
////                .max()
////                .orElse(0.0);
////    }
////}
//
////    @Step("Validating query results")
////    public void validateQueryResults() {
////        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
////        if (products != null) {
////            // Validate result count
////            //  Assertion softAssert = null;
////            log.info("Total products found: " + products.size());
////            Allure.addAttachment("Total Products", String.valueOf(products.size()));
////            softAssert.assertTrue(products.size() <= 1000,
////                    "Results should not exceed maximum records_per_page");
////
////            // Validate each product
////            log.info("Starting product relevance validation");
////            for (OspreyApiResponse.Doc product : products) {
////                validateProductRelevance(product);
////            }
////        }
////    }
//
////    @Step("Validating product relevance: {product.getCodeString()}")
////    private void validateProductRelevance(OspreyApiResponse.Doc product) {
////        boolean isRelevant = false;
////        String searchQuery = "Shirt"; // Get from test data
////        log.info("Validating product: " + product.getCodeString());
////
////        // Check product name
////        if (product.getNameTextEn() != null) {
////            isRelevant = product.getNameTextEn().toLowerCase()
////                    .contains(searchQuery.toLowerCase());
////            log.info("Product name: " + product.getNameTextEn());
////        }
////
////        // Check categories
////        if (!isRelevant && product.getL1l3categoryEnStringMv() != null) {
////            isRelevant = product.getL1l3categoryEnStringMv().stream()
////                    .anyMatch(category -> category.toLowerCase()
////                            .contains(searchQuery.toLowerCase()));
////            log.info("Product categories: " + String.join(", ", product.getL1l3categoryEnStringMv()));
////        }
////
////        Allure.addAttachment("Product Details",
////                String.format("Code: %s, Name: %s, Relevant: %s",
////                        product.getCodeString(),
////                        product.getNameTextEn(),
////                        isRelevant));
////        softAssert.assertTrue(isRelevant,
////                "Product " + product.getCodeString() + " should be relevant to search query");
////    }
////
////    @Step("Validating product details")
////    public void validateProductDetails() {
////        if (ospreyApiResponse.getDocs() != null) {
////            log.info("Starting product details validation");
////            for (OspreyApiResponse.Doc product : ospreyApiResponse.getDocs()) {
////                log.info("Validating product: " + product.getCodeString());
////                Allure.addAttachment("Product Info",
////                        String.format("Code: %s, Name: %s, Price: %s, CatalogId: %s",
////                                product.getCodeString(),
////                                product.getNameTextEn(),
////                                product.getPriceInrDouble(),
////                                product.getCatalogId()));
////
////                softAssert.assertNotNull(product.getCodeString(), "Product code should not be null");
////                softAssert.assertNotNull(product.getNameTextEn(), "Product name should not be null");
////                softAssert.assertNotNull(product.getPriceInrDouble(), "Price should not be null");
////                softAssert.assertNotNull(product.getCatalogId(), "Catalog ID should not be null");
////
////                // Validate store
////                softAssert.assertEquals(product.getCatalogId(), "rilfnlProductCatalog",
////                        "Product should belong to correct store");
////            }
////            log.info("Product details validation completed");
////        }
////    }
////
////    @Step("Validate error message for empty search query")
////    public void validateEmptySearchQueryError(OspreyApiResponse response) {
////        String responseBody = response.asString("","store");
////        String expectedErrorMessage = "{\"detail\": \"Search query cannot be empty or invalid.\"}";
////
////        // Validate exact error message
////        softAssert.assertEquals(responseBody.replaceAll("\\s+", ""),
////                expectedErrorMessage.replaceAll("\\s+", ""),
////                "Error message should match exactly");
////
////        // Validate error message content
////        softAssert.assertTrue(responseBody.contains("Search query cannot be empty or invalid."),
////                "Response should contain the error message");
////
////        log.info("Response received: " + responseBody);
////        Allure.addAttachment("Error Response", responseBody);
////
////        softAssert.assertAll();
////    }
////
////    @Step("Validate invalid store error response")
////    public void validateInvalidStoreResponse(String query,String store) {
////        String responseBody = ospreyApiResponse.asString(query,store);
////        String expectedError = "{\"detail\": \"Invalid store\"}";
////
////        softAssert.assertEquals(responseBody.replaceAll("\\s+", ""),
////                expectedError.replaceAll("\\s+", ""),
////                "Error message should match exactly for invalid store");
////
////        // Validate error message
////        softAssert.assertTrue(responseBody.contains("Invalid store"),
////                "Response should contain invalid store error message");
////
////        log.info("Response: " + responseBody);
////        Allure.addAttachment("Invalid Store Error Response", responseBody);
////
////        softAssert.assertAll();
////    }
////
////    @Step("Validate missing field error response")
////    public void validateMissingFieldResponse(String query,String store) {
////        String responseStr = ospreyApiResponse.asString(query,store);
////        String expectedError = "{\"detail\":[{\"type\":\"missing\",\"loc\":["body\",\"store\"],\"msg\":\"Field required\",\"input\":{\"query\":\"Jeans\",\"sort_field\":\"relevance\",\"records_per_page\":2}}]}";
////
////        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
////                expectedError.replaceAll("\\s+", ""),
////                "Error message should match exactly for missing field");
////
////        softAssert.assertTrue(responseStr.contains("Field required"),
////                "Response should contain field required message");
////        softAssert.assertTrue(responseStr.contains("missing"),
////                "Response should contain missing type");
////        softAssert.assertTrue(responseStr.contains("body"),
////                "Response should contain body location");
////        softAssert.assertTrue(responseStr.contains("query"),
////                "Response should contain query location");
////
////        log.info("Missing field response: " + responseStr);
////        Allure.addAttachment("Error Response", responseStr);
////
////        softAssert.assertAll();
////    }
////
////
////    @Description("Validate API response against query and generate pie charts")
////    public void validateSearchqueryResults(String query, SoftAssert softAssert, String queries) throws IOException {
////
////        int matchedCount = 0;
////        int unmatchedCount = 0;
////
////        if (ospreyApiResponse == null || ospreyApiResponse.getDocs() == null || ospreyApiResponse.getDocs().isEmpty()) {
////            //  softAssert.assertTrue(false, "Failed! API response has null or empty docs.");
////            Allure.step("No docs available for query: " + query + ". Skipping this query.");
////            return;
////        }
////        unmatchedCount = ospreyApiResponse.getDocs().size(); // Total docs start as unmatched
////        Allure.step("Docs populated with size: " + ospreyApiResponse.getDocs().size());
////
////        // Preprocess the query to split into individual words
////        String[] queryWords = query.toLowerCase().split("\\s+");
////
////        //Adding the steps here
////        //    List<OspreyApiResponse.Doc> unmatchedDocs = new ArrayList<>();
////        List<String> matchedKeywords = new ArrayList<>();
////        List<String> unmatchedKeywords = new ArrayList<>();
////
////        // Step 1: Verify the first document's brand name
////        OspreyApiResponse.Doc firstDoc = ospreyApiResponse.getDocs().get(0);
////        String brandName = firstDoc.getBrandNameTextEnMv().toString(); // Assuming brand name is stored in getBrandNameTextEnMv
////
////        if (brandName != null) { // && brandName.toLowerCase().contains(query.toLowerCase())) {
////            for (String word : queryWords) {
////                if (brandName.toLowerCase().contains(word)) {
////                    Allure.step("Validation successful for brand name in the first document: " + brandName);
////                    return; // Exit if the first document matches the brand name
////                } else {
////                    Allure.step("First document's brand name does not match. Proceeding to product name validation for all docs.");
////                }
////            }
////            // Step 2: Verify Product Name for all documents
////            // int productNameMatchCount = 0;
////            //  int unmatchedCount = 0;
////            //  List<OspreyApiResponse.Doc> matchedDocs = new ArrayList<>();
////            List<OspreyApiResponse.Doc> unmatchedDocs = new ArrayList<>();
////
////            for (OspreyApiResponse.Doc doc : ospreyApiResponse.getDocs()) {
////                String productName = doc.getNameTextEn();
////                boolean isMatched = false;
////                if (productName != null) {//&& productName.toLowerCase().contains(query.toLowerCase())) {
////                    for (String word : queryWords) {
////                        if (productName.toLowerCase().contains(word)) {
////                            matchedCount++;
////                            //Adding step here
////                            //     matchedKeywords.add(productName);
////                            System.out.println("Checking Product Name in Response: " + productName);
////                            unmatchedCount--;
////                            System.out.println("Checking Product Name in Response: " + productName);
////                            isMatched = true;
////                            break;
////                        }
////                    }
////                }
////                if (!isMatched) {
////                    //  unmatchedCount++;
////                    unmatchedDocs.add(doc);
////                    // Added the step here
////                    //    unmatchedKeywords.add(productName != null ? productName : "N/A");
////                    System.out.println("Checking Product Name in Response: " + productName);
////                }
////            }
////            // matchingProductNames.add(productName);
////
////            //  System.out.println("Matched product names:" + matchedDocs);
////            System.out.println("Matched product name count: " + matchedCount);
////            System.out.println("Unmatched product count after product name validation: " + unmatchedCount);
////
////            //   int l2CategoryMatchCount = 0;
////            List<OspreyApiResponse.Doc> unmatchedL2Docs = new ArrayList<>();
////
////            for (OspreyApiResponse.Doc doc : unmatchedDocs) {
////                String l2Category = doc.getL1l2categoryEnStringMv().toString();
////                boolean isMatched = false;
////                if (l2Category != null) { // && l2Category.toLowerCase().contains(query.toLowerCase())) {
////                    for (String word : queryWords) {
////                        if (l2Category.toLowerCase().contains(word)) {
////                            matchedCount++;
////                            // Added the step here
////                            matchedKeywords.add(l2Category);
////                            unmatchedCount--;
////                            isMatched = true;
////                            break;
////                        }
////                    }
////                }
////                if (!isMatched) {
////                    unmatchedL2Docs.add(doc);
////                    //Addded the step here
////                    unmatchedKeywords.add(l2Category != null ? l2Category : "N/A");
////                }
////            }
////
////            System.out.println("Matched L2 category count: " + matchedCount);
////            System.out.println("Unmatched product count after L2 category validation: " + unmatchedCount);
////
////            //   int l3CategoryMatchCount = 0;
////            List<OspreyApiResponse.Doc> unmatchedL3Docs = new ArrayList<>();
////
////            for (OspreyApiResponse.Doc doc : unmatchedL2Docs) {
////                String l3Category = doc.getL1l3categoryEnStringMv().toString();
////                boolean isMatched = false;
////                if (l3Category != null) { // && l3Category.toLowerCase().contains(query.toLowerCase())) {
////                    for (String word : queryWords) {
////                        if (l3Category.toLowerCase().contains(word)) {
////                            matchedCount++;
////                            matchedKeywords.add(l3Category);
////                            unmatchedCount--;
////                            isMatched = true;
////                            break;
////                        }
////                    }
////                }
////                if (!isMatched) {
////                    // Added the step here
////                    //   unmatchedCount++;
////                    unmatchedL3Docs.add(doc);
////                    //Added the step here
////                    //   unmatchedKeywords.add(l3Category != null ? l3Category : "N/A");
////                }
////            }
////
////            System.out.println("Matched L3 category count: " + matchedCount);
////            System.out.println("Unmatched product count after L3 category validation: " + unmatchedCount);
////
////            // Final assertion for unmatched products
////            if (!(unmatchedCount >= 0)) {
////                softAssert.assertTrue(true, "Passed! Unmatched products remain after all validations. Count: " + unmatchedCount);
////            } else {
////                Allure.step("All products validated successfully across brand name, product name, L2, and L3 categories.");
////            }
////
////            //Added the steps here
////
////            // Generate Allure Overview
////            int totalKeywords = matchedCount + unmatchedCount;
////
////            // Summary as Allure Step
////            String summary = String.format(
////                    "Total Keywords: %d\nMatched Keywords: %d\nUnmatched Keywords: %d",
////                    totalKeywords, matchedCount, unmatchedCount
////            );
////            Allure.step(summary);
////
////            // Attach Full List of Keywords to Allure
////            String keywordsList = "Matched Keywords:\n" + String.join(", ", matchedKeywords) +
////                    "\n\nUnmatched Keywords:\n" + String.join(", ", unmatchedKeywords);
////
////            Path keywordsFile = Paths.get("target/keywords_overview.txt");
////            Files.write(keywordsFile, keywordsList.getBytes());
////
////            Allure.addAttachment("Keywords Overview", new FileInputStream(keywordsFile.toFile()));
////
////
////            // Generate a pie chart for the final results
////            int MatchedCount = matchedCount;
////            int UnMatchedCount = unmatchedCount;
////            //  List<String> keywords = Collections.singletonList("");
////
////            createDoughnutChart(MatchedCount, UnMatchedCount, query);
////            String doughnutChartFile = "target/pie_chart.png";
////            attachPieChart(doughnutChartFile);
////            //  generatePieChart("Keyword Validation Results for: " + query, matchedCount, unmatchedCount);
////
////        }
////    }
////}
//
//
////      if(ospreyApiResponse.getFacetData().getBrandStringMv().contains(query)) {
////        System.out.println(query);
//
////               for( OspreyApiResponse.Doc doc: ospreyApiResponse.getDocs()) {
////                   if(o.spreyApiResponse.getDocs().get(0).getBrandNameTextEnMv().equals(query)) {
////
////               }
////
////    //        if(ospreyApiResponse.getDocs().get(0).getBrandNameTextEnMv().equals(query)) {
////
////      //     }
////            if (ospreyApiResponse.getDocs() == null || ospreyApiResponse.getDocs().isEmpty()) {
////                softAssert.assertTrue(false, "Failed! API response has null or empty docs.");
////                return;
////            } else {
////                log.info("Docs populated with size: " + ospreyApiResponse.getDocs().size());
////            }
////            //   System.out.println("Docs populated with size: " + ospreyApiResponse.getDocs().size());
////            //   }
////
////            boolean productNameMatch = false;
////            boolean l2CategoryMatch = false;
////            boolean l3CategoryMatch = false;
////
////            // Iterate through all documents to validate product name
////            for (OspreyApiResponse.Doc doc : ospreyApiResponse.getDocs()) {
////           //     for(OspreyApiResponse.Doc doc1 : ospreyApiResponse.getDocs())
////                // Validate Product Name
////                String productName = doc.getNameTextEn();
////                if (productName != null && productName.equalsIgnoreCase(query)) {
////                    productNameMatch = true;
////                }
////            }
////
////            if (productNameMatch) {
////                System.out.println("Validation successful for product name.");
////            } else {
////                System.out.println("No product name matches the query. Proceeding to L2 category validation.");
////            }
////
////            // Iterate through all documents to validate L2 category
////            for (OspreyApiResponse.Doc doc : ospreyApiResponse.getDocs()) {
////                String l2Category = doc.getL2Category();
////                if (l2Category != null && l2Category.equalsIgnoreCase(query)) {
////                    l2CategoryMatch = true;
////                }
////            }
////
////            if (l2CategoryMatch) {
////                System.out.println("Validation successful for L2 category.");
////            } else {
////                System.out.println("No L2 category matches the query. Proceeding to L3 category validation.");
////            }
////
////            // Iterate through all documents to validate L3 category
////            for (OspreyApiResponse.Doc doc : ospreyApiResponse.getDocs()) {
////                String l3Category = doc.getL3Category();
////                if (l3Category != null && l3Category.equalsIgnoreCase(query)) {
////                    l3CategoryMatch = true;
////                }
////            }
////
////            if (l3CategoryMatch) {
////                System.out.println("Validation successful for L3 category.");
////            } else {
////                softAssert.assertTrue(false, "Failed! No L3 category matches the query in all docs. Expected: " + query);
////            }
////
////            // Final assertions to ensure validation has occurred
////            if (!productNameMatch && !l2CategoryMatch && !l3CategoryMatch) {
////                softAssert.assertTrue(false, "Failed! No match found for query in product name, L2 category, or L3 category.");
////            }
////        }
////        }
////            //   public void validateSearchQueryResult(String query, String typeOfQuery) {
////            // Check if the query is present in the search results
////            boolean queryFound = false;
////            // Access the first product in the docs array
////            //  boolean product = ospreyApiResponse.getGetdocs().get(0);
////            //     ospreyApiResponse.getDocs().get(0).getBrandNameTextEnMv().contains(query);
////            if (ospreyApiResponse.getDocs() == null || ospreyApiResponse.getDocs().isEmpty()) {
////                softAssert.assertTrue(false, "Failed! Got null or empty docs");
////                return; // Exit if no docs found
////            }
////
////            // Extract first doc for convenience
////            //     Doc firstDoc = ospreyApiResponse.getDocs().get(0);
////            OspreyApiResponse.Doc firstdoc  = ospreyApiResponse.getDocs().get(0);
////
////            List<String> brandNames = firstdoc.getBrandNameTextEnMv();
////            // First verify with brand name
////            if (brandNames == null || brandNames.isEmpty()) {
////                softAssert.assertTrue(false, "Failed! Brand name list is null or empty");
////            } else {
////                // Check if any brand name matches the query
////                boolean matchFound = brandNames.stream()
////                        .anyMatch(brandName -> brandName.equalsIgnoreCase(query));
////
////                if (!matchFound) {
////                    softAssert.assertTrue(false, "Failed! No brand name matches the query. Expected: " + query + ", Found: " + brandNames);
////                } else {
////                    System.out.println("Brand name matches the query.");
////                }
////            }
////            softAssert.assertTrue(false, "Failed! Brand name mismatch. Expected: " + searchKeyword + ", Found: " + firstDoc.getBrandName());
////            // If brand doesn't match, move to product name
////        } else {
////            // Brand matches, proceed with product name check
////            if (firstdoc.getBrandNameTextEnMv() == null || firstdoc.getName().isEmpty()) {
////                softAssert.assertTrue(false, "Failed! Got null or empty product name");
////            } else {
////                // If product name matches, proceed to check l2 category
////                if (firstDoc.getL2Category() == null || !firstDoc.getL2Category().equalsIgnoreCase(expectedL2Category)) {
////                    softAssert.assertTrue(false, "Failed! L2 category mismatch. Expected: " + expectedL2Category + ", Found: " + firstDoc.getL2Category());
////                } else {
////                    // If L2 category matches, proceed to check l3 category
////                    if (firstDoc.getL3Category() == null || !firstDoc.getL3Category().equalsIgnoreCase(expectedL3Category)) {
////                        softAssert.assertTrue(false, "Failed! L3 category mismatch. Expected: " + expectedL3Category + ", Found: " + firstDoc.getL3Category());
////                    } else {
////                        // All categories match, proceed with price validation
////                        if (firstDoc.getPriceInfo() == null || firstDoc.getPriceInfo().getPrice() == null) {
////                            softAssert.assertTrue(false, "Failed! No price in the response");
////                        }
////                    }
////                }
////            }
////        }
////
////        // Finally, validate results count if all checks pass
////        validateResultsCount();
////    }
//// Based on the type of query, check the appropriate field in the response
//// Assert softAssert = true;
////        switch (typeOfQuery.toLowerCase()) {
////        case "brand":
////            // Check if the brand name matches the query (assuming brand names are in "brandName_text_en_mv")
////            List<String> brandNames =  product.get("brandName_text_en_mv");
////            for (String brandName : brandNames) {
////                if (brandName.contains(query)) {
////                    queryFound = true;
////                    break;
////                }
////            }
////            break;
////        case "product":
////            // Check if the product name matches the query (assuming product name is in "name_text_en")
////            String productName = (String) product.get("name_text_en");
////            if (productName != null && productName.contains(query)) {
////                queryFound = true;
////            }
////            break;
////        case "l2 category":
////            // Check if the L2 category matches the query (assuming L2 category IDs are in "l2category_string_mv")
////            List<String> l2Categories = (List<String>) product.get("l2category_string_mv");
////            for (String category : l2Categories) {
////                if (category.contains(query)) {
////                    queryFound = true;
////                    break;
////                }
////            }
////            break;
////        case "l3 category":
////            // Check if the L3 category matches the query (assuming L3 category IDs are in "l3category_string_mv")
////            List<String> l3Categories = (List<String>) product.get("l3category_string_mv");
////            for (String category : l3Categories) {
////                if (category.contains(query)) {
////                    queryFound = true;
////                    break;
////                }
////            }
////            break;
////        default:
////            softAssert.fail(typeOfQuery + " query type is not recognized.");
////            return;
////    }            // Assert based on whether the query was found
////               if (queryFound) {
////        softAssert.assertTrue(true, typeOfQuery + " query is working fine.");
////    } else {
////        softAssert.assertTrue(false, typeOfQuery + " misspelled query is not giving relevant details.");
////    }



//
//
//
//
//
