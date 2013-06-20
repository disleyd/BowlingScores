package com.test.services;

import com.test.domain.Frame;

public class BowlingRules {
    private static final int MAXIMUM_TWO_BALL_SCORE_FOR_LAST_FRAME = 20;

    public static final int MAX_NUMBER_OF_FRAMES = 10;
    public static final int FIRST_FRAME_NUMBER = 1;
    public static final int LAST_FRAME_NUMBER = MAX_NUMBER_OF_FRAMES;
    public static final int MINIMUM_NUMBER_OF_BOWLERS = 1;
    public static final int MAXIMUM_NUMBER_OF_BOWLERS = 10;
    public static final int MINIMUM_BALL_SCORE = 0;
    public static final int MAXIMUM_BALL_SCORE = 10;
    public static final int MAXIMUM_TWO_BALL_FRAME_SCORE = 10;

    public static boolean validFrameScore(int frameNumber, int scoreForBall1, int scoreForBall2) {
        if (frameNumber < LAST_FRAME_NUMBER) {
            return validScoreForAnyFrameExceptLastFrame(scoreForBall1, scoreForBall2);

        } else {
            return validScoreForLastFrame(scoreForBall1, scoreForBall2);
        }
    }

    public static boolean validScoreForASingleBall(int scoreForBall) {
        return scoreForBall >= MINIMUM_BALL_SCORE && scoreForBall <= MAXIMUM_BALL_SCORE;
    }

    public static boolean lastFrameBonusBallAwarded(int frameNumber, int scoreForBall1, int scoreForBall2) {
        return frameNumber == LAST_FRAME_NUMBER && (strikeBonusAwarded(scoreForBall1) || spareBonusAwarded(scoreForBall1, scoreForBall2));
    }

    private static boolean spareBonusAwarded(int scoreForBall1, int scoreForBall2) {
        return scoreForBall1 + scoreForBall2 == MAXIMUM_BALL_SCORE;
    }

    private static boolean strikeBonusAwarded(int scoreForBall1) {
        return scoreForBall1 == MAXIMUM_BALL_SCORE;
    }

    public static boolean isSpare(Frame frame) {
        return !isStrike(frame) && (frame.firstBall() + frame.secondBall() == MAXIMUM_BALL_SCORE);
    }

    public static boolean isStrike(Frame frame) {
        return frame.firstBall() == MAXIMUM_BALL_SCORE;
    }

    public static boolean isLastFrame(Frame frame) {
        return frame.frameNumber() == LAST_FRAME_NUMBER;
    }

    public static boolean isNotLastFrame(Frame frame) {
        return !isLastFrame(frame);
    }

    public static int scoreWithoutBonus(Frame thisFrame) {
        return thisFrame.firstBall() + thisFrame.secondBall() + thisFrame.thirdBall();
    }

    public static int scoreWithSpareOrStrikeBonus(Frame thisFrame, Frame nextFrame, Frame frameAfterNext) {
        int bonusScore = 0;

        if (isSpare(thisFrame)) {
            bonusScore += nextFrame.firstBall();

        } else if (isStrike(thisFrame)) {
            bonusScore += nextFrame.firstBall();

            if (isStrike(nextFrame) && isNotLastFrame(nextFrame)) {
                bonusScore += frameAfterNext.firstBall();

            } else {
                bonusScore += nextFrame.secondBall();
            }
        }
        return bonusScore;
    }

    private static boolean validScoreForLastFrame(int scoreForBall1, int scoreForBall2) {
        return scoreForBall1 + scoreForBall2 <= MAXIMUM_TWO_BALL_SCORE_FOR_LAST_FRAME;
    }

    private static boolean validScoreForAnyFrameExceptLastFrame(int scoreForBall1, int scoreForBall2) {
        return scoreForBall1 + scoreForBall2 <= MAXIMUM_TWO_BALL_FRAME_SCORE;
    }
}
