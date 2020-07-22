package com.test.app;

import com.test.dao.CommandLine;
import com.test.dao.DefaultCommandLine;
import com.test.domain.BowlingScoreCard;
import com.test.services.BowlingScoreService;
import com.test.services.DefaultBowlingScoreService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BowlingScoreCardApp {

    private final BowlingScoreService bowlingService;

    public BowlingScoreCardApp(BowlingScoreService bowlingService) {
        this.bowlingService = bowlingService;
    }

    public static void main(String[] args) {
        BufferedReader in = new BufferedReader((new InputStreamReader(System.in)));
        CommandLine commandLine = new DefaultCommandLine(in, System.out);

        BowlingScoreService bowlingService = new DefaultBowlingScoreService(commandLine);

        BowlingScoreCardApp bowlingApp = new BowlingScoreCardApp(bowlingService);
        bowlingApp.execute();
    }

    private void execute() {
        BowlingScoreCard scoreCard = new BowlingScoreCard();

        bowlingService.addAllBowlersToScoreCard(scoreCard)
                .startGame()
                .enterScoresForAllFrames(scoreCard)
                .endGame()
                .printWinningPlayerName(scoreCard)
                .printTeamScore(scoreCard);
    }
}
