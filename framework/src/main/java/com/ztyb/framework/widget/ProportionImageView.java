package com.ztyb.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.ztyb.framework.R;


/**
 * ============================================================
 * author : HUI
 * <p>
 * version : 1.0
 * <p>
 * date created : On June, 2015
 * <p>
 * description : 自己设置view宽高比（一般会知道宽）, 不需要去适配了
 * <p>
 * revision history :
 * <p>
 * ============================================================
 */
public class ProportionImageView extends AppCompatImageView {
    private static final String TAG = "PIV";
    // 用float可能更好
    private float mWidthPro;
    private float mHeightPro;
    private int mCorners = 0;

    public ProportionImageView(Context context) {
        this(context, null);
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionImageView(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ProportionImageView);
        mWidthPro = array.getFloat(
                R.styleable.ProportionImageView_width_proportion, mWidthPro);
        mHeightPro = array.getFloat(
                R.styleable.ProportionImageView_height_proportion, mHeightPro);
        mCorners = (int) array.getDimension(R.styleable.ProportionImageView_corners, mCorners);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 先测量一下
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 用户没有设置宽高比就不处理
        if (mWidthPro == 0 || mHeightPro == 0) {
            return;
        }
        // 拿到宽
        int width = getMeasuredWidth();
        int height = (int) (width * (mHeightPro / mWidthPro));
        // 宽和高按比例设置
        setMeasuredDimension(width, height);
    }


}
