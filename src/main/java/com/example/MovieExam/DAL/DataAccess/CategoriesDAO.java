package com.example.MovieExam.DAL.DataAccess;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.DAL.Interfaces.ICategoryDA;

import java.util.List;

public class CategoriesDAO implements ICategoryDA {
    @Override
    public List<Category> getAllCategories() throws Exception {
        return List.of();
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
