package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 16/11/7.
 */

public class PayResult implements Serializable {

    private String id;//结算id
    private String oridId;//订单id
    private String billNo;//结算编号
    private String state;//支付状态
    private String payDate;//交易日期
    private String payCorrName;
    private String payCorrInfo;
    private String payChanner;//支付方式



    public String getPayChanner() {
        return payChanner;
    }

    public void setPayChanner(String payChanner) {
        this.payChanner = payChanner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOridId() {
        return oridId;
    }

    public void setOridId(String oridId) {
        this.oridId = oridId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayCorrName() {
        return payCorrName;
    }

    public void setPayCorrName(String payCorrName) {
        this.payCorrName = payCorrName;
    }

    public String getPayCorrInfo() {
        return payCorrInfo;
    }

    public void setPayCorrInfo(String payCorrInfo) {
        this.payCorrInfo = payCorrInfo;
    }
}
