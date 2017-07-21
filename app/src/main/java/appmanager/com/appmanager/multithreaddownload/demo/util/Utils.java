package appmanager.com.appmanager.multithreaddownload.demo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.aspsine.multithreaddownload.util.ListUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Aspsine on 2015/7/29.
 */
public class Utils {

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    public static void onForwardToAccessibility(Context context) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        context.startActivity(intent);
    }
    public static void unInstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String getApkFilePackage(Context context, File apkFile) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (info != null){
            return info.applicationInfo.packageName;
        }
        return null;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (!ListUtils.isEmpty(packages)) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(packageName)) {

                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isNeedUpdate(Context context, String pkgName, String version) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        if (!ListUtils.isEmpty(packages)) {
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.packageName.equals(pkgName)) {
                    String ver = packageInfo.versionName;
                    if (TextUtils.isEmpty(ver))
                        System.out.println("ver is null");
                    if (TextUtils.isEmpty(version))
                        System.out.println("verion is null");
                    System.out.println("ver===" + ver);
                    System.out.println("version===" + version);
                    if (compareVersion(ver, version) == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }

        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");

        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;

        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index ++;
        }

        if (diff == 0) {
            for (int i = index; i < version1Array.length; i ++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i ++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }

            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }
    public static Intent isexit(Context context,String pk_name){
        PackageManager packageManager = context.getPackageManager();
        Intent it= packageManager.getLaunchIntentForPackage(pk_name);
        return it;
    }
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;

    }
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    public static int getStatusHeight(Context context) {
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight2;
    }

}
