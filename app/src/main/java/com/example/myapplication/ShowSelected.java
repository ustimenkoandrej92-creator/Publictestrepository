package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ShowSelected extends AppCompatActivity {

    ImageView imageView;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_selected);

        imageView = findViewById(R.id.iv_image);

        String photoPath = getIntent().getStringExtra("PHOTO_PATH");

        if (photoPath != null) {
            // Загружаем фото в ImageView
            loadPhotoIntoImageView(imageView, photoPath);
        }
    }

    // МЕТОД ДЛЯ ЗАГРУЗКИ ФОТО В IMAGEVIEW
    private void loadPhotoIntoImageView(ImageView imageView, String photoPath) {
        File photoFile = new File(photoPath);

        if (photoFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            imageView.setImageBitmap(bitmap);
        }

    }
}