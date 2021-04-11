package com.example.experiment.data;

import java.util.ArrayList;
import java.util.Random;

public class PointsInfo {
    public static final String[] POINT_NAME = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"};

    public ArrayList<SinglePoint> mPointsArray;
    private int maxWid;
    private int maxHeight;

    public PointsInfo(int wid, int height) {
        mPointsArray = new ArrayList<>();
        maxWid = wid;
        maxHeight = height;
    }

    public void initPoints(int pointNum) {
        mPointsArray.clear();
        Random r = new Random();
        for (int i = 0; i < pointNum; i++) {
            mPointsArray.add(new SinglePoint(SinglePoint.RADIUS * 2 + r.nextInt(maxWid - SinglePoint.RADIUS * 2), SinglePoint.RADIUS * 2 + r.nextInt(maxHeight - SinglePoint.RADIUS * 2), r.nextInt(10), POINT_NAME[i]));
        }
    }

    public float getPointsAccuracy() {
        int correct = 0;
        for (SinglePoint point : mPointsArray) {
            if (point.mNum == point.mRecord) {
                correct++;
            }
        }
        return ((float)correct / (float)mPointsArray.size());
    }
}
