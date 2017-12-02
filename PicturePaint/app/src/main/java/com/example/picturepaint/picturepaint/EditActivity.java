package com.example.picturepaint.picturepaint;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {

    private DrawingView mImageView;
    private String mCurrentPhotoPath;
    private SeekBar mBrushSize;
    private SeekBar mBrushTransparency;
    private int mCurrentColor;
    private Bitmap mBitmap;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mImageView = (DrawingView)findViewById(R.id.image);

        Intent intent = getIntent();
        mCurrentPhotoPath = intent.getStringExtra("filename");
        mBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        mImageView.setImageBitmap(mBitmap);

        // Display "UP" button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mCurrentColor = Color.BLACK;
        mImageView.setColor(mCurrentColor);
        mImageView.setBitmap(mBitmap);


        mBrushSize = (SeekBar)findViewById(R.id.brushSize);
        mBrushSize.setProgress(25);
        mBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
              @Override
              public void onStopTrackingTouch(SeekBar seekBar) {
                  mImageView.setSize(seekBar.getProgress());
              }

              @Override
              public void onStartTrackingTouch(SeekBar seekBar) {
              }
              @Override
              public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              }
          });

        mImageView.setSize(mBrushSize.getProgress());

        mBrushTransparency = (SeekBar)findViewById(R.id.brushTransparency);
        mBrushTransparency.setProgress(0);
        mBrushTransparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mImageView.setTransparency(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
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
        mImageView.setColor(color);
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

    public void saveImage(View view) {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing?");

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }

        final Context context = this.getApplicationContext();

        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                mImageView.setDrawingCacheEnabled(true);
                Bitmap bitmap = mImageView.getDrawingCache();
                saveImageToInternalStorage(bitmap);

                //tell the user it saved
                Toast savedToast = Toast.makeText(getApplicationContext(), "Drawing saved!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    // Creates image file name with a simple time stamp
    protected File createImageFile() throws IOException {
        File pictureDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_.jpg";
        File newfile = new File(pictureDirectory, imageFileName);
        mFile = newfile;
        mCurrentPhotoPath = newfile.getAbsolutePath();
        Log.d("test", mCurrentPhotoPath);

        return newfile;
    }


    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
            FileOutputStream fos = new FileOutputStream(mFile);

            // Use the compress method on the Bitmap object to write image to the OutputStream
            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d("test", "saved image at: "+mFile.getPath());
            return true;
        } catch (Exception e) {
            Log.d("test", e.getMessage());
            return false;
        }
    }
}
