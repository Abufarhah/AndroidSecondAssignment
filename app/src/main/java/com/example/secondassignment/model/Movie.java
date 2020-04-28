package com.example.secondassignment.model;

import java.util.Date;

public class Movie {
    private String name;
    private String directedBy;
    private String starring;
    private int openingOn;

    public Movie() {
    }

    public Movie(String name, String directedBy, String starring, int openingOn) {
        this.name = name;
        this.directedBy = directedBy;
        this.starring = starring;
        this.openingOn = openingOn;
    }

    public String getName() {
        return name;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public String getStarring() {
        return starring;
    }

    public int getOpeningOn() {
        return openingOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirectedBy(String directedBy) {
        this.directedBy = directedBy;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public void setOpeningOn(int openingOn) {
        this.openingOn = openingOn;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", directedBy='" + directedBy + '\'' +
                ", starring='" + starring + '\'' +
                ", openingOn=" + openingOn +
                '}';
    }
}
