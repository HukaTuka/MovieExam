package com.example.MovieExam.BLL;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.util.List;

public class MovieManager {

    private IMovieDA movieDAO;

    public MovieManager(IMovieDA movieDAO){
    this.movieDAO = movieDAO;
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    public Movie createMoviee(Movie NewMovie) throws Exception {
        return movieDAO.createMovie(NewMovie);
    }

    public void deleteMovie(Movie selectedMovie) throws Exception {
        movieDAO.deleteMovie(selectedMovie);
    }

    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.updateMovie(updatedMovie);
    }

}
