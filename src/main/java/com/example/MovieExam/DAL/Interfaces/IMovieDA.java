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

}
