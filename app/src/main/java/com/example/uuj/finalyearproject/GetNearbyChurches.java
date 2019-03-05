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

//google imports
//json imports
//java imports

public class GetNearbyChurches extends AsyncTask<Object, String, String>
{
    /*used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj &
    https://www.youtube.com/watch?v=oOVRNxPtfeQ&list=PLF0BIlN2vd8und4ajF-bdFI3jWyPTXxB5
    tutorial sets in the creation of this class*/

    /*used https://developers.google.com/places/web-service/intro &
    https://developers.google.com/maps/documentation/android-sdk/intro as guidance in the creation of this class*/

    /*account set up on https://console.cloud.google.com/google/maps-apis/overview?project=finalyearproject-c85ba&folder=&organizationId=
    for access to maps SDK, places API and other APIs included with the google maps functionality*/

    //Class member variables
    String url;
    GoogleMap nMap;
    InputStream inputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;

    //empty constructor required for GoogleMapsActivity.java to call this class
    public GetNearbyChurches(GoogleMapsActivity googleMapsActivity) {
    }

    /*method used to open the connection to the google maps search in the background
    and search google maps based on the url (which includes the users location and data requested), that is to be read by the bufferedReader and then turned into a string.
    This string data will then be used by the onPostExecute method to process the data and return the results which will then be processed and presented to the user
    within the GoogleMapsActivity.java findNearbyChurches method*/
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

    //method to transform the church data using the json objects from the jsonArray
    /*the data is then used to set the markers for the identified church locations based on the name of the church
    and latitude/longitude of the location of the church*/
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
                markerOptions.title(name_of_Church);
                markerOptions.position(latLng);

                nMap.addMarker(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
