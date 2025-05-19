package Com.jioMart.test.OspreySearch.categories;

import io.qameta.allure.LabelAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TestCategories {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LabelAnnotation(name = "category")
    public @interface PassedTests {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @LabelAnnotation(name = "category")
    public @interface FailedTests {
    }
}