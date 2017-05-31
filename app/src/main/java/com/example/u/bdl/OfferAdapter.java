package com.example.u.bdl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by u on 2017-05-30.
 */

public class OfferAdapter extends ArrayAdapter<Business> {

    private static final String TAG = "BusinessAdapter";
    private Context adapterContext;

    public OfferAdapter(@NonNull Context context, @NonNull List<Business> objects) {
        super(context, 0, objects);
        this.adapterContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.offer_item, parent, false);
        }

        // Get the {@link Business} object located at this position in the list
        final Business currentBusiness = getItem(position);
//        Log.v(TAG, currentBusiness.toString());

        // Find the TextView in the list_item.xml layout with the ID list_item_business_name
        TextView businessNameTextView = (TextView) listItemView.findViewById(R.id.offer_item_name);
        // Get the Business name from the current Business object and set this text on the Business name TextView
        businessNameTextView.setText(currentBusiness.name());

        // Find the ImageView in the list_item.xml layout with the ID list_item_business_image
        ImageView businessImageView = (ImageView) listItemView.findViewById(R.id.offer_item_image);
        // Load image from URL using Picasso library - http://square.github.io/picasso/
        Picasso.with(adapterContext).load(R.drawable.blkbrd_1).placeholder(R.drawable.no_image_available).into(businessImageView);

        // Set the business address
        TextView businessAddressTextView = (TextView) listItemView.findViewById(R.id.offer_item_update_time);
        businessAddressTextView.setText(currentBusiness.menuDateUpdated().toString());

        return listItemView;
    } // public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)

} // public class OfferAdapter extends ArrayAdapter<Business>
