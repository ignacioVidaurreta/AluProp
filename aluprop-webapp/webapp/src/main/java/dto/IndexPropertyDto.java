package dto;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collection;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IndexPropertyDto {

    private IndexPropertyDto() { }

    private long id;
    private String caption;
    private String description;
    private PropertyType propertyType;
    private NeighbourhoodDto neighbourhood;
    private boolean privacyLevel;
    private int capacity;
    private float price;
    private Image image;
    private Availability availability;

    public static IndexPropertyDto fromProperty(Property property) {
       IndexPropertyDto ret = new IndexPropertyDto();
       ret.id = property.getId();
       ret.caption = property.getCaption();
       ret.description = property.getDescription();
       ret.propertyType = property.getPropertyType();
       ret.neighbourhood = NeighbourhoodDto.fromNeighbourhood(property.getNeighbourhood());
       ret.privacyLevel = property.getPrivacyLevel();
       ret.capacity = property.getCapacity();
       ret.price = property.getPrice();
       ret.image = property.getMainImage();
       ret.availability = property.getAvailability();
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

    public Image getImage() {
        return image;
    }

    public Availability getAvailability() {
        return availability;
    }
}
