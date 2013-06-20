package com.test.services;

import com.test.dao.CommandLine;
import com.test.domain.Bowler;
import com.test.domain.BowlingScoreCard;

import static com.test.domain.Frame.frame;

public class DefaultBowlingScoreService implements BowlingScoreService {
    private CommandLine commandLine;

    public DefaultBowlingScoreService(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public BowlingScoreService startGame() {
        commandLine.displayStartGameMessage();
        return this;
    }

    @Override
    public BowlingScoreService addAllBowlersToScoreCard(BowlingScoreCard scoreCard) {
        int numberOfBowlers = commandLine.readNumberOfBowlers();
        int bowlerIndex = 0;

        do {
            if (addBowlerToScoreCard(scoreCard, new Bowler(commandLine.readBowlerName()))) {
                bowlerIndex++;
            }

        } while (bowlerIndex < numberOfBowlers);
        return this;
    }

    @Override
    public BowlingScoreService addScoresForFrame(int frameNumber, BowlingScoreCard scoreCard) {
        for (Bowler bowler : scoreCard.allBowlers()) {
            int[] frameScore = commandLine.readScoreForFrame(frameNumber, bowler.name());

            scoreCard.addScoreForFrame(bowler, frame(frameNumber, frameScore[0], frameScore[1], frameScore[2]));
        }
        return this;
    }

    @Override
    public BowlingScoreService printTeamScore(BowlingScoreCard scoreCard) {
        int teamScore = 0;

        for (Bowler bowler : scoreCard.allBowlers()) {
            teamScore += scoreCard.bowlerScore(bowler);
        }

        commandLine.printTeamScore(teamScore);
        return this;
    }

    @Override
    public BowlingScoreService endGame() {
        commandLine.displayEndGameMessage();
        return this;
    }

    @Override
    public BowlingScoreService enterScoresForAllFrames(BowlingScoreCard scoreCard) {
        for (int frameNumber = 1; frameNumber <= BowlingRules.LAST_FRAME_NUMBER; frameNumber++) {
            addScoresForFrame(frameNumber, scoreCard);
        }
        return this;
    }

    @Override
    public BowlingScoreService printWinningPlayerName(BowlingScoreCard scoreCard) {
        String winningBowlerName = "";
        int highestScore = 0;

        for (Bowler bowler : scoreCard.allBowlers()) {

            if (scoreCard.bowlerScore(bowler) > highestScore) {
                winningBowlerName = bowler.name();
                highestScore = scoreCard.bowlerScore(bowler);
            }
        }

        commandLine.printWinnersName(winningBowlerName);
        return this;
    }

    private boolean addBowlerToScoreCard(BowlingScoreCard scoreCard, Bowler bowler) {
        if (scoreCard.bowlerAlreadyOnScoreCard(bowler)) {
            commandLine.duplicateBowlerNameEntered();
            return false;
        }

        scoreCard.addBowler(bowler);
        return true;
    }
}
