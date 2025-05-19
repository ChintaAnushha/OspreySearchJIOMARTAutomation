package Com.jioMart.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import Com.jioMart.model.survey.CreateSurveyRequest;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class CreateSurveyResponseWrapper {

    @JsonProperty("data")
    public CreateSurveyRequest data;

    @JsonProperty("error")
    public Boolean error;
}
