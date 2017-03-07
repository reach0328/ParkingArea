package com.android.jh.parking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Remote.Callback {

    private GoogleMap mMap;
    private String url = "http://openapi.seoul.go.kr:8088/774e574b4b72656133344272687368/json/SearchParkingInfo/1/1000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Remote remote = new Remote();
        remote.getData(this);

        LatLng seoul = new LatLng(37.566696, 126.977942);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,12));
    }

    @Override
    public void call(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject rootObject = jsonObject.getJSONObject("SearchParkingInfo");
            JSONArray rows = rootObject.getJSONArray("row");
            int arrayLength = rows.length();
            List<String> parkCode = new ArrayList<>();
            for (int i = 0; i < arrayLength; i++) {
                JSONObject park = rows.getJSONObject(i);
                String code = park.getString("PARKING_CODE");
                if(parkCode.contains(code)){
                   continue;
                }
                parkCode.add(code);
                double lat = getDouble(park,"LAT");
                double lng = getDouble(park,"LNG");
                LatLng parking = new LatLng(lat, lng);
                int capacity = getInt(park,"CAPACITY");
                int current = getInt(park,"CUR_PARKING");
                int space = capacity - current;
                mMap.addMarker(new MarkerOptions().position(parking).title(space + "/" + capacity));
            }
            Log.i("MARKer","============================"+parkCode.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        while(true) {
//            LatLng parking = new LatLng(37, 126);
//            mMap.addMarker(new MarkerOptions().position(parking).title("Marker in Sydney"));
//        }
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Context getContext() {
        return this;
    }

    private double getDouble(JSONObject obj,String key) {
        double result = 0;
        try {
            result = obj.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    private int getInt(JSONObject obj,String key) {
        int result = 0;
        try {
            result = obj.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
