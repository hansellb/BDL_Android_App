package com.example.u.bdl;

//import android.app.Fragment; // This causes "Error:(124, 99) error: incompatible types: Item2Fragment cannot be converted to Fragment"
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by u on 2017-05-17.
 */

public class Item3Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.item3_fragment_layout, container, false);
        return v;
    }
}
