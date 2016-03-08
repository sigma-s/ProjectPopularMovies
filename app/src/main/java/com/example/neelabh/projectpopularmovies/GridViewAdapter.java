package com.example.neelabh.projectpopularmovies;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by neelabh on 2/14/2016.
 */
public class GridViewAdapter extends ArrayAdapter {

        private Context context;
        private int layoutResourceId;
        private ArrayList<GridItem> data = new ArrayList();

        public GridViewAdapter(Context context, int layoutResourceId, ArrayList<GridItem> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        public void setGridData(ArrayList<GridItem> gridItems) {
            this.data = gridItems;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.image = (ImageView) row.findViewById(R.id.image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            GridItem item = data.get(position);
            Picasso.with(context).load(item.getImage()).into(holder.image);
            return row;
        }

        static class ViewHolder {
            ImageView image;
        }
    }
