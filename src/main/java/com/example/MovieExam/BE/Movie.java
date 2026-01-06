package com.example.MovieExam.BE;

public class Movie {

    private int id;
    private String title;
    private String category;
    private int imdbRating;
    private int personalRating;

    /**
     * Constructor for Movie class
     * @param id
     * @param title
     * @param category
     * @param imdbRating
     * @param personalRating
     */
    public Movie(int id, String title, String category, int imdbRating, int personalRating) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.imdbRating = imdbRating;
        this.personalRating = personalRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return id + " " + title + " " + category + " " + imdbRating + " " + personalRating;
    }
}
