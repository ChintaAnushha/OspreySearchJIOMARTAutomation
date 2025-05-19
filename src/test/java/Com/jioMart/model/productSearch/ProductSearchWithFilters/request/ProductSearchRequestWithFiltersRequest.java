package Com.jioMart.model.productSearch.ProductSearchWithFilters.request;

import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import Com.jioMart.model.productSearch.ProductSearchWithFilters.response.ProductSearchWithFiltersResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductSearchRequestWithFiltersRequest {

//    @JsonProperty("numFound")
//    public int numFound;

//    @JsonProperty("storeRedirect")
//    public Object storeRedirect; // Can be null, so Object is used.

//    @JsonProperty("docs")
//    public List<Doc> docs;

//    @JsonProperty("facetData")
//    public FacetData facetData;

    @JsonProperty("query")
    private String query;

    @JsonProperty("store")
    private String store;

    @JsonProperty("sort_field")
    private String sortField;

    @JsonProperty("records_per_page")
    private Integer recordsPerPage;
//}
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class Doc {
//
//        @JsonProperty("ageing_double")
//        public double ageingDouble;
//
//        @JsonProperty("averageRating_double")
//        public double averageRatingDouble;
//
//        @JsonProperty("brandName_text_en_mv")
//        public List<String> brandNameTextEnMv;
//
//        @JsonProperty("catalogId")
//        public String catalogId;
//
//        @JsonProperty("code_string")
//        public String codeString;
//
//        @JsonProperty("name_text_en")
//        public String nameTextEn;
//
//        @JsonProperty("price_inr_double")
//        public double priceInrDouble;
//
//        @JsonProperty("discount_double")
//        public double discountDouble;
//
//        @JsonProperty("size_string_mv")
//        public List<String> sizeStringMv;
//
//        @JsonProperty("color_string_mv")
//        public List<String> colorStringMv;
//
//        @JsonProperty("url")
//        public String url;
//
//        @JsonProperty("image")
//        public String image;
//
//        @JsonProperty("gender")
//        public String gender;
//
//        @JsonProperty("rating")
//        public double rating;
//
//        @JsonProperty("l1 category")
//        public String l1Category;
//
//        @JsonProperty("l1l2category_en_string_mv")
//        public List<String> l1l2categoryEnStringMv;
//
//        @JsonProperty("l1l3category_en_string_mv")
//        public List<String> l1l3categoryEnStringMv;
//
//        @JsonProperty("availability")
//        public boolean availability;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class FacetData {
//
//        @JsonProperty("pricerange_inr_string")
//        public List<Facet> priceRangeInrString;
//
//        @JsonProperty("l1l3nestedcategory_en_string_mv")
//        public List<Facet> l1l3NestedCategoryEnStringMv;
//
//        @JsonProperty("brand_string_mv")
//        public List<Facet> brandStringMv;
//
//        @JsonProperty("discount_string")
//        public List<Facet> discountString;
//
//        @JsonProperty("size_string_mv")
//        public List<Facet> sizeStringMv;
//
//        @JsonProperty("color_string_mv")
//        public List<Facet> colorStringMv;
//
//        @JsonProperty("rating_double")
//        public List<Facet> ratingDouble;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class Facet {
//
//        @JsonProperty("name")
//        public String name;
//
//        @JsonProperty("value")
//        public int value;
//    }
//    @JsonProperty("userInfo")
//    public UserInfo userInfo;
//
//    @JsonProperty("jcsVisitorId")
//    public String jcsVisitorId;
    //    @JsonProperty("page")
//    public Integer page;
//    @JsonProperty("query")
//    public String query;
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class FacetData {
//
//        @JsonProperty("name")
//        public String name;
//
//        @JsonProperty("value")
//        public List<String> value;
//
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class NumericFacet {
//
//        @JsonProperty("key")
//        public String key;
//
//        @JsonProperty("max")
//        public String max;
//
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class UserInfo {
//
//        @JsonProperty("userId")
//        public String userId;
//
//    }

}
