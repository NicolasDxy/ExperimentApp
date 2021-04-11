package com.example.experiment.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.experiment.Utils.DensityUtils;
import com.example.experiment.R;
import com.example.experiment.Utils.ReadWriteUtils;
import com.example.experiment.data.ExperimentHelper;

public class EntranceActivity extends Activity implements View.OnClickListener {

    private View title;
    private Button mExpBtn1;
    private Button mExpBtn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);
        init();
    }


    private void init() {
        ActivityCompat.requestPermissions(EntranceActivity.this, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
        mExpBtn1 = findViewById(R.id.experimentBtn1);
        mExpBtn1.setOnClickListener(this);
        mExpBtn2 = findViewById(R.id.experimentBtn2);
        mExpBtn2.setOnClickListener(this);
        ExperimentHelper.init(getResources(), DensityUtils.dip2px(this, 380), DensityUtils.dip2px(this, 740));
        title = findViewById(R.id.title);
        title.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.experimentBtn1:
                ExperimentHelper.setExperimentType(ExperimentHelper.ExperimentType.EXPERIMENT_TYPE_ARC);
                startActivity(new Intent(this, ExperimentEntranceActivity.class));
                finish();
                break;
            case R.id.experimentBtn2:
                ExperimentHelper.setExperimentType(ExperimentHelper.ExperimentType.EXPERIMENT_TYPE_OVERVIEW);
                startActivity(new Intent(this, ExperimentEntranceActivity.class));
                finish();
                break;
            case R.id.title:
                Toast.makeText(this, ReadWriteUtils.load(this), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ShowRecordActivity.class));
                break;
        }
    }
}
