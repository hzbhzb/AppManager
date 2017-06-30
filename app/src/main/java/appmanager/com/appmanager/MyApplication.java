package appmanager.com.appmanager;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;

import appmanager.com.appmanager.multithreaddownload.demo.CrashHandler;

/**
 * Created by huangzhebin on 2017/6/16.
 */

public class MyApplication extends Application {

    private static RequestQueue requestQueue;
    public static int memoryCacheSize;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        memoryCacheSize = getMemoryCacheSize();
        sContext = getApplicationContext();
        initDownloader();
        CrashHandler.getInstance(sContext);
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    public static Context getContext() {
        return sContext;
    }
    public static RequestQueue getRequestQueue() {

        return requestQueue;
    }
    /**
     * @description
     *
     * @param context
     * @return 得到需要分配的缓存大小，这里用八分之一的大小来做
     */
    public int getMemoryCacheSize() {
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // Use 1/8th of the available memory for this memory cache.
        return maxMemory / 8;
    }
}
