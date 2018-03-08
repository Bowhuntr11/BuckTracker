package com.evilforge.bucktracker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RanchMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private String ranchKey;
    private String ranchName;
    private Marker marker = null;
    private FloatingActionButton save_btn;
    private FloatingActionButton delete_marker_btn;
    private double latitude = 33;
    private double longitude = -100;
    private Polygon polygon;
    private List<LatLng> markerLatLngList;
    private List<Marker> markerList;
    PolygonOptions rectOptions;
    private int markerCount = 0;

    private double topRightLongitude = -200;
    private double topRightLatitude = -200;
    private double bottomLeftLongitude = 200;
    private double bottomLeftLatitude = 200;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refRanches = database.getReference("Ranches");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranch_maps);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        save_btn = findViewById(R.id.fab_save_ranch);
        delete_marker_btn = findViewById(R.id.fab_delete_last_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        delete_marker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerLatLngList != null) {
                    if (!markerLatLngList.isEmpty()) {
                        marker = markerList.get(markerList.size() - 1);
                        marker.remove();
                        markerList.remove(markerList.size() - 1);
                        markerLatLngList.remove(markerLatLngList.size() - 1);
                        markerCount--;
                        if (markerLatLngList.size() > 1) {
                            polygon.setPoints(markerLatLngList);
                        } else {
                            if (polygon != null) {
                                polygon.remove();
                            }
                            rectOptions = null;
                            polygon = null;
                        }
                    } else {
                        markerLatLngList = null;
                        markerList = null;
                        markerCount = 0;
                        marker = null;
                        map.clear();
                    }
                }
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerCount < 3) {
                    Toast.makeText(RanchMapsActivity.this, "You need to put down more markers!", Toast.LENGTH_SHORT).show();
                } else {
                    new MaterialDialog.Builder(RanchMapsActivity.this)
                            .title("Ranch Name")
                            .inputRange(2, 20)
                            .inputType(InputType.TYPE_CLASS_TEXT)
                            .input(null, null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    ranchName = input.toString();
                                    Log.d("RanchMaps", ranchName);

                                    refRanches = refRanches.push();
                                    String key = refRanches.getKey();   //Get the Ranches key

                                    Ranch ranch = new Ranch(ranchName, markerLatLngList, topRightLongitude, topRightLatitude, bottomLeftLongitude, bottomLeftLatitude);
                                    refRanches.setValue(ranch);

                                    // Put the Ranches key under this User
                                    database.getReference("Users")
                                            .child(currentUser.getUid())
                                            .push()
                                            .setValue(key);

                                    Toast.makeText(RanchMapsActivity.this, "Ranch Saved", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .negativeText("Cancel")
                            .show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startLocation = new LatLng(latitude, longitude);

        map.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 2));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {

                if (markerLatLngList == null || markerLatLngList.size() == 0) {
                    markerLatLngList = new ArrayList<>();
                    markerList = new ArrayList<>();
                    rectOptions = new PolygonOptions();
                    rectOptions.fillColor(0x7F00FF00);
                    rectOptions.strokeColor(Color.RED);
                }
                MarkerOptions markerOption = new MarkerOptions()
                        .title("Ranch Corner");
                markerOption.position(arg0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = map.addMarker(markerOption);
                markerList.add(marker);

                LatLng position = marker.getPosition();
                latitude = position.latitude;
                longitude = position.longitude;
                markerLatLngList.add(position);
                markerCount++;

                Log.d("Latitude", String.valueOf(latitude));
                Log.d("Longitude", String.valueOf(longitude));
                Log.d("MarkerCount", String.valueOf(markerCount));

                if (latitude > topRightLatitude) {
                    topRightLatitude = latitude;
                }
                if (longitude > topRightLongitude) {
                    topRightLongitude = longitude;
                }
                if (latitude < bottomLeftLatitude) {
                    bottomLeftLatitude = latitude;
                }
                if (longitude < bottomLeftLongitude) {
                    bottomLeftLongitude = longitude;
                }

                if (polygon != null) {
                    polygon.setPoints(markerLatLngList);
                    Log.d("RanchMap", "Updated Polygon");
                }

                if (markerCount == 3) {
                    rectOptions.addAll(markerLatLngList);
                    polygon = map.addPolygon(rectOptions);
                }
            }
        });

    }
}
