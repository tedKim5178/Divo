package com.example.mk.mydramabulary.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.EndlessRecyclerOnScrollListener;
import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.activities.MainActivity;
import com.example.mk.mydramabulary.activities.TokenInfo;
import com.example.mk.mydramabulary.adapter.YoutubeAdapter;
import com.example.mk.mydramabulary.contract.Contract;
import com.example.mk.mydramabulary.dialog.CustomProgressDialog;
import com.example.mk.mydramabulary.retrofit.YoutubeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mk on 2017-02-10.
 */

public class DramaFragment extends Fragment{
    private static final String TAG = MainActivity.class.getSimpleName();

    private YoutubeAdapter youtubeAdapter;
    RecyclerView recyclerView;

    String query;
    CustomProgressDialog progressDlg;

    List<DataInfo> datainfos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drama, container, false);

        // 버터나이프 bind
        ButterKnife.bind(this, view);
//        Drawable alpha = ((FrameLayout)view.findViewById(R.id.drama_fragment)).getBackground();
//        alpha.setAlpha(20);

        datainfos = new ArrayList<>();

        progressDlg = new CustomProgressDialog(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_in_drama);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        Retrofit client = new Retrofit.Builder().baseUrl("https://accounts.google.com/").addConverterFactory(GsonConverterFactory.create()).build();
        YoutubeService youtubeService = client.create(YoutubeService.class);
        Call<TokenInfo> call = youtubeService.getRealToken("4/MfLDC9Wmo-ZBkUrB6qiGyMzZkxFv6rwOAW55JQ70Hhw",
                "894330957153-todgemm4li35vt5i018emruv1c4kv768.apps.googleusercontent.com","urn:ietf:wg:oauth:2.0:oob","authorization_code");
        call.enqueue(new Callback<TokenInfo>(){
            @Override
            public void onResponse(Call<TokenInfo> call, Response<TokenInfo> response) {
                Log.d(TAG, "레트로핏테스트(우하하성공) ");
                if(response.isSuccessful()){
                    TokenInfo tokenInfo = (TokenInfo)response.body();
                    Log.d(TAG, "레트로핏테스트 tokenInfo : " + tokenInfo.getAccess_token() + ", " + tokenInfo.getRefresh_in() + ", " + tokenInfo.getToken_type());
                }else{
                    Log.d(TAG, "레트로핏테스트 " + response.message() + ", " +response.errorBody() + ", " + response.code() + ", " + response.body() + ", " +response.headers());
                    Log.d(TAG, "[레트로핏테스트(우하하)] FAIL");
                }
            }

            @Override
            public void onFailure(Call<TokenInfo> call, Throwable t) {
                Log.d(TAG, "[레트로핏테스트(우하하)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(TAG, "무한리사이클러뷰 테스트 여기나오는거야?");
                // 레트로핏
                Retrofit client = new Retrofit.Builder().baseUrl(Contract.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                YoutubeService youtubeService = client.create(YoutubeService.class);
                Log.d(TAG, "무한리사이클러뷰 테스트 여기나오는거야?");
                Log.d(TAG,"무한리사이클러뷰 테스트 getCurrentpage: " + EndlessRecyclerOnScrollListener.getCurrentPage());
                Call<List<DataInfo>> call = youtubeService.getDataInfo(query, EndlessRecyclerOnScrollListener.getCurrentPage());
                call.enqueue(new Callback<List<DataInfo>>(){
                    @Override
                    public void onResponse(Call<List<DataInfo>> call, Response<List<DataInfo>> response) {
                        if(response.isSuccessful()){
                            datainfos = response.body();
                            Log.d(TAG, "레트로핏테스트 data size " + datainfos.size());
                            // 어댑터를 datainfos를 이용해 초기화
//                            youtubeAdapter = new YoutubeAdapter(getContext(), datainfos);
//                            recyclerView.setAdapter(youtubeAdapter);
//                            youtubeAdapter.setDataInfos(datainfos);
                            youtubeAdapter.addDataInfos(datainfos);
                            youtubeAdapter.notifyDataSetChanged();
                        }else{
                            Log.d(TAG, "레트로핏테스트 " + response.message());
                            Log.d(TAG, "[레트로핏테스트(contentInfo)] FAIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DataInfo>> call, Throwable t) {
                        Log.d(TAG, "[레트로핏테스트(contentInfo)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                        t.printStackTrace();
                    }
                });

//                youtubeAdapter.notifyDataSetChanged();
                Log.d(TAG, "무한리사이클러뷰 테스트 getFirstVisible" + EndlessRecyclerOnScrollListener.getFirstVisibleitem());
//                youtubeAdapter.notifyItemInserted(15);
//                youtubeAdapter.notifyItemRangeInserted(15,15);
                int lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
                youtubeAdapter.notifyItemRangeChanged(EndlessRecyclerOnScrollListener.getTotalItemCount(), 15);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume생성?");
        if(youtubeAdapter != null){
            Log.d(TAG, "onResume여기호출?");
            youtubeAdapter.notifyDataSetChanged();

        }
    }

    public void setQueryInDramaFragment(final String query, int turn){
        recyclerView.setVisibility(View.INVISIBLE);
        progressDlg.getWindow().setBackgroundDrawable(
                new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));
        if(turn == 1){
            progressDlg.show(); // 보여주기
        }
        datainfos.clear();
        if(query == ""){

        }else{
            this.query = query;

//            search_editText_in_drama.setText(query);
            EndlessRecyclerOnScrollListener.setCurrentPage(1);
            EndlessRecyclerOnScrollListener.setPreviousTotal(0);
            // 해당하는 동영상 불어와야겠지
            // 우선은 리사이클러뷰에 보여줄 것이다. 그렇다면 리사이클러뷰가 필요하겠지
            // query = search_editText_int_drama.getText().toString();
            Retrofit client = new Retrofit.Builder().baseUrl(Contract.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            final YoutubeService youtubeService = client.create(YoutubeService.class);
            Log.d(TAG,"무한리사이클러뷰 테스트 getCurrentpage: " + EndlessRecyclerOnScrollListener.getCurrentPage());
            Call<List<DataInfo>> call = youtubeService.getDataInfo(query, EndlessRecyclerOnScrollListener.getCurrentPage());
            call.enqueue(new Callback<List<DataInfo>>(){
                @Override
                public void onResponse(Call<List<DataInfo>> call, Response<List<DataInfo>> response) {
                    if(response.isSuccessful()){
                        datainfos = response.body();

                        youtubeAdapter = new YoutubeAdapter(getContext(), datainfos, query);
                        recyclerView.setAdapter(youtubeAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        progressDlg.dismiss();
                    }else{
                        Log.d(TAG, "레트로핏테스트 " + response.message());
                        Log.d(TAG, "[레트로핏테스트(contentInfo)] FAIL");
                        progressDlg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<DataInfo>> call, Throwable t) {
                    Log.d(TAG, "[레트로핏테스트(contentInfo)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                    t.printStackTrace();
                    progressDlg.dismiss();
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "생명주기테스트 onActivityResult in dramafragment");
    }

    public void refreshDataInfos(List<DataInfo> dataInfos, int position){
        Log.d(TAG, "생명주기테스트 refreshDataInfos");
        // 여기서 이제 adapter에 알려줘야함...!
        youtubeAdapter.refreshDataInfosInAdpater(dataInfos, position);
        // 사실은 여기서 데이터를 다시 받아와야됨...!

    }
}
