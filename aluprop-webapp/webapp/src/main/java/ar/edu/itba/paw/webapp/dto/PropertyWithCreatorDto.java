package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyType;

import javax.persistence.Index;

public class PropertyWithCreatorDto {
    private PropertyWithCreatorDto() {}

    private long id;
    private String caption;
    private String description;
    private PropertyType propertyType;
    private NeighbourhoodDto neighbourhood;
    private boolean privacyLevel;
    private int capacity;
    private float price;
    private ImageDto image;
    private Availability availability;
    private IndexUserDto owner;

    public static PropertyWithCreatorDto fromProperty(Property property) {
        PropertyWithCreatorDto ret = new PropertyWithCreatorDto();
        ret.id = property.getId();
        ret.caption = property.getCaption();
        ret.description = property.getDescription();
        ret.propertyType = property.getPropertyType();
        ret.neighbourhood = NeighbourhoodDto.fromNeighbourhood(property.getNeighbourhood());
        ret.privacyLevel = property.getPrivacyLevel();
        ret.capacity = property.getCapacity();
        ret.price = property.getPrice();
        ret.image = ImageDto.fromImage(property.getMainImage());
        ret.availability = property.getAvailability();
        ret.owner = IndexUserDto.fromUser(property.getOwner());
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getDescription() {
        return description;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public NeighbourhoodDto getNeighbourhood() {
        return neighbourhood;
    }

    public boolean isPrivacyLevel() {
        return privacyLevel;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getPrice() {
        return price;
    }

    public ImageDto getImage() {
        return image;
    }

    public Availability getAvailability() {
        return availability;
    }

    public IndexUserDto getOwner() {
        return owner;
    }
}
