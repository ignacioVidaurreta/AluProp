package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collection;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PropertyDto {

    private PropertyDto() { }
    private long id;
    private String caption;
    private String description;
    private PropertyType propertyType;
    private NeighbourhoodDto neighbourhood;
    private boolean privacyLevel;
    private int capacity;
    private float price;
    private ImageDto mainImage;
    private Availability availability;
    private IndexUserDto owner;
    private Collection<RuleDto> rules;
    private Collection<ServiceDto> services;
    private Collection<ImageDto> images;

    public static PropertyDto fromProperty(Property property) {
        PropertyDto ret = new PropertyDto();
        ret.id = property.getId();
        ret.caption = property.getCaption();
        ret.description = property.getDescription();
        ret.propertyType = property.getPropertyType();
        ret.neighbourhood = NeighbourhoodDto.fromNeighbourhood(property.getNeighbourhood());
        ret.privacyLevel = property.getPrivacyLevel();
        ret.capacity = property.getCapacity();
        ret.price = property.getPrice();
        ret.mainImage = ImageDto.fromImage(property.getMainImage());
        ret.availability = property.getAvailability();
        ret.owner = IndexUserDto.fromUser(property.getOwner());
        ret.rules = property.getRules().stream().map(RuleDto::fromRule).collect(Collectors.toList());
        ret.services = property.getServices().stream().map(ServiceDto::fromService).collect(Collectors.toList());
        ret.images = property.getImages().stream().map(ImageDto::fromImage).collect(Collectors.toList());
        return ret;
    }

    public Property toProperty(User currentUser) {
        return new Property.Builder().withId(id)
                                    .withCaption(caption)
                                    .withDescription(description)
                                    .withPropertyType(propertyType)
                                    .withNeighbourhood(neighbourhood.toNeighbourhood())
                                    .withPrivacyLevel(privacyLevel)
                                    .withCapacity(capacity)
                                    .withPrice(price)
                                    .withMainImage(new Image(mainImage.getId(), mainImage.getImage()))
                                    .withImages(images.stream().map(i -> new Image(i.getId(), i.getImage())).collect(Collectors.toList()))
                                    .withAvailability(availability)
                                    .withRules(rules.stream().map(r -> new Rule(r.getId(), r.getName())).collect(Collectors.toList()))
                                    .withServices(services.stream().map(r -> new Service(r.getId(), r.getName())).collect(Collectors.toList()))
                                    .withOwner(currentUser)
                                    .build();
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
        return mainImage;
    }

    public Availability getAvailability() {
        return availability;
    }

    public IndexUserDto getOwner() {
        return owner;
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

    public void setMainImageId(long id) {
        mainImage.setId(id);
    }

    public void setImageIds(long[] ids) {
        if (images.size() != ids.length)
            throw new IllegalArgumentException("quantity of ids must match images");
        int i = 0;
        for (ImageDto image : images) {
            image.setId(ids[i]);
            i++;
        }
    }
}
