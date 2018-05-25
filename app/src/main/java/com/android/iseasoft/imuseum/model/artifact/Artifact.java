package com.android.iseasoft.imuseum.model.artifact;

import java.util.List;

import com.android.iseasoft.imuseum.model.artifact.Image;
import com.android.iseasoft.imuseum.model.artifact.Museum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artifact {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("museum")
    @Expose
    private Museum museum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

}