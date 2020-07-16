package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ImageDao;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@Service
public class APImageService implements ImageService {

    private static final int WIDTH = 1150;

    @Autowired
    private ImageDao imageDao;

    @Override
    public Image get(long id) {
        return imageDao.get(id);
    }

    @Override
    public long create(byte[] image) {
        image = resize(image);
        return imageDao.create(image);
    }

    private byte[] resize(byte[] image) {
        try (InputStream in = new ByteArrayInputStream(image)) {
            BufferedImage original = ImageIO.read(in);
            double aspectRatio = ((double)original.getHeight())/original.getWidth();
            BufferedImage resized = new BufferedImage(WIDTH, (int) (WIDTH*aspectRatio), BufferedImage.SCALE_REPLICATE);
            Graphics2D g2 = (Graphics2D) resized.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(original, 0, 0, WIDTH, (int) (WIDTH*aspectRatio), null);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(resized, "png", baos);
                baos.flush();
                return baos.toByteArray();
            }
        }
        catch (IOException e) {

        }
        return null;
    }
}
