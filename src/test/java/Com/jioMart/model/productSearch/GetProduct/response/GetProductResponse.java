package Com.jioMart.model.productSearch.GetProduct.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetProductResponse {
    @JsonProperty("title")
    private String title;
    @JsonProperty("attributes")
    private Attributes attributes;
    @JsonProperty("localInventories")
    private List<LocalInventories> localInventories;


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Attributes {

        @JsonProperty("available_stores")
        private AvailableStores available_stores;

        @JsonProperty("inventory_stores")
        private InventoryStores inventory_stores;


    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AvailableStores {

        @JsonProperty("text")
        private List<String> text;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InventoryStores {

        @JsonProperty("text")
        private List<String> text;
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocalInventories {

        @JsonProperty("placeId")
        private String placeId;

        @JsonProperty("priceInfo")
        private PriceInfo priceInfo;

    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceInfo {

        @JsonProperty("currencyCode")
        private String currencyCode;

        @JsonProperty("price")
        private Integer price;

        @JsonProperty("originalPrice")
        private Integer originalPrice;

    }
}
