package com.app.projectstructure.socialmedialogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.projectstructure.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishal on 5/10/16.
 */
public class FBLoginFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    public  FbLoginCallback    mFbLoginCallback;
    private Context            mContext;
    private Activity           mActivity;
    private ProfileTracker     mProfileTracker;
    private LoginButton        mLoginButton;
    private AccessTokenTracker mAccessTokenTracker;
    private CallbackManager    mCallbackManager;
    private View               mViewObj;
    private String mFbAccessToken = "";

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            mFbAccessToken = accessToken.getToken();
            Log.e(TAG, "Facebook Token=" + accessToken.getToken());
            mFbLoginCallback.onFacebookLoginResponse(1, mFbAccessToken);

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    Bundle bFacebookData = getFacebookData(object);
                }

            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            mFbLoginCallback.onFacebookLoginResponse(0, "");
            Log.e(TAG, mContext.getString(R.string.msg_fblogin_failed));
        }

        @Override
        public void onError(FacebookException e) {
            mFbLoginCallback.onFacebookLoginResponse(0, "");
            Log.e(TAG, mContext.getString(R.string.msg_fblogin_failed));
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFbLoginCallback = (FbLoginCallback) context;
    }

    private void initFacebookSdk() {
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
        AppEventsLogger.activateApp(mContext);
    }

    private void initFacebookCallback() {
        mCallbackManager = CallbackManager.Factory.create();
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };
        mAccessTokenTracker.startTracking();
        mProfileTracker.startTracking();
        List<String> permissionList = new ArrayList<>();
        permissionList.add("public_profile");
        permissionList.add("email");
        mLoginButton.setReadPermissions(permissionList);
        mLoginButton.registerCallback(mCallbackManager, mFacebookCallback);
    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id     = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", id);
            // @formatter:off
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name" , object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email"     , object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender"    , object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday"  , object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location"  , object.getJSONObject("location").getString("name"));
            // @formatter:on
            return bundle;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopFacebookTokenProfileTracking();
    }

    private void stopFacebookTokenProfileTracking() {
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            Log.v(TAG, "onCreateView Called");
            mContext = (Context) getActivity();
            mActivity = getActivity();
            initFacebookSdk();
            mViewObj = inflater.inflate(R.layout.facebook_login_fragment, container, false);
            mLoginButton = (LoginButton) mViewObj.findViewById(R.id.login_button);
            initFacebookCallback();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mViewObj;
    }

    public interface FbLoginCallback {
        void onFacebookLoginResponse(int LoginStatus, String FbAccessToken);
    }

    public void showToastMessage()
    {
        Toast.makeText(mContext,"call received",Toast.LENGTH_SHORT).show();

    }
}
