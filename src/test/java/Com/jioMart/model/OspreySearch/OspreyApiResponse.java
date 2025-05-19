package Com.jioMart.model.OspreySearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class  OspreyApiResponse {

    // private ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty("numFound")
    public Integer numFound;

    @JsonProperty("storeRedirect")
    public Object storeRedirect; // Can be null, so Object is used.

    @JsonProperty("docs")
    public List<Doc> docs;


    @JsonProperty("detail")
    public Object detail;

    @JsonProperty("facetData")
    public Map<String, Object> facetData;
//    private boolean query;

//    public Object getQuery() {
//        return query;
//    }


    public OspreyApiResponse() {
        this.numFound = 0;
        this.docs = new ArrayList<>();
    }

    // Safe getter for numFound
    public Integer getNumFound() {
        return numFound != null ? numFound : 0;
    }

    public Map<String, Object> getFacetData() {
        return facetData;
    }
//
    public void setFacetData(Map<String,Object> facetData) {
        this.facetData = facetData;
    }

    public String getDetail() {
        return null;
    }

    public String getErrorMessage(String recordsoffset) {//, Object inputValue) {

        StringBuilder response = new StringBuilder();

        // Check for invalid query type (when query is boolean)
      //  if (recordsoffset != null) {
//            response.append("{\"detail\": [{\"type\": \"int_type\", \"loc\": [\"body\", \"records_offset\"], ")
//                    .append("\"msg\": \"Input should be a valid integer\", \"input\": \"")
//                    .append(recordsoffset)
//                    .append("\"}]}");
//        } else {
//            response.append(docs.toString());
//        }
//        return response.toString();
        //    if (recordsoffset != null) {
            if (recordsoffset != null) {
                String errorType;
                String errorMessage;

                if (recordsoffset.toString().equalsIgnoreCase("true") ||
                        recordsoffset.toString().equalsIgnoreCase("false")) {
                    errorType = "int_type";
                    errorMessage = "Input should be a valid integer";
                    response.append("{\"detail\": [{\"type\": \"")
                            .append(errorType)
                            .append("\", \"loc\": [\"body\", \"records_offset\"], ")
                            .append("\"msg\": \"")
                            .append(errorMessage)
                            .append("\", \"input\": ")
                            .append(recordsoffset.toString().toLowerCase())
                            .append("}]}");
                } else {
                    errorType = "int_type";
                    errorMessage = "Input should be a valid integer";
                    response.append("{\"detail\": [{\"type\": \"")
                            .append(errorType)
                            .append("\",\"loc\": [\"body\",\"records_offset\"], ")
                            .append("\"msg\": \"")
                            .append(errorMessage)
                            .append("\", \"input\": \"")
                            .append(recordsoffset)
                            .append("\"}]}");
                }
            } else {
                response.append(docs.toString());
            }
           //  String inputValue = String.valueOf(recordsOffset);
//        return String.format(
//                "{\"detail\":[{" +
//                        "\"type\":\"int_type\"," +
//                        "\"loc\":[\"body\",\"records_offset\"]," +
//                        "\"msg\":\"Input should be a valid integer\"," +
//                        "\"input\":\"%s\"" +
//                        "}]}",
//                String.valueOf(recordsOffset)
//        );
//        if (detail == null) {//
//            return null;
//        }
//        return "{\"detail\": \"" + detail + "\"}";
//        return String.format(
//                "{\"detail\":[{" +
//                        "\"type\":\"int_type\"," +
//                        "\"loc\":[\"body\",\"records_offset\"]," +
//                        "\"msg\":\"Input should be a valid integer\"," +
//                        "\"input\":\"%s\"" +
//                        "}]}",
//                detail.equals("\\[\\{.*\\}\\]"), "no");
        return response.toString();
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    //  public Map<String, List<FacetItem>> getFacetData() { return facetData; }
    //  public void setFacetData(Map<String, List<FacetItem>> facetData) { this.facetData = facetData; }


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Doc {

        @JsonProperty("applicable_regions")
        public List<String> applicableRegions;

        @JsonProperty("average_rating")
        public double averageRating;

        @JsonProperty("avg_discount")
        public double avgDiscount;

        @JsonProperty("avg_selling_price")
        public Double avgSellingPrice;

        @JsonProperty("brand_id")
        public Long brandId;

        @JsonProperty("brand_name")
        public String brandName;

        @JsonProperty("color")
        public String color;

        @JsonProperty("dealCode")
        public String dealCode;

        @JsonProperty("dealType")
        public String dealType;

        @JsonProperty("deal_price")
        public String dealPrice;

        @JsonProperty("discount")
        public String discount;

        @JsonProperty("discountPercentage")
        public Double discountPercentage;

        @JsonProperty("discountType")
        public String discountType;

        @JsonProperty("in_stock")
        public String inStock;

        @JsonProperty("inventory_stores")
        public List<String> inventoryStores;

        @JsonProperty("inventory_stores_3p")
        public List<String> inventoryStores3p;

        @JsonProperty("is_deal_price")
        public String isDealPrice;

        @JsonProperty("manufacturer_id")
        public Long manufacturerId;

        @JsonProperty("manufacturer_name")
        public String manufacturerName;

        @JsonProperty("max_qty_in_order")
        public Integer maxQtyInOrder;

        @JsonProperty("medias")
        public Map<String, String> medias;

        @JsonProperty("mrp")
        public Double mrp;

        @JsonProperty("number_of_user_ratings")
        public String numberOfUserRatings;

        @JsonProperty("popularity_score")
        public String popularityScore;

        @JsonProperty("price")
        public float price;

        @JsonProperty("sku_id")
        public String skuId;

        @JsonProperty("product_attributes")
        public  Map<String, Object>  productAttributes;

        @JsonProperty("product_name")
        public String productName;

        @JsonProperty("product_option_id")
        public String productOptionId;

        @JsonProperty("seller_id")
        public String sellerId;

        @JsonProperty("seller_name")
        public String sellerName;

        @JsonProperty("sku_id_internal")
        public String skuIdInternal;

        public float getPrice() {
            return price;
        }

        private double getPriceValue(Object price) {
            if (price == null) {
                return 0.0;
            }
            if (price instanceof Number) {
                return ((Number) price).doubleValue();
            }
            if (price instanceof String) {
                try {
                    return Double.parseDouble(((String) price).replaceAll("[^\\d.]", ""));
                } catch (NumberFormatException e) {
                    System.out.println("Failed to parse price: " + price);
                    return 0.0;
                }
            }
            return 0.0;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        @JsonProperty("product_category_hierarchy")
        public CategoryHierarchy productCategoryHierarchy;

        public Map<String, List<String>> getProductCategoryHierarchy() {
            return productCategoryHierarchy != null ? productCategoryHierarchy.toMap() : new HashMap<>();
        }

        public void setProductCategoryHierarchy(CategoryHierarchy productCategoryHierarchy) {
            this.productCategoryHierarchy = productCategoryHierarchy;
        }

        @JsonProperty("product_category_hierarchy_ids")
        public CategoryHierarchy.CategoryHierarchyIds productCategoryHierarchyIds;
    }


    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductAttributes {
        @JsonProperty("seller_ids")
        public List<String> sellerIds;

        @JsonProperty("seller_names")
        public List<String> sellerNames;

        @JsonProperty("vertical_code")
        public String verticalCode;

        @JsonProperty("mart_availability")
        public String martAvailability;

        @JsonProperty("facet_specification")
        public Map<String, Map<String, List<String>>> facetSpecification;

        @JsonProperty("product_type")
        public String productType;

        @JsonProperty("inventory_check")
        public String inventoryCheck;

        @JsonProperty("is_sodexo_eligible")
        public boolean isSodexoEligible;

        @JsonProperty("formulation_type")
        public String formulationType;

        @JsonProperty("is_tradein")
        public String isTradein;

        @JsonProperty("doorstep_finance_enabled")
        public int doorstepFinanceEnabled;

//        @JsonProperty("product_category_hierarchy")
//        public CategoryHierarchy productCategoryHierarchy;


//        @JsonProperty("product_category_hierarchy_ids")
//        public CategoryHierarchyIds productCategoryHierarchyIds;

//        @JsonProperty("product_name")
//        public String productName;
//
//        @JsonProperty("product_option_id")
//        public String productOptionId;
//
//        @JsonProperty("seller_id")
//        public String sellerId;
//
//        @JsonProperty("seller_name")
//        public String sellerName;
//
//        @JsonProperty("sku_id")
//        public String skuId;
//
//        @JsonProperty("sku_id_internal")
//        public String skuIdInternal;

        @JsonProperty("url_slug")
        public String urlSlug;
    }

    @Data
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CategoryHierarchy {

        @JsonProperty("l0")
        public List<String> l0;

        @JsonProperty("l1")
        public List<String> l1;

        @JsonProperty("l2")
        public List<String> l2;

        @JsonProperty("l3")
        public List<String> l3;

        @JsonProperty("l4")
        public List<String> l4;


        public Map<String, List<String>> toMap() {
            Map<String, List<String>> hierarchyMap = new HashMap<>();
            if (l0 != null && !l0.isEmpty()) hierarchyMap.put("l0", l0);
            if (l1 != null && !l1.isEmpty()) hierarchyMap.put("l1", l1);
            if (l2 != null && !l2.isEmpty()) hierarchyMap.put("l2", l2);
            if (l3 != null && !l3.isEmpty()) hierarchyMap.put("l3", l3);
            if (l4 != null && !l4.isEmpty()) hierarchyMap.put("l4", l4);
            return hierarchyMap;
        }
//
//        // Getters and setters
//        public List<String> getL0() { return l0; }
//        public void setL0(List<String> l0) { this.l0 = l0; }
//        public List<String> getL1() { return l1; }
//        public void setL1(List<String> l1) { this.l1 = l1; }
//        public List<String> getL2() { return l2; }
//        public void setL2(List<String> l2) { this.l2 = l2; }
//        public List<String> getL3() { return l3; }
//        public void setL3(List<String> l3) { this.l3 = l3; }
//        public List<String> getL4() { return l4; }
//        public void setL4(List<String> l4) { this.l4 = l4; }

        // Add these getter methods
        public List<String> getL0Categories() {
            return Optional.ofNullable(l0).orElse(Collections.emptyList());
        }

        public List<String> getL1Categories() {
            return Optional.ofNullable(l1).orElse(Collections.emptyList());
        }

        public List<String> getL2Categories() {
            return Optional.ofNullable(l2).orElse(Collections.emptyList());
        }

        public List<String> getL3Categories() {
            return Optional.ofNullable(l3).orElse(Collections.emptyList());
        }

        public List<String> getL4Categories() {
            return Optional.ofNullable(l4).orElse(Collections.emptyList());
        }


        @Data
        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CategoryHierarchyIds {
            @JsonProperty("l0")
            public List<Integer> l0;

            @JsonProperty("l1")
            public List<Integer> l1;

            @JsonProperty("l2")
            public List<Integer> l2;

            @JsonProperty("l3")
            public List<Integer> l3;

            @JsonProperty("l4")
            public List<Integer> l4;
        }


        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FacetItem {

            @JsonProperty("price_range")
            public List<Facet> priceRange;

            @JsonProperty("l0")
            public List<Facet> l0;

            @JsonProperty("l1")
            public List<Facet> l1;

            @JsonProperty("l2")
            public List<Facet> l2;

            @JsonProperty("l3")
            public List<Facet> l3;

            @JsonProperty("l4")
            public List<Facet> l4;

            @JsonProperty("avg_selling_price")
            public List<Facet> avgSellingPrice;

            @JsonProperty("avg_discount")
            public List<Facet> avgDiscount;

            @JsonProperty("brand_string_mv")
            public List<Facet> brandStringMv;

            @JsonProperty("brickprimarycolor_en_string_mv")
            public List<Facet> verticalColorFamilyEnStringMv;

            @JsonProperty("rating_string_mv")
            public List<Facet> ratingStringMv;

            @JsonProperty("l1l2category_en_string_mv")
            public List<Facet> l1l2categoryEnStringMv;

            @JsonProperty("occasion_en_string_mv")
            public List<Facet> occasionEnStringMv;

            @JsonProperty("tags_string_mv")
            public List<Facet> tagsStringMv;
        }

        @Getter
        @Setter
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Facet {

            @JsonProperty("name")
            public String name;

            @JsonProperty("value")
            public int value;

            private Map<String, List<FacetItem>> facetData;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Integer getValue() {
                return value;
            }

            public void setValue(Integer value) {
                this.value = value;
            }

        }
    }


    public String asString(String query, String filters) {
        StringBuilder response = new StringBuilder();

        // Check for invalid query type (when query is a number)
        if (query != null && query.matches("\\d+")) {
            response.append("{\"detail\": [{\"type\": \"string_type\", \"loc\": [\"body\", \"query\"], ")
                    .append("\"msg\": \"Input should be a valid string\", \"input\": ")
                    .append(query)
                    .append("}]}");
        }
//        // Check for missing store
//        else if (store == null || store.trim().isEmpty()) {
//            response.append("{\"detail\": [{\"type\": \"missing\", \"loc\": [\"body\", \"store\"], ")
//                    .append("\"msg\": \"Field required\", \"input\": {\"query\": \"")
//                    .append(query != null ? query : "")
//                    .append("\", \"sort_field\": \"relevance\", \"records_per_page\": 2}}]}");
//        }
        // Check for missing applicable_regions filter
        else if (filters == null || filters.trim().isEmpty()) { //.noneMatch(f -> "applicable_regions".equals(f.getFieldName()))) {
//        else if (filters == null || !hasApplicableRegionsFilter(filters)) {
//        else if (applicableRegions == null || applicableRegions.isEmpty()) {
            response.append("{\"detail\": [{")
                    .append("\"type\": \"value_error\", ")
                    .append("\"loc\": [\"body\"], ")
                    .append("\"msg\": \"Value error, A filter with fieldName 'applicable_regions' is required.\", ")
                    .append("\"input\": {\"query\": \"")
                    .append(query != null ? query : "")
                    .append("\"}, ")
                    .append("\"ctx\": {\"error\": {}}")
                    .append("}]}");
        }
        // Check for empty query
        else if (query == null || query.trim().isEmpty()) {
            response.append("{\"detail\": \"Search query cannot be empty or invalid.\"}");
        }
        // Check for invalid store
        else if (docs == null || docs.isEmpty()) {
            response.append("{\"detail\": \"Invalid store\"}");
        }
        // Valid case
        else {
            response.append(docs.toString());
        }

        return response.toString();
    }

    // Add this helper method in the class
    private boolean hasApplicableRegionsFilter(List<OspreyApiRequest.Filter> filters) {
        if (filters == null || filters.isEmpty()) {
            return false;
        }

        for (OspreyApiRequest.Filter filter : filters) {
            if (filter != null && "applicable_regions".equals(filter.getFieldName())) {
                return true;
            }
        }
        return false;
    }

    public String asFilterString(String filters, String store) {
        StringBuilder response = new StringBuilder();
        if (filters != null) {
            response.append("{\"detail\": [{")
                    .append("\"type\": \"list_type\", ")
                    .append("\"loc\": [\"body\", \"filters\"], ")
                    .append("\"msg\": \"Input should be a valid list\", ")
                    .append("\"input\": ");

            if (filters.equalsIgnoreCase("true")) {
                response.append("true");
            } else if (filters.equalsIgnoreCase("false")) {
                response.append("false");
            } else if (filters.matches("^[0-9]+$")) {
                response.append(filters);
            } else {
                response.append("\"").append(filters).append("\"");
            }

            response.append("}]}");
        }

        // Check for missing store
        else if (store == null || store.trim().isEmpty()) {
            response.append("{\"detail\": \"Invalid store\"}");
        }
        // Check for invalid store
        else if (docs == null || docs.isEmpty()) {
            response.append("{\"detail\": \"Invalid store\"}");
        }
        // Valid case
        else {
            response.append(docs.toString());
        }

        return response.toString();
    }

    public String handleBooleanResponse(Boolean query, String store) {
        StringBuilder response = new StringBuilder();

        // Check for invalid query type (when query is boolean)
        if (query != null) {
            response.append("{\"detail\": [{\"type\": \"string_type\", \"loc\": [\"body\", \"query\"], ")
                    .append("\"msg\": \"Input should be a valid string\", \"input\": ")
                    .append(query)
                    .append("}]}");
        } else {
            response.append(docs.toString());
        }
        return response.toString();
    }

    public String handleBooleanFilterResponse(Boolean filter, String store) {
        StringBuilder response = new StringBuilder();

        if (filter != null) {
            response.append("{\"detail\": [{")
                    .append("\"type\": \"list_type\", ")
                    .append("\"loc\": [\"body\", \"filters\"], ")
                    .append("\"msg\": \"Input should be a valid list\", ")
                    .append("\"input\": ")
                    .append(filter)
                    .append("}]}");
        } else {
            response.append(docs.toString());
        }

        return response.toString();
    }

    public static boolean isValidPageNumber(String pageNumber) {
        return pageNumber == null || pageNumber.matches("\\d+");
    }

    public static String validatePageNumber(String pageNumber) {
        if (!isValidPageNumber(pageNumber)) {
            return "{\"detail\": \"page_number must be an integer\"}";
        }
        return null;
    }

//    public String asStringBooleanPageNumber(Boolean pageNumber) { //, String store) {
//        StringBuilder response = new StringBuilder();
//
//        if (pageNumber != null) {
//            response.append("{\"detail\": \"page_number must be an integer greater than 0, boolean values are not allowed\"}");
//        } else {
//            response.append(docs.toString());
//        }
//
//        return response.toString();
//    }
public String asStringBooleanPageNumber(String pageNumber) {
    StringBuilder response = new StringBuilder();

    if (pageNumber != null) {
        response.append("{\"detail\":[{")
                .append("\"type\":\"int_type\",")
                .append("\"loc\":[\"body\",\"page_number\"],")
                .append("\"msg\":\"Input should be a valid integer\",")
                .append("\"input\":").append(pageNumber)
                .append("}]}");
    } else {
        response.append(docs.toString());
    }

    return response.toString();
}
}
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class PriceInrDouble {
//
//        @JsonProperty("price")
//        public Float price;
//        @JsonProperty("original_price")
//        public Float originalPrice;
//    }
//}
//@Getter
//@Setter
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class OspreyApiResponse {
//
//    @JsonProperty("getdocs")
//    public List<Object> getdocs = null;
//
//   // public List<Object> getdocs() {
//
//
////    @JsonProperty("results")
////    public List<Results> results = null;
//
//    public static class getDocs {
//
//        @JsonProperty("Docs")
//        public List<Doc> docs;
//
//    }
//    // public List<Object> getDocs() {
////   public List<getDocs> results = null;
//    //   }
//
//    public class BrandStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrandStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickcollarEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickcollarEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricknecklineEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricknecklineEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickpatternEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickpatternEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricksleeveEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricksleeveEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricksportEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BricksportEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickstyletypeEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class BrickstyletypeEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class DiscountString {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class DiscountString__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class Doc {
//        @JsonProperty("ageing_double")
//        public Float ageingDouble;
//
//        @JsonProperty("averageRating_double")
//        public Float averageRatingDouble;
//
//        @JsonProperty("brandName_text_en_mv")
//        public List<String> brandNameTextEnMv;
//
//        @JsonProperty("brandtype_en_string_mv")
//        public Object brandtypeEnStringMv;
//
//        @JsonProperty("brandtype_string_mv")
//        public List<String> brandtypeStringMv;
//
//        @JsonProperty("brickprimarycolor_en_string_mv")
//        public List<String> brickprimarycolorEnStringMv;
//
//        @JsonProperty("brickstyletype_en_string_mv")
//        public List<String> brickstyletypeEnStringMv;
//
//        @JsonProperty("catalogCommercialType_string")
//        public String catalogCommercialTypeString;
//
//        @JsonProperty("catalogId")
//        public String catalogId;
//
//        @JsonProperty("category_string_mv")
//        public List<String> categoryStringMv;
//
//        @JsonProperty("code_string")
//        public String codeString;
//
//        @JsonProperty("colorGroup_string")
//        public String colorGroupString;
//
//        @JsonProperty("commercialType_string")
//        public String commercialTypeString;
//
//        @JsonProperty("creationtime_date")
//        public String creationtimeDate;
//
//        @JsonProperty("discountType_string")
//        public String discountTypeString;
//
//        @JsonProperty("dod_enabled_int")
//        public Integer dodEnabledInt;
//
//        @JsonProperty("dod_price_inr_double")
//        public Float dodPriceInrDouble;
//
//        @JsonProperty("earlyAccessPrice_float")
//        public Object earlyAccessPriceFloat;
//
//        @JsonProperty("exactdiscount_int")
//        public Integer exactdiscountInt;
//
//        @JsonProperty("exclusiveTill_date")
//        public Object exclusiveTillDate;
//
//        @JsonProperty("extraImages_string_mv")
//        public List<String> extraImagesStringMv;
//
//        @JsonProperty("futureListPrice_inr_double")
//        public Object futureListPriceInrDouble;
//
//        @JsonProperty("golivedays_int")
//        public Integer golivedaysInt;
//
//        @JsonProperty("id")
//        public String id;
//
//        @JsonProperty("img-174Wx218H_string")
//        public String img174Wx218HString;
//
//        @JsonProperty("img-286Wx359H_string")
//        public String img286Wx359HString;
//
//        @JsonProperty("img-288Wx360H_string")
//        public String img288Wx360HString;
//
//        @JsonProperty("img-473Wx593H_string")
//        public String img473Wx593HString;
//
//        @JsonProperty("img-thumbNail_string")
//        public String imgThumbNailString;
//
//        @JsonProperty("inStockCount_double")
//        public Float inStockCountDouble;
//
//        @JsonProperty("inStockFlag_boolean")
//        public Boolean inStockFlagBoolean;
//
//        @JsonProperty("inTakeMarginPerc_double")
//        public Float inTakeMarginPercDouble;
//
//        @JsonProperty("isCodDisabled_boolean")
//        public Boolean isCodDisabledBoolean;
//
//        @JsonProperty("l1category_string_mv")
//        public List<String> l1categoryStringMv;
//
//        @JsonProperty("l1l2category_en_string_mv")
//        public List<String> l1l2categoryEnStringMv;
//
//        @JsonProperty("l1l3category_en_string_mv")
//        public List<String> l1l3categoryEnStringMv;
//
//        @JsonProperty("l2category_string_mv")
//        public List<String> l2categoryStringMv;
//
//        @JsonProperty("l3category_string_mv")
//        public List<String> l3categoryStringMv;
//
//        @JsonProperty("mrp_double")
//        public Float mrpDouble;
//
//        @JsonProperty("name_text_en")
//        public String nameTextEn;
//
//        @JsonProperty("numUserRatings_int")
//        public Integer numUserRatingsInt;
//
//        @JsonProperty("outfitPicture_string")
//        public Object outfitPictureString;
//
//        @JsonProperty("planningCategory_text_en")
//        public String planningCategoryTextEn;
//
//        @JsonProperty("priceValue_inr_double")
//        public Float priceValueInrDouble;
//        @JsonProperty("price_inr_double")
//        public Float priceInrDouble;
//        @JsonProperty("productToggleOn_string")
//        public Object productToggleOnString;
//        @JsonProperty("seasonCodeYear_string")
//        public Object seasonCodeYearString;
//        @JsonProperty("segmentProductTagValidity_date")
//        public Object segmentProductTagValidityDate;
//        @JsonProperty("segmentProductTag_string")
//        public Object segmentProductTagString;
//        @JsonProperty("size_en_string")
//        public String sizeEnString;
//        @JsonProperty("tradeDiscountedValue_double")
//        public Float tradeDiscountedValueDouble;
//        @JsonProperty("url_en_string")
//        public String urlEnString;
//        @JsonProperty("wasPrice_double")
//        public Float wasPriceDouble;
//        @JsonProperty("webcategory_string_mv")
//        public List<String> webcategoryStringMv;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class Example {
//        @JsonProperty("numFound")
//        public Integer numFound;
//        @JsonProperty("storeRedirect")
//        public Object storeRedirect;
//        @JsonProperty("docs")
//        public List<Doc> docs;
//        @JsonProperty("facetData")
//        public FacetData facetData;
//        @JsonProperty("brickcollar_en_string_mv")
//        public List<BrickcollarEnStringMv__1> brickcollarEnStringMv;
//        @JsonProperty("pricerange_inr_string")
//        public List<PricerangeInrString__1> pricerangeInrString;
//        @JsonProperty("l1l3nestedcategory_en_string_mv")
//        public List<L1l3nestedcategoryEnStringMv__1> l1l3nestedcategoryEnStringMv;
//        @JsonProperty("bricksport_en_string_mv")
//        public List<BricksportEnStringMv__1> bricksportEnStringMv;
//        @JsonProperty("brand_string_mv")
//        public List<BrandStringMv__1> brandStringMv;
//        @JsonProperty("occasion_en_string_mv")
//        public List<OccasionEnStringMv__1> occasionEnStringMv;
//        @JsonProperty("segmentcharacter_en_string_mv")
//        public List<SegmentcharacterEnStringMv__1> segmentcharacterEnStringMv;
//        @JsonProperty("brickpattern_en_string_mv")
//        public List<BrickpatternEnStringMv__1> brickpatternEnStringMv;
//        @JsonProperty("verticalfabrictype_en_string_mv")
//        public List<VerticalfabrictypeEnStringMv__1> verticalfabrictypeEnStringMv;
//        @JsonProperty("brickneckline_en_string_mv")
//        public List<BricknecklineEnStringMv__1> bricknecklineEnStringMv;
//        @JsonProperty("rating_string_mv")
//        public List<RatingStringMv__1> ratingStringMv;
//        @JsonProperty("golivedays_int")
//        public List<GolivedaysInt__1> golivedaysInt;
//        @JsonProperty("genderfilter_en_string_mv")
//        public List<GenderfilterEnStringMv__1> genderfilterEnStringMv;
//        @JsonProperty("verticalsizegroupformat_en_string_mv")
//        public List<VerticalsizegroupformatEnStringMv__1> verticalsizegroupformatEnStringMv;
//        @JsonProperty("bricksleeve_en_string_mv")
//        public List<BricksleeveEnStringMv__1> bricksleeveEnStringMv;
//        @JsonProperty("verticalsizegroup_en_string_mv")
//        public List<VerticalsizegroupEnStringMv__1> verticalsizegroupEnStringMv;
//        @JsonProperty("brickstyletype_en_string_mv")
//        public List<BrickstyletypeEnStringMv__1> brickstyletypeEnStringMv;
//        @JsonProperty("discount_string")
//        public List<DiscountString__1> discountString;
//        @JsonProperty("verticalcolorfamily_en_string_mv")
//        public List<VerticalcolorfamilyEnStringMv__1> verticalcolorfamilyEnStringMv;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class FacetData {
//        @JsonProperty("brickcollar_en_string_mv")
//        public List<BrickcollarEnStringMv> brickcollarEnStringMv;
//        @JsonProperty("pricerange_inr_string")
//        public List<PricerangeInrString> pricerangeInrString;
//        @JsonProperty("l1l3nestedcategory_en_string_mv")
//        public List<L1l3nestedcategoryEnStringMv> l1l3nestedcategoryEnStringMv;
//        @JsonProperty("bricksport_en_string_mv")
//        public List<BricksportEnStringMv> bricksportEnStringMv;
//        @JsonProperty("brand_string_mv")
//        public List<BrandStringMv> brandStringMv;
//        @JsonProperty("occasion_en_string_mv")
//        public List<OccasionEnStringMv> occasionEnStringMv;
//        @JsonProperty("segmentcharacter_en_string_mv")
//        public List<SegmentcharacterEnStringMv> segmentcharacterEnStringMv;
//        @JsonProperty("brickpattern_en_string_mv")
//        public List<BrickpatternEnStringMv> brickpatternEnStringMv;
//        @JsonProperty("verticalfabrictype_en_string_mv")
//        public List<VerticalfabrictypeEnStringMv> verticalfabrictypeEnStringMv;
//        @JsonProperty("brickneckline_en_string_mv")
//        public List<BricknecklineEnStringMv> bricknecklineEnStringMv;
//        @JsonProperty("rating_string_mv")
//        public List<RatingStringMv> ratingStringMv;
//        @JsonProperty("golivedays_int")
//        public List<GolivedaysInt> golivedaysInt;
//        @JsonProperty("genderfilter_en_string_mv")
//        public List<GenderfilterEnStringMv> genderfilterEnStringMv;
//        @JsonProperty("verticalsizegroupformat_en_string_mv")
//        public List<VerticalsizegroupformatEnStringMv> verticalsizegroupformatEnStringMv;
//        @JsonProperty("bricksleeve_en_string_mv")
//        public List<BricksleeveEnStringMv> bricksleeveEnStringMv;
//        @JsonProperty("verticalsizegroup_en_string_mv")
//        public List<VerticalsizegroupEnStringMv> verticalsizegroupEnStringMv;
//        @JsonProperty("brickstyletype_en_string_mv")
//        public List<BrickstyletypeEnStringMv> brickstyletypeEnStringMv;
//        @JsonProperty("discount_string")
//        public List<DiscountString> discountString;
//        @JsonProperty("verticalcolorfamily_en_string_mv")
//        public List<VerticalcolorfamilyEnStringMv> verticalcolorfamilyEnStringMv;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class GenderfilterEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class GenderfilterEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class GolivedaysInt {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class GolivedaysInt__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class L1l3nestedcategoryEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class L1l3nestedcategoryEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class OccasionEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class OccasionEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class PricerangeInrString {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class PricerangeInrString__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class RatingStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class RatingStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class SegmentcharacterEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class SegmentcharacterEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalcolorfamilyEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalcolorfamilyEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalfabrictypeEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalfabrictypeEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalsizegroupEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalsizegroupEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalsizegroupformatEnStringMv {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public class VerticalsizegroupformatEnStringMv__1 {
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("value")
//        public Integer value;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
