package com.example.experiment.data;

import java.util.ArrayList;
import java.util.Random;

public class PointsInfo {
    public static final String[] POINT_NAME = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I"};

    public ArrayList<SinglePoint> mPointsArray;

    public PointsInfo() {
        mPointsArray = new ArrayList<>();
    }

    public void initPoints(int pointNum, int maxWid, int maxHeight) {
        Random r = new Random();
        for (int i = 0; i < pointNum; i++) {
            mPointsArray.add(new SinglePoint(SinglePoint.RADIUS + r.nextInt(maxWid - SinglePoint.RADIUS), SinglePoint.RADIUS + r.nextInt(maxHeight - SinglePoint.RADIUS), r.nextInt(100), POINT_NAME[i]));
        }
    }
}
