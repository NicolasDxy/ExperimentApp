package com.example.experiment.Activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.View.ExperimentImageView;
import com.example.experiment.View.OverviewImageView;

public class OverviewExperimentActivity extends Activity {

    private ExperimentImageView mImageView;
    private OverviewImageView mOverviewView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_image_layout);
    }

    private void init() {
        mImageView = findViewById(R.id.ex2_imageview);
        mOverviewView = findViewById(R.id.ex2_overview);
        mImageView.setImageChangeListener(mOverviewView);
    }


}
