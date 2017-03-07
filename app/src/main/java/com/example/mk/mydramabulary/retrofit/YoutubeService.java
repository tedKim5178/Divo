package com.example.mk.mydramabulary.retrofit;

import com.example.mk.mydramabulary.DataInfo;
import com.example.mk.mydramabulary.RecommendationItems;
import com.example.mk.mydramabulary.activities.TokenInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mk on 2017-02-13.
 */

public interface YoutubeService {
    @GET("/db")
    Call<List<DataInfo>> getDataInfo(@Query("sentence") String sentence, @Query("page") int page);

    @GET("/text")
    Call<List<DataInfo>> getTextInfo(@Query("video_id") String video_id, @Query("startTime") int startTime);

    @GET("/rating")
    Call<Void> updateRating(@Query("p_id") int id, @Query("rating") float rating);

    @GET("/recommendation")
    Call<List<RecommendationItems>> gerRecommendationInfo();

    @GET("/o/oauth2/auth?client_id=894330957153-489o1uvfksvomvm067j7kq4b6agfvtbi.apps.googleusercontent.com&redirect_uri=urn:ietf:wg:oauth:2.0:oob&scope=https://www.googleapis.com/auth/youtube&response_type=code&access_type=offline&pageId=none")
    Call<ResponseBody> test();

    @FormUrlEncoded
    @POST("/o/oauth2/token")
    Call<TokenInfo> getRealToken(@Field("code") String code, @Field("client_id") String client_id, @Field("redirect_uri") String redirect_uri, @Field("grant_type") String grant_type);

}
