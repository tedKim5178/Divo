package com.example.mk.mydramabulary;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by mk on 2017-02-13.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener{
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private static int previousTotal = 0;

    private static int firstVisibleitem = 0;
    int visibleItemCount;
    private static int totalItemCount;
    private int visibleThreshold = 3;
    private boolean loading = true;

    private static int current_page = 1;
    private LinearLayoutManager linearlayoutManager;

    public static int getTotalItemCount(){
        return totalItemCount;
    }
    public static int getCurrentPage(){
        return current_page;
    }

    public static void setPreviousTotal(int previous){
        previousTotal = previous;
    }
    public static void setCurrentPage(int currentpage){
        current_page = currentpage;
    }
    public static int getFirstVisibleitem(){
        return firstVisibleitem;
    }

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager){
        this.linearlayoutManager = linearLayoutManager;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        Log.d(TAG, "무한리사이클러뷰테스트 visibleItemCount = " + visibleItemCount);
        totalItemCount = linearlayoutManager.getItemCount();
        Log.d(TAG, "무한리사이클러뷰테스트 totalItemCount = " + totalItemCount);
        firstVisibleitem = linearlayoutManager.findFirstVisibleItemPosition();
        Log.d(TAG, "무한리사이클러뷰테스트 firstVisibleitem = " + firstVisibleitem);
        if(loading){
            if(totalItemCount > previousTotal){
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        Log.d(TAG, "무한리사이클러뷰테스트 boolean !loading " + !loading);
        Log.d(TAG, "무한리사이클러뷰테스트 previous " + previousTotal);
        Log.d(TAG, "무한리사이클러뷰테스트 totalItemCount - visibleItemCount = " +(totalItemCount - visibleItemCount));
        Log.d(TAG, "무한리사이클러뷰테스트 firstVisibleitem + visibleThreshold = " +(firstVisibleitem + visibleThreshold));
        if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleitem + visibleThreshold)){
            current_page ++;
            onLoadMore(current_page);
            Log.d(TAG, "무한리사이클러뷰테스트 LoadMore 호출 and current page = " + current_page);
            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
