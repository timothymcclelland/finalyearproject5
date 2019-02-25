package com.example.uuj.finalyearproject;

//android imports
import android.os.AsyncTask;

//google imports
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//java imports
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyChurches extends AsyncTask<Object, String, String>
{
    //used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj tutorial set in the creation of this class


    //Class member variables
    private String googlePlaceData, url;
    private GoogleMap nMap;

    @Override
    protected String doInBackground(Object... objects) {

        //referencing variables to java objects
        nMap= (GoogleMap) objects[0];
        url = (String) objects[1];

        //creating object of downloadUrl and passing value from downloadUrl method
        downloadUrl downloadUrl = new downloadUrl();
        try {
            googlePlaceData = downloadUrl.readURl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaceData;
    }

    //method to display the church data in the nearbyChurchesList list using the displayNearbyChurches method
    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyChurchesList = null;
        dataParser dataParser = new dataParser();
        nearbyChurchesList = dataParser.parse(s);

        displayNearChurches(nearbyChurchesList);
    }

    //method to display nearby churches from the list created in dataParser.java, based on user's current location
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
