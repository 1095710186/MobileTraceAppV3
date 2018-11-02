package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by laixy
 */
public class DzInfo implements Serializable {
    /***
     * {payChanner	统计类别，"" -总计，P,B-刷卡，C-现金
     type	t今天 ；w周； m;月    cm 选择月
     orderQty	订单菜品重量合计
     orderAmt	订单实付金额合计}
     */

    private String payChanner;
    private String type; //t  w  m，cm
    private String date; //t  w  m，cm
    private String startBillDate;
    private String endBillDate;
    private String balState;//Y



    public DzInfo() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayChanner() {
        return payChanner;
    }

    public void setPayChanner(String payChanner) {
        this.payChanner = payChanner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartBillDate() {
        return startBillDate;
    }

    public void setStartBillDate(String startBillDate) {
        this.startBillDate = startBillDate;
    }


    public String getEndBillDate() {
        return endBillDate;
    }

    public void setEndBillDate(String endBillDate) {
        this.endBillDate = endBillDate;
    }

    public String getBalState() {
        return balState;
    }

    public void setBalState(String balState) {
        this.balState = balState;
    }
}
