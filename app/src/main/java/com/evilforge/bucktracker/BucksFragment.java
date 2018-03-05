package com.evilforge.bucktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BucksFragment extends Fragment {

    FloatingActionButton fab;
    ListView buckList;
    private FirebaseListAdapter<Bucks> adapter;

    private BucksFragment.OnFragmentInteractionListener mListener;

    public BucksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bucks, container, false);
        fab = view.findViewById(R.id.fab_bucks);
        buckList = view.findViewById(R.id.list_of_bucks);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayBucks();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewBuck.class));
            }
        });

        buckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bucks clickedBuck = (Bucks) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), SelectedBuckActivity.class);
                intent.putExtra("Bucks", clickedBuck);
                Toast.makeText(getActivity(), "Buck Clicked:" + i, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    private void displayBucks() {

        //Suppose you want to retrieve "chats" in your Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference().child("bucks");
        //The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<Bucks> options = new FirebaseListOptions.Builder<Bucks>()
                .setQuery(query, Bucks.class)
                .setLayout(R.layout.buck_list)
                .build();

        adapter = new FirebaseListAdapter<Bucks>(options) {
            @Override
            protected void populateView(View v, Bucks buck, int position) {
                  // Get references to the views of buck_list.xml
                TextView buckName = v.findViewById(R.id.buck_name);
                TextView lastSeen = v.findViewById(R.id.last_seen_date);
                ImageView isBuckShooterCheck = v.findViewById(R.id.isShooter_check);

                if (buck.isShooter()) {
                    Picasso.with(getActivity())
                            .load(R.drawable.checkmark)
                            .into(isBuckShooterCheck);
                } else {
                    Picasso.with(getActivity())
                            .load(R.drawable.delete_icon)
                            .into(isBuckShooterCheck);
                }
                buckName.setText(buck.getBuckName());

                // Format the date before showing it
                long val = buck.getLastSeen();
                Date date=new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("MMM d yyyy (HH:mm)", Locale.getDefault());
                String dateText = df2.format(date);
                lastSeen.setText(dateText);
            }
        };

        buckList.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BucksFragment.OnFragmentInteractionListener) {
            mListener = (BucksFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener {
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
