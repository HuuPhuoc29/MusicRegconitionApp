package com.example.voicerecording;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RecognitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();

        String value   = intent.getStringExtra("key");

        System.out.println("MYINTENTTTT@@@: " + value);

        super.onCreate(savedInstanceState);
    }
}
