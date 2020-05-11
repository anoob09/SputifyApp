package com.example.sputify;

public class MyListData{
    private String songName;
    private String userId;
    private int imgId;
    public MyListData(String songName, String userId, int imgId) {
        this.songName = songName;
        this.userId = userId;
        this.imgId = imgId;
    }

    public MyListData() {}

    public String getSongName() {
        return songName;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
