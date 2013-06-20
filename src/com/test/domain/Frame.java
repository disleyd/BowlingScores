package com.test.domain;

import com.test.services.BowlingRules;

public class Frame {
    static final Frame NULL_FRAME = frame(BowlingRules.FIRST_FRAME_NUMBER, 0, 0, 0);

    private int frameNumber;
    private final int ball1;
    private final int ball2;
    private final int ball3;

    private Frame(int frameNumber, int ball1, int ball2, int ball3) {
        this.frameNumber = frameNumber;
        this.ball1 = ball1;
        this.ball2 = ball2;
        this.ball3 = ball3;
    }

    public static Frame frame(int frameNumber, int ball1, int ball2, int ball3) {
        return new Frame(frameNumber, ball1, ball2, ball3);
    }

    public int firstBall() {
        return ball1;
    }

    public int secondBall() {
        return ball2;
    }

    public int thirdBall() {
        return ball3;
    }

    public int frameNumber() {
        return frameNumber;
    }
}
