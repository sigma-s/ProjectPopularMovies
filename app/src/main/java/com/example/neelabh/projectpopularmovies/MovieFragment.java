package com.example.neelabh.projectpopularmovies;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by neelabh on 4/17/2016.
 */
public class MovieFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
     //Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main,container,false);
    }

}