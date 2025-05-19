package Com.jioMart.model.productSearch.ProductSearchWithFilters.response;

import Com.jioMart.model.OspreySearch.OspreyApiResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchWithFiltersResponse {

    @JsonProperty("numFound")
    public Integer numFound;

    @JsonProperty("storeRedirect")
    public Object storeRedirect; // Can be null, so Object is used.

    @JsonProperty("docs")
    public List<Doc> docs;

    @JsonProperty("facetData")
    public FacetData facetData;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Doc {

        @JsonProperty("ageing_double")
        public Double ageingDouble;

        @JsonProperty("averageRating_double")
        public Double averageRatingDouble;

        @JsonProperty("brandName_text_en_mv")
        public List<String> brandNameTextEnMv;

        @JsonProperty("catalogId")
        public String catalogId;

        @JsonProperty("code_string")
        public String codeString;

        @JsonProperty("name_text_en")
        public String nameTextEn;

        @JsonProperty("price_inr_double")
        public Double priceInrDouble;

        @JsonProperty("discount_double")
        public Double discountDouble;

        @JsonProperty("size_string_mv")
        public List<String> sizeStringMv;

        @JsonProperty("color_string_mv")
        public List<String> colorStringMv;

        @JsonProperty("url")
        public String url;

        @JsonProperty("image")
        public String image;

        @JsonProperty("gender")
        public String gender;

        @JsonProperty("rating")
        public Double rating;

        @JsonProperty("l1 category")
        public String l1Category;

        @JsonProperty("l1l2category_en_string_mv")
        public List<String> l1l2categoryEnStringMv;

        @JsonProperty("l1l3category_en_string_mv")
        public List<String> l1l3categoryEnStringMv;

        @JsonProperty("availability")
        public Boolean availability;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FacetData {

        @JsonProperty("pricerange_inr_string")
        public List<Facet> priceRangeInrString;

        @JsonProperty("l1l3nestedcategory_en_string_mv")
        public List<Facet> l1l3NestedCategoryEnStringMv;

        @JsonProperty("brand_string_mv")
        public List<Facet> brandStringMv;

        @JsonProperty("discount_string")
        public List<Facet> discountString;

        @JsonProperty("size_string_mv")
        public List<Facet> sizeStringMv;

        @JsonProperty("color_string_mv")
        public List<Facet> colorStringMv;

        @JsonProperty("rating_double")
        public List<Facet> ratingDouble;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Facet {

        @JsonProperty("name")
        public String name;

        @JsonProperty("value")
        public Integer value;
    }
}



//    @JsonProperty("results")
//    public List<Results> results = null;
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Results {
//        @JsonProperty("hits")
//        public List<Hits> hits;
//
//
//        @JsonProperty("facets")
//        public Facets facets;
//
//        @JsonProperty("query")
//        public String query;
//
//        @JsonProperty("nbHits")
//        public String nbHits;
//
//        @JsonProperty("nbPages")
//        public String nbPages;
//
//        @JsonProperty("hitsPerPage")
//        public String hitsPerPage;
//
//        @JsonProperty("attributionToken")
//        public String attributionToken;
//
//        @JsonProperty("nextPageToken")
//        public String nextPageToken;
//
//        @JsonProperty("facets_stats")
//        public FacetStats facets_stats;
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Hits {
//        @JsonProperty("id")
////
//        public String id;
//        @JsonProperty("name")
//        public String name;
//        @JsonProperty("title")
//        public String title;
//        @JsonProperty("primaryProductId")
//        public String primaryProductId;
//        @JsonProperty("categoriesList")
//        public List<String> categoriesList;
//        @JsonProperty("brandsList")
//        public List<String> brandsList;
//        @JsonProperty("priceInfo")
//        public PriceInfo priceInfo;
//        @JsonProperty("images")
//        public List<Image> images;
//
//        @JsonProperty("localInventories")
//        public Object localInventories;
//        @JsonProperty("description")
//        public String description;
//        @JsonProperty("attributes")
//        public Attributes attributes;
//        @JsonProperty("uri")
//        public String uri;
//
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class PriceInfo {
//
//        @JsonProperty("price")
//        public Float price;
//
//        @JsonProperty("original_price")
//        public Float originalPrice;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Value {
//        @JsonProperty("value")
//        public String value;
//
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class Image {
//
//        @JsonProperty("uri")
//        public String uri;
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class Attributes {
//
//        @JsonProperty("category_level_l4")
//        public CategoryLevelL4 categoryLevelL4;
//        @JsonProperty("discount")
//        public Discount discount;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class CategoryLevelL4 {
//
//        @JsonProperty("textList")
//        public List<String> textList;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class Discount {
//
//        @JsonProperty("numbersList")
//        public List<Float> numbersList;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Facets {
//        @JsonProperty("category_tree_l2")
//        public Category_tree_l2 category_tree_l2;
//
//        @JsonProperty("category_tree_l0")
//        public Category_tree_l0 category_tree_l0;
//
//        @JsonProperty("attributes.discount")
//        public AttributesDiscount1 attributesDiscount;
//
//        @JsonProperty("category_tree_l1")
//        public Category_tree_l1 category_tree_l1;
//
//        @JsonProperty("brands")
//        public Brands brands;
//
//        @JsonProperty("attributes.category_level_l4")
//        public AttributesCategory_level_l4 attributesCategory_level_l4;
//
//        @JsonProperty("price")
//        public Price1 price;
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Category_tree_l2 {
//        @JsonProperty("Category > Electronics > Accessories")
//        public int category_Electronics_Accessories;
//        @JsonProperty("Category > Electronics > TV & Speaker")
//        public int category_Electronics_TV_Speaker;
//        @JsonProperty("Category > Electronics > resQ Services")
//        public int category_Electronics_resQServices;
//        @JsonProperty("Category > Electronics > Mobiles & Tablets")
//        public int category_Electronics_Mobiles_Tablets;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Category_tree_l0 {
//        @JsonProperty("Category")
//        public int category;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class AttributesDiscount1 {
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Category_tree_l1 {
//        @JsonProperty("Category > Electronics")
//        public int Category_Electronics;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Brands {
//        @JsonProperty("Apple")
//        public int Apple;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class AttributesCategory_level_l4 {
//        @JsonProperty("Smartphones")
//        public int smartphones;
//        @JsonProperty("Chargers & Adapters")
//        public int chargers_Adapters;
//        @JsonProperty("TV Cables & Adapters")
//        public int tV_Cables_Adapters;
//        @JsonProperty("Protection Plans")
//        public int protection_Plans;
//        @JsonProperty("Mobile Case, Back Cover")
//        public int mobile_Case_Back_Cover;
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Price1 {
//    }
//
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class FacetStats {
//        @JsonProperty("attributes.discount")
//        public AttributesDiscount attributesDiscount;
//
//        @JsonProperty("price")
//        public Price price;
//
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class AttributesDiscount {
//        @JsonProperty("min")
//        public Integer min;
//
//        @JsonProperty("max")
//        public Integer max;
//
//
//    }
//
//    @Getter
//    @Setter
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonIgnoreProperties(ignoreUnknown = false)
//    public static class Price {
//        @JsonProperty("min")
//        public Integer min;
//
//        @JsonProperty("max")
//        public Integer max;
//
//    }
//}
