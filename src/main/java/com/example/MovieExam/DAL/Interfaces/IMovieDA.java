package com.example.MovieExam.DAL.Interfaces;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BE.Movie;

import java.util.List;

public interface IMovieDA {

        List<Movie> getAllMovies() throws Exception;

        Movie createMovie(Movie newMovie) throws Exception;

        void updateMovie(Movie movie) throws Exception;

        void deleteMovie(Movie movie) throws Exception;

        List<Category> getAllCategories() throws Exception;

        Category createCategory(Category newCategory) throws Exception;

        void updateCategory(Category category) throws Exception;

        void deleteCategory(Category category) throws Exception;
}
