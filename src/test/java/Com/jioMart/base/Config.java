package Com.jioMart.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fazil created on 06-Dec-2022
 */

public class Config {

    public static Map<String, String> getProp(String getEnv) {
        switch (getEnv) {
            case "SIT":
                return Config.sitConfig();
            case "UAT":
                return Config.uatConfig();
            default:
                break;
        }
        return null;

    }

    public static Map<String, String> sitConfig() {

        Map<String, String> map = new HashMap<>();
        map.put("URL", "https://osprey-sit.services.ajio.com/jiomart");  //https://osprey-sit.services.ajio.com/jiomart
        map.put("database", "jdbc:mysql://");
        map.put("connectorURL", "com.mysql.jdbc.Driver");
        return map;

    }
    public static Map<String, String> uatConfig() {

        Map<String, String> map = new HashMap<>();
        map.put("URL", "https://osprey-sit.services.ajio.com/jiomart");
        map.put("database", "jdbc:mysql://");
        map.put("connectorURL", "com.mysql.jdbc.Driver");
        return map;

    }


}