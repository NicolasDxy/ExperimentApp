package com.example.experiment.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.experiment.R;

public class EntranceActivity extends Activity implements View.OnClickListener {

    private Button mExpBtn1;
    private Button mExpBtn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_layout);
        init();
    }


    private void init() {
        mExpBtn1 = findViewById(R.id.experimentBtn1);
        mExpBtn1.setOnClickListener(this);
        mExpBtn2 = findViewById(R.id.experimentBtn2);
        mExpBtn2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.experimentBtn1:
                startActivity(new Intent(this, ArcExperimentActivity.class));
                break;
            case R.id.experimentBtn2:
                startActivity(new Intent(this, OverviewExperimentActivity.class));
                break;
        }
    }
}
