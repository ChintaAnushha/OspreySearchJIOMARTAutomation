package Com.jioMart.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import Com.jioMart.model.survey.Choices;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = false)
public class Questions {

    @JsonProperty("id")
    public String id;

    @JsonProperty("questionText")
    public String questionText;

    @JsonProperty("questionType")
    public String questionType;

    @JsonProperty("choices")
    public List<Choices> choices;

}
