package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.ICategoryDA;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO implements ICategoryDA {
    @Override
    public List<Category> getAllCategories() throws Exception {
        ArrayList<Category> allCategories = new ArrayList<>();

        //try with resources
        try (Connection conn = new DBConnector().getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Movie;";
            ResultSet rs = stmt.executeQuery(sql);

            //Loop through rows from the database result set
            while (rs.next()) {

                //map DB row to Movie object
                int id = rs.getInt("Id");
                String name = rs.getString("Name");

                Category category = new Category(id, name);
                allCategories.add(category);
            }
            return allCategories;

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new Exception("Could not get movies from database", ex);
        }
    }

    @Override
    public Category createCategory(Category newCategory) throws Exception {
        return null;
    }

    @Override
    public void updateCategory(Category category) throws Exception {

    }

    @Override
    public void deleteCategory(Category category) throws Exception {

    }
}
