package Com.jioMart.model.AutoComplete.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class AutoCompleteRequest {

    @JsonProperty("query")
    private String query;

    @JsonProperty("visitorId")
    private String visitorId;

    @JsonProperty("partnerId")
    private String partnerId;
}
