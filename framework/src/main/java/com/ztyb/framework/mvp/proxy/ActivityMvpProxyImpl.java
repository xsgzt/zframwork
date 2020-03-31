package com.ztyb.framework.mvp.proxy;

import com.ztyb.framework.mvp.base.BaseView;

public class ActivityMvpProxyImpl<V extends BaseView> extends MvpProxyImpl<V> implements ActivityMvpProxy {
    public ActivityMvpProxyImpl(V view) {
        super(view);
    }
}
