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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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


public class StandsFragment extends Fragment {

    private FloatingActionButton fab;
    private ListView standList;
    private Button addRanchBtn;
    private ProgressBar progressBar;
    private FirebaseListAdapter<Stands> adapter;

    private StandsFragment.OnFragmentInteractionListener mListener;

    public StandsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stands, container, false);
        fab = view.findViewById(R.id.fab_stands);
        addRanchBtn = view.findViewById(R.id.add_ranch_btn);
        standList = view.findViewById(R.id.list_of_stands);
        progressBar = view.findViewById(R.id.stands_progress_bar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayStands();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewStand.class));
            }
        });

        standList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stands clickedStand = (Stands) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), SelectedStandActivity.class);
                intent.putExtra("Stands", clickedStand);
                Toast.makeText(getActivity(), "Stand Clicked:" + i, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        addRanchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RanchMapsActivity.class));
            }
        });
    }

    private void displayStands(){

        //Suppose you want to retrieve "chats" in your Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference().child("stands");
        //The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<Stands> options = new FirebaseListOptions.Builder<Stands>()
                .setQuery(query, Stands.class)
                .setLayout(R.layout.stand_list)
                .build();

        adapter = new FirebaseListAdapter<Stands>(options) {
            @Override
            protected void populateView(View v, Stands stand, int position) {
                // Get references to the views of buck_list.xml
                TextView standName = v.findViewById(R.id.stand_name);
                standName.setText(stand.getStandName());
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        };

        standList.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StandsFragment.OnFragmentInteractionListener) {
            mListener = (StandsFragment.OnFragmentInteractionListener) context;
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
