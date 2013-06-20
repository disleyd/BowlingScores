package com.test.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BowlingScoreCard {
    private Map<Bowler, ScoreCardLine> scoreCardLines = new HashMap<Bowler, ScoreCardLine>();

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
        return scoreCardLines.get(bowler).score();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlingScoreCard that = (BowlingScoreCard) o;

        return !(scoreCardLines != null ? !scoreCardLines.equals(that.scoreCardLines) : that.scoreCardLines != null);
    }


    @Override
    public int hashCode() {
        return scoreCardLines != null ? scoreCardLines.hashCode() : 0;
    }
}
