package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * 资金流水记录
 * 2016/9/2 13:49
 */
public class FlowRecordBean implements Serializable {
    private String id;//": "流水ID",
    private String orgId;//": null,
    private String merchantId;//": null,
    private String transType;//": "W",
    private String transDate;//": "2016-09-02",
    private String accountDate;//": "2016-09-02 15:34:31",
    private String effectDate;//": null,
    private String corrName;//": null,
    private String accountNo;//": null,
    private String bankId;//": null,
    private String state;//": null,
    private String iyear;//": null,
    private String imonth;//": null,
    private String operator;//": null,
    private String operatorId;//": null,
    private double amt;//": 1,
    private double balAmt;//": 10079068,
    private String corrId;//": null,
    private String corrIdExt;//": null,
    private String description;//": null

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getAccountDate() {
        return accountDate;
    }

    public void setAccountDate(String accountDate) {
        this.accountDate = accountDate;
    }

    public double getAmt() {
        return amt;
    }

    public String getAmtStr() {
        return String.format("%.2f", amt);
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public double getBalAmt() {
        return balAmt;
    }
    public String getBalAmtStr() {
        return String.format("%.2f", balAmt);
    }
    public void setBalAmt(double balAmt) {
        this.balAmt = balAmt;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }

    public String getCorrName() {
        return corrName;
    }

    public void setCorrName(String corrName) {
        this.corrName = corrName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIyear() {
        return iyear;
    }

    public void setIyear(String iyear) {
        this.iyear = iyear;
    }

    public String getImonth() {
        return imonth;
    }

    public void setImonth(String imonth) {
        this.imonth = imonth;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(String corrId) {
        this.corrId = corrId;
    }

    public String getCorrIdExt() {
        return corrIdExt;
    }

    public void setCorrIdExt(String corrIdExt) {
        this.corrIdExt = corrIdExt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
