package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by  */

public class PayReques_isorNo {

    public String order_no;//订单号

    public String out_trade_no;
    public String openid;
    public String total_fee;
    public int  pay_result;
    public String body;
    public String device_info;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public int getPay_result() {
        return pay_result;
    }

    public void setPay_result(int pay_result) {
        this.pay_result = pay_result;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
