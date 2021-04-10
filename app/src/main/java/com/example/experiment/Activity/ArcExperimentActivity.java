package com.example.experiment.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.View.ExperimentImageView;
import com.example.experiment.data.PointsInfo;
import com.example.experiment.data.SinglePoint;

public class ArcExperimentActivity extends Activity {

    private ExperimentImageView mImageView;
    private PointsInfo mPointsInfo;
    private Button mButton;

    public ArcExperimentActivity() {
        mPointsInfo = new PointsInfo();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_layout);
        init();
    }

    private void init() {
        mImageView = findViewById(R.id.experimentView);
        Bitmap bitmap = getPointsBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map).copy(Bitmap.Config.ARGB_8888, true));
        mImageView.setPointsInfo(mPointsInfo);
        mImageView.setShowArc(true);
        mImageView.setImageBitmap(bitmap);
        mButton = findViewById(R.id.submitBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 绘制散点图
     * @param bitmap
     * @return
     */
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
