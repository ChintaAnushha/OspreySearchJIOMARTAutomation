package Com.jioMart.enums;

public enum YamlKeys {

    //key used to store expected responseCode in YAML file
    EXPECTEDSTATUSCODE("expectedStatusCode"),

    // key used to store expected response in YAML file
    EXPECTEDRESPONSESTRING("expectedResponseString"),

    //key used to store api endpoint in YAML file
    APIENDPOINT("apiEndpoint"),
    GOOGLEAPIENDPOINT("googleApiEndpoint"),
    GOOGLEREQUESTJSONSTRING("googleRequestJsonString"),

    //key used to store requestBody in YAML file
    REQUESTJSONSTRING("requestJsonString");

    YamlKeys(String httpMethodString) {
        this.yamlkey = httpMethodString;
    }

    private String yamlkey;

    @Override
    public String toString() {
        return yamlkey;
    }

}
