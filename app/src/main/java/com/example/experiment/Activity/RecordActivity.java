package com.example.experiment.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.experiment.DensityUtils;
import com.example.experiment.R;
import com.example.experiment.data.ExperimentHelper;
import com.example.experiment.data.SinglePoint;

import java.util.ArrayList;

public class RecordActivity extends Activity implements View.OnClickListener {

    ArrayList<EditText> mRecordViewList;
    LinearLayout recordListLayout;
    Button mRecordBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_view_layout);
        init();
    }

    private void init() {
        mRecordViewList = new ArrayList<>();
        recordListLayout = findViewById(R.id.recordLayout);
        mRecordBtn = findViewById(R.id.submitBtn);
        mRecordBtn.setOnClickListener(this);
        int recordWidth = DensityUtils.dip2px(this, SinglePoint.RECORD_WIDTH);
        int recordHeight = DensityUtils.dip2px(this, SinglePoint.RECORD_HEIGHT);

        for (SinglePoint point : ExperimentHelper.getPointsInfo().mPointsArray) {
            LinearLayout recordView = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(recordWidth, recordHeight);
            layoutParams.gravity = Gravity.CENTER;
            recordView.setLayoutParams(layoutParams);
            recordView.setOrientation(LinearLayout.HORIZONTAL);

            TextView textView = new TextView(this);
            textView.setText(point.mName + " = ");
            recordView.addView(textView, new LinearLayout.LayoutParams(recordWidth / 2, recordHeight));

            EditText editText = new EditText(this);
            recordView.addView(editText, new LinearLayout.LayoutParams(recordWidth / 2, recordHeight));

            recordListLayout.addView(recordView);
            mRecordViewList.add(editText);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitBtn:
                for (SinglePoint point : ExperimentHelper.getPointsInfo().mPointsArray) {
                    int index = ExperimentHelper.getPointsInfo().mPointsArray.indexOf(point);
                    String string = mRecordViewList.get(index).getText().toString();
                    Integer record = -1;
                    if (!TextUtils.isEmpty(string)) {
                        record = Integer.parseInt(string);
                    }
                    point.mRecord = record;
                }
                Intent intent = ExperimentHelper.submitRecord(this);
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
        }
    }
}
