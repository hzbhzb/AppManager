package appmanager.com.appmanager.multithreaddownload.demo.entity;

import java.io.Serializable;

/**
 * Created by Aspsine on 2015/7/8.
 */
public class AppInfo implements Serializable {
    /**
    * 未下载
    */
    public static final int STATUS_NOT_DOWNLOAD = 0;
    /**
     * 链接中
     */
    public static final int STATUS_CONNECTING = 1;
    /**
     * 下载链接错误
     */
    public static final int STATUS_CONNECT_ERROR = 2;
    /**
     * 下载中
     */
    public static final int STATUS_DOWNLOADING = 3;
    /**
     * 暂停下载
     */
    public static final int STATUS_PAUSED = 4;
    /**
     * 下载出错
     */
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    /**
     * 下载完成
     */
    public static final int STATUS_COMPLETE = 6;
    /**
     * 已安装
     */
    public static final int STATUS_INSTALLED = 7;
    /**
     * 已安装有可更新的安装包
     */
    public static final int STATUS_RENEWABLE = 8;

    private String name;
    private String packageName;
    private String id;
    private String image;
    private String url;
    private int progress;
    private String downloadPerSize;
    private String verCode;
    private int status;

    public AppInfo() {
    }

    public AppInfo(String id, String name, String image, String url) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.url = url;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Not Download";
            case STATUS_CONNECTING:
                return "Connecting";
            case STATUS_CONNECT_ERROR:
                return "Connect Error";
            case STATUS_DOWNLOADING:
                return "Downloading";
            case STATUS_PAUSED:
                return "Pause";
            case STATUS_DOWNLOAD_ERROR:
                return "Download Error";
            case STATUS_COMPLETE:
                return "Complete";
            case STATUS_INSTALLED:
                return "Installed";
            default:
                return "Not Download";
        }
    }

    public String getButtonText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Download";
            case STATUS_CONNECTING:
                return "Cancel";
            case STATUS_CONNECT_ERROR:
                return "Try Again";
            case STATUS_DOWNLOADING:
                return "Pause";
            case STATUS_PAUSED:
                return "Resume";
            case STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case STATUS_COMPLETE:
                return "Install";
            case STATUS_INSTALLED:
                return "UnInstall";
            case STATUS_RENEWABLE:
                return "Update";
            default:
                return "Download";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
