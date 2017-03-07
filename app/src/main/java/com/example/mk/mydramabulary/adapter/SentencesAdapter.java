package com.example.mk.mydramabulary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.R;

import java.util.List;

/**
 * Created by mk on 2017-02-21.
 */

public class SentencesAdapter extends RecyclerView.Adapter<SentencesAdapter.SentenceViewHolder>{
    private static final String TAG = SentencesAdapter.class.getSimpleName();

    Context mContext;
    List<DataInfo> dataInfos;
    String query;
    SpannableStringBuilder ssb;

    public SentencesAdapter(Context context, List<DataInfo> dataInfos, String query){
        this.mContext = context;
        this.dataInfos = dataInfos;
        this.query = query;
    }

    @Override
    public SentencesAdapter.SentenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sentence_item_list, parent, false);
        Log.d(TAG, "센텐스뷰홀더테스트 ");
        return new SentencesAdapter.SentenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SentencesAdapter.SentenceViewHolder holder, int position) {
//        holder.startTime.setText(String.valueOf(dataInfos.get(position).getStart()));

        Log.d(TAG, "하이라이트테스트 : " + query);
        // 단어의 시작점과 끝점을 찾자
        int queryLength = query.length();
        int startIndex = 0;
        int endIndex = 0;

        String sentenceWithoutHighlight = dataInfos.get(position).getSentence().toLowerCase();

        String lowerQuery = query.toLowerCase();
        startIndex = sentenceWithoutHighlight.indexOf(lowerQuery);
        endIndex = startIndex + queryLength;

        ssb = new SpannableStringBuilder(dataInfos.get(position).getSentence().toLowerCase());
        if(startIndex != -1){
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#E32524")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // ssb를 text에 보여주면 되는거다..! 우선은 그냥 리사이클러뷰로!!
        // 보여줄거는 어차피 예문 두개..! 영어/ 한국어..!
        holder.sentence.setText(ssb);
    }

    @Override
    public int getItemCount() {
        return dataInfos.size();
    }

    public class SentenceViewHolder extends RecyclerView.ViewHolder{

//        TextView startTime;
        TextView sentence;

        public SentenceViewHolder(View itemView) {
            super(itemView);
//            startTime = (TextView) itemView.findViewById(R.id.startTime_in_sentences_adapter);
            sentence = (TextView) itemView.findViewById(R.id.sentence_in_sentences_adapter);
        }

    }
}
