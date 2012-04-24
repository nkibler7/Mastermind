package main.mastermind;

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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new StartListener());

    }

    private class StartListener implements View.OnClickListener {

        public void onClick(View v)
        {
            Intent intent = new Intent(v.getContext(),
                PlayingScreenActivity.class);
            startActivity(intent);
        }

    }
}