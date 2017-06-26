package appmanager.com.appmanager.bean;

import java.io.Serializable;

/**
 * Created by huangzhebin on 2017/6/17.
 */

public class ApkBean implements Serializable {

    private String proName;
    private String imgUrl;

    public ApkBean(String proName, String imgUrl) {
        this.proName = proName;
        this.imgUrl = imgUrl;
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
