package com.example.byungkyu.myapplication;

/**
 * Created by USER on 2017-08-24.
 */

public class AnalogActivityItem {
    private String analogID;
    private String value;
    private double progressBar;

    public void setProgressBar(double progressBar) {
        this.progressBar = progressBar;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAnalogID(String analogID) {
        this.analogID = analogID;
    }

    public String getValue() {
        return value;
    }

    public String getAnalogID() {
        return analogID;
    }

    public double getProgressBar() {
        return progressBar;
    }
}
