package com.example.neelabh.projectpopularmovies;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by neelabh on 4/17/2016.
 */
public class MovieFragment extends Fragment {

    static final String MOVIE_URI = "URI";
    private RecyclerView mRecyclerViewT;
    private MovieItem movieItem;
    private TrailerAdapter mAdapterT;
    private ArrayList<String> myTrailerList = new ArrayList<>();
    private ArrayList<String> trailerLink = new ArrayList<>();
    private TrailerItem trailers = new TrailerItem();

    private RecyclerView mRecyclerViewR;
    private ReviewAdapter mAdapterR;
    private ArrayList<ReviewItem.Review> myReviewList = new ArrayList<>();
    private ReviewItem reviews = new ReviewItem();
    private ShareActionProvider mShareActionProvider;
    private Intent sharingIntent;
    private Uri mUri;

    public MovieFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
     //Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Initialize recycler view
        mRecyclerViewT = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerViewT.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewT.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapterT = new TrailerAdapter(myTrailerList);
        mRecyclerViewT.setAdapter(mAdapterT);

        mRecyclerViewR = (RecyclerView) rootView.findViewById(R.id.recyclerviewReviews);
        mRecyclerViewR.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewR.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapterR = new ReviewAdapter(myReviewList);
        mRecyclerViewR.setAdapter(mAdapterR);
        Button button = (Button) rootView.findViewById(R.id.favoriteButton);

        Bundle arguments = getArguments();
        View detailView = (View)rootView.findViewById(R.id.detail_view);
        detailView.setVisibility(View.INVISIBLE);
        if (arguments != null) {

            detailView.setVisibility(View.VISIBLE);

            movieItem = arguments.getParcelable(MovieFragment.MOVIE_URI);

            try {
                TextView movieName = (TextView) rootView.findViewById(R.id.movie_name);
                movieName.setText(movieItem.getTitle());
                ImageView movieImage = (ImageView) rootView.findViewById(R.id.movie_image);
                Picasso.with(getActivity()).load(movieItem.getPoster_path()).into(movieImage);
                TextView movieYear = (TextView) rootView.findViewById(R.id.year);
                //String str[] = movieItem.getRelease_date().split("-");
                movieYear.setText(movieItem.getRelease_date());
                TextView movieLanguage = (TextView) rootView.findViewById(R.id.language);
                String language = getLanguage(movieItem.getOriginal_language());
                movieLanguage.setText(language);
                TextView movieRating = (TextView) rootView.findViewById(R.id.rating);
                movieRating.setText(movieItem.getVote_average() + "/10");
                TextView movieDescr = (TextView) rootView.findViewById(R.id.movie_descr);
                movieDescr.setText(movieItem.getOverview());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);}
            //check for network connection
            if(isNetworkConnectionAvailable()) {

                BackgroundTrailer task = new BackgroundTrailer();
                task.execute(movieItem);
                BackgroundReview taskReview = new BackgroundReview();
                taskReview.execute(movieItem);
            }



            try {
                button.setOnClickListener(
                        new Button.OnClickListener() {
                            public void onClick(View v) {
                                //check if the movie is already present
                                String selectionClause = MovieColumns._ID + "=" + movieItem.getId();
                                Cursor c = getActivity().getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                                        null, selectionClause, null, null);
                                if (c == null || c.getCount() == 0) {
                                    //if movie is not present in table, insert movie
                                    ContentValues cv = new ContentValues();
                                    cv.put(MovieColumns._ID, movieItem.getId());
                                    cv.put(MovieColumns.NAME, movieItem.getTitle());
                                    cv.put(MovieColumns.IMAGE, movieItem.getPoster_path());
                                    cv.put(MovieColumns.OVERVIEW, movieItem.getOverview());
                                    cv.put(MovieColumns.RATING, movieItem.getVote_average());
                                    cv.put(MovieColumns.RELEASE_DATE, movieItem.getRelease_date());
                                    cv.put(MovieColumns.LANGUAGE, movieItem.getOriginal_language());
                                    try {
                                        getActivity().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, cv);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.e("Error:", e.getMessage(), e);
                                    }
                                    Toast.makeText(getActivity(), "This movie has been selected as favorite", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Movie already a favorite", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error:", e.getMessage(), e);
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id== android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(getActivity());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(getActivity(), intent);
            return true;
        }

        if (id == R.id.menu_item_share) {
            if(sharingIntent!=null) {
                setShareIntent(sharingIntent);
                startActivity(Intent.createChooser(sharingIntent, "Share video using"));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent(Intent shareIntent) {
        if(mShareActionProvider!=null){
            mShareActionProvider.setShareIntent(shareIntent);
        }
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

    private class BackgroundTrailer extends AsyncTask<MovieItem, Void,
            TrailerItem> {
        Retrofit retrofit;
        TrailerItem trailers = new TrailerItem();


        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainFragment.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            trailers.setId(0);

        }

        @Override
        protected TrailerItem doInBackground(MovieItem... movie) {
            TheMovieDBAPI movieTrailer = retrofit.create(TheMovieDBAPI.class);
            String url = MainFragment.API_URL + "movie/" +movie[0].getId() + "/videos" + "?api_key=" + MainFragment.API_KEY;

          /*  Call<ResponseBody> call = movieTrailer.getTrailers(String.valueOf(movie[0].getId())
                    ,MainActivity.API_KEY);
            call.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                    try{
                        Log.d("My app",response.body().string());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t){
                    System.out.println(t.getMessage());
                }
            });*/
            Call<TrailerItem> call = movieTrailer.getTrailers(String.valueOf(movie[0].getId())
                    ,MainFragment.API_KEY);
            try{
                if(call!=null) {
                    trailers = call.execute().body();
                }
            }
            catch(IOException e){
                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);
            }
            /*For asynchronous request - both Asynctask and Asynchronous Request bad combination
            call.enqueue(new Callback<TrailerItem>() {
                @Override
                public void onResponse(Call<TrailerItem> call, Response<TrailerItem> response) {
                    System.out.println("Response status code:" + response.code());
                    if (!response.isSuccessful()) {
                        // print response body if unsuccessful
                        try {
                            System.out.println(response.errorBody().string());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                            Log.e("ERROR", e.getMessage(), e);
                        }
                        return;
                    }
                    TrailerItem decodedResponse = response.body();
                    if(decodedResponse==null)return;

                    trailers = response.body();
                   // int id = trailers.getId();
                    //String output = response.body().toString();


                    //int size = trailers.getTrailerList().size();
                   // TrailerItem.Trailer id2 = trailers.getTrailerList().get(0);

                }

                @Override
                public void onFailure(Call<TrailerItem> call, Throwable t) {
                    System.out.println("onFailure");
                    System.out.println(t.getMessage());

                }
            });*/
            int size2 = trailers.getTrailerList().size();
            return trailers;


        }

        @Override
        protected void onPostExecute(TrailerItem trailers) {
            if(trailers.getId()!=0) {
                List<TrailerItem.Trailer> list = trailers.getTrailerList();
                if(list.size()==0){
                    Toast.makeText(getActivity(), "No trailers available in database!", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < list.size(); i++) {
                    myTrailerList.add(i, "Trailer" + " "+ (i + 1));
                    trailerLink.add(i, list.get(i).getKey());
                }

                sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Trailer link for "+ movieItem.getTitle());
                if(trailers.getId()!=0 & list.size()!=0) {
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + list.get(0).getKey());


                    //mAdapter = new TrailerAdapter(myList);
                    //mRecyclerView.setAdapter(mAdapter);
                    mAdapterT.SetOnItemClickListener(new TrailerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            String url = "https://www.youtube.com/watch?v=" + trailerLink.get(position);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                }
                mAdapterT.notifyDataSetChanged();


            }
            else{
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BackgroundReview extends AsyncTask<MovieItem, Void,
            ReviewItem> {
        Retrofit retrofit;
        ReviewItem reviews = new ReviewItem();


        @Override
        protected void onPreExecute() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainFragment.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            reviews.setId(0);
        }

        @Override
        protected ReviewItem doInBackground(MovieItem... movie) {
            TheMovieDBAPI movieReview = retrofit.create(TheMovieDBAPI.class);
            String url = MainFragment.API_URL + "movie/" +movie[0].getId() + "/reviews" + "?api_key=" + MainFragment.API_KEY;

            Call<ReviewItem> call = movieReview.getReviews(String.valueOf(movie[0].getId())
                    ,MainFragment.API_KEY);
            try{
                if(call!=null){
                reviews = call.execute().body();
                }
            }
            catch(IOException e){
                e.printStackTrace();
                Log.e("ERROR", e.getMessage(), e);
            }

            int size2 = reviews.getResults().size();
            return reviews;


        }

        @Override
        protected void onPostExecute(ReviewItem reviews) {
            if(reviews.getId()!=0) {
                List<ReviewItem.Review> list = reviews.getResults();
                if(list.size()==0){
                    Toast.makeText(getActivity(), "No reviews available in database!", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < list.size(); i++) {
                    myReviewList.add(i, reviews.getResults().get(i));
                }

                mAdapterR.notifyDataSetChanged();

            }
            else{
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }



}
