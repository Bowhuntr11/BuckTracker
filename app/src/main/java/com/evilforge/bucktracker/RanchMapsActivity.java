package com.evilforge.bucktracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class RanchMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Marker marker = null;
    private FloatingActionButton save_btn;
    private double latitude = 33;
    private double longitude = -100;
    private Polygon polygon;
    private List<LatLng> markers;
    PolygonOptions rectOptions;
    private int markerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranch_maps);

        save_btn = findViewById(R.id.fab_save_ranch);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        save_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RanchMapsActivity.this, NewRanch.class);
//                finish();
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startLocation = new LatLng(latitude, longitude);
        markers = new ArrayList<>();
        rectOptions = new PolygonOptions();
        rectOptions.fillColor(0x7F00FF00);
        rectOptions.strokeColor(Color.RED);

        map.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                MarkerOptions markerOption = new MarkerOptions()
                        .title("Ranch Corner");
                markerOption.position(arg0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = map.addMarker(markerOption);


                LatLng position = marker.getPosition();
                latitude = position.latitude;
                longitude = position.longitude;
                markers.add(position);
                markerCount++;

                Log.d("Latitude", String.valueOf(latitude));
                Log.d("Longitude", String.valueOf(longitude));
                Log.d("MarkerCount", String.valueOf(markerCount));


                if (polygon != null) {
                    polygon.setPoints(markers);
                    Log.d("RanchMap", "Updated Polygon");
                }

                if (markerCount == 3) {
                    rectOptions.addAll(markers);
                    polygon = map.addPolygon(rectOptions);
                }
            }
        });

    }
}
