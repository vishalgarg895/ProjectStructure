package com.app.projectstructure.apicallbacks;
import com.app.projectstructure.model.FeaturedModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("app/list_featured")
    Call<FeaturedModel> featuredCouponList(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("app/list_nearby")
    Call<FeaturedModel> nearbyCouponList(@FieldMap Map<String, String> param);



}
