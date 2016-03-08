package com.example.neelabh.projectpopularmovies;

/**
 * Created by neelabh on 2/19/2016.
 */
public class GridItem {
    private String image;
    private String title;
    private MovieItem movieItem;

    public GridItem() {
        super();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieItem getMovieItem() { return movieItem;}

    public void setMovieItem(MovieItem movieItem) {this.movieItem = movieItem;}
}
