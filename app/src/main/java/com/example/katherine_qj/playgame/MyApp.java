package com.example.katherine_qj.playgame;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by Katherine-qj on 2016/8/7.
 */
public class MyApp extends Application {
    private Bitmap mBitmap;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

}