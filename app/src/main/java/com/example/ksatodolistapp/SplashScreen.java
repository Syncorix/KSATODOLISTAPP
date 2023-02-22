package com.example.ksatodolistapp;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    Handler s = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent z = new Intent(SplashScreen.this , Login.class);
                startActivity(z);
                finish();
            }
        },5000);
    }
}

