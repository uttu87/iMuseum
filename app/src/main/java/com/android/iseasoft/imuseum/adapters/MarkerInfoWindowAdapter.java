package com.android.iseasoft.imuseum.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.iseasoft.imuseum.R;
import com.android.iseasoft.imuseum.model.museum.Museum;
import com.android.iseasoft.imuseum.utils.Define;
import com.android.iseasoft.imuseum.utils.LogUtil;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
    private Context mContext;
    public MarkerInfoWindowAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }
    @Override
    public View getInfoWindow(Marker marker) {
        LogUtil.d("MarkerInfoWindowsAdapter", "getInfoWindow");
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.museum_marker_info_window, null);

        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        v.setMinimumWidth(display.widthPixels / 2);

        LatLng latLng = marker.getPosition();
        Museum museum = (Museum) marker.getTag();
        ImageView info_widow_close = v.findViewById(R.id.info_window_close);
        ImageView museumImage = v.findViewById(R.id.info_window_image);
        TextView txtMuseumName = v.findViewById(R.id.museum_name_text);
        TextView txtMuseumAddress = v.findViewById(R.id.museum_address_text);
        Button btnDetail = v.findViewById(R.id.btn_detail);
        Button btnDirection = v.findViewById(R.id.btn_direction);

        /*
        info_widow_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();
            }
        });

        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marker.hideInfoWindow();
            }
        });
        */

        txtMuseumName.setText(museum.getName());
        txtMuseumAddress.setText(museum.getLocation().getAddress());
        final String imageUrl = Define.IMAGE_BASE_URL + museum.getImages().get(0).getFilename();
        LogUtil.d("@hai.phamvan", "Museum Name: " + museum.getName());
        LogUtil.d("@hai.phamvan", "Museum ImageUrl: " + imageUrl);
        Glide.with(mContext)
                .load(imageUrl)
                .into(museumImage);
        return v;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        LogUtil.d("MarkerInfoWindowsAdapter", "getInfoContents");
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.museum_marker_info_window, null);

        return v;
    }
}
