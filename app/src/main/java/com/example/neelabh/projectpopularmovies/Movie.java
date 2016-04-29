package com.example.neelabh.projectpopularmovies;

import android.database.Cursor;

/**
 * Created by neelabh on 4/11/2016.
 */
public class Movie {
    public int movieid;
    public String name;
    public String image;
    public String release_date;
    public String language;
    public Double rating;
    public String overview;

    public Movie(){}
    public Movie(Integer movieid, String name, String image, String release_date,
                 String language, Double rating, String overview){
        this.movieid = movieid;
        this.name = name;
        this.image = image;
        this.release_date = release_date;
        this.language = language;
        this.rating = rating;
        this.overview = overview;
    }

    public int getMovieid(){return movieid;}

    public String getName(){return name;}

    public String getImage(){return image;}

    public String getRelease_date() {
        return release_date;
    }

    public String getLanguage() {
        return language;
    }

    public Double getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public static Movie fromCursor(Cursor cursor){
        Movie movie = new Movie();
        return movie;
    }

}
