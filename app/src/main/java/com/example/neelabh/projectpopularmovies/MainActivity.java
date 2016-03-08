package com.example.neelabh.projectpopularmovies;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;



public class MainActivity extends AppCompatActivity {
    private String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    private String API_KEY = "add your API KEY here";
    private String SortPopular = "popularity.desc";
    private String SortRating = "vote_average.desc";
    static final String SORT_KEY = "LastSortingOrder";
    private String SortOrder = SortPopular;
    private String FinalURL;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<GridItem> gridItems;
    public static final String PAR_KEY = "com.example.neelabh.projectpopularmovies.par";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.most_popular:
                SortOrder = SortPopular;
                FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                new GridViewActivity().execute(FinalURL);
                return true;
            case R.id.highest_rated:
                SortOrder = SortRating;
                FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                new GridViewActivity().execute(FinalURL);
                return true;
            default:
                return false;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            SortOrder = savedInstanceState.getString(SORT_KEY);
        }

        gridView = (GridView) findViewById(R.id.gridView);
        //initialize with empty data
        gridItems = new ArrayList<>();
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item, gridItems);
        gridView.setAdapter(gridAdapter);
        //let the download begin
        FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
        new GridViewActivity().execute(FinalURL);

        //make gridview clickable
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position, long id){
                Intent i = new Intent(getApplicationContext(),MovieActivity.class);
                MovieItem movieItem = new MovieItem();
                GridItem gridItem = (GridItem) gridView.getItemAtPosition(position);
                movieItem = gridItem.getMovieItem();
                Toast.makeText(getApplicationContext(),movieItem.getTitle(),Toast.LENGTH_SHORT).show();
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, movieItem);
                i.putExtra("id",mBundle);
                startActivity(i);

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SORT_KEY,SortOrder);

    }

    /*public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        SortOrder = savedInstanceState.getString(SORT_KEY);
    }*/




    public class GridViewActivity extends AsyncTask<String,Void, Integer> {

        private String BaseURL = "http://image.tmdb.org/t/p";
        private String posterSize = "w342";


        @Override
        protected Integer doInBackground(String... urls ) {
            Integer result=0;
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    if (urlConnection.getResponseCode() == 200) {

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();
                        parseResult(stringBuilder.toString());
                        result = 1;
                    }
                    else{
                        result = 0;
                    }
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);

            }
            return result;

        }
        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1)    {
                gridAdapter.setGridData(gridItems);
                //do something
            } else{
                Toast.makeText(MainActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
                //do something
            }
        }


        private void parseResult(String response) {
            try {
                JSONObject object = new JSONObject(response);
                ArrayList<MovieItem> movieInfo = new ArrayList();
                GridItem item;
                gridItems.clear();
                int pages = object.getInt("page");
                JSONArray results = object.getJSONArray("results");

                for(int i=0;i<results.length();i++){
                    JSONObject resultsObj = results.getJSONObject(i);

                    String adult = resultsObj.getString("adult");
                    String backdrop_path = resultsObj.getString("backdrop_path");
                    String genre_ids = resultsObj.getString("genre_ids");
                    Integer id = resultsObj.getInt("id");
                    String original_language = resultsObj.getString("original_language");
                    String original_title = resultsObj.getString("original_title");
                    String overview = resultsObj.getString("overview");
                    String release_date = resultsObj.getString("release_date");
                    String poster_path = BaseURL + "/" + posterSize + "/" + resultsObj.getString("poster_path");
                    Double popularity = resultsObj.getDouble("popularity");
                    String title = resultsObj.getString("title");
                    String video = resultsObj.getString("video");
                    Double vote_average = resultsObj.getDouble("vote_average");
                    Integer vote_count = resultsObj.getInt("vote_count");
                    MovieItem movieObj = new MovieItem(adult,backdrop_path,genre_ids,id,original_language,
                    original_title, overview, release_date, poster_path, popularity, title, video, vote_average,
                    vote_count);
                    movieInfo.add(movieObj);
                    item = new GridItem();
                    item.setImage(movieObj.getPoster_path());
                    item.setMovieItem(movieObj);
                    gridItems.add(item);
                }
                int total_pages = object.getInt("total_pages");
                int total_results = object.getInt("total_results");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




    }

}
