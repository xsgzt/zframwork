package com.ztyb.framework.mvp.base;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * presenter 基类
 *
 * @param <V>
 * @param <M>
 */
public class BasePresenter<V extends BaseView, M extends BaseModel> {
    private V mView;
    private V mProxyView;
    private M mModel;


    public void attach(V view) {
        mView = view;
        //动态代理

        mProxyView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (mView == null) {
                    //view已解绑
                    return null;
                }
                //view 没有解绑 执行相应的view方法
                return method.invoke(mView, args);

            }
        });

        //利用反射创建model 对象 获取类上的泛型 第二的真实类型

        Type types = getClass().getGenericSuperclass();

        Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();

        try {
            mModel = (M) ((Class) genericType[1]).newInstance();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }


    }

    public void detach() {
        this.mView = null;
    }


    public M getModel() {
        return mModel;
    }

    public V getView() {
        return mProxyView;
    }

}
