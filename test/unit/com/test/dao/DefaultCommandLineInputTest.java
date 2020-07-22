package com.test.dao;

import com.test.services.BowlingRules;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultCommandLineInputTest {

    private BufferedReader inputStreamMock;
    private PrintStream outputStreamMock;

    private CommandLine commandLine;

    @Before
    public void setUp() {
        inputStreamMock = mock(BufferedReader.class);
        outputStreamMock = mock(PrintStream.class);
        commandLine = new DefaultCommandLine(inputStreamMock, outputStreamMock);
    }

    @Test
    public void printMessageForStartOfGame() {
        commandLine.displayStartGameMessage();

        verify(outputStreamMock).println(DefaultCommandLine.START_BOWLING_MESSAGE);
    }

    @Test
    public void printAMessageAskingUserToEnterTheNumberOfBowlers() throws IOException {
        when(inputStreamMock.readLine()).thenReturn(Integer.toString(BowlingRules.MAXIMUM_NUMBER_OF_BOWLERS));

        commandLine.readNumberOfBowlers();

        verify(outputStreamMock).println(DefaultCommandLine.NUMBER_OF_BOWLERS_PROMPT);
    }

    @Test
    public void enterTheNumberOfBowlers() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("5");

        assertThat(commandLine.readNumberOfBowlers(), is(5));
    }

    @Test
    public void retryReadingNumberOfBowlersIfNumberIsLessThanOne() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("0", "-1", Integer.toString(BowlingRules.MINIMUM_NUMBER_OF_BOWLERS));

        assertThat(commandLine.readNumberOfBowlers(), is(1));

        verify(outputStreamMock, times(2)).println(DefaultCommandLine.INVALID_INPUT_MESSAGE);
        verify(outputStreamMock, times(2)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_HELP_MESSAGE);
        verify(outputStreamMock, times(3)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_PROMPT);
    }

    @Test
    public void retryReadingNumberOfBowlersIfNumberIsGreaterThanMaxNumber() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("11", "100", "500", Integer.toString(BowlingRules.MAXIMUM_NUMBER_OF_BOWLERS));

        assertThat(commandLine.readNumberOfBowlers(), is(BowlingRules.MAXIMUM_NUMBER_OF_BOWLERS));

        verify(outputStreamMock, times(4)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_PROMPT);
        verify(outputStreamMock, times(3)).println(DefaultCommandLine.INVALID_INPUT_MESSAGE);
        verify(outputStreamMock, times(3)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_HELP_MESSAGE);
    }

    @Test
    public void retryReadingNumberOfBowlersIfInputIsInvalid() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("hgaksgfksg", null, "", "0.00001", Integer.toString(BowlingRules.MINIMUM_NUMBER_OF_BOWLERS));

        int numberOfBowlers = commandLine.readNumberOfBowlers();

        verify(outputStreamMock, times(5)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_PROMPT);
        verify(outputStreamMock, times(4)).println(DefaultCommandLine.INVALID_INPUT_MESSAGE);
        verify(outputStreamMock, times(4)).println(DefaultCommandLine.NUMBER_OF_BOWLERS_HELP_MESSAGE);

        assertThat(numberOfBowlers, is(BowlingRules.MINIMUM_NUMBER_OF_BOWLERS));
    }

    @Test
    public void enterBowlerName() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("Fred");
        String bowlerName = commandLine.readBowlerName();

        verify(outputStreamMock).println(DefaultCommandLine.BOWLER_NAME_PROMPT);

        assertThat(bowlerName, is("Fred"));
    }

    @Test
    public void retryReadingBowlerNameIfInputIsInvalid() throws IOException {
        when(inputStreamMock.readLine()).thenReturn(null, "", "dsd2fd4F5", "a.a", "jh*kki%nn£", "Bob a Job");

        String bowlerName = commandLine.readBowlerName();

        verify(outputStreamMock, times(6)).println(DefaultCommandLine.BOWLER_NAME_PROMPT);
        verify(outputStreamMock, times(5)).println(DefaultCommandLine.BOWLER_NAME_HELP_MESSAGE);

        assertThat(bowlerName, is("Bob a Job"));
    }

    @Test
    public void readScoreForFrame() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("4", "1");

        int[] frame = commandLine.readScoreForFrame(8, "stan");

        verify(outputStreamMock).println(DefaultCommandLine.BOWLER_PROMPT + "stan");
        verify(outputStreamMock).println(DefaultCommandLine.FRAME_PROMPT + "8");
        verify(outputStreamMock).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_1_PROMPT);
        verify(outputStreamMock).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_2_PROMPT);

        assertThat(frame[0], is(4));
        assertThat(frame[1], is(1));
    }

    @Test
    public void enterScoreForLastFrameWhenABonusBallHasBeenAwarded() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("10", "5", "2");

        int[] frame = commandLine.readScoreForFrame(10, "Jack Kirby");

        verify(outputStreamMock).println(DefaultCommandLine.BOWLER_PROMPT + "Jack Kirby");
        verify(outputStreamMock).println(DefaultCommandLine.FRAME_PROMPT + "10");
        verify(outputStreamMock).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_1_PROMPT);
        verify(outputStreamMock).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_2_PROMPT);
        verify(outputStreamMock).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_3_PROMPT);

        assertThat(frame[0], is(10));
        assertThat(frame[1], is(5));
        assertThat(frame[2], is(2));
    }

    @Test
    public void retryReadingScoreForFrameIfInputIsInvalid() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("1000", "-1", "a", "3.5", "6", "11", "b-c", "4", "zzz", "*", "7");

        int[] frame = commandLine.readScoreForFrame(10, "john byrne");

        verify(outputStreamMock, times(1)).println(DefaultCommandLine.BOWLER_PROMPT + "john byrne");
        verify(outputStreamMock, times(1)).println(DefaultCommandLine.FRAME_PROMPT + "10");
        verify(outputStreamMock, times(5)).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_1_PROMPT);
        verify(outputStreamMock, times(3)).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_2_PROMPT);
        verify(outputStreamMock, times(3)).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_3_PROMPT);
        verify(outputStreamMock, times(8)).println(DefaultCommandLine.INVALID_INPUT_MESSAGE);
        verify(outputStreamMock, times(8)).println(DefaultCommandLine.BOWLING_SCORE_HELP_MESSAGE);

        assertThat(frame[0], is(6));
        assertThat(frame[1], is(4));
        assertThat(frame[2], is(7));
    }

    @Test
    public void rejectFrameIfTotalScoreIsGreaterThanTheMaxFrameScore() throws IOException {
        when(inputStreamMock.readLine()).thenReturn("9", "9", "9", "1");

        int[] frame = commandLine.readScoreForFrame(9, "charlie cheat");

        verify(outputStreamMock, times(2)).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_1_PROMPT);
        verify(outputStreamMock, times(2)).println(DefaultCommandLine.ENTER_SCORE_FOR_BALL_2_PROMPT);

        verify(outputStreamMock).println(DefaultCommandLine.INVALID_INPUT_MESSAGE);
        verify(outputStreamMock).println(DefaultCommandLine.TWO_BALL_FRAME_SCORE_HELP_MESSAGE);

        assertThat(frame[0], is(9));
        assertThat(frame[1], is(1));
    }

    @Test
    public void printTeamScore() {
        commandLine.printTeamScore(300);

        verify(outputStreamMock).println(DefaultCommandLine.TEAM_SCORE_MESSAGE + 300);
    }

    @Test
    public void printWinningPlayersName() {
        String winningBowlerName = "Dave the Rave";

        commandLine.printWinnersName(winningBowlerName);

        verify(outputStreamMock).println(DefaultCommandLine.WINNING_PLAYER_MESSAGE + winningBowlerName);
    }

    @Test
    public void printMessageForEndOfGame() {
        commandLine.displayEndGameMessage();

        verify(outputStreamMock).println(DefaultCommandLine.END_BOWLING_MESSAGE);
    }
}
