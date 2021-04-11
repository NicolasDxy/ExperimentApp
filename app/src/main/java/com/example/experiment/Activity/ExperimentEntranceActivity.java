package com.example.experiment.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.data.ExperimentHelper;

public class ExperimentEntranceActivity extends Activity {
    TextView mTitle;
    Button mPracticeBtn;
    Button mTestBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment_entrance_layout);
        init();
    }

    private void init() {
        //TODO:pointInfo
        mTitle = findViewById(R.id.title);
        mPracticeBtn = findViewById(R.id.practiceBtn);
        mTestBtn = findViewById(R.id.testBtn);
        if (ExperimentHelper.getExperimentType() == ExperimentHelper.ExperimentType.EXPERIMENT_TYPE_ARC) {
            mTitle.setText("Halo组任务");
            ArcClickListener arcListener = new ArcClickListener();
            mPracticeBtn.setOnClickListener(arcListener);
            mTestBtn.setOnClickListener(arcListener);
        } else if (ExperimentHelper.getExperimentType() == ExperimentHelper.ExperimentType.EXPERIMENT_TYPE_OVERVIEW) {
            mTitle.setText("O+D组任务");
            OverviewClickListener overviewClickListener = new OverviewClickListener();
            mPracticeBtn.setOnClickListener(overviewClickListener);
            mTestBtn.setOnClickListener(overviewClickListener);
        }

    }

    /**
     * arc实验监听
     */
    public class ArcClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.practiceBtn:
                    ExperimentHelper.startExperiment(ExperimentHelper.ExperimentStatus.EXPERIMENT_STATUS_PRACTICE);
                    startActivity(new Intent(ExperimentEntranceActivity.this, ArcExperimentActivity.class));
                    finish();
                    break;
                case R.id.testBtn:
                    ExperimentHelper.startExperiment(ExperimentHelper.ExperimentStatus.EXPERIMENT_STATUS_TEST);
                    startActivity(new Intent(ExperimentEntranceActivity.this, ArcExperimentActivity.class));
                    finish();
                    break;
            }
        }
    }

    /**
     * overview实验监听
     */
    public class OverviewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.practiceBtn:
                    ExperimentHelper.startExperiment(ExperimentHelper.ExperimentStatus.EXPERIMENT_STATUS_PRACTICE);
                    startActivity(new Intent(ExperimentEntranceActivity.this, OverviewExperimentActivity.class));
                    finish();
                    break;
                case R.id.testBtn:
                    ExperimentHelper.startExperiment(ExperimentHelper.ExperimentStatus.EXPERIMENT_STATUS_TEST);
                    startActivity(new Intent(ExperimentEntranceActivity.this, OverviewExperimentActivity.class));
                    finish();
                    break;
            }
        }
    }
}
