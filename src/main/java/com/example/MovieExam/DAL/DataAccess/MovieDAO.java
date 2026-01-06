package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.util.List;

public class MovieDAO implements IMovieDA {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        return List.of();
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {

    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {

    }
}
