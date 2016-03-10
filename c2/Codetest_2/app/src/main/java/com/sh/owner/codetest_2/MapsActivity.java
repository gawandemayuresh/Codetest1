package com.sh.owner.codetest_2;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double llat,llang;
    private String str_selectedname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent i = getIntent();
        String str_llat = i.getStringExtra(MainActivity.Tag_latitude);
        String str_llang = i.getStringExtra(MainActivity.Tag_longitude);
         str_selectedname = i.getStringExtra(MainActivity.Tag_name);

        llat=Double.parseDouble(str_llat);
        llang=Double.parseDouble(str_llang);

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

        // Add a marker in Sydney and move the camera
        LatLng ltlng = new LatLng(llat, llang);
        mMap.addMarker(new MarkerOptions().position(ltlng).title(str_selectedname));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ltlng, 15);
        mMap.animateCamera(cameraUpdate);
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(ltlng));
    }
}
