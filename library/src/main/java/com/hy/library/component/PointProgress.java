package com.hy.library.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hy.library.R;


public class PointProgress extends View {
    private int pointColor = Color.RED;//点的颜色
    private float pointSize = 30;//点的大小
    private Paint paint;
    private int totalTime = 0;//总时间
    private int currentTime = 0;//当前时间

    public PointProgress(Context context) {
        super(context);
        init(null, 0);
    }

    public PointProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PointProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PointProgress, defStyle, 0);

        pointColor = a.getColor(
                R.styleable.PointProgress_pointColor,
                pointColor);
        pointSize = a.getDimension(
                R.styleable.PointProgress_pointSize,
                pointSize);
        a.recycle();
        paint = new Paint();
        paint.setColor(pointColor);
        paint.setAntiAlias(true);
        totalTime = (int) (pointSize * 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) (getPaddingTop() + getPaddingBottom() + pointSize * 3);
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (getPaddingLeft() + getPaddingRight() + pointSize * 7);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentTime == totalTime) {
            currentTime = 0;
        }
        drawFirstPoint(canvas);
        drawSecondPoint(canvas);
        drawThridPoint(canvas);
        currentTime++;
        invalidate();
    }

    private void drawFirstPoint(Canvas canvas) {
        int alpha = 255 - 255 * 7 / 8 * currentTime / totalTime;
        paint.setAlpha(alpha);
        int width = (int) (getWidth() / 2 - 2 * pointSize + 4 * pointSize * currentTime / totalTime);
        int height;
        int heightSize = (int) (pointSize * 4 / totalTime);
        if (currentTime <= totalTime / 4) {
            height = getHeight() / 2 - heightSize * currentTime;
        } else if (currentTime > totalTime / 4 && currentTime <= totalTime / 2) {
            height = (int) (getHeight() / 2 - pointSize + heightSize * (currentTime - totalTime / 4));
        } else if (currentTime > totalTime / 2 && currentTime <= totalTime * 3 / 4) {
            height = getHeight() / 2 + heightSize * (currentTime - totalTime / 2);
        } else {
            height = (int) (getHeight() / 2 + pointSize - heightSize * (currentTime - totalTime * 3 / 4));
        }
        canvas.drawCircle(width, height, pointSize / 2, paint);
    }

    private void drawSecondPoint(Canvas canvas) {
        int alpha = 255 / 2 + 255 * 7 / 16 * currentTime / totalTime;
        paint.setAlpha(alpha);
        int width = (int) (getWidth() / 2 - 2 * pointSize * currentTime / totalTime);
        int height ;
        int heightSize = (int) (pointSize * 2 / totalTime);
        if (currentTime <= totalTime / 2) {
            height = getHeight() / 2 + heightSize * currentTime;
        } else {
            height = (int) (getHeight() / 2 + pointSize - heightSize * (currentTime - totalTime / 2));
        }
        canvas.drawCircle(width, height, pointSize / 2, paint);
    }

    private void drawThridPoint(Canvas canvas) {
        int alpha = 255 / 8 + 255 * 7 / 16 * currentTime / totalTime;
        paint.setAlpha(alpha);
        int width = (int) (getWidth() / 2 + 2 * pointSize - 2 * pointSize * currentTime / totalTime);
        int height ;
        int heightSize = (int) (pointSize * 2 / totalTime);
        if (currentTime <= totalTime / 2) {
            height = getHeight() / 2 - heightSize * currentTime;
        } else {
            height = (int) (getHeight() / 2 - pointSize + heightSize * (currentTime - totalTime / 2));
        }
        canvas.drawCircle(width, height, pointSize / 2, paint);
    }

    public int getPointColor() {
        return pointColor;
    }

    public void setPointColor(int pointColor) {
        this.pointColor = pointColor;
    }

    public float getPointSize() {
        return pointSize;
    }

    public void setPointSize(float pointSize) {
        this.pointSize = pointSize;
    }
}
