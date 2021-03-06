package rest;

import entities.Movie;
import facades.MovieFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie m1, m2, m3;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory EMF;
    private static MovieFacade FACADE;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        EMF = EMF_Creator.createEntityManagerFactoryForTest();
        FACADE = MovieFacade.getFacadeExample(EMF);

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.getTransaction().commit();

            // Movie(int year, String title, List<String> actors)
            m1 = FACADE.save(new Movie(2005, "The Big Bang", new ArrayList<>(Arrays.asList("Johnny Bravo", "Martin King"))));
            m1 = FACADE.save(new Movie(2007, "The Big Bang - The Extra", new ArrayList<>(Arrays.asList("Johnny Bravo", "Martin King"))));
            m2 = FACADE.save(new Movie(2020, "Javawakening", new ArrayList<>(Arrays.asList("Tom Hardy", "Ben Awad"))));
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/movie").then().statusCode(200);
    }

    //This test assumes the database contains two rows
    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/movie/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(3));
    }
    
    @Test
    public void testGetAll() {
        given()
                .contentType("application/json")
                .get("/movie/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", hasItems("The Big Bang", "Javawakening", "The Big Bang - The Extra"));
    }
    
    @Test
    public void testGetById() {
        given()
                .contentType("application/json")
                .get("/movie/" + m1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("title", equalTo(m1.getTitle()));
    }
    
    @Test
    public void testGetByTitle() {
        String title = "The Big Bang";
        given()
                .contentType("application/json")
                .get("/movie/title/" + title).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("", hasSize(2));
    }
}
