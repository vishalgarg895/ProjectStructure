package com.app.projectstructure.apicallbacks;

import android.content.Context;
import android.util.Log;

import com.app.projectstructure.model.FeaturedModel;
import com.app.projectstructure.model.ResponseModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkCallbackClient {

    //public static final String BASE_URL                  = "http://www.rekommendus.com/dawer/index.php/";
    /*this url is use to test multipart image uploading using retrofit.*/
    public static final String BASE_URL                    = "http://www.rekommendus.com/dawer/index.php/app/";
    public static final String TAG_FEATURED_API            = "featured_api";
    public static final String TAG_NEARBY_API              = "nearby_api";
    public static final String TAG_COPY_COUPON_API         = "copy_coupon_api";
    public static final String TAG_SHARED_COUPON_COUNT_API = "shared_coupon_api";
    public static final String TAG_REGISTER_API            = "register_api";
    public static final String TAG_SHARE_COUPON_API        = "share_coupon_api";

    private final String TAG = this.getClass().getSimpleName();
    private Context                    context;
    private ApiRequestCompleteCallback caller;
    private Retrofit                   retrofit;
    private ApiInterface               apiService;
    private String                     request_type;

    public NetworkCallbackClient(Context context, ApiRequestCompleteCallback a, String type) {
        caller = a;
        this.context = context;
        request_type = type;
        initRetrofitClient();
    }

    public void initRetrofitClient() {
        // Set the custom client when building adapter
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiInterface.class);
    }

    public void initRetrofitBasicAuth() {

        //----------------- Retrofit with Basic Authentication -----------------
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                String credential = Credentials.basic("prod2@a1tech.com", "production2@");
                Log.e("Credential", credential);
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", credential)
                        .build();
                return chain.proceed(newRequest);
            }
        };

// Add the interceptor to OkHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

// Set the custom client when building adapter
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.myevercar.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


    }

    public void uploadImageAPI(String path) {

        File file = new File(path);

        RequestBody        reqFile   = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body      = MultipartBody.Part.createFormData("profilepic", file.getName(), reqFile);
        RequestBody        token     = RequestBody.create(MediaType.parse("text/plain"), "a152e84173914146e4bc4f391sd0f686ebc4f31");
        RequestBody        is_social = RequestBody.create(MediaType.parse("text/plain"), "0");
        RequestBody        firstname = RequestBody.create(MediaType.parse("text/plain"), "Rohit");
        RequestBody        lastname  = RequestBody.create(MediaType.parse("text/plain"), "Jain");
        RequestBody        password  = RequestBody.create(MediaType.parse("text/plain"), "123456");
        RequestBody        email     = RequestBody.create(MediaType.parse("text/plain"), "email@gmail.com");
        RequestBody        username  = RequestBody.create(MediaType.parse("text/plain"), "rohit@gmail.com");


        Call<ResponseModel> call = apiService.upload(body, token, is_social, firstname, lastname, password, email, username);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                caller.onResponse(response.body(), request_type, true, response.code());
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                caller.onResponse("", request_type, true, 500);
            }
        });


    }


    public void callFeaturedAPI(HashMap<String, String> params) {
        Call<FeaturedModel> call = apiService.featuredCouponList(params);
        call.enqueue(new Callback<FeaturedModel>() {
            @Override
            public void onResponse(Call<FeaturedModel> call, Response<FeaturedModel> response) {
                caller.onResponse(response.body(), request_type, true, response.code());
            }

            @Override
            public void onFailure(Call<FeaturedModel> call, Throwable t) {
                caller.onResponse("", request_type, true, 500);
            }
        });
    }

    public void callNearbyAPI(HashMap<String, String> params) {
        Call<FeaturedModel> call = apiService.nearbyCouponList(params);
        call.enqueue(new Callback<FeaturedModel>() {
            @Override
            public void onResponse(Call<FeaturedModel> call, Response<FeaturedModel> response) {
                caller.onResponse(response.body(), request_type, true, response.code());
            }

            @Override
            public void onFailure(Call<FeaturedModel> call, Throwable t) {
                caller.onResponse("", request_type, true, 500);
            }
        });
    }

    public interface ApiRequestCompleteCallback {
        void onResponse(Object response, String requestType, boolean request_state, int statusCode);
    }
}
