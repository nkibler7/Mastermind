package main.mastermind;

import android.widget.TextView;
import android.graphics.Color;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;

// -------------------------------------------------------------------------
/**
 *  This is the main screen that will handle most of the gameplay for
 *  Mastermind.
 *
 *  @author nkibler7
 *  @version Apr 24, 2012
 */
public class PlayingScreenActivity extends Activity
{
    //Dialog window that asks for the code input.
    private Dialog codeDialog;
    private Dialog finishDialog;
    private SharedPreferences settings;
    private static final String PREFS_NAME = "MastermindData";
    private MMGame game;
    private int rows = 1;
    private CodeCircleView[] circles = new CodeCircleView[4];
    private PegCircleView[] pegs = new PegCircleView[4];
    private CodeAdapter codeAdapter;
    private PegAdapter pegAdapter;
    private int gameNum, maxGames;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing);
        settings = getSharedPreferences(PREFS_NAME, 0);

        for (int i = 0; i < circles.length; i++) {
            circles[i] = new CodeCircleView(this, null);
            circles[i].setLayoutParams(new GridView.LayoutParams(60, 60));
            circles[i].setPadding(0, 25, 0, 25);

            pegs[i] = new PegCircleView(this, null);
            pegs[i].setLayoutParams(new GridView.LayoutParams(30, 30));
            pegs[i].setPadding(0, 5, 0, 5);
        }

        gameNum = settings.getInt("gameNum", 0);
        maxGames = settings.getInt("maxGames", 0);

        GridView codeView = (GridView) findViewById(R.id.codeView);
        codeView.setNumColumns(4);
        codeAdapter = new CodeAdapter();
        codeView.setAdapter(codeAdapter);

        TextView playerTwo = (TextView) findViewById(R.id.playerTwo);
        playerTwo.setText(settings.getString("player2Type", ""));

        TextView playerOnePoints = (TextView) findViewById(R.id.playerOnePoints);
        playerOnePoints.setText("" + settings.getInt("p1score", 0));
        TextView playerTwoPoints = (TextView) findViewById(R.id.playerTwoPoints);
        playerTwoPoints.setText("" + settings.getInt("p2score", 0));

        GridView pegView = (GridView) findViewById(R.id.pegView);
        pegView.setNumColumns(2);
        pegAdapter = new PegAdapter();
        pegView.setAdapter(pegAdapter);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new SubmitListener());

        codeDialog = new Dialog(PlayingScreenActivity.this,
            R.style.Dialog);
        codeDialog.setContentView(R.layout.code_dialog);
        codeDialog.setTitle("Player " + (settings.getInt("playerNum", 1) + 1));

        Button done = (Button) codeDialog.findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                SharedPreferences.Editor editor = settings.edit();
                CodeCircleView code1 =
                    (CodeCircleView) codeDialog.findViewById(R.id.code1);
                CodeCircleView code2 =
                    (CodeCircleView) codeDialog.findViewById(R.id.code2);
                CodeCircleView code3 =
                    (CodeCircleView) codeDialog.findViewById(R.id.code3);
                CodeCircleView code4 =
                    (CodeCircleView) codeDialog.findViewById(R.id.code4);

                String code = code1.getColor() + "" + code2.getColor()
                    + code3.getColor() + code4.getColor();
                editor.putString("secretCode", code);
                editor.commit();
                PlayingScreenActivity.this.codeDialog.dismiss();
                developGame();

                DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            break;
                        }

                    }
                };
                String playerTurn = "";
                if (settings.getInt("playerNum", 0) == 1) {
                    playerTurn = "Player 1's";
                }
                else {
                    playerTurn = settings.getString("player2Type", "") + "'s";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("It's " + playerTurn + " turn!")
                    .setPositiveButton("Ok", dialogClickListener);
                builder.show();
            }
        });
        codeDialog.show();
    }

    // ----------------------------------------------------------
    /**
     * Creates the secret code based on saved information.
     */
    public void developGame() {
        String toParse = settings.getString("secretCode", "");
        if (!toParse.equals("")) {
            int[] code = new int[4];
            for (int i = 0; i < code.length - 1; i++) {
                int toEnd = toParse.indexOf('-', 1);
                code[i] = Integer.parseInt(toParse.substring(0, toEnd));
                toParse = toParse.substring(toEnd);
            }
            code[3] = Integer.parseInt(toParse);
            game = new MMGame(code);

        }
        else {
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            Intent restart = new Intent(this, MastermindActivity.class);
            finish();
            startActivity(restart);
        }
    }

    // ----------------------------------------------------------
    /**
     * Returns the game with the solution in it.
     * @return game - the solution to the game
     */
    public MMGame getGame() {
        return game;
    }

    // ----------------------------------------------------------
    /**
     * Adds a row of circles to the guesses pane.
     */
    public void addRow() {
        rows++;
    }


    private class CodeAdapter extends BaseAdapter {

        public int getCount()
        {
            return circles.length;
        }

        public Object getItem(int position)
        {
            return circles[position];
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            CodeCircleView circleView = circles[position];

            circleView.setVisibility(CodeCircleView.VISIBLE);
            return circleView;
        }
    }

    private class PegAdapter extends BaseAdapter {

        public int getCount()
        {
            return pegs.length;
        }

        public Object getItem(int position)
        {
            return pegs[position];
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            PegCircleView pegView = pegs[position];

            pegView.setVisibility(PegCircleView.VISIBLE);
            return pegView;
        }
    }

    private class SubmitListener implements OnClickListener {

        public void onClick(final View v)
        {
            int[] guessData = new int[4];
            for (int i = 0; i < 4; i++) {
                int position = (circles.length - 1) + (i - 3);
                guessData[i] = circles[position].getColor();
            }
            int[] pegData = game.calculateClue(guessData);
            AlertDialog.Builder builder = null;
            if (game.isWon() || game.isLost()) {
                if (settings.getInt("playerNum", 0) == 0) {
                    gameNum++;
                }
                finishDialog = new Dialog(PlayingScreenActivity.this,
                    R.style.Dialog);
                finishDialog.setContentView(R.layout.finish_dialog);
                String player = settings.getString("player2Type", "");
                int num = settings.getInt("playerNum", 0);
                if (player.equals("Player 2") && num == 0) {
                    player = "Player 1";
                }
                finishDialog.setTitle(player + ": Game "
                    + gameNum + "/" + maxGames);

                if (game.isWon()) {
                    TextView winLostText = (TextView)
                        finishDialog.findViewById(R.id.winLostText);
                    winLostText.setText("You guessed right! The code was:");
                }

                Button lostButton = (Button) finishDialog.findViewById(R.id.lostButton);
                if (gameNum < maxGames || settings.getInt("playerNum", 0) == 0) {
                    lostButton.setText("Play Next Round");
                }
                CodeCircleView code1 =
                    (CodeCircleView) finishDialog.findViewById(R.id.lostCode1);
                CodeCircleView code2 =
                    (CodeCircleView) finishDialog.findViewById(R.id.lostCode2);
                CodeCircleView code3 =
                    (CodeCircleView) finishDialog.findViewById(R.id.lostCode3);
                CodeCircleView code4 =
                    (CodeCircleView) finishDialog.findViewById(R.id.lostCode4);
                code1.changeClickable(false);
                code2.changeClickable(false);
                code3.changeClickable(false);
                code4.changeClickable(false);
                code1.setColor(game.getColorSolutionAt(0));
                code2.setColor(game.getColorSolutionAt(1));
                code3.setColor(game.getColorSolutionAt(2));
                code4.setColor(game.getColorSolutionAt(3));
                lostButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view)
                    {
                        SharedPreferences.Editor editor = settings.edit();
                        @SuppressWarnings("hiding")
                        int player = settings.getInt("playerNum", 0);
                        if (player == 0) {
                            player = 2;
                        }
                        else {
                            player = 1;
                        }
                        int pscore = settings.getInt("p" + player + "score", 0);
                        pscore += game.getScore();
                        editor.putInt("p" + player + "score", pscore);
                        editor.commit();
                        if (gameNum < maxGames || settings.getInt("playerNum", 0) != 1) {

                            editor.putInt("gameNum", gameNum);
                            int playerNum = settings.getInt("playerNum", 0);
                            playerNum = (playerNum + 1) % 2;
                            editor.putInt("playerNum", playerNum);

                            editor.commit();
                            finishDialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                        else {
                            PlayingScreenActivity.this.finishDialog.dismiss();
                            AlertDialog.Builder finish = new AlertDialog.Builder(PlayingScreenActivity.this);
                            String winner = "";
                            if (settings.getInt("p1score", 0) > settings.getInt("p2score", 0)) {
                                winner = "Player 1 has won!";
                            }
                            else if (settings.getInt("p1score", 0) < settings.getInt("p2score", 0)) {
                                if (settings.getString("player2Type", "").equals("Computer")) {
                                    winner = "The computer has won!";
                                }
                                else {
                                    winner = "Player 2 has won!";
                                }
                            }
                            else {
                                winner = "Both players have tied!";
                            }
                            String message = "Player 1: " + settings.getInt("p1score", 0)
                                + ", " + settings.getString("player2Type", "") + ": "
                                + settings.getInt("p2score", 0);

                            finish.setTitle(winner).setMessage(message)
                                .setPositiveButton("Return to Menu", new DialogInterface.OnClickListener() {

                                    public void onClick(
                                        DialogInterface dialog,
                                        int which)
                                    {
                                        finish();
                                    }
                                });
                            editor.clear();
                            editor.commit();
                            finish.show();
                        }
                    }
                });
                finishDialog.show();
            }
            else if (builder == null && circles.length < 40) {
                int oldRows = rows;
                addRow();
                CodeCircleView[] newCircles = new CodeCircleView[rows * 4];
                PegCircleView[] newPegs = new PegCircleView[rows * 4];
                for (int i = 0; i < circles.length; i++) {
                    circles[i].changeClickable(false);
                    newCircles[i] = circles[i];
                    newPegs[i] = pegs[i];
                    if (i >= circles.length - 4) {
                        if (pegData[0] > 0) {
                            newPegs[i].setColor(Color.BLACK);
                            pegData[0]--;
                        }
                        else if (pegData[1] > 0) {
                            newPegs[i].setColor(Color.WHITE);
                            pegData[1]--;
                        }
                    }
                }
                for (int i = oldRows * 4; i < rows * 4; i++) {
                    newCircles[i] = new CodeCircleView(getBaseContext(), null);
                    newCircles[i].setLayoutParams(new GridView.LayoutParams(60, 60));
                    newCircles[i].setPadding(0, 25, 0, 25);

                    newPegs[i] = new PegCircleView(getBaseContext(), null);
                    newPegs[i].setLayoutParams(new GridView.LayoutParams(30, 30));
                    newPegs[i].setPadding(0, 5, 0, 5);
                }
                circles = newCircles;
                pegs = newPegs;

                GridView codeView = (GridView) findViewById(R.id.codeView);
                codeAdapter.notifyDataSetChanged();
                codeView.smoothScrollToPosition(100);
                codeView.invalidate();

                GridView pegView = (GridView) findViewById(R.id.pegView);
                pegAdapter.notifyDataSetChanged();
                pegView.smoothScrollToPosition(100);
                pegView.invalidate();

            }
        }
    }
}
