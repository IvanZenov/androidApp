package com.example.zevs.mykursach2.Model;

public class User {
    private String age;
    private String bio;
    private String id;
    private String imageUrl;
    private String username;


    public User(String age, String bio, String id, String imageUrl, String username) {
        this.age = age;
        this.bio = bio;
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
    }
    public User () {}

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
