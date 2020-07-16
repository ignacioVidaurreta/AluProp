package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

@Path("image")
@Produces(value = {MediaType.APPLICATION_JSON})
public class ImageApiController {

    @Autowired
    private ImageService imageService;

    @Path("/{id}")
    @GET
    public Response image(@PathParam(value = "id") long id) {
        Image image = imageService.get(id);
        if (image == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        byte[] array = imageService.get(id).getImage();
        return  Response.ok(array).build();
    }
}
