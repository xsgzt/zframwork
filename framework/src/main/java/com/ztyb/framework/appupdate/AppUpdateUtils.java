package com.ztyb.framework.appupdate;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztyb.framework.R;
import com.ztyb.framework.dialog.AlertDialog;
import com.ztyb.framework.utils.ScreenUtils;

import java.io.File;
import java.util.ArrayList;

public class AppUpdateUtils {
    private Context mContext;
    private String oldVersion;
    private String appNewVersion;
    private String versionDesc;
    private String appName;
    private String mApkUrl;
    private boolean mIsForceUpdate;
    private int mDilogLayout = R.layout.dialog_update_layout;
    private ArrayList<Intercept> interceptList = new ArrayList();

    DownloadLisenter defulteLisenter = new DownloadLisenter() {
        @Override
        public void startDownLoad() {

        }

        @Override
        public void progress(long currentProgress, long totalSize) {

        }

        @Override
        public void finishDownLoad(File file) {


        }

        @Override
        public void onFail() {

        }


    };
    DownloadLisenter mLisenter;

    public AppUpdateUtils(Context context, String oldVersion, String appNewVersion, String versionDesc, String appName, String url, boolean isForceUpdate) {
        mContext = context;
        this.oldVersion = oldVersion;
        this.appNewVersion = appNewVersion;
        this.versionDesc = versionDesc;
        this.appName = appName;
        mApkUrl = url;
        mIsForceUpdate = isForceUpdate;
    }


    public void setDilogLayout(int layout) {
        mDilogLayout = layout;
    }

    public void update(DownloadLisenter lisenter) {
        if (lisenter == null) {
            mLisenter = defulteLisenter;
        }
        mLisenter = lisenter;
        if (oldVersion.equals(appNewVersion)) {
            //无须跟新
            return;
        } else {
            showUpdateDialog(oldVersion, appNewVersion, versionDesc, appName);
        }

    }

    private void showUpdateDialog(String oldVersion, String appNewVersion, String versionDesc, String appName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setContentView(mDilogLayout)
                .setGravity(Gravity.CENTER)
                .setCancelable(false)
                .setWidthAndHeight((int) (ScreenUtils.getScreenWidth(mContext) * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

        final AlertDialog alertDialog = builder.create();


        TextView tv_msg = alertDialog.getView(R.id.tv_msg);
        tv_msg.setText(versionDesc);
        TextView tv_cancle = alertDialog.getView(R.id.tv_cancle);
        if (mIsForceUpdate) {
            tv_cancle.setVisibility(View.GONE);
        } else {
            tv_cancle.setVisibility(View.VISIBLE);
        }
        TextView tv_ok = alertDialog.getView(R.id.tv_ok);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowloadApk();
                alertDialog.dismiss();
            }


        });
        alertDialog.show();

    }


    private void dowloadApk() {
        if (TextUtils.isEmpty(mApkUrl)) {
            return;
        }


        new DlownLoad(this, appName, mApkUrl).download();

    }

    public DownloadLisenter getLisenter() {
        return mLisenter;
    }

    File mApkFile;

    public File selectFileName(long filesize) {
        File dir;
        if (ExistSDCard() && getSDFreeSize() > filesize) {
            //sdk 挂载和  可以用
            dir = new File(Environment.getExternalStorageDirectory() + File.separator + "huhu" + File.separator);

//            fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "huhu" + File.separator + "huhu" + ".apk";
        } else {
            //使用内部存储
            dir = mContext.getFilesDir();
//            fileName = mContext.getFilesDir().getAbsolutePath() + File.separator + "huhu" + ".apk";

        }


        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir.toString() + File.separator + "huhu.apk");
        if (file.exists()) {
            Log.e("selectFileName: ", "文件存在");
            file.delete();
        }

        return mApkFile = file;
    }


    public void addIntercept(Intercept intercept) {
        interceptList.add(intercept);
    }

    public File getFileName() {
        return mApkFile;
    }


    public ArrayList<Intercept> getInterceptList() {
        return interceptList;
    }

    //SD是否存在
    private boolean ExistSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    //SD剩余空间
    public long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }
}
