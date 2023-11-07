package com.ecuacion.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.ecuacion.finalproject.ui.auth.ActivityLogin;

/** @noinspection deprecation*/
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Get the current orientation setting
        int currentOrientation = getResources().getConfiguration().orientation;
        // Lock the orientation to the current setting
        setRequestedOrientation(currentOrientation);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, ActivityLogin.class));
            finish();
        }, 1000);
    }


}