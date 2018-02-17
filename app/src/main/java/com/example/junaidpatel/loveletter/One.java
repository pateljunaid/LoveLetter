package com.example.junaidpatel.loveletter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class One implements Card {

    private String cardName = "guard";
    private String cardAbility = "Name a non-One card and choose another player. \nIf that player has that card, he or she is out of the round.";

    @Override
    public String getCardAbility() {
        return this.cardAbility;
    }

    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public void specialFunction(Game game, Player currentPlayer, final Player targetPlayer) {

        final Game mGame = game;
        final Player curr = currentPlayer;
        final Player target = targetPlayer;

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mMainActivity);
        builder.setTitle("What card do you guess?");
        builder.setItems(MainActivity.constants.guessList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Integer choice = id + 1;
                Log.d("yakkity", "Choice chosen: " +choice);

                if (choice == 1) {
                    mGame.addBroadcast("Cannot guess a 1");
                } else if (targetPlayer.getCard1() == choice) {
                    Log.d("yakkity", "right guess");
                    mGame.eliminate(targetPlayer);
                    mGame.addBroadcast(curr.getPlayerName() + " guesses a " + choice +
                            " on " + target.getPlayerName() + ". They are eliminated!");
                } else {
                    Log.d("yakkity", "wrong guess");
                    mGame.addBroadcast(curr.getPlayerName() + " guesses a " + choice +
                            " on " + target.getPlayerName() + ". Wrong guess!");
                }
                MainActivity.pushData();
            }
        });
        builder.show();
    }
}
