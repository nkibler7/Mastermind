package main.mastermind;

import java.util.Observable;
import android.graphics.Color;

/**
 *  Model for the Mastermind project for ENGE 1104.  Each game represents a
 *  single round with one person making guesses at a provided answer code.
 *
 *  @author  Chris Schweinhart (schwein)
 *  @version 2012.04.17
 */
public class MMGame extends Observable
{
    private int[] codeData;
    private int rightSpotNum;
    private int guessNum = 0;

    // Constants
    public static final int GAME_LENGTH = 10;
    public static final int COMBINATION_LENGTH = 4;
    public static final int NUM_COLORS = 5;

    /**
     * Constructor for the Mastermind Game model
     *
     * @param codeInput  The code to be used for checking guesses
     */
    public MMGame(int[] codeInput)
    {
        codeData = codeInput;
    }

    /**
     * Helper method to calculate the clue given the combination
     *
     * @param guessData - the color ids for the guess
     * @return int[]  The clue for the given guess
     */
    public int[] calculateClue(int[] guessData)
    {
        int[] result = {0, 0};

        for (int index = 0; index < guessData.length; index++)
        {
            if (codeData[index] == guessData[index])
            {
                result[0]++;
            }
        }

        for (int index = 0; index < guessData.length; index++)
        {
            for (int i = 0; i < guessData.length; i++) {
                if (guessData[i] != 0 && codeData[index] == guessData[i]) {
                    result[1]++;
                    guessData[i] = 0;
                    break;
                }
            }
        }
        result[1] -= result[0];
        rightSpotNum = result[0];
        guessNum++;
        return result;
    }

    /**
     * Method to determine if the game has been won
     *
     * @return boolean  Whether or not the game has been won
     */
    public boolean isWon()
    {
        return rightSpotNum == 4;
    }

    // ----------------------------------------------------------
    /**
     * Returns the score based on the number of guesses that have been made.
     * @return the score between 0 and 100
     */
    public int getScore() {
        return 110 - (guessNum * 10);
    }

    // ----------------------------------------------------------
    /**
     * Returns the color of the solution code at the specified position.
     * @param pos - the position (0-3) of the color to find
     * @return codeData[pos] the color of the solution circle
     */
    public int getColorSolutionAt(int pos) {
        return codeData[pos];
    }

    // ----------------------------------------------------------
    /**
     * Determines if the game is lost.
     * @return true if the game is lost, false otherwise
     */
    public boolean isLost() {
        return !isWon() && guessNum >= 10;
    }
}
