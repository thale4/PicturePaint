package com.example.picturepaint.picturepaint;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

public class EditActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener{

    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private SeekBar mBrushSize;
    private SeekBar mBrushTransparency;
    private int mCurrentColor;

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

        mCurrentColor = Color.BLACK;

        mBrushSize = (SeekBar)findViewById(R.id.brushSize);
        mBrushSize.setProgress(25);
        mBrushTransparency = (SeekBar)findViewById(R.id.brushTransparency);
        mBrushTransparency.setProgress(0);
    }

    protected void pickColor(View view)
    {
        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this, this, mCurrentColor);
        colorPickerDialog.show();
    }

    public void colorChanged(int color){
        mCurrentColor = color;
        Button colorButton = (Button) findViewById(R.id.brushColorButton);
        colorButton.setBackgroundColor(color);
    }

    public void showHelp(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(EditActivity.this).create();
        alertDialog.setTitle("HELP");
        alertDialog.setMessage("To choose a color, pick a color on the wheel and then press the color in the center");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
