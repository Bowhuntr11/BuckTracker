package com.evilforge.bucktracker;


import java.util.List;

public class Ranch {

    private String ranchName;
    private List markers;
    private double topRightLongitude;
    private double topRightLatitude;
    private double bottomLeftLongitude;
    private double bottomLeftLatitude;

    public Ranch(String ranchName, List markers, double topRightLongitude, double topRightLatitude, double bottomLeftLongitude, double bottomLeftLatitude) {
        this.ranchName = ranchName;
        this.markers = markers;
        this.topRightLongitude = topRightLongitude;
        this.topRightLatitude = topRightLatitude;
        this.bottomLeftLongitude = bottomLeftLongitude;
        this.bottomLeftLatitude = bottomLeftLatitude;
    }

    public Ranch() {

    }

    public String getRanchName() {
        return ranchName;
    }

    public void setRanchName(String ranchName) {
        this.ranchName = ranchName;
    }

    public List getMarkers() {
        return markers;
    }

    public void setMarkers(List markers) {
        this.markers = markers;
    }

    public double getTopRightLongitude() {
        return topRightLongitude;
    }

    public void setTopRightLongitude(double topRightLongitude) {
        this.topRightLongitude = topRightLongitude;
    }

    public double getTopRightLatitude() {
        return topRightLatitude;
    }

    public void setTopRightLatitude(double topRightLatitude) {
        this.topRightLatitude = topRightLatitude;
    }

    public double getBottomLeftLongitude() {
        return bottomLeftLongitude;
    }

    public void setBottomLeftLongitude(double bottomLeftLongitude) {
        this.bottomLeftLongitude = bottomLeftLongitude;
    }

    public double getBottomLeftLatitude() {
        return bottomLeftLatitude;
    }

    public void setBottomLeftLatitude(double bottomLeftLatitude) {
        this.bottomLeftLatitude = bottomLeftLatitude;
    }
}