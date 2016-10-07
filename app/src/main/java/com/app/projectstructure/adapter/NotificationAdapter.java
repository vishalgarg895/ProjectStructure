package com.app.projectstructure.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projectstructure.R;
import com.app.projectstructure.model.SharedCouponDetailModel;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<SharedCouponDetailModel.CouponModel> couponArrayList;
    private Context                                   mContext;
    private Activity                                  mActivity;

    public NotificationAdapter(Context mContext, List<SharedCouponDetailModel.CouponModel> couponArrayList) {
        this.couponArrayList = couponArrayList;
        this.mContext = mContext;
        mActivity = (Activity) mContext;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.facebook_login_fragment, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {
        SharedCouponDetailModel.CouponModel couponModel    = couponArrayList.get(position);
        String                              couponDiscount = couponModel.getCoupan_data().getDiscount();
        String                              couponDesc     = couponModel.getCoupan_data().getCoupon_description();
        String                              imageName      = couponModel.getCoupan_data().getImage();
        String                              couponTitle    = couponModel.getCoupan_data().getCoupan_title();

//        Glide.with(mContext).load(AppConstant.COUPON_IMAGES_BASE_URL + imageName)
//                .bitmapTransform(new CropCircleTransformation(mContext))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.mCouponImageIv);

        holder.mCouponDescTv.setText(couponDesc);
//        holder.mCouponTitleTv.setText(couponTitle);

        holder.mCouponTitleTv.setText(couponTitle);
        if (couponDiscount.trim().matches("") || couponDiscount.trim().matches("0")) {
            holder.mCouponDiscountTv.setVisibility(View.GONE);
        } else {
            holder.mCouponDiscountTv.setVisibility(View.VISIBLE);
            holder.mCouponDiscountTv.setText(couponDiscount + "%" + "\n off");
        }
    }

    @Override
    public int getItemCount() {
        return couponArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  mCouponDiscountTv;
        public TextView  mCouponTitleTv;
        public TextView  mCouponDescTv;
        public ImageView mCouponImageIv;

        public ViewHolder(View v) {
            super(v);
            // @formatter:off
//            mCouponDiscountTv = (TextView)  v.findViewById(R.id.tv_discount);
//            mCouponDescTv     = (TextView)  v.findViewById(R.id.tv_coupon_desc);
//            mCouponTitleTv    = (TextView)  v.findViewById(R.id.tv_coupon_title);
//            mCouponImageIv    = (ImageView) v.findViewById(R.id.img_coupon_img);
            // @formatter:on
        }
    }

}
