package com.ztyb.framework.appupdate;

import java.io.File;

public interface DownloadLisenter {
    void startDownLoad();

    void progress(long currentProgress, long totalSize);

    void finishDownLoad(File file);

    void onFail();

}
