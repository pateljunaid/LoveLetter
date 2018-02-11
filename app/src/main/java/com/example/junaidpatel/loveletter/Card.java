package com.example.junaidpatel.loveletter;

/**
 * The card class will be created to give certain state and functionality to all cards and
 * classes which inherit from the Cards class.
 * Created by padcf, paulvincentphillips & bradyc12 on 01/11/16.
 */

public interface Card {

    String getCardAbility();
    String getCardName();

    void specialFunction(Game game, Player currentPlayer, Player targetPlayer);

}