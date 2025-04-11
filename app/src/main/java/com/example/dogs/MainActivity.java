package com.example.dogs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView imageViewDog;
    private Button buttonNext;
    private ProgressBar progressBar;
    private MainViewModel viewModel;
    private static final String TAG = "TagMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.loadDogImage();
        // подписываемся на ошибки
        // и если произошла ошибка, то покажем Toast
        viewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError) {
                    Toast.makeText(MainActivity.this,
                            R.string.error_loading,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // подписываемся на изменения переменной
        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // подписываемся на объект getDogImage
        viewModel.getDogImage().observe(this, new Observer<DogImage>() {
            @Override
            public void onChanged(DogImage dogImage) {
                // получаем экземплар класса
                Glide.with(MainActivity.this)
                        .load(dogImage.getMessage()) // получаем адрес картинки
                        .into(imageViewDog); // указаываем куда нелбходимо направить
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.loadDogImage();
            }
        });

    }

    private void initViews() {
        imageViewDog = findViewById(R.id.imageViewDog);
        buttonNext = findViewById(R.id.buttonNext);
        progressBar = findViewById(R.id.progressBar);
    }


}