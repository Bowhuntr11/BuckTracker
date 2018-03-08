package com.evilforge.bucktracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StandsMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private String ranchKey;
    private Marker marker = null;
    private FloatingActionButton save_btn;
    private double latitude = 33;
    private double longitude = -100;
    private String standName;

    private double topRightLatitude;
    private double topRightLongitude;
    private double bottomLeftLatitude;
    private double bottomLeftLongitude;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsers = database.getReference("Users");
    private DatabaseReference refRanch = database.getReference("Ranches");

    private Ranch ranch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stands_maps);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        refUsers = refUsers.child(currentUser.getUid());

        refUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ranchKey = (String) postSnapshot.getValue();
                    refRanch = refRanch.child(ranchKey);
                    Log.d("StandMaps", ranchKey);
                    
                    refRanch.child(ranchKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (ranch == null) {
                                ranch = dataSnapshot.getValue(Ranch.class);
                                assert ranch != null;
                                Log.d("StandsMaps", ranch.toString());
                                bottomLeftLatitude = ranch.getBottomLeftLatitude();
                                bottomLeftLongitude = ranch.getBottomLeftLongitude();
                                topRightLatitude = ranch.getTopRightLatitude();
                                topRightLongitude = ranch.getTopRightLongitude();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("standName")) {
            standName = intent.getStringExtra("standName");
            Log.d("NewStand", standName);
        }


        save_btn = findViewById(R.id.fab_save_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StandsMapsActivity.this, NewStand.class);
                intent.putExtra("longitude", String.valueOf(longitude));
                intent.putExtra("latitude", String.valueOf(latitude));
                intent.putExtra("standName", standName);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng startLocation = new LatLng(latitude, longitude);
        MarkerOptions options = new MarkerOptions()
                .position(startLocation)
                .draggable(true)
                .title("Stand Location");


        map.addMarker(options);
        if (bottomLeftLatitude != 0) {
            LatLngBounds Ranch = new LatLngBounds(new LatLng(topRightLatitude, topRightLongitude), new LatLng(bottomLeftLatitude, bottomLeftLongitude));
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(Ranch, 0));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 12));
        }
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng arg0) {
                if (marker != null) {
                    marker.remove();
                }
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(arg0).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                marker = map.addMarker(markerOption);


                LatLng position = marker.getPosition();
                latitude = position.latitude;
                longitude = position.longitude;

                Log.d("Latitude", String.valueOf(latitude));
                Log.d("Longitude", String.valueOf(longitude));
            }
        });

    }
}
