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
public class Choices {

    @JsonProperty("id")
    public String id;

    @JsonProperty("choiceText")
    public String choiceText;

    @JsonProperty("choiceText1")
    public String choiceText1;
}

