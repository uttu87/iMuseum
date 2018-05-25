package com.android.iseasoft.imuseum.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.iseasoft.imuseum.R;
import com.android.iseasoft.imuseum.model.artifact.Artifact;
import com.android.iseasoft.imuseum.model.artifact.Dimensions;
import com.android.iseasoft.imuseum.utils.Define;
import com.android.iseasoft.imuseum.utils.LogUtil;
import com.bumptech.glide.Glide;

import java.util.List;

public class ArtifactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Artifact> mItems;
    private Context mContext;
    private AdapterListener mItemListener;
    public ArtifactAdapter(List<Artifact> items, AdapterListener postItemListener){
        //this.mContext = context;
        this.mItems = items;
        this.mItemListener = postItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.museum_item, null);

        int width = parent.getMeasuredWidth() / 2;
        v.setMinimumWidth(width);

        ArtifactViewHolder artifactViewHolder = new ArtifactViewHolder(v);
        return artifactViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ArtifactViewHolder artifactViewHolder = (ArtifactViewHolder) holder;
        final Artifact artifact = mItems.get(position);
        artifactViewHolder.tvTitle.setText(artifact.getName());
        //artifactViewHolder.tvDesc.setText(post.getPostDesc());

        final String imageURL = Define.IMAGE_BASE_URL + artifact.getImages().get(0).getFilename();
        LogUtil.d("@hai.phamvan", "Artifact Name: " + artifact.getName());
        LogUtil.d("@hai.phamvan", "Artifact ImageURL: " + imageURL);

        final Dimensions dimensions = (Dimensions) artifact.getImages().get(0).getDimensions();

        Glide.with(mContext)
                .load(imageURL)
                .into(artifactViewHolder.imgThumb);

        //DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        int width = artifactViewHolder.itemView.getMinimumWidth();
        int height = dimensions.getHeight() * width / dimensions.getWidth();

        artifactViewHolder.imgThumb.getLayoutParams().width = width;
        artifactViewHolder.imgThumb.getLayoutParams().height = height;

        /*
        artifactViewHolder.rlMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemListener != null){
                    mItemListener.onItemClickListener(artifact, position, artifactViewHolder);
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ArtifactViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rlMuseum;
        ImageView imgThumb;
        TextView tvTitle;
        TextView tvDesc;


        public ArtifactViewHolder(View itemView) {
            super(itemView);

            LogUtil.d("@hai.phamvan", "ArtifactViewHolder");
            rlMuseum = (RelativeLayout)itemView.findViewById(R.id.rv_museum_high_light);
            imgThumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);

        }
    }
}
