package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Range;
import org.springframework.lang.Nullable;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

public class FilteredSearchForm {
    @Nullable
    @Size(min=1, max=100)
    private String description;

    @Nullable
    @Range(min=0)
    private int propertyType;

    @Nullable
    @Range(min=0)
    private int neighbourhoodId;

    @Nullable
    @Range(min=0)
    private int privacyLevel;

    @Nullable
    @Range(min=1, max=100)
    private int capacity;

    @Nullable
    @DecimalMin("1")
    @DecimalMax("9999999")
    private float minPrice;

    @Nullable
    @DecimalMin("1")
    @DecimalMax("9999999")
    private float maxPrice;

    @Nullable
    private long[] ruleIds;

    @Nullable
    private long[] serviceIds;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public int getNeighbourhoodId() {
        return neighbourhoodId;
    }

    public void setNeighbourhoodId(int neighbourhoodId) {
        this.neighbourhoodId = neighbourhoodId;
    }

    public int getPrivacyLevel() {
        return privacyLevel;
    }

    public void setPrivacyLevel(int privacyLevel) {
        this.privacyLevel = privacyLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float price) {
        this.minPrice = price;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float price) {
        this.maxPrice = price;
    }

    public long[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(long[] ruleIds) {
        this.ruleIds = ruleIds;
    }

    public long[] getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(long[] serviceIds) {
        this.serviceIds = serviceIds;
    }

}
