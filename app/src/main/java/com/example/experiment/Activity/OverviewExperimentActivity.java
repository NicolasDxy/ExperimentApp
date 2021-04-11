package com.example.experiment.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.View.ExperimentImageView;
import com.example.experiment.View.OverviewImageView;
import com.example.experiment.data.ExperimentHelper;
import com.example.experiment.data.SinglePoint;

public class OverviewExperimentActivity extends Activity {

    private ExperimentImageView mImageView;
    private OverviewImageView mOverviewView;
    private Button mButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_image_layout);
        init();
    }

    private void init() {
        ExperimentHelper.updatePointsInfo();

        mImageView = findViewById(R.id.ex2_imageview);
        Bitmap bitmap = getPointsBitmap(ExperimentHelper.getBitmap());
        mImageView.setPointsInfo(ExperimentHelper.getPointsInfo());
        mImageView.setShowArc(false);
        mImageView.setImageBitmap(bitmap);


        mOverviewView = findViewById(R.id.ex2_overview);
        mOverviewView.setImageBitmap(bitmap);
        mImageView.setImageChangeListener(mOverviewView);
        mOverviewView.setClickable(false);

        mButton = findViewById(R.id.recordBtn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverviewExperimentActivity.this, RecordActivity.class));
                finish();
            }
        });
    }

    /**
     * 绘制散点图
     *
     * @param bitmap
     * @return
     */
    private Bitmap getPointsBitmap(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        for (SinglePoint point : ExperimentHelper.getPointsInfo().mPointsArray) {
            canvas.drawCircle(point.mPixelX, point.mPixelY, SinglePoint.RADIUS, paint);
        }
        return bitmap;
    }
}
