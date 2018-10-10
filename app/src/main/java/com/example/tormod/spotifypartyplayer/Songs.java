package com.example.tormod.spotifypartyplayer;


public class Songs {
    private String uri;
    private String songTitle;
    private String artist;
    private String picturePath;
    private int length;
    private long startTime;

    public String getUri() {
        return uri;
    }
    public String getSongTitle() {
        return songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public Songs(String uri, String songTitle, String artist, String picturePath, int length, long startTime) {
        this.uri = uri;
        this.songTitle = songTitle;
        this.artist = artist;
        this.picturePath = picturePath;
        this.length = length;
        this.startTime = startTime;
    }
}
