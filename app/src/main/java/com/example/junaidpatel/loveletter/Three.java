package com.example.junaidpatel.loveletter;

public class Three implements Card {
    private String cardName = "baron";
    private String cardAbility = "You and another player secretly compare hands. \nThe player with the lower value is out of the round.";

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
        if (currentPlayer.getCard1() > targetPlayer.getCard1()) {
            game.eliminate(targetPlayer);
            game.addBroadcast(targetPlayer.getPlayerName() + " compared cards with " + currentPlayer.getPlayerName());
            targetPlayer.setSecretMessage(currentPlayer.getPlayerName() +" was holding a " + targetPlayer.getCard1());
        }
        else if (currentPlayer.getCard1() < targetPlayer.getCard1()) {
            game.eliminate(currentPlayer);
            game.addBroadcast(currentPlayer.getPlayerName() + " compared cards with " + targetPlayer.getPlayerName());
            currentPlayer.setSecretMessage(targetPlayer.getPlayerName() +" was holding a " + targetPlayer.getCard1());
        }
        else {
            game.addBroadcast("Compare was a tie");
        }
    }
}
