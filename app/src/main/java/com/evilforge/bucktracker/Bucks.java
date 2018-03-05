package com.evilforge.bucktracker;

import java.io.Serializable;

public class Bucks implements Serializable{

    private String buckName;
    private boolean isShooter;
    private Long lastSeen;
    private String lastStand = "";
    private String defaultPhotoURL;
    private String newStand;
    private Long newStandSeenTime;


    public Bucks(String buckName, boolean isShooter, Long whenSeen, String standName, String defaultPhotoURL) {
        this.buckName = buckName;
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

    public String getDefaultPhotoURL() {
        return defaultPhotoURL;
    }

    public void setDefaultPhotoURL(String defaultPhotoURL) {
        this.defaultPhotoURL = defaultPhotoURL;
    }

    public String getNewStand() {
        return newStand;
    }

    public void setNewStand(String newStand) {
        this.newStand = newStand;
    }

    public Long getNewStandSeenTime() {
        return newStandSeenTime;
    }

    public void setNewStandSeenTime(Long newStandSeenTime) {
        this.newStandSeenTime = newStandSeenTime;
    }
}
