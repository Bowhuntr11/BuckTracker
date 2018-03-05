package com.evilforge.bucktracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements ChatFragment.OnFragmentInteractionListener,
        BucksFragment.OnFragmentInteractionListener,
        StandsFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String mUsername;
    private String mPhotoUrl;


    private FragmentTransaction transaction;
    private Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fragment = new ChatFragment();
                    break;
                case R.id.navigation_bucks:
                    fragment = new BucksFragment();
                    break;
                case R.id.navigation_stands:
                    fragment = new StandsFragment();
                    break;
            }
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = currentUser.getDisplayName();
            if (currentUser.getPhotoUrl() != null) {
                mPhotoUrl = currentUser.getPhotoUrl().toString();
            }
        }

        transaction = getSupportFragmentManager().beginTransaction();
        fragment = new ChatFragment();
        transaction.add(R.id.fragment, fragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mAuth.signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign_out_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            currentUser = user;
            mUsername = user.getDisplayName();
        } else {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
    }
}
