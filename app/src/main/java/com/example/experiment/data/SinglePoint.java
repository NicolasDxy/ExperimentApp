package com.example.experiment.data;

public class SinglePoint {
    public static final int RADIUS = 50;
    public static final int RECORD_HEIGHT = 70;
    public static final int RECORD_WIDTH = 300;
    public float mPixelX;
    public float mPixelY;
    public int mNum;
    public String mName;
    public int mRecord;

    public SinglePoint(float x, float y, int num, String name) {
        mPixelX = x;
        mPixelY = y;
        mNum = num;
        mName = name;
        mRecord = -1;
    }
}
