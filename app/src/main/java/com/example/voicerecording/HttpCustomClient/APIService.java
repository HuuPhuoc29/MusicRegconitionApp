package com.example.voicerecording.HttpCustomClient;

import model.RecognitionModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface APIService {
    @Multipart
    @POST("http://103.197.184.66/song/recognize")
    static Call<RecognitionModel> MusicRecognize(@Body RequestBody body);
}
