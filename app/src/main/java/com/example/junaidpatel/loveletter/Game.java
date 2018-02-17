package com.example.junaidpatel.loveletter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
    public ArrayList<String> playerNames = new ArrayList<String>();
    private ArrayList deck;
    private ArrayList<Player> players;
    private HashMap<String, Player> playersMap;
    private HashMap<Integer, Card> cardsMap = new HashMap<Integer, Card>();
    private Integer topCard;
    private String broadcast;
    private boolean gameOn;
    private boolean hasStarted;
    protected Integer turn;

    public Game() {
        this.deck = MainActivity.constants.deck;
        this.players = new ArrayList<Player>();
        this.playersMap = new HashMap<String, Player>();
        this.turn = 0;
        Card one = new One();
        Card two = new Two();
        Card three = new Three();
        Card four = new Four();
        Card five = new Five();
        Card six = new Six();
        Card seven = new Seven();
        Card eight = new Eight();
        cardsMap.put(1, one);
        cardsMap.put(2, two);
        cardsMap.put(3, three);
        cardsMap.put(4, four);
        cardsMap.put(5, five);
        cardsMap.put(6, six);
        cardsMap.put(7, seven);
        cardsMap.put(8, eight);
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

    public ArrayList<Integer> getDeck() {
        return this.deck;
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
        player.setPlayerName(MainActivity.constants.playerNames.get(player.getPlayerId() - 1));
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

//        this.playersMap.put(String.valueOf(player.getPlayerId()), player);
        this.playerNames.add(player.getPlayerName());
//        Log.d("yakkity", Game.this.playersMap.toString());

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
        //TODO: int burned = (int) this.deck.remove(0);
        for (Player player: this.players) {
            player.setCard1(this.drawCard());
        }
    }

    public Integer drawCard() {
        Log.d("yakkity", this.getDeck().toString());
        return (Integer) this.deck.remove(0);
    }

    public void playCard(Player currPlayer, int card, int cardNum) {
        MainActivity.removeListeners();
        MainActivity.card2.setVisibility(View.INVISIBLE);
        if (cardNum == 1) {
            currPlayer.setCard1(currPlayer.getCard2());
            currPlayer.setCard2(null);
        } else {
            currPlayer.setCard2(null);
        }
        this.addBroadcast(currPlayer.getPlayerName() + " has played " + card);
        this.topCard = card;
        currPlayer.setLastPlayed(card);

        final Player curr = currPlayer;
        final int mCard = card;

        if (Arrays.asList(1,2,3,5,6).contains(card)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mMainActivity);
            builder.setTitle("Pick a player");
            //TODO: Disallow selecting self as a target
            builder.setItems(this.playerNames.toArray(new CharSequence[this.playerNames.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    id+=1;
                    Log.d("yakkity", String.valueOf(id));
//                    Log.d("yakkity", Game.this.playersMap.toString());
                    Log.d("yakkity", Game.this.playerNames.toString());

                    Player targetPlayer = Game.this.agetPlayerById(id);

                    if (targetPlayer.getLastPlayed() != 4) {
                        Game.this.cardsMap.get(mCard).specialFunction(Game.this, curr, targetPlayer);
                    } else {
                        Game.this.addBroadcast(targetPlayer.getPlayerName() + " is protected");
                        MainActivity.pushData();
                    }
                }
            });
            builder.show();

        } else {
            this.cardsMap.get(card).specialFunction(this, currPlayer, null);
            MainActivity.pushData();
        }
    }


    public Player agetPlayerById(int playerId) {
        for (Player player : this.players) {
            if (player.getPlayerId() == playerId) {
                return player;
            }
        }

        return null;
    }

    public Player agetPlayerByName(String name) {
        for (Player player : this.players) {
            if (player.getPlayerName() == name) {
                return player;
            }
        }

        return null;
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
        this.players.remove(agetPlayerById(player.getPlayerId()));
        this.playerNames.remove(player.getPlayerName());
//        this.playersMap.remove(String.valueOf(player.getPlayerId()));
        player.setInGame(false);
        player.setEliminated(true);
    }

    public Player nextPlayer(Player currPlayer) {
        int i = this.players.indexOf(currPlayer);

        if (i == (this.players.size() - 1)) {
            return this.players.get(0);
        } else {
            return this.players.get(i + 1);
        }
    }

//    public HashMap<String, Player> getPlayersMap() {
//        return playersMap;
//    }
//
//    public void setPlayersMap(HashMap<String, Player> playersMap) {
//        this.playersMap = playersMap;
//    }
}
