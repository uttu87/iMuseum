package com.android.iseasoft.imuseum.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.iseasoft.imuseum.R;
import com.android.iseasoft.imuseum.adapters.InfoWindowListener;
import com.android.iseasoft.imuseum.model.museum.Museum;
import com.android.iseasoft.imuseum.utils.Define;
import com.android.iseasoft.imuseum.utils.LogUtil;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {

    Museum mMuseum;
    InfoWindowListener mListener;

    public FormFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public FormFragment(Museum museum, InfoWindowListener listener) {
        this.mMuseum = museum;
        this.mListener = listener;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.museum_marker_info_window, container, false);

        //ImageView info_widow_close = view.findViewById(R.id.info_window_close);
        ImageView museumImage = view.findViewById(R.id.info_window_image);
        TextView txtMuseumName = view.findViewById(R.id.museum_name_text);
        TextView txtMuseumAddress = view.findViewById(R.id.museum_address_text);
        Button btnDetail = view.findViewById(R.id.btn_detail);
        Button btnDirection = view.findViewById(R.id.btn_direction);


        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Click Detail Button", Toast.LENGTH_SHORT).show();
            }
        });

        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Click Direction Button", Toast.LENGTH_SHORT).show();
                if(mListener != null){
                    mListener.onClickListener(mMuseum);
                }
            }
        });

        if(mMuseum != null) {
            txtMuseumName.setText(mMuseum.getName());
            txtMuseumAddress.setText(mMuseum.getLocation().getAddress());
            final String imageUrl = Define.IMAGE_BASE_URL + mMuseum.getImages().get(0).getFilename();
            LogUtil.d("@hai.phamvan", "Museum Name: " + mMuseum.getName());
            LogUtil.d("@hai.phamvan", "Museum ImageUrl: " + imageUrl);
            Glide.with(getActivity())
                    .load(imageUrl)
                    .into(museumImage);
        }
        return view;
    }

}
