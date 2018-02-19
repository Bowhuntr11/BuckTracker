package com.evilforge.bucktracker;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;


public class Stands {

    private String standName;
    private Place standPlace;

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public Place getStandPlace() {
        return standPlace;
    }

    public void setStandPlace(Place standPlace) {
        this.standPlace = standPlace;
    }
}
