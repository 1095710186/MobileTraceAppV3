package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by wushiwei on 2016/11/4.
 */

public class PaymentInformationBean implements Serializable {

    private String oridId;//订单ID
    private String payAmt;//应付金额
    private String payDate; //结算日期
    private String amt; // 支付金额
    private String responseId;//结算响应流水ID
    private String payCorrName;//  客户手机号payCorrNa
    private String state; //交易状态，Y-成功，N-失败
    private String accountNo; // //付款账号
    private String bankId; //  付款银行
    private String transType;//业务类型，O-订单结算，A-赊销结算
    private String payChanner; // 交易渠道，C-现金，N-网银，P-刷卡
    private String responseTime; //  结算响应时间

    public String getOridId() {
        return oridId;
    }

    public void setOridId(String oridId) {
        this.oridId = oridId;
    }

    public String getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(String payAmt) {
        this.payAmt = payAmt;
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

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
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

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
