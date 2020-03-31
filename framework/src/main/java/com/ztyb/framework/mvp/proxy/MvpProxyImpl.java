package com.ztyb.framework.mvp.proxy;

import com.ztyb.framework.mvp.base.BasePresenter;
import com.ztyb.framework.mvp.base.BaseView;
import com.ztyb.framework.mvp.inject.InjectPresenter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理的实现
 */
public class MvpProxyImpl<V extends BaseView> implements IMvpProxy {
    private V mView;
    private List<BasePresenter> mPresenters;

    public MvpProxyImpl(V view) {
        mView = view;
        mPresenters = new ArrayList<>();
        crearePresenter();
    }

    @Override
    public void crearePresenter() {
        Field[] fields = mView.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            InjectPresenter injectPresenter = field.getAnnotation(InjectPresenter.class);
            if (injectPresenter != null) {
                Class<? extends BasePresenter> presenterClazz = null;
                try {
                    presenterClazz = (Class<? extends BasePresenter>) field.getType();
                } catch (Exception e) {
                    throw new RuntimeException("注入异常");
                }


                try {
                    BasePresenter basePresenter = presenterClazz.newInstance();

                    basePresenter.attach(mView);

                    field.set(mView, basePresenter);

                    mPresenters.add(basePresenter);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }


        }


    }

    @Override
    public void unBindPresenter() {
        for (BasePresenter mPresenter : mPresenters) {
            mPresenter.detach();
        }
    }
}
