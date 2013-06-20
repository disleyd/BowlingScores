package com.test.services;

import com.test.dao.CommandLine;
import com.test.domain.Bowler;
import com.test.domain.BowlingScoreCard;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.test.domain.Bowler.bowler;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultBowlingScoreServiceTest {
    private CommandLine commandLineMock;
    private BowlingScoreService bowlingScoreService;

    @Before
    public void setUp() throws Exception {
        commandLineMock = mock(CommandLine.class);

        bowlingScoreService = new DefaultBowlingScoreService(commandLineMock);
    }

    @Test
    public void printStartBowlingMessage() {
        bowlingScoreService.startGame();

        verify(commandLineMock).displayStartGameMessage();
    }

    @Test
    public void createBowlingScoreCard() {
        when(commandLineMock.readNumberOfBowlers()).thenReturn(2);
        when(commandLineMock.readBowlerName()).thenReturn("Fred", "George");

        BowlingScoreCard expectedScoreCard = new BowlingScoreCard().addBowler(bowler("Fred")).addBowler(bowler("George"));

        BowlingScoreCard scoreCard = new BowlingScoreCard();
        bowlingScoreService.addAllBowlersToScoreCard(scoreCard);

        verify(commandLineMock).readNumberOfBowlers();
        verify(commandLineMock, times(2)).readBowlerName();
        assertThat(scoreCard, is(expectedScoreCard));
    }

    @Test
    public void doNotAllowDuplicateBowlerNamesOnScoreCard() {
        when(commandLineMock.readNumberOfBowlers()).thenReturn(2);
        when(commandLineMock.readBowlerName()).thenReturn("Doug", "Doug", "Dougy");

        BowlingScoreCard expectedScoreCard = new BowlingScoreCard().addBowler(bowler("Doug")).addBowler(bowler("Dougy"));

        BowlingScoreCard scoreCard = new BowlingScoreCard();
        bowlingScoreService.addAllBowlersToScoreCard(scoreCard);

        verify(commandLineMock).readNumberOfBowlers();
        verify(commandLineMock, times(1)).duplicateBowlerNameEntered();
        assertThat(scoreCard, is(expectedScoreCard));
    }

    @Test
    public void enterScoreForOneFrame() {
        String bowlerName = "Ringo";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 4, 2);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(6));
    }

    @Test
    public void enterScoresForAllFrames() {
        Bowler steve = Bowler.bowler("Steve");

        BowlingScoreCard scoreCard = createScoreCardForBowler(steve.name());

        when(commandLineMock.readScoreForFrame(1, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(2, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(3, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(4, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(5, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(6, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(7, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(8, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(9, steve.name())).thenReturn(scoreForFrame(10, 0));
        when(commandLineMock.readScoreForFrame(10, steve.name())).thenReturn(scoreForFrame(10, 10, 10));

        bowlingScoreService.enterScoresForAllFrames(scoreCard);

        assertThat(scoreCard.bowlerScore(steve), is(300));
    }

    @Test
    public void enterScoreAndCalculateSpareBonus() {
        String bowlerName = "star man";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 9, 1);
        addScoreToFrameForBowler(2, bowlerName, scoreCard, 5, 0);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(20));
    }

    @Test
    public void enterScoreAndCalculateSpareBonusWhenStrikeAfterSpare() {
        String bowlerName = "star man";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 9, 1);
        addScoreToFrameForBowler(2, bowlerName, scoreCard, 10, 0);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(30));
    }

    @Test
    public void enterScoreAndCalculateStrikeBonus() {
        String bowlerName = "hot diggedy dog";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(2, bowlerName, scoreCard, 2, 1);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(16));
    }

    @Test
    public void enterScoreAndCalculateBonusWhenTwoStrikes() {
        String bowlerName = "t";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(2, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(3, bowlerName, scoreCard, 1, 3);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(21 + 14 + 4));
    }

    @Test
    public void enterScoreAndCalculateBonusWhenThreeStrikes() {
        String bowlerName = "hedwig the owl";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);
        addScoreToFrameForBowler(1, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(2, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(3, bowlerName, scoreCard, 10, 0);
        addScoreToFrameForBowler(4, bowlerName, scoreCard, 8, 1);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(30 + 28 + 19 + 9));
    }

    @Test
    public void enterScoreForLastFrameWhenASpareIsScored() {
        String bowlerName = "whoooooooooooooooooo hoooooooooooooooooo kid";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);

        enterZeroScoresForAllFramesUpTo(9, bowlerName, scoreCard);
        addScoreToFrameForBowler(10, bowlerName, scoreCard, 7, 3, 8);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(7 + 3 + 8));
    }

    @Test
    public void enterScoreForLastFrameWhenAStrikeIsScored() {
        String bowlerName = "The Veteran Cosmic Rocker";
        BowlingScoreCard scoreCard = createScoreCardForBowler(bowlerName);

        enterZeroScoresForAllFramesUpTo(9, bowlerName, scoreCard);
        addScoreToFrameForBowler(10, bowlerName, scoreCard, 10, 10, 10);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName)), is(10 + 10 + 10));
    }

    @Test
    public void enterScoresForMoreThanOneBowler() {
        String bowlerName1 = "the first Bowler";
        String bowlerName2 = "the second Bowler";
        BowlingScoreCard scoreCard = createScoreCardForBowlers(bowlerName1, bowlerName2);

        when(commandLineMock.readScoreForFrame(1, bowlerName1)).thenReturn(scoreForFrame(4, 2));
        when(commandLineMock.readScoreForFrame(1, bowlerName2)).thenReturn(scoreForFrame(7, 2));
        bowlingScoreService.addScoresForFrame(1, scoreCard);

        when(commandLineMock.readScoreForFrame(2, bowlerName1)).thenReturn(scoreForFrame(2, 3));
        when(commandLineMock.readScoreForFrame(2, bowlerName2)).thenReturn(scoreForFrame(8, 1));
        bowlingScoreService.addScoresForFrame(2, scoreCard);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName1)), is(6 + 5));
        assertThat(scoreCard.bowlerScore(bowler(bowlerName2)), is(9 + 9));
    }

    @Test
    public void calculateTeamScoreAtEndOfTheGame() {
        String bowlerName1 = "the first Bowler";
        String bowlerName2 = "the second Bowler";
        String bowlerName3 = "the third Bowler";
        BowlingScoreCard scoreCard = createScoreCardForBowlers(bowlerName1, bowlerName2, bowlerName3);

        for (int frameNumber = 1; frameNumber <= 10; frameNumber++) {
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName1)).thenReturn(scoreForFrame(1, 1));
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName2)).thenReturn(scoreForFrame(2, 2));
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName3)).thenReturn(scoreForFrame(3, 3));
            bowlingScoreService.addScoresForFrame(frameNumber, scoreCard);
        }

        bowlingScoreService.printTeamScore(scoreCard);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName1)), is(20));
        assertThat(scoreCard.bowlerScore(bowler(bowlerName2)), is(40));
        assertThat(scoreCard.bowlerScore(bowler(bowlerName3)), is(60));
        verify(commandLineMock).printTeamScore(20 + 40 + 60);
    }

    @Test
    public void addUpAllScoresAndCalculateTheWinnersName() {
        String bowlerName1 = "the first Bowler";
        String bowlerName2 = "the second Bowler";
        String bowlerName3 = "the third Bowler";
        BowlingScoreCard scoreCard = createScoreCardForBowlers(bowlerName1, bowlerName2, bowlerName3);

        for (int frameNumber = 1; frameNumber <= 10; frameNumber++) {
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName1)).thenReturn(scoreForFrame(1, 1));
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName2)).thenReturn(scoreForFrame(2, 2));
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName3)).thenReturn(scoreForFrame(3, 3));
            bowlingScoreService.addScoresForFrame(frameNumber, scoreCard);
        }

        bowlingScoreService.printWinningPlayerName(scoreCard);

        assertThat(scoreCard.bowlerScore(bowler(bowlerName1)), is(20));
        assertThat(scoreCard.bowlerScore(bowler(bowlerName2)), is(40));
        assertThat(scoreCard.bowlerScore(bowler(bowlerName3)), is(60));
        verify(commandLineMock).printWinnersName(bowlerName3);
    }

    @Test
    public void printEndBowlingMessage() {
        bowlingScoreService.endGame();

        verify(commandLineMock).displayEndGameMessage();
    }

    private void enterZeroScoresForAllFramesUpTo(int numberOdFrames, String bowlerName, BowlingScoreCard scoreCard) {
        for (int frameNumber = 1; frameNumber <= numberOdFrames; frameNumber++) {
            when(commandLineMock.readScoreForFrame(frameNumber, bowlerName)).thenReturn(scoreForFrame());
            bowlingScoreService.addScoresForFrame(frameNumber, scoreCard);
        }
    }

    private int[] scoreForFrame(int... scores) {
        switch (scores.length) {
            case 1: return new int[]{scores[0], 0, 0};
            case 2: return new int[]{scores[0], scores[1], 0};
            case 3: return new int[]{scores[0], scores[1], scores[2]};
            default: return new int[]{0, 0, 0};
        }
    }

    private BowlingScoreCard createScoreCardForBowler(String bowlerName) {
        when(commandLineMock.readNumberOfBowlers()).thenReturn(1);
        when(commandLineMock.readBowlerName()).thenReturn(bowlerName);

        BowlingScoreCard scoreCard = new BowlingScoreCard();
        bowlingScoreService.addAllBowlersToScoreCard(scoreCard);

        return scoreCard;
    }

    private BowlingScoreCard createScoreCardForBowlers(String... bowlerNames) {
        when(commandLineMock.readNumberOfBowlers()).thenReturn(bowlerNames.length);

        if (bowlerNames.length == 1) {
            when(commandLineMock.readBowlerName()).thenReturn(bowlerNames[0]);

        } else {
            when(commandLineMock.readBowlerName()).thenReturn(bowlerNames[0], Arrays.copyOfRange(bowlerNames, 1, bowlerNames.length));
        }

        BowlingScoreCard scoreCard = new BowlingScoreCard();
        bowlingScoreService.addAllBowlersToScoreCard(scoreCard);

        return scoreCard;
    }

    private void addScoreToFrameForBowler(int frameNumber, String bowlerName, BowlingScoreCard scoreCard, int... score) {
        when(commandLineMock.readScoreForFrame(frameNumber, bowlerName)).thenReturn(scoreForFrame(score));
        bowlingScoreService.addScoresForFrame(frameNumber, scoreCard);
    }
}
