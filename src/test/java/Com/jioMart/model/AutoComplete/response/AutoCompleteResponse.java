package Com.jioMart.model.AutoComplete.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = false)
public class AutoCompleteResponse {

    @JsonProperty("results")
    public List<Result> results=null;


    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class Result {

        @JsonProperty("hits")
        public List<Hit> hits;
        @JsonProperty("query")
        public String query;

    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class Hit {

        @JsonProperty("categories")
        public List<Value> categories;
        @JsonProperty("brands")
        public List<Value> brands;
        @JsonProperty("query")
        public String query;

    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = false)
    public static class Value {
        @JsonProperty("value")
        public String value;

    }
}
