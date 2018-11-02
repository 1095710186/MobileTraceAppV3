package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * 订单结算   laixiaoyang
 */
public class Binbean implements Serializable {
    /**
     * 刷卡
     */
    private String oridId;//结算订单ID
    private String origIdExt; //[如赊销结算]：订单赊销ID
    private String state;  //:[支Y成功/N失败]支付状态
    private String amt;//结算金额
    private String responseId; //结算ID,
    private String transType;//[O订单结算/A赊销结算]:结算类型;//
    private String responseTime;//:结算返回时间;
    private String payChanner;//:[C现金/N网银/A支付宝/T微信]支付渠道.;


    /**
     * 赊销
     */
    private String orderId;//:[必需]赊销订单ID;//
//    private String amt;//:[必需]赊销金额; //
    private String interAmt;//:到期利息; //
    private String arriveDate;//:到期还款时间;//:
   ////结算支付信息


    public String getOridId() {
        return oridId;
    }

    public void setOridId(String oridId) {
        this.oridId = oridId;
    }

    public String getOrigIdExt() {
        return origIdExt;
    }

    public void setOrigIdExt(String origIdExt) {
        this.origIdExt = origIdExt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getPayChanner() {
        return payChanner;
    }

    public void setPayChanner(String payChanner) {
        this.payChanner = payChanner;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInterAmt() {
        return interAmt;
    }

    public void setInterAmt(String interAmt) {
        this.interAmt = interAmt;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }
}
