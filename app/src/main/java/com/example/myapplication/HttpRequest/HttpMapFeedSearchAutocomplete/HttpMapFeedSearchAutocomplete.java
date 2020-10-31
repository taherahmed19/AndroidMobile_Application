package com.example.myapplication.HttpRequest.HttpMapFeedSearchAutocomplete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.myapplication.Fragments.MapFeedSearchFragment.MapFeedSearchFragment;
import com.example.myapplication.Handlers.MapFeedSearchAutocompleteHandler.MapFeedSearchAutocompleteHandler;
import com.example.myapplication.Handlers.MapFeedSearchFragmentHandler.MapFeedSearchFragmentHandler;
import com.example.myapplication.JsonBuilders.MapFeedSearchAutocompleteJsonBuilder.MapFeedSearchAutocompleteJsonBuilder;
import com.example.myapplication.R;
import com.example.myapplication.Refactor.searchAutocomplete.Places;
import com.example.myapplication.Refactor.searchAutocomplete.PlacesFinder;
import com.example.myapplication.Utils.SSL.SSL;
import com.example.myapplication.Utils.Tools.Tools;
import com.google.android.gms.maps.model.LatLng;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;

public class HttpMapFeedSearchAutocomplete extends AsyncTask<String, Void, Places> {

    static final String QUERY = "https://maps.googleapis.com/maps/api/place/queryautocomplete/json?key={0}&input={1}";

    @SuppressLint("StaticFieldLeak")
    Context context;
    MapFeedSearchAutocompleteJsonBuilder mapFeedSearchAutocompleteJsonBuilder;

    URL url = null;
    HttpsURLConnection urlConnection = null;
    int responseCode = 0;

    boolean validResponse;

    MapFeedSearchFragment.FragmentSearchListener listener;

    public HttpMapFeedSearchAutocomplete(Context context, MapFeedSearchFragment.FragmentSearchListener listener) {
        this.context = context;
        this.validResponse = true;
        this.mapFeedSearchAutocompleteJsonBuilder = new MapFeedSearchAutocompleteJsonBuilder();
        this.listener = listener;
    }

    @Override
    protected Places doInBackground(String... strings) {
        SSL.AllowSSLCertificates();
        Places places = null;

        String apiRequest = this.createApiQuery(strings[0]);

        try {
            String response = handleRequest(apiRequest);

            if(!TextUtils.isEmpty(response)){
                places = mapFeedSearchAutocompleteJsonBuilder.jsonHandler(response);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return places;
    }

    @Override
    protected void onPostExecute(Places places) {
        listener.onTriggerResultsClear();
        for(int i = 0; i < places.getPlaces().size(); i++){
            listener.onSearchTextChanged(places.getPlaces().get(i), places.getPlaces().get(i).getMainText(), places.getPlaces().get(i).getSecondText());
        }
    }

    String handleRequest(String apiRequest){
        String response = null;

        try {
            url = new URL(apiRequest);
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(SSL.DUMMY_VERIFIER);

            response = handleResponse();

        }catch (Exception e){
            e.printStackTrace();
        }

        return response;
    }

    String handleResponse(){
        try {
            responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                return Tools.readStream(urlConnection.getInputStream());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    String createApiQuery(String input){
        return MessageFormat.format(HttpMapFeedSearchAutocomplete.QUERY, this.context.getResources().getString(R.string.GOOGLE_API_KEY_2), Tools.encodeString(input));
    }
}
