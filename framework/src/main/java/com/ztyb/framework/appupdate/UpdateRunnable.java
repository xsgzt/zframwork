package com.ztyb.framework.appupdate;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class UpdateRunnable implements Runnable {

    private String mUrl;
    ;
    private AppUpdateUtils mAppUpdateUtils;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int finalLengtsh;


    public UpdateRunnable(AppUpdateUtils appUpdateUtils, String url) {
        mUrl = url;
        mAppUpdateUtils = appUpdateUtils;
    }

    @Override
    public void run() {
        // 新建一个URL对象
        try {

//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            TrustManager[] tm = {new MyX509TrustManager()};
//            sslContext.init(null, tm, new java.security.SecureRandom());
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();








            SSLContext sslcontext = SSLContext.getInstance("SSL");//第一个参数为协议,第二个参数为提供者(可以缺省)
            TrustManager[] tm = {new MyX509TrustManager()};
            sslcontext.init(null, tm, new SecureRandom());
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslsession) {
                    System.out.println("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());




            URL url = new URL(mUrl);
            // 打开一个HttpURLConnection连接
            HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
//            urlConn.
//            urlConn.setSSLSocketFactory(sslSocketFactory);
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置为Post请求
//            urlConn.setRequestMethod("POST");
            urlConn.setRequestMethod("GET");

            urlConn.connect();

            final int contentLength = urlConn.getContentLength();
            int apkSizeM = contentLength / 1024 / 1024;
            Log.e("run: ", "" + apkSizeM);
            Log.e("run: ", "" + mUrl);

            //用于拦截判断 是否有文件

            final File apkFile = mAppUpdateUtils.selectFileName(apkSizeM);


            Log.e("run: ", apkFile.getAbsolutePath());
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                InputStream inputStream = urlConn.getInputStream();

                FileOutputStream os = new FileOutputStream(apkFile);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppUpdateUtils.getLisenter().startDownLoad();
                    }
                });


                int length;
                int lengtsh = 0;
                byte[] bytes = new byte[1024];

                while ((length = inputStream.read(bytes)) != -1) {
                    os.write(bytes, 0, length);
                    //获取当前进度值
                    lengtsh += length;

                    finalLengtsh = lengtsh;

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAppUpdateUtils.getLisenter().progress(finalLengtsh, contentLength);
                        }
                    });


//                    Log.e("run: ", "呼呼真在" + contentLength + "下载" + lengtsh);
                }
                //关闭流
                inputStream.close();
                os.close();
                os.flush();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppUpdateUtils.getLisenter().finishDownLoad(apkFile);

                    }
                });


                ArrayList<Intercept> interceptList = mAppUpdateUtils.getInterceptList();
                for (Intercept intercept : interceptList) {
                    intercept.intercept(apkFile);
                }


            } else {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppUpdateUtils.getLisenter().onFail();
                    }
                });
                Log.e("请求失败", "POST请求失败");
            }


        } catch (final Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAppUpdateUtils.getLisenter().onFail();
                    Log.e("run: ", "下载失败");
                }
            });

            e.printStackTrace();
        }


    }


}
