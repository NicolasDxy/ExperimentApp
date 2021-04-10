package com.example.experiment.View;

import com.example.experiment.Interface.ImageChangeListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;

public class OverviewImageView extends AppCompatImageView implements ImageChangeListener, ViewTreeObserver.OnGlobalLayoutListener {

    private float[] mStart;
    private float[] mEnd;
    private Rect mRect;
    private boolean mIsOneLoad = true;
    private Matrix mScaleMatrix;
    private Paint mPointPositionPaint;

    public OverviewImageView(Context context) {
        this(context, null);
    }

    public OverviewImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverviewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        mStart = new float[2];
        mEnd = new float[2];
        mScaleMatrix = new Matrix();
        mPointPositionPaint = new Paint();
        mPointPositionPaint.setColor(Color.YELLOW);
        mPointPositionPaint.setStrokeWidth(6);
        mPointPositionPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mRect, mPointPositionPaint);
    }

    /**
     * imageView加载完成后调用，获取imageView加载完成后的图片大小
     */
    @Override
    public void onGlobalLayout() {
        if (mIsOneLoad) {

            //得到控件的宽和高
            int width = getWidth();
            int height = getHeight();

            //获取图片,如果没有图片则直接退出
            Drawable d = getDrawable();
            if (d == null)
                return;
            //获取图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            float scale = 1.0f;
            if (dw > width && dh <= height) {
                scale = width * 1.0f / dw;
            }
            if (dw <= width && dh > height) {
                scale = height * 1.0f / dh;
            }
            if ((dw <= width && dh <= height) || (dw >= width && dh >= height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            //设置移动数据,把改变比例后的图片移到中心点
            float translationX = width * 1.0f / 2 - dw / 2;
            float translationY = height * 1.0f / 2 - dh / 2;

            mScaleMatrix.postTranslate(translationX, translationY);
            mScaleMatrix.postScale(scale, scale, width * 1.0f / 2, height * 1.0f / 2);
            setImageMatrix(mScaleMatrix);
            mIsOneLoad = false;
        }
    }


    @Override
    //更新概览方框
    public void onImageChange(float[] start, float[] end) {
        mScaleMatrix.mapPoints(mStart, start);
        mScaleMatrix.mapPoints(mEnd, end);
        mRect = new Rect((int) mStart[0], (int) mStart[1], (int) mEnd[0], (int) mEnd[1]);
        invalidate();
    }
}
