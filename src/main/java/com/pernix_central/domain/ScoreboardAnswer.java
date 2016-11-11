package com.pernix_central.domain;

public class ScoreboardAnswer {
    private String first_name;
    private String last_name;
    private long points;

    public ScoreboardAnswer(String first_name, String last_name, long points){
        this.first_name = first_name;
        this.last_name = last_name;
        this.points = points;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() { return last_name; }

    public long getPoints() { return points; }

}
