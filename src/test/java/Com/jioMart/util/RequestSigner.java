package Com.jioMart.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestSigner {
    public String HTTPVerb;
    public String canonicalReqPath;
    public Map<String, String> queryParam;
    public Map<String, String> headers;
    public String body;
    public String nowDateTime;
    final String kCredentials = "1234567";

    public interface Fields {
        String SEMI_COLON = ";";
        String COLON = ":";
        String EMPTY_STRING = "";
        String EQUALS = "=";
        String AMPERSAND = "&";
    }

    public RequestSigner() {
    }

    public RequestSigner(String HTTPVerb, String canonicalReqPath, Map<String, String>  queryParam, Map<String, String> headers, String body) {
        this.HTTPVerb = HTTPVerb;
        this.canonicalReqPath = canonicalReqPath;
        this.queryParam = queryParam;
        this.headers = headers;
        this.body = body;
    }

    //function hmac
    String hmac(byte[] secretKey, byte[] message) {byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return toHexString(hmacSha256);
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //function hash
    String hash(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return toHexString(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }

    //function encodeRfc3986
    public String encodeRfc3986(String urlEncodedString) {
        if (urlEncodedString == null) {
            return Fields.EMPTY_STRING;
        }
        try {
            return URLEncoder.encode(urlEncodedString, String.valueOf(StandardCharsets.UTF_8))
                    .replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }

    //function encodeRfc3986Full
    String encodeRfc3986Full(String str) {
        return str;
    }

    // function  getDateTime()
    public String getDateTime() {
        if (nowDateTime == null) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            nowDateTime = nowAsISO.replace("-", Fields.EMPTY_STRING).replace(Fields.COLON, Fields.EMPTY_STRING)
                    .replace("'", Fields.EMPTY_STRING);
        }
        return nowDateTime;
    }

    String signature() throws NoSuchAlgorithmException, InvalidKeyException {
        String canonicalStringFromAPI=generateCanonicalString();
        //System.out.println("Final String:: "+canonicalStringFromAPI);
        String strTosign=headers.get("x-fp-date")+"\n"+hash(canonicalStringFromAPI);
        //System.out.println("strTosign:: "+strTosign);
        return "v1:"+hmac(kCredentials.getBytes(UTF_8), strTosign.getBytes(UTF_8));
    }

    public String generateCanonicalString() throws NoSuchAlgorithmException {
        StringBuilder canonicalRequest = new StringBuilder(Fields.EMPTY_STRING);
        canonicalRequest.append(HTTPVerb).append("\n");
        canonicalRequest.append(this.encodeRfc3986Full(canonicalReqPath)).append("\n");
        if (!queryParam.isEmpty()) {
            canonicalRequest.append(canonicalQueryString());
        }else{
            canonicalRequest.append(Fields.EMPTY_STRING);
        }
        canonicalRequest.append("\n");
        canonicalRequest.append(getCanonicalHeaders(headers)).append("\n");
        if (!body.equals(Fields.EMPTY_STRING)) {
            canonicalRequest.append(hash(body));
        } else {
            canonicalRequest.append(hash(Fields.EMPTY_STRING));
        }
        return canonicalRequest.toString();

    }


    public String getCanonicalHeaders(Map<String, String> headers) {
        StringBuilder canonicalHeaderKeyStringBuilder = new StringBuilder(Fields.EMPTY_STRING);
        StringBuilder canonicalHeaderValueStringBuilder = new StringBuilder(Fields.EMPTY_STRING);
        TreeMap<String, String> sortedHeadersMap
                = new TreeMap<String, String>(headers);
        for (String s : sortedHeadersMap.keySet()) {
            canonicalHeaderKeyStringBuilder.append(s);
            canonicalHeaderKeyStringBuilder.append(Fields.COLON);
            canonicalHeaderKeyStringBuilder.append(sortedHeadersMap.get(s));
            canonicalHeaderKeyStringBuilder.append("\n");
        }
        canonicalHeaderKeyStringBuilder.append("\n");
        for (String s : sortedHeadersMap.keySet()) {
            canonicalHeaderValueStringBuilder.append(s);
            canonicalHeaderValueStringBuilder.append(Fields.SEMI_COLON);
        }
        String canonicalHeader=canonicalHeaderValueStringBuilder.substring(0, canonicalHeaderValueStringBuilder.length() - 1);
        return canonicalHeaderKeyStringBuilder.append(canonicalHeader).toString();
    }

    public String canonicalQueryString() {
        StringBuilder canonicalQueryString = new StringBuilder(Fields.EMPTY_STRING);
        TreeMap<String, String> sortedQueryParam
                = new TreeMap<String,String>(queryParam);
        for(String s:sortedQueryParam.keySet()){
            canonicalQueryString.append(s);
            canonicalQueryString.append(Fields.EQUALS);
            canonicalQueryString.append(sortedQueryParam.get(s));
            canonicalQueryString.append(Fields.AMPERSAND);
        }
        String canonicalQueryParamString=canonicalQueryString.substring(0, canonicalQueryString.length() - 1);
        return canonicalQueryParamString;
    }

    public String getxFpDate(){
        return this.getDateTime();
    }
    public String getxFpSignature() {
        String xfpsignature="";
        try {

            xfpsignature=this.signature();
            //System.out.println("x-fp-signature:: "+xfpsignature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return xfpsignature;
    }

    /*public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        Auth auth=new Auth();
        Map<String,String> headers=new HashMap<String,String>();
        headers.put("host","www.jiomarkethostx0.de");
        headers.put("x-fp-date",auth.getDateTime());
        Auth hm = new Auth("GET","/api/service/application/catalog/v1.0/products/blue-straight-jeans-qdvf9on08qx/sizes/","",headers,"");
        System.out.println("x-fp-signature: "+hm.canonicalString());

        //Post call
        Auth auth=new Auth();
        Map<String,String> headers=new HashMap<String,String>();
        headers.put("host","www.jiomarkethostx0.de");
        headers.put("x-fp-date",auth.getDateTime());
        Auth hm = new Auth("POST","/api/service/application/cart/v1.0/detail","",headers,"{\"items\":[{\"item_id\":7500031,\"item_size\":\"XL\",\"quantity\":1,\"article_assignment\":{\"level\":\"multi-companies\",\"strategy\":\"optimal\"},\"seller_id\":1,\"store_id\":1}]}");
        System.out.println("x-fp-signature: "+hm.canonicalString());

    }*/

}
