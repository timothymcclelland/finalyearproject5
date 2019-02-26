package com.example.uuj.finalyearproject;

//android imports

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetNearbyChurches extends AsyncTask<Object, String, String>
{
    //used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj tutorial set in the creation of this class

    //Class member variables
    String url;
    GoogleMap nMap;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;

    public GetNearbyChurches(GoogleMapsActivity googleMapsActivity) {
    }

    @Override
    protected String doInBackground(Object... objects) {

        //referencing variables to java objects
        nMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        try
        {
            URL myUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) myUrl.openConnection();
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine())!=null)
            {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();

        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return data;
    }

    //method to display the church data in the nearbyChurchesList list using the displayNearbyChurches method
    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i =0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                JSONObject loctationObj = jsonObj.getJSONObject("geometry").getJSONObject("location");

                String latitude = loctationObj.getString("lat");
                String longitude = loctationObj.getString("lng");

                JSONObject nameObject = jsonArray.getJSONObject(i);

                String name_of_Church = nameObject.getString("name");
                String vicinity = nameObject.getString("vicinity");

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(vicinity);
                markerOptions.position(latLng);

                nMap.addMarker(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
