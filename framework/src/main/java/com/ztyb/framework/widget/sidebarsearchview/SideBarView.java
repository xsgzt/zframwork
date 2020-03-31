package com.ztyb.framework.widget.sidebarsearchview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SideBarView extends View {

    public static String[] itmes = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private int painColor = Color.parseColor("#999999");
    private Paint mPaint;
    private int mWidthSize;
    private int mHeightSize;
    private double mItmeHeight;

    public SideBarView(Context context) {
        this(context, null);
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(painColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextSize(sp2px(11));

    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    /**
     * 测量 空间的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        mHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidthSize, mHeightSize);
    }


    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mItmeHeight = mHeightSize * 1.0 / itmes.length;

        for (int i = 0; i < itmes.length; i++) {
            Rect rect = new Rect();
            mPaint.getTextBounds(itmes[i], 0, itmes[i].length(), rect);
            canvas.drawText(itmes[i], mWidthSize / 2 - (rect.width() / 2), (float) ((mItmeHeight / 2) + (mItmeHeight * i)) + getBaseline(mPaint), mPaint);
        }

    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *
     * @param p
     * @return 基线和centerY的距离
     */
    public static float getBaseline(Paint p) {
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

    float y = 0;
    int index = 0;
    int oldIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                //计算出索引
                index = (int) (y / mItmeHeight);
                break;
            case MotionEvent.ACTION_MOVE:

                y = event.getY();
                index = (int) (y / mItmeHeight);
                break;
            case MotionEvent.ACTION_UP:
                y = event.getY();
                index = (int) (y / mItmeHeight);
                oldIndex = -1;
                if (mOnTouchListener != null) {
                    mOnTouchListener.touchText(itmes[index], View.GONE);
                }
                break;
        }

        //回调出去
        if (mOnTouchListener != null) {
            if ((index >= 0 && index < itmes.length) && oldIndex != index && event.getAction() != MotionEvent.ACTION_UP) {
                mOnTouchListener.touchText(itmes[index], View.VISIBLE);
                Log.e("onTouchEvent: ", itmes[index]);
            }
        }
        oldIndex = index;
        return true;
    }

    private OnTouchListener mOnTouchListener;

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public interface OnTouchListener {
        void touchText(String s, int visiblity);
    }


}
