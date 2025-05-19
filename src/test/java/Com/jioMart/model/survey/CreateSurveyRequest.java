package Com.jioMart.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import Com.jioMart.model.survey.Questions;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class CreateSurveyRequest {

    @JsonProperty("id")
    public String id;

    @JsonProperty("surveyTitle")
    public String surveyTitle;

    @JsonProperty("description")
    public String description;

    @JsonProperty("isAnonymous")
    public boolean isAnonymous;

    @JsonProperty("documentId")
    public String documentId;

    @JsonProperty("questions")
    public List<Questions> questions;



}
