package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Statement;

public class MovieDAO implements IMovieDA {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movie ORDER BY lastViewed DESC";

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                movies.add(createMovieFromResultSet(rs));
            }
        } catch (SQLException ex) {
            throw new Exception("Could not get movies from database", ex);
        }
        return movies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        String sql = "INSERT INTO Movie (name, category, imdbRating, personalRating, fileLink, lastViewed) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newMovie.getName());
            stmt.setString(2, newMovie.getCategory());
            stmt.setDouble(3, newMovie.getImdbRating());
            stmt.setDouble(4, newMovie.getPersonalRating());
            stmt.setString(5, newMovie.getFileLink());
            // Set lastViewed to current time if null
            LocalDateTime lastViewed = newMovie.getLastViewed();
            if (lastViewed == null) {
                lastViewed = LocalDateTime.now();
                newMovie.setLastViewed(lastViewed);
            }
            stmt.setTimestamp(6, Timestamp.valueOf(lastViewed));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                newMovie.setId(rs.getInt(1));
            }
        }
        return newMovie;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        String sql = "UPDATE Movie SET name = ?, category = ?, imdbRating = ?, personalRating = ?, fileLink = ?, lastViewed = ? WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, movie.getName());
            stmt.setString(2, movie.getCategory());
            stmt.setDouble(3, movie.getImdbRating());
            stmt.setDouble(4, movie.getPersonalRating());
            stmt.setString(5, movie.getFileLink());

            if (movie.getLastViewed() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(movie.getLastViewed()));
            } else {
                stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setInt(7, movie.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Could not update movie", ex);
        }
    }

    @Override
    public void deleteMovie(int movieId) throws Exception {

    }

    @Override
    public Movie getMovieById(int id) throws SQLException {
        String sql = "SELECT * FROM Movie WHERE id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return createMovieFromResultSet(rs);
            }
        }
        return null;
    }

    private Movie createMovieFromResultSet(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("imdbRating"),
                rs.getDouble("personalRating"),
                rs.getString("fileLink"),
                rs.getTimestamp("lastViewed").toLocalDateTime()
        );
    }
}
