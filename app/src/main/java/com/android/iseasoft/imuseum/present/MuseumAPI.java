package com.android.iseasoft.imuseum.present;

import android.content.Context;

import com.android.iseasoft.imuseum.model.artifact.ArtifactData;
import com.android.iseasoft.imuseum.model.artifact.Data;
import com.android.iseasoft.imuseum.model.museum.MuseumData;
import com.android.iseasoft.imuseum.utils.Define;

import retrofit2.Callback;

public class MuseumAPI {


    public static MuseumService getSOService() {
        return RetrofitClient.getClient(Define.API_BASE_URL).create(MuseumService.class);
    }


    public  static void getMuseumData(Context ctx, String query, Callback<MuseumData> callback){

        MuseumService service = MuseumAPI.getSOService();

        service.getMuseumData(query).enqueue(callback);
    }

    public  static void getArtifactData(Context ctx, String query, Callback<ArtifactData> callback){

        MuseumService service = MuseumAPI.getSOService();

        service.getArtifactData(query).enqueue(callback);
    }
}
