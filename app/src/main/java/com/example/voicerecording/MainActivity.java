package com.example.voicerecording;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
//import com.squareup.moshi.JsonAdapter;
//import com.squareup.moshi.Moshi;
//import com.squareup.moshi.Types;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.RecognitionModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_PERMISSION_CODE = 101;
    WavAudioRecorder wavAudioRecorder;
    MediaPlayer mediaPlayer;
    ImageView ibRecognition;
    ImageView ibRecord;
    ImageView ibPlay;
//    ImageView ibRegMore;
    TextView tvTime;
//    TextView tvRecordingPath; line 64, 97
    ImageView ivSimpleBg;
    boolean isRecording = false;
    boolean isPlaying = false;
    int seconds = 0;
    String path = null;
    LottieAnimationView lavPlaying;
    int dummySeconds = 0;
    int playableSeconds = 0;
    Handler handler;

    ExecutorService executorService= Executors.newSingleThreadExecutor();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ibRecognition = findViewById(R.id.ib_reg);
        ibRecord = findViewById(R.id.ib_record);
        ibPlay = findViewById(R.id.ib_play);
        tvTime = findViewById(R.id.tv_time);
        ivSimpleBg = findViewById(R.id.iv_simple_bg);
        lavPlaying = findViewById(R.id.lav_playing);
        mediaPlayer = new MediaPlayer();

        ibRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo OkHttpClient để lấy dữ liệu.
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                File file = new File(getRecordingFilePath());

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("audio_file", file.getName(),
                                RequestBody.create(MediaType.parse("audio/wav"), file))
                        .build();
                // Tạo request lên server.
                Request request = new Request.Builder()
                        .url("http://103.197.184.66/song/recognize")
                        .post(requestBody)
                        .build();

                // Thực thi request.
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("Error", String.valueOf(e));
//                        Toast.makeText(getApplicationContext(),"Đã có lỗi xảy ra!!!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
//                        JSONObject jsonObj = null;
//                        try {
//                            jsonObj = new JSONObject(json);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println(jsonObj);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                                intent.putExtra("key", json);
                                MainActivity.this.startActivity(intent);
                            }
                        });
                    }
                });
            }
        });

        ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRecordingPermission()) {
                    if (!isRecording) {
                        isRecording = true;
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                wavAudioRecorder = new WavAudioRecorder(1,44100,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
//                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                wavAudioRecorder.setOutputFile(getRecordingFilePath());
                                path = getRecordingFilePath();
//                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                                wavAudioRecorder.prepare();
                                wavAudioRecorder.start();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivSimpleBg.setVisibility(View.VISIBLE);
                                        lavPlaying.setVisibility(View.GONE);
//                                        tvRecordingPath.setText(getRecordingFilePath());
                                        playableSeconds = 0;
                                        seconds = 0;
                                        dummySeconds = 0;

                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.recording_active));
                                        runTimer();
                                    }
                                });
                            }
                        });
                    } else {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                wavAudioRecorder.stop();
                                wavAudioRecorder.reset();
                                wavAudioRecorder.release();
                                wavAudioRecorder = null;
                                playableSeconds = seconds;
                                dummySeconds = seconds;
                                seconds = 0;
                                isRecording = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivSimpleBg.setVisibility(View.VISIBLE);
                                        lavPlaying.setVisibility(View.GONE);
                                        handler.removeCallbacksAndMessages(null);

                                        ibRecord.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.recording_in_active));
                                    }
                                });
                            }
                        });
                    }
                } else {
                    requestRecordingPermission();
                }
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    if (path != null) {
                        try {
                            mediaPlayer.setDataSource(getRecordingFilePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Recording Present", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    isPlaying = true;
                    ibPlay.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.recording_pause));
                    ivSimpleBg.setVisibility(View.GONE);
                    lavPlaying.setVisibility(View.VISIBLE);
                    runTimer();
                }
                else
                {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer();
                    isPlaying = false;
                    seconds = 0;

                    handler.removeCallbacksAndMessages(null);
                    ivSimpleBg.setVisibility(View.VISIBLE);
                    lavPlaying.setVisibility(View.GONE);
                    ibPlay.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.recording_play));

                }
            }
        });
    }

    private void runTimer()
    {
        handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes=(seconds %3600)/60;
                int secs=seconds%60;
                String time = String.format(Locale.getDefault(), "%82d:%02d", minutes, secs);
                tvTime.setText(time);

                if(isRecording || (isPlaying && playableSeconds!= -1))
                {
                    seconds++;
                    playableSeconds--;

                    if(playableSeconds == -1 && isPlaying)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        isPlaying = false;
                        mediaPlayer = null;
                        mediaPlayer = new MediaPlayer();
                        playableSeconds = dummySeconds;
                        seconds = 0;
                        handler.removeCallbacksAndMessages(null);
                        ivSimpleBg.setVisibility(View.VISIBLE);
                        lavPlaying.setVisibility(View.GONE);
                        ibPlay.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.recording_play));
                        return;
                    }
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void requestRecordingPermission()
    {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public boolean checkRecordingPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
        {
            requestRecordingPermission();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_AUDIO_PERMISSION_CODE)
        {
            if(grantResults.length > 0)
            {
                boolean permissionToRecord=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(permissionToRecord)
                {
                    Toast.makeText(getApplicationContext(), "Permission Given", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getRecordingFilePath()
    {
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File music = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file=new File(music,"testFile"+".wav");

        System.out.println("Path: " + file.getPath());

        return file.getPath();
    }
}