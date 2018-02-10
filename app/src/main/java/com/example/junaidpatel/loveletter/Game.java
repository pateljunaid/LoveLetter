package com.example.junaidpatel.loveletter;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by junaid.patel on 2018-01-27.
 */

public class Game {
    private ArrayList deck;
    private ArrayList<Player> players;
    private HashMap<Integer, Player> playersMap;
    private HashMap<Integer, Card> cards = new HashMap<Integer, Card>();
    private Integer topCard;
    protected Integer turn;
    private String broadcast;
    private boolean gameOn;
    private boolean hasStarted;

    public Game() {
        this.deck = MainActivity.constants.deck;
        this.players = new ArrayList<Player>();
        this.playersMap = new HashMap<Integer, Player>();
        this.turn = 0;
        Card one = new One();
        Card two = new Two();
        Card three = new Three();
        Card four = new Four();
        Card five = new Five();
        Card six = new Six();
        Card seven = new Seven();
        Card eight = new Eight();
        cards.put(1, one);
        cards.put(2, two);
        cards.put(3, three);
        cards.put(4, four);
        cards.put(5, five);
        cards.put(6, six);
        cards.put(7, seven);
        cards.put(8, eight);
    }


    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public void addBroadcast(String broadcast) {
        this.broadcast += "\n" + broadcast;
    }

    public ArrayList<Integer> agetDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Integer> deck) {
        this.deck = deck;
    }

    public Integer getTopCard() {
        return topCard;
    }

    public void setTopCard(Integer topCard) {
        this.topCard = topCard;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {

        player.setPlayerId(this.players.size() + 1);
        player.setPlayerName("Player " + player.getPlayerId());
        this.players.add(player);
        player.setInGame(true);

        //Show the start game button to the host once they join
        if (player.getPlayerId() == 1) {
            MainActivity.startGame.setVisibility(View.VISIBLE);
        }

        //TODO: opt
        if (this.players.size() >= 1) {
            this.gameOn = true;
        }

        this.playersMap.put(player.getPlayerId(), player);
    }

    public void endGame() {
        this.gameOn = false;
        this.players.clear();
        this.topCard = null;
        this.deck = new ArrayList(Arrays.asList(1,1,1,1,1,2,2,3,3,4,4,5,5,6,7,8));
        this.turn = 0;
    }

    public Integer getTurn() {
        return turn;
    }

    public void asetTurn(Player currPlayer) {
        int i = this.players.indexOf(currPlayer);

        if (i == (this.players.size() - 1)) {
            this.turn = this.players.get(0).getPlayerId();
        }
        else {
            this.turn = this.players.get(i + 1).getPlayerId();
        }
    }

    public void deal() {
        Collections.shuffle(this.deck);
        Log.d("yakkity", this.deck.toString());
//        int burned = (int) this.deck.remove(0);
        for (Player player: this.players) {
            player.setCard1(this.drawCard());
        }
    }

    public Integer drawCard() {
        return (Integer) this.deck.remove(0);
    }

    public void playCard(Player currPlayer,Integer card, int cardNum) {
        currPlayer.setLastPlayed(card);
        this.topCard = card;

        if (cardNum == 1) {
            currPlayer.setCard1(currPlayer.getCard2());
            currPlayer.setCard2(null);
        }
        else {
            currPlayer.setCard2(null);
        }
        MainActivity.card2.setVisibility(View.INVISIBLE);

        this.addBroadcast(currPlayer.getPlayerName() + " has played " + card);

        if (targetPlayer.getLastPlayed() != 4) {
            cards.get(card).specialFunction(this, guess, currPlayer, targetPlayer);
        }
        else {
            this.addBroadcast(targetPlayer.getPlayerName() + " is protected");
        }
        MainActivity.pushData();
    }

    public Player agetPlayerById(int playerId) {
        return this.players.get(playerId - 1);
    }

    public boolean isHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public void startGame() {
        this.setHasStarted(true);
        this.deal();
    }

    public void eliminate(Player player) {
        this.players.remove(player.getPlayerId());
        player.setInGame(false);
    }

    public Player nextPlayer(Player currPlayer) {
        int i = this.players.indexOf(currPlayer);

        if (i == (this.players.size() - 1)) {
            return this.players.get(0);
        } else {
            return this.players.get(i + 1);
        }
    }
}
