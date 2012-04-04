package main.mastermind;

import android.app.Activity;
import android.os.Bundle;

// -------------------------------------------------------------------------
/**
 *  This is the main activity class for the Mastermind game.
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
    }
}