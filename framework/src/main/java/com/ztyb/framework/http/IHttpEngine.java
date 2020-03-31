package com.ztyb.framework.http;

import android.content.Context;


import com.ztyb.framework.http.download.EngineDownLoadCallBack;
import com.ztyb.framework.http.upload.EngineUploadCallBack;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18.
 * 网络引擎接口
 */

public interface IHttpEngine {
    void get(Context context, String url, Map<String, Object> params, HttpEngineCallBack callBack, boolean isCach);

    void post(Context context, String url, Map<String, Object> params, HttpEngineCallBack callBack, boolean isCach);

    void put(Context context, String url, Map<String, Object> params, HttpEngineCallBack callBack, boolean isCach);

    void delete(Context context, String url, Map<String, Object> params, HttpEngineCallBack callBack, boolean isCach);

    void postJson(Context context, String url, Map<String, Object> params, HttpEngineCallBack callBack, boolean isCach);

    void download(Context context, String url, File dir, String fileName, EngineDownLoadCallBack callBack);

    void upload(Context context, String url, Map<String, Object> params, EngineUploadCallBack callBack);






}
