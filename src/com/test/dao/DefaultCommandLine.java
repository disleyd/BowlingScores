package com.test.dao;

import com.test.services.BowlingRules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class DefaultCommandLine implements CommandLine {
    static final String INVALID_BOWLER_NAME = "";
    static final int INVALID_NUMBER = -999;

    static final String NUMBER_OF_BOWLERS_PROMPT = "Enter Number of bowlers ";
    static final String BOWLER_NAME_PROMPT = "Enter bowler name ";
    static final String BOWLER_PROMPT = "Bowler: ";
    static final String FRAME_PROMPT = "Frame: ";
    static final String ENTER_SCORE_FOR_BALL_1_PROMPT = "Enter Score for Ball: 1";
    static final String ENTER_SCORE_FOR_BALL_2_PROMPT = "Enter Score for Ball: 2";
    static final String ENTER_SCORE_FOR_BALL_3_PROMPT = "Enter Score for Ball: 3";

    static final String START_BOWLING_MESSAGE = "Start Bowling!!!";
    static final String END_BOWLING_MESSAGE = "Game Over!!!";
    static final String INVALID_INPUT_MESSAGE = "I'm sorry, I didn't understand that... ";
    static final String DUPLICATE_BOWLER_MESSAGE = "Sorry, that name has already been entered. Please enter a different name.";
    static final String TEAM_SCORE_MESSAGE = "Team score is...";
    static final String WINNING_PLAYER_MESSAGE = "*** The winner is...";

    static final String NUMBER_OF_BOWLERS_HELP_MESSAGE = "Please enter a whole number between " + BowlingRules.MINIMUM_NUMBER_OF_BOWLERS + " and " + BowlingRules.MAXIMUM_NUMBER_OF_BOWLERS;
    static final String BOWLING_SCORE_HELP_MESSAGE = "Please enter a whole number between " + BowlingRules.MINIMUM_BALL_SCORE + " and " + BowlingRules.MAXIMUM_BALL_SCORE;
    static final String BOWLER_NAME_HELP_MESSAGE = "Please enter upper and lowercase letters and spaces (no numbers, no punctuation)";
    static final String TWO_BALL_FRAME_SCORE_HELP_MESSAGE = "Please enter a frame score of " + BowlingRules.MAXIMUM_TWO_BALL_FRAME_SCORE + " or less";

    private BufferedReader inputStream;
    private PrintStream outputStream;

    public DefaultCommandLine(BufferedReader inputStream, PrintStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void displayStartGameMessage() {
        outputStream.println(START_BOWLING_MESSAGE);
    }

    @Override
    public int readNumberOfBowlers() {
        int numberOfBowlers;

        do {
            outputStream.println(NUMBER_OF_BOWLERS_PROMPT);
            numberOfBowlers = readNumber();

            if (numberOfBowlers == INVALID_NUMBER ||
                    (numberOfBowlers < BowlingRules.MINIMUM_NUMBER_OF_BOWLERS || numberOfBowlers > BowlingRules.MAXIMUM_NUMBER_OF_BOWLERS)) {

                outputStream.println(INVALID_INPUT_MESSAGE);
                outputStream.println(NUMBER_OF_BOWLERS_HELP_MESSAGE);
                numberOfBowlers = INVALID_NUMBER;
            }

        } while (numberOfBowlers == INVALID_NUMBER);

        return numberOfBowlers;
    }

    @Override
    public String readBowlerName() {
        String name;

        do {
            outputStream.println(BOWLER_NAME_PROMPT);
            name = readName();

            if (invalidName(name)) {
                name = INVALID_BOWLER_NAME;
                outputStream.println(BOWLER_NAME_HELP_MESSAGE);
            }

        } while (name.equals(INVALID_BOWLER_NAME));

    return name;
    }

    @Override
    public void duplicateBowlerNameEntered() {
        outputStream.println(DUPLICATE_BOWLER_MESSAGE);
    }

    @Override
    public int[] readScoreForFrame(int frameNumber, String bowlerName) {
        int scoreForBall1;
        int scoreForBall2;
        boolean validScore;

        do {
            outputStream.println(BOWLER_PROMPT + bowlerName);
            outputStream.println(FRAME_PROMPT + frameNumber);

            scoreForBall1 = readScoreForBall(ENTER_SCORE_FOR_BALL_1_PROMPT);
            scoreForBall2 = readScoreForBall(ENTER_SCORE_FOR_BALL_2_PROMPT);
            validScore = BowlingRules.validFrameScore(frameNumber, scoreForBall1, scoreForBall2);

            if (!validScore) {
                outputStream.println(INVALID_INPUT_MESSAGE);
                outputStream.println(TWO_BALL_FRAME_SCORE_HELP_MESSAGE);
            }

        } while (!validScore);

        int scoreForBall3 = readScoreForBonusBall(frameNumber, scoreForBall1, scoreForBall2);

        return new int[]{scoreForBall1, scoreForBall2, scoreForBall3};
    }

    @Override
    public void printWinnersName(String winningBowlerName) {
        outputStream.println(WINNING_PLAYER_MESSAGE + winningBowlerName);
    }

    @Override
    public void printTeamScore(int teamScore) {
        outputStream.println(TEAM_SCORE_MESSAGE + teamScore);
    }

    @Override
    public void displayEndGameMessage() {
        outputStream.println(END_BOWLING_MESSAGE);
    }

    private boolean invalidName(String name) {
        final String ALPHA_ONLY_REGEX = "[a-zA-Z ]*";

        return null == name || "".equals(name) || !(name.matches(ALPHA_ONLY_REGEX));
    }

    private int readScoreForBall(String prompt) {
        int scoreForBall;

        do {
            outputStream.println(prompt);
            scoreForBall = readNumber();

            if (scoreForBall == INVALID_NUMBER || !BowlingRules.validScoreForASingleBall(scoreForBall)) {
                outputStream.println(INVALID_INPUT_MESSAGE);
                outputStream.println(BOWLING_SCORE_HELP_MESSAGE);
                scoreForBall = INVALID_NUMBER;
            }

        } while (scoreForBall == INVALID_NUMBER);

        return scoreForBall;
    }

    private int readScoreForBonusBall(int frameNumber, int scoreForBall1, int scoreForBall2) {
        if (BowlingRules.lastFrameBonusBallAwarded(frameNumber, scoreForBall1, scoreForBall2)) {
            return readScoreForBall(ENTER_SCORE_FOR_BALL_3_PROMPT);

        } else {
            return 0;
        }
    }

    private String readName() {
        String name;

        try {
            name = inputStream.readLine();

        } catch (IOException e) {
            return INVALID_BOWLER_NAME;
        }
        return name;
    }

    private int readNumber() {
        int inputNumber;

        try {
            String inputLine = inputStream.readLine();
            inputNumber = Integer.parseInt(inputLine);

        } catch (IOException e) {
            return INVALID_NUMBER;

        } catch (NumberFormatException e) {
            return INVALID_NUMBER;
        }

        return inputNumber;
    }
}
