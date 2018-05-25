package com.android.iseasoft.imuseum.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParsePolyline {
    public static List<LatLng> getLatLngListFromJson(String json){
        List<LatLng> latLngs = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray routes = jsonObject.getJSONArray("routes");

            for(int i = 0; i < routes.length(); ++i){
                JSONObject eachRoute = routes.getJSONObject(i);
                LogUtil.d("@hai.phamvan", eachRoute.toString());
                if(eachRoute.getJSONObject("overview_polyline") != null){
                    String point = eachRoute.getJSONObject("overview_polyline").getString("points");
                    LogUtil.d("@hai.phamvan", point.toString());
                    latLngs = PolyUtil.decode(point);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return latLngs;
    }
}
