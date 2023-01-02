package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class welcomePage extends AppCompatActivity {
Handler h= new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(welcomePage.this,Login.class));

            }
        },1000);

    }
}