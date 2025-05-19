package Com.jioMart.util;

import Com.jioMart.base.BaseScript;
import Com.jioMart.enums.HttpMethod;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import Com.jioMart.util.HttpClientCaller;
import java.io.IOException;

@Log4j
public class ApiMethodCalls extends BaseScript {

    private HttpResponse httpResponse = null;

    //Confirming http method type and using HttpClientCaller method accordingly
    public HttpResponse perfromAPICall(String requestBodyString, String apiCompleteURL, HttpMethod httpMethod) {
        log.info("API Request Url is :" + apiCompleteURL);

        if (httpMethod.toString().equals("GET"))
            httpResponse = HttpClientCaller.performGetAPICall(apiCompleteURL, cookie, auth_token);
        if(httpMethod.toString().equals("DELETE")){
            httpResponse = HttpClientCaller.performDeleteAPICall(apiCompleteURL, cookie, auth_token);
        }
        else {
            if (null != requestBodyString &&
                    !(requestBodyString.contains("password") || requestBodyString.contains("Password")))
                log.info("API Request Body is :" + requestBodyString);
            if (httpMethod.toString().equals("POST"))
                httpResponse = HttpClientCaller.performPostAPICall(apiCompleteURL, requestBodyString, cookie, auth_token);

            else if (httpMethod.toString().equals("PUT"))
                httpResponse = HttpClientCaller.performPutAPICall(apiCompleteURL, requestBodyString, cookie, auth_token);
            else if (httpMethod.toString().equals("PATCH"))
                httpResponse = HttpClientCaller.performPatchAPICall(apiCompleteURL, requestBodyString, cookie, auth_token);
        }
        try {
            log.info("API ResponseCODE is : " + httpResponse.getStatusLine().getStatusCode());
            // Storing reponse String in BaseScript
            httpResponseEntity = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
      // log.info("API Response is :" + httpResponseEntity);
        return httpResponse;

    }

    public HttpResponse perfromGoogleAPICall(String requestBodyString, String apiCompleteURL, HttpMethod httpMethod) {
        log.info("API Request Url is :" + apiCompleteURL);
        if (httpMethod.toString().equals("GET"))
            //Do nothing
            httpResponse = HttpClientCaller.performGetGoogleAPICall(apiCompleteURL);
        if(httpMethod.toString().equals("DELETE")){
           //Do nothing
        }
       if (httpMethod.toString().equals("POST")){
           httpResponse = HttpClientCaller.performPostGoogleAPICall(apiCompleteURL, requestBodyString);
       }

        try {
            log.info("API ResponseCODE is : " + httpResponse.getStatusLine().getStatusCode());
            // Storing reponse String in BaseScript
            httpResponseEntity = EntityUtils.toString(httpResponse.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //log.info("API Response is :" + httpResponseEntity);
        return httpResponse;
    }

}
