package com.example.uuj.finalyearproject;

//android imports
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//google maps imports
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//java imports
import java.io.IOException;
import java.util.List;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /*used https://www.youtube.com/playlist?list=PLxefhmF0pcPlGUW8tyyOJ8-uF7Nk2VpSj &
    https://www.youtube.com/watch?v=oOVRNxPtfeQ&list=PLF0BIlN2vd8und4ajF-bdFI3jWyPTXxB5
    tutorial sets in the creation of this class*/

    /*used https://developers.google.com/places/web-service/intro &
    https://developers.google.com/maps/documentation/android-sdk/intro as guidance in the creation of this class*/

    /*account set up on https://console.cloud.google.com/google/maps-apis/overview?project=finalyearproject-c85ba&folder=&organizationId=
    for access to maps SDK, places API and other APIs included with the google maps functionality*/

    //Class member variables
    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    LatLng latLngCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //method to get location on activity startup
            //GoogleApiClient is the main object to allow the app to access the maps and location services of google
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            googleApiClient.connect();

            mMap.setMyLocationEnabled(true);
        }
    }

    //method to append the json data from the google maps URL search and display the collected location results on the map
    public void findNearbyChurches(View v) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("&location=" + latLngCurrent.latitude + "," + latLngCurrent.longitude);
        stringBuilder.append("&radius=" + 10000);
        stringBuilder.append("&keyword=" + "church");
        stringBuilder.append("&key=" + getResources().getString(R.string.google_places_api_key));

        String url = stringBuilder.toString();

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyChurches getNearbyChurches = new GetNearbyChurches(this);
        getNearbyChurches.execute(dataTransfer);
    }

    //onClick method referencing xml search button in activity_google_maps.xml
    //method to search map for a church based on text entered
    public void searchNearbyChurches(View view) {
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
    }

    //auto-generated comment code when creating Google Maps activity
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    //method called when location of user changes
    //gets the longitude and latitude of user and sets the marker on their location
    //enable camera movement based on longitude and latitude and enables zoom to show location in greater detail
    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
        } else {
            latLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngCurrent, 10);
            mMap.animateCamera(update);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLngCurrent);
            markerOptions.title("User Current Location");
            mMap.addMarker(markerOptions);
        }

    }

    //method called when device is connected to get the current location
    //method to get accurate location of user as they move around
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //checks if device has allowed permission to access its GPS(location)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //get user location continually as they move
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    //method called when device GPS is temporarily disconnected
    @Override
    public void onConnectionSuspended(int i) {

    }

    //method called when device is unable to connect to get current location
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}

