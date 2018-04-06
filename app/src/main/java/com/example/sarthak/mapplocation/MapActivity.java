package com.example.sarthak.mapplocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private String address;
    private GoogleMap map;
    private LatLng position;
    private String jsonreturn;
    private String[] jsonData;
    private boolean viewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //get user address
        address = getIntent().getStringExtra(MainActivity.ADDRESS);
        mapInit();
    }

    private void mapInit() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                moveCamera(position);
                setMarkerAt(position);
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                jsonreturn = mapHTTPRequest.findCoords(params[0]);
                return null;
            }
        }.execute(address);

        while (jsonreturn == null) ;
        String[] returnItems = jsonreturn.split(",");
        jsonData = returnItems;
        position = new LatLng(Double.parseDouble(returnItems[0]), Double.parseDouble(returnItems[1]));
    }

    private void moveCamera(LatLng latLng) {
        //update the map camera
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
    }

    private void setMarkerAt(LatLng point) {
        MarkerOptions newMarkerOptions = new MarkerOptions().position(point);
        newMarkerOptions.title(jsonData[2] + "," + jsonData[3] + "," + jsonData[4]);

        map.addMarker(newMarkerOptions);
        map.setBuildingsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);
        }
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        map.setMyLocationEnabled(true);
    }

    public void toggleView(View view) {
        if(viewState) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        else {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        viewState = !viewState;
        //map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
    public void moveBackToTarget(View view) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14f));
    }
}
