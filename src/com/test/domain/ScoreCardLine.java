package com.test.domain;

import com.test.services.BowlingRules;

import java.util.ArrayList;

import static com.test.domain.Frame.NULL_FRAME;

public class ScoreCardLine {

    private ArrayList<Frame> frames = new ArrayList<Frame>(BowlingRules.MAX_NUMBER_OF_FRAMES);

    public void addFrameScore(Frame frame) {
        if (frames.size() <= BowlingRules.MAX_NUMBER_OF_FRAMES) {
            frames.add(frame);
        }
    }

    public int score() {
        int totalScore = 0;

        for (int frameIndex = 0; frameIndex < frames.size(); frameIndex++) {
            if (frameIndex < BowlingRules.MAX_NUMBER_OF_FRAMES) {
                totalScore += scoreForFrame(frameIndex);
            }
        }
        return totalScore;
    }

    private int scoreForFrame(int frameIndex) {
        int scoreForFrame = 0;

        Frame thisFrame = frames.get(frameIndex);
        Frame nextFrame = getNextFrame(frameIndex);
        Frame frameAfterNext= getFrameAfterNext(frameIndex);

        scoreForFrame += BowlingRules.scoreWithoutBonus(thisFrame);
        scoreForFrame += BowlingRules.scoreWithSpareOrStrikeBonus(thisFrame, nextFrame, frameAfterNext);

        return scoreForFrame;
    }

    private Frame getFrameAfterNext(int frameIndex) {
        if (frameIndex + 2 < frames.size()) {
            return frames.get(frameIndex + 2);

        } else {
            return NULL_FRAME;
        }
    }

    private Frame getNextFrame(int frameIndex) {
        if (frameIndex + 1 < frames.size()) {
            return frames.get(frameIndex + 1);

        } else {
            return NULL_FRAME;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreCardLine that = (ScoreCardLine) o;

        return !(frames != null ? !frames.equals(that.frames) : that.frames != null);
    }

    @Override
    public int hashCode() {
        return frames != null ? frames.hashCode() : 0;
    }
}
