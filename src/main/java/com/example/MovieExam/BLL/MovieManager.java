package com.example.MovieExam.BLL;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DataAccess.MovieDAO;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MovieManager {

    private IMovieDA movieDAO;
    private static final String DATA_FOLDER = "data";

    public MovieManager() {
        this.movieDAO = new MovieDAO();

        // Ensure data folder exists
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public List<Movie> getAllMovies() throws Exception {
        return movieDAO.getAllMovies();
    }

    /**
     * Create a new movie with validation
     * @param name
     * @param categories
     * @param imdbRating
     * @param personalRating
     * @param filelink
     * @return
     * @throws Exception
     */
    public Movie createMovie(String name, List<Category> categories, double imdbRating, double personalRating, String filelink) throws Exception {
        // Validation
        validateMovieInput(name, categories, imdbRating, personalRating, filelink);

        // Check if file exists
        File file = new File(filelink);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + filelink);
        }

        // Validate file is in data folder
        if (!isFileInDataFolder(file)) {
            throw new IllegalArgumentException("Movie files must be located in the 'data' folder. Please move your file to: " + new File(DATA_FOLDER).getAbsolutePath());
        }

        // Check file extension
        String extension = getFileExtension(filelink);
        if (!extension.equalsIgnoreCase("mp4") && !extension.equalsIgnoreCase("mpeg4")) {
            throw new IllegalArgumentException("Only MP4 and MPEG4 files are supported");
        }

        Movie movie = new Movie(name, categories, imdbRating, personalRating, filelink);
        return movieDAO.createMovie(movie);
    }

    public void deleteMovie(int movieId) throws Exception {
        if (movieId <= 0) {
            throw new IllegalArgumentException("Invalid movie ID");
        }
        movieDAO.deleteMovie(movieId);
    }

    public Movie getMovieById(int id) throws Exception {
        return movieDAO.getMovieById(id);
    }

    public void deleteMovie(int movieId, boolean deleteFile) throws Exception {
        if (deleteFile) {
            Movie movie = movieDAO.getMovieById(movieId);
            if (movie != null) {
                File file = new File(movie.getFileLink());
                if (file.exists() && isFileInDataFolder(file)) {
                    if (!file.delete()) {
                        throw new Exception("Failed to delete file: " + movie.getFileLink());
                    }
                }
            }
        }
        deleteMovie(movieId);
    }

    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.updateMovie(updatedMovie);
    }

    /**
     * Check if file is located within the data folder
     * @param file
     * @return
     * @throws IOException
     */
    private boolean isFileInDataFolder(File file) throws IOException {
        File dataDir = new File(DATA_FOLDER).getCanonicalFile();
        File parentDir = file.getCanonicalFile().getParentFile();

        while (parentDir != null) {
            if (parentDir.equals(dataDir)) {
                return true;
            }
            parentDir = parentDir.getParentFile();
        }
        return false;
    }

    /**
     * Validate movie input fields
     * @param name
     * @param categories
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @throws IllegalArgumentException
     */
    public void validateMovieInput(String name, List<Category> categories, double imdbRating, double personalRating, String fileLink) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie name cannot be empty");
        }
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("At least one category must be selected");
        }
        if (imdbRating < 0 || imdbRating > 10) {
            throw new IllegalArgumentException("IMDB rating must be between 0 and 10");
        }
        if (personalRating < 0 || personalRating > 10) {
            throw new IllegalArgumentException("Personal rating must be between 0 and 10");
        }
        if (fileLink == null || fileLink.trim().isEmpty()) {
            throw new IllegalArgumentException("File link cannot be empty");
        }
    }

    /**
     * Get file extension from file path
     * @param filePath
     * @return
     */
    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filePath.length() - 1) {
            return filePath.substring(lastDot + 1);
        }
        return "";
    }

    /**
     * Get absolute path of data folder
     * @return
     */
    public static String getDataFolderPath() {
        return new File(DATA_FOLDER).getAbsolutePath();
    }

    /**
     * Search movies by title or category name
     * @param query Search query (can be partial match)
     * @return List of movies matching the search criteria
     * @throws Exception
     */
    public List<Movie> searchMovies(String query) throws Exception {
        return searchMovies(query, null, null);
    }

    /**
     * Search movies by title or category name with rating filters
     * @param query Search query (can be partial match)
     * @param minImdbRating Minimum IMDB rating (null to ignore)
     * @param minPersonalRating Minimum personal rating (null to ignore)
     * @return List of movies matching the search criteria
     * @throws Exception
     */
    public List<Movie> searchMovies(String query, Double minImdbRating, Double minPersonalRating) throws Exception {
        if ((query == null || query.trim().isEmpty()) && minImdbRating == null && minPersonalRating == null) {
            return getAllMovies();
        }
        return movieDAO.searchMovies(query != null ? query.trim() : null, minImdbRating, minPersonalRating);
    }
}