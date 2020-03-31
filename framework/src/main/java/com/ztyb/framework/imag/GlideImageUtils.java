package com.ztyb.framework.imag;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ztyb.framework.R;

public class GlideImageUtils {

    public static void loadImag(Context context, String url, ImageView view) {
        Glide.with(context.getApplicationContext()).load(url).error(R.drawable.erro).into(view);
    }
}
