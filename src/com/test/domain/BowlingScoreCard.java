package com.test.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BowlingScoreCard {
    private final Map<Bowler, ScoreCardLine> scoreCardLines = new HashMap<Bowler, ScoreCardLine>();

    public BowlingScoreCard() {}

    public BowlingScoreCard addBowler(Bowler bowler) {
        scoreCardLines.put(bowler, new ScoreCardLine());
        return this;
    }

    public boolean bowlerAlreadyOnScoreCard(Bowler bowler) {
        return scoreCardLines.containsKey(bowler);
    }

    public BowlingScoreCard addScoreForFrame(Bowler bowler, Frame frame) {
        if (scoreCardLines.containsKey(bowler)) {
            ScoreCardLine scoreCardLine = scoreCardLines.get(bowler);
            scoreCardLine.addFrameScore(frame);
        }
        return this;
    }

    public Set<Bowler> allBowlers() {
        return scoreCardLines.keySet();
    }

    public int bowlerScore(Bowler bowler) {
        return scoreCardLines.get(bowler).totalScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlingScoreCard that = (BowlingScoreCard) o;

        return scoreCardLines.equals(that.scoreCardLines);
    }


    @Override
    public int hashCode() {
        return scoreCardLines.hashCode();
    }
}
