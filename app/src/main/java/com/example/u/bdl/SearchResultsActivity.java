package com.example.u.bdl;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.*;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";

    private JSONObject jsonResponse;

    // Credentials for Yelp API v2
    private static final String consumerKey = "lKr8NPKRqAD5rPbv7KP1nw";
    private static final String consumerSecret = "PK3vxdEJ6eabb0dSLZN7SYqefAc";
    private static final String token = "s-8w_o4dlaX4Hk1HyRHoEgWdI71xjNks";
    private static final String tokenSecret = "82b0axKIoOik5YCJzsL1-JslHU4";

    // Credentials for Yelp API v3
    private static final String accessToken = "access_token"; // "ACCESS_TOKEN"
    private static final String tokenType = "token_type"; // "bearer"
    private static final String expiresIn = "expires_in"; // 15552000

    private static final int search_limit = 20;





    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        Bundle bundleIntentExtras = intent.getExtras(); //getIntent().getExtras();

//        Log.v(TAG, bundleIntentExtras.toString());
//        Log.v(TAG, bundleIntentExtras.getString("latitude") + "," + bundleIntentExtras.getString("longitude"));
//        Log.v(this.getLocalClassName(), intent.getDataString()); //Log.v(this.getLocalClassName(), intent.getData().toString());
        //Log.v(this.getLocalClassName(), intent.toString()); // Prints an output that looks like JSON data
        //Log.v(this.getLocalClassName(), intent.getAction());

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // SearchManager.QUERY returns the string "query" and intent.getStringExtra looks for a
            // parameter "query" that was added with putExtra(). It this case, it generates an error!
            //String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(intent.getDataString());
        }

        // Create the basic Yelp API objects
        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        // Create a Map to pass parameters to the Yelp API object
        Map<String, String> requestParameters = new HashMap<>();

        // Add some General Search Parameters
        //requestParameters.put("term", intent.getDataString());
        //requestParameters.put("term", bundleIntentExtras.getString("query"));
//        requestParameters.put("term", "bar");
        requestParameters.put("category_filter", "bars");
        requestParameters.put("sort", "1");
        requestParameters.put("limit", Integer.toString(search_limit));

        // Add Locale Parameters
//        requestParameters.put("cc", "se");
        requestParameters.put("lang", "en");

        // Create the Yelp API search call and add Location, cll Parameters, CoordinateOptions, BoundingBoxOptions and other request parameters
        Call<SearchResponse> call = null;

        if(bundleIntentExtras.getString("latitude") == null){
            String location = "Gamla Stan, Stockholm";
            call = yelpAPI.search(location, requestParameters);
        }else{
            CoordinateOptions coordinateOptions = CoordinateOptions.builder()
                    .latitude(Double.valueOf(bundleIntentExtras.getString("latitude"))) // returns Double object
                    .longitude(Double.valueOf(bundleIntentExtras.getString("longitude"))) // returns Double object
//                    .longitude(Double.parseDouble(bundleIntentExtras.getString("longitude"))) // returns double primitive value
                    .build();
//            call = yelpAPI.search(coordinateOptions, requestParameters);
            String location = "Gamla Stan, Stockholm";
            call = yelpAPI.search(location, requestParameters);
        }

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Searching...");
        pDialog.show();

        // Execute the Call request
//        try {
//            // The Call execution MUST be done in an AsyncTask or with a Callback, otherwise it will generate a NetworkOnMainThreadException error.
//            //retrofit2.Response<SearchResponse> response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Set the call to be executed asynchronously
        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                //pDialog.hide(); // Potentially creates a window leak
                pDialog.dismiss();

                SearchResponse searchResponse = response.body();

                // Update UI text with the searchResponse.
//                Log.v(TAG, searchResponse.total().toString());
//                for (int i = 0; i < search_limit; i++) {
//                    Log.v(TAG, searchResponse.businesses().get(i).name());
//                }
                if(response.body().total() != 0) {
                    ArrayList<Business> businesses = searchResponse.businesses();

                    BusinessAdapter businessAdapter = new BusinessAdapter(SearchResultsActivity.this, businesses);

                    ListView listView = (ListView) findViewById(R.id.search_results_list_view);

                    listView.setAdapter(businessAdapter);
                }else{
                    Toast.makeText(SearchResultsActivity.this, "No search results!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
                //pDialog.hide();
                Log.v(TAG, "", t);
                pDialog.dismiss();
            }
        }; // Callback<SearchResponse> callback = new Callback<SearchResponse>()

        call.enqueue(callback);
    } // protected void onCreate(Bundle savedInstanceState)

} // public class SearchResultsActivity extends AppCompatActivity
