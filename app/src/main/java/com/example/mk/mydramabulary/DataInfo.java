package com.example.mk.mydramabulary;

import java.io.Serializable;

/**
 * Created by mk on 2017-02-13.
 */

public class DataInfo implements Serializable{
    int id;
    int start;
    int end;
    String sentence;
    String youtube_id;
    String title;
    float rating_sum;
    int rating_count;

    public float getRating_sum() {
        return rating_sum;
    }

    public void setRating_sum(float rating_sum) {
        this.rating_sum = rating_sum;
    }

    public int getRating_count() {
        return rating_count;
    }

    public void setRating_count(int rating_count) {
        this.rating_count = rating_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYoutube_id() {
        return youtube_id;
    }

    public void setYoutube_id(String youtube_id) {
        this.youtube_id = youtube_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

}
