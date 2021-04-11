package com.example.experiment.data;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.experiment.Activity.ArcExperimentActivity;
import com.example.experiment.Activity.EntranceActivity;
import com.example.experiment.Activity.ExperimentEntranceActivity;
import com.example.experiment.Activity.OverviewExperimentActivity;
import com.example.experiment.R;

public class ExperimentHelper {

    public enum ExperimentType {
        EXPERIMENT_TYPE_ARC,
        EXPERIMENT_TYPE_OVERVIEW
    }

    public enum ExperimentStatus {
        EXPERIMENT_STATUS_PRACTICE,
        EXPERIMENT_STATUS_TEST
    }

    private static ExperimentType experimentType;
    private static ExperimentStatus experimentStatus;
    private static PointsInfo pointsInfo;
    private static Bitmap bitmap;
    private static int leftTestTime;

    public static void setExperimentType(ExperimentType type) {
        experimentType = type;
    }

    public static ExperimentType getExperimentType() {
        return experimentType;
    }

    public static void startExperiment(ExperimentStatus state) {
        experimentStatus = state;
        //如果是第一次就开始计时
        if (experimentStatus == ExperimentStatus.EXPERIMENT_STATUS_TEST && leftTestTime <= 0) {
            leftTestTime = 9;
        }
    }

    public static void init(Resources resources) {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.map).copy(Bitmap.Config.ARGB_8888, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        pointsInfo = new PointsInfo(width, height);
    }

    public static PointsInfo getPointsInfo() {
        return pointsInfo;
    }

    public static void updatePointsInfo() {
        if (leftTestTime < 4) {
            pointsInfo.initPoints(9);
        } else if (leftTestTime >= 4 && leftTestTime < 7) {
            pointsInfo.initPoints(7);
        } else if (leftTestTime >= 7 && leftTestTime < 10) {
            pointsInfo.initPoints(5);
        }
    }

    public static Bitmap getBitmap() {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public static Intent submitRecord(Context context) {
        Intent intent = null;
        if (experimentStatus == ExperimentStatus.EXPERIMENT_STATUS_TEST) {
            if (leftTestTime > 0) {
                if (experimentType == ExperimentType.EXPERIMENT_TYPE_ARC) {
                    intent = new Intent(context, ArcExperimentActivity.class);
                } else {
                    intent = new Intent(context, OverviewExperimentActivity.class);
                }
                leftTestTime--;
            } else {
                //TODO：打开浏览器
                intent = new Intent(context, EntranceActivity.class);
            }
        } else {
            intent = new Intent(context, ExperimentEntranceActivity.class);
        }
        //TODO:记录数据
        Toast.makeText(context, pointsInfo.printPointsRecord(), Toast.LENGTH_SHORT).show();
        return intent;
    }


}
