package com.example.experiment.View;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.OverScroller;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.experiment.Interface.ImageChangeListener;
import com.example.experiment.data.ExperimentHelper;
import com.example.experiment.data.PointsInfo;
import com.example.experiment.data.SinglePoint;


public class ExperimentImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    public static final float POSITION_OFFSET = 50;

    private PointsInfo mPointsInfo;

    private boolean mIsOneLoad = true;

    //初始化的比例,也就是最小比例
    private float mInitScale;
    //图片最大比例
    private float mMaxScale;
    //双击能达到的最大比例
    private float mMidScale;

    private Matrix mScaleMatrix;
    //捕获用户多点触控
    private ScaleGestureDetector mScaleGestureDetector;

    //移动
    private GestureDetector gestureDetector;

    //双击
    private boolean isEnlarge = false;//是否放大
    private ValueAnimator mAnimator; //双击缩放动画

    //滚动
    private OverScroller scroller;
    private int mCurrentX, mCurrentY;
    private ValueAnimator translationAnimation; //惯性移动动画

    //单击
    private OnClickListener onClickListener;//单击监听

    private Paint mPointInfoPaint;
    private Paint mPointPositionPaint;

    private ImageChangeListener mListener;

    private boolean mShowArc;

    public ExperimentImageView(Context context) {
        this(context, null);
    }

    public ExperimentImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExperimentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPointInfoPaint = new Paint();
        mPointInfoPaint.setColor(Color.WHITE);
        mPointInfoPaint.setTextSize(100);
        mPointInfoPaint.setStyle(Paint.Style.FILL);

        mPointPositionPaint = new Paint();
        mPointPositionPaint.setColor(Color.YELLOW);
        mPointPositionPaint.setStrokeWidth(10);
        mPointPositionPaint.setStyle(Paint.Style.STROKE);
        //记住，一定要把ScaleType设置成ScaleType.MATRIX，否则无法缩放
        setScaleType(ScaleType.MATRIX);

        scroller = new OverScroller(context);
        scroller.setFriction(0.08f);
        mScaleMatrix = new Matrix();
        //手势缩放
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale(detector);
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                scaleEnd(detector);
            }
        });

        //滑动和双击监听
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, final float distanceX, final float distanceY) {
                //滑动监听
                onTranslationImage(-distanceX, -distanceY);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                velocityX = velocityX / 2;
                velocityY = velocityY / 2;

                //滑动惯性处理
                mCurrentX = (int) e2.getX();
                mCurrentY = (int) e2.getY();

                RectF rectF = getMatrixRectF();
                if (rectF == null) {
                    return false;
                }
                //startX为当前图片左边界的x坐标
                int startX = mCurrentX;
                int startY = mCurrentY;
                int minX = 0, maxX = 0, minY = 0, maxY = 0;
                int vX = Math.round(velocityX);
                int vY = Math.round(velocityY);

                maxX = Math.round(rectF.width());
                maxY = Math.round(rectF.height());

                if (startX != maxX || startY != maxY) {
                    //调用fling方法，然后我们可以通过调用getCurX和getCurY来获得当前的x和y坐标
                    //这个坐标的计算是模拟一个惯性滑动来计算出来的，我们根据这个x和y的变化可以模拟
                    //出图片的惯性滑动
                    scroller.fling(startX, startY, vX, vY, 0, maxX, 0, maxY, maxX, maxY);
                }

                if (translationAnimation != null && translationAnimation.isStarted())
                    translationAnimation.end();

                translationAnimation = ObjectAnimator.ofFloat(0, 1);
                translationAnimation.setDuration(600);
                translationAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (scroller.computeScrollOffset()) {
                            //获得当前的x坐标
                            int newX = scroller.getCurrX();
                            int dx = newX - mCurrentX;
                            mCurrentX = newX;
                            //获得当前的y坐标
                            int newY = scroller.getCurrY();
                            int dy = newY - mCurrentY;
                            mCurrentY = newY;
                            //进行平移操作
                            if (dx != 0 && dy != 0)
                                onTranslationImage(dx, dy);
                        }
                    }
                });
                translationAnimation.start();
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //单击事件
                if (onClickListener != null)
                    onClickListener.onClick(ExperimentImageView.this);
                return true;
            }
        });

    }

    public void setImageChangeListener(ImageChangeListener listener) {
        mListener = listener;
    }

    public void setPointsInfo(PointsInfo pointsInfo) {
        mPointsInfo = pointsInfo;
    }

    public void setShowArc(boolean ifShow) {
        mShowArc = ifShow;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

            //图片原始比例，图片回复原始大小时使用
            mInitScale = scale;
            //图片双击后放大的比例
            mMidScale = mInitScale * 5;
            //手势放大时最大比例
            mMaxScale = mInitScale * 10;

            //设置移动数据,把改变比例后的图片移到中心点
            float translationX = width * 1.0f / 2 - dw / 2;
            float translationY = height * 1.0f / 2 - dh / 2;

            mScaleMatrix.postTranslate(translationX, translationY);
            mScaleMatrix.postScale(mInitScale, mInitScale, width * 1.0f / 2, height * 1.0f / 2);
            setImageMatrix(mScaleMatrix);
            mIsOneLoad = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ExperimentHelper.recordData.touchTime++;
        }
        return mScaleGestureDetector.onTouchEvent(event) |
                gestureDetector.onTouchEvent(event);
    }

    //手势操作（缩放）
    public void scale(ScaleGestureDetector detector) {

        Drawable drawable = getDrawable();
        if (drawable == null)
            return;

        float scale = getScale();
        //获取手势操作的值,scaleFactor>1说明放大，<1则说明缩小
        float scaleFactor = detector.getScaleFactor();
        //获取手势操作后的比例，当放操作后比例在[mInitScale,mMaxScale]区间时允许放大
        mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        setImageMatrix(mScaleMatrix);
        removeBorderAndTranslationCenter();

    }

    //手势操作结束
    public void scaleEnd(ScaleGestureDetector detector) {
        float scale = getScale();
        scale = detector.getScaleFactor() * scale;
        if (scale < mInitScale) {
            scaleAnimation(mInitScale, getWidth() / 2, getHeight() / 2);
        } else if (scale > mMaxScale) {
            scaleAnimation(mMaxScale, getWidth() / 2, getHeight() / 2);
        }
    }

    //手势操作（移动）
    private void onTranslationImage(float dx, float dy) {

        if (getDrawable() == null)
            return;

        RectF rect = getMatrixRectF();

        //图片宽度小于控件宽度时不允许左右移动
        if (rect.width() <= getWidth())
            dx = 0.0f;
        //图片高度小于控件宽度时，不允许上下移动
        if (rect.height() <= getHeight())
            dy = 0.0f;

        //移动距离等于0，那就不需要移动了
        if (dx == 0.0f && dy == 0.0f)
            return;

        mScaleMatrix.postTranslate(dx, dy);
        setImageMatrix(mScaleMatrix);
        //去除移动边界
        removeBorderAndTranslationCenter();
    }

    //消除控件边界和把图片移动到中间
    private void removeBorderAndTranslationCenter() {
        RectF rectF = getMatrixRectF();
        if (rectF == null)
            return;

        int width = getWidth();
        int height = getHeight();
        float widthF = rectF.width();
        float heightF = rectF.height();
        float left = rectF.left;
        float right = rectF.right;
        float top = rectF.top;
        float bottom = rectF.bottom;
        float translationX = 0.0f, translationY = 0.0f;

        if (left > 0) {
            //左边有边界
            if (widthF > width) {
                //图片宽度大于控件宽度，移动到左边贴边
                translationX = -left;
            } else {
                //图片宽度小于控件宽度，移动到中间
                translationX = width * 1.0f / 2f - (widthF * 1.0f / 2f + left);
            }
        } else if (right < width) {
            //右边有边界
            if (widthF > width) {
                //图片宽度大于控件宽度，移动到右边贴边
                translationX = width - right;
            } else {
                //图片宽度小于控件宽度，移动到中间
                translationX = width * 1.0f / 2f - (widthF * 1.0f / 2f + left);
            }
        }

        if (top > 0) {
            //顶部有边界
            if (heightF > height) {
                //图片高度大于控件高度，去除顶部边界
                translationY = -top;
            } else {
                //图片高度小于控件宽度，移动到中间
                translationY = height * 1.0f / 2f - (top + heightF * 1.0f / 2f);
            }
        } else if (bottom < height) {
            //底部有边界
            if (heightF > height) {
                //图片高度大于控件高度，去除顶部边界
                translationY = height - bottom;
            } else {
                //图片高度小于控件宽度，移动到中间
                translationY = height * 1.0f / 2f - (top + heightF * 1.0f / 2f);
            }
        }

        mScaleMatrix.postTranslate(translationX, translationY);
        setImageMatrix(mScaleMatrix);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getScale() >= mMaxScale) {
            showPointNum(canvas);
        }
        if (mShowArc) {
            updatePointsPosition(canvas);
        }
        if (mListener != null) {
            float[][] imagePosition = getImagePoint();
            mListener.onImageChange(imagePosition[0], imagePosition[1]);
        }
    }

    /**
     * 获取当前屏幕左上角和右下角在bitmap的坐标
     *
     * @return
     */
    private float[][] getImagePoint() {
        // 目标点的坐标
        float[] start = new float[2];
        float[] end = new float[2];
        // 创建一个逆矩阵
        Matrix inverseMatrix = new Matrix();
        // 求逆，逆矩阵被赋值
        mScaleMatrix.invert(inverseMatrix);
        // 通过逆矩阵映射得到目标点的值
        inverseMatrix.mapPoints(start, new float[]{0, 0});
        inverseMatrix.mapPoints(end, new float[]{getRight(), getBottom()});
        // 判断dstX, dstY在Bitmap上的位置即可
        return new float[][]{start, end};
    }

    /**
     * 在放大最大的时候绘制数字信息
     *
     * @param canvas
     */
    private void showPointNum(Canvas canvas) {
        float[][] imagePosition = getImagePoint();
        float[] start = imagePosition[0];
        float[] end = imagePosition[1];
        float[] paintPoint = new float[2];
        for (SinglePoint point : mPointsInfo.mPointsArray) {
            if (point.mPixelX >= start[0] && point.mPixelX <= end[0]
                    && point.mPixelY >= start[1] && point.mPixelY <= end[1]) {
                mScaleMatrix.mapPoints(paintPoint, new float[]{point.mPixelX, point.mPixelY});
                canvas.drawText(point.mName + "=" + point.mNum, paintPoint[0] - SinglePoint.RADIUS * getScale() * 3 / 5, paintPoint[1] + 25, mPointInfoPaint);
            }
        }
    }

    private void updatePointsPosition(Canvas canvas) {
        float[][] imagePosition = getImagePoint();
        float[] start = imagePosition[0];
        float[] end = imagePosition[1];
        float[] paintPoint = new float[2];
        for (SinglePoint point : mPointsInfo.mPointsArray) {
            if (!(point.mPixelX >= start[0] && point.mPixelX <= end[0]
                    && point.mPixelY >= start[1] && point.mPixelY <= end[1])) {
                float radius = 0;
                mScaleMatrix.mapPoints(paintPoint, new float[]{point.mPixelX, point.mPixelY});
                if (point.mPixelX < start[0]) {
                    radius = Math.abs(start[0] - point.mPixelX);
                    if (point.mPixelY < start[1]) {
                        radius = (float) Math.sqrt(Math.pow(point.mPixelY - start[1], 2) + Math.pow(radius, 2));
                    } else if (point.mPixelY > end[1]) {
                        radius = (float) Math.sqrt(Math.pow(point.mPixelY - end[1], 2) + Math.pow(radius, 2));
                    }
                } else if (point.mPixelX > end[0]) {
                    radius = Math.abs(end[0] - point.mPixelX);
                    if (point.mPixelY < start[1]) {
                        radius = (float) Math.sqrt(Math.pow(point.mPixelY - start[1], 2) + Math.pow(radius, 2));
                    } else if (point.mPixelY > end[1]) {
                        radius = (float) Math.sqrt(Math.pow(point.mPixelY - end[1], 2) + Math.pow(radius, 2));
                    }
                } else if (point.mPixelY < start[1]) {
                    radius = Math.abs(point.mPixelY - start[1]);
                } else if (point.mPixelY > end[1]) {
                    radius = Math.abs(point.mPixelY - end[1]);
                }
                radius = radius * getScale() + POSITION_OFFSET;
                canvas.drawCircle(paintPoint[0], paintPoint[1], radius, mPointPositionPaint);
            }
        }
    }


    /**
     * 缩放动画
     *
     * @param drowScale 缩放的比例
     * @param x         中心点
     * @param y         中心点
     */
    private void scaleAnimation(final float drowScale, final float x, final float y) {
        if (mAnimator != null && mAnimator.isRunning())
            return;
        mAnimator = ObjectAnimator.ofFloat(getScale(), drowScale);
        mAnimator.setDuration(300);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((float) animation.getAnimatedValue()) / getScale();
                mScaleMatrix.postScale(value, value, x, y);
                setImageMatrix(mScaleMatrix);
                removeBorderAndTranslationCenter();
            }
        });

        mAnimator.start();
    }


    //获取图片宽高以及左右上下边界
    private RectF getMatrixRectF() {

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        RectF rectF = new RectF(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Matrix matrix = getImageMatrix();
        matrix.mapRect(rectF);

        return rectF;
    }


    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    private float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }


    /**
     * 解决和父控件滑动冲突 只要图片边界超过控件边界，返回true
     *
     * @param direction
     * @return true 禁止父控件滑动
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        RectF rect = getMatrixRectF();
        if (rect == null || rect.isEmpty())
            return false;

        if (direction > 0) {
            return rect.right >= getWidth() + 1;
        } else {
            return rect.left <= 0 - 1;
        }

    }

    /**
     * 同楼上
     *
     * @param direction
     * @return
     */
    @Override
    public boolean canScrollVertically(int direction) {
        RectF rect = getMatrixRectF();
        if (rect == null || rect.isEmpty())
            return false;

        if (direction > 0) {
            return rect.bottom >= getHeight() + 1;
        } else {
            return rect.top <= 0 - 1;
        }
    }


}
