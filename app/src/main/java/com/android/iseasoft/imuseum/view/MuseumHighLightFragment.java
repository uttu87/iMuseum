package com.android.iseasoft.imuseum.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.iseasoft.imuseum.R;
import com.android.iseasoft.imuseum.adapters.AdapterListener;
import com.android.iseasoft.imuseum.adapters.ArtifactAdapter;
import com.android.iseasoft.imuseum.model.artifact.Artifact;
import com.android.iseasoft.imuseum.model.artifact.ArtifactData;
import com.android.iseasoft.imuseum.present.MuseumAPI;
import com.android.iseasoft.imuseum.utils.Define;
import com.android.iseasoft.imuseum.utils.LogUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MuseumHighLightFragment extends Fragment {


    private static MuseumHighLightFragment mInstance = null;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArtifactAdapter mArtifactAdapter;

    private ArrayList<Artifact> mArtifacts = new ArrayList<Artifact>(0);
    private RecyclerView rvArtifact;

    private ProgressBar mProgressBar;

    public static synchronized MuseumHighLightFragment getInstance(){
        if(mInstance == null){
            mInstance = new MuseumHighLightFragment();
        }
        return  mInstance;
    }

    @SuppressLint("ValidFragment")
    private MuseumHighLightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum_high_light, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArtifacts.clear();
                mArtifactAdapter.notifyDataSetChanged();
                getArtifactData();
            }
        });

        rvArtifact = (RecyclerView) view.findViewById(R.id.rv_museum_high_light);
        mProgressBar = view.findViewById(R.id.museum_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mArtifactAdapter = new ArtifactAdapter(mArtifacts, new AdapterListener() {
            @Override
            public void onItemClickListener(Object object, int pos, RecyclerView.ViewHolder holder) {

                LogUtil.d("@hai.phamvan", "onItemClick position: " + pos);
                //TODO
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mLayoutManager.offsetChildrenVertical(50);
        rvArtifact.setItemViewCacheSize(20);
        rvArtifact.setLayoutManager(mLayoutManager);
        rvArtifact.setItemAnimator(new DefaultItemAnimator());
        rvArtifact.setAdapter(mArtifactAdapter);

        mArtifacts.clear();
        mArtifactAdapter.notifyDataSetChanged();
        getArtifactData();
    }

    private void getArtifactData(){
        MuseumAPI.getArtifactData(getActivity(), Define.ARTIFACTS_DATA_QUERY, new Callback<ArtifactData>() {
            @Override
            public void onResponse(Call<ArtifactData> call, final Response<ArtifactData> response) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                        if(response.isSuccessful()) {
                            LogUtil.d("Artifacts", "Artifact Data loaded from API");                           //mMuseumData = response.body();

                            mArtifacts.addAll(response.body().getData().getArtifacts());
                            mArtifactAdapter.notifyDataSetChanged();
                        }else {
                            int statusCode  = response.code();
                            // handle request errors depending on status code

                            LogUtil.d("MainActivity", "Museum Data loaded from API error code: " + statusCode);
                        }

                    }
                });

            }

            @Override
            public void onFailure(Call<ArtifactData> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Kết nối mạng có vấn đề, vui lòng kiểm tra lại!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}
