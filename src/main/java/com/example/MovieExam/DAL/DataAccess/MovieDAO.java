package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.sql.*;
import java.util.List;

public class MovieDAO implements IMovieDA {

    @Override
    public List<Movie> getAllMovies() throws Exception {
        return List.of();
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
            stmt.setDate(6, java.sql.Date.valueOf(newMovie.getLastViewed().toLocalDate()));

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
