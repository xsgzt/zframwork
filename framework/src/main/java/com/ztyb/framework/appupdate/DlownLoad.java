package com.ztyb.framework.appupdate;


import androidx.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DlownLoad {


    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public Executor THREAD_POOL_EXECUTOR;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();  //cpu
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;


    /**
     * 线程队列
     */
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(128);


    static {

    }

    private String mApkUrl;
    private String mAppNmae;
    private AppUpdateUtils mAppUpdateUtils;


    public DlownLoad(AppUpdateUtils appUpdateUtils,String appName, String url) {
        mAppUpdateUtils = appUpdateUtils;
        mApkUrl = url;
        mAppNmae = appName;
        THREAD_POOL_EXECUTOR = createThreadPool();

    }

    private ThreadPoolExecutor createThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,        //核心线程数
                MAXIMUM_POOL_SIZE,    //最大线程数
                KEEP_ALIVE_SECONDS,  //线程保活时间
                TimeUnit.SECONDS,      //时间单位
                sPoolWorkQueue,       //队列
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        return new Thread(r, "应用更新线程");
                    }
                });
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        return threadPoolExecutor;
    }


    /**
     * 下载
     */
    public void download() {

        THREAD_POOL_EXECUTOR.execute(new UpdateRunnable(mAppUpdateUtils, mApkUrl));
    }


}
