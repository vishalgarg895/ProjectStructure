package com.app.projectstructure.views.registration;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.projectstructure.R;
import com.app.projectstructure.socialmedialogin.FBLoginFragment;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateHashKey();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void generateHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.projectstructure",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("Hash key", Base64.encodeToString(md.digest(),
                                                        Base64.NO_WRAP));
//                ((TextView) findViewById(R.id.hashKey))
//                        .setText(Base64.encodeToString(md.digest(),
                //                    Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    public void login(View view) {
        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        Toast.makeText(getApplicationContext(), "success" +
                                               LISessionManager.getInstance(getApplicationContext())
                                                       .getSession().getAccessToken().toString(),
                                       Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                               + error.toString(),
                                       Toast.LENGTH_LONG).show();
                    }
                }, true);
    }

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                                  requestCode, resultCode, data);
    }
}
