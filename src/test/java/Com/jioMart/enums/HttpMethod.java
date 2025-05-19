package Com.jioMart.enums;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    UPDATE("UPDATE"),
    PATCH("PATCH");

    HttpMethod(String httpMethodString) {
        this.httpMethodString = httpMethodString;
    }

    private String httpMethodString;

    @Override
    public String toString() {
        return httpMethodString;
    }

}
