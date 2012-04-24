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
    // Instance fields
    private Color[] code;
    private int[] codeData;
    private Color[][] guesses;
    private int[][] clues;
    private int current;

    // Constants
    public static final int GAME_LENGTH = 10;
    public static final int COMBINATION_LENGTH = 4;
    public static final int NUM_COLORS = 5;

    /**
     * Constructor for the Mastermind Game model
     *
     * @param codeInput  The code to be used for checking guesses
     */
    public MMGame(Color[] codeInput)
    {
        code = codeInput;
        codeData = getData(code);
        guesses = new Color[GAME_LENGTH][COMBINATION_LENGTH];
        clues = new int[GAME_LENGTH][2];
        current = 0;
    }

    /**
     * Helper method to determine the data for a given combination
     *
     * @param  combination  The given combination to break down
     * @return int[]        The Color breakdown for the combination (ROYGB)
     */
    private int[] getData(Color[] combination)
    {
        int[] result = new int[NUM_COLORS];

        for(Color compare : combination)
        {
            if (compare.equals(Color.RED))
            {
                result[0]++;
            }
            if (compare.equals(Color.rgb(255, 165, 0)))
            {
                result[1]++;
            }
            if (compare.equals(Color.YELLOW))
            {
                result[2]++;
            }
            if (compare.equals(Color.GREEN))
            {
                result[3]++;
            }
            if (compare.equals(Color.BLUE))
            {
                result[4]++;
            }
        }

        return result;
    }

    /**
     * Method to add a new guess to the game
     *
     * @param combination  The guess to be added and processed
     */
    public void addGuess(Color[] combination)
    {
        guesses[current] = combination;

        clues[current] = calculateClue(combination);

        current++;
    }

    /**
     * Helper method to calculate the clue given the combination
     *
     * @param  guess  The given guess to find clue for
     * @return int[]  The clue for the given guess
     */
    private int[] calculateClue(Color[] guess)
    {
        int[] guessData = getData(guess);

        int[] result = new int[2];

        for (int index = 0; index < guess.length; index++)
        {
            if (code[index].equals(guess[index]))
            {
                result[0]++;
            }
        }

        for (int index = 0; index < guess.length; index++)
        {
            result[1]+= Math.min(codeData[index], guessData[index]);
        }

        result[1] -= result[0];

        return result;
    }

    /**
     * Method to determine if the game has been won
     *
     * @return boolean  Whether or not the game has been won
     */
    public boolean isWon()
    {
        if (current == 0)
        {
            return false;
        }

        return calculateClue(guesses[current--])[0] == COMBINATION_LENGTH;
    }

    /**
     * Method to get the guess matrix for GUI display
     *
     * @return Color[][]  The current set of guesses
     */
    protected Color[][] getGuesses()
    {
        return guesses;
    }

    /**
     * Method to get the clue matrix for GUI display
     *
     * @return int[][]  The current set of clues
     */
    protected int[][] getClues()
    {
        return clues;
    }
}
