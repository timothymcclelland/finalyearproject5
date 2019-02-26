package com.example.uuj.finalyearproject;

//android imports
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//google imports
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//java imports
import java.io.IOException;
import java.util.List;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj tutorial set in the creation of this class

    //Class member variables
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private double latitude, longitude;
    private static final int Request_User_Location_Code = 99;
    private int proximityRadius = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        //Checks if SDK version is greater than or equal to Android Marshmallow for permission to be available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //method to get default location of user. this is the current location of the user as and when they open this activity and click the GPS icon
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //checks if device has allowed permission to access its GPS(location)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();

            mMap.setMyLocationEnabled(true);
        }
    }

    //onClick method referencing xml onClick buttons
    public void onClick(View view)
    {
        String church = "church";
        Object transferData[] = new Object[2];
        GetNearbyChurches getNearbyChurches = new GetNearbyChurches();

        switch (view.getId())
        {
            //search field and button
            case R.id.search:
                EditText churchField = findViewById(R.id.location_search);
                String churchAddress = churchField.getText().toString();

                List<Address> addressList = null;
                MarkerOptions churchMarkerOptions = new MarkerOptions();

                //checks if editText field is not empty and then uses the search result to search the map based on Geocode or location name entered by user
                if (!TextUtils.isEmpty(churchAddress)) {
                    Geocoder geocoder = new Geocoder(GoogleMapsActivity.this);

                    try {
                        //search functionality to return a maximum of 10 results based on search
                        addressList = geocoder.getFromLocationName(churchAddress, 10);

                        //checks that addressList is not null before returning results and looping through them
                        if (addressList != null) {
                            //loop through items in addressList and set marker based on selected item's latitude, longitude and title
                            for (int i = 0; i < addressList.size(); i++) {
                                Address churchAdd = addressList.get(i);
                                LatLng latLng = new LatLng(churchAdd.getLatitude(), churchAdd.getLongitude());
                                churchMarkerOptions.position(latLng);
                                churchMarkerOptions.title(churchAddress);
                                churchMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                                mMap.addMarker(churchMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                            }
                        } else {
                            //Toast message if church cannot be found
                            Toast.makeText(GoogleMapsActivity.this, "Church can not be found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //Toast message if address has not been entered
                    Toast.makeText(GoogleMapsActivity.this, "Please enter a church name", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nearby_churches:
                mMap.clear();
                String url = getUrl(latitude, longitude, church);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyChurches.execute(transferData);
                Toast.makeText(this, "Searching for nearby churches", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private String getUrl(double latitude, double longitude, String nearbyChurch)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + proximityRadius);
        googleURL.append("&type=" + nearbyChurch);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyBv-FCpO2fabbdVwBGjyls8NWM-gh-ZPwE");

        Log.d("GoogleMapsActivity", "url = " +googleURL.toString());

        return googleURL.toString();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    //method to check if the user permission has been granted or not
    public boolean checkUserLocationPermission(){
        //checks if device has allowed permission to access its GPS(location)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    //method to handle request permission result from checkUserLocationPermission method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //method to get location on activity startup
    //GoogleApiClient is the main object to allow the app to access the maps and location services of google
    protected synchronized void buildGoogleApiClient(){

    }

    //method called when location of user changes
    //gets the longitude and latitude of user and sets the marker on their location
    //enable camera movement based on longitude and latitude and enable zoom in on location for greater detail
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        lastLocation = location;
        if(currentUserLocationMarker !=null){
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng((location.getLatitude()),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient !=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    //method called when device is connected to get the current location
    //method to get accurate location of user as they move around
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest().create();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //checks if device has allowed permission to access its GPS(location)
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED)
        {
            //get user location continually as they move
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //method called when device is unable to connect to get current location
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
