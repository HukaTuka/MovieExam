package com.example.MovieExam.GUI.Model;

import com.example.MovieExam.BE.Category;
import com.example.MovieExam.BLL.CategoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CategoryModel {

    private ObservableList<Category> categoriesToBeViewed;
    private CategoryManager categoryManager;

    public CategoryModel() throws Exception {
        this.categoryManager = new CategoryManager();
        this.categoriesToBeViewed = FXCollections.observableArrayList();
        loadAllCategories();
    }
    public ObservableList<Category> getObservableCategories() {
        return categoriesToBeViewed;
    }

    public void loadAllCategories() throws Exception {
        categoriesToBeViewed.clear();
        categoriesToBeViewed.addAll(categoryManager.getAllCategories());
    }

    public void createCategory(String name) throws Exception {
        categoryManager.createCategory(name);
        loadAllCategories();
    }

    public void updateCategory(Category category) throws Exception {
        categoryManager.updateCategory(category);
        loadAllCategories();
    }

    public void deleteCategory(Category category, boolean deleteFile) throws Exception {
        categoryManager.deleteCategory(category.getId(), deleteFile);
        loadAllCategories();
    }



    public Category getCategoryById(int id) throws Exception {
        return categoryManager.getCategoryById(id);
    }
}
