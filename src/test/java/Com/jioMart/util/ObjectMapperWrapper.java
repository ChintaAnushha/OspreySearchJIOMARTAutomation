package Com.jioMart.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

@Log4j
public class ObjectMapperWrapper<T> {
    private T obj;
    private T[] objArray;

    private String stringValue;
    private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);


    //constructor to convert string into Java Class Object
    //use getObj() method after calling this constructor
    public ObjectMapperWrapper(String objectString, Class<T> objectClass, SoftAssert softAssert) {
        try {
            this.obj = objectMapper.readValue(objectString,
                    objectClass);
        } catch (IOException e) {
            log.error("Failed readValue as object inside ObjectMapperWrapper:::" + e.getMessage());
            softAssert.assertTrue(false, "Failed! with 200 - Error while processing API httpResponseEntity. Please refer Stack trace: " + e.getMessage());
        }
    }

    //constructor to convert Java Class object into string
    //use getStringValue() method after calling this constructor
    public ObjectMapperWrapper(T object, SoftAssert softAssert) {
        try {
            this.stringValue = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Failed writeValue as string inside ObjectMapperWrapper:::" + e.getMessage());
            softAssert.assertTrue(false, "Failed! - Error while parsing request object. Please refer Stack trace: " + e.getMessage());
        }
    }

    public ObjectMapperWrapper(Class<T[]> objectClass, String objectString, SoftAssert softAssert) {
        try {
            this.objArray = objectMapper.readValue(objectString,
                    objectClass);
        } catch (IOException e) {
            log.error("Failed readValue as object inside ObjectMapperWrapper:::" + e.getMessage());
            softAssert.assertTrue(false, "Failed! with 200 - Error while processing API httpResponseEntity. Please refer Stack trace: " + e.getMessage());
        }
    }

    public T getObj() {
        return obj;
    }

    public T[] getObjArray() {
        return objArray;
    }


    public String getStringValue() {
        return stringValue;
    }
}
