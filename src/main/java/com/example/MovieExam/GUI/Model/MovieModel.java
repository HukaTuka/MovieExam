package com.example.MovieExam.GUI.Model;

//Project imports
import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.BLL.MovieManager;


//Java imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


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

    public void createMovie(String name, String category, double imdbRating, double personalRating, String fileLink) throws Exception {
        movieManager.createMovie(name, category, imdbRating, personalRating, fileLink);
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
        if (query == null || query.trim().isEmpty()) {
            loadAllMovies();
        } else {
            moviesToBeViewed.clear();
            moviesToBeViewed.addAll(movieManager.searchMovies(query));
        }
    }


    public Movie getMovieById(int id) throws Exception {
        return movieManager.getMovieById(id);
    }
}
