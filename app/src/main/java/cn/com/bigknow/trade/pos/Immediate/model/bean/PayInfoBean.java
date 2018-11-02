package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 2016/8/29 11:44
 */
public class PayInfoBean implements Serializable {
    private String oridId;
    private String payDate;
    private String amt;//支付金额
    private String payCorrName;//客户手机号
    private String state;//交易状态
    private String transType;//业务类型
    private String payChanner;
    private String payAmt;//应付金额


    public String getOridId() {
        return oridId;
    }

    public void setOridId(String oridId) {
        this.oridId = oridId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPayCorrName() {
        return payCorrName;
    }

    public void setPayCorrName(String payCorrName) {
        this.payCorrName = payCorrName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getPayChanner() {
        return payChanner;
    }

    public void setPayChanner(String payChanner) {
        this.payChanner = payChanner;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
    }
}
