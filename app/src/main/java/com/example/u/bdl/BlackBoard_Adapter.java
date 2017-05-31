package com.example.u.bdl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 2017-04-14.
 */

public class BlackBoard_Adapter extends ArrayAdapter<BlackBoard> {

    ArrayList<BlackBoard> blackboards;
    Context context;
    int resource;

    public BlackBoard_Adapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<BlackBoard> blackboards) {
        super(context, resource, blackboards);
        this.blackboards = blackboards;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView ==  null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_item, null, true);
        }
        BlackBoard blackboard = getItem(position);
        ImageView image = (ImageView) convertView.findViewById(R.id.bkbrd_image);
        TextView name = (TextView) convertView.findViewById(R.id.bkbrd_name);
        RatingBar rating = (RatingBar) convertView.findViewById(R.id.bkbrd_rating);

        name.setText( blackboard.getName() );
        rating.setRating( blackboard.getRating() );
        Picasso.with(context).load( blackboard.getImage_url() ).into(image);

        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
