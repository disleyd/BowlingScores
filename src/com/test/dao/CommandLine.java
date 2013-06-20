package com.test.dao;

public interface CommandLine {
    void displayStartGameMessage();

    int readNumberOfBowlers();

    String readBowlerName();

    void duplicateBowlerNameEntered();

    int[] readScoreForFrame(int frameNumber, String bowlerName);

    void printWinnersName(String winningBowlerName);

    void printTeamScore(int teamScore);

    void displayEndGameMessage();
}
