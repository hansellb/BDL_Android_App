package com.example.u.bdl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.*;

//import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by u on 2017-05-18.
 *
 */

public class SearchResultsFragment extends Fragment {

    private static final String TAG = "SearchResultsFragment";

//    private JSONObject jsonResponse;

    // Credentials for Yelp API v2
    private static final String consumerKey = "lKr8NPKRqAD5rPbv7KP1nw";
    private static final String consumerSecret = "PK3vxdEJ6eabb0dSLZN7SYqefAc";
    private static final String token = "s-8w_o4dlaX4Hk1HyRHoEgWdI71xjNks";
    private static final String tokenSecret = "82b0axKIoOik5YCJzsL1-JslHU4";

//    // Credentials for Yelp API v3
//    private static final String accessToken = "access_token"; // "ACCESS_TOKEN"
//    private static final String tokenType = "token_type"; // "bearer"
//    private static final String expiresIn = "expires_in"; // 15552000

    private static final int search_limit = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.search_results_layout, container, false);

        // Create the basic Yelp API objects
        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        // Create a Map to pass parameters to the Yelp API object
        Map<String, String> requestParameters = new HashMap<>();

        // Add some General Search Parameters
        Log.v(TAG, getArguments().toString());
        Log.v(TAG, String.valueOf(getArguments().getBundle("query_string")));
        requestParameters.put("term", String.valueOf(getArguments().getBundle("query_string"))); //requestParameters.put("term", intent.getDataString());
        requestParameters.put("sort", "1");
        requestParameters.put("limit", Integer.toString(search_limit));

        // Add Location, cll Parameters, CoordinateOptions, BoundingBoxOptions
        String location = "Kista";

        // Add Locale Parameters
        requestParameters.put("cc", "se");
        requestParameters.put("lang", "en");

        Call<SearchResponse> call = yelpAPI.search(location, requestParameters);

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
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                // Update UI text with the searchResponse.
//                Log.v(TAG, searchResponse.total().toString());
//                for (int i = 0; i < search_limit; i++) {
//                    Log.v(TAG, searchResponse.businesses().get(i).name());
//                }
                if(response.body().total() != 0) {
                    assert searchResponse != null;
                    ArrayList<Business> businesses = searchResponse.businesses();

                    BusinessAdapter businessAdapter = new BusinessAdapter(SearchResultsFragment.this.getContext(), businesses);

                    ListView listView = (ListView) SearchResultsFragment.this.getActivity().findViewById(R.id.search_results_list_view);

                    listView.setAdapter(businessAdapter);
                }else{
                    Toast.makeText(SearchResultsFragment.this.getContext(), "No search results!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };

        call.enqueue(callback);

        return v;
    }
}
