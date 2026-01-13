package com.example.MovieExam.GUI.Model;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.BLL.MovieManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MovieModel {

    private ObservableList<Movie> moviesToBeViewed;
    private MovieManager movieManager;

    public MovieModel() throws Exception {
        this.movieManager = new MovieManager();
        this.moviesToBeViewed = FXCollections.observableArrayList();
        loadAllMovies();
    }

    public ObservableList<Movie> getObservableMovies() {
        return moviesToBeViewed;
    }

    public void loadAllMovies() throws Exception {
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public void createMovie(String name, List<Category> categories, double imdbRating, double personalRating, String fileLink) throws Exception {
        movieManager.createMovie(name, categories, imdbRating, personalRating, fileLink);
        loadAllMovies();
    }

    public void updateMovie(Movie movie) throws Exception {
        movieManager.updateMovie(movie);
        loadAllMovies();
    }

    public void deleteMovie(Movie movie, boolean deleteFile) throws Exception {
        movieManager.deleteMovie(movie.getId(), deleteFile);
        loadAllMovies();
    }

    public void searchMovies(String query) throws Exception {
        searchMovies(query, null, null);
    }

    public void searchMovies(String query, Double minImdbRating, Double minPersonalRating) throws Exception {
        if ((query == null || query.trim().isEmpty()) && minImdbRating == null && minPersonalRating == null) {
            loadAllMovies();
        } else {
            moviesToBeViewed.clear();
            List<Movie> results = movieManager.searchMovies(query, minImdbRating, minPersonalRating);
            if (results != null) {
                moviesToBeViewed.addAll(results);
            }
        }
    }

    public Movie getMovieById(int id) throws Exception {
        return movieManager.getMovieById(id);
    }
}