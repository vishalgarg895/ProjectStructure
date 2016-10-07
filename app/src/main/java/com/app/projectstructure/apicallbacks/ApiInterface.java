package com.app.projectstructure.apicallbacks;

import com.app.projectstructure.model.FeaturedModel;
import com.app.projectstructure.model.ResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("app/list_featured")
    Call<FeaturedModel> featuredCouponList(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST("app/list_nearby")
    Call<FeaturedModel> nearbyCouponList(@FieldMap Map<String, String> param);

    @Multipart
    @POST("registration")
        Call<ResponseModel> upload(@Part MultipartBody.Part file,
                                   @Part("token") RequestBody token,
                                   @Part("is_social") RequestBody is_social,
                                   @Part("firstname") RequestBody firstname,
                                   @Part("lastname") RequestBody lastname,
                                   @Part("password") RequestBody password,
                                   @Part("email") RequestBody email,
                                   @Part("username") RequestBody username


    );

}
