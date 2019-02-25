package com.example.uuj.finalyearproject;

//JSON related imports
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//java imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dataParser {

    //used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj tutorial set in the creation of this class

    //method to get name of Church, vicinity, latitude, latitude and reference of church and return within a Hash map
    private HashMap<String, String> getChurch(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googleChurchMap = new HashMap<>();
        String nameOfChurch = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            //checks if name of church json field is empty and then gets json and sets to string namOfChurch
            if(!googlePlaceJSON.isNull("name"))
            {
                nameOfChurch = googlePlaceJSON.getString("name");
            }
            //checks if vicinity json field is empty and then gets json and sets to string namOfChurch
            if(!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            //gets json objects latitude and longitude and sets to corresponding strings
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //gets reference json and sets to reference string
            reference = googlePlaceJSON.getString("reference");

            //HashMap to store all data of a church
            googleChurchMap.put("church_name", nameOfChurch);
            googleChurchMap.put("vicinity", vicinity);
            googleChurchMap.put("lat", latitude);
            googleChurchMap.put("lng", longitude);
            googleChurchMap.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return HashMap
        return googleChurchMap;
    }

    //method to store a list of all the churches nearby collected by the HashMap
    private List<HashMap<String, String>> getAllChurches(JSONArray jsonArray)
    {
        int counter = jsonArray.length();
        List<HashMap<String, String>> nearbyChurchesList = new ArrayList<>();

        HashMap<String, String> nearbyChurchesMap = null;

        for(int i=0; i<counter; i++)
        {
            try {
                //get church json object and add it to jsonArray and then add to the list
                nearbyChurchesMap = getChurch((JSONObject) jsonArray.get(i));
                nearbyChurchesList.add(nearbyChurchesMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //return the list of churches
        return nearbyChurchesList;
    }

    //method to parse the data and pass it into the getAllChurches method as a jsonArray
    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray= jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getAllChurches(jsonArray);
    }
}