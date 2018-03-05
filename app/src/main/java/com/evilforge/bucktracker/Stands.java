package com.evilforge.bucktracker;


import java.io.Serializable;

public class Stands implements Serializable {

    private String standName;
    private String latitude;
    private String longitude;

    public Stands(String standName, String latitude, String longitude) {
        this.standName = standName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Stands() {
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
