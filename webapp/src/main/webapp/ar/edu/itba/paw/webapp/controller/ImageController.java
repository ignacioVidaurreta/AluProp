package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "images/{id}", method = RequestMethod.GET)
    public @ResponseBody byte[]
    get(@PathVariable long id) throws IOException {
        InputStream x = imageService.get(id).getImage();
        return IOUtils.toByteArray(x);
    }
}