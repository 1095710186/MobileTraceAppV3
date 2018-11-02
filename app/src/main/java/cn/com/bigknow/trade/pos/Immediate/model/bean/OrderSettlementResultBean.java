package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * 2016/9/13 11:57
 * 结算入账/赊销入账返回
 */

/**
 * {"payCorrId":"297ebe0e560b972901560c1338be0009","
 * billDate":"2016-09-13","creatorId":"297ebe0e560b972901562510a1b900fd",
 * "state":"N","payCorrName":"13551027292","creator":"杨",
 * "balAmt":0.00,"id":"LE7YFN6DRIDLBVHE3X8RHL213ZECCX83","amt":2.0,
 * "billNo":"DEVF20160913000006","createDate":"2016-09-13 15:58:58",
 * "orderId":"MJ1IXTFRU7DV0CFSTHZC7L2KBEX9LHGF"}
 *
 * //s赊销
 * {"state":"Y","receiveCorrName":"测试商户",
 * "receiveCorrType":"M","id":"HF2D2KKWLO3BQN9FVS30CFOEVG1HB5G","
 * billNo":"DEVE20160913000037","oridId":"7R05FKFPXR5YYNWACCHTRKL5F135WNCA",
 * "payCorrId":"297ebe0e560b972901560c1338be0009",
 * "payCorrName":"13551027292","amt":44.0,
 * "transType":"O","payCorrType":"M",
 * "receiveCorrId":"7R05FKFPXR5YYNWACCHTRKL5F135WNCA"}
 */
public class OrderSettlementResultBean implements Serializable {
    private String payCorrId;
    private String billDate;
    private String creatorId;
    private String state;
    private String payCorrName;
    private String creator;
    private String id;
    private float balAmt;
    private float amt;
    private float discAmt;
    private String createDate; //
    private String billNo;
    private String orderId;

    private String orderNo;// d订单编号

    private String receiveCorrId;
    private String payCorrType;
    private String transType;
//    private String payCorrName;
//    private String payCorrId;
    private String receiveCorrType;
    private String receiveCorrName;
    private String oridId;


    private String payCorrInfo;

    private String type; // Y s刷卡；N ;赊销

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayCorrInfo() {
        return payCorrInfo;
    }

    public void setPayCorrInfo(String payCorrInfo) {
        this.payCorrInfo = payCorrInfo;
    }

    public String getPayCorrId() {
        return payCorrId;
    }

    public void setPayCorrId(String payCorrId) {
        this.payCorrId = payCorrId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayCorrName() {
        return payCorrName;
    }

    public void setPayCorrName(String payCorrName) {
        this.payCorrName = payCorrName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(float balAmt) {
        this.balAmt = balAmt;
    }

    public float getAmt() {
        return amt;
    }

    public void setAmt(float amt) {
        this.amt = amt;
    }

    public float getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(float discAmt) {
        this.discAmt = discAmt;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReceiveCorrId() {
        return receiveCorrId;
    }

    public void setReceiveCorrId(String receiveCorrId) {
        this.receiveCorrId = receiveCorrId;
    }

    public String getPayCorrType() {
        return payCorrType;
    }

    public void setPayCorrType(String payCorrType) {
        this.payCorrType = payCorrType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getReceiveCorrType() {
        return receiveCorrType;
    }

    public void setReceiveCorrType(String receiveCorrType) {
        this.receiveCorrType = receiveCorrType;
    }

    public String getReceiveCorrName() {
        return receiveCorrName;
    }

    public void setReceiveCorrName(String receiveCorrName) {
        this.receiveCorrName = receiveCorrName;
    }

    public String getOridId() {
        return oridId;
    }

    public void setOridId(String oridId) {
        this.oridId = oridId;
    }
}
