package com.example.testchart.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ReverseRelativeLayout extends LinearLayout {
    private boolean isReverse = false;
    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }
    public ReverseRelativeLayout(Context context) {
        super(context);
    }

    public ReverseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReverseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isReverse) {
            canvas.scale(-1, 1, canvas.getWidth()/2, canvas.getHeight()/2);
        }
        super.dispatchDraw(canvas);
    }
}
