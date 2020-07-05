package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Image;

public class ImageDto {

    private ImageDto() { }

    private long id;
    private byte[] image;

    public static ImageDto fromImage(Image image) {
        ImageDto ret = new ImageDto();
        ret.id = image.getId();
        ret.image = image.getImage();
        return ret;
    }

    public long getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    /* package */ void setId(long id) {
        this.id = id;
    }
}
