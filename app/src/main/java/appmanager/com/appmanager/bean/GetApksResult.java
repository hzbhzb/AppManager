package appmanager.com.appmanager.bean;

import java.util.List;

import appmanager.com.appmanager.ApkResponse;

/**
 * Created by huangzhebin on 2017/6/28.
 */

public class GetApksResult {


    /**
     * ts : 1498639413169
     * acc : launcher_telpo
     * status : 20001
     * desc : 执行成功
     * remark :
     * data : [{"type":"DLJ","typeName":"动力加","dtype":"CLASSPAD_XX","dtypeName":"班级派","name":"班级派","pkg":"cn.dlj.classpad.xx","ver":"20061300","verName":"2.0.06130.0","path":"https://res.test.dlj100.cn/apk/cn.dlj.classpad.xx.20061300.apk","logo":"https://res.test.dlj100.cn/apk/cn.dlj.classpad.xx.20061300.png","md5":"c70f9a918ba06fa681ffe07cfe965761","time":"2017-06-19 19:12:32","rmk":""},{"type":"DLJ","typeName":"动力加","dtype":"YOUER_14","dtypeName":"动力宝贝横屏版","name":"动力宝贝(横屏版)","pkg":"cn.dlj.youer","ver":"1104210.0","verName":"1.1.04210.0","path":"https://res.test.dlj100.cn/apk/cn.dlj.youer.1104210.0.apk","logo":"https://res.test.dlj100.cn/apk/cn.dlj.youer.1104210.0.png","md5":"70a7eb2ec55170ed4f013ab642749bd7","time":"2017-06-19 19:14:50","rmk":""},{"type":"DLJ","typeName":"动力加","dtype":"YOUER_21","dtypeName":"动力宝贝竖屏版","name":"动力宝贝(竖屏版)","pkg":"cn.dlj.youer","ver":"11042100","verName":"1.1.04210.0","path":"https://res.test.dlj100.cn/apk/cn.dlj.youer.11042100.apk","logo":"https://res.test.dlj100.cn/apk/cn.dlj.youer.11042100.png","md5":"2bc0a3ddf55482fc94b5adba96ec4358","time":"2017-06-19 19:14:01","rmk":""},{"type":"THIRD","typeName":"第三方","dtype":"","dtypeName":"","name":"谷歌拼音","pkg":"com.google.android.inputmethod.pinyin","ver":"4.3.3.138040165","verName":"4.3.3.138040165","path":"https://res.test.dlj100.cn/apk/com.google.android.inputmethod.pinyin.4.3.3.138040165.apk","logo":"https://res.test.dlj100.cn/apk/com.google.android.inputmethod.pinyin.4.3.3.138040165.png","md5":"546e5ff5c280c605cc6e44964bcf785e","time":"2017-06-19 19:16:14","rmk":""},{"type":"THIRD","typeName":"第三方","dtype":"","dtypeName":"","name":"讯飞语音+","pkg":"com.iflytek.speechcloud","ver":"1.1.1045","verName":"1.1.1045","path":"https://res.test.dlj100.cn/apk/com.iflytek.speechcloud.1.1.1045.apk","logo":"https://res.test.dlj100.cn/apk/com.iflytek.speechcloud.1.1.1045.png","md5":"42df795a082d25aa87db38eb319ab45d","time":"2017-06-19 19:17:21","rmk":""}]
     */

    private String ts;
    private String acc;
    private String status;
    private String desc;
    private String remark;
    private List<ApkResponse> data;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ApkResponse> getData() {
        return data;
    }

    public void setData(List<ApkResponse> data) {
        this.data = data;
    }


}
