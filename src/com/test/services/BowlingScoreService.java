package com.test.services;

import com.test.domain.BowlingScoreCard;

public interface BowlingScoreService {
    BowlingScoreService startGame();

    BowlingScoreService addAllBowlersToScoreCard(BowlingScoreCard scoreCard);

    BowlingScoreService addScoresForFrame(int frameNumber, BowlingScoreCard scoreCard);

    BowlingScoreService printWinningPlayerName(BowlingScoreCard scoreCard);

    BowlingScoreService printTeamScore(BowlingScoreCard scoreCard);

    BowlingScoreService endGame();

    BowlingScoreService enterScoresForAllFrames(BowlingScoreCard scoreCard);
}
