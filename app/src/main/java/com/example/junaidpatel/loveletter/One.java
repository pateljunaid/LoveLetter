package com.example.junaidpatel.loveletter;

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
    public void  specialFunction(Game game, Integer choice, Player currentPlayer, Player targetPlayer) {
        if (targetPlayer.getCard1() == choice) {
            game.eliminate(targetPlayer);
            game.addBroadcast(currentPlayer.getPlayerName() + " guesses a " + choice +
                    " on " + targetPlayer.getPlayerName() + ". They are eliminated!");
        }
        else {
            game.addBroadcast(currentPlayer.getPlayerName() + " guesses a " + choice +
                    " on " + targetPlayer.getPlayerName() + ". Wrong guess!");
        }
    }
}
