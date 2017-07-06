package appmanager.com.appmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangzhebin on 2017/6/16.
 */

public class ApkResponse implements Parcelable {

    /**
     * type : DLJ
     * typeName : 动力加
     * dtype : CLASSPAD_XX
     * dtypeName : 班级派
     * name : 班级派
     * pkg : cn.dlj.classpad.xx
     * ver : 20061300
     * verName : 2.0.06130.0
     * path : https://res.test.dlj100.cn/apk/cn.dlj.classpad.xx.20061300.apk
     * logo : https://res.test.dlj100.cn/apk/cn.dlj.classpad.xx.20061300.png
     * md5 : c70f9a918ba06fa681ffe07cfe965761
     * time : 2017-06-19 19:12:32
     * rmk :
     */

    private String type;
    private String typeName;
    private String dtype;
    private String dtypeName;
    private String name;
    private String pkg;
    private String ver;
    private String verName;
    private String path;
    private String logo;
    private String md5;
    private String time;
    private String rmk;

    protected ApkResponse(Parcel in) {
        type = in.readString();
        typeName = in.readString();
        dtype = in.readString();
        dtypeName = in.readString();
        name = in.readString();
        pkg = in.readString();
        ver = in.readString();
        verName = in.readString();
        path = in.readString();
        logo = in.readString();
        md5 = in.readString();
        time = in.readString();
        rmk = in.readString();
    }

    public static final Creator<ApkResponse> CREATOR = new Creator<ApkResponse>() {
        @Override
        public ApkResponse createFromParcel(Parcel in) {
            return new ApkResponse(in);
        }

        @Override
        public ApkResponse[] newArray(int size) {
            return new ApkResponse[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDtypeName() {
        return dtypeName;
    }

    public void setDtypeName(String dtypeName) {
        this.dtypeName = dtypeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(typeName);
        dest.writeString(dtype);
        dest.writeString(dtypeName);
        dest.writeString(name);
        dest.writeString(pkg);
        dest.writeString(ver);
        dest.writeString(verName);
        dest.writeString(path);
        dest.writeString(logo);
        dest.writeString(md5);
        dest.writeString(time);
        dest.writeString(rmk);
    }
}
