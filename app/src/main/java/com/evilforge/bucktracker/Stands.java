package com.evilforge.bucktracker;



import com.google.android.gms.location.places.Place;

public class Stands {

    private String standName;
    private Place standPlace;

    public Stands(String standName, Place standPlace) {
        this.standName = standName;
        this.standPlace = standPlace;
    }

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
