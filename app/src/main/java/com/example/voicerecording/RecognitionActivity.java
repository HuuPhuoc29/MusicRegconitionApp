package com.example.voicerecording;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerResultsIntent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import model.RecognitionModel;
import model.ResultListRecognition;

public class RecognitionActivity extends AppCompatActivity {

    TextView tvSingerName;
    TextView tvSongName;
    ImageView imgSinger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognize_layout);
        getSupportActionBar().hide();

        tvSingerName = findViewById(R.id.tv_singer_name);
        tvSongName = findViewById(R.id.tv_song_name);
        imgSinger = findViewById(R.id.iv_avatar);

        Intent intent = getIntent();

        String value = intent.getStringExtra("key");

        try {
            Gson gson = new Gson();

            ResultListRecognition r = gson.fromJson(value, ResultListRecognition.class);

            Log.d("Music", r.getList().get(0).getSinger_name());

            tvSingerName.setText(r.getList().get(0).getSinger_name());

            tvSongName.setText(r.getList().get(0).getSong_name());

            Picasso.get()
                    .load("https://musicregconition.click/media/" + r.getList().get(0).getSinger_avatar_url())
                    .into(imgSinger);

        } catch (Exception e) {
            Log.d("exception", e.toString());
        }




    }
}
