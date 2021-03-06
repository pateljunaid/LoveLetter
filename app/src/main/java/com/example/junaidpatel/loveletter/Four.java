package com.example.junaidpatel.loveletter;

public class Four implements Card{
    private String cardName = "handmaid";
    private String cardAbility = "Until your next turn, ignore all effects from other players' cards.";

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
        game.addBroadcast(currentPlayer.getPlayerName() + " is protected for the round");
    }
}