package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collection;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PropertyDto {

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
    private Collection<RuleDto> rules;
    private Collection<ServiceDto> services;
    private Collection<ImageDto> images;

    public Property toProperty() {
        return new Property.Builder().withId(id)
                                    .withCaption(caption)
                                    .withDescription(description)
                                    .withPropertyType(propertyType)
                                    .withNeighbourhood(neighbourhood.toNeighbourhood())
                                    .withPrivacyLevel(privacyLevel)
                                    .withCapacity(capacity)
                                    .withPrice(price)
                                    .withMainImage(image)
                                    .withImages(images)
                                    .withAvailability(availability)
                                    .withRules(rules)
                                    .withServices(services);
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

    public Collection<RuleDto> getRules() {
        return rules;
    }

    public Collection<ServiceDto> getServices() {
        return services;
    }

    public Collection<ImageDto> getImages() {
        return images;
    }
}
