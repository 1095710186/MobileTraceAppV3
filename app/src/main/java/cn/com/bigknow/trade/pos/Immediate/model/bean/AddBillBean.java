package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 2016/9/9..
 */


public class AddBillBean implements Serializable {

    private String id;
    private String orgId;
    private String merchantId;
    private String orderType;
    private String billNo;
    private String billDate;
    private String iyear;//
    private String imonth;
    private String corrType;
    private String corrId;
    private String corrName;
    private float sumAmt;
    private float privilegeAmt; //
    private float ignoreAmt;
    private float discRate;
    private float discAmt;//
    private float payAmt;
    private float subsidyAmt;
    private String state;
    private float creditAmt;

    private float balAmt;//
    private String balState; //
    private String creator; //
    private String creatorId;//:
    private String createDate;//
    private String updateDate;//

    private String confirmer;//
    private String confirmerId; //
    private String confirmDate; //
    private String transAddr;//:
    private String receiveAddr;//
    private String receiveContacter;//

    private String receiveState;//
    private String billSrc; //
    private String origId; //
    private String origIdExt;//:
    private String mvpPayinfo;//:
    private String corrInfo;//:
    private String mvpOrderDet;//:
    private String description;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
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

    public String getCorrType() {
        return corrType;
    }

    public void setCorrType(String corrType) {
        this.corrType = corrType;
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

    public float getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(float sumAmt) {
        this.sumAmt = sumAmt;
    }

    public float getPrivilegeAmt() {
        return privilegeAmt;
    }

    public void setPrivilegeAmt(float privilegeAmt) {
        this.privilegeAmt = privilegeAmt;
    }

    public float getIgnoreAmt() {
        return ignoreAmt;
    }

    public void setIgnoreAmt(float ignoreAmt) {
        this.ignoreAmt = ignoreAmt;
    }

    public float getDiscRate() {
        return discRate;
    }

    public void setDiscRate(float discRate) {
        this.discRate = discRate;
    }

    public float getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(float discAmt) {
        this.discAmt = discAmt;
    }

    public float getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(float payAmt) {
        this.payAmt = payAmt;
    }

    public float getSubsidyAmt() {
        return subsidyAmt;
    }

    public void setSubsidyAmt(float subsidyAmt) {
        this.subsidyAmt = subsidyAmt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public float getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(float creditAmt) {
        this.creditAmt = creditAmt;
    }

    public float getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(float balAmt) {
        this.balAmt = balAmt;
    }

    public String getBalState() {
        return balState;
    }

    public void setBalState(String balState) {
        this.balState = balState;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer;
    }

    public String getConfirmerId() {
        return confirmerId;
    }

    public void setConfirmerId(String confirmerId) {
        this.confirmerId = confirmerId;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getTransAddr() {
        return transAddr;
    }

    public void setTransAddr(String transAddr) {
        this.transAddr = transAddr;
    }

    public String getReceiveAddr() {
        return receiveAddr;
    }

    public void setReceiveAddr(String receiveAddr) {
        this.receiveAddr = receiveAddr;
    }

    public String getReceiveContacter() {
        return receiveContacter;
    }

    public void setReceiveContacter(String receiveContacter) {
        this.receiveContacter = receiveContacter;
    }

    public String getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(String receiveState) {
        this.receiveState = receiveState;
    }

    public String getBillSrc() {
        return billSrc;
    }

    public void setBillSrc(String billSrc) {
        this.billSrc = billSrc;
    }

    public String getOrigId() {
        return origId;
    }

    public void setOrigId(String origId) {
        this.origId = origId;
    }

    public String getOrigIdExt() {
        return origIdExt;
    }

    public void setOrigIdExt(String origIdExt) {
        this.origIdExt = origIdExt;
    }

    public String getMvpPayinfo() {
        return mvpPayinfo;
    }

    public void setMvpPayinfo(String mvpPayinfo) {
        this.mvpPayinfo = mvpPayinfo;
    }

    public String getCorrInfo() {
        return corrInfo;
    }

    public void setCorrInfo(String corrInfo) {
        this.corrInfo = corrInfo;
    }

    public String getMvpOrderDet() {
        return mvpOrderDet;
    }

    public void setMvpOrderDet(String mvpOrderDet) {
        this.mvpOrderDet = mvpOrderDet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
