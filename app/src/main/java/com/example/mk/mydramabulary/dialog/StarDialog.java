package com.example.mk.mydramabulary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.contract.Contract;
import com.example.mk.mydramabulary.retrofit.YoutubeService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mk on 2017-02-20.
 */

public class StarDialog extends Dialog {

    @Bind(R.id.small_rating_bar_in_star_dialog)
    RatingBar ratingBar_in_star_dialog;
    @Bind(R.id.okay_button_in_star_dialog)
    Button okay_button_in_star_dialog;
    @Bind(R.id.cancel_button_in_star_dialog)
    Button cancel_button_in_star_dialog;

    private boolean okayButton = false;
    boolean rating_first_touch = false;
    float rate;
    float rating_sum;
    int id;
    public StarDialog(Context context, int id, float rating_sum){
        super(context);
        this.id = id;
        this.rating_sum = rating_sum;
    }
    private static final String TAG = StarDialog.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_star);

        Log.d(TAG, "값전달" + id);
        ButterKnife.bind(this);
        ratingBar_in_star_dialog.setNumStars(5);
        ratingBar_in_star_dialog.setRating(5);
//        Drawable drawable = ratingBar_in_star_dialog.getProgressDrawable();
//        drawable.setColorFilter(Color.parseColor("#FFFDEC00"), PorterDuff.Mode.SRC_ATOP);
        ratingBar_in_star_dialog.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {

                    public void onRatingChanged(
                            RatingBar ratingBar, float rating, boolean fromUser) {
                        rating_first_touch = true;
                        ratingBar_in_star_dialog.setRating(rating);

                        rate = rating;
                        // rating을 전달해줘야겠지..!
                    }
                });
    }
    @OnClick({R.id.cancel_button_in_star_dialog, R.id.okay_button_in_star_dialog})
    public void onClick(View view){
        int viewId = view.getId();
        switch (viewId){
            case R.id.okay_button_in_star_dialog:
                okayButton = true;
                // 여기서 rating을 가지고 database에 접근하는게 필요한데.. 어떤식으로 할까? 일단 rating을 가져오긴 했다..!
                // 여기서는 그냥 추가해주자.. attribute를 바꿔야 할 거 같다..! 우선 동영상 제목 추가해주고,,! rating을 빼고 총합과, 참여인원을 하자..!
                // 즉 여기서는 총합에 참여인원을 하나씩 추가해주면되는데 어떻게 하냐..? update문을 사용해야되는데 기존것에 더하는게 되나?


                if(rating_first_touch ==  false)
                {
                    rate = 5;
                }

                Toast.makeText(getContext(),
                        "별점 :" + String.valueOf(rate) + "을 주셨습니다", Toast.LENGTH_LONG).show();

                Retrofit client = new Retrofit.Builder().baseUrl(Contract.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                final YoutubeService youtubeService = client.create(YoutubeService.class);

                rating_sum = rating_sum + rate;
                Log.d(TAG, "평균테스트 : " +rate);
                Call<Void> call = youtubeService.updateRating(id, rate);
                call.enqueue(new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d(TAG, "업데이트 정상적으로 됨..!");
                        }else{
                            Log.d(TAG, "레트로핏테스트 " + response.message());
                            Log.d(TAG, "[레트로핏테스트(contentInfo)] FAIL");
                            dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "[레트로핏테스트(contentInfo)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                        t.printStackTrace();
                        okayButton = false;
                        dismiss();
                    }
                });
                dismiss();
                break;
            case R.id.cancel_button_in_star_dialog:

                // 취소 눌렀는데..
                dismiss();
                break;
            default:
                break;
        }
    }

    public float getRate() {
        Log.d(TAG, "인터페이스테스트 getRate" + rate);
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public boolean getOkay_Button(){
        return okayButton;
    }
    public void setOkay_Button(boolean okayButton){
        this.okayButton = okayButton;
    }
}
