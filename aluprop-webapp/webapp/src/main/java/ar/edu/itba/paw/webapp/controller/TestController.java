package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("test")
@Produces(value = { MediaType.APPLICATION_JSON })
public class TestController {

    @GET
    public Response test() {
        return Response.ok(new int[]{1,2,3}).build();
    }
}

