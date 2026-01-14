package com.example.MovieExam.BLL;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.DAL.DataAccess.CategoriesDAO;
import com.example.MovieExam.DAL.Interfaces.ICategoryDA;

import java.io.File;
import java.util.List;

public class CategoryManager {

    private ICategoryDA CategoriesDAO;
    private static final String DATA_FOLDER = "data";

    public CategoryManager() {
        this.CategoriesDAO = new CategoriesDAO();

        // Ensure data folder exists
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public List<Category> getAllCategories() throws Exception {
        return CategoriesDAO.getAllCategories();
    }

    /**
     * Create a new Category after validating input and file location
     *
     * @throws Exception
     */
    public Category createCategory(String name) throws Exception {
        // Validation
        validateCategoryInput(name);

        Category category = new Category(name);
        return CategoriesDAO.createCategory(category);
    }

    public void deleteCategory(int categoryId) throws Exception {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        CategoriesDAO.deleteCategory(categoryId);
    }
    public Category getCategoryById(int id) throws Exception {
        return CategoriesDAO.getCategoryById(id);
    }

    public void deleteCategory(int categoryId, boolean deleteFile) throws Exception {
        deleteCategory(categoryId);
    }

    public void updateCategory(Category updatedCategory) throws Exception {
        CategoriesDAO.updateCategory(updatedCategory);
    }

    /**
     * Validate category input fields
     *
     * @param name
     * @throws IllegalArgumentException
     */
    public void validateCategoryInput(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
    }

}
