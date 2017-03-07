package com.example.mk.mydramabulary.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.RecommendationItems;

import java.util.List;

/**
 * Created by mk on 2017-02-23.
 */

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>{

    Context mContext;
    List<RecommendationItems> recommendationItemsList;

    public RecommendationAdapter(Context mContext, List<RecommendationItems> recommendationItemsList){
        this.mContext = mContext;
        this.recommendationItemsList = recommendationItemsList;
    }

    @Override
    public RecommendationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommendation_list_item, parent, false);
        return new RecommendationAdapter.RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendationViewHolder holder, int position) {

        // 뷰 연동
        String title = recommendationItemsList.get(position).getTitle();
        String imageurl = recommendationItemsList.get(position).getImageurl();

        holder.recommanded_title_in_first_place.setText(title);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        holder.recommanded_image_in_first_place.setLayoutParams(params);

        Glide.with(mContext).load(Uri.parse(imageurl)).into(holder.recommanded_image_in_first_place);


    }

    @Override
    public int getItemCount() {
        return recommendationItemsList.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder{

        TextView recommanded_title_in_first_place;
        ImageView recommanded_image_in_first_place;

        public RecommendationViewHolder(View itemView) {
            super(itemView);
            recommanded_title_in_first_place = (TextView) itemView.findViewById(R.id.recommanded_title_in_first_place);
            recommanded_image_in_first_place = (ImageView) itemView.findViewById(R.id.recommanded_image_in_first_place);
        }
    }



}
