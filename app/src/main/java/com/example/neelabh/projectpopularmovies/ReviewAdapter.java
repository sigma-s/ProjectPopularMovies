package com.example.neelabh.projectpopularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by neelabh on 4/9/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<
        ReviewAdapter.MyViewHolder> {

    private ArrayList<ReviewItem.Review> reviewList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;

        public MyViewHolder(View v){
            super(v);
            author = (TextView)v.findViewById(R.id.review_author);
            content = (TextView)v.findViewById(R.id.review_content);

        }
    }

    public void add(int position, ReviewItem.Review item){
        reviewList.add(position,item);
        notifyItemInserted(position);
    }

    public void remove(ReviewItem.Review item){
        int position = reviewList.indexOf(item);
        reviewList.remove(position);
        notifyItemRemoved(position);
    }

    public ReviewAdapter(ArrayList<ReviewItem.Review> myReviewList){
        this.reviewList = myReviewList;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        holder.author.setText(reviewList.get(position).getAuthor());
        holder.content.setText(reviewList.get(position).getContent());
    }

    @Override
    public int getItemCount(){
        return reviewList.size();
    }

}