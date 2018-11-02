package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 16/11/22.
 */

public class Version {


    /**
     * id : LN5PNSBBNQW3NH1N2605XJO28WPFLVZD
     * appType : POS
     * version : 0.7
     * remark : 新版本，修复BUG
     * fileId : 6TXK2GX50ETU3VHU2C22I0ATE7U50CI4
     */

    private String id;
    private String appType;
    private double version;
    private String remark;
    private String fileId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
