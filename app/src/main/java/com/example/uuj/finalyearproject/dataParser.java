package com.example.uuj.finalyearproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class dataParser {
    private HashMap<String, String> getChurch(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String nameOfChurch = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if(!googlePlaceJSON.isNull("name"))
            {
                nameOfChurch = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");

            googlePlaceMap.put("church_name", nameOfChurch);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }

    private List<HashMap<String, String>> getAllChurches(JSONArray jsonArray)
    {
        int counter = jsonArray.length();
        List<HashMap<String, String>> nearbyChurchesList = new ArrayList<>();

        HashMap<String, String> nearbyChurchesMap = null;

        for(int i=0; i<counter; i++)
        {
            try {
                nearbyChurchesMap = getChurch((JSONObject) jsonArray.get(i));
                nearbyChurchesList.add(nearbyChurchesMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return nearbyChurchesList;
    }

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
