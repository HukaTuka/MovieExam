package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Movie;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.IMovieDA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
}
