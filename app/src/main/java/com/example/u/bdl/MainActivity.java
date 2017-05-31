package com.example.u.bdl;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;

import android.support.v7.widget.Toolbar;

import android.os.Bundle;

import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity   extends AppCompatActivity
                            implements  GoogleApiClient.ConnectionCallbacks,
                                        GoogleApiClient.OnConnectionFailedListener,
                                        LocationListener,
                                        BaseSliderView.OnSliderClickListener,
                                        ViewPagerEx.OnPageChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

//    private Resources resources = null;
//    private View rootView = null;

    private MaterialSearchView mMaterialSearchView = null;

    private GoogleApiClient mGoogleApiClient = null;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = null;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks = null;

    // Used to configure the Location Request's parameters such as setInterval(), setFastInterval() and setPriority()
    private LocationRequest mLocationRequest = null;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 sec */

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1234;

    private Location mCurrentLocation = null;
    protected Location mLastLocation = null;

    private AddressResultReceiver mResultReceiver;

    private boolean mAddressRequested = false;

//    private SliderLayout imageSlider;



    /**
     * Called when Activity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Always call the superclass first
        setContentView(R.layout.activity_main);

//        rootView = findViewById(R.id.root_layout);

        // Create a Toolbar
        Toolbar toolbarWidget = (Toolbar) findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbarWidget);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbarWidget.setElevation(getResources().getDimension(R.dimen.toolbar_elevation)); // Requires API 21

// *************** Create the bottom navigation bar using Aurel Hubert's AHBottomNavigation library
        // Create AHBottomNavigation object
        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create the bottom navigation bar creating all items individually and then adding them to the AHBottomNavigation object
        // Create bottom navigation items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.menu_item_home, R.drawable.ic_home_black_24dp, R.color.bottom_navigation_item_color);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.menu_item_offers, R.drawable.ic_local_offer_black_24dp, R.color.bottom_navigation_item_color);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.menu_item_events, R.drawable.ic_event_black_24dp, R.color.bottom_navigation_item_color);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.menu_item_artists, R.drawable.ic_local_activity_black_24dp, R.color.bottom_navigation_item_color);

        // Add the items to the bar
        bottomNavigation.addItem(item1);
//        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

//        resources = this.getResources();
//        Log.v(TAG, String.valueOf(resources.getDimensionPixelSize(R.dimen.bottom_navigation_height)));

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#b71c1c")); //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

        // Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

        // Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#ffffff")); //bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#000000")); //bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        // Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);


        // Set AHBottomNavigation listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                //Log.v(TAG, "position: " + Integer.toString(position) + " | wasSelected: " + wasSelected);
                switch (position) {
                    case 0:
                        Item1Fragment f1 = new Item1Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, f1).commit();
//                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Item2Fragment f2 = new Item2Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, f2).commit();
//                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Item3Fragment f3 = new Item3Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, f3).commit();
//                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Item4Fragment f4 = new Item4Fragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, f4).commit();
//                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        }); // bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener()

        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });


// *************** Show the initial fragment in the activity
        // Set the initial fragment
        Item1Fragment f = new Item1Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, f).commit();

//        imageSlider = (SliderLayout) findViewById(R.id.slider);
//
////        HashMap<String,String> sliderImages = new HashMap<String, String>();
////        sliderImages.put("Hannibal",
////                "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
////        sliderImages.put("Big Bang Theory",
////                "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
////        sliderImages.put("House of Cards",
////                "http://cdn3.nflximg.net/images/3093/2043093.jpg");
////        sliderImages.put("Game of Thrones",
////                "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");
//        HashMap<String,Integer> sliderImagesHashMap = new HashMap<String, Integer>();
//        sliderImagesHashMap.put("Tritonia", R.drawable.blkbrd_1);
//        sliderImagesHashMap.put("Stampen", R.drawable.blkbrd_2);
//        sliderImagesHashMap.put("Pharmarium", R.drawable.blkbrd_3);
//        sliderImagesHashMap.put("The Liffey", R.drawable.blkbrd_4);
//
//        for(String name : sliderImagesHashMap.keySet()){
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a imageSlider
//            textSliderView
//                .description(name)
//                .image(sliderImagesHashMap.get(name))
//                .setScaleType(BaseSliderView.ScaleType.Fit)
//                .setOnSliderClickListener(this);
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra",name);
//            imageSlider.addSlider(textSliderView);
//        }
//        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        imageSlider.setCustomAnimation(new DescriptionAnimation());
//        imageSlider.setDuration(2000);
//        imageSlider.addOnPageChangeListener(this);

//        // Create a OnQueryTextListener for the SearchView
//        // There are differences between android.widget.SearchView and android.support.v7.widget.SearchView
//        // Only ONE should be listed in the imports!
//        SearchView searchView = (SearchView) findViewById(R.id.action_bar_menu_item_search);
////        SearchView searchView = (SearchView) findViewById(R.id.search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //Toast.makeText(MainActivity.this, "Query string: " + query, Toast.LENGTH_SHORT).show();
//                // Start a new activity
//
//                //// Use Intent constructor Intent(Context packageContext, Class<?> cls). Create an intent for a specific component.
//                //Intent searchResultsIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
//
//                // Use Intent constructor Intent(String action, Uri uri, Context packageContext, Class<?> cls). Create an intent for a specific component with a specified action and data.
//                // Create the necessary Uri. URI and Uri are different!!! Uri is android.net URI is java.net
//                Uri searchUri = Uri.parse(query);
//                Intent searchResultsIntent = new Intent(Intent.ACTION_SEARCH, searchUri, MainActivity.this, SearchResultsActivity.class);
//                startActivity(searchResultsIntent);
//
////                if(query.length() > 0) {
////                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
////                    SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
////                    Bundle args = new Bundle();
////                    args.putString("query_string", query);
////                    searchResultsFragment.setArguments(args);
////
////                    fragmentTransaction.replace(R.id.frame_layout, searchResultsFragment);
////                    fragmentTransaction.addToBackStack(null);
////                    fragmentTransaction.commit();
////                }
//
//                // These two lines are needed to hide/dismiss the keyboard if we return true instead of false
//                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                return true; // Returning flase closes the keyboard
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //Toast.makeText(MainActivity.this, "Text: " + newText, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        }); // searchView.setOnQueryTextListener
//
//
//
//        // Create an OnFocusChangeListener to hide/dismiss the keyboard when the SearchView is not focused
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                Log.v(TAG, "View: " + v + " | hasFocus: " + hasFocus);
//                if(!hasFocus){
////                    Log.v(TAG, "View: " + v + " | hasFocus: " + hasFocus);
////                    InputMethodManager inputMethodManager = (InputMethodManager)MainActivity.this.getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
////                    inputMethodManager.hideSoftInputFromWindow(MainActivity.this.findViewById(R.id.search_view).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.root_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//        }); // searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()


// *************** Set up the Main menu's PopupMenu
        // Create OnClickListener for the menu ImageView
        ImageButton menuImageButton = (ImageButton) findViewById(R.id.menu_image_button);
        menuImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove focus from other layout elements
//                Log.v(TAG, MainActivity.this.getCurrentFocus().toString());
                //Log.v(TAG, String.valueOf(MainActivity.this.getCurrentFocus().isFocused()));
//                MainActivity.this.findViewById(R.id.search_view).clearFocus(); // This works!
//                MainActivity.this.getCurrentFocus().clearFocus(); // This half worked for an androd.support.v7.widget.SearchView
//                Log.v(TAG, MainActivity.this.getCurrentFocus().toString());
//                v.requestFocusFromTouch();
//                v.clearFocus();

                // Create the PopupMenu object for the Main menu
                PopupMenu popupMenuMain = new PopupMenu(MainActivity.this, v, Gravity.CENTER);
                // Inflate the menu. The following two lines are equivalent
                //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenuMain.inflate(R.menu.popup_menu);

                // Create OnMenuItemClick listener
                popupMenuMain.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Log.v(TAG, "ItemId: " + item.getItemId() + " | Title: " + item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.popup_menu_item_favorites:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_profile:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_settings:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_about:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                }); // menuImageButton.setOnClickListener(new View.OnClickListener()
                popupMenuMain.show();
            } // public void onClick(View v)
        }); //menuImageButton.setOnClickListener(new View.OnClickListener()

// *************** Set up the Location's PopupMenu
        // Create OnClickListener for the ImageView
        ImageButton locationImageButton = (ImageButton) findViewById(R.id.location_image_button);
        locationImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the PopupMenu object for the Main menu
                PopupMenu popupMenuLocation = new PopupMenu(MainActivity.this, v, Gravity.CENTER);
                // Inflate the menu. The following two lines are equivalent
                //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenuLocation.inflate(R.menu.popup_menu_location);

                // Create OnMenuItemClick listener
                popupMenuLocation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Log.v(TAG, "ItemId: " + item.getItemId() + " | Title: " + item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.popup_menu_item_favorites:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_profile:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_settings:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_item_about:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                }); // menuImageButton.setOnClickListener(new View.OnClickListener()
                popupMenuLocation.show();
            } // public void onClick(View v)
        }); //menuImageButton.setOnClickListener(new View.OnClickListener()

// *************** Set up the Favorites's PopupMenu
        // Create OnClickListener for the ImageView
        ImageButton favoritesImageButton = (ImageButton) findViewById(R.id.favorites_image_button);
        favoritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the PopupMenu object for the Main menu
                PopupMenu popupMenuLocation = new PopupMenu(MainActivity.this, v, Gravity.CENTER);
                // Inflate the menu. The following two lines are equivalent
                //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenuLocation.inflate(R.menu.popup_menu_favorites);

                // Create OnMenuItemClick listener
                popupMenuLocation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Log.v(TAG, "ItemId: " + item.getItemId() + " | Title: " + item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.popup_menu_favorites_item_1:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_favorites_item_2:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.popup_menu_favorites_item_3:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                }); // menuImageButton.setOnClickListener(new View.OnClickListener()
                popupMenuLocation.show();
            } // public void onClick(View v)
        }); //menuImageButton.setOnClickListener(new View.OnClickListener()

// *************** Set up the Filters's PopupMenu
        // Create OnClickListener for the ImageView
        ImageButton filtersImageButton = (ImageButton) findViewById(R.id.filters_image_button);
        filtersImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create the PopupMenu object for the Main menu
                PopupMenu popupMenuLocation = new PopupMenu(MainActivity.this, v, Gravity.CENTER);
                // Inflate the menu. The following two lines are equivalent
                //popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenuLocation.inflate(R.menu.popup_menu_filters);

                // Create OnMenuItemClick listener
                popupMenuLocation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Log.v(TAG, "ItemId: " + item.getItemId() + " | Title: " + item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.popup_menu_filters_configure:
                                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }
                    }
                }); // menuImageButton.setOnClickListener(new View.OnClickListener()
                popupMenuLocation.show();
            } // public void onClick(View v)
        }); //menuImageButton.setOnClickListener(new View.OnClickListener()

// *************** Set up the SearchView using Miguel Catalan's MaterialSearchView library
        mMaterialSearchView = (MaterialSearchView) findViewById(R.id.material_search_view);
        mMaterialSearchView.setVoiceSearch(false);
        mMaterialSearchView.setCursorDrawable(R.drawable.custom_cursor);

        // Create Listeners
        mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundleIntentExtras = new Bundle();
                bundleIntentExtras.putString("query", query);

                if ((ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                        (ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
                    Log.v(TAG, "After requesting permissions!");
                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
//                    query += " " + String.valueOf(mLastLocation.getLatitude()) + " " + String.valueOf(mLastLocation.getLongitude());
                    bundleIntentExtras.putString("latitude", String.valueOf(mLastLocation.getLatitude()));
                    bundleIntentExtras.putString("longitude", String.valueOf(mLastLocation.getLongitude()));
                }else{
                    Log.v(TAG, "Location is unknown and could not be determined!");
                }
//                Snackbar.make(findViewById(R.id.frame_layout), "Query: " + query, Snackbar.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, "Query: " + query, Toast.LENGTH_SHORT).show();
//                Toast.makeText(MainActivity.this, "Query: " + bundleIntentExtras.toString(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this,
//                        "query: " + bundleIntentExtras.getString("query") +
//                        " latitude: " + bundleIntentExtras.getString("latitude") +
//                        " longitude: " + bundleIntentExtras.get("longitude"),
//                        Toast.LENGTH_LONG).show();
//                Uri searchUri = Uri.parse(query);
//                Intent searchResultsIntent = new Intent(Intent.ACTION_SEARCH, searchUri, MainActivity.this, SearchResultsActivity.class);
                Intent searchResultsIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
                searchResultsIntent.setAction(Intent.ACTION_SEARCH);
                searchResultsIntent.putExtras(bundleIntentExtras);
                startActivity(searchResultsIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        }); // mMaterialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()

        mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        }); // mMaterialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener()


// *************** Create the Google Play Services API client object
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    //.enableAutoManage(this, this) // This manages connections with automatically resolved errors (ConnectionCallbacks & OnConnectionFailedListener MUST be implemented (the Activity or Fragment MUST "implements" these interfaces and no need to use connect() in onStart & disconnect() in onStop
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API) //
                    .addApi(Places.GEO_DATA_API) //
                    .addApi(Places.PLACE_DETECTION_API) //
                    .build();
        }


// *************** Set up location services using LocationManager
//        // Create a LocationManager object
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        // Check if GPS is enabled or not
//        ContentResolver contentResolver = getBaseContext().getContentResolver();
//        boolean isGpsEnabled = Settings.Secure.isLocationProviderEnabled()


    } // protected void onCreate


    /**
     * Activity's Lifecycle methods:
     * onCreate -
     * onStart -
     * onResume - Activity comes to the foreground
     * onPause - Activity is still partially visible but not in focus, e.g., a new, semi-transparent activity (such as a dialog) opens
     * onStop - Activity is no longer visible, e.g., a newly launched activity covers the entire screen.
     * onDestroy -
     */
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect(); // Needed when GoogleApiClient is NOT automanaged
    }

    @Override
    protected void onResume() {
        super.onResume();
//        rootView.requestFocus();
    }

    @Override
    protected void onStop() {
        //super.onStop();
//        imageSlider.stopAutoCycle();

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect(); // Needed when GoogleApiClient is NOT automanaged
        }

        super.onStop();
    }


    /**
     * Called when the App connects to Google Play and location services APIs
     * The following methods MUST be implemented to work with Google APIs:
     *
     * In GoogleApiClient.ConnectionCallbacks:
     * onConnected - Called when the App connects to Google Play and location services APIs
     * onConnectionSuspend - alled when the Google Play and location services connection is suspended
     *
     * In GoogleApiClient.OnConnectionFailedListener:
     * onConnectionFailed - alled when the App fails to connect to Google Play and location services APIs
     *
     * In LocationListener:
     * onLocationChanged - Called when we configure an updatable LocationRequest
     * onStatusChanged -
     * onProviderEnabled -
     * onProviderDisabled -
     *
     * NOTE: These methods belong to "Interfaces" therefore we must add them to our class/Activity/Fragment using "implements"
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Gets the best and most recent location currently available (without updates),
        // which may be null in rare cases when a location is not available.

        // This code was moved to query submit, which is where the location is needed
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = new String[3];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            //permissions[2] = Manifest.permission.ACCESS_NETWORK_STATE;
            //ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_ACCESS_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
//            Toast.makeText(this, Manifest.permission.ACCESS_FINE_LOCATION + ": " + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION), Toast.LENGTH_LONG).show();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Note that this can be NULL if last location isn't already known.
        if (mLastLocation != null) {
            // Print current location if not null
//            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
//            Log.v(TAG, "current location: " + mLastLocation.toString());
//            Log.v(TAG, "Latitude: " + String.valueOf(mLastLocation.getLatitude()) + " | Longitude: " + String.valueOf(mLastLocation.getLongitude()));
//            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            Log.v(TAG, latLng.toString());

            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available,
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (mAddressRequested) {
                startIntentService();
            }
        }
        // Set up and request and updatable location
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(10000); // Update location every ten seconds
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        // Begin polling for new location updates.
        startLocationUpdates();
    } // public void onConnected(@Nullable Bundle bundle)

    @Override
    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "GoogleApiClient connection has been suspend");
        Log.v(TAG, "GoogleApiClient connection has been suspend");
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(MainActivity.this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(MainActivity.this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.i(TAG, "GoogleApiClient connection has failed");
        Log.v(TAG, "GoogleApiClient connection has failed");
        Toast.makeText(this, "GoogleApiClient connection has failed" + connectionResult.toString(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onLocationChanged(Location location) {
//        Log.i(TAG, "Location received: " + location.toString());
        Log.v(TAG, "Location received: " + location.toString());
        Toast.makeText(this, "Location received: " + location.toString(), Toast.LENGTH_LONG).show();
        mLastLocation = location;
    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * Creates and sets up LocationRequest and triggers location updates at intervals
     */
    protected void startLocationUpdates() {
        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

//        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder();
//        locationSettingsRequestBuilder.addLocationRequest(mLocationRequest);
//
//        PendingResult<LocationSettingsResult> locationSettingsResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
//
//        locationSettingsResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//                final Status status = locationSettingsResult.getStatus();
//                final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can
//                        // initialize location requests here.
//                        break;
////                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(
//                                    OuterClass.this,
//                                    REQUEST_CHECK_SETTINGS);
//                        } catch (SendIntentException e) {
//                             Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way
//                        // to fix the settings so we won't show the dialog.
//                        break;
//                }
//            }
//        }); // locationSettingsResult.setResultCallback(new ResultCallback<LocationSettingsResult>()
//
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
            Log.v(TAG, "After requesting permissions!");
            return;
        }

        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    } // protected void startLocationUpdates()





    /**
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 0) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.v(TAG, String.valueOf(grantResults.length) + " " + grantResults[0] + " " + grantResults[1]);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission to access FINE & COARSE location was denied!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }





    /**
     * Creates the action bar menu using a Miguel Catalan's MaterialSearchView
     * @param menu - Menu object
     * @return -
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_bar_menu_item_search);
        mMaterialSearchView.setMenuItem(item);

        return true;
    } // public boolean onCreateOptionsMenu(Menu menu)





/*
    @Override
    public void onBackPressed() {
        if (mMaterialSearchView.isSearchOpen()) {
            mMaterialSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
*/





/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mMaterialSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/


    /**
     * Creates the action bar menu using a SearchView
     * @param menu
     * @return
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //return super.onCreateOptionsMenu(menu);
//        // Inflate the options menu from XML; this adds items to the action bar if present.
//        // Two line implementation
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.action_bar_menu, menu);
//        // One line implementation
//        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
//
//        // Create the SearchManager to put in the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        // Use "menu" to get the android.widget.SearchView
////        SearchView searchView = (SearchView) menu.findItem(R.id.action_bar_menu_item_search).getActionView(); //SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
//        // Use MenuItemCompat to get the android.support.v7.widget.SearchView
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_bar_menu_item_search));
//
//        // Assumes current activity is the searchable activity
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        // Uses another activity as the searchable activity
//        // Two line implementation
////        ComponentName componentName = new ComponentName(this, SearchResultsActivity.class);
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
//        // One line implementation
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(MainActivity.this, SearchResultsActivity.class)));
//
//        // Do not iconify the widget; expand it by default
//        searchView.setIconifiedByDefault(false);
//        searchView.setQueryHint("Search...");
//        return true;
//    } // public boolean onCreateOptionsMenu(Menu menu)






//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //return super.onOptionsItemSelected(item);
//        Log.v(TAG, "ItemId: " + item.getItemId() + " | Title: " + item.getTitle());
//        switch(item.getItemId()){
//            case R.id.action_bar_menu_item_home: //action_bar_menu_item1:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_offers: //action_bar_menu_item2:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_events: //action_bar_menu_item3:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_artists: //action_bar_menu_item4:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_search: //action_bar_menu_item5:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_settings: //action_bar_menu_item6:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.action_bar_menu_item_about: //action_bar_menu_item7:
//                Toast.makeText(MainActivity.this, item.getTitle() + " was selected", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    } // public boolean onOptionsItemSelected(MenuItem item)






//    // Request code to use when launching the resolution activity
//    private static final int REQUEST_RESOLVE_ERROR = 1001;
//    // Unique tag for the error dialog fragment
//    private static final String DIALOG_ERROR = "dialog_error";
//    // Bool to track whether the app is already resolving an error
//    private boolean mResolvingError = false;
//
//    // ...
//
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (mResolvingError) {
//            // Already attempting to resolve an error.
//            return;
//        } else if (result.hasResolution()) {
//            try {
//                mResolvingError = true;
//                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
//            } catch (IntentSender.SendIntentException e) {
//                // There was an error with the resolution intent. Try again.
//                mGoogleApiClient.connect();
//            }
//        } else {
//            // Show dialog using GoogleApiAvailability.getErrorDialog()
//            showErrorDialog(result.getErrorCode());
//            mResolvingError = true;
//        }
//    }
//
//    // The rest of this code is all about building the error dialog
//
//    /* Creates a dialog for an error message */
//    private void showErrorDialog(int errorCode) {
//        // Create a fragment for the error dialog
//        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
//        // Pass the error that should be displayed
//        Bundle args = new Bundle();
//        args.putInt(DIALOG_ERROR, errorCode);
//        dialogFragment.setArguments(args);
//        dialogFragment.show(getSupportFragmentManager(), "errordialog");
//    }
//
//    /* Called from ErrorDialogFragment when the dialog is dismissed. */
//    public void onDialogDismissed() {
//        mResolvingError = false;
//    }
//
//    /* A fragment to display an error dialog */
//    public static class ErrorDialogFragment extends DialogFragment {
//        public ErrorDialogFragment() { }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Get the error code and retrieve the appropriate dialog
//            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
//            return GoogleApiAvailability.getInstance().getErrorDialog(
//                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
//        }
//
//        @Override
//        public void onDismiss(DialogInterface dialog) {
//            ((MainActivity) getActivity()).onDialogDismissed();
//        }
//    }





    /**
     * Method starts an IntentService for Geocoding
     */
    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }





    /**
     * Services the onClick method for a button on the app's UI
     * @param view - View that called this method
     */
    public void fetchAddressButtonHandler(View view) {
        // Only start the service to fetch the address if GoogleApiClient is
        // connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        // If GoogleApiClient isn't connected, process the user's request by
        // setting mAddressRequested to true. Later, when GoogleApiClient connects,
        // launch the service to fetch the address. As far as the user is
        // concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
//        updateUIWidgets();
    }





    /**
     * Handles the response from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
//            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
//            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
//                showToast(getString(R.string.address_found));
                Toast.makeText(MainActivity.this, "Address Found!!!", Toast.LENGTH_SHORT).show();
            }
        } // protected void onReceiveResult(int resultCode, Bundle resultData)
    } // class AddressResultReceiver extends ResultReceiver

}// public class MainActivity extends AppCompatActivity {
