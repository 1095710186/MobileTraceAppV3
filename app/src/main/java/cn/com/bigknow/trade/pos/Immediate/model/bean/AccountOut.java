package cn.com.bigknow.trade.pos.Immediate.model.bean;

/**
 * Created by hujie on 2016/9/6..
 */


public class AccountOut {

    /**
     * 商户ID
     */
    public String merchant_id;
    /**
     * [必需]交易类型
     */
    public String transType;
    /**
     * 交易日期
     */
    public String transDate;
    /**
     * [必需]交易金额
     */
    public String amt;

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
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

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}
