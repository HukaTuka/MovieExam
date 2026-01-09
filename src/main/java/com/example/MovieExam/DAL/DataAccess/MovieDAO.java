package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieDAO implements IMovieDA {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        if (!DBConnector.isConnectionAvailable()) {
            System.out.println("Database offline - returning empty movie list");
            return new ArrayList<>();
        }

        Map<Integer, Movie> movieMap = new HashMap<>();
        String sql = "SELECT m.*, c.id as catId, c.Name as catName " +
                "FROM Movie m " +
                "LEFT JOIN CatMovie cm ON m.id = cm.movieId " +
                "LEFT JOIN Category c ON cm.categoryId = c.id " +
                "ORDER BY m.lastViewed DESC";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int movieId = rs.getInt("id");

                // Get or create movie
                Movie movie = movieMap.get(movieId);
                if (movie == null) {
                    movie = new Movie(
                            movieId,
                            rs.getString("name"),
                            new ArrayList<>(),
                            rs.getDouble("imdbRating"),
                            rs.getDouble("personalRating"),
                            rs.getString("fileLink"),
                            rs.getTimestamp("lastViewed") != null ?
                                    rs.getTimestamp("lastViewed").toLocalDateTime() : null
                    );
                    movieMap.put(movieId, movie);
                }

                // Add category if exists
                int catId = rs.getInt("catId");
                if (!rs.wasNull()) {
                    String catName = rs.getString("catName");
                    movie.getCategories().add(new Category(catId, catName));
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Could not get movies from database: " + ex.getMessage(), ex);
        }

        return new ArrayList<>(movieMap.values());
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        if (!DBConnector.isConnectionAvailable()) {
            throw new Exception(
                    "Cannot save movie - Database is not available.\n\n" +
                            "Error: " + DBConnector.getLastError() + "\n\n" +
                            "Please check your database connection and try again."
            );
        }

        String sql = "INSERT INTO Movie (name, imdbRating, personalRating, fileLink, lastViewed) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newMovie.getName());
            stmt.setDouble(2, newMovie.getImdbRating());
            stmt.setDouble(3, newMovie.getPersonalRating());
            stmt.setString(4, newMovie.getFileLink());

            LocalDateTime lastViewed = newMovie.getLastViewed();
            if (lastViewed == null) {
                lastViewed = LocalDateTime.now();
                newMovie.setLastViewed(lastViewed);
            }
            stmt.setTimestamp(5, Timestamp.valueOf(lastViewed));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                newMovie.setId(rs.getInt(1));
            }

            // Insert categories into CatMovie table
            if (newMovie.getCategories() != null && !newMovie.getCategories().isEmpty()) {
                insertMovieCategories(conn, newMovie.getId(), newMovie.getCategories());
            }

        } catch (SQLException ex) {
            throw new Exception("Could not create movie: " + ex.getMessage(), ex);
        }
        return newMovie;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        if (!DBConnector.isConnectionAvailable()) {
            throw new Exception(
                    "Cannot update movie - Database is not available.\n\n" +
                            "Error: " + DBConnector.getLastError()
            );
        }

        String sql = "UPDATE Movie SET name = ?, imdbRating = ?, personalRating = ?, fileLink = ?, lastViewed = ? WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getName());
            stmt.setDouble(2, movie.getImdbRating());
            stmt.setDouble(3, movie.getPersonalRating());
            stmt.setString(4, movie.getFileLink());

            if (movie.getLastViewed() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(movie.getLastViewed()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setInt(6, movie.getId());
            stmt.executeUpdate();

            // Update categories - delete old ones and insert new ones
            deleteMovieCategories(conn, movie.getId());
            if (movie.getCategories() != null && !movie.getCategories().isEmpty()) {
                insertMovieCategories(conn, movie.getId(), movie.getCategories());
            }

        } catch (SQLException ex) {
            throw new Exception("Could not update movie: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteMovie(int movieId) throws Exception {
        if (!DBConnector.isConnectionAvailable()) {
            throw new Exception(
                    "Cannot delete movie - Database is not available.\n\n" +
                            "Error: " + DBConnector.getLastError()
            );
        }

        // The CASCADE delete will handle CatMovie entries automatically
        String sql = "DELETE FROM Movie WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not delete movie: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Movie getMovieById(int id) throws SQLException {
        if (!DBConnector.isConnectionAvailable()) {
            return null;
        }

        String sql = "SELECT m.*, c.id as catId, c.Name as catName " +
                "FROM Movie m " +
                "LEFT JOIN CatMovie cm ON m.id = cm.movieId " +
                "LEFT JOIN Category c ON cm.categoryId = c.id " +
                "WHERE m.id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            Movie movie = null;
            while (rs.next()) {
                if (movie == null) {
                    movie = new Movie(
                            rs.getInt("id"),
                            rs.getString("name"),
                            new ArrayList<>(),
                            rs.getDouble("imdbRating"),
                            rs.getDouble("personalRating"),
                            rs.getString("fileLink"),
                            rs.getTimestamp("lastViewed") != null ?
                                    rs.getTimestamp("lastViewed").toLocalDateTime() : null
                    );
                }

                // Add category if exists
                int catId = rs.getInt("catId");
                if (!rs.wasNull()) {
                    String catName = rs.getString("catName");
                    movie.getCategories().add(new Category(catId, catName));
                }
            }
            return movie;
        }
    }

    /**
     * Insert movie-category associations into CatMovie table
     * @param conn
     * @param movieId
     * @param categories
     * @throws SQLException
     */
    private void insertMovieCategories(Connection conn, int movieId, List<Category> categories) throws SQLException {
        String sql = "INSERT INTO CatMovie (movieId, categoryId) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Category category : categories) {
                stmt.setInt(1, movieId);
                stmt.setInt(2, category.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    /**
     * Delete all category associations for a movie
     * @param conn
     * @param movieId
     * @throws SQLException
     */
    private void deleteMovieCategories(Connection conn, int movieId) throws SQLException {
        String sql = "DELETE FROM CatMovie WHERE movieId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }
}