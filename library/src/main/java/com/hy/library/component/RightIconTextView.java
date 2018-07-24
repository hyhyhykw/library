package com.hy.library.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created time : 2018/6/5 15:48.
 *
 * @author HY
 */
public class RightIconTextView extends AppCompatTextView {
    public RightIconTextView(Context context) {
        super(context);
    }

    public RightIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void drawPic(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables[0] != null) {
            setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

            drawPic(canvas, drawables[0], Gravity.START);
        } else {
            if (drawables[1] != null) {
                setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);

                drawPic(canvas, drawables[1], Gravity.TOP);
                return;
            }

            if (drawables[2] != null) {
                setGravity(Gravity.END | Gravity.CENTER_VERTICAL);

                drawPic(canvas, drawables[2], Gravity.END);
                return;
            }

            if (drawables[3] != null) {

                setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                drawPic(canvas, drawables[3], Gravity.BOTTOM);
            }
        }

    }

    void drawPic(Canvas canvas, Drawable drawable, int gravity) {
        byte direction = -1;
        int drawablePadding = getCompoundDrawablePadding();
        switch (gravity) {
            case Gravity.START:
                direction = 1;
            case Gravity.END:
                float width = getPaint().measureText(getText().toString()) + drawable.getIntrinsicWidth() + drawablePadding +
                        getPaddingLeft() + getPaddingRight();
                canvas.translate(direction * (getWidth() - width) / 2F, 0F);
                break;
            case Gravity.TOP:
                direction = 1;
            case Gravity.BOTTOM:
                Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
                float height = fontMetrics.descent - fontMetrics.ascent + drawable.getIntrinsicHeight() +
                        drawablePadding + getPaddingTop() + getPaddingBottom();
                canvas.translate(0F, direction * (getHeight() - height) / 2F);
                break;
            default:
        }
    }

    protected void onDraw(Canvas canvas) {
        drawPic(canvas);
        super.onDraw(canvas);
    }
}
