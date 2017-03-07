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

import com.example.mk.mydramabulary.R;

import java.util.ArrayList;

/**
 * Created by mk on 2017-02-19.
 */

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    private static final String TAG = YoutubeAdapter.class.getSimpleName();
    private Context mContext;
    private String query="";
    private ArrayList koreanExampleSentences = new ArrayList();
    private ArrayList englishExampleSentences = new ArrayList();
    SpannableStringBuilder ssb;

    public WordAdapter(Context mContext, ArrayList koreanExampleSentences, ArrayList englishExampleSentences, String query){
        this.mContext = mContext;
        this.koreanExampleSentences = koreanExampleSentences;
        this.englishExampleSentences = englishExampleSentences;
        this.query = query;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.word_item_list, parent, false);
        return new WordAdapter.WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {

        Log.d(TAG, "터짐방지테스트" + position);
        String sentence = englishExampleSentences.get(position).toString() + "\n" + koreanExampleSentences.get(position).toString();
        String lowerSentece = englishExampleSentences.get(position).toString().toLowerCase() + "\n" + koreanExampleSentences.get(position).toString();

        // 단어의 시작점과 끝점을 찾자
        int queryLength = query.length();
        int startIndex = 0;
        int endIndex = 0;

        String lowerQuery = query.toLowerCase();
        startIndex = lowerSentece.indexOf(lowerQuery);
        endIndex = startIndex + queryLength;

        Log.d(TAG, "Span테스트 startIndex : " + startIndex);
        Log.d(TAG, "Span테스트 endIndex : " + endIndex);
        ssb = new SpannableStringBuilder(sentence);
        if(startIndex != -1){
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#E32524")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // ssb를 text에 보여주면 되는거다..! 우선은 그냥 리사이클러뷰로!!
        // 보여줄거는 어차피 예문 두개..! 영어/ 한국어..!
        holder.sentence.setText(ssb);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "터짐방지테스트 size : " +englishExampleSentences.size() + ", "+ koreanExampleSentences.size());
        // 둘중에 작은거까지만..!
        if(englishExampleSentences.size() >= koreanExampleSentences.size()){
            return koreanExampleSentences.size();
        }else{
            return englishExampleSentences.size();
        }

    }


    class WordViewHolder extends RecyclerView.ViewHolder{

        TextView sentence;

        public WordViewHolder(View itemView){
            super(itemView);
            sentence = (TextView) itemView.findViewById(R.id.show_sentence_in_voca_fragment);
        }
    }

}
