package com.example.MovieExam.BLL;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DataAccess.MovieDAO;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MovieManager {

    private IMovieDA movieDAO;
    private static final String DATA_FOLDER = "data";

    public MovieManager(IMovieDA movieDAO) {
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
     * Create a new movie after validating input and file location
     * @param name
     * @param category
     * @param imdbRating
     * @param personalRating
     * @param filelink
     * @param lastViewed
     * @return
     * @throws Exception
     */
    public Movie createMovie(String name, String category, double imdbRating, double personalRating, String filelink, LocalDateTime lastViewed) throws Exception {
        // Validation
        validateMovieInput(name, category, imdbRating, personalRating, filelink, lastViewed);


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

        Movie movie = new Movie(name, category, imdbRating, personalRating, filelink, lastViewed);
        return movieDAO.createMovie(movie);
    }

    public void deleteMovie(int movieId) throws Exception {
        movieDAO.deleteMovie(movieId);
    }

    public void updateMovie(Movie updatedMovie) throws Exception {
        movieDAO.updateMovie(updatedMovie);
    }

    /**
     * Check if the file is located within the data folder
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
     * @param category
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @param lastViewed
     * @throws IllegalArgumentException
     */
    public void validateMovieInput(String name, String category, double imdbRating, double personalRating, String fileLink, LocalDateTime lastViewed) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie name cannot be empty");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
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
        if (lastViewed == null) {
            throw new IllegalArgumentException("Last viewed date cannot be null");
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
}
