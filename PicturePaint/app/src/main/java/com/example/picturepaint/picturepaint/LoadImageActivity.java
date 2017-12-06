package com.example.picturepaint.picturepaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class LoadImageActivity extends AppCompatActivity {

    SharedPreferences mPrefFilenames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.image_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> names = new ArrayList<String>();

        mPrefFilenames = getSharedPreferences("filenames", 0);
        int count = mPrefFilenames.getInt("count", 0);
        for(int i = 1; i <= count; i++){
            String filename = mPrefFilenames.getString("file" + i, "");
            names.add(filename);
            //Log.d("test", filename);
        }

        ImageAdapter adapter = new ImageAdapter(names);
        recyclerView.setAdapter(adapter);

        // Display "UP" button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private class ImageHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView mImage;
        private String mName;

        public ImageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_image, parent, false));
            itemView.setOnClickListener(this);
            mImage = (ImageView) itemView.findViewById(R.id.picture);
        }

        public void bind(String filename) {
            Bitmap mBitmap = BitmapFactory.decodeFile(filename);
            mImage.setImageBitmap(mBitmap);
            mName = filename;
        }

        @Override
        public void onClick(View view) {
            //go to edit activity with selected image
            Intent intent = new Intent(getApplicationContext(), EditActivity.class);
            intent.putExtra("filename", mName);
            startActivity(intent);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

        private List<String> mFilenames;

        public ImageAdapter(List<String> filenames) {
            mFilenames = filenames;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new ImageHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            String name = mFilenames.get(position);
            holder.bind(name);
        }

        @Override
        public int getItemCount() {
            return mPrefFilenames.getInt("count", 0);
        }
    }
}
