package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Image;

public interface ImageService {
    Image get(long id);
    long create(byte[] image);
    void resize();
    long checkAspectRatio();
}
