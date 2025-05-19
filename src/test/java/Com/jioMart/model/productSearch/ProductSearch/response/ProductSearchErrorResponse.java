package Com.jioMart.model.productSearch.ProductSearch.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = false)
public class ProductSearchErrorResponse {

    @JsonProperty("error")
    public Boolean error;
    @JsonProperty("timestamp")
    public String timestamp;
    @JsonProperty("code")
    public String code;
    @JsonProperty("message")
    public String message;
    @JsonProperty("stacktrace")
    public Object stacktrace;
    @JsonProperty("data")
    public String data;
}
