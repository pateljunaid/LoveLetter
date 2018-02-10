package com.example.junaidpatel.loveletter;

public class Eight implements Card {
    private String cardName = "princess";
    private String cardAbility = "If you discard this card, you are out of the round";

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

        game.addBroadcast(currentPlayer.getPlayerName() + " has dropped an Eight! They are out!");
        game.eliminate(currentPlayer);
    }
}

