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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.SimpleDateFormat;
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
        fab = view.findViewById(R.id.fab);
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
                startActivity(new Intent(getActivity(), AddBuck.class));
            }
        });

        buckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bucks clickedBuck = (Bucks) adapterView.getItemAtPosition(i);

                Toast.makeText(getActivity(), "Buck Clicked:" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBucks() {
        adapter = new FirebaseListAdapter<Bucks>(getActivity(), Bucks.class,
                R.layout.buck_list, FirebaseDatabase.getInstance().getReference().child("bucks")) {
            @Override
            protected void populateView(View v, Bucks buck, int position) {
                // Get references to the views of buck_list.xml
                TextView buckName = v.findViewById(R.id.buck_name);
                TextView isBuckShooter = v.findViewById(R.id.isShooter_text);
                TextView lastSeen = v.findViewById(R.id.last_seen);

                isBuckShooter.setText(getString(R.string.shooter_buck,  String.valueOf(buck.isShooter())));
                buckName.setText(buck.getBuckName());

                // Format the date before showing it
                Format formatter = new SimpleDateFormat("MMM d yyyy (HH:mm)", Locale.getDefault());
                lastSeen.setText(formatter.format(buck.getLastSeen()));
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

}
