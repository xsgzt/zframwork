package com.ztyb.framework.imag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.ztyb.framework.R;
import com.ztyb.framework.utils.Constant;
import com.ztyb.framework.utils.StatusBarUtils;

/**
 * Created by Administrator on 2018/6/8.
 */

public class ImageScaleActivity extends Activity {

    private String mImagUrl;
    @IdRes
    private int resId;
    private PhotoView mPvImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBar(this, R.color.white);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_iamgescale);
        parceIntent();

        initView();

    }


    private void initView() {
        mPvImage = (PhotoView) findViewById(R.id.pv_image);
        if (!TextUtils.isEmpty(mImagUrl)) {

            Glide.with(this).asBitmap().load(mImagUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    mPvImage.setImageBitmap(resource);
                }

            });

        }
        if(resId !=0) {
            mPvImage.setImageResource(resId);
        }




    }

    /**
     * 解析意图
     */
    private void parceIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constant.ACTIVTYPUTBUNDLEKEY);
        mImagUrl = bundle.getString(Constant.IMAGE_URL_KEY);
        if(TextUtils.isEmpty(mImagUrl)){
            resId = bundle.getInt(Constant.IMAGE_URL_KEY);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


}
