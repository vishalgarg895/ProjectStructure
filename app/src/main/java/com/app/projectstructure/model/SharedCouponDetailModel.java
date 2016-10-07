package com.app.projectstructure.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vishal on 17/9/16.
 */
public class SharedCouponDetailModel {

    @SerializedName("status")
    private String status = "";
    @SerializedName("data")
    private List<CouponModel> data;

    public String getStatus() {
        return status;
    }

    public List<CouponModel> getData() {
        return data;
    }

    public class CouponModel {

        @SerializedName("coupan_data")
        private SharedCouponDetailModel.CouponModel.CouponData coupan_data;

        public CouponData getCoupan_data() {
            return coupan_data;
        }

        public class CouponData {
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

            public String getImage() {
                return image;
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

            public String getLink() {
                return link;
            }

            public String getId() {
                return id;

            }
        }
    }
}
