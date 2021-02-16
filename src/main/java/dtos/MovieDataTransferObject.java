/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import entities.Movie;

import javax.persistence.ElementCollection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class MovieDataTransferObject {
    private Long id;
    private int year;
    private String title;
    private List<String> actors;

    public MovieDataTransferObject(Movie m) {
        this.id = m.getId();
        this.year = m.getYear();
        this.title = m.getTitle();
        this.actors = m.getActors();
    }
    
    public static List<MovieDataTransferObject> toList(List<Movie> movies){
        List<MovieDataTransferObject> list = new ArrayList<>();
        movies.forEach(m -> list.add(new MovieDataTransferObject(m)));
        return list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "MovieDataTransferObject{" +
                "id=" + id +
                ", year=" + year +
                ", title='" + title + '\'' +
                ", actors=" + actors +
                '}';
    }
}
