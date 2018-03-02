package com.evilforge.bucktracker;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Bucks {

    private String buckName;
    private boolean isShooter;
    private Long lastSeen;
    private String lastStand = "";
    private String[] photos;
    private String defaultPhotoURL;
    private HashMap<String, Long> bucksStands = null;


    public Bucks(String buckName, boolean isShooter, Long whenSeen, String standName, String defaultPhotoURL) {
        this.buckName = buckName;
        this.isShooter = isShooter;
        this.defaultPhotoURL = defaultPhotoURL;
        this.lastSeen = whenSeen;
        this.lastStand = standName;
        bucksStands.put(standName, whenSeen);
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

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getLastStand() {
        return lastStand;
    }

    public void setLastStand(String lastStand) {
        this.lastStand = lastStand;
    }

    public String[] getPhotos() {
        return photos;
    }

    public void setPhotos(String[] photos) {
        this.photos = photos;
    }

    public String getDefaultPhotoURL() {
        return defaultPhotoURL;
    }

    public void setDefaultPhotoURL(String defaultPhotoURL) {
        this.defaultPhotoURL = defaultPhotoURL;
    }

    public HashMap<String, Long> getBucksStands() {
        return bucksStands;
    }

    public void setBucksStands(HashMap<String, Long> bucksStands) {
        this.bucksStands = bucksStands;
    }
}
