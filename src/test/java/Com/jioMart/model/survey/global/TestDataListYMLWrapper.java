package Com.jioMart.model.survey.global;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import Com.jioMart.model.survey.global.TestData;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class TestDataListYMLWrapper {
  //  private Map<String, TestData> testDataMap = new HashMap<>();

    @JsonProperty("testDataMap")
    public Map<String, TestData> testDataMap = null;

//    public TestDataListYMLWrapper() {
//        // Empty constructor required for YAML parsing
//    }

//    public TestDataListYMLWrapper(Map<String, TestData> testDataMap) {
//        this.testDataMap = testDataMap;
//    }

    // Getter and Setter
//    public Map<String, TestData> getTestDataMap() { return testDataMap; }
//    public void setTestDataMap(Map<String, TestData> testDataMap) { this.testDataMap = testDataMap; }

}
