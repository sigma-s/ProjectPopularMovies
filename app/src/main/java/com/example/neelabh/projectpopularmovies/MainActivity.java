package com.example.neelabh.projectpopularmovies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

    private static final String MOVIEFRAGMENT_TAG = "MFTAG";
    private boolean mTwoPane;


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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.detail_fragment)!=null){
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail_fragment, new MovieFragment(), MOVIEFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        MainFragment mainFragment =  ((MainFragment)getFragmentManager()
                .findFragmentById(R.id.main_fragment));
        mainFragment.setUseTodayLayout(!mTwoPane);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    public void onItemSelected(MovieItem movieItem,boolean flag) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Log.d("Check Movie Item:",movieItem.getTitle());
            Toast.makeText(this,movieItem.getTitle(),Toast.LENGTH_SHORT).show();
            Bundle args = new Bundle();
            args.putParcelable(MovieFragment.MOVIE_URI, movieItem);

            MovieFragment fragment = new MovieFragment();
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment, fragment, MOVIEFRAGMENT_TAG)
                    .commit();
        } else {
            Bundle args = new Bundle();
            args.putParcelable(MovieFragment.MOVIE_URI, movieItem);
            Intent intent = new Intent(this, MovieActivity.class);
            try {
                intent.putExtra("id", args);
                if (!flag) {
                    startActivity(intent);
                }
            }
            catch(Exception e){
                e.printStackTrace();
                Log.e("Error:",e.getMessage(),e);
            }
        }
    }

}
