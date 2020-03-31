package com.ztyb.framework.widget.tabselectview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/9/3.
 */

public abstract class TabSelectAdapter {

    TabSelectViewObserver mTabSelectViewObserver;
    //注册监听者
    public void regsterObserver(TabSelectViewObserver observer){
        mTabSelectViewObserver = observer;
    }

    public void unregstebserver(TabSelectViewObserver observer){
        mTabSelectViewObserver = null;
    }

    //获取几个tab
    public abstract int getConut();

    //获取对应位置的tabView
    public abstract View getTabView(int poistion, ViewGroup parent);

    //获取菜单的view
    public abstract View getMeunView(int poistion, ViewGroup parent);


    public abstract void openMeun(View tabView, int position, ViewGroup parent);

    public abstract void closeMeun(View tabView, int position, ViewGroup parent);

    public void cloce(){
        mTabSelectViewObserver.notiCloseMeun();
    }
}
