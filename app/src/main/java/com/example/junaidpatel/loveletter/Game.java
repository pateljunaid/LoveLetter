package com.example.junaidpatel.loveletter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
    private HashMap<Integer, Card> cardsMap;
    private Integer topCard;
    private String broadcast;
    private boolean hasStarted;
    private boolean isOver;
    protected Integer turn;

    public Game() {
        this.deck = MainActivity.constants.deck;
        this.players = new ArrayList<>();
        this.cardsMap = new HashMap<>();
        this.turn = 0;
        this.broadcast = "";
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

    public ArrayList<Integer> getDeck() {
        return this.deck;
    }

    public void setDeck(ArrayList<Integer> deck) {
        this.deck = deck;
    }

    public void addBroadcast(String broadcast) {
        this.broadcast += "\n" + broadcast;
    }

    public Integer getTopCard() {
        return topCard;
    }

    public void setTopCard(Integer topCard) {
        this.topCard = topCard;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void addPlayer(Player player) {

        player.setPlayerId(this.players.size() + 1);
        player.setPlayerName("Player "+player.getPlayerId());
        this.players.add(player);
        player.setInGame(true);

        //Show the start game button to the host once they join
        if (player.getPlayerId() == 1) {
            MainActivity.startGame.setVisibility(View.VISIBLE);
            MainActivity.resetDB.setVisibility(View.VISIBLE);
        }
    }

    public Integer getTurn() {
        return turn;
    }

    public void asetTurn(Player currPlayer) {
        int i = this.turn;

        if (!currPlayer.isEliminated()) {
            i+=1;
        }

        if (i >= this.players.size()) {
            i = 0;
        }

        this.turn = i;
    }

    public void deal() {
        Collections.shuffle(this.deck);
        int burned = (int) this.deck.remove(0);
        for (Player player: this.players) {
            player.setCard1(this.drawCard());
        }
    }

    public Integer drawCard() {
        return (Integer) this.deck.remove(0);
    }

    public void playCard(final Player currPlayer, final int card, final int cardNum) {
        if (Arrays.asList(1,2,3,5,6).contains(card)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mMainActivity);
            builder.setTitle("Pick a player");
            builder.setItems(this.agetPlayerNames().toArray(new CharSequence[this.players.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Player targetPlayer = Game.this.players.get(id);
                    if (currPlayer == targetPlayer && card != 5) {
                        MainActivity.showToast("Cannot target yourself");
                        builder.show();
                    }
                    else {
                        MainActivity.removeListeners();
                        Game.this.topCard = card;
                        currPlayer.setLastPlayed(card);
                        MainActivity.card2.setVisibility(View.INVISIBLE);
                        if (cardNum == 1) {
                            currPlayer.setCard1(currPlayer.getCard2());
                            currPlayer.setCard2(null);
                        } else {
                            currPlayer.setCard2(null);
                        }

                        if (targetPlayer.getLastPlayed() != 4) {
                            Game.this.cardsMap.get(card).specialFunction(Game.this, currPlayer, targetPlayer);
                        }
                        else {
                            Game.this.addBroadcast(targetPlayer.getPlayerName() + " is protected");
                            MainActivity.nextTurn();
                        }
                        Game.this.addBroadcast(currPlayer.getPlayerName() + " has played " + card + " on " + targetPlayer.getPlayerName());
                    }
                    }
            });
            builder.show();

        } else {
            MainActivity.removeListeners();
            Game.this.topCard = card;
            currPlayer.setLastPlayed(card);
            MainActivity.card2.setVisibility(View.INVISIBLE);
            if (cardNum == 1) {
                currPlayer.setCard1(currPlayer.getCard2());
                currPlayer.setCard2(null);
            } else {
                currPlayer.setCard2(null);
            }
            this.cardsMap.get(card).specialFunction(this, currPlayer, null);
            MainActivity.nextTurn();
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

    public ArrayList<String> agetPlayerNames() {
        ArrayList<String> playerNames = new ArrayList<String>();
        for (Player player: this.players) {
            playerNames.add(player.getPlayerName());
        }
        return playerNames;
    }

    public boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public void eliminate(Player player) {
        this.addBroadcast(player.getPlayerName() + "  is eliminated");
        this.players.remove(agetPlayerById(player.getPlayerId()));
        player.setInGame(false);
        player.setEliminated(true);
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public Player agetCurrentTurnPlayer() {
        return this.players.get(this.turn);
    }
}
