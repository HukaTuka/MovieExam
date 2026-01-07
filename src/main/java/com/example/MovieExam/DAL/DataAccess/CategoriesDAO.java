package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.DAL.DB.DBConnector;
import com.example.MovieExam.DAL.Interfaces.ICategoryDA;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDAO implements ICategoryDA {

    @Override
    public List<Category> getAllCategories() throws Exception {
        List<Category> allCategories = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Category;";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                allCategories.add(new Category(id, name));
            }
            return allCategories;

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not get categories from database", ex);
        }
    }

    @Override
    public Category createCategory(Category newCategory) throws Exception {
        String sql = "INSERT INTO dbo.Category (Name) VALUES (?);";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, newCategory.getName());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            return new Category(id, newCategory.getName());

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not create category", ex);
        }
    }

    @Override
    public void updateCategory(Category category) throws Exception {
        String sql = "UPDATE dbo.Category SET Name = ? WHERE ID = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not update category", ex);
        }
    }

    @Override
    public void deleteCategory(Category category) throws Exception {
        String sql = "DELETE FROM dbo.Category WHERE ID = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, category.getId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception("Could not delete category", ex);
        }
    }
}
