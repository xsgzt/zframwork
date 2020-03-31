package com.ztyb.framework.http.download;

import android.content.Context;

import java.io.File;
import java.util.Map;

public interface EngineDownLoadCallBack {
    EngineDownLoadCallBack DEFAULT_CALL_BACK = new EngineDownLoadCallBack() {
        @Override
        public void onPreExecute(Context var1, Map<String, Object> var2) {

        }

        @Override
        public void onError(Exception var1) {

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onResponse(long currenSize, long contenLeng) {

        }

        @Override
        public void onSuccess(File file) {

        }

    };

    void onPreExecute(Context var1, Map<String, Object> var2);
    void onError(Exception var1);
    void onComplete();
    void onResponse(long currenSize, long contenLeng);

    //直接返回文件
    void onSuccess(File file);
}
