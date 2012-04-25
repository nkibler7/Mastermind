package main.mastermind;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author nkibler7
 *  @version Apr 24, 2012
 */
public class PegCircleView extends View
{
    private int fill = Color.rgb(166, 166, 166);

    // ----------------------------------------------------------
    /**
     * Create a new PegView object.
     * @param context
     * @param attrs
     */
    public PegCircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setClickable(false);
    }

    // ----------------------------------------------------------
    /**
     * Sets the color of the peg circle to either black or white.
     * @param color
     */
    public void setColor(int color) {
        if (color == Color.WHITE) {
            fill = Color.WHITE;
        }
        else if (color == Color.BLACK) {
            fill = Color.BLACK;
        }
    }


    /**
     * Draws the circle. The color is determined by advanceColor() and the
     * outer circle is always white (for cosmetic purposes).
     * @param canvas - the canvas to draw onto
     */
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(fill);
        Paint outside = new Paint();
        outside.setColor(Color.WHITE);

        paint.setAntiAlias(true);
        outside.setAntiAlias(true);
        canvas.drawCircle(12, 12, 12, outside);
        canvas.drawCircle(12, 12, 10, paint);
    }


}
