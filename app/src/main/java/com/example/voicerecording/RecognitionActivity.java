package com.example.voicerecording;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RecognitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();

        String value = intent.getStringExtra("key");
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        System.out.println("Json: " + jsonObj);
        Log.d("Music", jsonObj.toString());

        System.out.println("MYINTENT: " + value);

        super.onCreate(savedInstanceState);
    }
}
