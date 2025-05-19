package Com.jioMart.model.productSearch.ProductSearch.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchRequestWrapper {

    @JsonProperty("query")
    private String query;

    @JsonProperty("store")
    private  String store;

    @JsonProperty("records_per_page")
    private Integer recordsperpage;

//    @JsonProperty("jcsVisitorId")
//    private String jcsVisitorId;

}



