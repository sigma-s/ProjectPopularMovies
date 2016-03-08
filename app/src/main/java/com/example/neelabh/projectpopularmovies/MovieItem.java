package com.example.neelabh.projectpopularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by neelabh on 2/26/2016.
 */
public class MovieItem implements Parcelable {
    private String adult;
    private String backdrop_path;
    private String genre_ids;
    private Integer id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private Double popularity;
    private String title;
    private String video;
    private Double vote_average;
    private Integer vote_count;

    public MovieItem(String adult, String backdrop_path, String genre_ids,
                     Integer id, String original_language, String original_title, String overview,
                     String release_date, String poster_path, Double popularity, String title,
                     String video, Double vote_average, Integer vote_count) {

        super();
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genre_ids=genre_ids;
        this.id=id;
        this.original_language=original_language;
        this.original_title=original_title;
        this.overview=overview;
        this.release_date=release_date;
        this.poster_path=poster_path;
        this.popularity=popularity;
        this.title=title;
        this.video=video;
        this.vote_average=vote_average;
        this.vote_count=vote_count;
        }

    public MovieItem(){
        //blank
    }

    private MovieItem(Parcel source){
        adult = source.readString();
        backdrop_path = source.readString();
        genre_ids=source.readString();
        id=source.readInt();
        original_language=source.readString();
        //original_title=source.readString();
        overview=source.readString();
        release_date=source.readString();
        poster_path=source.readString();
        popularity=source.readDouble();
        title=source.readString();
        video=source.readString();
        vote_average=source.readDouble();
        vote_count=source.readInt();
    }



    public void setAdult(String adult) {
        this.adult = adult;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setGenre_ids(String genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }

    public void setVote_count(Integer vote_count) {
        this.vote_count = vote_count;
    }

    public String getAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public Integer getId() {
        return id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public Integer getVote_count() {
        return vote_count;
    }

    public static final Parcelable.Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(adult);
        parcel.writeString(backdrop_path);
        parcel.writeString(genre_ids);
        parcel.writeInt(id);
        parcel.writeString(original_language);
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeString(poster_path);
        parcel.writeDouble(popularity);
        parcel.writeString(title);
        parcel.writeString(video);
        parcel.writeDouble(vote_average);
        parcel.writeInt(vote_count);
    }
}

