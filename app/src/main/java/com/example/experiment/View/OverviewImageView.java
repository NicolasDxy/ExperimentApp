package com.example.experiment.View;
import com.example.experiment.Interface.ImageChangeListener;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class OverviewImageView extends View implements ImageChangeListener{

    private float[] mStart;
    private float[] mEnd;

    public OverviewImageView(Context context) {
        super(context);
        init();
    }

    public void init() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    //更新概览方框
    public void onImageChange(float[] start, float[] end) {
        mStart = start;
        mEnd = end;
        invalidate();
    }
}
