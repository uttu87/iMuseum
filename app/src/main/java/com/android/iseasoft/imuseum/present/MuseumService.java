package com.android.iseasoft.imuseum.present;

import com.android.iseasoft.imuseum.model.artifact.ArtifactData;
import com.android.iseasoft.imuseum.model.museum.MuseumData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MuseumService {

    @GET("graphql?")
    Call<MuseumData> getMuseumData(@Query("query") String query);

    @GET("graphql?")
    Call<ArtifactData> getArtifactData(@Query("query") String query);

}