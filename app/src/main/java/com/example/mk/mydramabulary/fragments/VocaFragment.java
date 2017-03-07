package com.example.mk.mydramabulary.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.RecommendationItems;
import com.example.mk.mydramabulary.activities.MainActivity;
import com.example.mk.mydramabulary.adapter.RecommendationAdapter;
import com.example.mk.mydramabulary.adapter.WordAdapter;
import com.example.mk.mydramabulary.contract.Contract;
import com.example.mk.mydramabulary.dialog.CustomProgressDialog;
import com.example.mk.mydramabulary.parsing.ParsingContract;
import com.example.mk.mydramabulary.retrofit.YoutubeService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mk on 2017-02-10.
 */

public class VocaFragment extends Fragment{

    private static final String TAG = MainActivity.class.getSimpleName();


    static String queryTest;

    @Bind(R.id.search_result_textView)
    TextView search_result_textView;
    @Bind(R.id.word_definition)
    TextView word_definition;
    @Bind(R.id.word_definition_cardview)
    CardView word_definition_cardview;

//    @Bind(R.id.progressBar_in_voca)
//    ProgressBar progressBar_in_voca;

    @Bind(R.id.recycler_view_in_voca)
    RecyclerView recyclerView_in_vova;

    @Bind(R.id.recycler_view_first_place)
    RecyclerView recycler_view_first_place;

    @Bind(R.id.view_when_divo_clicked)
    LinearLayout view_when_divo_clicked;
    @Bind(R.id.view_with_contents)
    NestedScrollView view_with_contents;

    WordAdapter wordAdapter;
    RecommendationAdapter rocommendationAdapter;

    AsyncTask<?, ?, ?> searchTask;
    ArrayList koreanExampleSentences;
    ArrayList englishExampleSentences;

    ArrayList<String> wordOverall;
    ArrayList<TextView> textViewAll;
    String query="";
//    LinearLayout containers;
//    int count = 0;

    List<RecommendationItems> recommendationItemsList;

    private boolean networkCondition =  false;
    CustomProgressDialog progressDlg;
    public interface CustomInterface{
        void passQuery(String query);
    }

    SpannableStringBuilder ssb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary, container, false);
        ButterKnife.bind(this, view);

//        Drawable alpha = ((FrameLayout)view.findViewById(R.id.drawer)).getBackground();
//        alpha.setAlpha(100);

        progressDlg = new CustomProgressDialog(getContext());

        view_with_contents.setVisibility(View.INVISIBLE);
        view_when_divo_clicked.setVisibility(View.VISIBLE);

        // 버터나이프 bind
        koreanExampleSentences = new ArrayList();
        englishExampleSentences = new ArrayList();
        textViewAll = new ArrayList<TextView>();
        wordOverall = new ArrayList<>();

        // initialize recyclerview
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_in_vova.setLayoutManager(linearLayoutManager);
        recyclerView_in_vova.setNestedScrollingEnabled(false);
        view.findViewById(R.id.recycler_view_in_voca).setFocusable(false);
        view.findViewById(R.id.linearlayout_container).requestFocus();

        // 처음에 띄워주자..!
        // 레트로핏 필요..!
        final LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        recycler_view_first_place.setLayoutManager(linearLayoutManager2);

        Retrofit client = new Retrofit.Builder().baseUrl(Contract.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        final YoutubeService youtubeService = client.create(YoutubeService.class);
        Call<List<RecommendationItems>> call = youtubeService.gerRecommendationInfo();
        call.enqueue(new Callback<List<RecommendationItems>>(){
            @Override
            public void onResponse(Call<List<RecommendationItems>> call, Response<List<RecommendationItems>> response) {
                if(response.isSuccessful()){
                    recommendationItemsList = response.body();

                    rocommendationAdapter = new RecommendationAdapter(getContext(), recommendationItemsList);
                    recycler_view_first_place.setAdapter(rocommendationAdapter);

                }else{
                    Log.d(TAG, "레트로핏테스트 " + response.message());
                    Log.d(TAG, "[레트로핏테스트(contentInfo)] FAIL");
                }
            }

            @Override
            public void onFailure(Call<List<RecommendationItems>> call, Throwable t) {
                Log.d(TAG, "[레트로핏테스트(contentInfo)] onFail" + t.getMessage() + t.toString() + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });


        return view;
    }

    // TODO : This is unnecessay part now, but this is about how to pass data from Activity
    // TODO : to Fragment. This will be so much helpful for you later.
//    public VocaFragment newInstance(){
//        VocaFragment vocaFragment = new VocaFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("abcd", "kk");
//        setArguments(bundle);
//        return vocaFragment;
//    }

    // 검색 눌리면

    //TODO : This is unnecessary part now, but this about callback will be helpful for you.
//    @OnClick(R.id.search_button_in_vocabulary)
//    void buttonClicked(View view) {
//        // 파싱부분 시작
//        // 우선은 Toast!
//
//        progressBar_in_voca.setVisibility(View.VISIBLE);
//        if (view.getId() == R.id.search_button_in_vocabulary) {
//            // 버튼이 눌리면 edittext에 있는거 가지고오자
//            query = search_editText_in_vocabulary.getText().toString();
//
//            CustomInterface passQuery = (CustomInterface) getContext();
//            passQuery.passQuery(query);
//
//            searchTask = new searchTask().execute();
//            Toast.makeText(getContext(),query,Toast.LENGTH_SHORT).show();
//            for(int i=0; i< textViewAll.size(); i++){
//                containers.removeView(textViewAll.get(i));
//            }
//        }
//    }

    private class searchTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            progressDlg.dismiss(); // 없애기

            if(networkCondition){
                // query 정보를 textView에 표시해주자
                if(query == null){
                }else if(query.trim().length() == 0){
                    search_result_textView.setText("검색어를 다시 입력해주세요");
                }else{
                    search_result_textView.setText(query);
                }

                if(wordOverall.size() == 0){
                    Log.d(TAG, "여기안옴?");
                    word_definition.setText("검색 결과가 없습니다");
                }else{
                    for(int i=0; i<wordOverall.size(); i++){
                        word_definition.append(wordOverall.get(i));
                        if(i>3){
                            break;
                        }
                    }
                }

                wordAdapter = new WordAdapter(getContext(), koreanExampleSentences, englishExampleSentences, query);
                recyclerView_in_vova.setAdapter(wordAdapter);

                // 단어 뜻 이제 보여주자..!
                view_with_contents.setVisibility(View.VISIBLE);
                search_result_textView.setVisibility(View.VISIBLE);
                word_definition.setVisibility(View.VISIBLE);
                word_definition_cardview.setVisibility(View.VISIBLE);
                recyclerView_in_vova.setVisibility(View.VISIBLE);

            }else{
                search_result_textView.setText("네트워크 상황을 확인해주세요");
                view_with_contents.setVisibility(View.VISIBLE);
                search_result_textView.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {

            // 단어 파싱하는 부분
            try {
                Document wordHompage = Jsoup.connect(ParsingContract.WORD_FIRST_BASE_URL+ query + ParsingContract.WORD_LAST_BASE_URL).get();
                Elements wordsSeperatedByDiv = wordHompage.select(ParsingContract.WORD_SEPARATION);
                for(int i=0; i< wordsSeperatedByDiv.size(); i++){
                    Element element = wordsSeperatedByDiv.get(i);
                    Elements wordsClassification = element.getElementsByClass(ParsingContract.WORD_CLASSIFICATION_CLASS);
                    Elements wordsDefinition = element.getElementsByClass(ParsingContract.WORD_DEFINITION);

                    wordOverall.add(wordsClassification.text() + " " + wordsDefinition.text() + "\n");
                }
                networkCondition = true;

            }catch (Exception e){
                networkCondition = false;
                Log.d(TAG, "로그로찍어준다");
//                Toast.makeText(getContext(), "와이파이없음", Toast.LENGTH_SHORT).show();
            }

            // 예문 파싱하는 부분
            try {
                Document sentencesHompage = Jsoup.connect(ParsingContract.SENTENCE_FRIST_BASE_URL+query+ParsingContract.SENTENCE_LAST_BASE_URL).get();
                Elements englishSeperatedByDiv = sentencesHompage.select(ParsingContract.ENGLISH_PARSING_QUERY_DIV);

                for (Element e : englishSeperatedByDiv) {
                    Elements e2 = e.getElementsByTag(ParsingContract.ENGLISH_PARSING_QUERY_TAG);
                    englishExampleSentences.add(e2.get(e2.size()-1).attr(ParsingContract.ENGLISH_PARSING_QUERY_ATTR));
                }
                Elements input2 = sentencesHompage.select(ParsingContract.KOREAN_PARSING_QUERY_DIV);
                for (Element e3 : input2) {
                    koreanExampleSentences.add(e3.text());
                }
            } catch (Exception e) {
            }


            return null;
        }
    }


    public void setQueryInVocaFragment(String query, int turn){
        // 검색창에서 들어온 쿼리를 이용해서 파싱을 진행하자...!

        word_definition.setText("");
        search_result_textView.setVisibility(View.INVISIBLE);
        word_definition.setVisibility(View.INVISIBLE);
        word_definition_cardview.setVisibility(View.INVISIBLE);
        recyclerView_in_vova.setVisibility(View.INVISIBLE);
        view_when_divo_clicked.setVisibility(View.INVISIBLE);

        if(turn == 0){
            progressDlg.show(); // 보여주기
        }
        this.query = query;
        // 기존에 있던 recyclerView에 들어가는 정보를 초기화해주면된다 즉 arraylist들 초기화
        koreanExampleSentences.clear();
        englishExampleSentences.clear();
        wordOverall.clear();

        searchTask = new searchTask().execute();

        // make progressbar visible

//        progressBar_in_voca.setVisibility(View.VISIBLE);

        progressDlg.getWindow().setBackgroundDrawable(
                new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));

    }
}

