package com.example.experiment.Activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.experiment.R;
import com.example.experiment.Utils.ReadWriteUtils;

public class ShowRecordActivity extends Activity {


    EditText textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_record_layout);
        textView = findViewById(R.id.textView);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText(ReadWriteUtils.load(this));
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) ShowRecordActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(textView.getText().toString());
                Toast.makeText(ShowRecordActivity.this, "复写信息已复制", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
