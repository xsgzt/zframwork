package com.ztyb.framework.appupdate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.ztyb.framework.utils.GeneralUtil;

import java.io.File;
import java.io.InputStream;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * 安装拦截器
 */
public class InstallIntercept implements Intercept {
    private Context mContext;
    private String mPageName;

    public InstallIntercept(Context context, String pageName) {
        mContext = context;
        mPageName = pageName;
    }

    @Override
    public void intercept(File apkFile) {
        installApkFile(apkFile);
    }

//    private void installApp(File apkFile) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Log.w("android", "版本大于 N ，开始使用 fileProvider 进行安装");
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            String absolutePath = apkFile.getAbsolutePath();
//            Log.e("installApp: ", absolutePath);
//            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".fileprovider", apkFile);
////            Uri contentUri = FileProvider.getUriForFile(
////                    mContext
////                    , mPageName + ".provider"   //.fileprovider
////                    , apkFile);
//            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        } else {
//            Log.w(TAG, "正常进行安装");
//            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//        }
//        mContext.startActivity(intent);
//    }


    public void installApkFile(File apkFile) {
        if (apkFile != null && apkFile.exists()) {
            try {
                String[] args2 = {"chmod", "777", apkFile.getAbsolutePath()};
                Runtime.getRuntime().exec(args2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(mContext.getApplicationContext(), mContext.getApplicationContext().getPackageName() + ".provider", apkFile);
                installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(apkFile);
            }
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.getApplicationContext().startActivity(installIntent);
        }
    }


}
