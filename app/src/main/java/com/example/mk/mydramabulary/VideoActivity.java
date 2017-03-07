package com.example.mk.mydramabulary;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.mk.mydramabulary.activities.MainActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.ButterKnife;

/**
 * Created by mk on 2017-02-13.
 */

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private String serverKey = "AIzaSyCG0I2eqR_8nelRBC_3yFDAIh9H_yiIV5Y";
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private String VIDEO_ID = "eFJ2y4Wc2_M";
    private static final String TAG = MainActivity.class.getSimpleName();



    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_video);
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubePlayerView.initialize(serverKey, this);

        // butterknife 를 통해 view들 묶어주자.
        ButterKnife.bind(this);



    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
