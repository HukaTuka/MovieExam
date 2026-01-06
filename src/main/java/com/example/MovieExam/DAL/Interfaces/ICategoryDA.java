package com.example.MovieExam.DAL.Interfaces;

import com.example.MovieExam.BE.Category;

import java.util.List;

public interface ICategoryDA {
    List<Category> getAllCategories() throws Exception;

    Category createCategory(Category newCategory) throws Exception;

    void updateCategory(Category category) throws Exception;

    void deleteCategory(Category category) throws Exception;
}
