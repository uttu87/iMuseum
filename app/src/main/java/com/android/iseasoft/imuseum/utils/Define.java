package com.android.iseasoft.imuseum.utils;

public class Define {

    public static final String API_BASE_URL = "https://api-dev.imuseum.vn/";
    public static final String IMAGE_BASE_URL = "https://d2fj7ltie6ax01.cloudfront.net/";
    public static final String MUSEUM_DATA_QUERY = "{museums(limit:null){_id,name,images{_id,filename},location{place_id,address,lat,lng}}}";
    public static final String ARTIFACTS_DATA_QUERY = "{artifacts(limit:10,skip:0,sortField:\"views\",sortOrder:DSC){_id,name,images{filename,dimensions{width,height}}museum{_id,name}}}";
    //query={museums(limit:null){_id,name,images%20{_id,filename},location{place_id,address,lat,lng}}}
    //https://api-dev.imuseum.vn/graphql?query={artifacts(limit:10,skip:0,sortField:%22views%22,sortOrder:DSC){_id,name,images{filename,dimensions{width,height}}museum{_id,name}}}


    public static final int TIME_TO_MOVE_CAMERA = 3000;
    public static final int ZOOM_VALUE = 15;
    public static final int POLYLINE_COLOR = 0xFF743D;
    public static final String GOOGLE_MAP_API_DIRECTION_KEY = "";
    public static final String GOOGLE_MAP_API_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=SOURCE_LATITUDE,SOURCE_LONGITUDE&destination=DES_LATITUDE,DES_LONGITUDE&key=GOOGLE_MAP_API_KEY";
}
