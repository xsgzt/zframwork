package com.ztyb.framework.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.ztyb.framework.utils.AppManagerUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {
    private Activity mActivity;
    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    private ExceptionCrashHandler(Activity activity) {
        mActivity = activity;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        Thread.currentThread();
        mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("ExceptionCrashHandler", "报异常了~");
        String crashFileName = this.saveInfoToSD(ex);
        this.cacheCrashFile(crashFileName);
        AppManagerUtil.instance().finishAllActivity();
        this.mDefaultExceptionHandler.uncaughtException(thread, ex);
//        this.mActivity.finish();

        this.mActivity = null;

    }

    private void cacheCrashFile(String fileName) {
        SharedPreferences sp = this.mActivity.getSharedPreferences("crash", 0);
        sp.edit().putString("CRASH_FILE_NAME", fileName).commit();
    }

    public File getCrashFile() {
        String crashFileName = this.mActivity.getSharedPreferences("crash", 0).getString("CRASH_FILE_NAME", "");
        return new File(crashFileName);
    }

    private String saveInfoToSD(Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();
        Iterator var4 = this.obtainSimpleInfo(this.mActivity).entrySet().iterator();

        while (var4.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var4.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }

        sb.append(this.obtainExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals("mounted")) {
//            File dir = new File(this.mActivity.getFilesDir() + File.separator + "crash" + File.separator);
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "crash" + File.separator);

            if (dir.exists()) {
                this.deleteDir(dir);
            }

            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                fileName = dir.toString() + File.separator + this.getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        return fileName;
    }


    private String getAssignTime(String dateFormatStr) {
        DateFormat dataFormat = new SimpleDateFormat(dateFormatStr);
        long currentTime = System.currentTimeMillis();
        return dataFormat.format(Long.valueOf(currentTime));
    }


    @SuppressLint("WrongConstant")
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;

        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 1);
        } catch (PackageManager.NameNotFoundException var6) {
            var6.printStackTrace();
        }

        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        map.put("MOBLE_INFO", getMobileInfo());
        return map;
    }

    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();

        try {
            Field[] fields = Build.class.getDeclaredFields();
            Field[] var2 = fields;
            int var3 = fields.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Field field = var2[var4];
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get((Object) null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return sb.toString();
    }

    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            File[] var3 = children;
            int var4 = children.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                File child = var3[var5];
                child.delete();
            }
        }

        return true;
    }

    public static void attach(Activity activity) {
        new ExceptionCrashHandler(activity);
    }
}

