package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecognitionModel {
    @SerializedName("id")
    private String id;

    @SerializedName("song_id")
    private String song_id;
    @SerializedName("song_name")
    public String song_name;

    @SerializedName("singer_id")
    private String singer_id;
    @SerializedName("singer_name")
    public String singer_name;

    @SerializedName("singer_avatar_url")
    public String singer_avatar_url;

    @SerializedName("album_id")
    private String album_id;
    @SerializedName("album_name")
    private String album_name;

    public RecognitionModel(String id, String song_id, String song_name, String singer_id, String singer_name, String singer_avatar_url, String album_id, String album_name) {
        this.id = id;
        this.song_id = song_id;
        this.song_name = song_name;
        this.singer_id = singer_id;
        this.singer_name = singer_name;
        this.singer_avatar_url = singer_avatar_url;
        this.album_id = album_id;
        this.album_name = album_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(String singer_id) {
        this.singer_id = singer_id;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public String getSinger_avatar_url() {
        return singer_avatar_url;
    }

    public void setSinger_avatar_url(String singer_avatar_url) {
        this.singer_avatar_url = singer_avatar_url;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }
}



