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
import com.example.experiment.Utils.ReadWriteUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ExperimentHelper {

    public enum ExperimentType {
        EXPERIMENT_TYPE_ARC,
        EXPERIMENT_TYPE_OVERVIEW
    }

    public enum ExperimentStatus {
        EXPERIMENT_STATUS_PRACTICE,
        EXPERIMENT_STATUS_TEST
    }

    public static class TestCount {
        public static int[] POINT_NUM_LIST = new int[]{5, 7, 9};
        public int[] pointTimeList;
        Random random;

        public TestCount() {
            pointTimeList = new int[3];
            random = new Random();
        }

        public void startNewTest() {
            pointTimeList[0] = 3;
            pointTimeList[1] = 3;
            pointTimeList[2] = 3;
        }

        public int leftTime() {
            return pointTimeList[0] + pointTimeList[1] + pointTimeList[2];
        }

        public int getTestPointsNum() {
            if (leftTime() <= 0) {
                return 9;
            }
            int num = 0;
            while (true) {
                int index = random.nextInt(3);
                if (pointTimeList[index] > 0) {
                    num = POINT_NUM_LIST[index];
                    pointTimeList[index]--;
                    break;
                }
            }
            return num;
        }

    }

    public static class RecordData {
        public int pointNum;
        public ExperimentType experimentType;
        public long watchTime;
        public int touchTime;
        public float accuracy;

        public void clear() {
            pointNum = 0;
            experimentType = null;
            watchTime = 0;
            touchTime = 0;
        }

        public JSONObject getRecordData() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("pointsNum", pointNum);
                jsonObject.put("watchTime", watchTime);
                jsonObject.put("touchTime", touchTime);
                jsonObject.put("accuracy", accuracy);
                if (experimentType == ExperimentType.EXPERIMENT_TYPE_ARC) {
                    jsonObject.put("experiment", "Halo");
                } else if (experimentType == ExperimentType.EXPERIMENT_TYPE_OVERVIEW) {
                    jsonObject.put("experiment", "O+D");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }

    private static ExperimentType experimentType;
    private static ExperimentStatus experimentStatus;
    private static PointsInfo pointsInfo;
    private static Bitmap bitmap;
    private static TestCount testCount;
    private static ArrayList<String> recordDataArray;
    public static RecordData recordData;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日_HH:mm:ss");

    public static void setExperimentType(ExperimentType type) {
        experimentType = type;
    }

    public static ExperimentType getExperimentType() {
        return experimentType;
    }

    public static void startExperiment(ExperimentStatus state) {
        experimentStatus = state;
        //如果是第一次就开始计时
        if (experimentStatus == ExperimentStatus.EXPERIMENT_STATUS_TEST && testCount.leftTime() <= 0) {
            testCount.startNewTest();
        }
    }

    public static void init(Resources resources, int mScreenWidth, int mScreenHeight) {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.map).copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createScaledBitmap(bitmap, mScreenWidth, mScreenHeight, true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        pointsInfo = new PointsInfo(width, height);
        testCount = new TestCount();
        recordDataArray = new ArrayList<>();
        recordData = new RecordData();
    }

    public static PointsInfo getPointsInfo() {
        return pointsInfo;
    }

    public static void updatePointsInfo() {
        int pointsNum = testCount.getTestPointsNum();
        recordData.experimentType = experimentType;
        recordData.pointNum = pointsNum;
        recordData.watchTime = System.currentTimeMillis();
        pointsInfo.initPoints(pointsNum);
//        if (leftTestTime < 4) {
//            pointsInfo.initPoints(9);
//        } else if (leftTestTime >= 4 && leftTestTime < 7) {
//            pointsInfo.initPoints(7);
//        } else if (leftTestTime >= 7 && leftTestTime < 10) {
//            pointsInfo.initPoints(5);
//        }

    }

    public static Bitmap getBitmap() {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public static Intent submitRecord(Context context) {
        Intent intent = null;
        if (experimentStatus == ExperimentStatus.EXPERIMENT_STATUS_TEST) {
            if (testCount.leftTime() > 0) {
                if (experimentType == ExperimentType.EXPERIMENT_TYPE_ARC) {
                    intent = new Intent(context, ArcExperimentActivity.class);
                    recordData.watchTime = (System.currentTimeMillis() - recordData.watchTime) / 1000;
                    recordData.accuracy = pointsInfo.getPointsAccuracy();
                    recordDataArray.add(recordData.getRecordData().toString());
                    Toast.makeText(context, recordData.getRecordData().toString(), Toast.LENGTH_SHORT).show();
                    recordData = new RecordData();
                } else {
                    intent = new Intent(context, OverviewExperimentActivity.class);
                    recordData.watchTime = (System.currentTimeMillis() - recordData.watchTime) / 1000;
                    recordData.accuracy = pointsInfo.getPointsAccuracy();
                    recordDataArray.add(recordData.getRecordData().toString());
                    Toast.makeText(context, recordData.getRecordData().toString(), Toast.LENGTH_SHORT).show();
                    recordData = new RecordData();
                }
            } else {
                recordData.watchTime = (System.currentTimeMillis() - recordData.watchTime) / 1000;
                recordData.accuracy = pointsInfo.getPointsAccuracy();
                recordDataArray.add(recordData.getRecordData().toString());
                Toast.makeText(context, recordData.getRecordData().toString(), Toast.LENGTH_SHORT).show();
                recordData = new RecordData();
                recordTestData(context);
                recordDataArray = new ArrayList<>();

                //TODO：打开浏览器
                intent = new Intent(context, EntranceActivity.class);
            }
        } else {
            intent = new Intent(context, ExperimentEntranceActivity.class);
        }
        return intent;
    }

    public static void recordTestData(Context context) {
        Date date = new Date(System.currentTimeMillis());
//        FileUtils.saveFile(recordDataArray.toString(), simpleDateFormat.format(date));
//        ReadWriteUtils.writeData(simpleDateFormat.format(date), recordDataArray.toString());
        ReadWriteUtils.save(simpleDateFormat.format(date), recordDataArray, context);
    }

}
