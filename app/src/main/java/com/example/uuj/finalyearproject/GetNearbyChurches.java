package com.example.uuj.finalyearproject;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyChurches extends AsyncTask<Object, String, String>
{

    private String googleplaceData, url;
    private GoogleMap nMap;

    @Override
    protected String doInBackground(Object... objects) {

        nMap= (GoogleMap) objects[0];
        url = (String) objects[1];

        downloadUrl downloadUrl = new downloadUrl();
        try {
            googleplaceData = downloadUrl.readURl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyChurchesList = null;
        dataParser dataParser = new dataParser();
        nearbyChurchesList = dataParser.parse(s);

        displayNearChurches(nearbyChurchesList);
    }

    private void displayNearChurches(List<HashMap<String, String>> nearbyChurchesList)
    {
        for(int i=0; i<nearbyChurchesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyChurch = nearbyChurchesList.get(i);
            String nameOfChurch = googleNearbyChurch.get("church_name");
            String vicinity = googleNearbyChurch.get("vicinity");
            double lat = Double.parseDouble(googleNearbyChurch.get("lat"));
            double lng = Double.parseDouble(googleNearbyChurch.get("lng"));

            LatLng latLng = new LatLng(lat,lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfChurch + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            nMap.addMarker(markerOptions);
            nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            nMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
    }
}
