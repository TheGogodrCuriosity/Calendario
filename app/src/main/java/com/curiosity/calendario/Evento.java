package com.curiosity.calendario;

import android.graphics.Bitmap;

import java.util.Calendar;

/**
 * Created by Gogodr on 22/07/2016.
 */
public class Evento implements Comparable<Evento>{
    private Calendar startTime,endTime;
    private String photoUser;
    private int color;
    public Evento(){}
    public Evento(Calendar startTime, Calendar endTime, String photoUser, int color) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoUser = photoUser;
        this.color = color;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int compareTo(Evento another) {
        return (int)((this.getStartTime().getTimeInMillis() - another.getStartTime().getTimeInMillis())/1000);
    }
}
