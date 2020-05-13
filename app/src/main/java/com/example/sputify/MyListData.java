package com.example.sputify;

public class MyListData{
    private String songName;
    private String userId;
    private int imgId;
    private String songUrl;
    private String albumUrl;
    public MyListData(String albumUrl, String songName, String userId, int imgId, String songUrl) {
        this.songName = songName;
        this.userId = userId;
        this.imgId = imgId;
        this.songUrl = songUrl;
        this.albumUrl = albumUrl;
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
    public void setSongUrl(String songUrl) {this.songUrl = songUrl;}
    public String getSongUrl(){return songUrl;};
    public String getAlbumUrl(){return  albumUrl;}
    public void setAlbumUrl(String albumUrl) {this.albumUrl = albumUrl;}
}
