package com.android.iseasoft.imuseum.model.museum;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MuseumData {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}