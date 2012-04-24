package main.mastermind;

import android.view.MotionEvent;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Canvas;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

// -------------------------------------------------------------------------
/**
 *  This is the custom view class for each "circle" or "node" that the
 *  game uses to display different colors to the code.
 *
 *  @author nkibler7
 *  @version Apr 24, 2012
 */
public class CodeCircleView extends View
{
    private int fill = Color.BLUE;
    private int index = 0;

    // ----------------------------------------------------------
    /**
     * Create a new CodeCircleView object.
     * @param context - the context of this Activity
     * @param attrs - the AttributeSet given to this Activity
     */
    public CodeCircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Changes the color with each increment (click). Cycles through the colors
     * by using a modulus operator.
     */
    private void advanceColor() {
        int[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.RED, Color.rgb(255, 165, 0)};
        fill = colors[(index + 1) % colors.length];
        index++;
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
        canvas.drawCircle(25, 25, 25, outside);
        canvas.drawCircle(25, 25, 21, paint);
    }

    /**
     * The onTouchEvent handler. Only switches the color when an "ACTION_UP"
     * has been performed on the circle.
     * @return true if it was successful, false if there was an error
     * @param event - the MotionEvent object that occurred
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            advanceColor();
            postInvalidate();
        }
        return true;
    }

}
