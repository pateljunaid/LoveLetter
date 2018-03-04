package com.example.junaidpatel.loveletter;

public class Six implements Card {
    private String cardName = "king";
    private String cardAbility = "Trade hands with another player of your choice.";

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
        int one = currentPlayer.getCard1();
        int two = targetPlayer.getCard1();

        currentPlayer.setCard1(two);
        targetPlayer.setCard1(one);

        game.addBroadcast(currentPlayer.getPlayerName() + " and " + targetPlayer.getPlayerName() + " have swapped hands!");
        MainActivity.nextTurn();
    }
}