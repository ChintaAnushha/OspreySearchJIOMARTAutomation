package Com.jioMart.test.OspreySearch;


import Com.jioMart.apiservice.Osprey.OspreyApiServiceFilters;
import Com.jioMart.base.BaseScript;
import org.testng.annotations.Test;

public class SearchWithFiltersTest extends BaseScript{

    OspreyApiServiceFilters ospreyApiServiceFilters = new OspreyApiServiceFilters();

    @Test(priority =0)
    public void OspreyApiServiceSearchWithBrandFilter() {
        ospreyApiServiceFilters.productSearchWithBrandFilter();
    }

    @Test(priority =1)
    public void OspreyApiServiceSearchWithColorFilter() {
       ospreyApiServiceFilters.productSearchWithColorFilter();
    }

    @Test(priority =2)
    public void OspreyApiServiceSearchWithPriceRangeFilter() {
          ospreyApiServiceFilters.productSearchWithPriceRangeFilter();
    }

    @Test(priority =3)
    public void OspreyApiServiceSearchWithDiscountFilter() {
      ospreyApiServiceFilters.productSearchWithDiscountFilter();
    }

    @Test(priority =4)
    public void OspreyApiServiceSearchWithGenderFilter() {
      ospreyApiServiceFilters.productSearchWithGenderFilter();
    }

    @Test(priority = 5)
    public void OspreyApiServiceSearchWithCategoryFilter() {
          ospreyApiServiceFilters.productSearchWithL1L3CategoryFilter();
    }

    @Test(priority = 6)
    public void OspreyApiServiceSearchWithSizeFilter() {
      ospreyApiServiceFilters.productSearchWithSizeFilter();
    }

    @Test(priority = 7)
    public void OspreyApiServiceSearchWithMultipleFilter() {
        ospreyApiServiceFilters.productSearchWithMultipleFilters();
    }

    @Test(priority = 8)
    public void OspreyApiServiceSearchWithOneFilterMultipleValues() {
        ospreyApiServiceFilters.productSearchWithOneFilterMultipleValues();
    }

    @Test(priority = 9)
    public void OspreyApiServiceSearchWithEmptyFilters() {
        ospreyApiServiceFilters.ospreyAPIWithEmptyFilterListType();
    }

    @Test(priority = 10)
    public void OspreyApiServiceSearchWithBooleanAsFilter() {
        ospreyApiServiceFilters.ospreyAPIWithBooleanFilterValue();
    }


}

