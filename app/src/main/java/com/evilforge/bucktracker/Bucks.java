package com.evilforge.bucktracker;

import java.util.Calendar;

public class Bucks {

    private String buckName;
    private boolean isShooter;
    private Calendar dateSeen;

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

    public Calendar getDateSeen() {
        return dateSeen;
    }

    public void setDateSeen(Calendar dateSeen) {
        this.dateSeen = dateSeen;
    }
}
