package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ImageService;
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
        final int oneHour = 3600;
        byte[] image = imageService.get(id).getImage();
        final CacheControl cache = new CacheControl();
        cache.setNoTransform(false);
        cache.setMaxAge(oneHour);
        final Date expireDate = new Date(new Date().getTime() + oneHour);
        final Response.ResponseBuilder builder = Response.ok(image).header(HttpHeaders.CONTENT_TYPE, "image/*")
                                                        .cacheControl(cache).expires(expireDate);
        if (image == null)
            return builder.status(Response.Status.NOT_FOUND).build();
        return builder.build();
    }

    @POST
    public Response create(byte[] image) {
        imageService.create(image);
        return Response.ok().build();
    }
}
