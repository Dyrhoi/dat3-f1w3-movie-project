package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.MovieDataTransferObject;
import facades.Populator;
import utils.EMF_Creator;
import facades.MovieFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final MovieFacade FACADE =  MovieFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    //This is needed...
    public MovieResource() {}

    @Path("_populate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String populate() {
        if(FACADE.count() == 0) {
            Populator.populate();
            return "POPULATED";
        }
        return "ALREADY POPULATED...";
    }

    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("STATUS", "OK");
        return Response.ok().entity(map).build();
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCount() {
        Map<String, Long> map = new HashMap<>();
        map.put("count", FACADE.count());
        return Response.ok().entity(map).build();
    }

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<MovieDataTransferObject> list = MovieDataTransferObject.toList(FACADE.getAll());
        return Response.ok().entity(list).build();
    }

    @Path("id/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") long id) {
        MovieDataTransferObject mdto = new MovieDataTransferObject(FACADE.getById(id));
        return Response.ok().entity(mdto).build();
    }
}