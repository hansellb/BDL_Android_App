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
import java.util.Random;

/**
 * Created by u on 2017-05-17.
 */

public class BusinessAdapter extends ArrayAdapter<Business> {

    private static final String TAG = "BusinessAdapter";
    private Context adapterContext;

    public BusinessAdapter(@NonNull Context context, @NonNull List<Business> objects) {
        super(context, 0, objects);
        this.adapterContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link Business} object located at this position in the list
        final Business currentBusiness = getItem(position);
//        Log.v(TAG, currentBusiness.toString());

        // Find the TextView in the list_item.xml layout with the ID list_item_business_name
        TextView businessNameTextView = (TextView) listItemView.findViewById(R.id.list_item_business_name);
        // Get the Business name from the current Business object and set this text on the Business name TextView
        businessNameTextView.setText(currentBusiness.name());

        // Find the RatingBar in the list_item.xml layout with the ID list_item_rating_bar
        RatingBar businessRating = (RatingBar) listItemView.findViewById(R.id.list_item_rating_bar);
        // Get the Business rating from the current Business object and set this rating on the Business rating RatingBar
        businessRating.setRating(currentBusiness.rating().floatValue());

        // Find the ImageView in the list_item.xml layout with the ID list_item_business_image
        ImageView businessImageView = (ImageView) listItemView.findViewById(R.id.list_item_business_image);
        // Load image from URL using Picasso library - http://square.github.io/picasso/
        Picasso.with(adapterContext).load(currentBusiness.imageUrl()).placeholder(R.drawable.no_image_available).into(businessImageView);

        // Set the business address
        TextView businessAddressTextView = (TextView) listItemView.findViewById(R.id.list_item_address);
        //businessAddressTextView.setText(String.valueOf(Math.round(currentBusiness.distance())) + currentBusiness.location().displayAddress());
        String address = currentBusiness.location().displayAddress().get(0) + ", ";
        address += currentBusiness.location().displayAddress().get(1) + ", ";
        address += currentBusiness.location().displayAddress().get(2);
        businessAddressTextView.setText(address);

        // Set the business distance
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        TextView businessDistance = (TextView) listItemView.findViewById(R.id.list_item_distance);
        if(currentBusiness.distance() == null) {
            businessDistance.setText(null);
        }else{
            businessDistance.setText(String.format(Locale.US, "%.2f", (currentBusiness.distance()/1000)) + "km");
        }

        int min = 0,max = 2;
        String[] updatedItems = {"New Menu!!!", "New Drink!!!", "New Dish"};
        Random random = new Random();

        TextView businessUpdatedItem = (TextView) listItemView.findViewById(R.id.list_item_updated_item);

        TextView businessUpdatedTime = (TextView) listItemView.findViewById(R.id.list_item_updated_time);

        Log.v(TAG, currentBusiness.name());
        if(currentBusiness.name().equals("Stampen")){

//            businessUpdatedItem.setText(updatedItems[random.nextInt(max-min+1)+min]);
            businessUpdatedItem.setText("New Event!!!");
//            businessUpdatedItem.setText(String.valueOf(random.nextInt(max - min + 1) + min));
            businessUpdatedTime.setText("May 30th 2017");
        }

        if(currentBusiness.name().equals("Tritonia Ölverkstad")){
            businessUpdatedItem.setText("New Menu Item!!!");
            businessUpdatedTime.setText("48min ago");
        }

        if(currentBusiness.name().equals("Wirströms Pub")){
            businessUpdatedItem.setText("New Offer!!!");
            businessUpdatedTime.setText("3h 36min ago");
        }

        // Add OnClickListeners to the business location icon and set a Google Maps Intent
        ImageView businessLocation = (ImageView) listItemView.findViewById(R.id.list_item_location);
        businessLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(adapterContext, "" + currentBusiness.location().coordinate().latitude() + "," + currentBusiness.location().coordinate().longitude(), Toast.LENGTH_SHORT).show();
                String latitudeLongitude = currentBusiness.location().coordinate().latitude() + "," + currentBusiness.location().coordinate().longitude();

                // Create a Uri from an intent string. Use the result to create an Intent.
//                 Uri.parse("google.streetview:cbll=46.414382,10.013988");
//                 Uri.parse("geo:37.7749,-122.4194"); // Centers the map at this location (no pin/marker is shown)
//                 Uri gmmIntentUri = Uri.parse("geo:" + latitudeLongitude + "?z=15&q=" + latitudeLongitude); // ?z=zoomLevel ?q=someQuery - if latitude,longitude are pased, a marker is shown
                Uri gmmIntentUri = Uri.parse("geo:" + latitudeLongitude + "?z=15&q=" + latitudeLongitude + "(" + Uri.encode(currentBusiness.name()) + ")"); // Center map at latitudeLongitude and searches the business name

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                if (mapIntent.resolveActivity(adapterContext.getPackageManager()) != null) {
                    adapterContext.startActivity(mapIntent);
                }

            }
        });

        return listItemView;
    } // public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)

} // public class BusinessAdapter extends ArrayAdapter<Business>
