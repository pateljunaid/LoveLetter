package com.example.junaidpatel.loveletter;

/**
 * Getters and setters for each player object
 * Keeps track of certain variables that are unique to each player such as player score
 * Created by pateljunaid
 */

public class Player {
    private boolean isHost;
    private boolean inGame;
    private boolean playedFour;
    private String playerName;
    private Integer playerId;
    private Integer lastPlayed;
    private Integer card1;
    private Integer card2;

    public Player() {
        //Default values
        this.lastPlayed = 0;
    }

    public Integer getCard1() {
        return card1;
    }

    public void setCard1(Integer card1) {
        this.card1 = card1;
    }

    public Integer getCard2() {
        return card2;
    }

    public void setCard2(Integer card2) {
        this.card2 = card2;
    }

    public void setPlayerName(String name){
        playerName = name;
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isHost() {
        return this.playerId == 1;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Integer getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Integer lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setPlayedFour(boolean playedFour) {
        this.playedFour = playedFour;
    }

    public boolean getPlayedFour() {
        return playedFour;
    }

    public String getSecretMessage() {
        return secretMessage;
    }

    public void setSecretMessage(String secretMessage) {
        this.secretMessage = secretMessage;
    }

    private String secretMessage;

}
