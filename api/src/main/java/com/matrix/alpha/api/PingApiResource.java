package com.matrix.alpha.api;

import com.google.gson.JsonObject;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PingApiResource {

    @GET
    @Path("/ping")
    public Response ping() {
        JsonObject object = new JsonObject();
        object.addProperty("data","Pong!");
        return Response.ok().entity("{\"data\":\"Pong!\"}").build();
    }
}
