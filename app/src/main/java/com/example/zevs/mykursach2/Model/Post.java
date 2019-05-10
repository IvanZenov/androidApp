package com.example.zevs.mykursach2.Model;

import java.util.Date;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String amountvisitors;
    private String type;
    private Long timestamp;
    private String name;




    public Post(String postid, String postimage,
                String description, String publisher,
                String amountvisitors,Long timestamp,
                String type, String name) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.amountvisitors = amountvisitors;
        this.timestamp = timestamp;
        this.type = type;
        this.name = name;
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Post() {
    }

}
