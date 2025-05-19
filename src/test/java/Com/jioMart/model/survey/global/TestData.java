package Com.jioMart.model.survey.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class TestData {

    @JsonProperty("urls")
    public Map<String, String> urls = null;

    @JsonProperty("headersMap")
    public Map<String, String> headersMap = null;

    @JsonProperty("otherParams")
    public Map<String, String> otherParams = null;


}
