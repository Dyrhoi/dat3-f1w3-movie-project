/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.MovieDataTransferObject;
import entities.Movie;
import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        MovieFacade fe = MovieFacade.getFacadeExample(emf);
        fe.save(new Movie(2005, "The Big Bang", new ArrayList<>(Arrays.asList("Johnny Bravo", "Martin King"))));
        fe.save(new Movie(2020, "Javawakening", new ArrayList<>(Arrays.asList("Tom Hardy", "Ben Awad"))));
    }
    
    public static void main(String[] args) {
        populate();
    }
}
