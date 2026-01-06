package com.example.MovieExam.BE;

import java.time.LocalDateTime;

public class Movie {

    private int id;
    private String name;
    private String category;
    private int imdbRating;
    private int personalRating;
    private String filelink;
    private LocalDateTime lastViewed;

    /**
     * Constructor for Movie class
     * @param id
     * @param name
     * @param category
     * @param imdbRating
     * @param personalRating
     * @param filelink
     * @param lastViewed
     */
   public void movie(int id, String name, String category, int imdbRating, int personalRating, String filelink, LocalDateTime lastViewed) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
        this.filelink = filelink;
        this.lastViewed = lastViewed;
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

    public String getFilelink() {
        return filelink;
    }

    public void setFilelink(String filelink) {
        this.filelink = filelink;
    }

    public int getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(int personalRating) {
        this.personalRating = personalRating;
    }

    public int getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(int imdbRating) {
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
}
