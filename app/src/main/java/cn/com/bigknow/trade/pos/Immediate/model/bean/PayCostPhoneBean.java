package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 2016/8/30..
 */


public class PayCostPhoneBean {
    /**
     * [必需]买方名称-手机号
     */
    public String corrName;

    public String corrInfo ;

    public String corrId;




    /**
     * 平台补贴
     */
    public String subsidyAmt;
    /**
     * 抹零
     */
    public String ignoreAmt;
    /**
     * 商户优惠金额
     */
    public String privilegeAmt;
    /**
     * 折扣比率..
     */
    public String discRate;


    public void setCorrInfo(String corrInfo) {
        this.corrInfo = corrInfo;
    }

    public String getCorrInfo() {
        return corrInfo;
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(String corrId) {
        this.corrId = corrId;
    }

    public String getCorrName() {
        return corrName;
    }

    public void setCorrName(String corrName) {
        this.corrName = corrName;
    }

    public String getSubsidyAmt() {
        return subsidyAmt;
    }

    public void setSubsidyAmt(String subsidyAmt) {
        this.subsidyAmt = subsidyAmt;
    }

    public String getIgnoreAmt() {
        return ignoreAmt;
    }

    public void setIgnoreAmt(String ignoreAmt) {
        this.ignoreAmt = ignoreAmt;
    }

    public String getPrivilegeAmt() {
        return privilegeAmt;
    }

    public void setPrivilegeAmt(String privilegeAmt) {
        this.privilegeAmt = privilegeAmt;
    }

    public String getDiscRate() {
        return discRate;
    }

    public void setDiscRate(String discRate) {
        this.discRate = discRate;
    }
}
