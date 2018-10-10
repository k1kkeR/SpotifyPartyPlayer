package com.example.tormod.spotifypartyplayer;

import java.util.ArrayList;


public class Sessions {
    private int sessionID;
    private String partyName;
    private String genre;
    private String password1;
    private String password2;
    private String passowrd3;
    private String currentSong;
    private String currentArtist;
    private ArrayList<Songs> queueList;
    private ArrayList<String> queueToBe;
    private Users host;
    private ArrayList connectedUsers;
    private boolean connectedToSpotify;
    private boolean connectedToNSD;

    public ArrayList<Songs> getQueueList() {
        return  this.queueList;
    }

    public void setQueueList(ArrayList<Songs> queueList) {
        this.queueList = queueList;
    }
    public void clearQueueToBe(){
        this.queueToBe = null;
        this.queueToBe = new ArrayList<>();
    }
    public void addToQueueList(Songs songURI){
        this.queueList.add(songURI);
    }
    public Songs findIndexOfQueue(int index){

        return this.queueList.get(index);
}
    public void addToQueueToBe(String uri){
        this.queueToBe.add(uri);
    }
    public void resetQueueToBe(){
        this.queueToBe = null;
        this.queueToBe = new ArrayList<>(100);
    }
    public int getQueueToBeSize(){
        return this.queueToBe.size();
    }
    public String getQueueToBeIndex(int i){
        return  this.queueToBe.get(i);
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void setPassowrd3(String passowrd3) {
        this.passowrd3 = passowrd3;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getCurrentSong() {
        return currentSong;
    }

    public String getCurrentArtist() {
        return currentArtist;
    }

    public void setCurrentSong(String currentSong) {
        this.currentSong = currentSong;
    }

    public void setCurrentArtist(String currentArtist) {
        this.currentArtist = currentArtist;
    }

    public Sessions() {
        this.sessionID = 0;
        this.partyName = "Best party evvah!";
        this.genre = "K-POP";
        this.password1 = "";
        this.password2 = "";
        this.passowrd3 = "bestPasswordEvvah";
        this.currentSong = null;
        this.queueList = new ArrayList<>();
        this.queueToBe = new ArrayList<>();
        this.host = null;
        this.connectedUsers = null;
        this.connectedToSpotify = false;
        this.connectedToNSD = false;
    }
    public Sessions(int sessionID, String partyName, String genre, String password1, String password2, String passowrd3, String currentSong, String currentArtist,
                    ArrayList queueList, ArrayList queueToBe, Users host, ArrayList connectedUsers, boolean connectedToSpotify, boolean connectedToNSD) {
        this.sessionID = sessionID;
        this.partyName = partyName;
        this.genre = genre;
        this.password1 = password1;
        this.password2 = password2;
        this.passowrd3 = passowrd3;
        this.currentSong = currentSong;
        this.currentArtist = currentArtist;
        this.queueList = queueList;
        this.queueToBe = queueToBe;
        this.host = host;
        this.connectedUsers = connectedUsers;
        this.connectedToSpotify = connectedToSpotify;
        this.connectedToNSD = connectedToNSD;
    }

}
