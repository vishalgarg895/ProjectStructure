package com.app.projectstructure.socialmedialogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.projectstructure.R;
import com.app.projectstructure.controller.FragmentController;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * How to integrate twitter with android application
 * Step 1- Add Fabric with android studio and add Twitter api with the app.
 * Step 2- create App on Fabric website for registering app
 * ----- (https://fabric.io/home)-------
 * Step 3- then use this code to add Twitter Login
 */
public class TwitterLoginFragment extends FragmentController {

    private final String TAG = this.getClass().getSimpleName();
    private View                 mViewObj;
    private TwitterLoginButton   loginButton;
    private TwitterLoginCallback mTwitterLoginCallback;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewObj = inflater.inflate(R.layout.twitter_login_fragment, container, false);
        loginButton = (TwitterLoginButton) mViewObj.findViewById(R.id.login_button);
        initTwitterLoginCallback();
        return mViewObj;
    }

    private void initTwitterLoginCallback() {
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                initTwitterSession();
            }

            @Override
            public void failure(TwitterException exception) {
                mTwitterLoginCallback.onTwitterLoginResponse(0, "", "");
            }
        });
    }

    private void initTwitterSession() {
        TwitterSession   session   = Twitter.getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();
        String           token     = authToken.token;
        String           secret    = authToken.secret;
        mTwitterLoginCallback.onTwitterLoginResponse(1, token, secret);
        Log.e(TAG, "Twitter Token=" + token);
        Log.e(TAG, "Twitter secret key=" + secret);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTwitterLoginCallback = (TwitterLoginCallback) context;

    }

    public interface TwitterLoginCallback {
        void onTwitterLoginResponse(int status, String token, String secretKey);
    }
}
