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
             TrailerAdapter.ViewHolder> {
    private ArrayList<String> trailerList = new ArrayList<String>();
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image;
        public TextView text;

        public ViewHolder(View v){
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
        //this.trailerList = myTrailerList;
        for(int i=0;i<5;i++){
            trailerList.add(i,"Trailer"+(i+1));
        }
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.text.setText(trailerList.get(position));
        notifyDataSetChanged();
       /* holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });*/
    }

    @Override
    public int getItemCount(){
        //return trailerList.size();
        return 5;
    }

}
