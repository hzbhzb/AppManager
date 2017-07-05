package appmanager.com.appmanager.bean;

import java.io.Serializable;

/**
 * Created by huangzhebin on 2017/6/17.
 */

public class ApkBean implements Serializable {

    private String proName;
    private String imgUrl;
    private String downUrl;

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public ApkBean() {

    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
