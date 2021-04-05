package com.example.experiment.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.View.ExperimentImageView;
import com.example.experiment.View.PointsView;
import com.example.experiment.data.PointsInfo;
import com.example.experiment.data.SinglePoint;

public class ExperimentActivity extends Activity {

    private ExperimentImageView mImageView;
    private PointsInfo mPointsInfo;

    public ExperimentActivity() {
        mPointsInfo = new PointsInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_layout);
        init();
    }

    private void init() {
        mImageView = (ExperimentImageView) findViewById(R.id.experimentView);
        PointsView pointsView = new PointsView(this);
        pointsView.setBackgroundResource(R.drawable.deadpool);
        Bitmap bitmap = getPointsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map).copy(Bitmap.Config.ARGB_8888, true));
        mImageView.setPointsInfo(mPointsInfo);
        mImageView.setImageBitmap(bitmap);
    }

    private Bitmap getPointsBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        mPointsInfo.initPoints(6, width, height);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        for (SinglePoint point : mPointsInfo.mPointsArray) {
            canvas.drawCircle(point.mPixelX, point.mPixelY, SinglePoint.RADIUS, paint);
        }
        return bitmap;
    }

}
