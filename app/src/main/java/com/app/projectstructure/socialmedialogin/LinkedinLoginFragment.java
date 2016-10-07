package com.app.projectstructure.socialmedialogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.projectstructure.R;
import com.app.projectstructure.constant.AppConstant;
import com.app.projectstructure.controller.FragmentController;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * How to integrate Linkedin with android application
 * <p/>
 * Step 1- Download Linkedin Android sdk.
 * Step 2- Add sdk as module in app.
 * Step 3- Create application on Developer site and
 * (https://www.linkedin.com/developer/apps/4809145/mobile)
 * register package name and hash key
 * step 4- Install linkedin application in mobile and use this code
 */
public class LinkedinLoginFragment extends FragmentController {

    private final String TAG = this.getClass().getSimpleName();
    private View    mViewObj;
    private Context mAppContext;
    private Context mContext;
    private LinkedinLoginCallback mLinkedinLoginCallback;

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLinkedinLoginCallback=(LinkedinLoginCallback)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewObj = inflater.inflate(R.layout.linkedin_login_fragment, container, false);
        mAppContext = getContext().getApplicationContext();
        generateHashkey();
        mContext = getContext();
        return mViewObj;
    }

    public void generateHashkey() {
        try {
            PackageInfo info = mAppContext.getPackageManager().getPackageInfo(
                    AppConstant.PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("Hash key", Base64.encodeToString(md.digest(),Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    public void login(View view) {
        LISessionManager.getInstance(mAppContext)
                .init((Activity) mContext, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {

                        String token=LISessionManager
                                .getInstance(mAppContext)
                                .getSession().getAccessToken().toString();

                        mLinkedinLoginCallback.onLinkedinLoginResponse(1,token);
                        Log.e(TAG,token);
                       /* Toast.makeText(mAppContext, "success" +
                                               LISessionManager
                                                       .getInstance(mAppContext)
                                                       .getSession().getAccessToken().toString(),
                                       Toast.LENGTH_LONG).show();*/
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {

                        mLinkedinLoginCallback.onLinkedinLoginResponse(0,"");
                        Log.e(TAG,error.toString());
//                        Toast.makeText(mAppContext, "failed "
//                                               + error.toString(),
//                                       Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(mAppContext)
                .onActivityResult((Activity) mContext,
                                  requestCode, resultCode, data);
    }

    public interface LinkedinLoginCallback {
        void onLinkedinLoginResponse(int status, String token);
    }
}
