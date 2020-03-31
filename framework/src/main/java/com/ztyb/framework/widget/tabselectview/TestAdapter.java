package com.ztyb.framework.widget.tabselectview;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ztyb.framework.R;

import java.util.List;

/**
 * Created by lenovo on 2017/9/3.
 */

public class TestAdapter extends TabSelectAdapter {
    private List<String> mData;
    private Context mContext;

    public TestAdapter(Context context, List<String> data) {
        mData = data;
        mContext = context;
    }

    @Override
    public int getConut() {
        return mData.size();
    }

    @Override
    public View getTabView(int poistion, ViewGroup parent) {
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tab_layout, null);
        view.setText(mData.get(poistion));
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public View getMeunView(int poistion, ViewGroup parent) {
        TextView tex = (TextView) LayoutInflater.from(mContext).inflate(R.layout.content, null);
        tex.setText(mData.get(poistion));
        tex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭菜单
                //1.第一种解决方式我们写监听回调
                //2.第二中方法可以在实例化adapter中的从activity中传入TabSeleectView的引用
                //利用观察着模式来解决掉 我们只要通知关闭就行了
                cloce();

            }
        });
        return tex;
    }

    @Override
    public void openMeun(View tabView, int position, ViewGroup parent) {
        TextView textView = (TextView) tabView;
        textView.setTextColor(Color.RED);
    }

    @Override
    public void closeMeun(View tabView, int position, ViewGroup parent) {
        TextView textView = (TextView) tabView;
        textView.setTextColor(Color.GREEN);
    }
}
