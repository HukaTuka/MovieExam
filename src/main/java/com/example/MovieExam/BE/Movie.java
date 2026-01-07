package com.example.MovieExam.BE;

import java.time.LocalDateTime;

public class Movie {

    private int id;
    private String name;
    private String category;
    private double imdbRating;
    private double personalRating;
    private String fileLink;
    private LocalDateTime lastViewed;

    /**
     * Constructor for Movie class
     * @param id
     * @param name
     * @param category
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @param lastViewed
     */
   public Movie(int id, String name, String category, double imdbRating, double personalRating, String fileLink, LocalDateTime lastViewed) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.fileLink = fileLink;
        this.lastViewed = lastViewed;
    }

    /**
     * Constructor for Movie class without id
     * @param name
     * @param category
     * @param imdbRating
     * @param personalRating
     * @param fileLink
     * @param lastViewed
     */
    public Movie (String name, String category, double imdbRating, double personalRating, String fileLink, LocalDateTime lastViewed) {
       this(-1, name, category, imdbRating, personalRating, fileLink, lastViewed);

    }
    public Movie (String name, String category, double imdbRating, double personalRating, String fileLink) {
        this(-1, name, category, imdbRating, personalRating, fileLink, null);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ": " + name + " | " + category + " | IMDB: " + imdbRating + " | Personal: " + personalRating + " | Last Viewed: " + lastViewed;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

}
