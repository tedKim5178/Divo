package com.example.mk.mydramabulary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.R;

import java.util.List;

/**
 * Created by mk on 2017-02-12.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.YoutubeViewHolder> {
    private static final String TAG = YoutubeAdapter.class.getSimpleName();
    private Context mContext;
    EndlessScrollListener endlessScrollListener;
    List<DataInfo> dataInfos;

    CallbackInterface mCallback;

    RatingBar ratingBar_in_youtube_Adapter;
    SpannableStringBuilder ssb;
    String query;

    public interface CallbackInterface{
        void onHandleSelection(int position, List<DataInfo> dataInfos);
    }

    public YoutubeAdapter(Context mContext, List<DataInfo> dataInfos, String query){
        this.mContext = mContext;
        this.dataInfos = dataInfos;
        this.query = query;
        try {
            mCallback = (CallbackInterface) mContext;
            Log.d(TAG, "인터페이스테스트 mCallback " + mCallback);

        }catch (Exception e){
            Log.d(TAG, "인터페이스테스트 mCallback is null");
            Log.d(TAG, "인터페이스테스트" + e.toString());
        }
    }

    public void addDataInfos(List<DataInfo> dataInfos){
        for(int i=0; i<dataInfos.size(); i++){
            this.dataInfos.add(dataInfos.get(i));
        }
    }
    @Override
    public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thumbnail_item_list, parent, false);
        ratingBar_in_youtube_Adapter = (RatingBar) view.findViewById(R.id.ratingBar_in_youtube_adapter);
        // ratinbar 속성 여기서 관리하자.
        LayerDrawable stars = (LayerDrawable)ratingBar_in_youtube_Adapter.getProgressDrawable();
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(mContext, R.color.colorYellow));
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(mContext, R.color.colorWhite));
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(mContext, R.color.colorWhite));

        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YoutubeViewHolder holder, int position) {


        float average_rating = 0;
        int rating_count = dataInfos.get(position).getRating_count();
        float rating_sum = dataInfos.get(position).getRating_sum();
        if(rating_count != 0){
            average_rating = rating_sum/rating_count;
        }
        Log.d(TAG, "리사이클러뷰테스트 onbindviewholder position = " + position);
        Glide.with(mContext).load(Uri.parse("http://img.youtube.com/vi/"+ dataInfos.get(position).getYoutube_id()+"/0.jpg")).override(500,300).into(holder.thumbnail_image);


        String lowerQuery = query.toLowerCase();
        String sentenceWithoutHighlight = dataInfos.get(position).getSentence().toLowerCase();
        // 단어의 시작점과 끝점을 찾자
        int queryLength = lowerQuery.length();
        int startIndex = 0;
        int endIndex = 0;

        startIndex = sentenceWithoutHighlight.indexOf(lowerQuery);
        endIndex = startIndex + queryLength;
        ssb = new SpannableStringBuilder(dataInfos.get(position).getSentence());
        if(startIndex != -1){
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#E32524")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.thumbnail_sentence.setText(ssb);
        holder.ratingBar_in_youtube_adapter.setRating(average_rating);

        if(position == getItemCount() - 3){
            if(endlessScrollListener != null){
                endlessScrollListener.onLoadMore(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "무한리사이클러뷰 테스트 get" + dataInfos.size());
        return dataInfos.size();
    }

    class YoutubeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // 썸네일 이미지를 보여줄 이미지뷰 하나랑
        // 텍스트뷰 하나 필요하다(스크립트 한문장 보여주기)

        ImageView thumbnail_image;
        TextView thumbnail_sentence;
        RatingBar ratingBar_in_youtube_adapter;

        public YoutubeViewHolder(View itemView){
            super(itemView);
            thumbnail_image = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            thumbnail_sentence = (TextView) itemView.findViewById(R.id.thumbnail_sentence);
            ratingBar_in_youtube_adapter = (RatingBar) itemView.findViewById(R.id.ratingBar_in_youtube_adapter);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();

            if(mCallback != null){
                Log.d(TAG, "인터페이스테스트 onClick");
                mCallback.onHandleSelection(position, dataInfos);
            }
        }
    }

    public interface EndlessScrollListener{
        boolean onLoadMore(int position);
    }

    public void refreshDataInfosInAdpater(List<DataInfo> dataInfos, int position){
        this.dataInfos = dataInfos;
        Log.d(TAG, "인터페이스테스트 " + dataInfos.get(position).getRating_count() + " : " + dataInfos.get(position).getRating_sum());
        Log.d(TAG, "리사이클러뷰테스트으 refresh 할때 position 값 : " + position);
        notifyItemChanged(position);
    }

    public void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }


}
