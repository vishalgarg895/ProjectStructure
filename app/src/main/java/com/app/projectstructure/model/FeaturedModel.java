package com.app.projectstructure.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishal on 14/9/16.
 */
public class FeaturedModel {
    private static final long serialVersionUID = 1L;

    @SerializedName("status")
    private String status = "";
    @SerializedName("data")
    private List<CouponModel> info;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public List<CouponModel> getInfo() {
        if (info == null) {
            info = new ArrayList<>();
        }
        return info;
    }

    public class CouponModel {

        @SerializedName("id")
        private String id                 = "";
        @SerializedName("coupan_title")
        private String coupan_title       = "";
        @SerializedName("coupon_description")
        private String coupon_description = "";
        @SerializedName("discount")
        private String discount           = "";
        @SerializedName("image")
        private String image              = "";
        @SerializedName("business_website")
        private String business_website   = "";
        @SerializedName("share_text")
        private String share_text         = "";
        @SerializedName("map_Lat")
        private String map_Lat            = "";
        @SerializedName("link")
        private String link               = "";
        @SerializedName("map_Long")
        private String map_Long           = "";
        @SerializedName("added_by")
        private String added_by           = "";
        @SerializedName("added_at")
        private String added_at           = "";
        @SerializedName("valid_at")
        private String valid_at           = "";
        @SerializedName("user_id")
        private String user_id            = "";

        public String getImage() {
            return image;
        }

        public String getLink() {
            return link;
        }

        public String getId() {
            return id;

        }

        public String getCoupan_title() {
            return coupan_title;
        }

        public String getCoupon_description() {
            return coupon_description;
        }

        public String getDiscount() {
            return discount;
        }

        public String getBusiness_website() {
            return business_website;
        }

        public String getShare_text() {
            return share_text;
        }

        public String getMap_Lat() {
            return map_Lat;
        }

        public String getMap_Long() {
            return map_Long;
        }

        public String getAdded_by() {
            return added_by;
        }

        public String getAdded_at() {
            return added_at;
        }

        public String getValid_at() {
            return valid_at;
        }

        public String getUser_id() {
            return user_id;
        }


    }
}
