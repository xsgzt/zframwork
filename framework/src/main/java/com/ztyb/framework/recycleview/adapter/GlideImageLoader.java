package com.ztyb.framework.recycleview.adapter;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ztyb.framework.R;

/**
 * Created by Administrator on 2018/4/26.
 */

public class GlideImageLoader extends ImageLoader {

    public GlideImageLoader(String url) {
        super(url);
    }

    @Override
    public void load(ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(mImageUrl).error(R.drawable.erro).into(imageView);

    }
}
