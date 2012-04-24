package main.mastermind;

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

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playing);

        codeDialog = new Dialog(PlayingScreenActivity.this);
        codeDialog.setContentView(R.layout.code_dialog);

        Button done = (Button) codeDialog.findViewById(R.id.doneButton);
        done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
               PlayingScreenActivity.this.codeDialog.dismiss();
            }
        });
        codeDialog.show();
    }
}
