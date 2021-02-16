package facades;

import entities.Movie;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {

    private static EntityManagerFactory EMF;
    private static MovieFacade FACADE;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactoryForTest();
        FACADE = MovieFacade.getFacadeExample(EMF);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.getTransaction().commit();

            // Movie(int year, String title, List<String> actors)
            FACADE.save(new Movie(2005, "The Big Bang", new ArrayList<>(Arrays.asList("Johnny Bravo", "Martin King"))));
            FACADE.save(new Movie(2020, "Javawakening", new ArrayList<>(Arrays.asList("Tom Hardy", "Ben Awad"))));
        } finally {
            em.close();
        }
    }

    @Test
    public void testCount() {
        assertEquals(2, FACADE.count());
    }

}
