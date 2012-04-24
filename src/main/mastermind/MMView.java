package main.mastermind;

import android.graphics.Canvas;
import java.util.Observable;
import java.util.Observer;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

// -------------------------------------------------------------------------
/**
 *  This is the view class that will show the GUI for the Mastermind game.
 *
 *  @author nkibler7
 *  @version Apr 24, 2012
 */
public class MMView extends View
{

    private MMGame game;

    // ----------------------------------------------------------
    /**
     * Creates a new MMView.
     *
     * A constructor with these arguments is required for any view that will be
     * added to a layout XML file. Just pass the arguments along to the
     * superclass constructor, and then perform any other initialization after
     * that if you need to.
     *
     * @param context the Context that the view is running in
     * @param attrs the attributes of the XML tag that is inflating the view
     */
    public MMView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param newGame
     */
    public void setGame(MMGame newGame) {
        game = newGame;
        game.addObserver(new MMViewObserver());
    }

    public void onDraw(Canvas canvas) {
        //TODO implement this
    }

    private class MMViewObserver implements Observer
    {
        // The invalidate() method is used to force a view to be repainted
        // at the earliest opportunity (which in most cases is essentially
        // immediately, but may not always be). Note that this is a method
        // on the View class, not the Observer.
        public void update(Observable observable, Object data) {
            // TODO Auto-generated method stub
            invalidate();
        }
    }

}
