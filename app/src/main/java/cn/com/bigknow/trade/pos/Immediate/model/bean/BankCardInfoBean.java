package cn.com.bigknow.trade.pos.Immediate.model.bean;

import java.io.Serializable;

/**
 * Created by hujie on 2016/9/5 10:17
 */
public class BankCardInfoBean implements Serializable {
    private String iyear;//": null,
    private String cardType;//": "DEPOSIT",卡类型
    private String remark;//": null,
    private String bankName;//": "工商银行",银行名称
    private String microLimitAmt;//": null,
    private String cardNo;//": "613022527800116456",卡号
    private String dayLimitAmt;//": null,
    private String certNo;//": null,
    private String bankId;//": "JLGADSZH08HS971IYYY1S91BQ52PM5NV",银行ID
    private String merchantId;//": "297ebe0e560b972901560c1338be0009",
    private String defFlag;//": "Y",是否默认卡
    private String imonth;//": null,
    private String id;//": "D40OO4GS2J8Y4C0NGTI4S5LKVJ0GXSLH",银行卡ID
    private String microPaymentFlag;//": "N"
    private String bankCode;//银行编码

    public String getIyear() {
        return iyear;
    }

    public void setIyear(String iyear) {
        this.iyear = iyear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMicroLimitAmt() {
        return microLimitAmt;
    }

    public void setMicroLimitAmt(String microLimitAmt) {
        this.microLimitAmt = microLimitAmt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getCardNoStr() {
        return cardNo.substring(0, 4) + "**** ****" + cardNo.substring(cardNo.length() - 4);
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDayLimitAmt() {
        return dayLimitAmt;
    }

    public void setDayLimitAmt(String dayLimitAmt) {
        this.dayLimitAmt = dayLimitAmt;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getDefFlag() {
        return defFlag;
    }

    public void setDefFlag(String defFlag) {
        this.defFlag = defFlag;
    }

    public String getImonth() {
        return imonth;
    }

    public void setImonth(String imonth) {
        this.imonth = imonth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMicroPaymentFlag() {
        return microPaymentFlag;
    }

    public void setMicroPaymentFlag(String microPaymentFlag) {
        this.microPaymentFlag = microPaymentFlag;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
