package Com.jioMart.model.OspreySearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OspreyApiRequest {

//    @JsonProperty("query")
//    public String query;

    @JsonProperty("query")
    public Object query;

    @JsonProperty("store")
    public String store;

    @JsonProperty("records_per_page")
    public int recordsperpage;

    @JsonProperty("sort_field")
    public String sortfield;

//    @JsonProperty("filters")
//    public List<Filter> filters;

  //  @JsonSerialize(using = EmptyFilterSerializer.class)
    @JsonProperty("filters")
    public List<Filter> filters;

    @JsonProperty("sort_order")
    public String sortOrder;

    @JsonProperty("page_number")
    public Object pageNumber;

    @JsonProperty("applicable_regions")
    public List<String> applicableRegions;

    @JsonProperty("start")
    public int start;

    public void setStart(int start) {
        this.start = start;
    }

    public List<String> getApplicableRegions() {
        return applicableRegions != null ? applicableRegions : new ArrayList<>();
    }

    public void setApplicableRegions(List<String> regions) {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        Filter regionFilter = new Filter();
        regionFilter.setFieldName("applicable_regions");
        regionFilter.setValues(regions);
        filters.add(regionFilter);
    }

    @JsonProperty("disable_facets")
    public boolean disableFacets;

    @JsonProperty("enable_facet_values_count")
    public boolean enableFacetValuesCount;

    @JsonProperty("attributes_to_retrieve")
    public List<String> attributesToRetrieve;

    @JsonProperty("records_offset")
    public Object recordsOffset;

    public void setSortParameters(String field, String order) {
        if (field.equalsIgnoreCase("price")) {
            this.sortfield = "avg_selling_price";  // Use the correct field name for price sorting
        } else {
            this.sortfield = field;
        }

        // Normalize sort order
        if ("asc".equalsIgnoreCase(order)) {
            this.sortOrder = "asc";
        } else if ("desc".equalsIgnoreCase(order)) {
            this.sortOrder = "desc";
        } else {
            throw new IllegalArgumentException("Sort order must be either 'asc' or 'desc'");
        }
    }

    public void setApplicableRegionFilter(String RegionValues) {
        List<Filter> filters = new ArrayList<>();
        Filter ApplicableRegion = new Filter();
        ApplicableRegion.setFieldName("applicable_regions");

        if (RegionValues != null && !RegionValues.isEmpty()) {
            ApplicableRegion.setValues(Collections.singletonList(RegionValues));
        } else {
            throw new IllegalArgumentException("Brand values cannot be null or empty");

        }
        this.filters = filters;
        filters.add(ApplicableRegion);
    }

    public void setBrandFilter(String brandValues) {
        List<Filter> filters = new ArrayList<>();
        Filter brandFilter = new Filter();
        brandFilter.setFieldName("brand_string_mv");

        if (brandValues != null && !brandValues.isEmpty()) {
             brandFilter.setValues(Collections.singletonList(brandValues));
        } else {
            throw new IllegalArgumentException("Brand values cannot be null or empty");

        }
        this.filters = filters;
        filters.add(brandFilter);
    }

    public void setColorFilter(String colorValues) {
        List<Filter> filters = new ArrayList<>();
        Filter colorFilter = new Filter();
        colorFilter.setFieldName("verticalcolorfamily_en_string_mv");

        if (colorValues != null && !colorValues.isEmpty()) {
            colorFilter.setValues(Collections.singletonList(colorValues));
        } else {
            throw new IllegalArgumentException("Color values cannot be null or empty");
        }

        filters.add(colorFilter);
        this.filters = filters;
    }

    public void setPriceRangeFilter(String fieldName, String priceRange) {
        List<Filter> filters = new ArrayList<>();
        Filter priceFilter = new Filter();
        priceFilter.setFieldName(fieldName);
        priceFilter.setValues(Collections.singletonList(priceRange));
        filters.add(priceFilter);
        this.filters = filters;
    }

    public void setDiscountFilter(String fieldName, String discountRange) {
        List<Filter> filters = new ArrayList<>();
        Filter discountFilter = new Filter();
        discountFilter.setFieldName(fieldName);
        discountFilter.setValues(Collections.singletonList(discountRange));
        filters.add(discountFilter);
        this.filters = filters;
    }

    public void setGenderFilter(String fieldName, String gender) {
        List<Filter> filters = new ArrayList<>();
        Filter genderFilter = new Filter();
        genderFilter.setFieldName(fieldName);
        genderFilter.setValues(Collections.singletonList(gender));
        filters.add(genderFilter);
        this.filters = filters;
    }

    public void setL1L3CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l1l3Filter = new Filter();
        l1l3Filter.setFieldName(fieldName);
        l1l3Filter.setValues(Collections.singletonList(category));
        filters.add(l1l3Filter);
        this.filters = filters;
    }

    public void setL0CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l0Filter = new Filter();
        l0Filter.setFieldName(fieldName);
        l0Filter.setValues(Collections.singletonList(category));
        filters.add(l0Filter);
        this.filters = filters;
    }

    public void setL1CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l1Filter = new Filter();
        l1Filter.setFieldName(fieldName);
        l1Filter.setValues(Collections.singletonList(category));
        filters.add(l1Filter);
        this.filters = filters;
    }

    public void setL2CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l2Filter = new Filter();
        l2Filter.setFieldName(fieldName);
        l2Filter.setValues(Collections.singletonList(category));
        filters.add(l2Filter);
        this.filters = filters;
    }

    public void setL3CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l3Filter = new Filter();
        l3Filter.setFieldName(fieldName);
        l3Filter.setValues(Collections.singletonList(category));
        filters.add(l3Filter);
        this.filters = filters;
    }

    public void setL4CategoryFilter(String fieldName, String category) {
        List<Filter> filters = new ArrayList<>();
        Filter l4Filter = new Filter();
        l4Filter.setFieldName(fieldName);
        l4Filter.setValues(Collections.singletonList(category));
        filters.add(l4Filter);
        this.filters = filters;
    }

    public void setSizeFilter(String fieldName, String size) {
        List<Filter> filters = new ArrayList<>();
        Filter sizeFilter = new Filter();
        sizeFilter.setFieldName(fieldName);
        sizeFilter.setValues(Collections.singletonList(size));
        filters.add(sizeFilter);
        this.filters = filters;
    }

    public void setRecordsOffset(String value) {
        try {
            this.recordsOffset = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            this.recordsOffset = value;
        }
    }

    public void setQuery(Object query) {
        this.query = query;
    }

    public Object getRecordsOffset() {
        return recordsOffset;
    }

    public void setDisableFacets(boolean disableFacets) {
        this.disableFacets = disableFacets;
    }

    public boolean isDisableFacets() {
        return disableFacets;
    }

    public void setEnableFacetCounts(boolean enableFacetValuesCount) {
        this.enableFacetValuesCount = enableFacetValuesCount;
    }

    public boolean isEnableFacetCounts() {
        return enableFacetValuesCount;
    }

    public String getErrorMessage() {
        String inputValue = String.valueOf(recordsOffset);
        return String.format(
                "{\"detail\":[{" +
                        "\"type\":\"int_type\"," +
                        "\"loc\":[\"body\",\"records_offset\"]," +
                        "\"msg\":\"Input should be a valid integer\"," +
                        "\"input\":\"%s\"" +
                        "}]}",
                inputValue
        );
    }

    public void setAttributesToRetrieve(List<String> attributesToRetrieve) {
        this.attributesToRetrieve = attributesToRetrieve;
    }

    public List<String> getAttributesToRetrieve() {
        return attributesToRetrieve;
    }

    public void setPageNumber(Object pageNumber) {
        this.pageNumber = pageNumber;
    }


    public void setPageNumberValue(String value) {
        try {
            this.pageNumber = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            this.pageNumber = value;
        }
    }


    public static class Filter {

        @JsonProperty("fieldName")
        public String fieldName;

        @JsonProperty("values")
        public List<String> values;

        public Filter() {}



        @JsonAnySetter
        public void handleUnknown(String key, Object value) {
            if ("fieldName".equals(key)) {
                this.fieldName = (String) value;
            } else if ("values".equals(key)) {
                if (value instanceof List) {
                    this.values = (List<String>) value;
                } else if (value instanceof String) {
                    this.values = Collections.singletonList((String) value);
                }
            }
        }

        // Getters and setters
        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }


        public void setValues(List<String> values) {
            this.values = values;
        }

        // Convert to Map
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("fieldName", fieldName);
            map.put("values", values);
            return map;
        }

        private Map<String, Object> filterMap;

        public Filter(String fieldName, List<String> values) {
            this.fieldName = fieldName;
            this.values = values;
            this.filterMap = new HashMap<>();
            this.filterMap.put("fieldName", fieldName);
            this.filterMap.put("values", values);
        }

        public Map<String, Object> getFilterMap() {
            return filterMap;
        }

        public void setFilterMap(Map<String, Object> filterMap) {
            this.filterMap = filterMap;
        }

        public List<String> getValues() {
            return values;
        }

    }

    public List<Filter> getFilters() {
        return filters;
    }

@SuppressWarnings("unchecked")
public void setFilters(List<Filter> filters) {
    this.filters = new ArrayList<>(filters);
    }

}




