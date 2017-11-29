package com.example.picturepaint.picturepaint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

public class EditActivity extends AppCompatActivity {

    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private SeekBar brushSize;
    private SeekBar brushTransparency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mImageView = (ImageView)findViewById(R.id.image);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra("filename");
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mImageView.setImageBitmap(bitmap);


        // Display "UP" button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
