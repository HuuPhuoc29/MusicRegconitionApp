package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultListRecognition {
    @SerializedName("data")
    private List<RecognitionModel> list;

    public ResultListRecognition(List<RecognitionModel> list) {
        this.list = list;
    }

    public List<RecognitionModel> getList() {
        return list;
    }

    public void setList(List<RecognitionModel> list) {
        this.list = list;
    }
}
