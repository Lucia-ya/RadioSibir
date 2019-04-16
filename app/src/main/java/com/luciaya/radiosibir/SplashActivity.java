package com.luciaya.radiosibir;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/** Загрузочный экран */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
