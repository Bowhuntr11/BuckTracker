package com.evilforge.bucktracker;



public class BuckSighting {

    private String standName;
    private Long whenSeen;

    public BuckSighting(String recentStand, Long latestTime) {
        this.standName = recentStand;
        this.whenSeen = latestTime;
    }

    public BuckSighting() {
    }

    public String getStandName() {
        return standName;
    }

    public void setStandName(String standName) {
        this.standName = standName;
    }

    public Long getWhenSeen() {
        return whenSeen;
    }

    public void setWhenSeen(Long whenSeen) {
        this.whenSeen = whenSeen;
    }
}