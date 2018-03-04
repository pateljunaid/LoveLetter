package com.example.junaidpatel.loveletter;

public class Two implements Card {
    private String cardName = "priest";
    private String cardAbility = "Look at another player's hand.";

    @Override
    public String getCardAbility() {
        return this.cardAbility;
    }

    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public void specialFunction(Game game, Player currentPlayer, Player targetPlayer) {
        game.addBroadcast(currentPlayer.getPlayerName() + " took a peak at " + targetPlayer.getPlayerName() +
        "'s card");
        MainActivity.showToast(targetPlayer.getPlayerName() + " is holding a " + targetPlayer.getCard1());
        MainActivity.nextTurn();
    }
}
