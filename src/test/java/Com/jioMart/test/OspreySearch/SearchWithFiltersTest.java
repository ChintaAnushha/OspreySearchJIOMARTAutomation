package Com.jioMart.test.OspreySearch;


import Com.jioMart.apiservice.Osprey.OspreyApiServiceFilters;
import Com.jioMart.base.BaseScript;
import org.testng.annotations.Test;

public class SearchWithFiltersTest extends BaseScript{

    OspreyApiServiceFilters ospreyApiServiceFilters = new OspreyApiServiceFilters();

    @Test(priority =1)
    public void OspreyApiServiceSearchWithBrandFilter() {
        ospreyApiServiceFilters.productSearchWithBrandFilter();
    }

    @Test(priority = 0)
    public void OspreyApiServiceSearchWithColorFilter() {
       ospreyApiServiceFilters.productSearchWithColorFilter();
    }

    @Test(priority =2)
    public void OspreyApiServiceSearchWithPriceRangeFilter() {
          ospreyApiServiceFilters.productSearchWithPriceRangeFilter();
    }

    @Test(priority =3)
    public void OspreyApiServiceSearchWithDiscountDesc() {
      ospreyApiServiceFilters.productSearchWithDiscountFilter();
    }

    @Test(priority =4)
    public void OspreyApiServiceSearchWithGenderFilter() {
      ospreyApiServiceFilters.productSearchWithGenderFilter();
    }

    @Test(priority = 5)
    public void OspreyApiServiceSearchWithL1l3CategoryFilter() {
          ospreyApiServiceFilters.productSearchWithL1L3CategoryFilter();
    }

    @Test(priority = 4)
    public void OspreyApiServiceSearchWithL0CategoryFilter() {
        ospreyApiServiceFilters.productSearchWithL0CategoryFilter();
    }

    @Test(priority = 5)
    public void OspreyApiServiceSearchWithL1CategoryFilter() {
        ospreyApiServiceFilters.productSearchWithL1CategoryFilter();
    }

    @Test(priority = 6)
    public void OspreyApiServiceSearchWithL2CategoryFilter() {
        ospreyApiServiceFilters.productSearchWithL2CategoryFilter();
    }

    @Test(priority = 7)
    public void OspreyApiServiceSearchWithL3CategoryFilter() {
        ospreyApiServiceFilters.productSearchWithL3CategoryFilter();
    }

    @Test(priority = 8)
    public void OspreyApiServiceSearchWithL4CategoryFilter() {
        ospreyApiServiceFilters.productSearchWithL4CategoryFilter();
    }

    @Test(priority = 11)
    public void OspreyApiServiceSearchWithSizeFilter() {
      ospreyApiServiceFilters.productSearchWithSizeFilter();
    }

    @Test(priority = 9)
    public void OspreyApiServiceSearchWithMultipleFilter() {
        ospreyApiServiceFilters.productSearchWithMultipleFilters();
    }

    @Test(priority = 10)
    public void OspreyApiServiceSearchWithOneFilterMultipleValues() {
        ospreyApiServiceFilters.productSearchWithOneFilterMultipleValues();
    }

    @Test(priority = 11)
    public void OspreyApiServiceSearchWithEmptyFilters() {
        ospreyApiServiceFilters.ospreyAPIWithEmptyFilterListType();
    }

    @Test(priority = 12)
    public void OspreyApiServiceSearchWithBooleanAsFilter() {
        ospreyApiServiceFilters.ospreyAPIWithBooleanFilterValue();
    }
}

