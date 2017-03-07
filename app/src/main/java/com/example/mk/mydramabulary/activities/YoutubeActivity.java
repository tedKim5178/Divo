package com.example.mk.mydramabulary.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.adapter.SentencesAdapter;
import com.example.mk.mydramabulary.contract.Contract;
import com.example.mk.mydramabulary.dialog.StarDialog;
import com.example.mk.mydramabulary.retrofit.YoutubeService;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by mk on 2017-02-15.
 */

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private static final String TAG = YoutubeActivity.class.getSimpleName();
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private String serverKey = "AIzaSyCG0I2eqR_8nelRBC_3yFDAIh9H_yiIV5Y";
    private int position;
    private List<DataInfo> dataInfos;
    private String query="";

    final Handler handler = new Handler();

    @Bind(R.id.give_star_button_in_youtube_activity)
    Button give_star_button_in_youtube_activtiy;
    @Bind(R.id.recycler_view_in_youtube)
    RecyclerView recycler_view_in_youtube;

//    @Bind(R.id.first_flag_image_button)
//    ImageButton first_flag_image_button;
//    @Bind(R.id.second_flag_image_button)
//    ImageButton second_flag_image_button;
    @Bind(R.id.toolbar_in_youtube_activity)
    Toolbar toolbar;
    @Bind(R.id.show_rating_bar_in_video_activity)
    RatingBar show_rating_bar_in_video_activity;
    @Bind(R.id.show_rating_in_text_in_video_activity)
    TextView show_rating_in_text_in_video_activity;

    Float rate;
    SentencesAdapter sentencesAdapter;

    Bundle bundle;
    int p_id;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube);
        ButterKnife.bind(this);
//        this.bundle = bundle;
        Intent intent = getIntent();
        getDataFromIntent(intent);

        float rating_count = dataInfos.get(position).getRating_count();
        float rating_sum = dataInfos.get(position).getRating_sum();
        float average_rating = 0;

        String str = "";
        if(rating_count != 0){
            average_rating = rating_sum/rating_count;
            show_rating_bar_in_video_activity.setRating(average_rating);
            str = String.format("%.1f", average_rating);
//            show_rating_in_text_in_video_activity.setText(String.valueOf(average_rating));
            show_rating_in_text_in_video_activity.setText(str);
        }else{
            str = String.format("%.1f", average_rating);
            show_rating_in_text_in_video_activity.setText(str);
        }

        toolbar.setSubtitle(dataInfos.get(position).getTitle());
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_arrow_back_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                // 여기서 값 전달 해줘야됨
                Log.d(TAG, "인터페이스테스트 youtubeActivity onClick시 position, rating_sum" + position + " , " + dataInfos.get(position).getRating_sum());
                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataInfos", (Serializable) dataInfos);
                returnIntent.putExtras(bundle);
                returnIntent.putExtra("position", position);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });


        // 제목도 가져왔다고 가정하고 제목을 세팅하자
//        show_title_this_video_in_youtube_activity.append(title);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_in_youtube.setLayoutManager(linearLayoutManager);

        // 불러준 값들을 가지고 쿼리문을 서버로 다시 한번 날려야한다.
        // 사용할건 youtube_id와 startTime이다
        Retrofit client = new Retrofit.Builder().baseUrl(Contract.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        final YoutubeService youtubeService = client.create(YoutubeService.class);
        Call<List<DataInfo>> call = youtubeService.getTextInfo(dataInfos.get(position).getYoutube_id(), dataInfos.get(position).getStart());
        call.enqueue(new Callback<List<DataInfo>>(){
            @Override
            public void onResponse(Call<List<DataInfo>> call, Response<List<DataInfo>> response) {
                if(response.isSuccessful()){
                    List<DataInfo> datainfos = response.body();
                    for(int i=0; i<datainfos.size(); i++){
                        sentencesAdapter = new SentencesAdapter(getApplicationContext(), datainfos, query);
                        recycler_view_in_youtube.setAdapter(sentencesAdapter);
                    }
                    //TODO : 한글인지 아닌지 판별필요
//                    Glide.with(getApplicationContext()).load(R.drawable.america_flag).override(70,70).into(first_flag_image_button);
//                    Glide.with(getApplicationContext()).load(R.drawable.korea_flag).override(70,70).into(second_flag_image_button);

                }else{
                    Log.d(TAG, "유튜브테스트(레트로핏) " + response.message());
                    Log.d(TAG, "유튜브테스트(레트로핏) FAIL");
                }
            }

            @Override
            public void onFailure(Call<List<DataInfo>> call, Throwable t) {
                Log.d(TAG, "[유튜브(contentInfo)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });


        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(serverKey, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {


        youTubePlayer.loadVideo(dataInfos.get(position).getYoutube_id(), dataInfos.get(position).getStart() - 4000);
        youTubePlayer.play();
//        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(youTubePlayer.getCurrentTimeMillis() <= (dataInfos.get(position).getEnd() + 3000)){
                    Log.d(TAG, "유튜브영상테스트조건문  : " + youTubePlayer.getCurrentTimeMillis() + ", " + dataInfos.get(position).getEnd());
                    handler.postDelayed(this,1000);
                }else{
                    Log.d(TAG, "유튜브영상테스트");
                    handler.removeCallbacks(this);
                    youTubePlayer.pause();
                }
            }
        }, 1000);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @OnClick(R.id.give_star_button_in_youtube_activity)
    public void onClick(View view){
        int viewId = view.getId();
        switch (viewId){
            case R.id.give_star_button_in_youtube_activity :
                Log.d(TAG, "버튼테스트 잘 눌린다..!");
                // 버튼이 눌리면 이제 다이얼로그를 띄워주자..!
                float rating_sum = dataInfos.get(position).getRating_sum();
                // 그리고 youtube_id도 전해주자..!
                final StarDialog dialog = new StarDialog(this, p_id, rating_sum);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        rate = dialog.getRate();

                    }
                });
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        // 근데 여기 취소누른건지 확인누른건지 고려해줘야됨.

                        if(dialog.getOkay_Button()){
                            Log.d(TAG, "다이얼로그테스트 onDismiss");
                            rate = dialog.getRate();
                            Log.d(TAG, "인터페이스테스트 기존 rating_sum 값, 추가되는 rate값 : " + dataInfos.get(position).getRating_sum() + " , " + rate);
                            dataInfos.get(position).setRating_sum(dataInfos.get(position).getRating_sum() + rate);
                            dataInfos.get(position).setRating_count(dataInfos.get(position).getRating_count()+1);

                            // 새로 이 화면에 돌아왔으니까 이제 rating_bar 수정해줘야됨
                            float average = dataInfos.get(position).getRating_sum()/dataInfos.get(position).getRating_count();
                            show_rating_bar_in_video_activity.setRating(average);
                            String str = String.format("%.1f", average);
                            show_rating_in_text_in_video_activity.setText(str);
                        }else{

                        }
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    public void getDataFromIntent(Intent intent){
        position = intent.getIntExtra("position",0 );
        query = intent.getStringExtra("query");
        Log.d(TAG, "하이라이트테스트 in Youtube : " + query);
        Bundle bundle = intent.getExtras();
        this.bundle = bundle;
        dataInfos = (List<DataInfo>)bundle.getSerializable("dataInfos");
        p_id = dataInfos.get(position).getId();

        Log.d(TAG, "값전달 position 값 " + position);
        Log.d(TAG, "값전달 " + dataInfos.get(position).getTitle());
    }

    // 생명주기 이용해야함..

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "생명주기테스트 onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "생명주기테스트 onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "생명주기테스트 onDestroy");

//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("dataInfos", (Serializable) dataInfos);
//        intent.putExtras(bundle);
//        intent.putExtra("position", position);
//        setResult(RESULT_OK, intent);

        handler.removeMessages(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataInfos", (Serializable) dataInfos);
        intent.putExtras(bundle);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
