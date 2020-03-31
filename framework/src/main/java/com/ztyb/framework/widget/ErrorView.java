package com.ztyb.framework.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ztyb.framework.R;


/**
 * Created by Administrator on 2018/4/23.
 */

public class ErrorView extends LinearLayout{
   private Context mContext;
    public ErrorView(Context context) {
        this(context,null);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext =context;
        initView();
    }

    private void initView() {

        View errorpage = LayoutInflater.from(mContext).inflate(R.layout.errorpage, this);

    }


}
