package com.app.projectstructure.views.registration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.projectstructure.R;
import com.app.projectstructure.apicallbacks.NetworkCallbackClient;
import com.app.projectstructure.controller.ActivityController;
import com.app.projectstructure.model.ResponseModel;

import java.io.IOException;

/**
 * Created by vishal on 7/10/16.
 */
public class ImageSelectorActivity extends ActivityController implements NetworkCallbackClient.ApiRequestCompleteCallback {

    public static final int    REQUEST_CODE_PIC_IMAGE = 1122;
    private final       String TAG                    = this.getClass().getSimpleName();
    private ImageView mProfilePicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_picker_activity);
        mProfilePicImg = (ImageView) findViewById(R.id.img_profile_pic);
        mProfilePicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PIC_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PIC_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            RealPathUtil pathObj = new RealPathUtil();

            String realPath;
            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = pathObj.getRealPathFromURI_BelowAPI11(this, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = pathObj.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = pathObj.getRealPathFromURI_API19(this, data.getData());


            Uri uri = data.getData();
 /*
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();

            Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));

            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex);*/

            NetworkCallbackClient callback = new NetworkCallbackClient(this, ImageSelectorActivity.this, "upload");
            callback.uploadImageAPI(realPath);

            /*Log.e(TAG,"Image Path="+picturePath);
            // returns null
            cursor.close();*/


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mProfilePicImg.setImageBitmap(bitmap);
                // Log.d(TAG, String.valueOf(bitmap));

//                ImageView imageView = (ImageView) findViewById(R.id.imageView);
//                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onResponse(Object response, String requestType, boolean request_state, int statusCode) {


        if (statusCode == 200) {
            if (response != null) {
                ResponseModel responseObj = (ResponseModel) response;
                if (responseObj.getStatus().matches("1")) {

                    Log.e(TAG, "User created successfully.");

                } else {
                    Log.e(TAG, "user not created=0");
                }
            } else {
                alertWithPositiveBtn("Server is not responding. Please try again.");
            }
        } else {
            ResponseModel responseObj = (ResponseModel) response;

        }
    }


    public class RealPathUtil {

        @SuppressLint("NewApi")
        public String getRealPathFromURI_API19(Context context, Uri uri) {
            String filePath = "";
            String wholeID  = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                               column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }


        @SuppressLint("NewApi")
        public String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj   = {MediaStore.Images.Media.DATA};
            String   result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
            String[] proj   = {MediaStore.Images.Media.DATA};
            Cursor   cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

}
