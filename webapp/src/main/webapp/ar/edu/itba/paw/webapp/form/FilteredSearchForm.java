package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

public class FilteredSearchForm {
    @Size(min=1, max=100)
    private String caption;

    @Range(min=0)
    private int propertyType;

    @Range(min=0)
    private int neighbourhoodId;

    @Range(min=0)
    private int privacyLevel;

    @Range(min=1, max=100)
    private int capacity;

    @DecimalMin("1")
    @DecimalMax("9999999")
    private float minPrice;

    @DecimalMin("1")
    @DecimalMax("9999999")
    private float maxPrice;

    private long[] ruleIds;
    private long[] serviceIds;


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
