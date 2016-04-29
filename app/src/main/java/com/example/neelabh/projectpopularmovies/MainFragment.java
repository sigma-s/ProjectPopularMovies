package com.example.neelabh.projectpopularmovies;

import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by neelabh on 4/17/2016.
 */
public class MainFragment extends Fragment {
    public static final String LOG_TAG = MainFragment.class.getSimpleName();
    public static final String MAIN_TAG = MainActivity.class.getSimpleName();
    public static final String FEED_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    public static final String API_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = "put your API key here";
    public static final String BaseURL = "http://image.tmdb.org/t/p";
    public static final String posterSize = "w342";
    //private static final String LOG_TAG = MoviesFavorite.class.getSimpleName();
    private static final String MOVIEFRAGMENT_TAG = "MFTAG";
    private String SortPopular = "popularity.desc";
    private String SortRating = "vote_average.desc";
    static final String SORT_KEY = "LastSortingOrder";
    private String SortOrder = SortPopular;
    private String FinalURL;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<GridItem> gridItems;
    public static final String PAR_KEY = "com.example.neelabh.projectpopularmovies.par";
    private static final int FORECAST_LOADER = 0;
    private boolean mUseTodayLayout;
    private int id;
    private boolean flag=false;

    public interface Callback{

        public void onItemSelected(MovieItem movieItem, boolean flag);
    }

    public MainFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        //enter code for menu inflation
    }

    public boolean onOptionsItemSelected(MenuItem item){
        id = item.getItemId();

        switch (id) {
            case R.id.most_popular:
                SortOrder = SortPopular;
                FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                new GridViewTask().execute(FinalURL);
               // changeMovieDetail(0);
                return true;
            case R.id.highest_rated:
                SortOrder = SortRating;
                FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                new GridViewTask().execute(FinalURL);
                //changeMovieDetail(0);
                return true;
            case R.id.my_favorites:
                MoviesFavorite mf = new MoviesFavorite();
                mf.onCreateView();
                flag=true;
                changeMovieDetail(0, flag);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(SORT_KEY,id);
    }

    public void changeMovieDetail(int position,boolean flag){
        MovieItem movieItem = new MovieItem();
        GridItem gridItem = (GridItem) gridView.getItemAtPosition(position);
        if(gridItem!=null) {
            movieItem = gridItem.getMovieItem();
            Log.d("Check Movie Item:", movieItem.getTitle());
            Toast.makeText(getActivity(), movieItem.getTitle(), Toast.LENGTH_SHORT).show();
            ((Callback) getActivity()).onItemSelected(movieItem, flag);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);

        if(savedInstanceState!=null) {
            id = savedInstanceState.getInt(SORT_KEY);
        }else {id = 0;}

        gridView = (GridView) rootView.findViewById(R.id.gridView);
        //initialize with empty data
        gridItems = new ArrayList<>();
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, gridItems);
        gridView.setAdapter(gridAdapter);
        //let the download begin
        if(id==0) {
            FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
            new GridViewTask().execute(FinalURL);
        }
        else {
            switch (id) {
                case R.id.most_popular:
                    SortOrder = SortPopular;
                    FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                    new GridViewTask().execute(FinalURL);
                    break;
                case R.id.highest_rated:
                    SortOrder = SortRating;
                    FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                    new GridViewTask().execute(FinalURL);
                    break;
                case R.id.my_favorites:
                    MoviesFavorite mf = new MoviesFavorite();
                    mf.onCreateView();
                    break;
                default:
                    SortOrder = SortPopular;
                    FinalURL = FEED_URL + SortOrder + "&api_key=" + API_KEY;
                    new GridViewTask().execute(FinalURL);
                    break;
            }
        }


        //make gridview clickable
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position, long id){
                flag=false;
                changeMovieDetail(position,flag);

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //getLoaderManager().initLoader(FORECAST_LOADER, null, getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    public class GridViewTask extends AsyncTask<String,Void, Integer> {
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
                Toast.makeText(getActivity(), "Failed to fetch data! No internet connection", Toast.LENGTH_SHORT).show();
                //do something
            }
            flag=true;
            changeMovieDetail(0,flag);
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

    public class MoviesFavorite implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private GridView gridView;

        private static final int CURSOR_LOADER_ID = 0;

        Movie[] movies ={
                new Movie(209112, "Batman v Superman: Dawn of Justice", "6bCplVkhowCjTHXWv49UjRPn0eK.jpg","2016-03-23","en",5.79,"Fearing the actions of a god-like Super Hero left unchecked, Gotham City’s own formidable, forceful vigilante takes on Metropolis’s most revered, modern-day savior, while the world wrestles with what sort of hero it really needs. And with Batman and Superman at war with one another, a new threat quickly arises, putting mankind in greater danger than it’s ever known before."),
                new Movie(140607, "Star Wars: The Force Awakens", "vZpB8ezB1IqpxI9rx553TuGwDzJ.jpg","2015-12-15","en",7.66,"Thirty years after defeating the Galactic Empire, Han Solo and his allies face a new threat from the evil Kylo Ren and his army of Stormtroopers."),
                new Movie(135397, "Jurassic World", "jjBgi2r5cRt36xF6iNUEhzscEcb.jpg","2015-06-09","en",6.65,"Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond."),
                new Movie(293660, "Deadpool", "inVq3FRqcYIRl2la8iZikYYxFNR.jpg","2016-02-09","en",7.18,"Based upon Marvel Comics’ most unconventional anti-hero, DEADPOOL tells the origin story of former Special Forces operative turned mercenary Wade Wilson, who after being subjected to a rogue experiment that leaves him with accelerated healing powers, adopts the alter ego Deadpool. Armed with his new abilities and a dark, twisted sense of humor, Deadpool hunts down the man who nearly destroyed his life."),
                new Movie(131634, "The Hunger Games: Mockingjay - Part 2", "w93GAiq860UjmgR6tU9h2T24vaV.jpg","2015-11-18","en",6.67,"With the nation of Panem in a full scale war, Katniss confronts President Snow in the final showdown. Teamed with a group of her closest friends – including Gale, Finnick, and Peeta – Katniss goes off on a mission with the unit from District 13 as they risk their lives to stage an assassination attempt on President Snow who has become increasingly obsessed with destroying her. The mortal traps, enemies, and moral choices that await Katniss will challenge her more than any arena she faced in The Hunger Games."),
                new Movie(336004, "Heist", "t5tGykRvvlLBULIPsAJEzGg1ylm.jpg","2015-11-13","en",5.1,"A father is without the means to pay for his daughter's medical treatment. As a last resort, he partners with a greedy co-worker to rob a casino. When things go awry they're forced to hijack a city bus.")
        };


        public MoviesFavorite() {
        }

        public void onCreateView() {


            //gridView = (GridView) view.findViewById(R.id.gridView);
            //this.context=context;
            //MovieItem movieItem = new MovieItem();
            //GridItem item = new GridItem();
            gridItems.clear();
            Cursor cursor = null;
            //get entire table data
            /*cursor = getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    null, null, null, null);
            if(cursor!=null || cursor.getCount()!=0){
                deleteData();
            }*/
            cursor = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                    null, null, null, null);
            //prints the contents of the cursor on the screen
            //DatabaseUtils.dumpCursor(cursor);
            if (cursor == null || cursor.getCount() == 0){
                //Only used to insert data in table for checking purposes
                //Else comment out the line below
               // insertData();
                cursor = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                        null, null, null, null);
            }
            cursor.moveToFirst();

            try {
                while (!cursor.isAfterLast()) {
                    MovieItem movieItem = new MovieItem();
                    GridItem item = new GridItem();
                    movieItem.setId(cursor.getInt(cursor.getColumnIndex(MovieColumns._ID)));
                    movieItem.setTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.NAME)));
                    movieItem.setPoster_path(BaseURL + "/" + posterSize + "/" + cursor.getString(cursor.getColumnIndex(MovieColumns.IMAGE)));
                    movieItem.setRelease_date(cursor.getString(cursor.getColumnIndex(MovieColumns.RELEASE_DATE)));
                    movieItem.setOriginal_language(cursor.getString(cursor.getColumnIndex(MovieColumns.LANGUAGE)));
                    movieItem.setVote_average(cursor.getDouble(cursor.getColumnIndex(MovieColumns.RATING)));
                    movieItem.setOverview(cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW)));
                    movieItem.setAdult(" ");
                    movieItem.setBackdrop_path(" ");
                    movieItem.setGenre_ids(" ");
                    movieItem.setPopularity(0.0);
                    movieItem.setVideo(" ");
                    movieItem.setVote_count(0);
                    item.setImage(movieItem.getPoster_path());
                    item.setMovieItem(movieItem);
                    gridItems.add(item);
                    cursor.moveToNext();
                }
                cursor.close();
            }catch(Exception e){
                e.printStackTrace();
                Log.e("Error",e.getMessage(),e);
            }
            gridAdapter.setGridData(gridItems);

        }

        public void insertData(){
            Log.d(LOG_TAG, "insert");
            ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movies.length);
            //  final Context appContext = this.getApplicationContext();
            for (Movie movie : movies){
                ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                        MovieProvider.Movies.CONTENT_URI);
                builder.withValue(MovieColumns._ID, movie.getMovieid());
                builder.withValue(MovieColumns.NAME, movie.getName());
                builder.withValue(MovieColumns.IMAGE, movie.getImage());
                builder.withValue(MovieColumns.RELEASE_DATE, movie.getRelease_date());
                builder.withValue(MovieColumns.LANGUAGE, movie.getLanguage());
                builder.withValue(MovieColumns.RATING, movie.getRating());
                builder.withValue(MovieColumns.OVERVIEW, movie.getOverview());
                batchOperations.add(builder.build());
            }

            try{
                getActivity().getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
            } catch(RemoteException | OperationApplicationException e){
                Log.e(LOG_TAG, "Error applying batch insert", e);
            }

        }

        public void deleteData(){
            Log.d(LOG_TAG, "delete");
            try{
                getActivity().getContentResolver().delete(MovieProvider.Movies.CONTENT_URI,null,null);
            }
            catch(Exception e){
                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);
            }


        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args){
            //figure out later
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data){
            //figure out later

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader){
            //figure out later
        }
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;

    }

}


