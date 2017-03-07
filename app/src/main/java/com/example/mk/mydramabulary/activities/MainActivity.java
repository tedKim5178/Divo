package com.example.mk.mydramabulary.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.R;
import com.example.mk.mydramabulary.adapter.TabPagerAdapter;
import com.example.mk.mydramabulary.adapter.YoutubeAdapter;
import com.example.mk.mydramabulary.fragments.DramaFragment;
import com.example.mk.mydramabulary.fragments.VocaFragment;

import java.io.Serializable;
import java.util.List;


public class MainActivity extends AppCompatActivity implements VocaFragment.CustomInterface, YoutubeAdapter.CallbackInterface{


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String queryInMain="";
    private int turn = 0;
    private final int VOCAFRAGMENT_TURN = 0;
    private final int DRAMAFRAGMENT_TURN = 1;

    private static final String TAG = MainActivity.class.getSimpleName();
    SearchView searchView;
    List<DataInfo> dataInfos;
    private Typeface type;
    Boolean viewpagerRefresh = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                queryInMain = query;
                searchView.onActionViewCollapsed();

                VocaFragment vocaFragment = ((VocaFragment) pagerAdapter.getItem(0));
                DramaFragment dramaFragment = ((DramaFragment)pagerAdapter.getItem(1));

                vocaFragment.setQueryInVocaFragment(query, turn);
                dramaFragment.setQueryInDramaFragment(query, turn);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
//        Log.d(TAG,  "null테스트"+menu.findItem(R.id.action_research).toString());
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_research).getActionView();
//        Log.d(TAG, "null테스트 + " + searchView);
//        Log.d(TAG, "null테스트" + searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    TabPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUsEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // 여기서 fragment 바꿔주면 될거같은데 즉 replacement
                viewpagerRefresh = true;
                pagerAdapter = null;
                pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(pagerAdapter);
                viewpagerRefresh = false;
            }
        });

        tabLayout =(TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Definition"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));

        type = Typeface.MONOSPACE;
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for(int i=0, count = tabLayout.getTabCount(); i<count; i++){
            AppCompatTextView view = (AppCompatTextView)((ViewGroup)slidingTabStrip.getChildAt(i)).getChildAt(1);
            view.setTypeface(type, Typeface.NORMAL);
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d(TAG, "Fragment Refresh");

                // 만약 toolbar를 눌러서 들어온거라면...
                // 뷰페이져 새로 만들어줄까..?
                // 아니면 이게 toolbar를 통해서 온거라는 어떤 특정한 것의 값만 알면 뷰를 바꿔줄수 있는데.. how..?

                if(viewpagerRefresh){

                }else{
                    if(tab.getPosition() == VOCAFRAGMENT_TURN){
                        turn = VOCAFRAGMENT_TURN;
                    }else{
                        turn = DRAMAFRAGMENT_TURN;
                    }
                    viewPager.setCurrentItem(tab.getPosition());
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void passQuery(String query) {
        Log.d(TAG, "하이라이트테스트 여긴 언제..? ");
        this.queryInMain = query;
    }

    @Override
    public void onHandleSelection(int position, List<DataInfo> dataInfos) {
        Log.d(TAG, "인터페이스테스트");
        Intent intent = new Intent(getApplicationContext(), YoutubeActivity.class);
        intent.putExtra("position", position);
        Log.d(TAG, "하이라이트테스트 in Main" + queryInMain);
        intent.putExtra("query", queryInMain);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dataInfos", (Serializable) dataInfos);
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "생명주기테스트 onActivityResult in main activity");
        // 그럼 이제 데이터를 받아와야됨...!
        if( data == null){
            Log.d(TAG, "생명주기테스트 onActivityResult in main and data is null");
        }else{
            Bundle bundle = data.getExtras();
            dataInfos = (List<DataInfo>)bundle.getSerializable("dataInfos");
            int position = data.getIntExtra("position", 0);

            Log.d(TAG, "생명주기테스트 onActivityResult position, getRating_sum : " + position + ",: " + dataInfos.get(position).getRating_sum());
            // 함수 호출

            ((DramaFragment)pagerAdapter.getItem(1)).refreshDataInfos(dataInfos, position);
        }

    }


}
