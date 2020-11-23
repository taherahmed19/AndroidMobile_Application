package com.example.myapplication.Fragments.MapFeedSearchFragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myapplication.Fragments.MapFragment.MapFragment;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.Interfaces.CurrentLocationListener;
import com.example.myapplication.Models.CurrentLocation.CurrentLocation;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Place;

public class MapFeedSearchFragment extends Fragment implements CurrentLocationListener {

    public final static String TAG = MapFeedSearchFragment.class.getName();

    MapFragment mapFragment;
    MapFeedSearchFragmentHandler mapFeedSearchFragmentHandler;
    FragmentSearchListener listener;
    CurrentLocation currentLocation;

    public MapFeedSearchFragment(MapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.mapFeedSearchFragmentHandler = new MapFeedSearchFragmentHandler(mapFragment, this, this);
    }

    @Override
    public void updateUserLocation(Location location) {
        Log.d("Print", "MapFeedSearchFragment callback");
        this.mapFeedSearchFragmentHandler.updateUserLocation(location);
    }

    public interface FragmentSearchListener {
        void onInputSearchSent(CharSequence input);
        void onSearchTextChanged(Place place, String mainText, String secondText);
        void onTriggerResultsClear();
        int checkSearchFieldLength();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.currentLocation = new CurrentLocation(getActivity(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_feed_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.mapFeedSearchFragmentHandler.configureElements();
    }

    public int searchFieldLength(){
        return this.mapFeedSearchFragmentHandler.searchFieldLength();
    }

    public void updateEditText(CharSequence newText) {
        this.mapFeedSearchFragmentHandler.updateEditText(newText);
    }

    public void showErrorFragment(){
        //mapFeedSearchFragmentHandler.showErrorFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(mapFragment != null && mapFragment instanceof FragmentSearchListener){
            this.mapFeedSearchFragmentHandler.setListener((FragmentSearchListener) mapFragment);
        }else{
            throw new RuntimeException(mapFragment.toString() + " must implemented FragmentSearchListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.mapFeedSearchFragmentHandler.setListener(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        currentLocation.startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentLocation.stopLocationUpdates();
    }
}