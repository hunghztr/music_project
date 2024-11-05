package com.example.musicproject;

public class Album {
    private int id;
    private String name;
    private String singerName;

    public Album(int id, String name, String singerName) {
        this.id = id;
        this.name = name;
        this.singerName = singerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
