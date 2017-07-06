package appmanager.com.appmanager.bean;

import android.graphics.drawable.Drawable;
/**
 * 类名称：AppInfo
 * 类描述：应用程序类，包括了程序相关属性
 * 创建人：LXH
 * 创建时间：2013-10-21 上午10:30:00
 */
public class LocalAppInfo {
	private Drawable icon;
	private String appName;
	private String packageName;
	private boolean isSystemApp;
	private long codesize;
	private String iconUrl;
	private int iconResId;

	public int getIconResId() {
		return iconResId;
	}

	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public long getCodesize() {
		return codesize;
	}
	public void setCodesize(long codesize) {
		this.codesize = codesize;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

}