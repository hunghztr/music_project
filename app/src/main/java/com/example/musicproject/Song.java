package com.example.musicproject;

import java.io.Serializable;

public class Song implements Serializable {
    private int fileId;
    private int imageId;
    private String songName;
    private String singerName;


    public Song() {
    }

    public Song(int imageId, String songName, String singerName,int fileId ) {
        this.imageId = imageId;
        this.songName = songName;
        this.singerName = singerName;
        this.fileId = fileId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
