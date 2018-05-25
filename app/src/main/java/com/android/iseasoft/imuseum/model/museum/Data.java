package com.android.iseasoft.imuseum.model.museum;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("museums")
    @Expose
    private List<Museum> museums = null;

    public List<Museum> getMuseums() {
        return museums;
    }

    public void setMuseums(List<Museum> museums) {
        this.museums = museums;
    }
}