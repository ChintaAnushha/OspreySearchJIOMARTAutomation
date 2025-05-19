package Com.jioMart.util;

import java.util.Base64;

public class RandomUtil {

    public static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"0123456789"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static String getNameString(int n){
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(string.length() * Math.random());
            sb.append(string.charAt(index));
        }
        return sb.toString();
    }

    public static String getNumericString(int n) {
        String NumericString = "0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(NumericString.length() * Math.random());
            sb.append(NumericString.charAt(index));
        }
        return sb.toString();
    }

    public static String getSpecialCharactersString(int n){
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"+"!@#$%^&*()_"+"abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static String getEncodedString(String s){
        String encodedString = Base64.getEncoder().encodeToString(s.getBytes());
        return  encodedString;
    }

    public static String getValidEmail()
    {
        return "automation_"+getAlphaNumericString(10)+"@ril.com";
    }
    public static String getRandomPageUrl()
    {
        return "https://automation-website.com/"+getAlphaNumericString(10);
    }
}
