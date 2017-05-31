package com.example.u.bdl;

//import android.app.Fragment; // This causes "Error:(120, 99) error: incompatible types: Item1Fragment cannot be converted to Fragment"
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.games.GamesMetadata;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.Category;
import com.yelp.clientlib.entities.Deal;
import com.yelp.clientlib.entities.GiftCertificate;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by u on 2017-05-17.
 */

public class Item1Fragment  extends Fragment
                            implements  BaseSliderView.OnSliderClickListener,
                                        ViewPagerEx.OnPageChangeListener {

    Context context;

    private static final String TAG = "Item1Fragment";

    private GridView gridView;



    public Item1Fragment(){

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        // Create the layout from the xml resource file
//        View rootView = inflater.inflate(R.layout.item1_fragment_layout, container, false);
//
////        gridView = (GridView) rootView.findViewById(R.id.search_results_list_view);
//
//        String[] letters = new String[] { "A", "B", "C", "D", "E",
//                "F", "G", "H", "I", "J",
//                "K", "L", "M", "N", "O",
//                "P", "Q", "R", "S", "T",
//                "U", "V", "W", "X", "Y", "Z"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, letters);
//
////        gridView.setAdapter(adapter);
//
//        return rootView;
        // Inflate the layout for this fragment
//        SliderLayout sliderShow = (SliderLayout)getActivity().findViewById(R.id.slider);// container.findViewById(R.id.slider);
//
//
//
//        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
//        file_maps.put("Tritonia",R.drawable.blkbrd_1);
//        file_maps.put("Stampen",R.drawable.blkbrd_2);
//        file_maps.put("Pharmarium",R.drawable.blkbrd_3);
//        file_maps.put("The Liffey", R.drawable.blkbrd_4);
//
//        for(String name : file_maps.keySet()) {
//            TextSliderView textSliderView = new TextSliderView(getActivity());
//
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(file_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
//            .setOnSliderClickListener(Item1Fragment.this);
//
//            Bundle bundle = new Bundle();
//            textSliderView.bundle(bundle);
//            textSliderView.getBundle().putString("extra", name);
//            sliderShow.addSlider(textSliderView);
//        }
//        sliderShow.setPresetTransformer(SliderLayout.Transformer.Fade);
//        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//        sliderShow.setCustomAnimation(new DescriptionAnimation());
//        sliderShow.setDuration(1000);
//        sliderShow.addOnPageChangeListener(Item1Fragment.this);

        return inflater.inflate(R.layout.item1_fragment_layout, container, false);
    }


  /*  @Nullable
    @Override
    public View getView() {
        ArrayList<Business> businesses = new ArrayList<Business>();
        businesses.add(Business.builder().id("tritonia").name("Tritonia").imageUrl("blkbrd_1").menuDateUpdated(1496132861L).reviewCount(R.drawable.blkbrd_1).build());
        businesses.add(Business.builder().id("pharmarium").name("Pharmarium").imageUrl("blkbrd_2").menuDateUpdated(1496132861L).reviewCount(R.drawable.blkbrd_2).build());
        businesses.add(Business.builder().id("the-liffey").name("The Liffey").imageUrl("blkbrd_3").menuDateUpdated(1496132861L).reviewCount(R.drawable.blkbrd_3).build());
        businesses.add(Business.builder().id("stampen").name("Stampen").imageUrl("blkbrd_4").menuDateUpdated(1496132861L).reviewCount(R.drawable.blkbrd_4).build());

        OfferAdapter offerAdapter = new OfferAdapter(this.getContext(), businesses);

        if(offerAdapter == null){
            Log.v(TAG, "offerAdapter is null");
        }
        ListView gridView = (ListView) this.getActivity().findViewById(R.id.offers_grid_view);

        gridView.setAdapter(offerAdapter);
        return super.getView();
    }
*/

    public void StartSlider(){

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
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

} // public class Item1Fragment

