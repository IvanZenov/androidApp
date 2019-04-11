package com.example.zevs.mykursach2.Model;

import java.util.Date;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String amountvisitors;
    private String agefrom;
    private String ageto;

    public String getDatepost() {
        return datepost;
    }

    public void setDatepost(String datepost) {
        this.datepost = datepost;
    }

    private String datepost;






    public Post(String postid, String postimage, String description, String publisher,String amountvisitors,String agefrom, String ageto,String datepost) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.amountvisitors = amountvisitors;
        this.agefrom = agefrom;
        this.ageto = ageto;
        this.datepost = datepost;
    }


    public String getAgefrom() {
        return agefrom;
    }

    public void setAgefrom(String agefrom) {
        this.agefrom = agefrom;
    }

    public String getAgeto() {
        return ageto;
    }

    public void setAgeto(String ageto) {
        this.ageto = ageto;
    }

    public String getAmountvisitors() {
        return amountvisitors;
    }

    public void setAmountvisitors(String amountvisitors) {
        this.amountvisitors = amountvisitors;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Post() {
    }

}
