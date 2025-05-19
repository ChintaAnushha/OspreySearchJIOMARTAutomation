package Com.jioMart.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class GetAllSurveysResponseWrapper {

    @JsonProperty("data")
    public String data;

    @JsonProperty("error")
    public Boolean error;

    @JsonProperty("id")
    public Integer id;

}
