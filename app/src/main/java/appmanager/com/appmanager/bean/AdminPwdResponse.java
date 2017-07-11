package appmanager.com.appmanager.bean;

import java.util.List;

/**
 * Created by huangzhebin on 2017/7/11.
 */

public class AdminPwdResponse {

    /**
     * ts : 1499759576007
     * acc : launcher_telpo
     * status : 20001
     * desc : 执行成功
     * remark :
     * data : [{"id":20170711,"pwd":"623067"},{"id":20170712,"pwd":"970237"},{"id":20170713,"pwd":"383214"},{"id":20170714,"pwd":"400901"},{"id":20170715,"pwd":"879339"},{"id":20170716,"pwd":"633339"},{"id":20170717,"pwd":"823707"},{"id":20170718,"pwd":"276680"},{"id":20170719,"pwd":"148012"},{"id":20170720,"pwd":"929178"},{"id":20170721,"pwd":"658403"},{"id":20170722,"pwd":"805032"},{"id":20170723,"pwd":"778397"},{"id":20170724,"pwd":"649088"},{"id":20170725,"pwd":"217933"},{"id":20170726,"pwd":"331669"},{"id":20170727,"pwd":"536769"},{"id":20170728,"pwd":"584402"},{"id":20170729,"pwd":"141022"},{"id":20170730,"pwd":"820441"},{"id":20170731,"pwd":"834233"},{"id":20170801,"pwd":"722302"},{"id":20170802,"pwd":"465068"},{"id":20170803,"pwd":"470067"},{"id":20170804,"pwd":"876342"},{"id":20170805,"pwd":"246696"},{"id":20170806,"pwd":"541739"},{"id":20170807,"pwd":"953679"},{"id":20170808,"pwd":"393475"},{"id":20170809,"pwd":"501949"},{"id":20170810,"pwd":"547868"},{"id":20170811,"pwd":"296882"},{"id":20170812,"pwd":"866496"},{"id":20170813,"pwd":"718983"},{"id":20170814,"pwd":"110137"},{"id":20170815,"pwd":"333967"},{"id":20170816,"pwd":"632864"},{"id":20170817,"pwd":"053065"},{"id":20170818,"pwd":"644202"},{"id":20170819,"pwd":"456510"},{"id":20170820,"pwd":"669272"},{"id":20170821,"pwd":"013564"},{"id":20170822,"pwd":"635476"},{"id":20170823,"pwd":"611687"},{"id":20170824,"pwd":"064331"},{"id":20170825,"pwd":"921698"},{"id":20170826,"pwd":"245616"},{"id":20170827,"pwd":"978023"},{"id":20170828,"pwd":"541421"},{"id":20170829,"pwd":"626745"},{"id":20170830,"pwd":"504785"},{"id":20170831,"pwd":"824580"},{"id":20170901,"pwd":"192538"},{"id":20170902,"pwd":"741051"},{"id":20170903,"pwd":"341229"},{"id":20170904,"pwd":"763637"},{"id":20170905,"pwd":"557246"},{"id":20170906,"pwd":"230601"},{"id":20170907,"pwd":"882758"},{"id":20170908,"pwd":"757009"},{"id":20170909,"pwd":"008790"}]
     */

    private String ts;
    private String acc;
    private String status;
    private String desc;
    private String remark;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 20170711
         * pwd : 623067
         */

        private int id;
        private String pwd;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
