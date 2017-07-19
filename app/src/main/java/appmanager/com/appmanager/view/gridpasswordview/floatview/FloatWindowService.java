package appmanager.com.appmanager.view.gridpasswordview.floatview;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import appmanager.com.appmanager.MyApplication;

public class FloatWindowService extends Service {

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔0.5秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			// 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
			if (!isHome() && !MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
					}
				});
			}
			// 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
			else if (isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow(getApplicationContext());
						MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}
//			// 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
//			else if (isHome() && MyWindowManager.isWindowShowing()) {
//				handler.post(new Runnable() {
//					@Override
//					public void run() {
//						MyWindowManager.updateUsedPercent(getApplicationContext());
//					}
//				});
//			}
		}

	}

	/**
	 * 判断当前界面是否是桌面
	 */
	private boolean isHome() {
		if (MyApplication.allApkPkgNames.size() == 0) {
			System.out.println("allApkPkgName is null");
			return false;
		}
		System.out.println("allApkPkgNames ==" + MyApplication.allApkPkgNames.get(0));

		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		System.out.println("topActivity PkgName==" + getTopApp(getApplicationContext()));
		String topPkgName = getTopApp(getApplicationContext());
		return MyApplication.allApkPkgNames.contains(topPkgName) || TextUtils.equals(topPkgName, getPackageName()) || TextUtils.equals(topPkgName, "");
	}


	/**
	 * 获得属于桌面的自带的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			if (!MyApplication.allApkPkgNames.contains(ri.activityInfo.packageName))
			    names.add(ri.activityInfo.packageName);
		}
		return names;
	}
	private String getTopApp(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			UsageStatsManager m = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
			if (m != null) {
				long now = System.currentTimeMillis();
				//获取60秒之内的应用数据
				List<UsageStats> stats = m.queryUsageStats(UsageStatsManager.INTERVAL_BEST, now - 60 * 1000, now);
				Log.i("Top Activity", "Running app number in last 60 seconds : " + stats.size());

				String topActivity = "";

				//取得最近运行的一个app，即当前运行的app
				if ((stats != null) && (!stats.isEmpty())) {
					int j = 0;
					for (int i = 0; i < stats.size(); i++) {
						if (stats.get(i).getLastTimeUsed() > stats.get(j).getLastTimeUsed()) {
							j = i;
						}
					}
					topActivity = stats.get(j).getPackageName();
				}
				Log.i("Top Activity", "top running app is : "+topActivity);
				return topActivity;

			}
		}
		return "";
	}
}
