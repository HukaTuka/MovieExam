package com.example.MovieExam.BE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Movie {

    private int id;
    private String name;
    private List<Category> categories;
    private double imdbRating;
    private double personalRating;
    private String fileLink;
    private LocalDateTime lastViewed;

    /**
     * Full constructor
     * @param id
     * @param name
     * @param categories
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @param lastViewed
     */
    public Movie(int id, String name, List<Category> categories, double imdbRating, double personalRating, String fileLink, LocalDateTime lastViewed) {
        this.id = id;
        this.name = name;
        this.categories = categories != null ? categories : new ArrayList<>();
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.fileLink = fileLink;
        this.lastViewed = lastViewed;
    }

    /**
     * Constructor without id (for new movies)
     * @param name
     * @param categories
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @param lastViewed
     */
    public Movie(String name, List<Category> categories, double imdbRating, double personalRating, String fileLink, LocalDateTime lastViewed) {
        this(-1, name, categories, imdbRating, personalRating, fileLink, lastViewed);
    }

    public Movie(String name, List<Category> categories, double imdbRating, double personalRating, String fileLink) {
        this(-1, name, categories, imdbRating, personalRating, fileLink, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getLastViewed() {
        return lastViewed;
    }

    public void setLastViewed(LocalDateTime lastViewed) {
        this.lastViewed = lastViewed;
    }

    public double getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(double personalRating) {
        this.personalRating = personalRating;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
    }

    /**
     * Get categories as a comma-separated string
     * @return
     */
    public String getCategoriesAsString() {
        if (categories == null || categories.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < categories.size(); i++) {
            sb.append(categories.get(i).getName());
            if (i < categories.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ": " + name + " | " + getCategoriesAsString() + " | IMDB: " + imdbRating + " | Personal: " + personalRating + " | Last Viewed: " + lastViewed;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }
}