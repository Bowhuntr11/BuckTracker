package com.evilforge.bucktracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Bucks {

    private String buckName;
    private boolean isShooter;
    private Date lastSeen;
    private Stands lastStand;
    private ArrayList<String> photos = new ArrayList<>();
    private String defaultPhotoURL;
    private HashMap<Date, Stands> bucksStands = null;


    public Bucks(String buckName, boolean isShooter, Date whenSeen, Stands standName, String defaultPhotoURL) {
        this.buckName = buckName;
        bucksStands.put(whenSeen, standName);
        this.isShooter = isShooter;
        this.defaultPhotoURL = defaultPhotoURL;
        this.lastSeen = whenSeen;
        this.lastStand = standName;
    }

    public Bucks() {
    }

    public String getBuckName() {
        return buckName;
    }

    public void setBuckName(String buckName) {
        this.buckName = buckName;
    }

    public boolean isShooter() {
        return isShooter;
    }

    public void setShooter(boolean shooter) {
        isShooter = shooter;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date whenSeen, Stands standName) {
        if(whenSeen.after(getLastSeen()) || getLastSeen() == null) {
            this.lastSeen = whenSeen;
            this.lastStand = standName;
        }
    }

    public void addPhoto(String photoURL) {
        this.photos.add(photoURL);
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setDefaultPhotoURL(String defaultPhotoURL) {
        this.defaultPhotoURL = defaultPhotoURL;
    }

    public String getDefaultPhotoURL() {
        return defaultPhotoURL;
    }

    public HashMap<Date, Stands> getBucksStands() {
        return bucksStands;
    }

    public Stands getLastStand() {
        return lastStand;
    }
}
