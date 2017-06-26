package appmanager.com.appmanager;

/**
 * Created by huangzhebin on 2017/6/16.
 */

public class ApkResponse {

    /**
     * type : CLASSPAD_XX
     * typeName : 班级派
     * pkg : cn.dlj.classpad.xx
     * ver : 20005031
     * verName : 2.0.05031
     * path : https://res.test.dlj100.cn/apk/classpad_xx.2.0.05031.0.apk
     * md5 : 32bb11c8dfb97dae0c0badf4660be069
     * ptime : 2017-05-04 14:36:40
     * plog :
     * icon : https://res.dlj100.cn/img/CLASSPAD_XX.png
     */

    private String type;
    private String typeName;
    private String pkg;
    private String ver;
    private String verName;
    private String path;
    private String md5;
    private String ptime;
    private String plog;
    private String icon;

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

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPlog() {
        return plog;
    }

    public void setPlog(String plog) {
        this.plog = plog;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
