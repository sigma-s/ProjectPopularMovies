package com.example.neelabh.projectpopularmovies;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by neelabh on 3/27/2016.
 */
public interface TheMovieDBAPI {
    @GET("movie/{id}/videos")
    Call<TrailerItem> getTrailers(@Path("id") String id,
                            @Query("api_key")String key
    );
    /*Call<ResponseBody> getTrailers(@Path("id") String id,
                                   @Query("api_key")String key
    );*/
    @GET("movie/{id}/reviews")
    Call<ReviewItem> getReviews(@Path("id") String id,
                                @Query("api_key") String key
    );

}