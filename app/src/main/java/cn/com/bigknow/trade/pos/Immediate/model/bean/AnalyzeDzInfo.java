package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by laixy
 */
public class AnalyzeDzInfo implements Serializable {
    /***
     * {type	统计类别，S-总计，P-刷卡，C-现金
     orderNum	订单数合计
     orderQty	订单菜品重量合计
     orderAmt	订单实付金额合计}
     */

    private String type;
    private int orderNum;
    private double orderQty;
    private double orderAmt;



    public AnalyzeDzInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    public double getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(double orderAmt) {
        this.orderAmt = orderAmt;
    }
}
