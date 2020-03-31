package com.ztyb.framework.http;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.ztyb.framework.http.download.EngineDownLoadCallBack;
import com.ztyb.framework.http.upload.EngineUploadCallBack;
import com.ztyb.framework.http.upload.ProgressRequestListener;
import com.ztyb.framework.http.upload.UploadProgressRequstBody;
import com.ztyb.framework.log.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2018/4/18.
 * 该类封装了get post put delet 请求
 */

public class OkHttpEngine implements IHttpEngine {

    private Handler mHandler = new Handler();


    private static OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("HTTP_Interceptor", message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new HeadersIntercept())
            .readTimeout(5000, TimeUnit.SECONDS)
            .connectTimeout(5000, TimeUnit.SECONDS)
//            .addNetworkInterceptor(new HttpResponseIntercept())
            .build();


    @Override
    public void get(Context context, String url, Map<String, Object> params, final HttpEngineCallBack callBack, boolean isCach) {
        //get请求
        //拼接url
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(url).newBuilder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            httpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue() + "");
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        String finalUrl = httpUrl.toString();

        LogUtils.e("Get请求路径：", finalUrl);

        //判断是否有缓存
        // TODO: 2018/4/18


        Request request = new Request.Builder()
                .get()
                .tag(context)
                .url(finalUrl)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //失败   不在子线程中
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                //成功
                //解析一response下
                if (response.isSuccessful() || response.code() == 401 || response.code() == 406) {

                    try {
                        final String resultJson = response.body().string();
                        LogUtils.json("Get返回结果：", resultJson);
                        //成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(resultJson);
                            }
                        });

                    } catch (IOException e) {
                        //失败
                        onFailure(call, e);
                        e.printStackTrace();
                    }

                } else {
//                    失败
                    onFailure(call, new IOException("网络异常！"));
//
                }

            }
        });

    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final HttpEngineCallBack callBack, boolean isCach) {
        //post请求
        final String finalUrl = HttpUtils.jointParams(url, params);  //打印
        LogUtils.e("Post请求路径：", finalUrl);
        // 加密参数
//        String encryptParam = AESEncrypt.encrypt(params);


        Request request = new Request.Builder()
                .tag(context)
                .url(url)
                .post(createPostBody(params))
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //失败   不在子线程中
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                //解析一response下
//
                if (response.isSuccessful()) {

                    try {
                        final String resultJson = response.body().string();
                        LogUtils.json("Post返回结果：", resultJson);
                        //成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("onrepomnse: ", resultJson);
                                callBack.onSuccess(resultJson);
                            }
                        });

                    } catch (IOException e) {
                        //失败
                        onFailure(call, e);
                        e.printStackTrace();
                    }

                } else {
                    //失败
                    onFailure(call, new IOException("网络异常！"));

                }

            }
        });


    }

    /**
     * 表单提交的请求体
     *
     * @param params
     * @return
     */
    private RequestBody createPostBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.parse("multipart/form-data"));
        if (!params.isEmpty()) {
            addParams(builder, params);
        }
        return builder.build();
    }


    //拼接参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject();
        ;
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof File) {
                    //是文件
                    File file = (File) value;
                    //可能是image  text  等 所以要猜测文件
                    builder.addFormDataPart(key
                            , file.getName()
                            , RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));

                } else if (value instanceof List) {
                    //提交的是文件集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (File file : listFiles) {
                            builder.addFormDataPart(key, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        //多文件上传失败
                        e.printStackTrace();
                    }

                } else {
                    //普通的post请求
                    String finalValue = String.valueOf(value);
                    builder.addFormDataPart(key, finalValue + "");
                    // key+value
                }


            }


        }
    }


    /**
     * 猜测文件类型
     *
     * @param path
     * @return
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }

        return contentTypeFor;
    }

    @Override
    public void put(Context context, String url, Map<String, Object> params, final HttpEngineCallBack callBack, boolean isCach) {

        RequestBody body = craetePostJsonBody(params);
        Request request = new Request.Builder()
                .tag(context)
                .url(url)
                .put(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //失败   不在子线程中
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        final String resultJson = response.body().string();
                        LogUtils.json("put返回结果：", resultJson);
                        //成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("onrepomnse: ", resultJson);
                                callBack.onSuccess(resultJson);
                            }
                        });

                    } catch (IOException e) {
                        //失败
                        onFailure(call, e);
                        e.printStackTrace();
                    }

                } else {
                    //失败
                    onFailure(call, new IOException("网络异常！"));

                }

            }
        });
    }


    @Override
    public void delete(Context context, String url, Map<String, Object> params, final HttpEngineCallBack callBack, boolean isCach) {
//        RequestBody body = craetePostJsonBody(params);
        StringBuilder stringBuilder = new StringBuilder(url);
        for (Map.Entry<String, Object> en : params.entrySet()) {
            stringBuilder.append("/").append(en.getKey());
        }
        url = stringBuilder.toString();

        Request request = new Request.Builder()
                .tag(context)
                .url(url)
                .delete()
//                .delete(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //失败   不在子线程中
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        final String resultJson = response.body().string();
                        LogUtils.json("delete返回结果：", resultJson);
                        //成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("onrepomnse: ", resultJson);
                                callBack.onSuccess(resultJson);
                            }
                        });

                    } catch (IOException e) {
                        //失败
                        onFailure(call, e);
                        e.printStackTrace();
                    }

                } else {
                    //失败
                    onFailure(call, new IOException("网络异常！"));

                }

            }
        });


    }

    /**
     * post 提交json 格式 数据
     *
     * @param context
     * @param url
     * @param params
     * @param callBack
     * @param isCach
     */
    @Override
    public void postJson(Context context, String url, Map<String, Object> params, final HttpEngineCallBack callBack, boolean isCach) {
        RequestBody requestBody = craetePostJsonBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() || response.code() == 401 || response.code() == 406) {

                    try {
                        String token = response.header("Authorization");
                        String bodyStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(bodyStr);
                        if (TextUtils.isEmpty(token)) {
                            token = "";
                        }
                        jsonObject.put("token", token);
                        final String resultJson = jsonObject.toString();
                        LogUtils.json("postJson返回结果：", resultJson);
                        //成功
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(resultJson);
                            }
                        });

                    } catch (IOException e) {
                        //失败
                        onFailure(call, e);
                        e.printStackTrace();
                    } catch (Exception ex) {

                    }

                } else {
                    //失败
                    onFailure(call, new IOException("网络异常！"));

                }

            }
        });


    }


    /**
     * 下载文件
     *
     * @param context
     * @param url
     * @param callBack
     */
    @Override
    public void download(Context context, String url, final File destFileDir, final String fileName, final EngineDownLoadCallBack callBack) {
        Request request = new Request.Builder()

                .url(url)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(360, TimeUnit.SECONDS)
                .readTimeout(360, TimeUnit.SECONDS)
                .writeTimeout(360, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //下载文件

                if (!destFileDir.exists()) {
                    destFileDir.mkdirs();
                }
                final File file = new File(destFileDir, fileName);


                InputStream is = response.body().byteStream(); //文件流
                final long total = response.body().contentLength();   //文件长度

                //读取流
                FileOutputStream os = new FileOutputStream(file);
                long sum = 0;
                byte[] buf = new byte[2048];
                int len = 0;

                while ((len = is.read(buf)) != -1) {
                    os.write(buf, 0, len);
                    sum += len;
                    //回调下载进度
                    final long finalSum = sum;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onResponse(finalSum, total);
                        }
                    });

                }
                os.flush();
                is.close();
                os.close();
                //下载完毕
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(file);
                    }
                });

            }
        });


    }

    /**
     * @param context
     * @param url
     * @param params
     * @param callBack
     */
    @Override
    public void upload(Context context, String url, Map<String, Object> params, final EngineUploadCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);  //打印
        LogUtils.e("上传路径：", jointUrl);

        RequestBody requestBody = createPostBody(params);


        Request request = new Request.Builder()
                .tag(context)
                .url(url)
                .post(new UploadProgressRequstBody(requestBody, new ProgressRequestListener() {
                    @Override
                    public void onRequestProgress(final long bytesWritten, final long contentLength, boolean done) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onResponse(bytesWritten, contentLength);
                                if (bytesWritten / contentLength == 1) {
                                    callBack.onComplete();
                                }
                            }
                        });

                    }
                }))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(360, TimeUnit.SECONDS)
                .readTimeout(360, TimeUnit.SECONDS)
                .writeTimeout(360, TimeUnit.SECONDS)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String resultJson = response.body().string();
                LogUtils.e("json->", resultJson);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(resultJson);
                    }
                });
            }
        });

    }


    /**
     * 构建请求体
     *
     * @param params
     * @return
     */
    private RequestBody craetePostJsonBody(Map<String, Object> params) {
        //可以利用butterknife思想 自动生成一个jsonBean类
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String requestJson = jsonObject.toString();

        LogUtils.json(requestJson);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestJson);
        return body;
    }


    /**
     * 取消
     */
    public static void cancel(Context context) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        //
        List<Call> queuCalls = dispatcher.queuedCalls();
        synchronized (dispatcher) {
            for (Call call : queuCalls) {
                if (context.equals(call.request().tag())) {
                    call.cancel();
                }

            }

            List<Call> runCalls = dispatcher.runningCalls();
            for (Call runCall : runCalls) {
                if (context.equals(runCall.request().tag())) {
                    runCall.cancel();
                }
            }
        }
    }
}



