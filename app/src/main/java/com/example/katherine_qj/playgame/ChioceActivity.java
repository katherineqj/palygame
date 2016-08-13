package com.example.katherine_qj.playgame;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;

/**
 * Created by Katherine-qj on 2016/8/4.
 */
public class ChioceActivity extends Activity {
    public static final int CHOICE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    private Button Easy;
    private Button Best;
    private Button choice;
    String path;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_activity);
        InitView();
        Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChioceActivity.this, MainActivity.class);
                intent.putExtra("Level", "esay");
                startActivity(intent);

            }
        });
        Best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChioceActivity.this,MainActivity.class);
                intent.putExtra("Level","best");
                startActivity(intent);
            }
        });
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAbuml();
            }
        });

    }
    public void InitView(){
        Easy = (Button)findViewById(R.id.esay);
        Best = (Button)findViewById(R.id.best);
        choice = (Button)findViewById(R.id.choice);
    }

    public void OpenAbuml(){
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, CHOICE_PHOTO);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOICE_PHOTO :
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                     uri = data.getData();
                    path = getImagePath(uri, null);
                    ContentResolver cr = this.getContentResolver();
                    intent.setDataAndType(uri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        /* 将Bitmap设定到ImageView */
                        /*res_head.setImageBitmap(bitmap);*/
                        Log.e("qwe",path);
                    } catch (FileNotFoundException e) {
                        Log.e("qwe", e.getMessage(), e);
                    }
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        MyApp app = (MyApp)getApplication();
                        //  Intent intent = new Intent(MainActivity.this,ChuLiActivity.class);
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        app.setBitmap(bitmap);
                        Log.e("111","222");
                        app.setBitmap(bitmap);
                        Log.e("qwe",bitmap.toString());
                       // picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ChioceActivity.this, MainActivity.class);
                    intent.putExtra("Level", "esay");
                    startActivity(intent);
                }
                break;

        }

        }

       /* if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            path = getImagePath(uri, null);
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                *//* 将Bitmap设定到ImageView *//*
                *//*res_head.setImageBitmap(bitmap);*//*

                Log.e("qwe",path);
            } catch (FileNotFoundException e) {
                Log.e("qwe", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);*/

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }
}
