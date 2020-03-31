package com.ztyb.framework.appupdate;

import java.io.File;
import java.io.InputStream;

public interface Intercept {

    void intercept(File apkFile);
}
