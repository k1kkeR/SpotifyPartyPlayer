package com.example.tormod.spotifypartyplayer;


public class Users {
    private int userID;
    private String nickName;
    private int sessionID;
    private boolean connectedToNSD;

    public int getUserID() {
        return userID;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getSessionID() {
        return sessionID;
    }
    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }
    public boolean isConnectedToNSD() {
        return connectedToNSD;
    }
    public void setConnectedToNSD(boolean connectedToNSD) {
        this.connectedToNSD = connectedToNSD;
    }

    //Default constructer
    public Users() {
        this.userID = 1337;
        this.nickName = "Håvar";
        this.sessionID = 0;
        this.connectedToNSD = false;
    }

    public Users(int userID, String nickName, int sessionID, boolean connectedToNSD) {
        this.userID = userID;
        this.nickName = nickName;
        this.sessionID = sessionID;
        this.connectedToNSD = connectedToNSD;
    }
}
