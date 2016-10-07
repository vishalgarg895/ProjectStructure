package com.app.projectstructure.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishal on 8/10/16.
 */
public class ResponseModel {

    @SerializedName("status")
    private String status  = "";
    @SerializedName("message")
    private String message = "";
    @SerializedName("user_id")

    private String user_id = "";

    public String getUser_id() {
        return user_id;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
