package com.ztyb.framework.http;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.ztyb.framework.utils.Constant;
import com.ztyb.framework.utils.GeneralUtil;
import com.ztyb.framework.utils.PreferencesUtil;
import com.ztyb.framework.utils.SystemUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/7.
 */

public class HeadersIntercept implements Interceptor {

    private String token = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String param = (String) PreferencesUtil.getInstance().getParam(Constant.TOKEN, "");
        if (param != null) {
            token = param;
        }

//        Request request1 = request.newBuilder().addHeader("token", token).build();

        Context tag = (Context) request.tag();
        Request.Builder builder = request.newBuilder();
        if(!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", "Bearer "+token);
        }

        Request newRequest = builder.build();

        return chain.proceed(newRequest);
    }
}
