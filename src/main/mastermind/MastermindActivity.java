package main.mastermind;

import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.os.Bundle;

// -------------------------------------------------------------------------
/**
 *  This is the main activity class for the Mastermind game.
 *  Testing (chris schweinhart)
 *
 *  @author nkibler7
 *  @version Apr 4, 2012
 */
public class MastermindActivity extends Activity {
    private SharedPreferences settings;
    private static final String PREFS_NAME = "MastermindData";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new StartListener());

        settings = getSharedPreferences(PREFS_NAME, 0);
    }

    private class StartListener implements View.OnClickListener {

        public void onClick(View v)
        {
            SharedPreferences.Editor editor = settings.edit();

            RadioGroup playerGroup = (RadioGroup) findViewById(R.id.playerGroup);
            RadioButton selection = (RadioButton) findViewById(playerGroup.getCheckedRadioButtonId());
            String title = "Computer";
            if (selection.getText().toString().substring(0, 1).equals("1")) {
                title = "Computer";
            }
            else {
                title = "Player 2";
            }
            editor.putString("player2Type", title);

            editor.putInt("playerNum", 0);

            RadioGroup gameGroup = (RadioGroup) findViewById(R.id.gameGroup);
            editor.putInt("gameNum", 0);

            selection = (RadioButton) findViewById(gameGroup.getCheckedRadioButtonId());
            editor.putInt("maxGames", Integer.parseInt(selection.getText().toString()));

            editor.putInt("p1score", 0);
            editor.putInt("p2score", 0);

            editor.commit();
            Intent intent = new Intent(v.getContext(),
                PlayingScreenActivity.class);
            startActivity(intent);
        }

    }
}