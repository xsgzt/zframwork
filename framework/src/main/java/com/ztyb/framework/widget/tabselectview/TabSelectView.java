package com.ztyb.framework.widget.tabselectview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ztyb.framework.R;


/**
 * Created by lenovo on 2017/9/3.
 */

public class TabSelectView extends LinearLayout implements View.OnClickListener {
    TabSelectViewObserver mTabSelectViewObserver;


    private Context mContext;
    //放置tab的容器
    private LinearLayout mTabContainr;
    //盛放阴影 和 内容容器的容器
    private FrameLayout mContainr;

    int SHAOM_BANCKGRUOUND = Color.parseColor("#222222");
    private FrameLayout mMeunContainr;

    private TabSelectAdapter mAdapter;  //适配器
    private View shaomView;
    private int mMeunHeight = dp2px(160);


    private int mOpenPisotion = -1;

    private boolean isNewInstance;

    private boolean isAnimatorIng;//是否在动画中

    public TabSelectView(Context context) {
        this(context, null);
    }

    public TabSelectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabSelectView);
        mMeunHeight = (int) typedArray.getDimension(R.styleable.TabSelectView_muen_height, mMeunHeight);

        typedArray.recycle();
        //初始化所有的容器
        init();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isNewInstance) {
            return;
        }
//        mContainr.setVisibility(GONE);
        shaomView.setVisibility(GONE);
        mMeunHeight = mMeunContainr.getMeasuredHeight();
        mMeunContainr.setVisibility(GONE);

        isNewInstance = true;
    }


    /**
     * 初始化布局
     */
    private void init() {
        //第一种方法是可以用xml 的形式来初始化的
        //由于这个布局比较简单我们使用  我们代码来实例
        mTabContainr = new LinearLayout(mContext);
        LayoutParams tabContainrlp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTabContainr.setLayoutParams(tabContainrlp);
        addView(mTabContainr);

        //初始化盛放内容容器和阴影的容器
        mContainr = new FrameLayout(mContext);
        LayoutParams mContainrLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainr.setLayoutParams(mContainrLp);
        addView(mContainr);


        //初始化阴影
        shaomView = new View(mContext);
        FrameLayout.LayoutParams shaomLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        shaomView.setLayoutParams(shaomLp);
        shaomView.setBackgroundColor(SHAOM_BANCKGRUOUND);
        mContainr.addView(shaomView);
        shaomView.setOnClickListener(this);
        //内容容器
        mMeunContainr = new FrameLayout(mContext);
        mMeunContainr.setOnClickListener(this);
        mMeunContainr.setBackgroundColor(Color.parseColor("#ffffff"));
//        FrameLayout.LayoutParams meunContainrLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMeunHeight);
        FrameLayout.LayoutParams meunContainrLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeunContainr.setLayoutParams(meunContainrLp);

        mContainr.addView(mMeunContainr);


    }


    class TabSelectViewObserverImpl extends TabSelectViewObserver {

        @Override
        public void notiCloseMeun() {
            closeMeun();
        }
    }

    /**
     * 添加内容和tab
     */
    private void addTabAndMeun() {
        if (mAdapter == null) {
            return;
        }
        if (mTabSelectViewObserver != null) {
            mAdapter.unregstebserver(mTabSelectViewObserver);
        }
        mTabSelectViewObserver = new TabSelectViewObserverImpl();
        mAdapter.regsterObserver(mTabSelectViewObserver);

        for (int i = 0; i < mAdapter.getConut(); i++) {
            //添加tab
            final View tabView = mAdapter.getTabView(i, mTabContainr);
            mTabContainr.addView(tabView);
            LayoutParams tabLp = (LayoutParams) tabView.getLayoutParams();
            tabLp.weight = 1;
            tabLp.gravity = Gravity.CENTER;
            tabView.setLayoutParams(tabLp);

            final int finalI = i;
            tabView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOpenPisotion == -1) {
                        //打开
                        openMeun(tabView, finalI);
                    } else {
                        mAdapter.closeMeun(mTabContainr.getChildAt(mOpenPisotion), mOpenPisotion, mTabContainr);
                        if (finalI == mOpenPisotion) {
                            closeMeun();
                        } else {
                            //切换
                            mMeunContainr.setVisibility(VISIBLE);
                            mMeunContainr.getChildAt(mOpenPisotion).setVisibility(GONE);
                            mOpenPisotion = finalI;
                            mAdapter.openMeun(tabView, mOpenPisotion, mTabContainr);
                            mMeunContainr.getChildAt(mOpenPisotion).setVisibility(VISIBLE);
                        }

                    }

                }


            });
            //添加menu
            View meunView = mAdapter.getMeunView(i, mMeunContainr);
            meunView.setVisibility(GONE);
            mMeunContainr.addView(meunView);


        }
    }


    /**
     * 关闭菜单
     */
    public void closeMeun() {
        if (isAnimatorIng) {
            return;
        }
        if (mOpenPisotion != -1) {
            mAdapter.closeMeun(mTabContainr.getChildAt(mOpenPisotion), mOpenPisotion, mTabContainr);
        }

        isAnimatorIng = true;
        ObjectAnimator alphAnimator = ObjectAnimator.ofFloat(shaomView, "alpha", 1, 0);
        alphAnimator.setDuration(350);
        alphAnimator.start();
        ObjectAnimator openAnimator = ObjectAnimator.ofFloat(mMeunContainr, "translationY", 0, -mMeunHeight);
        openAnimator.setDuration(350);
        openAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimatorIng = false;
                shaomView.setVisibility(GONE);
//                mContainr.setVisibility(GONE);
                if (mOpenPisotion != -1) {

                    mMeunContainr.getChildAt(mOpenPisotion).setVisibility(GONE);
                }
                mMeunContainr.setVisibility(GONE);
                mOpenPisotion = -1;
            }
        });
        openAnimator.start();

    }

    /**
     * 打开菜单
     *
     * @param tabView
     * @param finalI
     */
    private void openMeun(View tabView, int finalI) {
        if (isAnimatorIng) {
            return;
        }
        isAnimatorIng = true;
        Log.e("openMeun: ", finalI + "");
        shaomView.setVisibility(VISIBLE);
        mContainr.setVisibility(VISIBLE);
        mOpenPisotion = finalI;
        mMeunContainr.setVisibility(VISIBLE);
        mMeunContainr.getChildAt(mOpenPisotion).setVisibility(VISIBLE);
        ObjectAnimator alphAnimator = ObjectAnimator.ofFloat(shaomView, "alpha", (float)0.0, (float)0.5);
        alphAnimator.setDuration(350);
        alphAnimator.start();
        ObjectAnimator openAnimator = ObjectAnimator.ofFloat(mMeunContainr, "translationY", -mMeunHeight, 0);
        openAnimator.setDuration(350);
        openAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimatorIng = false;
            }
        });
        openAnimator.start();
        mAdapter.openMeun(tabView, finalI, mTabContainr);
    }


    public void setAdapter(TabSelectAdapter adapter) {
        mAdapter = adapter;
        //采用adpter模式来适配我们的tab
        addTabAndMeun();
    }

    @Override

    public void onClick(View v) {
        if (v == shaomView) {
            closeMeun();
        }

    }


    public void setContetntView(View view) {
        if (mContainr != null) {
            mContainr.addView(view, 0);
        }

    }


    private int dp2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getResources().getDisplayMetrics());
    }
}
