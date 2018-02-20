package com.evilforge.bucktracker;

import java.util.Calendar;

public class Bucks {

    private String buckName;
    private boolean isShooter;
    private Calendar lastSeen;

    public Bucks(String buckName, Boolean isShooter) {
        this.buckName = buckName;
        this.isShooter = isShooter;
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

    public Calendar getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Calendar lastSeen) {
        this.lastSeen = lastSeen;
    }
}
