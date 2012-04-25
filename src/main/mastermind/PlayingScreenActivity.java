package main.mastermind;

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
    private SharedPreferences settings;
    private static final String PREFS_NAME = "MastermindData";
    private MMGame game;
    private int rows = 1;
    private CodeCircleView[] circles = new CodeCircleView[4];
    private PegCircleView[] pegs = new PegCircleView[4];
    private CodeAdapter codeAdapter;
    private PegAdapter pegAdapter;

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

        GridView codeView = (GridView) findViewById(R.id.codeView);
        codeView.setNumColumns(4);
        codeAdapter = new CodeAdapter();
        codeView.setAdapter(codeAdapter);

        GridView pegView = (GridView) findViewById(R.id.pegView);
        pegView.setNumColumns(2);
        pegAdapter = new PegAdapter();
        pegView.setAdapter(pegAdapter);

        Button submit = (Button) findViewById(R.id.submitButton);
        submit.setOnClickListener(new SubmitListener());

        codeDialog = new Dialog(PlayingScreenActivity.this,
            R.style.Dialog);
        codeDialog.setContentView(R.layout.code_dialog);
        codeDialog.setTitle("Player 1");

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
                System.out.println(code);
                PlayingScreenActivity.this.codeDialog.dismiss();
                developGame();
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

        public void onClick(View v)
        {
            int[] guessData = new int[4];
            for (int i = 0; i < 4; i++) {
                int position = (circles.length - 1) + (i - 3);
                guessData[i] = circles[position].getColor();
            }
            int[] pegData = game.calculateClue(guessData);
            if (game.isWon()) {
                DialogInterface.OnClickListener dialogClickListener =
                    new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            SharedPreferences.Editor editor = settings.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent intent = new Intent(null,
                                MastermindActivity.class);
                            startActivity(intent);
                            break;
                        }

                    }
                };
                AlertDialog.Builder builder = new AlertDialog.
                    Builder(getBaseContext());
                builder.setMessage("Congrats! You guessed it correctly!")
                    .setPositiveButton("Return to Menu", dialogClickListener);
            }
            if (circles.length < 40) {
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
                codeView.smoothScrollToPosition(50);
                codeView.invalidate();

                GridView pegView = (GridView) findViewById(R.id.pegView);
                pegAdapter.notifyDataSetChanged();
                pegView.smoothScrollToPosition(50);
                pegView.invalidate();

            }
        }
    }
}
