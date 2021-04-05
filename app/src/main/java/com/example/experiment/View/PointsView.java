package com.example.experiment.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PointsView extends View {

    public PointsView(Context context) {
        super(context);
    }

    public PointsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PointsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawCircle(100, 100, 30, p);
    }
}
