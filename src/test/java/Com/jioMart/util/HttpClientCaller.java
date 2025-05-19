package Com.jioMart.util;

import Com.jioMart.base.BaseScript;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Fazil created on 06-Dec-2022
 */
@Log4j
public class HttpClientCaller extends BaseScript {

    public static HttpResponse performGetAPICall(String apiCompleteURL, String cookie, String authToken) {
        HttpResponse responseBody = null;
        try {
            CloseableHttpClient httpclient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpGet httpGet = new HttpGet(apiCompleteURL);
            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("rfc_code","SKAB");
            if (null != cookie && !cookie.equals(""))
                httpGet.setHeader("Cookie", cookie);
            if (null != authToken && !authToken.equals(""))
                httpGet.setHeader("Authorization", "Bearer " + authToken);
            if(null!= reqHeaderMap && ! reqHeaderMap.isEmpty()) {
                reqHeaderMap.forEach((key, value) ->{
                    httpGet.setHeader(key, value);
                });
            }
            responseBody = httpclient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse performPostAPICall(String apiCompleteURL, String requestBody, String cookie, String authToken) {
        HttpResponse responseBody = null;
        try {
            CloseableHttpClient httpclient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpPost httpPost = new HttpPost(apiCompleteURL);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            if(null!= reqHeaderMap && ! reqHeaderMap.isEmpty()) {
                reqHeaderMap.forEach((key, value) ->{
                    httpPost.setHeader(key, value);
                });
            }
            if (null != cookie && !cookie.equals(""))
                httpPost.setHeader("Cookie", cookie);
            if (null != authToken && !authToken.equals(""))
                httpPost.setHeader("Authorization", "Bearer " + authToken);
            if (null != apiKey && !apiKey.equals(""))
                httpPost.setHeader("apiKey", apiKey);
            if (null != requestBody) {
                StringEntity stringEntity = new StringEntity(requestBody);
                httpPost.setEntity(stringEntity);
            }

            responseBody = httpclient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    // performing http PUT API call
    public static HttpResponse performPutAPICall(String apiCompleteURL, String requestBody, String cookie, String authToken) {
        HttpResponse responseBody = null;
        try {
            CloseableHttpClient httpclient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpPut httpPut = new HttpPut(apiCompleteURL);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            if(null!= reqHeaderMap && ! reqHeaderMap.isEmpty()) {
                reqHeaderMap.forEach((key, value) ->{
                    httpPut.setHeader(key, value);
                });
            }
            if (null != cookie && !cookie.equals(""))
                httpPut.setHeader("Cookie", cookie);
            if (null != authToken && !authToken.equals(""))
                httpPut.setHeader("Authorization", "Bearer " + authToken);
            StringEntity stringEntity = new StringEntity(requestBody);
            httpPut.setEntity(stringEntity);
            responseBody = httpclient.execute(httpPut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse performPatchAPICall(String apiCompleteURL, String requestBody, String cookie, String authToken) {
        HttpResponse responseBody = null;
        try {
            CloseableHttpClient httpclient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpPatch httpPatch = new HttpPatch(apiCompleteURL);
            httpPatch.setHeader("Accept", "application/json");
            httpPatch.setHeader("Content-type", "application/json");
            if(null!= reqHeaderMap && ! reqHeaderMap.isEmpty()) {
                reqHeaderMap.forEach((key, value) ->{
                    httpPatch.setHeader(key, value);
                });
            }
            if (null != cookie && !cookie.equals(""))
                httpPatch.setHeader("Cookie", cookie);
            if (null != authToken && !authToken.equals(""))
                httpPatch.setHeader("Authorization", "Bearer " + authToken);
            if (null != requestBody) {
                StringEntity stringEntity = new StringEntity(requestBody);
                httpPatch.setEntity(stringEntity);
            }
            responseBody = httpclient.execute(httpPatch);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse performDeleteAPICall(String apiCompleteURL, String cookie, String authToken) {
        HttpResponse responseBody = null;
        try {
            CloseableHttpClient httpclient = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
            HttpDelete httpDelete = new HttpDelete(apiCompleteURL);
            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            if(null!= reqHeaderMap && ! reqHeaderMap.isEmpty()) {
                reqHeaderMap.forEach((key, value) ->{
                    httpDelete.setHeader(key, value);
                });
            }
            if (null != cookie && !cookie.equals(""))
                httpDelete.setHeader("Cookie", cookie);
            if (null != authToken && !authToken.equals(""))
                httpDelete.setHeader("Authorization", "Bearer " + authToken);
            responseBody = httpclient.execute(httpDelete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse micrositeAPIGetCall(String fqdn, String endpoint, String cookie) {
        HttpResponse responseBody = null;
        try  {
            log.info("Request Url is::: "+fqdn + endpoint);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(fqdn + endpoint);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            if (null != cookie && !cookie.equals(""))
                httpGet.setHeader("Cookie", cookie);
            responseBody = httpclient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse micrositeAPIPostCall(String fqdn, String endpoint, String requestBody, String cookie) {
        HttpResponse responseBody = null;
        try  {
            log.info("Request Url is::: "+fqdn + endpoint);
            System.out.println(endpoint);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(fqdn + endpoint);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            if (null != cookie && !cookie.equals(""))
                httpPost.setHeader("api-key", cookie);
            StringEntity stringEntity = new StringEntity(requestBody);
            httpPost.setEntity(stringEntity);
            responseBody = httpclient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse micrositeAPIPutCall(String fqdn, String endpoint, String requestBody, String cookie) {
        HttpResponse responseBody = null;
        try  {
            log.info("Request Url is::: "+fqdn + endpoint);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(fqdn + endpoint);
            httpPut.setHeader("Accept", "application/json");
            httpPut.setHeader("Content-type", "application/json");
            if (null != cookie && !cookie.equals(""))
                httpPut.setHeader("Cookie", cookie);
            StringEntity stringEntity = new StringEntity(requestBody);
            httpPut.setEntity(stringEntity);
            responseBody = httpclient.execute(httpPut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseBody;
    }


    public static HttpResponse micrositeAPIDeleteCall(String fqdn, String endpoint, String cookie) {
        HttpResponse responseBody = null;
        try  {
            log.info("Request Url is::: "+fqdn + endpoint);
            System.out.println("endpoint iss:::"+endpoint);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(fqdn + endpoint);
            if (null != cookie && !cookie.equals(""))
                httpDelete.setHeader("Cookie", cookie);
            responseBody = httpclient.execute(httpDelete);
        } catch (Exception e) {
            System.out.println("exception is:::::" +e);
            throw new RuntimeException(e);
        }
        return responseBody;
    }

    public static HttpResponse performPostGoogleAPICall(String apiCompleteURL, String requestBody) {
        HttpResponse httpResponse = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(apiCompleteURL);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            String auth= getGoogleAPIBearerToken();
            httpPost.setHeader("Authorization","Bearer "+auth);
            httpPost.setHeader("X-Goog-User-Project","sr-pr-3pmkt-teamhk-dev-np");
            if (null != requestBody) {
                StringEntity stringEntity = new StringEntity(requestBody);
                httpPost.setEntity(stringEntity);
            }
            httpResponse = httpclient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpResponse;
    }

    public static HttpResponse performGetGoogleAPICall(String apiCompleteURL){
        HttpResponse httpResponse = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiCompleteURL);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json; charset=utf-8");
            String auth= getGoogleAPIBearerToken();
            //log.info("Beareer token: "+auth);
            httpGet.setHeader("Authorization","Bearer "+auth);
            httpGet.setHeader("X-Goog-User-Project","sr-pr-3pmkt-teamhk-dev-np");
            httpResponse = httpclient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return httpResponse;
    }

}



