package com.example.neelabh.projectpopularmovies;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

/**
 * Created by neelabh on 2/28/2016.
 */
public class MovieActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        Bundle mBundle = getIntent().getBundleExtra("id");

        MovieItem movieItem = (MovieItem)mBundle.getParcelable(MainActivity.PAR_KEY);
        try {
            TextView movieName = (TextView) findViewById(R.id.movie_name);
            movieName.setText(movieItem.getTitle());
            ImageView movieImage = (ImageView) findViewById(R.id.movie_image);
            Picasso.with(this).load(movieItem.getPoster_path()).into(movieImage);
            TextView movieYear = (TextView) findViewById(R.id.year);
            String str[] = movieItem.getRelease_date().split("-");
            movieYear.setText(str[0]);
            TextView movieLanguage = (TextView)findViewById(R.id.language);
            String language = getLanguage(movieItem.getOriginal_language());
            movieLanguage.setText(language);
            TextView movieRating = (TextView) findViewById(R.id.rating);
            movieRating.setText(movieItem.getVote_average() + "/10");
            TextView movieDescr = (TextView) findViewById(R.id.movie_descr);
            movieDescr.setText(movieItem.getOverview());
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getLanguage(String languageID){
        String language;
        if(languageID.equals("en")) {
            language = "English";
        }
        else if(languageID.equals("es")) {
            language = "Spanish";
        }
        else if(languageID.equals("uk")) {
            language = "Ukranian";
        }
        else if(languageID.equals("de")) {
            language = "German";
        }
        else if(languageID.equals("pt")) {
            language = "Portuguese";
        }
        else if(languageID.equals("fr")) {
            language = "French";
        }
        else if(languageID.equals("nl")) {
            language = "Dutch";
        }
        else if(languageID.equals("hu")) {
            language = "Hungarian";
        }
        else if(languageID.equals("ru")) {
            language = "Russian";
        }
        else if(languageID.equals("it")) {
            language = "Italian";
        }
        else if(languageID.equals("tr")) {
            language = "Turkish";
        }
        else if(languageID.equals("zh")) {
            language = "Mandarin";
        }
        else if(languageID.equals("da")) {
            language = "Danish";
        }
        else if(languageID.equals("sv")) {
            language = "Swedish";
        }
        else if(languageID.equals("pl")) {
            language = "Polish";
        }
        else if(languageID.equals("fi")) {
            language = "Finnish";
        }
        else if(languageID.equals("he")) {
            language = "Hebrew";
        }
        else{
            language = "Not known";
        }

        return language;
    }

}
