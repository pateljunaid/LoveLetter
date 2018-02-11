package com.example.junaidpatel.loveletter;

public class Seven implements Card {
    private String cardName = "countess";
    private String cardAbility = "If you have this card and the Six or Five in your hand, \nyou must discard this card";


    private int imageId = R.drawable.countess;

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
    }
}