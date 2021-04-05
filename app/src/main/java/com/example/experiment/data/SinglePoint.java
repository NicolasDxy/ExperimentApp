package com.example.experiment.data;

public class SinglePoint {
    public static final int RADIUS = 50;
    public float mPixelX;
    public float mPixelY;
    public int mNum;
    public String mName;

    public SinglePoint(float x, float y, int num, String name) {
        mPixelX = x;
        mPixelY = y;
        mNum = num;
        mName = name;
    }
}
