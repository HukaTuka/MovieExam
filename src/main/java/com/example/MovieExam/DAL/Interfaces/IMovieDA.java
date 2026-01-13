package com.example.MovieExam.DAL.Interfaces;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BE.Movie;

import java.util.List;

public interface IMovieDA {

        List<Movie> getAllMovies() throws Exception;

        Movie createMovie(Movie movie) throws Exception;

        void updateMovie(Movie movie) throws Exception;

        void deleteMovie(int movieId) throws Exception;

        Movie getMovieById(int id) throws Exception;

        List<Movie> searchMovies(String query) throws Exception;

        List<Movie> searchMovies(String query, Double minImdbRating, Double minPersonalRating) throws Exception;

}
