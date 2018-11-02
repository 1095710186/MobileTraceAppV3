package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单laixiy
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
 payChanner	支付方式
 payDate	交易时间
 */
public class AnalyzeDzBillinfo implements Serializable {
    private String id;//订单ID
    private String billNo;//订单编号
    private String corrId; //买方ID
    private String corrName; // 买方账号，手机号
    private String corrInfo;//买方描述,

    private String kinds;//  品种数
    private float sumQty; //总重量

    private float sumAmt; ////订单金额
    private float payAmt; //实际应付
    private float balAmt; //实际应付
    private String state;  //状态,

    private String balState; // //结算状态
    private String billDate; // //": 订单日期
    private String createTime;//: //创建时间

    private String payChanner;
    private String payDate;

    public float getBalAmt() {
        return balAmt;
    }

    public void setBalAmt(float balAmt) {
        this.balAmt = balAmt;
    }

    public String getPayChanner() {
        return payChanner;
    }

    public void setPayChanner(String payChanner) {
        this.payChanner = payChanner;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getCorrInfo() {
        return corrInfo;
    }

    public void setCorrInfo(String corrInfo) {
        this.corrInfo = corrInfo;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }

    public float getSumQty() {
        return sumQty;
    }

    public void setSumQty(float sumQty) {
        this.sumQty = sumQty;
    }

    public float getSumAmt() {
        return sumAmt;
    }

    public void setSumAmt(float sumAmt) {
        this.sumAmt = sumAmt;
    }

    public float getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(float payAmt) {
        this.payAmt = payAmt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBalState() {
        return balState;
    }

    public void setBalState(String balState) {
        this.balState = balState;
    }
}
