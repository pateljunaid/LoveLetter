package com.example.junaidpatel.loveletter;

public class Five implements Card{
    private int cardValue = 5;
    private String cardName = "prince";
    private String cardAbility = "Choose any player (including yourself) to discard his or her hand and drawCard a new card.";

    @Override
    public String getCardAbility() {
        return this.cardAbility;
    }

    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public void  specialFunction(Game game, Player currentPlayer, Player targetPlayer) {
        game.addBroadcast(currentPlayer.getPlayerName() + " targeted " + targetPlayer.getPlayerName());
        game.setTopCard(targetPlayer.getCard1());
        if (targetPlayer.getCard1() == 8) {
            game.eliminate(targetPlayer);
            game.addBroadcast("They dropped an 8, they're out!");
        }
        else {
            targetPlayer.setCard1(game.drawCard());
            game.addBroadcast("They drew a new card!");
        }
        MainActivity.nextTurn();
    }
}
