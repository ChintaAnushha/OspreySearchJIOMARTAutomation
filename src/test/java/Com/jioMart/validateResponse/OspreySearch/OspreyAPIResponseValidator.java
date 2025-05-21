package Com.jioMart.validateResponse.OspreySearch;

import Com.jioMart.base.BaseScript;
import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.productSearch.ProductSearch.response.ProductSearchResponse;
import Com.jioMart.model.survey.global.TestData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

import org.testng.Assert;
import com.google.gson.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@Listeners({ AllureTestNg.class })
public class OspreyAPIResponseValidator extends BaseScript {

    private static final Logger log = Logger.getLogger(OspreyAPIResponseValidator.class);
    TestData testData = null;
    Map<String, String> testDataParams = null;

    OspreyApiResponse ospreyApiResponse = null;
    private SoftAssert softAssert;

    //  GetCarouselDataResponseWrapper getCarouselDataResponseWrapper = null;
    //  public OspreyApiResponse ospreyApiResponse;


    public OspreyAPIResponseValidator(OspreyApiResponse response, TestData data) {
        this.ospreyApiResponse = response;
        this.testData = data;
        this.testDataParams = data.getOtherParams();
        this.softAssert = new SoftAssert();
    }


    public void validateTop10Results(String query) {

        int count = ospreyApiResponse.getNumFound();
        System.out.println(count);
        //  String title = null;
        //   for (int i = 0; i < 10; i++) {
        //    title = String.valueOf(ospreyApiResponse.getNumFound());//.brandStringMv.get(0).name.equalsIgnoreCase(query));//getDocs().get(0).getNameTextEn();//getdocs().get(0).getHits().get(i).getTitle();
        //  if (title.contains(query) == true) {
        //  logger.info("Result " + i + " is::::" + ospreyApiResponse.getDocs().get(0).getNameTextEn());//getHits().get(i).getTitle());
        //     System.out.println("Result " + i + " is::::" + ospreyApiResponse.getDocs().get(0).getNameTextEn());//getHits().get(i).getTitle());
        //   } else {
        //        System.out.println("Result " + i + " " + title + " is mismatch");
    }
    //}
//    }

    @Step("Validating basic response")
    public void validateBasicResponse() {

        log.info("=================== Basic Response Validation Started ===================");

        try {
            softAssert.assertNotNull(ospreyApiResponse, "Response should not be null");
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();
            int recordsPerPage = Integer.parseInt(testData.getOtherParams().get("records_per_page"));

            log.info("Response Analysis:");
            log.info("Total Results Found: " + numFound);
            log.info("Records Per Page: " + recordsPerPage);

            softAssert.assertNotNull(docs, "Response docs should not be null");
            softAssert.assertTrue(numFound >= 0, "Number of results should be non-negative");

            boolean docsValid = docs != null;
            boolean withinLimit = docsValid && docs.size() <= recordsPerPage;

            if (docsValid) {
                softAssert.assertTrue(withinLimit, "Results should not exceed records_per_page limit");

                // Validate each doc has required fields
                for (OspreyApiResponse.Doc doc : docs) {
                    softAssert.assertNotNull(doc.getProductName(), "Product name should not be null");
                    softAssert.assertNotNull(doc.getSkuId(), "SKU ID should not be null");
                    softAssert.assertNotNull(doc.getApplicableRegions(), "Applicable regions should not be null");
                }
            }

            // Add Allure attachment with enhanced information
            Allure.addAttachment("Basic Response Validation", String.format(
                    "Response Analysis:\n" +
                            "----------------\n" +
                            "Total Results Found: %d\n" +
                            "Records Per Page: %d\n" +
                            "Docs Present: %s\n" +
                            "Docs Count: %s\n" +
                            "Within Page Limit: %s\n" +
//                            "Applicable Regions Present: %s\n" +
                            "Validation Status: %s",
                    numFound,
                    recordsPerPage,
                    docsValid ? "Yes" : "No",
                    docsValid ? docs.size() : "N/A",
                    withinLimit ? "Yes" : "No",
                 //   docsValid && docs.stream().allMatch(doc -> doc.getApplicableRegions() != null) ? "Yes" : "No",
                    (docsValid && withinLimit) ? "PASSED" : "FAILED"
            ));

        } catch (Exception e) {
            log.error("Basic validation failed: " + e.getMessage());
            Allure.addAttachment("Basic Response Validation Error", e.getMessage());
            Assert.fail(e.getMessage());
        }

        log.info("=================== Basic Response Validation Completed ===================");
    }


    @Step("Validating query results")
    public void validateQueryResults() {

        log.info("=================== Query Results Validation Started ===================");

        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        if (ospreyApiResponse == null || products == null || products.isEmpty()) {
            String errorMessage = "Products list is null or empty";
            log.error(errorMessage);
            Allure.addAttachment("Query Results Validation Error", errorMessage);
            Assert.fail(errorMessage);
            return;
        }

        String searchQuery = testData.getOtherParams().get("query");
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            String errorMessage = "Search query is null or empty";
            log.error(errorMessage);
            Assert.fail(errorMessage);
            return;
        }
        searchQuery = searchQuery.toLowerCase().trim();

        log.info("Validating relevancy for query: " + searchQuery);

        StringBuilder relevancyDetails = new StringBuilder();
        relevancyDetails.append(String.format("Search Query: %s\n\n", searchQuery));
        int relevantCount = 0;

        for (OspreyApiResponse.Doc product : products) {
//             boolean isRelevant = false;
            String productName = product.getProductName();
            String productCode = product.getSkuId();
            String instock = product.getInStock();
            boolean isRelevant = false;
            // Check product name
            boolean nameMatch = productName != null &&
                    productName.toLowerCase().contains(searchQuery);

            // Get product attributes and check categories
            // Check L4 categories
            boolean l4Match = false;
            Map<String, List<String>> hierarchy = product.getProductCategoryHierarchy();
            if (hierarchy != null) {
                List<String> l4Categories = hierarchy.get("l4");
                if (l4Categories != null && !l4Categories.isEmpty()) {
                    String finalSearchQuery = searchQuery;
                    l4Match = l4Categories.stream()
                            .anyMatch(category -> category != null &&
                                    category.toLowerCase().contains(finalSearchQuery));
                }
            }
//            if (product.getProductAttributes() != null) { //    &&
//                //    product.getProductAttributes().getProductCategoryHierarchy() != null) {
//
//                List<String> l4Categories = product.getProductCategoryHierarchy().getL4Categories();
////                        .getProductCategoryHierarchy()
////                        .getL4();
//
//                if (l4Categories != null && !l4Categories.isEmpty()) {
//                    String finalSearchQuery = searchQuery;
//                    l4Match = l4Categories.stream()
//                            .anyMatch(category -> category != null &&
//                                    category.toLowerCase().contains(finalSearchQuery));
//                }
//            }


            // Product is relevant if either name or L4 category matches
            isRelevant = nameMatch || l4Match;
            if (isRelevant) {
                relevantCount++;
            }

            // Build category details for reporting
            StringBuilder categoryInfo = new StringBuilder();
            if (hierarchy != null) {
                categoryInfo.append("Category Hierarchy:\n");
                Arrays.asList("l0", "l1", "l2", "l3", "l4").forEach(level -> {
                    List<String> categories = hierarchy.get(level);
                    categoryInfo.append(level.toUpperCase()).append(": ")
                            .append(formatCategories(categories)).append("\n");
                });
            } else {
                categoryInfo.append("No category information available\n");
            }
//            if (product.getProductAttributes() != null &&
//                    product.getProductCategoryHierarchy() != null) {
//
//                var hierarchy = product.getProductCategoryHierarchy();
//                categoryInfo.append("Category Hierarchy:\n");
//                categoryInfo.append("L0: ").append(formatCategories(hierarchy.getL0())).append("\n");
//                categoryInfo.append("L1: ").append(formatCategories(hierarchy.getL1())).append("\n");
//                categoryInfo.append("L2: ").append(formatCategories(hierarchy.getL2())).append("\n");
//                categoryInfo.append("L3: ").append(formatCategories(hierarchy.getL3())).append("\n");
//                categoryInfo.append("L4: ").append(formatCategories(hierarchy.getL4())).append("\n");
//            } else {
//                categoryInfo.append("No category information available\n");
//            }

            relevancyDetails.append(String.format(
                    "Product Details:\n" +
                            "---------------\n" +
                            "Code: %s\n" +
                            "Name: %s\n" +
                            "Name Match: %s\n" +
                            "L4 Category Match: %s\n" +
                            "%s\n" +
                            "Overall Relevancy: %s\n" +
                            "------------------------\n",
                    productCode,
                    productName,
                    nameMatch ? "Yes" : "No",
                    l4Match ? "Yes" : "No",
                    categoryInfo.toString(),
                    isRelevant ? "Relevant" : "Not Relevant"
            ));
        }
        // Calculate relevancy metrics
        double relevancyRate = products.isEmpty() ? 0 : (relevantCount * 100.0 / products.size());
        boolean isAcceptableRelevancy = relevancyRate >= 50.0;

        // Create summary
        String summary = String.format(
                "Relevancy Analysis Summary:\n" +
                        "------------------------\n" +
                        "Total Products: %d\n" +
                        "Relevant Products: %d\n" +
                        "Relevancy Rate: %.2f%%\n" +
                        "Status: %s\n\n" +
                        "Detailed Analysis:\n" +
                        "----------------\n%s",
                products.size(),
                relevantCount,
                relevancyRate,
                isAcceptableRelevancy ? "PASSED" : "FAILED",
                relevancyDetails.toString()
        );

        log.info(summary);
        Allure.addAttachment("Query Results Validation", summary);

        Assert.assertTrue(isAcceptableRelevancy,
                String.format("Low relevancy rate: %.2f%% (minimum expected: 50%%)", relevancyRate));
    }

//    private String formatCategories(List<String> categories) {
//        if (categories == null || categories.isEmpty()) {
//            return "None";
//        }
//        return String.join(", ", categories);
//    }

//    private String buildCategoryDetails(OspreyApiResponse.ProductAttributes attributes) {
//        if (attributes == null || getProductCategoryHierarchy() == null) {
//            return "No category information available";
//        }
//
//        OspreyApiResponse.CategoryHierarchy hierarchy = attributes.getProductCategoryHierarchy();
//        return String.format(
//                "L0: %s\n" +
//                        "L1: %s\n" +
//                        "L2: %s\n" +
//                        "L3: %s\n" +
//                        "L4: %s",
//                formatCategoryList(hierarchy.getL0()),
//                formatCategoryList(hierarchy.getL1()),
//                formatCategoryList(hierarchy.getL2()),
//                formatCategoryList(hierarchy.getL3()),
//                formatCategoryList(hierarchy.getL4())
//        );
//    }

   private String formatCategoryList(List<String> categories) {
        return categories != null && !categories.isEmpty() ?
                String.join(", ", categories) : "N/A";
    }

        // Get all category levels
//                    List<String> allCategories = new ArrayList<>();
//                    addCategoriesWithLevel(allCategories, hierarchy.getL0(), "L0");
//                    addCategoriesWithLevel(allCategories, hierarchy.getL1(), "L1");
//                    addCategoriesWithLevel(allCategories, hierarchy.getL2(), "L2");
//                    addCategoriesWithLevel(allCategories, hierarchy.getL3(), "L3");
//                    addCategoriesWithLevel(allCategories, hierarchy.getL4(), "L4");

//                    // Check relevancy in name and categories
//                    boolean nameMatch = productName != null &&
//                            productName.toLowerCase().contains(searchQuery);
//                    String finalSearchQuery = searchQuery;
//                    boolean categoryMatch = allCategories.stream()
//                            .map(String::toLowerCase)
//                            .anyMatch(category -> category.contains(finalSearchQuery));
//
//                    isRelevant = nameMatch || categoryMatch;
//                    if (isRelevant) {
//                        relevantCount++;

//                    }

                    // Build category details
//                    categoryInfo.append("Category Hierarchy:\n");
////                    appendCategoryLevel(categoryInfo, hierarchy.getL0(), "L0");
////                    appendCategoryLevel(categoryInfo, hierarchy.getL1(), "L1");
////                    appendCategoryLevel(categoryInfo, hierarchy.getL2(), "L2");
////                    appendCategoryLevel(categoryInfo, hierarchy.getL3(), "L3");
//                    appendCategoryLevel(categoryInfo, hierarchy.getL4(), "L4");
//                }
//            }

            // Build product relevancy details
//            relevancyDetails.append(String.format(
//                    "Product Details:\n" +
//                            "---------------\n" +
//                            "Code: %s\n" +
//                            "Name: %s\n" +
//                            "In Stock: %s\n" +
//                            "%s\n" +
//                            "Relevancy: %s\n" +
//                            "------------------------\n",
//                    productCode,
//                    productName,
//                    instock,
//                    categoryInfo.toString(),
//                    isRelevant ? "Relevant" : "Not Relevant"
//            ));
//        }
//
//        // Calculate relevancy metrics
//        double relevancyRate = products.isEmpty() ? 0 : (relevantCount * 100.0 / products.size());
//        boolean isAcceptableRelevancy = relevancyRate >= 50.0;
//
//        // Create summary
//        String summary = String.format(
//                "Relevancy Analysis Summary:\n" +
//                        "------------------------\n" +
//                        "Total Products: %d\n" +
//                        "Relevant Products: %d\n" +
//                        "Relevancy Rate: %.2f%%\n" +
//                        "Status: %s\n\n" +
//                        "Detailed Analysis:\n" +
//                        "----------------\n" +
//                        "Search Query: %s\n\n%s",
//                products.size(),
//                relevantCount,
//                relevancyRate,
//                isAcceptableRelevancy ? "PASSED" : "FAILED",
//                searchQuery,
//                relevancyDetails.toString()
//        );
//
//        log.info(summary);
//        Allure.addAttachment("Query Results Validation", summary);
//
//        Assert.assertTrue(isAcceptableRelevancy,
//                String.format("Low relevancy rate: %.2f%% (minimum expected: 50%%)", relevancyRate));
//    }

    private void addCategoriesWithLevel(List<String> target, List<String> categories, String level) {
        if (categories != null && !categories.isEmpty()) {
            categories.forEach(category -> target.add(String.format("%s: %s", level, category)));
        }
    }

    private void appendCategoryLevel(StringBuilder builder, List<String> categories, String level) {
        builder.append(level).append(": ");
        if (categories != null && !categories.isEmpty()) {
            builder.append(String.join(", ", categories));
        } else {
            builder.append("N/A");
        }
        builder.append("\n");
    }

    private static class RelevancyResult {
        boolean isRelevant;
        String details;

        RelevancyResult(boolean isRelevant, String details) {
            this.isRelevant = isRelevant;
            this.details = details;
        }
    }

//    private RelevancyResult checkProductRelevancy(OspreyApiResponse.Doc product, String searchQuery) {
//        boolean nameMatch = false;
//        boolean categoryMatch = false;
//        boolean descriptionMatch = false;
//
//        // Check product name
//        String productName = product.getProductName();
//        if (productName != null) {
//            nameMatch = productName.toLowerCase().contains(searchQuery);
//        }
//
////        // Check description
////        String description = product.get();
////        if (description != null) {
////            descriptionMatch = description.toLowerCase().contains(searchQuery);
////        }
//
//        // Check categories
//        List<String> categories = product.g;
//        if (categories != null) {
//            categoryMatch = categories.stream()
//                    .anyMatch(category -> category != null &&
//                            category.toLowerCase().contains(searchQuery));
//        }
//
//        boolean isRelevant = nameMatch || categoryMatch || descriptionMatch;
//
//        String details = String.format(
//                "Product Details:\n" +
//                        "---------------\n" +
//                        "Name: %s\n" +
//                        "SKU: %s\n" +
//                        "Categories: %s\n" +
//                        "Name Match: %s\n" +
//                        "Category Match: %s\n" +
//                        "Description Match: %s\n" +
//                        "Overall Relevancy: %s\n" +
//                        "------------------------\n",
//                productName,
//                product.getSkuId(),
//                categories != null ? String.join(", ", categories) : "N/A",
//                nameMatch ? "Yes" : "No",
//                categoryMatch ? "Yes" : "No",
//                descriptionMatch ? "Yes" : "No",
//                isRelevant ? "Relevant" : "Not Relevant"
//        );
//
//        return new RelevancyResult(isRelevant, details);
//    }

    @Step("Validate applicable regions response")
    public void validateApplicableRegionsResponse() {
        log.info("Starting applicable regions validation");

        try {
            // Get applicable regions from response
            List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
            if (products == null || products.isEmpty()) {
                Assert.fail("No products found in response");
                return;
            }

            // Get expected regions from test data
            String regionsStr = testData.getOtherParams().get("applicable_regions");
            if (regionsStr == null || regionsStr.trim().isEmpty()) {
                log.warn("No applicable regions specified in test data");
                return;
            }

            List<String> expectedRegions = Arrays.asList(regionsStr.split(","));
            int matchingProducts = 0;
            StringBuilder validationDetails = new StringBuilder();

            for (OspreyApiResponse.Doc product : products) {
                List<String> productRegions = product.getApplicableRegions();
                boolean matches = false;

                if (productRegions != null && !productRegions.isEmpty()) {
                    for (String expectedRegion : expectedRegions) {
                        if (productRegions.contains(expectedRegion.trim())) {
                            matches = true;
                            break;
                        }
                    }
                }

                if (matches) {
                    matchingProducts++;
                }

                validationDetails.append(String.format(
                        "Product: %s\n" +
                                "Regions: %s\n" +
                                "Matches: %s\n" +
                                "-------------------\n",
                        product.getProductName(),
                        productRegions != null ? String.join(", ", productRegions) : "None",
                        matches ? "Yes" : "No"
                ));
            }

            // Validate results
            boolean allMatch = (matchingProducts == products.size());

            // Log results
            log.info(String.format(
                    "Applicable Regions Validation:\n" +
                            "Total Products: %d\n" +
                            "Matching Products: %d\n" +
                            "Status: %s",
                    products.size(), matchingProducts,
                    allMatch ? "PASSED" : "FAILED"
            ));

            // Add to Allure report
            Allure.addAttachment("Applicable Regions Validation",
                    String.format(
                            "Expected Regions: %s\n" +
                                    "Total Products: %d\n" +
                                    "Matching Products: %d\n" +
                                    "Status: %s\n\n" +
                                    "Details:\n%s",
                            String.join(", ", expectedRegions),
                            products.size(),
                            matchingProducts,
                            allMatch ? "PASSED" : "FAILED",
                            validationDetails.toString()
                    )
            );

            Assert.assertTrue(allMatch,
                    String.format("Not all products match applicable regions. Found %d/%d matching products",
                            matchingProducts, products.size()));

        } catch (Exception e) {
            log.error("Error during applicable regions validation: " + e.getMessage());
            Assert.fail("Applicable regions validation failed: " + e.getMessage());
        }
    }

    // Helper methods
    private void addIfNotEmpty(List<String> target, List<String> source) {
        if (source != null && !source.isEmpty()) {
            target.addAll(source);
        }
    }

    private String formatCategories(List<String> categories) {
        return categories != null && !categories.isEmpty()
                ? String.join(", ", categories)
                : "None";
    }

    @Step("Validating product relevance: {product.getCodeString()}")
    private void validateProductRelevance(OspreyApiResponse.Doc product,String searchQuery) {
        boolean isRelevant = false;
        String productCode = product.getSkuId();
        String productName = product.getProductName();

        log.info("\nValidating Product:");
        log.info("Code: " + productCode);
        log.info("Name: " + productName);

        // Check product name
        if (productName != null) {
            isRelevant = productName.toLowerCase().contains(searchQuery.toLowerCase());
            log.info("Name Match: " + isRelevant);
        }

        // Check categories if name doesn't match
//        List<String> categories = product.getL1l3categoryEnStringMv();
//        if (!isRelevant && categories != null) {
//            isRelevant = categories.stream()
//                    .map(String::toLowerCase)
//                    .anyMatch(category -> category.contains(searchQuery.toLowerCase()));
//            log.info("Categories: " + String.join(", ", categories));
//            log.info("Category Match: " + isRelevant);
//        }

        if (!isRelevant) {
            log.error(String.format("Product not relevant to search query '%s':", searchQuery));
            log.error("Product Code: " + productCode);
            log.error("Product Name: " + productName);
            Assert.fail("Product " + productCode + " is not relevant to search query: " + searchQuery);
        }
    }

    @Step("Validating product details")
    public void validateProductDetails() {
        log.info("=================== Product Details Validation Started ===================");

        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        if (products == null) {
            String errorMessage = "Products list is null";
            log.error(errorMessage);
            Allure.addAttachment("Product Details Validation Error", errorMessage);
            Assert.fail(errorMessage);
            return;
        }

        StringBuilder productDetails = new StringBuilder();
        int validProductCount = 0;

        for (OspreyApiResponse.Doc product : products) {
            log.info("Validating product: " + product.getProductName());
            boolean isValid = true;
            Map<String, Boolean> validationResults = new HashMap<>();

            // Validate all required fields
            validationResults.put("Brand Name", product.getBrandName() != null && !product.getBrandName().isEmpty());
            validationResults.put("SKU ID", product.getSkuId() != null && !product.getSkuId().trim().isEmpty());
            validationResults.put("Product Name", product.getProductName() != null && !product.getProductName().trim().isEmpty());
//            validationResults.put("Price", product.getPrice() != null && product.getPrice() > 0);
            validationResults.put("Price", validatePrice(product.getPrice()));
            validationResults.put("In Stock", product.getInStock() != null);
            validationResults.put("Applicable Regions", product.getApplicableRegions() != null && !product.getApplicableRegions().isEmpty());
            validationResults.put("Inventory Stores", product.getInventoryStores() != null && !product.getInventoryStores().isEmpty());

            // Check if all validations passed
            isValid = validationResults.values().stream().allMatch(result -> result);
            if (isValid) validProductCount++;

            // Build detailed validation report
            productDetails.append(String.format(
                    "Product Details:\n" +
                            "---------------\n" +
                            "Name: %s\n" +
                            "Brand: %s\n" +
                            "SKU ID: %s\n" +
                            "Price: %.2f\n" +
                            "In Stock: %s\n" +
                            "Regions: %s\n" +
                            "Stores: %s\n\n" +
                            "Validation Status:\n" +
                            "----------------\n" +
                            "- Brand Name: %s\n" +
                            "- SKU ID: %s\n" +
                            "- Product Name: %s\n" +
                            "- Price: %s\n" +
                            "- In Stock Status: %s\n" +
                            "- Applicable Regions: %s\n" +
                            "- Inventory Stores: %s\n" +
                            "Overall Status: %s\n" +
                            "------------------------\n",
                    product.getProductName(),
                    product.getBrandName() != null ? String.join(", ", product.getBrandName()) : "N/A",
                    product.getSkuId(),
                    product.getPrice(),
                    product.getInventoryStores(),
                    product.getApplicableRegions() != null ? String.join(", ", product.getApplicableRegions()) : "N/A",
                    product.getInventoryStores() != null ? String.join(", ", product.getInventoryStores()) : "N/A",
                    validationResults.get("Brand Name") ? "✓" : "✗",
                    validationResults.get("SKU ID") ? "✓" : "✗",
                    validationResults.get("Product Name") ? "✓" : "✗",
                    validationResults.get("Price") ? "✓" : "✗",
                    validationResults.get("In Stock") ? "✓" : "✗",
                    validationResults.get("Applicable Regions") ? "✓" : "✗",
                    validationResults.get("Inventory Stores") ? "✓" : "✗",
                    isValid ? "VALID" : "INVALID"
            ));

            // Perform assertions
            softAssert.assertNotNull(product.getBrandName(), "Brand name should not be null");
            softAssert.assertNotNull(product.getSkuId(), "SKU ID should not be null");
            softAssert.assertNotNull(product.getProductName(), "Product name should not be null");
            softAssert.assertNotNull(product.getPrice(), "Price should not be null");
         //   softAssert.assertTrue(product.getPrice() > 0, "Price should be greater than 0");
       //     softAssert.assertTrue(product.getPrice() > 0, "Price should be greater than 0");
            softAssert.assertNotNull(product.getInStock(), "Inventory status should not be null");
            softAssert.assertNotNull(product.getApplicableRegions(), "Applicable regions should not be null");
            softAssert.assertNotNull(product.getInventoryStores(), "Inventory stores should not be null");
        }

        // Add summary to Allure
        Allure.addAttachment("Product Details Validation", String.format(
                "Product Validation Summary:\n" +
                        "------------------------\n" +
                        "Total Products: %d\n" +
                        "Valid Products: %d\n" +
                        "Validation Rate: %.2f%%\n\n" +
                        "Detailed Analysis:\n" +
                        "----------------\n%s",
                products.size(),
                validProductCount,
                (validProductCount * 100.0 / products.size()),
                productDetails.toString()
        ));

        log.info(String.format("Validation Complete - %d/%d products valid (%.2f%%)",
                validProductCount, products.size(), (validProductCount * 100.0 / products.size())));
        log.info("=================== Product Details Validation Completed ===================");
    }

    private boolean validatePrice(Object price) {
        if (price == null) {
            return false;
        }

        try {
            float priceValue;
            if (price instanceof Number) {
                priceValue = ((Number) price).floatValue();
            } else if (price instanceof String) {
                priceValue = Float.parseFloat(((String) price).replaceAll("[^\\d.]", ""));
            } else {
                return false;
            }
            return priceValue > 0;
        } catch (NumberFormatException e) {
            log.warn("Failed to parse price value: " + price);
            return false;
        }
    }


    @Step("Validate error message for empty search query")
    public void validateEmptySearchQueryError(OspreyApiResponse response,TestData testData) {
//        try {
//            String responseBody = response.asString("", "store");
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode rootNode = mapper.readTree(responseBody);
//            JsonNode detailNode = rootNode.get("detail");
//
//            if (detailNode != null) {
//                log.info("Validating Error Response");
//            String actualMessage = "";
//
//            if (detailNode.isTextual()) {
//                actualMessage = detailNode.asText();
//            } else if (detailNode.isArray() && detailNode.size() > 0) {
//                JsonNode firstError = detailNode.get(0);
//                actualMessage = firstError.get("msg").asText();
//            }
//
//            String expectedMessage = "Search query cannot be empty or invalid.";
//            softAssert.assertEquals(actualMessage, expectedMessage, "Error message should match");
//
//            log.info("Validated error message: " + actualMessage);
//            Allure.addAttachment("Error Response", responseBody);
//
//            } else {
//                // Success Response Validation
//                log.info("Validating Success Response");
//
//                softAssert.assertNotNull(ospreyApiResponse, "Response should not be null");
//                List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
//                int numFound = ospreyApiResponse.getNumFound();
//                int recordsPerPage = Integer.parseInt(testData.getOtherParams().get("records_per_page"));
//
//                log.info("Response Analysis:");
//                log.info("Total Results Found: " + numFound);
//                log.info("Records Per Page: " + recordsPerPage);
//
//                softAssert.assertNotNull(docs, "Response docs should not be null");
//                softAssert.assertTrue(numFound >= 0, "Number of results should be non-negative");
//
//                if (docs != null) {
//                    boolean withinLimit = docs.size() <= recordsPerPage;
//                    softAssert.assertTrue(withinLimit, "Results should not exceed records_per_page limit");
//
//                    // Validate each doc
//                    for (OspreyApiResponse.Doc doc : docs) {
//                        softAssert.assertNotNull(doc.getProductName(), "Product name should not be null");
//                        softAssert.assertNotNull(doc.getSkuId(), "SKU ID should not be null");
//                        softAssert.assertNotNull(doc.getApplicableRegions(), "Applicable regions should not be null");
//                    }
//
//                    // Add response details to Allure
//                    String details = String.format(
//                            "Response Analysis:\n" +
//                                    "----------------\n" +
//                                    "Total Results Found: %d\n" +
//                                    "Records Per Page: %d\n" +
//                                    "Docs Present: Yes\n" +
//                                    "Docs Count: %d\n" +
//                                    "Within Page Limit: %s\n" +
//                                    "Validation Status: %s",
//                            numFound,
//                            recordsPerPage,
//                            docs.size(),
//                            withinLimit ? "Yes" : "No",
//                            withinLimit ? "PASSED" : "FAILED"
//                    );
//
//                    Allure.addAttachment("Response Validation Details", details);
//                    log.info(details);
//                }
//            }
//
//            softAssert.assertAll();
//
//        } catch (Exception e) {
//            log.error("Validation failed: " + e.getMessage());
//            Allure.addAttachment("Validation Error", e.getMessage());
//            Assert.fail("Validation failed: " + e.getMessage());
//        }
//
//        log.info("=================== Search Response Validation Completed ===================");
//    }

        log.info("=================== Search Response Validation Started ===================");

        try {
         //   String responseBody = response.asString("","filters");
         //   ObjectMapper mapper = new ObjectMapper();
          //  JsonNode rootNode = mapper.readTree(responseBody);

            // Get query value
            Object query = testData.getOtherParams().get("query");
            boolean isQueryEmpty = query == null || query.toString().trim().isEmpty();

            log.info("Query Status: " + (isQueryEmpty ? "Empty" : "Not Empty"));
            log.info("Query Value: " + query);

            // Proceed with response validation
            softAssert.assertNotNull(ospreyApiResponse, "Response should not be null");
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();
            int recordsPerPage = Integer.parseInt(testData.getOtherParams().get("records_per_page"));

            log.info("Response Analysis:");
            log.info("Total Results Found: " + numFound);
            log.info("Records Per Page: " + recordsPerPage);

            softAssert.assertNotNull(docs, "Response docs should not be null");
            softAssert.assertTrue(numFound >= 0, "Number of results should be non-negative");

            if (docs != null) {
                boolean withinLimit = docs.size() <= recordsPerPage;
                softAssert.assertTrue(withinLimit, "Results should not exceed records_per_page limit");

                // Validate each doc
                for (OspreyApiResponse.Doc doc : docs) {
                    softAssert.assertNotNull(doc.getProductName(), "Product name should not be null");
                    softAssert.assertNotNull(doc.getSkuId(), "SKU ID should not be null");
                    softAssert.assertNotNull(doc.getApplicableRegions(), "Applicable regions should not be null");
                }

                // Add response details to Allure
                String details = String.format(
                        "Response Analysis:\n" +
                                "----------------\n" +
                                "Query Status: %s\n" +
                                "Total Results Found: %d\n" +
                                "Records Per Page: %d\n" +
                                "Docs Present: Yes\n" +
                                "Docs Count: %d\n" +
                                "Within Page Limit: %s\n" +
                                "Validation Status: %s",
                        isQueryEmpty ? "Empty" : "Not Empty",
                        numFound,
                        recordsPerPage,
                        docs.size(),
                        withinLimit ? "Yes" : "No",
                        withinLimit ? "PASSED" : "FAILED"
                );

                Allure.addAttachment("Response Validation Details", details);
                log.info(details);
            }

            softAssert.assertAll();

        } catch (Exception e) {
            log.error("Validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Validation failed: " + e.getMessage());
        }

        log.info("=================== Search Response Validation Completed ===================");
    }

//            softAssert.assertAll();
//        } catch (Exception e) {
//            log.error("Failed to parse response: " + e.getMessage());
//            softAssert.fail("JSON parsing failed: " + e.getMessage());
//        }
//    }

    @Step("Validate invalid store error response")
    public void validateInvalidStoreResponse(String query,String store) {
        String responseBody = ospreyApiResponse.asString(query,store);
        String expectedError = "{\"detail\": \"Invalid store\"}";

        softAssert.assertEquals(responseBody.replaceAll("\\s+", ""),
                expectedError.replaceAll("\\s+", ""),
                "Error message should match exactly for invalid store");

        // Validate error message
        softAssert.assertTrue(responseBody.contains("Invalid store"),
                "Response should contain invalid store error message");

        log.info("Response: " + responseBody);
        Allure.addAttachment("Invalid Store Error Response", responseBody);

        softAssert.assertAll();
    }

    @Step("Validate missing field error response")
    public void validateMissingFieldResponse(String query,String filters) throws IOException {

        String responseStr = ospreyApiResponse.asString(query, filters);

        // Parsing the response string into a JsonNode for easy traversal
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualResponseJson = mapper.readTree(responseStr);

        // Define the expected message for validation
        String expectedMsg = "Value error, A filter with fieldName 'applicable_regions' is required.";

        // Extract the 'detail' node from the response
        JsonNode detailNode = actualResponseJson.get("detail");

        // Ensure the detail array is not empty
        if (detailNode != null && detailNode.isArray() && detailNode.size() > 0) {
            JsonNode firstDetail = detailNode.get(0);

            // Extract and validate the 'msg' field
            String actualMsg = firstDetail.get("msg").asText();
            softAssert.assertEquals(actualMsg, expectedMsg, "Error message should match exactly for missing field");

            // Check other fields like 'type' and 'loc'
            softAssert.assertTrue(firstDetail.get("type").asText().contains("value_error"),
                    "Response should contain 'value_error' as type");
            softAssert.assertTrue(firstDetail.get("loc").toString().contains("body"),
                    "Response should contain 'body' in location");

            // Validate that the response contains the query
            softAssert.assertTrue(responseStr.contains(query),
                    "Response should include the original query: " + query);

            // Attach the response to Allure for visual inspection
            log.info("Missing field response: " + responseStr);
            Allure.addAttachment("Error Response", responseStr);
        } else {
            // Handle the case where 'detail' is missing or malformed
            softAssert.fail("The 'detail' field is missing or malformed in the response");
        }

        // Ensure all assertions are checked
        softAssert.assertAll();
    }

    @Step("Validate price sorting response")
    public void validatePriceAscSortingResponse() {
//        log.info("=================== Price Sorting Validation Started ===================");
//
//        // Get sorting parameters
//        String sortField = testData.getOtherParams().get("sort_field");
//        String sortOrder = testData.getOtherParams().get("sort_order");
//        log.info("Validating price sorting - Field: " + sortField + ", Order: " + sortOrder);
//
//        // Get products and validate sorting
//        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
//        boolean isSorted = true;
//
//        // Format prices to handle decimal precision
//        DecimalFormat df = new DecimalFormat("#.##");
//        df.setRoundingMode(RoundingMode.HALF_UP);
//
//        for (int i = 0; i < products.size() - 1; i++) {
//            // Convert prices to BigDecimal for precise comparison
//            double currentPriceValue = convertPriceToDouble(products.get(i).getAvgSellingPrice());
//            double nextPriceValue = convertPriceToDouble(products.get(i + 1).getAvgSellingPrice());
////            BigDecimal currentPrice = convertToPrice(products.get(i).getPrice());
////            BigDecimal nextPrice = convertToPrice(products.get(i + 1).getPrice());
//            //   BigDecimal currentPrice = BigDecimal.valueOf(products.get(i).getPrice()).setScale(2, RoundingMode.HALF_UP);
//            //     BigDecimal nextPrice = BigDecimal.valueOf(products.get(i + 1).getPrice()).setScale(2, RoundingMode.HALF_UP);
//
//            log.info(String.format("Product %d - Price INR: %.2f", i + 1, currentPriceValue));
//            //   double currentPrice = products.get(i).getPrice();
//            //   double nextPrice = products.get(i + 1).getPrice();
//
//            //     log.info(String.format("Product %d - Price INR: %.2f", i + 1, currentPrice));
//
//            if (sortOrder.equalsIgnoreCase("asc")) {
//                if (currentPriceValue > nextPriceValue) {
//                    isSorted = false;
//                    //  log.error("Price sorting error at index " + i + ": " + currentPrice + " > " + nextPrice);
//                    log.error(String.format("Price sorting error at index %d: %.2f > %.2f",
//                            i, currentPriceValue, nextPriceValue));
//                    break;
//                }
//            } else {
//                if (currentPriceValue < nextPriceValue) {
//                    isSorted = false;
//                    log.error(String.format("Price sorting error at index %d: %.2f < %.2f",
//                            i, currentPriceValue, nextPriceValue));
//                    break;
//                }
//            }
//        }
//
//        // Log last product price
//        if (!products.isEmpty()) {
//            double lastPrice = convertPriceToDouble(products.get(products.size() - 1).getAvgSellingPrice());
//            log.info(String.format("Product %d - Price INR: %.2f",
//                    products.size(), lastPrice));
//        }
//        // Validation
//        softAssert.assertTrue(isSorted, "Products should be sorted by price in " + sortOrder + " order");
//
//        // Log price analysis
//        // Log price analysis
//        log.info("\nPrice Analysis:");
//        log.info("Total products analyzed: " + products.size());
//
//        double firstPrice = convertPriceToDouble(products.get(0).getAvgSellingPrice());
//        double lastPrice = convertPriceToDouble(products.get(products.size() - 1).getAvgSellingPrice());
//        log.info(String.format("Price range (INR): %.2f to %.2f", firstPrice, lastPrice));
//
//        Allure.addAttachment("Price Sorting Results", String.format(
//                "Test Summary:\n" +
//                        "-------------\n" +
//                        "Sort Field: %s\n" +
//                        "Sort Order: %s\n" +
//                        "Total Products: %d\n\n" +
//                        "Price Range (INR):\n" +
//                        "------------\n" +
//                        "First Product: %.2f\n" +
//                        "Last Product: %.2f\n\n" +
//                        "Price Statistics:\n" +
//                        "---------------\n" +
//                        "Minimum Price: %.2f\n" +
//                        "Maximum Price: %.2f\n\n" +
//                        "Validation:\n" +
//                        "----------\n" +
//                        "Sorting Status: %s",
//                sortField,
//                sortOrder,
//                products.size(),
//                firstPrice,
//                lastPrice,
//                getMinPrice(products),
//                getMaxPrice(products),
//                isSorted ? "PASSED" : "FAILED"
//        ));
//
//        log.info("Price Sorting Validation Completed ");
//    }

        log.info("=================== Price Sorting Validation (ASC) Started ===================");

        String sortField = testData.getOtherParams().get("sort_field");
        String sortOrder = testData.getOtherParams().get("sort_order");
        log.info("Validating price sorting - Field: " + sortField + ", Order: " + sortOrder);

        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        boolean isSorted = true;

        log.info("\n Product Price Details (Ascending) ");
        StringBuilder priceDetails = new StringBuilder();

        for (int i = 0; i < products.size() - 1; i++) {
            OspreyApiResponse.Doc currentProduct = products.get(i);
            OspreyApiResponse.Doc nextProduct = products.get(i + 1);

            double currentPrice = convertPriceToDouble(currentProduct.getAvgSellingPrice());
            double nextPrice = convertPriceToDouble(nextProduct.getAvgSellingPrice());

            String priceLog = String.format(
                    "Product %d:\n" +
                            "Name: %s\n" +
                            "Price (INR): %.2f\n" +
                            "------------------------",
                    (i + 1), currentProduct.getProductName(), currentPrice
            );
            log.info(priceLog);
            priceDetails.append(priceLog).append("\n");

            // Validate ascending order
            if (currentPrice > nextPrice) {
                isSorted = false;
                log.error(String.format("Price sorting error at index %d: %.2f > %.2f",
                        i, currentPrice, nextPrice));
                break;
            }
        }

        // Log last product
        if (!products.isEmpty()) {
            OspreyApiResponse.Doc lastProduct = products.get(products.size() - 1);
            double lastPrice = convertPriceToDouble(lastProduct.getAvgSellingPrice());
            String lastPriceLog = String.format(
                    "Product %d:\n" +
                            "Name: %s\n" +
                            "Price (INR): %.2f\n" +
                            "------------------------",
                    products.size(), lastProduct.getProductName(), lastPrice
            );
            log.info(lastPriceLog);
            priceDetails.append(lastPriceLog);
        }

        // Validation summary
        softAssert.assertTrue(isSorted, "Products should be sorted by price in ascending order");

        // Price analysis
        double lowestPrice = convertPriceToDouble(products.get(0).getAvgSellingPrice());
        double highestPrice = convertPriceToDouble(products.get(products.size() - 1).getAvgSellingPrice());

        log.info("\nPrice Range Analysis:");
        log.info(String.format("Lowest Price (First Product): %.2f INR", lowestPrice));
        log.info(String.format("Highest Price (Last Product): %.2f INR", highestPrice));
        log.info("Total Products Analyzed: " + products.size());

        Allure.addAttachment("Price Sorting (ASC) Results", String.format(
                "Test Summary:\n" +
                        "-------------\n" +
                        "Sort Field: %s\n" +
                        "Sort Order: %s\n" +
                        "Total Products: %d\n\n" +
                        "Price Range:\n" +
                        "------------\n" +
                        "Lowest Price: %.2f INR\n" +
                        "Highest Price: %.2f INR\n" +
                        "Price Difference: %.2f INR\n\n" +
                        "Validation:\n" +
                        "----------\n" +
                        "Sorting Status: %s\n" +
                        "Validation Message: Products are %s sorted in ascending order",
                sortField,
                sortOrder,
                products.size(),
                lowestPrice,
                highestPrice,
                highestPrice - lowestPrice,
                isSorted ? "PASSED" : "FAILED",
                isSorted ? "correctly" : "not correctly"
        ));

        log.info("Price Sorting Validation (ASC) Completed");
    }

// Add this helper method to your class
        private double convertPriceToDouble (Object price){
            if (price == null) {
                return 0.0;
            }
            try {
                if (price instanceof Number) {
                    return ((Number) price).doubleValue();
                } else if (price instanceof String) {
                    return Double.parseDouble(((String) price).replaceAll("[^\\d.]", ""));
                }
            } catch (NumberFormatException e) {
                log.warn("Failed to parse price: " + price);
            }
            return 0.0;
        }

    @Step("Validating Disabled Facets")
    public void validateDisabledFacets() {

        log.info("=================== Empty Facet Lists Validation Started ===================");

        try {

            softAssert.assertNotNull(ospreyApiResponse, "Response should not be null");

            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
         //   Map<String,Object> facetData = ospreyApiResponse.getFacetData();
            StringBuilder facetAnalysis = new StringBuilder();
            int emptyListCount = 0;
            int totalFacets = 0;
            List<String> nonEmptyFacets = new ArrayList<>();

            if (facetData == null) {
                log.info("FacetData is null in response");
                Allure.addAttachment("Empty Facet Lists Analysis", "FacetData is null in response");
                return;
            }

                for (Map.Entry<String, Object> entry : facetData.entrySet()) {
                 //   for (Map.Entry<String,Object> entry : facetData.entrySet()) {
                    String facetName = entry.getKey();
                    Object value = entry.getValue();
                    List<Map<String, Object>> facetList ;//= (List<Map<String, Object>>) entry.getValue();

                    // Safely handle different value types
                    try {
                        if (value instanceof List) {
                            facetList = (List<Map<String, Object>>) value;
                        } else if (value instanceof Map) {
                            // Handle single map case
                            Map<String, Object> mapValue = (Map<String, Object>) value;
                            facetList = Collections.singletonList(mapValue);
                        } else {
                            log.error("Facet {} has unexpected type: {}");
                            facetList = Collections.emptyList();
                        }
                    } catch (ClassCastException e) {
                        log.error("Failed to process facet {}: {}");
                        facetList = Collections.emptyList();
                    }

                    boolean isEmpty = facetList == null || facetList.isEmpty();
                    totalFacets++;

                    if (isEmpty) {
                        emptyListCount++;
                    } else {
                        nonEmptyFacets.add(facetName);
                    }

                    String status = isEmpty ? "✓ EMPTY" : "✗ NOT EMPTY";
                    String validationResult = isEmpty ? "PASSED" : "FAILED";

                    facetAnalysis.append(String.format(
                            "Facet: %s\n" +
                                    "Status: %s\n" +
                                    "List Size: %d\n" +
                                    "Validation: %s\n" +
                                    "------------------------\n",
                            facetName,
                            status,
                            facetList != null ? facetList.size() : 0,
                            validationResult
                    ));
                }

            double emptyPercentage = totalFacets > 0 ?
                    (emptyListCount * 100.0 / totalFacets) : 0;
            boolean allListsEmpty = emptyListCount == totalFacets;

            // Create status with symbols
            String overallStatus = allListsEmpty ?
                    "✅ ALL LISTS EMPTY - PASSED" :
                    "❌ SOME LISTS NOT EMPTY - FAILED";

            // Add results to Allure
            Allure.addAttachment("Empty Facet Lists Analysis", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary Statistics:\n" +
                            "------------------\n" +
                            "Total Facets: %d\n" +
                            "Empty Lists: %d\n" +
                            "Non-Empty Lists: %d\n" +
                            "Empty Percentage: %.2f%%\n\n" +
                            "Non-Empty Facets:\n" +
                            "----------------\n%s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    totalFacets,
                    emptyListCount,
                    (totalFacets - emptyListCount),
                    emptyPercentage,
                    nonEmptyFacets.isEmpty() ? "None" : String.join("\n", nonEmptyFacets),
                    facetAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Empty facet lists validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Facet validation failed: " + e.getMessage());
        }

        log.info("=================== Empty Facet Lists Validation Completed ===================");
    }

    @Step("Validating Enabled Facets")
    public void validateEnabledFacets() {
//        log.info("=================== Enabled Facets Validation Started ===================");
//
//        try {
//            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
//            StringBuilder facetAnalysis = new StringBuilder();
//            int nonEmptyListCount = 0;
//            int totalFacets = 0;
//            List<String> emptyFacets = new ArrayList<>();
//
//            if (facetData == null) {
//                log.error("FacetData is null when facets are enabled");
//                Assert.fail("FacetData should not be null when facets are enabled");
//                return;
//            }
//
//            for (Map.Entry<String, Object> entry : facetData.entrySet()) {
//                String facetName = entry.getKey();
//          //      Object facetValue = entry.getValue();
//                List<Map<String, Object>> facetList = (List<Map<String, Object>>) entry.getValue();
//                boolean hasData = facetList != null && !facetList.isEmpty();
//              //  boolean hasData = false;
//                int dataSize = facetList != null ? facetList.size() : 0;
//                totalFacets++;
//
//                if (hasData) {
//                    nonEmptyListCount++;
//                } else {
//                    emptyFacets.add(facetName);
//                }
//
//                String status = hasData ? "✓ HAS DATA" : "✗ EMPTY";
//                String validationResult = hasData ? "PASSED" : "FAILED";
//
//                facetAnalysis.append(String.format(
//                        "Facet: %s\n" +
//                                "Status: %s\n" +
//                                "List Size: %d\n" +
//                                "Validation: %s\n" +
//                                "------------------------\n",
//                        facetName,
//                        status,
//                        dataSize,
//                        validationResult,
//                        facetList != null ? facetList.size() : 0,
//                        validationResult
//                ));
//            }
//
//            double dataPercentage = totalFacets > 0 ?
//                    (nonEmptyListCount * 100.0 / totalFacets) : 0;
//            boolean hasFacetData = nonEmptyListCount > 0;
//
//            String overallStatus = hasFacetData ?
//                    "✅ FACETS CONTAIN DATA - PASSED" :
//                    "❌ NO FACET DATA - FAILED";
//
//            Allure.addAttachment("Enabled Facets Analysis", String.format(
//                    "Overall Status: %s\n\n" +
//                            "Summary Statistics:\n" +
//                            "------------------\n" +
//                            "Total Facets: %d\n" +
//                            "Facets with Data: %d\n" +
//                            "Empty Facets: %d\n" +
//                            "Data Percentage: %.2f%%\n\n" +
//                            "Empty Facets:\n" +
//                            "------------\n%s\n\n" +
//                            "Detailed Analysis:\n" +
//                            "-----------------\n%s",
//                    overallStatus,
//                    totalFacets,
//                    nonEmptyListCount,
//                    (totalFacets - nonEmptyListCount),
//                    dataPercentage,
//                    emptyFacets.isEmpty() ? "None" : String.join("\n", emptyFacets),
//                    facetAnalysis.toString()
//            ));
//
//            softAssert.assertTrue(hasFacetData, "At least some facets should contain data when facets are enabled");
//
//        } catch (Exception e) {
//            log.error("Enabled facets validation failed: " + e.getMessage());
//            Allure.addAttachment("Validation Error", e.getMessage());
//            Assert.fail("Facet validation failed: " + e.getMessage());
//        }
//
//        log.info("=================== Enabled Facets Validation Completed ===================")

        log.info("=================== Enabled Facets Validation Started ===================");

        try {
            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
            StringBuilder facetAnalysis = new StringBuilder();
            int nonEmptyCount = 0;
            int totalFacets = 0;
            List<String> emptyFacets = new ArrayList<>();

            if (facetData == null) {
                log.error("FacetData is null when facets are enabled");
                Assert.fail("FacetData should not be null when facets are enabled");
                return;
            }

            for (Map.Entry<String, Object> entry : facetData.entrySet()) {
                String facetName = entry.getKey();
                Object facetValue = entry.getValue();
                boolean hasData = false;
                String dataDetails = "";
                int dataSize = 0;

                if (facetValue instanceof List) {
                    List<?> list = (List<?>) facetValue;
                    hasData = !list.isEmpty();
                    dataSize = list.size();
                    dataDetails = String.format("List entries: %d", dataSize);
                } else if (facetValue instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) facetValue;
                    hasData = !map.isEmpty();
                    if (map.containsKey("min") && map.containsKey("max")) {
                        dataDetails = String.format("Range: min=%.2f, max=%.2f",
                                ((Number)map.get("min")).doubleValue(),
                                ((Number)map.get("max")).doubleValue());
                        dataSize = 1;
                    } else {
                        dataSize = map.size();
                        dataDetails = String.format("Map entries: %d", dataSize);
                    }
                }

                totalFacets++;
                if (hasData) {
                    nonEmptyCount++;
                } else {
                    emptyFacets.add(facetName);
                }

                String status = hasData ? "✓ HAS DATA" : "✗ EMPTY";
                String validationResult = hasData ? "PASSED" : "FAILED";
                String facetType = facetValue instanceof Map ? "Aggregate" : "List";

                facetAnalysis.append(String.format(
                        "Facet: %s\n" +
                                "Type: %s\n" +
                                "Status: %s\n" +
                                "Details: %s\n" +
                                "Validation: %s\n" +
                                "------------------------\n",
                        facetName,
                        facetType,
                        status,
                        dataDetails,
                        validationResult
                ));
            }

            double dataPercentage = totalFacets > 0 ? (nonEmptyCount * 100.0 / totalFacets) : 0;
            boolean hasFacetData = nonEmptyCount > 0;

            String overallStatus = hasFacetData ?
                    "✅ FACETS CONTAIN DATA - PASSED" :
                    "❌ NO FACET DATA - FAILED";

            Allure.addAttachment("Enabled Facets Analysis", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary Statistics:\n" +
                            "------------------\n" +
                            "Total Facets: %d\n" +
                            "Facets with Data: %d\n" +
                            "Empty Facets: %d\n" +
                            "Data Percentage: %.2f%%\n\n" +
                            "Empty Facets:\n" +
                            "------------\n%s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    totalFacets,
                    nonEmptyCount,
                    (totalFacets - nonEmptyCount),
                    dataPercentage,
                    emptyFacets.isEmpty() ? "None" : String.join("\n", emptyFacets),
                    facetAnalysis.toString()
            ));

            softAssert.assertTrue(hasFacetData, "At least some facets should contain data when facets are enabled");

        } catch (Exception e) {
            log.error("Enabled facets validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Facet validation failed: " + e.getMessage());
        }

        log.info("=================== Enabled Facets Validation Completed ===================");

    }

    @Step("Validating Disabled Facet Value Counts")
    public void validateDisabledFacetValueCounts() {
        log.info("=================== Facet Value Counts Validation Started ===================");

        try {
            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
            StringBuilder facetAnalysis = new StringBuilder();
            int nullValueFacets = 0;
            int totalValues = 0;
            List<String> nonNullValues = new ArrayList<>();

            if (facetData != null) {
                for (Map.Entry<String, Object> entry : facetData.entrySet()) {
                    String facetName = entry.getKey();
                    Object facetValue = entry.getValue();

                    if (facetValue instanceof List) {
                        List<?> facetList = (List<?>) facetValue;
                        for (Object item : facetList) {
                            if (item instanceof Map) {
                                Map<String, Object> value = (Map<String, Object>) item;
                                totalValues++;
                                String name = String.valueOf(value.get("name"));
                                Object countValue = value.get("value");

                                boolean isNullValue = countValue == null;
                                if (isNullValue) {
                                    nullValueFacets++;
                                } else {
                                    nonNullValues.add(String.format("%s: %s", facetName, name));
                                }

                                facetAnalysis.append(String.format(
                                        "Facet: %s\n" +
                                                "Name: %s\n" +
                                                "Value: %s\n" +
                                                "Status: %s\n" +
                                                "------------------------\n",
                                        facetName,
                                        name,
                                        countValue == null ? "null" : countValue,
                                        isNullValue ? "✓ NULL VALUE" : "✗ HAS VALUE"
                                ));
                            }
                        }
                    } else if (facetValue instanceof Map) {
                        Map<String, Object> aggregateData = (Map<String, Object>) facetValue;

//                        totalValues++;
//                        String name = facetName;
//                        Object min = aggregateData.get("min");
//                        Object max = aggregateData.get("max");
//                        Object avg = aggregateData.get("avg");
//
//                        boolean isNullValue = (min == null && max == null && avg == null);
//                        if (isNullValue) {
//                            nullValueFacets++;
//                        } else {
//                            nonNullValues.add(String.format("%s: min=%s, max=%s, avg=%s",
//                                    facetName, min, max, avg));
//                        }

                        facetAnalysis.append(String.format(
                                "Facet: %s (AggregateSkipped from Validation)\n" +
                                        "Min: %s\n" +
                                        "Max: %s\n" +
                                        "Avg: %s\n" +
                                      //  "Status: %s\n" +
                                        "------------------------\n",
                                facetName,
//                                min == null ? "null" : min,
//                                max == null ? "null" : max,
//                                avg == null ? "null" : avg,
//                                isNullValue ? "✓ NULL VALUE" : "✗ HAS VALUE"
                                aggregateData.get("min"),
                                aggregateData.get("max"),
                                aggregateData.get("avg")
                        ));
                    }
                }
            }

//            boolean allValuesNull = nullValueFacets == totalValues;
            boolean allValuesNull = totalValues == 0 || nullValueFacets == totalValues;
            String overallStatus = allValuesNull ?
                    "✅ ALL FACET VALUES NULL - PASSED" :
                    "❌ SOME FACETS HAVE VALUES - FAILED";

            Allure.addAttachment("Facet Value Counts Analysis", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary Statistics:\n" +
                            "------------------\n" +
                            "Total Facet Values: %d\n" +
                            "Null Values: %d\n" +
                            "Non-null Values: %d\n" +
                            "Null Percentage: %.2f%%\n\n" +
                            "Non-null Value Details:\n" +
                            "---------------------\n%s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    totalValues,
                    nullValueFacets,
                    (totalValues - nullValueFacets),
                    (nullValueFacets * 100.0 / totalValues),
                    nonNullValues.isEmpty() ? "None" : String.join("\n", nonNullValues),
                    facetAnalysis.toString()
            ));

            softAssert.assertTrue(allValuesNull,
                    "All facet values should be null when enable_facet_counts is false");

        } catch (Exception e) {
            log.error("Facet value counts validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Facet value counts validation failed: " + e.getMessage());
        }

        log.info("=================== Facet Value Counts Validation Completed ===================");
    }

    @Step("Validating Enabled Facet Values Counts")
    public void validateEnabledFacetValuesCounts() {
//        log.info("=================== Enabled Facet Values Validation Started ===================");
//
//        try {
//            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
//            StringBuilder facetAnalysis = new StringBuilder();
//            int nonNullValueFacets = 0;
//            int totalValues = 0;
//            List<String> nullValues = new ArrayList<>();
//
//            if (facetData != null) {
//                for (Map.Entry<String, Object> entry : facetData.entrySet()) {
//                    String facetName = entry.getKey();
//                    List<Map<String, Object>> facetValues = (List<Map<String, Object>>) entry.getValue();
//
//                    if (facetValues != null) {
//                        for (Map<String, Object> value : facetValues) {
//                            totalValues++;
//                            String name = (String) value.get("name");
//                            Object countValue = value.get("value");
//
//                            boolean hasValue = countValue != null;
//                            if (hasValue) {
//                                nonNullValueFacets++;
//                            } else {
//                                nullValues.add(String.format("%s: %s", facetName, name));
//                            }
//
//                            facetAnalysis.append(String.format(
//                                    "Facet: %s\n" +
//                                            "Name: %s\n" +
//                                            "Value: %s\n" +
//                                            "Status: %s\n" +
//                                            "------------------------\n",
//                                    facetName,
//                                    name,
//                                    countValue == null ? "null" : countValue,
//                                    hasValue ? "✓ HAS VALUE" : "✗ NULL VALUE"
//                            ));
//                        }
//                    }
//                }
//            }
        log.info("=================== Enabled Facet Values Validation Started ===================");

        try {
            Map<String, Object> facetData = ospreyApiResponse.getFacetData();
            StringBuilder facetAnalysis = new StringBuilder();
            int nonNullValueFacets = 0;
            int totalValues = 0;
            List<String> nullValues = new ArrayList<>();

            if (facetData != null) {
                for (Map.Entry<String, Object> entry : facetData.entrySet()) {
                    String facetName = entry.getKey();
                    Object facetValue = entry.getValue();

                    if (facetValue instanceof List) {
                        List<?> facetList = (List<?>) facetValue;
                        for (Object item : facetList) {
                            if (item instanceof Map) {
                                Map<String, Object> value = (Map<String, Object>) item;
                                totalValues++;
                                String name = String.valueOf(value.get("name"));
                                Object countValue = value.get("value");

                                boolean hasValue = countValue != null;
                                if (hasValue) {
                                    nonNullValueFacets++;
                                } else {
                                    nullValues.add(String.format("%s: %s", facetName, name));
                                }

                                facetAnalysis.append(String.format(
                                        "Facet: %s (List)\n" +
                                                "Name: %s\n" +
                                                "Value: %s\n" +
                                                "Status: %s\n" +
                                                "------------------------\n",
                                        facetName,
                                        name,
                                        countValue == null ? "null" : countValue,
                                        hasValue ? "✓ HAS VALUE" : "✗ NULL VALUE"
                                ));
                            }
                        }
                    } else if (facetValue instanceof Map) {
                        Map<String, Object> aggregateData = (Map<String, Object>) facetValue;
                        totalValues++;

                        Object min = aggregateData.get("min");
                        Object max = aggregateData.get("max");
                        Object avg = aggregateData.get("avg");

                        boolean hasValue = (min != null || max != null || avg != null);
                        if (hasValue) {
                            nonNullValueFacets++;
                        } else {
                            nullValues.add(String.format("%s: aggregate", facetName));
                        }

                        facetAnalysis.append(String.format(
                                "Facet: %s (Aggregate)\n" +
                                        "Min: %s\n" +
                                        "Max: %s\n" +
                                        "Avg: %s\n" +
                                        "Status: %s\n" +
                                        "------------------------\n",
                                facetName,
                                min == null ? "null" : min,
                                max == null ? "null" : max,
                                avg == null ? "null" : avg,
                                hasValue ? "✓ HAS VALUE" : "✗ NULL VALUE"
                        ));
                    }
                }
            }

            boolean allValuesPresent = nonNullValueFacets == totalValues;
            String overallStatus = allValuesPresent ?
                    "✅ ALL FACET VALUES PRESENT - PASSED" :
                    "❌ SOME FACETS HAVE NULL VALUES - FAILED";

            Allure.addAttachment("Enabled Facet Values Analysis", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary Statistics:\n" +
                            "------------------\n" +
                            "Total Facet Values: %d\n" +
                            "Values Present: %d\n" +
                            "Null Values: %d\n" +
                            "Value Presence Percentage: %.2f%%\n\n" +
                            "Null Value Details:\n" +
                            "-----------------\n%s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    totalValues,
                    nonNullValueFacets,
                    (totalValues - nonNullValueFacets),
                    (nonNullValueFacets * 100.0 / totalValues),
                    nullValues.isEmpty() ? "None" : String.join("\n", nullValues),
                    facetAnalysis.toString()
            ));

            softAssert.assertTrue(allValuesPresent,
                    "All facet values should be present when enable_facet_counts is true");

        } catch (Exception e) {
            log.error("Enabled facet values validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Facet values validation failed: " + e.getMessage());
        }

        log.info("=================== Enabled Facet Values Validation Completed ===================");
    }

    @Step("Validating Retrieved Attributes")
    public void validateAttributesRetrieval() {
//        log.info("=================== Attributes Retrieval Validation Started ===================");
//
//        try {
//            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
//            int numFound = ospreyApiResponse.getNumFound();
//            StringBuilder attributeAnalysis = new StringBuilder();
//            int validDocs = 0;
//            int totalDocs = 0;
//
//            log.info("\nSearch Results Summary:");
//            log.info("=====================");
//            log.info("Total Results Found: " + numFound);
//            log.info("Documents Returned: " + (docs != null ? docs.size() : 0));
//            log.info("\nDocument Details:");
//            log.info("================");
//
//            if (docs != null) {
//                for (OspreyApiResponse.Doc doc : docs) {
//                    totalDocs++;
//                    boolean isValid = true;
//
//                    // Check if only name_text_en is present
//                    String name = doc.getProductName();
//                    boolean hasName = name != null && !name.isEmpty();
//
//                    // Verify no other fields are present
//                    boolean hasOnlyName = true; // Add logic to check other fields are null
//
//                    isValid = hasName && hasOnlyName;
//                    if (isValid) validDocs++;
//
//                    attributeAnalysis.append(String.format(
//                            "Document %d:\n" +
//                                    "Name: %s\n" +
//                                    "Has Name: %s\n" +
//                                    "Only Name Present: %s\n" +
//                                    "Status: %s\n" +
//                                    "------------------------\n",
//                            totalDocs,
//                            name,
//                            hasName ? "✓" : "✗",
//                            hasOnlyName ? "✓" : "✗",
//                            isValid ? "VALID" : "INVALID"
//                    ));
//                }
//            }
//
//            boolean allDocsValid = validDocs == totalDocs;
//            String overallStatus = allDocsValid ?
//                    "✅ ALL DOCS HAVE CORRECT ATTRIBUTES - PASSED" :
//                    "❌ SOME DOCS HAVE INCORRECT ATTRIBUTES - FAILED";
//
//            Allure.addAttachment("Attributes Retrieval Analysis", String.format(
//                    "Overall Status: %s\n\n" +
//                            "Summary Statistics:\n" +
//                            "------------------\n" +
//                            "Total Documents: %d\n" +
//                            "Valid Documents: %d\n" +
//                            "Invalid Documents: %d\n" +
//                            "Success Rate: %.2f%%\n\n" +
//                            "Detailed Analysis:\n" +
//                            "-----------------\n%s",
//                    overallStatus,
//                    totalDocs,
//                    validDocs,
//                    (totalDocs - validDocs),
//                    (validDocs * 100.0 / totalDocs),
//                    attributeAnalysis.toString()
//            ));
//
//            softAssert.assertTrue(allDocsValid,
//                    "All documents should only contain the name_text_en attribute");
//
//        } catch (Exception e) {
//            log.error("Attributes retrieval validation failed: " + e.getMessage());
//            Allure.addAttachment("Validation Error", e.getMessage());
//            Assert.fail("Attributes validation failed: " + e.getMessage());
//        }
//
//        log.info("=================== Attributes Retrieval Validation Completed ===================");

        log.info("=================== Attributes Retrieval Validation Started ===================");

        try {
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();
            StringBuilder attributeAnalysis = new StringBuilder();
            int validDocs = 0;
            int totalDocs = 0;

            log.info("\nSearch Results Summary:");
            log.info("=====================");
            log.info("Total Results Found: " + numFound);
            log.info("Documents Returned: " + (docs != null ? docs.size() : 0));
            log.info("\nDocument Details:");
            log.info("================");

            if (docs != null && !docs.isEmpty()) {
                for (OspreyApiResponse.Doc doc : docs) {
                    totalDocs++;
                    boolean isValid = true;
                    Map<String, Object> validationResults = new HashMap<>();

                    // Validate required fields
                    validationResults.put("Product Name", validateField(doc.getProductName()));
                    validationResults.put("SKU ID", validateField(doc.getSkuId()));
                    validationResults.put("Product Option ID", validateField(doc.getProductOptionId()));

                    // Validate category hierarchy
                    Map<String, List<String>> hierarchy = doc.getProductCategoryHierarchy();
                    if (hierarchy != null) {
                        for (String level : Arrays.asList("l0", "l1", "l2", "l3", "l4")) {
                            List<String> categories = hierarchy.get(level);
                            validationResults.put("Category " + level.toUpperCase(),
                                    validateField(categories != null && !categories.isEmpty() ? categories.get(0) : null));
                        }
                    }

                    // Check if all required fields are valid
                    isValid = !validationResults.containsValue(false);
                    if (isValid) validDocs++;

                    // Build detailed analysis
                    attributeAnalysis.append(String.format(
                            "Document %d:\n", totalDocs));

                    for (Map.Entry<String, Object> result : validationResults.entrySet()) {
//                        attributeAnalysis.append(String.format(
//                                "%s: %s\n",
//                                result.getKey(),
//                                result.getValue() ? "✓" : "✗"
//                        ));
                        String status;
                        if (result.getValue() instanceof Boolean) {
                            status = (Boolean) result.getValue() ? "✓" : "✗";
                        } else {
                            status = result.getValue() != null ? result.getValue().toString() : "N/A";
                        }

                        attributeAnalysis.append(String.format(
                                "%s: %s\n",
                                result.getKey(),
                                status
                        ));
                    }

                    attributeAnalysis.append(String.format(
                            "Status: %s\n" +
                                    "------------------------\n",
                            isValid ? "VALID" : "INVALID"
                    ));
                }
            }

            boolean allDocsValid = totalDocs > 0 && validDocs == totalDocs;
            String overallStatus = allDocsValid ?
                    "✅ ALL DOCS HAVE REQUIRED ATTRIBUTES - PASSED" :
                    "❌ SOME DOCS HAVE MISSING ATTRIBUTES - FAILED";

            Allure.addAttachment("Attributes Retrieval Analysis", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary Statistics:\n" +
                            "------------------\n" +
                            "Total Documents: %d\n" +
                            "Valid Documents: %d\n" +
                            "Invalid Documents: %d\n" +
                            "Success Rate: %.2f%%\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    totalDocs,
                    validDocs,
                    (totalDocs - validDocs),
                    totalDocs > 0 ? (validDocs * 100.0 / totalDocs) : 0.0,
                    attributeAnalysis.toString()
            ));

            softAssert.assertTrue(allDocsValid,
                    "All documents should contain required attributes");

        } catch (Exception e) {
            log.error("Attributes retrieval validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Attributes validation failed: " + e.getMessage());
        }

        log.info("=================== Attributes Retrieval Validation Completed ===================");
}
// Helper method to validate field
        private boolean validateField(Object field) {
            if (field == null) return false;
            if (field instanceof String) return !((String) field).trim().isEmpty();
            if (field instanceof Number) return true;
            if (field instanceof Collection) return !((Collection<?>) field).isEmpty();
            return true;
        }

    @Step("Validating Store Redirect")
    public void validateStoreRedirect() {
        log.info("=================== Store Redirect Validation Started ===================");

        try {
            Object storeRedirectObj = ospreyApiResponse.getStoreRedirect();
            int numFound = ospreyApiResponse.getNumFound();
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();

            // Handle null storeRedirect
            String storeRedirect = storeRedirectObj != null ? storeRedirectObj.toString() : null;

            // Log redirect details
            log.info("\nStore Redirect Analysis:");
            log.info("======================");
            log.info("Original Store: " + testData.getOtherParams().get("store"));
            log.info("Redirected Store: " + (storeRedirect != null ? storeRedirect : "No Redirect"));
            log.info("Number of Results: " + numFound);
            log.info("Documents Returned: " + (docs != null ? docs.size() : 0));

            // Validate redirect conditions
            boolean isRedirectRequired = numFound == 0;
            boolean hasValidRedirect = storeRedirect != null && !storeRedirect.isEmpty();
            boolean docsEmpty = docs == null || docs.isEmpty();

            StringBuilder redirectAnalysis = new StringBuilder();
            redirectAnalysis.append(String.format(
                    "Redirect Validation:\n" +
                            "-------------------\n" +
                            "Zero Results: %s\n" +
                            "Has Redirect Store: %s\n" +
                            "Empty Docs: %s\n" +
                            "Redirect Store Value: %s\n",
                    isRedirectRequired ? "✓" : "✗",
                    hasValidRedirect ? "✓" : "✗",
                    docsEmpty ? "✓" : "✗",
                    storeRedirect != null ? storeRedirect : "None"
            ));

            // Validate conditions
            softAssert.assertEquals(numFound, 0, "Number of results should be 0 for redirect");
            softAssert.assertTrue(hasValidRedirect, "Store redirect should not be null or empty when numFound is 0");
            softAssert.assertTrue(docsEmpty, "Document list should be empty");

            String overallStatus = (isRedirectRequired && hasValidRedirect && docsEmpty) ?
                    "✅ STORE REDIRECT VALIDATED - PASSED" :
                    "❌ STORE REDIRECT VALIDATION - FAILED";

            // Add to Allure report
            Allure.addAttachment("Store Redirect Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary:\n" +
                            "--------\n" +
                            "Original Store: %s\n" +
                            "Redirect Store: %s\n" +
                            "Results Found: %d\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    testData.getOtherParams().get("store"),
                    storeRedirect != null ? storeRedirect : "None",
                    numFound,
                    redirectAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Store redirect validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Store redirect validation failed: " + e.getMessage());
        }

        log.info("=================== Store Redirect Validation Completed ===================");
    }

    @Step("Validating Page Number Response")
    public void validatePageNumberResponse() {
        log.info("=================== Page Number Validation Started ===================");

        try {
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();
            int requestedPage = Integer.parseInt(testData.getOtherParams().get("page_number"));
            int recordsPerPage = Integer.parseInt(testData.getOtherParams().get("records_per_page"));

            log.info("\nPagination Analysis:");
            log.info("===================");
            log.info("Total Results Found: " + numFound);
            log.info("Requested Page: " + requestedPage);
            log.info("Records Per Page: " + recordsPerPage);
            log.info("Documents Returned: " + (docs != null ? docs.size() : 0));

            // Calculate expected start and end indices
            int expectedStartIndex = (requestedPage - 1) * recordsPerPage;
            int expectedEndIndex = Math.min(expectedStartIndex + recordsPerPage, numFound);
            int expectedDocCount = expectedEndIndex - expectedStartIndex;

            StringBuilder pageAnalysis = new StringBuilder();
            pageAnalysis.append(String.format(
                    "Page Validation:\n" +
                            "----------------\n" +
                            "Expected Start Index: %d\n" +
                            "Expected End Index: %d\n" +
                            "Expected Document Count: %d\n" +
                            "Actual Document Count: %d\n",
                    expectedStartIndex,
                    expectedEndIndex,
                    expectedDocCount,
                    docs != null ? docs.size() : 0
            ));

            // Validate document count
            if (docs != null) {
                softAssert.assertEquals(docs.size(), expectedDocCount,
                        "Document count should match the expected count for page " + requestedPage);

                // Log document details
                pageAnalysis.append("\nDocument Details:\n");
                for (int i = 0; i < docs.size(); i++) {
                    OspreyApiResponse.Doc doc = docs.get(i);
                    pageAnalysis.append(String.format(
                            "Document %d:\n" +
                                    "Code: %s\n" +
                                    "Name: %s\n" +
                                    "----------------\n",
                            i + 1,
                            doc.getSkuId(),
                            doc.getProductName()
                    ));
                }
            }

            String overallStatus = (docs != null && docs.size() == expectedDocCount) ?
                    "✅ PAGE NUMBER VALIDATION - PASSED" :
                    "❌ PAGE NUMBER VALIDATION - FAILED";

            // Add to Allure report
            Allure.addAttachment("Page Number Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary:\n" +
                            "--------\n" +
                            "Total Results: %d\n" +
                            "Page Number: %d\n" +
                            "Records Per Page: %d\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    numFound,
                    requestedPage,
                    recordsPerPage,
                    pageAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Page number validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Page number validation failed: " + e.getMessage());
        }

        log.info("=================== Page Number Validation Completed ===================");
    }

    @Step("Validating Invalid Page Number Error")
    public void validateInvalidPageNumberError() {
        log.info("=================== Invalid Page Number Error Validation Started ===================");

        try {
            String pageNumber = testData.getOtherParams().get("page_number");
            String errorMessage = ospreyApiResponse.validatePageNumber(pageNumber);
            String expectedError = "{\"detail\": \"page_number must be an integer\"}";

            log.info("\nError Response Analysis:");
            log.info("======================");
            log.info("Page Number: " + pageNumber);
            log.info("Error Message: " + errorMessage);

            // Validate error message
            boolean isValidError = expectedError.equals(errorMessage);

            StringBuilder errorAnalysis = new StringBuilder();
            errorAnalysis.append(String.format(
                    "Error Validation:\n" +
                            "-----------------\n" +
                            "Error Message Match: %s\n" +
                            "Expected: %s\n" +
                            "Actual: %s\n",
                    isValidError ? "✓" : "✗",
                    expectedError,
                    errorMessage
            ));

            // Perform assertion
            softAssert.assertEquals(errorMessage, expectedError,
                    "Error message should match exactly with JSON format");

            String overallStatus = isValidError ?
                    "✅ INVALID PAGE NUMBER ERROR VALIDATED - PASSED" :
                    "❌ INVALID PAGE NUMBER ERROR VALIDATION - FAILED";

            // Add to Allure report
            Allure.addAttachment("Invalid Page Number Error Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Error Details:\n" +
                            "-------------\n" +
                            "Expected Error: %s\n" +
                            "Actual Error: %s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    expectedError,
                    errorMessage,
                    errorAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Invalid page number error validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Error validation failed: " + e.getMessage());
        }

        log.info("=================== Invalid Page Number Error Validation Completed ===================");
    }

    @Step("Validating Invalid Records Offset Error")
    public void validateInvalidRecordsOffsetError() {

        log.info("=================== Invalid Records Offset Validation Started ===================");

        try {
            String recordsOffset = testData.getOtherParams().get("records_offset");
            String actualError = ospreyApiResponse.getErrorMessage(recordsOffset);
            boolean isValidError = false;

            log.info("\nError Response Analysis:");
            log.info("======================");
            log.info("Records Offset: " + recordsOffset);

            // Check if input is boolean
            if (recordsOffset.equalsIgnoreCase("true") || recordsOffset.equalsIgnoreCase("false")) {
              //  String expectedBooleanError = "{\"detail\": \"records_offset must be an integer greater than 0, boolean values are not allowed\"}";
                String expectedBooleanError = String.format(
                        "{\"detail\": [{\"type\": \"int_type\",\"loc\": [\"body\",\"records_offset\"], " + "\"msg\": \"Input should be a valid integer\", \"input\": %s}]}",

                        recordsOffset.toLowerCase());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualJson = mapper.readTree(actualError);
                JsonNode expectedJson = mapper.readTree(expectedBooleanError);
                      //  isValidError = expectedBooleanError.equals(actualError);
                isValidError = actualJson.equals(expectedJson);

                log.info("Input Type: Boolean");
                log.info("Expected Error: " + expectedBooleanError);
                softAssert.assertEquals(actualError, expectedBooleanError,
                        "Error message should match expected format for boolean input");
            }
            // Check if input is string
            else {
               // String expectedStringError = "{\"detail\": \"records_offset must be an integer\"}";
              //  String expectedStringError = "{\"detail\": [{\"type\": \"int_type\", \"loc\": [\"body\", \"records_offset\"], \"msg\": \"Input should be a valid integer\", \"input\": \"no\"}]}";
                String expectedStringError = String.format(
                        "{\"detail\": [{\"type\": \"int_type\", \"loc\": [\"body\", \"records_offset\"], " +
                                "\"msg\": \"Input should be a valid integer\", \"input\": \"%s\"}]}",
                        recordsOffset.toLowerCase());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode actualJson = mapper.readTree(actualError);
                JsonNode expectedJson = mapper.readTree(expectedStringError);
                isValidError = actualJson.equals(expectedJson);

              //  isValidError = expectedStringError.equals(actualError);
                log.info("Input Type: String");
                log.info("Expected Error: " + expectedStringError);
                softAssert.assertEquals(actualError, expectedStringError,
                        "Error message should match expected format for string input");
            }

            log.info("Actual Error: " + actualError);
            log.info("Validation Result: " + (isValidError ? "PASSED" : "FAILED"));

            String overallStatus = isValidError ?
                    "✅ INVALID RECORDS OFFSET VALIDATED - PASSED" :
                    "❌ INVALID RECORDS OFFSET VALIDATION - FAILED";

            Allure.addAttachment("Invalid Records Offset Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Test Details:\n" +
                            "-------------\n" +
                            "Input Value: %s\n" +
                            "Input Type: %s\n" +
                            "Actual Error: %s\n" +
                            "Validation Result: %s",
                    overallStatus,
                    recordsOffset,
                    recordsOffset.equalsIgnoreCase("true") || recordsOffset.equalsIgnoreCase("false") ?
                            "Boolean" : "String",
                    actualError,
                    isValidError ? "PASSED" : "FAILED"
            ));

        } catch (Exception e) {
            log.error("Invalid records offset validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Error validation failed: " + e.getMessage());
        }

        log.info("=================== Invalid Records Offset Validation Completed ===================");
    }

    @Step("Validating Records Offset")
    public void validateRecordsOffset() {
        log.info("=================== Records Offset Validation Started ===================");

        try {
            int recordsOffset = Integer.parseInt(testData.getOtherParams().get("records_offset"));
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();

            log.info("\nOffset Analysis:");
            log.info("================");
            log.info("Total Results Found: " + numFound);
            log.info("Records Offset: " + recordsOffset);
            log.info("Documents Returned: " + (docs != null ? docs.size() : 0));

            // Validate first document is from correct offset
            if (docs != null && !docs.isEmpty()) {
                StringBuilder offsetAnalysis = new StringBuilder();
                offsetAnalysis.append(String.format(
                        "Offset Validation:\n" +
                                "-----------------\n" +
                                "Starting from document: %d\n" +
                                "First Document Details:\n" +
                                "Name: %s\n" +
                                "Code: %s\n",
                        recordsOffset + 1,
                        docs.get(0).getProductName(),
                        docs.get(0).getSkuId()
                ));

                // Validate offset
                boolean isValidOffset = numFound > recordsOffset;
                softAssert.assertTrue(isValidOffset,
                        "Offset should be less than total results found");

                String overallStatus = isValidOffset ?
                        "✅ RECORDS OFFSET VALIDATED - PASSED" :
                        "❌ RECORDS OFFSET VALIDATION - FAILED";

                Allure.addAttachment("Records Offset Validation", String.format(
                        "Overall Status: %s\n\n" +
                                "Summary:\n" +
                                "--------\n" +
                                "Total Results: %d\n" +
                                "Offset: %d\n" +
                                "Documents Returned: %d\n\n" +
                                "Detailed Analysis:\n" +
                                "-----------------\n%s",
                        overallStatus,
                        numFound,
                        recordsOffset,
                        docs.size(),
                        offsetAnalysis.toString()
                ));
            }

        } catch (Exception e) {
            log.error("Records offset validation failed: " + e.getMessage());
            Assert.fail("Records offset validation failed: " + e.getMessage());
        }

        log.info("=================== Records Offset Validation Completed ===================");
    }

    @Step("Validating Records Per Page")
    public void validateRecordsPerPage() {
        log.info("=================== Records Per Page Validation Started ===================");

        try {
            int expectedRecordsPerPage = Integer.parseInt(testData.getOtherParams().get("records_per_page"));
            List<OspreyApiResponse.Doc> docs = ospreyApiResponse.getDocs();
            int numFound = ospreyApiResponse.getNumFound();
            int actualDocsSize = docs != null ? docs.size() : 0;

            log.info("\nRecords Per Page Analysis:");
            log.info("=========================");
            log.info("Total Results Found: " + numFound);
            log.info("Expected Records Per Page: " + expectedRecordsPerPage);
            log.info("Actual Documents Returned: " + actualDocsSize);

            StringBuilder paginationAnalysis = new StringBuilder();
            paginationAnalysis.append(String.format(
                    "Pagination Validation:\n" +
                            "--------------------\n" +
                            "Expected Records: %d\n" +
                            "Actual Records: %d\n" +
                            "Total Available: %d\n",
                    expectedRecordsPerPage,
                    actualDocsSize,
                    numFound
            ));

            // Validate records per page
            boolean isValidCount = (actualDocsSize <= expectedRecordsPerPage) &&
                    (actualDocsSize > 0 || numFound == 0);

            if (docs != null && !docs.isEmpty()) {
                paginationAnalysis.append("\nFirst Document Details:\n");
                paginationAnalysis.append(String.format(
                        "Name: %s\n" +
                                "Code: %s\n",
                        docs.get(0).getProductName(),
                        docs.get(0).getSkuId()
                ));
            }

            softAssert.assertTrue(isValidCount,
                    String.format("Documents returned (%d) should not exceed records_per_page (%d)",
                            actualDocsSize, expectedRecordsPerPage));

            String overallStatus = isValidCount ?
                    "✅ RECORDS PER PAGE VALIDATED - PASSED" :
                    "❌ RECORDS PER PAGE VALIDATION - FAILED";

            Allure.addAttachment("Records Per Page Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Summary:\n" +
                            "--------\n" +
                            "Expected Records Per Page: %d\n" +
                            "Actual Documents Returned: %d\n" +
                            "Total Results Found: %d\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    expectedRecordsPerPage,
                    actualDocsSize,
                    numFound,
                    paginationAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Records per page validation failed: " + e.getMessage());
            Assert.fail("Records per page validation failed: " + e.getMessage());
        }

        log.info("=================== Records Per Page Validation Completed ===================");
    }

    @Step("Validate invalid query type error response")
    public void validateInvalidQueryTypeResponse(Object query,String store) {
        String responseStr = ospreyApiResponse.asString(query.toString(),store);
        String expectedError = "{\"detail\":[{\"type\":\"string_type\",\"loc\":[\"body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":111}]}";

        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
                expectedError.replaceAll("\\s+", ""),
                "Error message should match exactly for invalid query type");

        softAssert.assertTrue(responseStr.contains("Input should be a valid string"),
                "Response should contain invalid string message");
        softAssert.assertTrue(responseStr.contains("string_type"),
                "Response should contain string_type type");
        softAssert.assertTrue(responseStr.contains("body"),
                "Response should contain body location");
        softAssert.assertTrue(responseStr.contains("query"),
                "Response should contain query location");

        log.info("Invalid query type response: " + responseStr);
        Allure.addAttachment("Error Response", responseStr);

        softAssert.assertAll();
    }

    @Step("Validate invalid query type error response")
    public void validateInvalidBooleanQueryTypeResponse(boolean query,String store) {
       // String responseStr = ospreyApiResponse.asString(query.toString(), store);
        String responseStr = ospreyApiResponse.handleBooleanResponse(query,store);

        String expectedError = "{\"detail\":[{\"type\":\"string_type\",\"loc\":[\"body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":true}]}";

        // Determine expected error based on query type
//        if (query instanceof Boolean) {
//            expectedError = String.format("{\"detail\":[{\"type\":\"string_type\",\"loc\":[\"body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":%s}]}", query);
//        } else {
//            expectedError = String.format("{\"detail\":[{\"type\":\"string_type\",\"loc\":[\"body\",\"query\"],\"msg\":\"Input should be a valid string\",\"input\":%d}]}", query);
//        }

        softAssert.assertEquals(responseStr.replaceAll("\\s+", ""),
                expectedError.replaceAll("\\s+", ""),
                "Error message should match exactly for invalid query type");

        // Common validations
        softAssert.assertTrue(responseStr.contains("Input should be a valid string"),
                "Response should contain invalid string message");
        softAssert.assertTrue(responseStr.contains("string_type"),
                "Response should contain string_type type");
        softAssert.assertTrue(responseStr.contains("body"),
                "Response should contain body location");
        softAssert.assertTrue(responseStr.contains("query"),
                "Response should contain query location");

        log.info("Invalid query type response: " + responseStr);
        Allure.addAttachment("Error Response", responseStr);

        softAssert.assertAll();
    }

    @Step("Validating Boolean Page Number Error")
    public void validateBooleanPageNumberError(boolean pageNumber,String store) {

          log.info("=================== Invalid Page Number Validation Started ===================");
        try {
            String invalidPageNumber = testData.getOtherParams().get("page_number");
            String responseStr = ospreyApiResponse.asStringBooleanPageNumber(invalidPageNumber);//, store);

            // Expected error format for both boolean and non-integer values
            String expectedError = "{\"detail\":[{\"type\":\"int_type\",\"loc\":[\"body\",\"page_number\"],\"msg\":\"Input should be a valid integer\",\"input\":" + invalidPageNumber + "}]}";

            log.info("\nError Response Analysis:");
            log.info("======================");
            log.info("Invalid Page Number: " + invalidPageNumber);
            log.info("Error Message: " + responseStr);

            // Remove whitespace and normalize JSON for comparison
//            JsonElement expectedJson = JsonParser.parseString(expectedError);
//            JsonElement actualJson = JsonParser.parseString(responseStr);
            Gson gson = new Gson();
            JsonElement expectedJson = gson.fromJson(expectedError, JsonElement.class);
            JsonElement actualJson = gson.fromJson(responseStr, JsonElement.class);
            boolean isValidError = expectedJson.equals(actualJson);

            StringBuilder errorAnalysis = new StringBuilder();
            errorAnalysis.append(String.format(
                    "Error Validation:\n" +
                            "-----------------\n" +
                            "Error Message Match: %s\n" +
                            "Expected: %s\n" +
                            "Actual: %s\n",
                    isValidError ? "✓" : "✗",
                    expectedError,
                    responseStr
            ));

            // Perform assertion
            softAssert.assertTrue(isValidError,
                    "Error message should match exactly for invalid page number: " + invalidPageNumber);

            String overallStatus = isValidError ?
                    "✅ INVALID PAGE NUMBER ERROR VALIDATED - PASSED" :
                    "❌ INVALID PAGE NUMBER ERROR VALIDATION - FAILED";

            // Add to Allure report
            Allure.addAttachment("Invalid Page Number Error Validation", String.format(
                    "Overall Status: %s\n\n" +
                            "Error Details:\n" +
                            "-------------\n" +
                            "Invalid Input: %s\n" +
                            "Expected Error: %s\n" +
                            "Actual Error: %s\n\n" +
                            "Detailed Analysis:\n" +
                            "-----------------\n%s",
                    overallStatus,
                    invalidPageNumber,
                    expectedError,
                    responseStr,
                    errorAnalysis.toString()
            ));

        } catch (Exception e) {
            log.error("Invalid page number error validation failed: " + e.getMessage());
            Allure.addAttachment("Validation Error", e.getMessage());
            Assert.fail("Error validation failed: " + e.getMessage());
        }

        log.info("=================== Invalid Page Number Validation Completed ===================");
    }



    private double getMinPrice(List<OspreyApiResponse.Doc> products) {
        return products.stream()
                .mapToDouble(OspreyApiResponse.Doc::getPrice)
                .min()
                .orElse(0.0);
    }

    private double getMaxPrice(List<OspreyApiResponse.Doc> products) {
        return products.stream()
                .mapToDouble(OspreyApiResponse.Doc::getPrice)
                .max()
                .orElse(0.0);
    }

    @Step("Validate price sorting response for descending order")
    public void validatePriceDescSortingResponse() {
        log.info("=================== Price Sorting Validation (DESC) Started ===================");

        String sortField = testData.getOtherParams().get("sort_field");
        String sortOrder = testData.getOtherParams().get("sort_order");
        log.info("Validating price sorting - Field: " + sortField + ", Order: " + sortOrder);

        List<OspreyApiResponse.Doc> products = ospreyApiResponse.getDocs();
        boolean isSorted = true;

        log.info("\n Product Price Details (Descending) ");
        StringBuilder priceDetails = new StringBuilder();

        for (int i = 0; i < products.size() - 1; i++) {
            OspreyApiResponse.Doc currentProduct = products.get(i);
            OspreyApiResponse.Doc nextProduct = products.get(i + 1);

            double currentPrice = convertPriceToDouble(currentProduct.getAvgSellingPrice());
            double nextPrice = convertPriceToDouble(nextProduct.getAvgSellingPrice());

            String priceLog = String.format(
                    "Product %d:\n" +
                            "Name: %s\n" +
                            "Price (INR): %.2f\n" +
                            "------------------------",
                    (i + 1), currentProduct.getProductName(), currentPrice
            );
            log.info(priceLog);
            priceDetails.append(priceLog).append("\n");

            // Validate descending order
            if (currentPrice < nextPrice) {
                isSorted = false;
                log.error(String.format("Price sorting error at index %d: %.2f < %.2f",
                        i, currentPrice, nextPrice));
                break;
            }
        }

        // Log last product
        if (!products.isEmpty()) {
            OspreyApiResponse.Doc lastProduct = products.get(products.size() - 1);
            double lastPrice = convertPriceToDouble(lastProduct.getAvgSellingPrice());
            String lastPriceLog = String.format(
                    "Product %d:\n" +
                            "Name: %s\n" +
                            "Price (INR): %.2f\n" +
                            "------------------------",
                    products.size(), lastProduct.getProductName(), lastPrice
            );
            log.info(lastPriceLog);
            priceDetails.append(lastPriceLog);
        }

        // Validation summary
        softAssert.assertTrue(isSorted, "Products should be sorted by price in descending order");

        // Price analysis
        double highestPrice = convertPriceToDouble(products.get(0).getAvgSellingPrice());
        double lowestPrice = convertPriceToDouble(products.get(products.size() - 1).getAvgSellingPrice());

        log.info("\nPrice Range Analysis:");
        log.info(String.format("Highest Price (First Product): %.2f INR", highestPrice));
        log.info(String.format("Lowest Price (Last Product): %.2f INR", lowestPrice));
        log.info("Total Products Analyzed: " + products.size());

        Allure.addAttachment("Price Sorting (DESC) Results", String.format(
                "Test Summary:\n" +
                        "-------------\n" +
                        "Sort Field: %s\n" +
                        "Sort Order: %s\n" +
                        "Total Products: %d\n\n" +
                        "Price Range:\n" +
                        "------------\n" +
                        "Highest Price: %.2f INR\n" +
                        "Lowest Price: %.2f INR\n" +
                        "Price Difference: %.2f INR\n\n" +
                        "Validation:\n" +
                        "----------\n" +
                        "Sorting Status: %s\n" +
                        "Validation Message: Products are %s sorted in descending order",
                sortField,
                sortOrder,
                products.size(),
                highestPrice,
                lowestPrice,
                highestPrice - lowestPrice,
                isSorted ? "PASSED" : "FAILED",
                isSorted ? "correctly" : "not correctly"
        ));

        log.info("Price Sorting Validation (DESC) Completed");
    }
    }




