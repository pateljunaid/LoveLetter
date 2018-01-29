package com.example.junaidpatel.loveletter;

import java.security.Guard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by junaid.patel on 2018-01-27.
 */

public class Game {
    private ArrayList deck;
    private ArrayList<Card> deckAsObjects;
    private ArrayList<String> players = new ArrayList<String>();
    private ArrayList<Player> playersAsObjects = new ArrayList<Player>();
    private Integer lastCardPlayed;
    private boolean gameOn;
    private Integer lastTurn;

    public Game() {
        deck = new ArrayList(Arrays.asList(1,1,1,1,1,2,2,3,3,4,4,5,5,6,7,8));
        deckAsObjects = new ArrayList<Card>();
        Card one = new One();
        Card two = new Two();
        Card three = new Three();
        Card four = new Four();
        Card five = new Five();
        Card six = new Six();
        Card seven = new Seven();
        Card eight = new Eight();
        deckAsObjects.add(one);
        deckAsObjects.add(one);
        deckAsObjects.add(one);
        deckAsObjects.add(one);
        deckAsObjects.add(one);
        deckAsObjects.add(two);
        deckAsObjects.add(two);
        deckAsObjects.add(three);
        deckAsObjects.add(three);
        deckAsObjects.add(four);
        deckAsObjects.add(four);
        deckAsObjects.add(five);
        deckAsObjects.add(five);
        deckAsObjects.add(six);
        deckAsObjects.add(seven);
        deckAsObjects.add(eight);
    }

    public ArrayList<Integer> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Integer> deck) {
        this.deck = deck;
    }

    public Integer getLastCardPlayed() {
        return lastCardPlayed;
    }

    public void setLastCardPlayed(Integer lastCardPlayed) {
        this.lastCardPlayed = lastCardPlayed;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public ArrayList<String> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public Player addPlayer(Player player) {
        player.setPlayerId(this.players.size() + 1);
        player.setPlayerName("Player " + player.getPlayerId());
        this.players.add(player.getPlayerName());
        if (this.players.size() >= 1) {
            this.gameOn = true;
        }
        return player;
    }

    public void endGame() {
        this.gameOn = false;
        this.players.clear();
        this.lastCardPlayed = null;
        this.deck = new ArrayList(Arrays.asList(1,1,1,1,1,2,2,3,3,4,4,5,5,6,7,8));
        this.lastTurn = null;

        //TODO: alternatively, just delete db object
    }

    public Integer getLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(int id) {
        this.lastTurn = id;
    }

    public void deal() {
        for (Player player: this.playersAsObjects) {
            this.playersAsObjects.re
        }
        Collections.shuffle;
    }
}
