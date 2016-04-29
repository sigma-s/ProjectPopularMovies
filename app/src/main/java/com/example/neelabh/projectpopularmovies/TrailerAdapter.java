package com.example.neelabh.projectpopularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by neelabh on 3/27/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<
             TrailerAdapter.MyViewHolder> {
    private ArrayList<String> trailerList;
    OnItemClickListener mItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image;
        public TextView text;

        public MyViewHolder(View v){
            super(v);
            image = (ImageView)v.findViewById(R.id.trailer_image);
            text = (TextView)v.findViewById(R.id.trailer_text);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

    public void add(int position, String item){
        trailerList.add(position,item);
        notifyItemInserted(position);
    }

    public void remove(String item){
        int position = trailerList.indexOf(item);
        trailerList.remove(position);
        notifyItemRemoved(position);
    }

    public TrailerAdapter(ArrayList<String> myTrailerList){
        this.trailerList = myTrailerList;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        holder.text.setText(trailerList.get(position));
    }

    @Override
    public int getItemCount(){
        return trailerList.size();
        //return 5;
    }

}
