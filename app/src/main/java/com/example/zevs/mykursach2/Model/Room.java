package com.example.zevs.mykursach2.Model;

import java.util.ArrayList;

public class Room {
    public String id;
    public ArrayList<String> member;

    public Room (){}
    public Room(ArrayList<String> member,String id) {
        this.member = member;
        this.id = id;
    }

    public ArrayList<String> getMember() {
        return member;
    }

    public void setMember(ArrayList<String> member) {
        this.member = member;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
