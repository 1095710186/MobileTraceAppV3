package cn.com.bigknow.trade.pos.Immediate.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * 订单
 *
 * id	订单ID
 billNo	订单编号
 corrName	买家手机号
 corrInfo	买家信息
 kinds	品种数
 sumQty	总重量
 sumAmt	订单金额
 payAmt	应付金额
 billDate	订单日期

 createTime	创建时间
 state	订单状态
 balState	支付状态
 payment	支付类型 P-刷卡，B-联名卡，C-现金
 transTime	交易时间
 */
public class BillDetailbean implements Serializable {

    static final long serialVersionUID = 42L;
    private String id;//订单ID
    private String billNo;//订单编号
    private String corrName;//	买家手机号;
    private String corrInfo;//	买家信息;
    private String corrId;
    private int kinds;//种类
    private float sumQty;//菜品总重
    private float sumAmt; //订单合计
    private float payAmt; //实际应付
    private float balAmt; //实际应付
    private String billDate; //订单日期,

    private String createDate;//	创建时间
    private String state;  //订单状态,
    private String balState;

    private String payment;//	支付类型 P-刷卡，B-联名卡，C-现金
    private String transTime;//	交易时间
    private List<BillFoodInfo> mvpOrderDet;
    private String responseId;// 流水

    public float getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(float balAmt) {
        this.balAmt = balAmt;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public BillDetailbean() {
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(String corrId) {
        this.corrId = corrId;
    }

    public List<BillFoodInfo> getMvpOrderDet() {
        return mvpOrderDet;
    }

    public void setMvpOrderDet(List<BillFoodInfo> mvpOrderDet) {
        this.mvpOrderDet = mvpOrderDet;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillNo() {
        return this.billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return this.billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public float getSumAmt() {
        return this.sumAmt;
    }

    public void setSumAmt(float sumAmt) {
        this.sumAmt = sumAmt;
    }

    public float getPayAmt() {
        return this.payAmt;
    }

    public void setPayAmt(float payAmt) {
        this.payAmt = payAmt;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCorrName() {
        return corrName;
    }

    public void setCorrName(String corrName) {
        this.corrName = corrName;
    }

    public String getCorrInfo() {
        return corrInfo;
    }

    public void setCorrInfo(String corrInfo) {
        this.corrInfo = corrInfo;
    }

    public int getKinds() {
        return kinds;
    }

    public void setKinds(int kinds) {
        this.kinds = kinds;
    }

    public float getSumQty() {
        return sumQty;
    }

    public void setSumQty(float sumQty) {
        this.sumQty = sumQty;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBalState() {
        return balState;
    }

    public void setBalState(String balState) {
        this.balState = balState;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
}
